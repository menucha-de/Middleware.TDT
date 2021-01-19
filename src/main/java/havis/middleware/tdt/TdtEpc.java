package havis.middleware.tdt;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TdtEpc {

	// Holds the Definitions for all epc-formats read in at start up
	private TdtDefinitions tdtDefinitions;

	// epc field read from tag
	// epc field like received from input
	private String epcIdentifier;

	private String epcBinaryData;

	// extra parameters for decoding and encoding
	private String parameterList;

	// Fields extracted from input, from extra parameters or calculated from
	// rules
	private TdtFields fields;

	// Matching scheme for the input Data
	// Matching level for the input data
	private LevelX inputLevel;

	// Matching option for the input data
	private OptionX inputOption;

	private String uriId;

	private String uriTag;

	private String uriBinary;

	private String uriLegacy;

	private String uriLegacyAlt;

	private String uriLegacyAi;

	private String uriRawHexField;

	private String uriOnsHostname;

	private String uriRawDecimalField;

	private boolean isEpcGlobal;

	private String uriRawHexNotEpcGlobal;

	public String statusText;

	private byte[] epcData;

	/**
	 * Constructor
	 */
	TdtEpc(TdtDefinitions tdtDefinitions) {
		this.tdtDefinitions = tdtDefinitions;
		this.fields = new TdtFields();
	}

	String getEpcBinaryData() {
		return epcBinaryData;
	}

	boolean hasAdditionalBinaryData() {
		return epcIdentifier != null && epcBinaryData != null && epcIdentifier.length() < epcBinaryData.length();
	}

	TdtFields getTdtFields() {
		return fields;
	}

	/**
	 * URI for pure identity type (like
	 * "urn:epc:id:sgtin:123456785.9013.547365")
	 */
	String getUriId() throws TdtTranslationException {
		if (uriId == null)
			uriId = encodeUriId();
		return uriId;
	}

	/**
	 * URI for the tag encoding type (like
	 * "urn:epc:tag:grai-96:1.00000050.2437.547365")
	 */
	String getUriTag() throws TdtTranslationException {
		if (uriTag == null)
			uriTag = encodeUriTag();
		return uriTag;
	}

	/**
	 * Binary encoding i.e.
	 * "00111010011101000000010010000001000100001100010000011100000000000000000000000000001100010011101010001110010100011"
	 */
	String getUriBinary() throws TdtTranslationException {
		if (uriBinary == null)
			uriBinary = encodeUriBinary();
		return uriBinary;
	}

	/**
	 * URI for the legacy encoding type i.e. "gdti=0073796251024651842211"
	 */
	String getUriLegacy() throws TdtTranslationException {
		if (uriLegacy == null)
			uriLegacy = encodeUriLegacy();
		return uriLegacy;
	}

	/**
	 * URI for the alt legacy encoding type i.e. "grai13=0073796251024651842211"
	 */
	String getUriLegacyAlt() throws TdtTranslationException {
		if (uriLegacyAlt == null)
			uriLegacyAlt = encodeUriLegacyAlt();
		return uriLegacyAlt;
	}

	/**
	 * URI for the legacy ai (application identifier) encoding type i.e.
	 * "(253)0073796251024651842211"
	 */
	String getUriLegacyAi() throws TdtTranslationException {
		if (uriLegacyAi == null)
			uriLegacyAi = encodeUriLegacyAi();
		return uriLegacyAi;
	}

	/**
	 * URI for the ONS hostname encoding type (only gstin) i.e.
	 * "025102.0073796.sgtin.id.onsepc.com"
	 */
	String getUriOnsHostname() throws TdtTranslationException {
		if (uriOnsHostname == null)
			uriOnsHostname = encodeUriOnsHostname();
		return uriOnsHostname;
	}

	/**
	 * URI for the raw hex encoding type i.e.
	 * "urn:epc:raw:96.x3330000019000c5000085a25"
	 */
	String getUriRawHex() throws TdtTranslationException {
		if (uriRawHexField == null)
			uriRawHexField = encodeUriRawHex();
		return uriRawHexField;
	}

	/**
	 * URI for the raw decimal encoding type i.e.
	 * "urn:epc:raw:96.514800002500128000089037"
	 */
	String getUriRawDecimal() throws TdtTranslationException {
		if (uriRawDecimalField == null)
			uriRawDecimalField = encodeUriRawDecimal();
		return uriRawDecimalField;
	}

	/**
	 * URI for the raw hex for epc not epc global
	 */
	String getUriRawHexNotEpcGlobal() {
		if (uriRawHexNotEpcGlobal == null)
			uriRawHexNotEpcGlobal = encodeUriRawHexNotEpcGlobal();
		return uriRawHexNotEpcGlobal;
	}

	/**
	 * Length information of the epc as int
	 */
	int getLength() {
		return inputLevel.getScheme().getIntTagLength();
	}

	boolean isEpcGlobal() {
		return isEpcGlobal;
	}

	String getStatusText() {
		return statusText;
	}

	byte[] getEpcData() throws TdtTranslationException {
		if (epcData == null)
			epcData = fillEpcData();
		return epcData;
	}

	/**
	 * Decodes the EPC Field from Binary String or URN
	 *
	 * @param epcIdentifier
	 *            urn string for decoding
	 * @param parameterList
	 *            optional additional parameters needed for decoding
	 */
	void decode(String epcIdentifier, String parameterList) throws TdtTranslationException {
		isEpcGlobal = true;
		this.epcIdentifier = epcIdentifier;
		this.epcBinaryData = null;
		this.parameterList = parameterList;
		decodeParameterList();
		decodeEpcIdentifier();
		if (isEpcGlobal) {
			runRules(inputLevel, ModeList.EXTRACT);
		} else {
			copyEpcIdentifierToEpcData();
		}
	}

	/**
	 * Decodes EPC Field from byte array
	 *
	 * @param epcData
	 *            EPC field from transponder
	 */
	void decode(byte[] epcData) {
		isEpcGlobal = false;
		this.epcData = epcData;
		try {
			epcBinaryData = makeBinaryString(epcData);
			epcIdentifier = epcBinaryData;
			inputLevel = tdtDefinitions.getLevelForBuffer(epcData);
			if (inputLevel == null) {
				statusText = "Could not find encoding scheme";
				// CopyEpcIdentifierToEpcData();
				return;
			}
			if (epcIdentifier.length() < inputLevel.getScheme().getIntTagLength()) {
				// This message for compatibility with standard decoding
				statusText = "Could not find encoding scheme";
				return;
			}
			fields.tryAdd("taglength", Integer.toString(inputLevel.getScheme().getIntTagLength()));
			if (epcIdentifier.length() > inputLevel.getScheme().getIntTagLength()) {
				epcIdentifier = epcIdentifier.substring(0, inputLevel.getScheme().getIntTagLength());
			}
			inputOption = inputLevel.getEncodingOption(epcIdentifier);
			if (inputOption == null) {
				statusText = "Could not find encoding scheme";
				return;
			}
			// Read option tag encoding for finding the formatting options
			OptionX optionTagEncoding = inputLevel.getScheme().getLevel(LevelTypeList.TAG_ENCODING).getOptionByOptionKey(inputOption.getOptionKey());
			for (FieldX field : inputOption.getFields()) {
				String result = TdtCompactionMethods.decompactItem(epcIdentifier, field);
				fields.addOrReplace(field.getName(), padField(field, optionTagEncoding, result));
			}
			runRules(inputLevel, ModeList.EXTRACT);
		} catch (TdtTranslationException tdtEx) {
			statusText = tdtEx.getMessage();
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
			statusText = "Exception Type " + ex.getClass().getName();
			return;
		}
		isEpcGlobal = true;
	}

	/**
	 * Build the TdtEpc object when all values given in parameter list
	 *
	 * @param parameterList
	 *            semicolon separated list of parametername=value pairs i.e.
	 *            "type=GRAI-96;gs1companyprefixlength=8;filter=1;gs1companyprefix=0050;assettype=2437;serial=547365;"
	 */
	void decodeParam(String parameterList) throws TdtTranslationException {
		isEpcGlobal = false;
		HashMap<String, String> pams = new HashMap<String, String>();
		for (String parameter : parameterList.split(";")) {
			if (parameter.contains("=")) {
				//TODO: Checking for keyValue length = 2 or throwing Exception
				String[] keyValue = parameter.split("=");
				if (keyValue.length != 2){
					throw new TdtTranslationException("Value for parameter '" + keyValue[0]
							+ "' is missing in the parameter list");
				}
				pams.put(keyValue[0], keyValue[1]);
			}
		}
		// Find requested scheme
		String epcTypeName;
		epcTypeName = pams.get("type");
		if (epcTypeName == null) {
			throw new TdtTranslationException(
					"Parameter 'type' missing in parameter list! Only accepting parameter list, e.g. type=GRAI-96;gs1companyprefixlength=8;filter=1;gs1companyprefix=0050;assettype=2437;serial=547365;");
		}
		SchemeX scheme = tdtDefinitions.getSchemeByName(epcTypeName);
		if (scheme == null) {
			throw new TdtTranslationException("Scheme '" + epcTypeName
					+ "' not found");
		}
		LevelX level = scheme.getLevel(LevelTypeList.TAG_ENCODING);
		String optionKey;
		optionKey = pams.get("optionKey");
		if (optionKey == null) {
			optionKey = pams.get(scheme.getOptionKey());
		}
		// Find matching option
		OptionX option = null;
		if (optionKey == null || optionKey.isEmpty()) {
			// No option key supplied as parameter, so find the first matching
			// option
			for (OptionX loopOption : level.getOptions()) {
				boolean match = true;
				for (FieldX loopField : loopOption.getFields()) {
					if (!pams.containsKey(loopField.getName())) {
						throw new TdtTranslationException("Parameter '"
								+ loopField.getName() + "' is missing");
					}
					match = loopField.valueCheckMatch(pams.get(loopField
							.getName()));
					if (!match) {
						break;
					}
				}
				if (!match)
					continue;
				// All fields are matching, so we find the first matching option
				option = loopOption;
				break;
			}
		} else {
			// Option key supplied
			option = level.getOptionByOptionKey(optionKey);
		}
		if (option == null) {
			throw new TdtTranslationException(
					"No matching option found for parameter list");
		}
		// Construct the URN for the parameter list
		StringBuilder urn = new StringBuilder();
		for (String fieldname : option.getGrammar().split(" ")) {
			if (fieldname.startsWith("'")) {
				urn.append(fieldname.replace("'", ""));
			} else {
				FieldX field = option.getField(fieldname);
				String fieldvalue = pams.get(fieldname);
				if (field.getPadDir() != null
						&& (fieldvalue.length() < field.getIntLength())) {
					fieldvalue = (field.getPadDir() == PadDirectionList.LEFT)
							? Utils.padLeft(fieldvalue, field.getIntLength(), field.getPadChar())
							: Utils.padRight(fieldvalue, field.getIntLength(), field.getPadChar());
				}
				urn.append(fieldvalue);
			}
		}
		// Initialize the TDTTagInfo from the URN
		decode(urn.toString(), "");
		isEpcGlobal = true;
	}

	/**
	 * Decode the list of additional parameters
	 */
	private void decodeParameterList() throws TdtTranslationException {
		if ((parameterList != null) && !parameterList.isEmpty()) {
			for (String parameter : parameterList.split(";")) {
				String[] pars = parameter.split("=");
				if (fields.containsKey(pars[0]) == false) {
					if (pars.length != 2) {
						throw new TdtTranslationException(
								"Invalid value in parameterlist");
					}
					fields.add(pars[0], pars[1]);
				}
			}
		}
	}

	private void decodeEpcIdentifier() throws TdtTranslationException {
		findEncoding();
		if (isEpcGlobal) {
			switch (inputLevel.getType()) {
			case BINARY:
				decodeEpcIdentifierBinary();
				break;
			case TAG_ENCODING:
				fields.tryAdd("taglength", Integer.toString(inputLevel
						.getScheme().getIntTagLength()));
				decodeEpcIdentifierUrn();
				break;
			case PURE_IDENTITY:
				decodeEpcIdentifierUrn();
				break;
			case LEGACY:
				decodeEpcIdentifierLegacy();
				break;
			case LEGACY_AI:
				decodeEpcIdentifierLegacy();
				break;
			case LEGACY_ALT:
				decodeEpcIdentifierLegacy();
				break;
			case ONS_HOSTNAME:
				throw new TdtTranslationException(
						"Level Type ONS_HOSTNAME cannot be decoded");
			default:
				throw new TdtTranslationException("Unknown LevelType");
			}
		} else {
			copyEpcIdentifierToEpcData();
		}
	}

	private void findEncoding() throws TdtTranslationException {
		TdtField fieldTagLength = fields.getField("taglength");
		int taglength = (fieldTagLength != null) ? Integer
				.parseInt(fieldTagLength.getValue()) : 0;
		// Find matching Input Option
		inputOption = tdtDefinitions
				.getEncodingOption(epcIdentifier, taglength);
		if (inputOption == null) {
			// If _epcIdentifier contains only 0 and 1 this can result from a
			// non EPC-global encoded tag
			if (Pattern.matches("^[01]*$", epcIdentifier)) {
				isEpcGlobal = false;
				return;
			}
			throw new TdtTranslationException("Could not find encoding scheme");
		}
		// Check if all required parsing parameters are supplied
		String parameters = inputOption.getLevel()
				.getRequiredParsingParameters();
		if ((parameters != null) && !parameters.isEmpty()) {
			for (String param : parameters.split(",")) {
				TdtField field = fields.getField(param);
				if (field == null) {
					throw new TdtTranslationException(
							"required parsing parameter '" + param
									+ "' missing");
				}
			}
		}
		// For LEGACY Input option Key must be supplied as input parameter
		TdtField fieldOptionKey = fields.getField(inputOption.getLevel()
				.getScheme().getOptionKey());
		if (fieldOptionKey != null &&
		        ((inputOption.getOptionKey() == null && fieldOptionKey.getValue() != null) ||
		          inputOption.getOptionKey() != null && !inputOption.getOptionKey().equals(fieldOptionKey.getValue()))) {
			// Replace input option
			inputOption = inputOption.getLevel().getOptionByOptionKey(
					fieldOptionKey.getValue());
		}
		inputLevel = inputOption.getLevel();
	}

	private void decodeEpcIdentifierBinary() {
		try {
			copyEpcIdentifierToEpcData();
			OptionX optionTagEncoding = inputLevel.getScheme().getOption(
					LevelTypeList.TAG_ENCODING, inputOption.getOptionKey());
			fields.tryAdd("taglength",
					Integer.toString(epcIdentifier.length()));
			for (FieldX field : inputOption.getFields()) {
				String result = TdtCompactionMethods.decompactItem(
						epcIdentifier, field);
				fields.addOrReplace(field.getName(),
						padField(field, optionTagEncoding, result));
			}
		} catch (Exception e) {
			isEpcGlobal = false;
			statusText = "";
		}
	}

	private void copyEpcIdentifierToEpcData() {
		int len = epcIdentifier.length() / 8
				+ (((epcIdentifier.length() % 8) != 0) ? 1 : 0);
		epcData = new byte[len];
		String s = Utils.padRight(epcIdentifier, len * 8, '0');

		for (int i = 0; i < len; i++) {
			String sub = s.substring(i * 8, i * 8 + 8);
			epcData[i] = (byte) Short.parseShort(sub, 2);
		}
	}

	private void decodeEpcIdentifierUrn() throws TdtTranslationException {
		// String[] values = _inputOption.PatternRegex.split(_epcIdentifier);
		Matcher matcher = inputOption.getPattern().matcher(epcIdentifier);
		if (matcher.matches()) {
			for (FieldX field : inputOption.getFields()) {
				// String value = values[field.field.getSeq().shortValue()];
				String value = matcher.group(field.getSeq());
				// System.out.println("Group value: " + value);
				if (field.isNumeric() && value.length() > 0) {
					field.checkRange(value);
				}
				// Check for %-encoded characters
				if (value.contains("%")) {
					value = TdtTranslateCodeTable.decodeURIForm(value);
				}
				// TODO: ?? Padding for value ??
				fields.addOrReplace(field.getName(), value);
			}
		}
	}

	private void decodeEpcIdentifierLegacy() {
		Matcher matcher = inputOption.getPattern().matcher(epcIdentifier);
		if (matcher.matches()) {
			for (FieldX field : inputOption.getFields()) {
				// TODO:?? Padding for value, check range ??
				if (field.getSeq() <= matcher.groupCount())
					fields.addOrReplace(field.getName(),
							matcher.group(field.getSeq()));
			}
		}
	}

	private String encodeUriTag() throws TdtTranslationException {
		return encodeUri(LevelTypeList.TAG_ENCODING);
	}

	private String encodeUriId() throws TdtTranslationException {
		return encodeUri(LevelTypeList.PURE_IDENTITY);
	}

	private String encodeUriLegacy() throws TdtTranslationException {
		return encodeUri(LevelTypeList.LEGACY);
	}

	private String encodeUriLegacyAlt() throws TdtTranslationException {
		return encodeUri(LevelTypeList.LEGACY_ALT);
	}

	private String encodeUriLegacyAi() throws TdtTranslationException {
		return encodeUri(LevelTypeList.LEGACY_AI);
	}

	private String encodeUriOnsHostname() throws TdtTranslationException {
		return encodeUri(LevelTypeList.ONS_HOSTNAME);
	}

	private String encodeUri(LevelTypeList levelType)
			throws TdtTranslationException {
		OptionX outputOption = findOuputOption(levelType);
		runRules(outputOption.getLevel(), ModeList.FORMAT);
		return encodeEpcIdentifierUrn(outputOption);
	}

	private String encodeEpcIdentifierUrn(OptionX option)
			throws TdtTranslationException {
		StringBuilder sb = new StringBuilder(128);
		for (String fieldname : option.getGrammar().split(" ")) {
			String value;
			if (fieldname.startsWith("'")) {
				value = fieldname.replace("'", "");
			} else {
				value = fields.getFieldValue(fieldname);
				if (value == null) {
					// throw new
					// TdtTranslationException(string.Format("Required parameter {0} is missing",fieldname));
				}
				FieldX field = option.getField(fieldname);
				if (field != null && value != null) {
					field.checkCharacterSet(value);
					if (field.getLength() != null) {
						if (value.length() > field.getIntLength()) {
							value = field.getIntLength() == 0 ? "" : value
									.substring(value.length()
											- field.getIntLength());
						}
					}

					if (value != null) {
						if (field.getPadDir() != null
								&& (value.length() < field.getIntLength())) {
							value = (field.getPadDir() == PadDirectionList.LEFT)
									? Utils.padLeft(value, field.getIntLength(), field.getPadChar())
									: Utils.padRight(value, field.getIntLength(), field.getPadChar());
						}
						// Check if there are any %encodings to be done
						if (!field.isNumeric()) {
							if (option.getLevel().getType() == LevelTypeList.ONS_HOSTNAME
									|| option.getLevel().getType() == LevelTypeList.PURE_IDENTITY
									|| option.getLevel().getType() == LevelTypeList.TAG_ENCODING) {
								value = TdtTranslateCodeTable
										.encodeURIForm(value);
							}
						}
					}
				} else {
					// TODO:
				}
			}
			sb.append(value);
		}
		return sb.toString();
	}

	private String encodeUriRawHex() throws TdtTranslationException {
		return "urn:epc:raw:" + inputLevel.getScheme().getIntTagLength() + ".x"
				+ encodeUriPartRawHex();
	}

	private String encodeUriRawDecimal() throws TdtTranslationException {
		return "urn:epc:raw:" + inputLevel.getScheme().getIntTagLength() + "."
				+ encodeUriPartRawDecimal();
	}
	
    private String encodeUriPartRawHex() throws TdtTranslationException {
    	StringBuilder result = new StringBuilder();
        String binaryString = encodeUriPartRawBinary();
        for (int i = 0; i < binaryString.length(); i += 8) {
        	result.append(Utils.padLeft(Integer.toHexString(Short.parseShort(binaryString.substring(i, i + 8), 2)), 2, '0'));
        }
        return result.toString().toUpperCase();
    }

	private String encodeUriPartRawDecimal() throws TdtTranslationException {
		String binaryString = encodeUriPartRawBinary();
		return new BigInteger(binaryString, 2).toString();
	}

    private String encodeUriPartRawBinary() throws TdtTranslationException {
        String binaryString = getUriBinary();
		if ((binaryString.length() % 8) != 0) {
			binaryString = Utils.padRight(binaryString, (binaryString.length() / 8) * 8 + 8, '0');
		}
        return binaryString;
    }

	private OptionX findOuputOption(LevelTypeList levelType)
			throws TdtTranslationException {
		LevelX level = inputLevel.getScheme().getLevel(levelType);
		if (level == null) {
			throw new TdtTranslationException("Could not find level type '"
					+ levelType + "' for scheme '"
					+ inputLevel.getScheme().getId() + "'");
		}
		// Output option must have the same option key like input option key
		OptionX option = level.getOptionByOptionKey(inputOption.getOptionKey());
		if (option == null) {
			throw new TdtTranslationException(
					"Could not find output option for level type '" + levelType
							+ "' for scheme '" + inputLevel.getScheme().getId()
							+ "'");
		}
		return option;
	}

	private String encodeUriBinary() throws TdtTranslationException {
		OptionX outputOption = findOuputOption(LevelTypeList.BINARY);
		runRules(outputOption.getLevel(), ModeList.FORMAT);
		checkRequiredFormattingParameters(outputOption.getLevel());
		StringBuilder sb = new StringBuilder(inputLevel.getScheme()
				.getIntTagLength());
		for (String fieldname : outputOption.getGrammar().split(" ")) {
			if (fieldname.startsWith("'")) {
				sb.append(fieldname.replace("'", ""));
			} else {
				String value = fields.getFieldValue(fieldname);
				if (value == null) {
					throw new TdtTranslationException(
							"Value not found, fieldname='" + fieldname + "'");
				}
				FieldX field = outputOption.getField(fieldname);
				String result = TdtCompactionMethods.compactItem(value, field);
				sb.append(result);
			}
		}
		return sb.toString();
	}

	private void checkRequiredFormattingParameters(LevelX outputLevel)
			throws TdtTranslationException {
		if (outputLevel.getRequiredFormattingParameters() != null) {
			for (String parameterName : outputLevel
					.getRequiredFormattingParameters().split(",")) {
				if (!fields.containsKey(parameterName)) {
					throw new TdtTranslationException(
							"required formatting parameter '" + parameterName
									+ "' missing");
				}
			}
		}
	}

	public byte[] fillEpcData() throws TdtTranslationException {
		String uriBinary = getUriBinary();
		int length = uriBinary.length();
		byte[] EpcData = new byte[(length + (16 - length % 16) % 16) / 8];
		int n = 0;
		for (int i = 0; i < length; i += 8) {
			int l = ((length - i) < 8 ? (length - i) : 8);
			String substr = Utils.padRight(uriBinary.substring(i, i + l), 8, '0');
			EpcData[n++] = (byte) Short.parseShort(substr, 2);
		}

		return EpcData;
	}

	public String encodeUriRawHexNotEpcGlobal() {
		// "urn:epc:raw:" + _inputLevel.MyScheme.tagLength + ".x" +
		// EncodeUriRaw();
		StringBuilder urn = new StringBuilder();
		if (epcData != null) {
			urn.append("urn:epc:raw:").append(epcData.length * 8).append(".x");
			for (short b : epcData) {
				//TODO: Uppercase?
				urn.append(Utils.padLeft(Integer.toHexString(b), 2, '0'));
			}
		}
		return urn.toString();
	}

	private static String makeBinaryString(byte[] byteArray) {
		StringBuilder sb = new StringBuilder(byteArray.length * 8);
		for (short b : byteArray) {
			sb.append(Utils.padLeft(Integer.toBinaryString(b & 0xFF), 8, '0'));
		}
		return sb.toString();
	}

	private static String padField(FieldX field, OptionX optionTagEncoding,
			String result) {
		FieldX fieldTagEncoding = optionTagEncoding.getField(field.getName());
		if ((fieldTagEncoding != null)
				&& (fieldTagEncoding.getPadDir() != null)
				&& (result.length() < fieldTagEncoding.getIntLength())) {
			result = (fieldTagEncoding.getPadDir() == PadDirectionList.LEFT)
					? Utils.padLeft(result, fieldTagEncoding.getIntLength(), fieldTagEncoding.getPadChar())
					: Utils.padRight(result, fieldTagEncoding.getIntLength(), fieldTagEncoding.getPadChar());
		}
		return result;
	}

	private void runRules(LevelX level, ModeList mode)
			throws TdtTranslationException {
		if (level.getRuleList() != null) {
			for (RuleX rule : level.getRules()) {
				if (rule.getType() == mode) {
					if (!fields.containsKey(rule.getNewFieldName())) {
						fields.add(rule.getNewFieldName(),
								rule.getValue(fields));
					}
				}
			}
		}
	}
}