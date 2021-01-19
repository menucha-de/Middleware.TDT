package havis.middleware.tdt;

import java.util.regex.Pattern;

/**
 * This class implements the Rule property of the EPCglobal Tag Data Translation
 * (TDT) Specification and the methods for calculationg the rules. (For more
 * information see Document EPCglobal Tag Data Translation (TDT) 1.4, Ratified
 * Standard, June 10,2009. Latest version 1.4., chapter 3).
 */
class RuleX {

	enum Function {
		SUBSTR, CONCAT, LENGTH, GS1CHECKSUM, TABLELOOKUP, ADD, MULTIPLY, DEVIDE, SUBSTRACT, MOD
	}

	private Rule rule;

	/**
	 * Reference to the Level object this rule belongs to
	 */
	private LevelX level;

	/**
	 * Regular expression for checking string is binary
	 */
	private static Pattern isBinaryString = Pattern.compile("^[0-1]*$");

	/**
	 * Regular expression for checking string is numeric characters only
	 */
	private static Pattern isNumericString = Pattern.compile("^[0-9]*$");

	/**
	 * Integer value to property length
	 */
	private int length;

	RuleX(Rule rule, LevelX level) {
		this.rule = rule;
		this.level = level;
		init();
	}

	/**
	 * Initialize property
	 */
	void init() {
		length = rule.getLength() == null ? 0 : Integer.parseInt(rule
				.getLength());
	}

	/**
	 * The unique ID of this rule
	 */
	String getId() {
		return level.getId() + "." + rule.getNewFieldName();
	}

	/**
	 * Calculate rule for one field
	 *
	 * @param fields
	 *            calculate value as string
	 */
	String getValue(TdtFields fields) throws TdtTranslationException {
		String value;
		String functionName;
		String[] parameters;
		String function = rule.getFunction();
		if (function.contains("(")) {
			int i = function.indexOf('(');
			functionName = function.substring(0, i);
			parameters = (function.substring(i + 1, function.indexOf(')')))
					.split(",");
		} else {
			throw new TdtTranslationException("Invalid rule " + getId());
		}
		switch (Function.valueOf(functionName.toUpperCase())) {
		case SUBSTR:
			value = callFunctionSubstr(fields, parameters);
			break;
		case CONCAT:
			value = callFunctionConcat(fields, parameters);
			break;
		case LENGTH:
			value = callFunctionLength();
			break;
		case GS1CHECKSUM:
			value = callFunctionGsChecksum(fields, parameters);
			break;
		case TABLELOOKUP:
			value = callFunctionTablelookup();
			break;
		case ADD:
			value = callFunctionAdd();
			break;
		case MULTIPLY:
			value = callFunctionMultiply();
			break;
		case DEVIDE:
			value = callFunctionDivide();
			break;
		case SUBSTRACT:
			value = callFunctionSubtract();
			break;
		case MOD:
			value = callFunctionMod();
			break;
		default:
			throw new TdtTranslationException("Unknown funktion '"
					+ functionName + "' in '" + getId() + "'");
		}
		if (rule.getBitPadDir() != null) {
			// Not used in TDT 1.4
		}
		if (length > value.length()) {
			StringBuilder sb = new StringBuilder();
			for (int i = value.length(); i < length; i++)
				sb.append('0');
			if (rule.getPadDir() == PadDirectionList.LEFT) {
				sb.append(value);
			} else {
				sb.insert(0, value);
			}
			value = sb.toString();
		}
		return value;
	}

	private String callFunctionSubstr(TdtFields fields, String[] parameters)
			throws TdtTranslationException {
		String value = getValueFromField(fields, parameters[0]);
		switch (parameters.length) {
		case 2: {
			value = value.substring(getIntFromString(fields, parameters[1]));
		}
			break;
		case 3: {
			int beginIndex = getIntFromString(fields, parameters[1]);
            int length = getIntFromString(fields, parameters[2]);
            value = value.substring(beginIndex, beginIndex + length);
		}
			break;
		default:
			throw new TdtTranslationException(
					"Invalid number of parameters for substr in option "
							+ getId());
		}
		return value;
	}

	private String callFunctionConcat(TdtFields fields, String[] parameters)
			throws TdtTranslationException {
		StringBuilder value = new StringBuilder();
		for (String fieldname : parameters) {
			if (isBinaryString.matcher(fieldname).matches()) {
				value.append(fieldname);
			} else {
				String par2 = getValueFromField(fields, fieldname);
				if (par2 != null) {
					value.append(par2);
				}
			}
		}
		return value.toString();
	}

	private String callFunctionLength() throws TdtTranslationException {
		// not used in TDT 1.4
		throw new TdtTranslationException(
				"Not Implemented : callFunctionLength in rule " + getId());
	}

	private static String callFunctionGsChecksum(TdtFields fields,
			String[] parameters) {
		String value = fields.getFieldValue(parameters[0]);
		// Calculate the Checksum see http://www.gs1.org/barcodes
		int factor = ((value.length() % 2) != 0) ? 3 : 1;
		int sum = 0;
		for (char c : value.toCharArray()) {
			sum += (c - '0') * factor;
			factor = (factor == 3) ? 1 : 3;
		}
		return Integer.valueOf(((sum % 10) == 0) ? 0 : sum + 10 - (sum % 10) - sum)
				.toString();
	}

	private String callFunctionTablelookup() throws TdtTranslationException {
		// not used in TDT 1.4
		throw new TdtTranslationException(
				"Not Implemented : callFunctionTablellookup in rule " + getId());
	}

	private String callFunctionAdd() throws TdtTranslationException {
		// not used in TDT 1.4
		throw new TdtTranslationException(
				"Not Implemented : callFunctionAdd in rule " + getId());
	}

	private String callFunctionMultiply() throws TdtTranslationException {
		// not used in TDT 1.4
		throw new TdtTranslationException(
				"Not Implemented : callFunctionMultiply in rule " + getId());
	}

	private String callFunctionDivide() throws TdtTranslationException {
		// not used in TDT 1.4
		throw new TdtTranslationException(
				"Not Implemented : callFunctionDivide in rule " + getId());
	}

	private String callFunctionSubtract() throws TdtTranslationException {
		// not used in TDT 1.4
		throw new TdtTranslationException(
				"Not Implemented : callFunctionSubtract in rule " + getId());
	}

	private String callFunctionMod() throws TdtTranslationException {
		// TODO
		throw new TdtTranslationException(
				"Not Implemented : callFunctionMod in rule " + getId());
	}

	private int getIntFromString(TdtFields fields, String parameter)
			throws NumberFormatException, TdtTranslationException {
		return Integer
				.parseInt(isNumericString.matcher(parameter).matches() ? parameter
						: getValueFromField(fields, parameter));
	}

	private String getValueFromField(TdtFields fields, String fieldname)
			throws TdtTranslationException {
		String value = fields.getFieldValue(fieldname);
		if (value == null) {
			throw new TdtTranslationException("Unknown fieldname '" + fieldname
					+ "' in rule " + getId());
		}
		return value;
	}

	ModeList getType() {
		return rule.getType();
	}

	String getNewFieldName() {
		return rule.getNewFieldName();
	}
}
