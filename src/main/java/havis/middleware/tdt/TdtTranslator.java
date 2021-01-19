package havis.middleware.tdt;

/**
 * The Tag Data Translator (TDT) as specified in the EPCglobal Documents -
 * EPCglobal Tag Data Translation (TDT) 1.4, June 10, 2009 - EPCglobal Tag Data
 * Standard (TDS) Version 1.5, August 18, 2010
 * 
 * {@code
 * 
 * }
 */
public class TdtTranslator {

	enum Format {
		BINARY, TAG_ENCODING, PURE_IDENTITY, LEGACY, LEGACY_ALT, LEGACY_AI, ONS_HOSTNAME
	}

	private TdtDefinitions tdtDefinitions;

	/**
	 * Constructor
	 */
	public TdtTranslator() {
		tdtDefinitions = new TdtDefinitions();
	}

	public TdtDefinitions getTdtDefinitions() {
		return tdtDefinitions;
	}

	/**
	 * Translates epcIdentifier frome one representation into another within the
	 * same coding scheme (Standard method for client API as specified in
	 * Document EPCglobal Tag Data Translation (TDT) 1.4, June 10, 2009 chapter
	 * 6.1).
	 * 
	 * @param epcIdentifier
	 *            The epcIdentifier to be converted. This should be expressed as
	 *            a string, in accordance with one of the grammars or patterns
	 *            in the TDT markup files, i.e. a binary string consisting of
	 *            characters '0' and '1', a IURI (either tag encoding or pure
	 *            binary formats), or a serialized leacy code.
	 * @param parameterList
	 *            This is a parameter string containing key value pairs, using
	 *            the semicolon [";"] as delimet between key=value pairs.
	 * @param outputFormat
	 *            The output format, into which the epcIdentifier shall be
	 *            converted.The following are the formats converted : BINARY |
	 *            LEGACY | LEGACY_AI | TAG_ENCODING | PURE_IDENTITY |
	 *            ONS_HOSTNAME
	 * @return The converted value into the requested format
	 * @throws TdtTranslationException
	 */
	public String translate(String epcIdentifier, String parameterList,
			String outputFormat) throws TdtTranslationException {
		TdtTagInfo tagInfo = new TdtTagInfo(tdtDefinitions);
		tagInfo.decode(epcIdentifier, parameterList);
		switch (Format.valueOf(outputFormat)) {
		case BINARY:
			return tagInfo.getUriBinary();
		case TAG_ENCODING:
			return tagInfo.getUriTag();
		case PURE_IDENTITY:
			return tagInfo.getUriId();
		case LEGACY:
			return tagInfo.getUriLegacy();
		case LEGACY_ALT:
			return tagInfo.getUriLegacyAlt();
		case LEGACY_AI:
			return tagInfo.getUriLegacyAi();
		case ONS_HOSTNAME:
			return tagInfo.getUriOnsHostname();
		}
		throw new TdtTranslationException("Invalid output format requested "
				+ outputFormat);
	}

	public TdtTagInfo translate(byte[] epcData) throws TdtTranslationException {
		return translate(epcData, null, null, null, null);
	}

	// Translate with user memory
	public TdtTagInfo translate(byte[] epcData, byte[] pc, byte[] xpc,
			byte[] tid, byte[] userMemory) throws TdtTranslationException {
		TdtTagInfo tagInfo = new TdtTagInfo(tdtDefinitions);
		tagInfo.decode(epcData, pc, xpc, tid, userMemory);
		return tagInfo;
	}

	public TdtTagInfo translate(String epcIdentifier)
			throws TdtTranslationException {
		TdtTagInfo tagInfo = new TdtTagInfo(tdtDefinitions);
		tagInfo.decode(epcIdentifier, "");
		return tagInfo;
	}

	public TdtTagInfo translateParam(String paramameterList)
			throws TdtTranslationException {
		TdtTagInfo tagInfo = new TdtTagInfo(tdtDefinitions);
		tagInfo.decodeParam(paramameterList);
		return tagInfo;
	}

	public void refreshTranslations() throws TdtTranslationException {
		throw new TdtTranslationException(
				"Method not implemented : refreshTranslations");
	}
}