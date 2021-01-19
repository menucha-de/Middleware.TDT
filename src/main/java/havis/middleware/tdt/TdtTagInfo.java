package havis.middleware.tdt;

import java.math.BigInteger;

public class TdtTagInfo {

	private boolean isEpcGlobal;

	private TdtDefinitions tdtDefinitions;

	private TdtPc tdtPc;

	private TdtEpc tdtEpc;

	private TdtXpc tdtXpc;

	private TdtTid tdtTid;

	private enum Format {
		BINARY, HEX, DECIMAL
	}

	/**
	 * Constructor
	 */
	public TdtTagInfo(TdtDefinitions tdtDefinitions) {
		this.tdtDefinitions = tdtDefinitions;
		isEpcGlobal = false;
	}

	public boolean isEpcGlobal() {
		return isEpcGlobal;
	}

	TdtFields getFields() {
		return isEpcGlobal ? tdtEpc.getTdtFields() : null;
	}

	public void setEpcGlobal(boolean isEpcGlobal) {
		this.isEpcGlobal = isEpcGlobal;
	}

	/**
	 * Binary encoding i.e.
	 * "00111010011101000000010010000001000100001100010000011100000000000000000000000000001100010011101010001110010100011"
	 */
	public String getUriBinary() throws TdtTranslationException {
		return isEpcGlobal ? tdtEpc.getUriBinary() : getRawUri(Format.BINARY);
	}

	/**
	 * URI for pure identity type i.e. "urn:epc:id:sgtin:123456785.9013.547365"
	 */
	public String getUriId() throws TdtTranslationException {
		return isEpcGlobal ? tdtEpc.getUriId() : getRawUri(Format.HEX);
	}

	/**
	 * URI for the tag encoding type i.e.
	 * "urn:epc:tag:grai-96:1.00000050.2437.547365"
	 */
	public String getUriTag() throws TdtTranslationException {
		return isEpcGlobal ? tdtEpc.getUriTag() : getRawUri(Format.HEX);
	}

	/**
	 * URI for the legacy encoding type i.e. "gdti=0073796251024651842211"
	 *
	 * @throws TdtTranslationException
	 */
	public String getUriLegacy() throws TdtTranslationException {
		return isEpcGlobal ? tdtEpc.getUriLegacy() : getRawUri(Format.HEX);
	}

	/**
	 * URI for the alt legacy encoding type i.e. "grai13=0073796251024651842211"
	 */
	public String getUriLegacyAlt() throws TdtTranslationException {
		return isEpcGlobal ? tdtEpc.getUriLegacyAlt() : getRawUri(Format.HEX);
	}

	/**
	 * URI for the legacy ai (application identifier) encoding type i.e
	 * "(253)0073796251024651842211"
	 */
	public String getUriLegacyAi() throws TdtTranslationException {
		return isEpcGlobal ? tdtEpc.getUriLegacyAi() : getRawUri(Format.HEX);
	}

	/**
	 * URI for the ONS hostname encoding type (only gstin) i.e.
	 * "025102.0073796.sgtin.id.onsepc.com"
	 */
	public String getUriOnsHostname() throws TdtTranslationException {
		return isEpcGlobal ? tdtEpc.getUriOnsHostname() : getRawUri(Format.HEX);
	}

	/**
	 * URI for the raw hex encoding type i.e.
	 * "urn:epc:raw:96.x3330000019000c8000085a25"
	 */
	public String getUriRawHex() throws TdtTranslationException {
		return isEpcGlobal && !tdtEpc.hasAdditionalBinaryData() ? tdtEpc.getUriRawHex() : getRawUri(Format.HEX);
	}

	/**
	 * URI for the raw decimal encoding type i.e.
	 * "urn:epc:raw:96.3330000019000c8000085a25"
	 */
	public String getUriRawDecimal() throws TdtTranslationException {
		return isEpcGlobal && !tdtEpc.hasAdditionalBinaryData() ? tdtEpc.getUriRawDecimal() : getRawUri(Format.DECIMAL);
	}

	/**
	 * Length information of the URI in int
	 */
	public int getLength() {
		return isEpcGlobal ? tdtEpc.getLength() : tdtEpc.getEpcBinaryData().length();
	}

	public byte[] getEpcData() throws TdtTranslationException {
		return tdtEpc.getEpcData();
	}

	/**
	 * Decode only EPC-Field from byte array
	 */
	void decode(byte[] epcData) throws TdtTranslationException {
		decode(epcData, null, null, null, null);
	}

	/**
	 * Decode all memory banks of a transponder
	 */
	void decode(byte[] epcData, byte[] pcData, byte[] xpcData, byte[] tidData,
			byte[] userMemoryData) throws TdtTranslationException {
		tdtEpc = new TdtEpc(tdtDefinitions);
		if (epcData != null) {
			try {
				tdtEpc.decode(epcData);
				isEpcGlobal = tdtEpc.isEpcGlobal();
			} catch (Exception e) {
				isEpcGlobal = false;
			}
		}
		tdtPc = new TdtPc(pcData);
		// Extended Protocol Control Field
		tdtXpc = new TdtXpc();
		if (xpcData != null) {
			tdtXpc.decode(tdtPc, xpcData);
		}
		tdtTid = new TdtTid();
		if (tidData != null) {
			tdtTid.decode(tdtPc, tidData);
		}
	}

	/**
	 * Decode an urn string
	 *
	 * @param epcIdentifier
	 *            urn string
	 * @param parameterList
	 *            additional parameters
	 * @throws TdtTranslationException
	 */
	void decode(String epcIdentifier, String parameterList)
			throws TdtTranslationException {
		tdtEpc = new TdtEpc(tdtDefinitions);
		tdtEpc.decode(epcIdentifier, parameterList);
		isEpcGlobal = tdtEpc.isEpcGlobal();
	}

	/**
	 * Creates a TDTTagInfoObject with all parameters supplied as semicolon
	 * separated key-value pairs.
	 *
	 * @param parameterList
	 *            List of semicolon separated key value pairs. i.e.
	 *            "type=GRAI-96;gs1companyprefixlength=8;filter=1;gs1companyprefix=0050;assettype=2437;serial=547365;"
	 *            This list must at least contain one entry type=xxx with xxx
	 *            the name of the epc-specification (eg sgtin-96). If the
	 *            parameterlist contains a value-pair "optionKey=n" or a
	 *            value-pair for the optionKey-entry in the epc-specification,
	 *            the corresponding option will be used for formatting,
	 *            otherwise the first matching Option will be used.
	 * @throws TdtTranslationException
	 */
	void decodeParam(String parameterList) throws TdtTranslationException {
		tdtEpc = new TdtEpc(tdtDefinitions);
		tdtEpc.decodeParam(parameterList);
		isEpcGlobal = tdtEpc.isEpcGlobal();
	}

	/**
	 * Returns the URI if tag is not EPC-global encoded
	 *
	 * @return The non conform URI
	 */
	private String getRawUri(Format format) {
		String binaryData = tdtEpc.getEpcBinaryData();
		if (binaryData == null) {
			return "urn:epc:raw:0";
		}
		StringBuilder value = new StringBuilder().append("urn:epc:raw:").append(binaryData.length()).append('.');
		if (format == Format.HEX) {
			value.append('x');
		}

		// Length must be a full multiple of 8
		if ((binaryData.length() % 8) != 0) {
			binaryData = Utils.padRight(binaryData, (binaryData.length() / 8) * 8 + 8, '0');
		}

		if (format == Format.DECIMAL) {
			value.append(new BigInteger(binaryData, 2).toString());
		} else {
			for (int i = 0; i < binaryData.length(); i += 8) {
				if (format == Format.BINARY) {
					value.append(binaryData.substring(i, i + 8));
				} else if (format == Format.HEX) {
					value.append((Utils.padLeft(Integer.toHexString(Short.parseShort(binaryData.substring(i, i + 8), 2)), 2, '0')).toUpperCase());
				}
			}
		}
		return value.toString();
	}

	@Override
	public String toString() {
		try {
			return getUriTag();
		} catch (TdtTranslationException e) {
			return "urn:epc:raw:0";
		}
	}
}