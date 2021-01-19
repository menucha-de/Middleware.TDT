package havis.middleware.tdt;

import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * This class holds the information about one field of the EPCglobal Tag Data
 * Translation (TDT) Definition Markup Files. The first partial class was
 * generated from the Field.xsd file. (For more information see Document
 * EPCglobal Tag Data Translation (TDT) 1.4, Ratified Standard, June 10,2009.
 * Latest version 1.4., chapter 3).
 */
public class FieldX {

	private Field field;

	/**
	 * Reference to the option object of this field
	 */
	private OptionX option;

	/**
	 * startposition in buffer for binary decoding/encoding
	 */
	private int bitPosition;

	/**
	 * number of bits for binary decoding/encoding as integer value
	 */
	private int intBitLength;

	/**
	 * Indicator, if minimum and maximum for this field is specified
	 */
	private boolean hasRange;

	/**
	 * number of bits for binary decoding/encoding as integer value (Converted
	 * from length - property)
	 */
	private int intLength;

	/**
	 * Minimum value allowed for this field (Converted from decimalMaximum -
	 * property)
	 */
	private Long minimum;

	/**
	 * Maximum value allowed for this field as integer value (Converted from
	 * decimalMaximum - property)
	 */
	private Long maximum;

	/**
	 * Indicator if only numeric values are allowed for this field
	 */
	private boolean isNumeric;

	/**
	 * Precompiled regular expression for checking value against characterSet
	 * property
	 */
	private Pattern charSetRegex;

	FieldX(Field field, OptionX option) {
		this.field = field;
		this.option = option;
		init();
	}

	/**
	 * unique ID of this field
	 */
	String getId() {
		return option.getId() + "." + field.getName();
	}

	/**
	 * startposition in buffer for binary decoding/encoding
	 */
	int getBitPosition() {
		return bitPosition;
	}

	void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}

	/**
	 * number of bits for binary decoding/encoding as integer value (converted
	 * from bitLength - property)
	 */
	int getIntBitLength() {
		return intBitLength;
	}

	void setIntBitLength(int intBitLength) {
		this.intBitLength = intBitLength;
	}

	/**
	 * Indicator, if minimum and maximum for this field is specified
	 */
	boolean HasRange() {
		return hasRange;
	}

	/**
	 * number of bits for binary decoding/encoding as integer value (Converted
	 * from length - property) number of bits for binary decoding/encoding as
	 * integer value (Converted from length - property)
	 */
	int getIntLength() {
		return intLength;
	}

	/**
	 * Minimum value allowed for this field as integer value (Converted from
	 * decimalMaximum - property)
	 */
	public Long getMinimum() {
		return minimum;
	}

	/**
	 * Maximum value allowed for this field as integer value (Converted from
	 * decimalMaximum - property)
	 */
	public Long getMaximum() {
		return maximum;
	}

	/**
	 * Indicator if only numeric values are allowed for this field
	 */
	boolean isNumeric() {
		return isNumeric;
	}

	CompactionMethodList getCompaction() {
		CompactionMethodList compaction = field.getCompaction();
		return compaction == null ? CompactionMethodList._32BIT : compaction;
	}

	public String getName() {
		return field.getName();
	}

	PadDirectionList getBitPadDir() {
		return field.getBitPadDir();
	}

	PadDirectionList getPadDir() {
		return field.getPadDir();
	}

	public BigInteger getLength() {
		return field.getLength();
	}

	public char getPadChar() {
		return field.getPadChar() == null ? ' ' : field.getPadChar().charAt(0);
	}

	int getSeq() {
		return field.getSeq().intValue();
	}

	/**
	 * Initialize this field object Convert the string value from xml
	 * specification to numeric values and initialize indicator fields,
	 */
	private void init() {
		// Convert the string Values from file to numerical values
		if ((field.getDecimalMinimum() != null)
				&& (field.getDecimalMaximum() != null)) {
			try {
				hasRange = true;
				minimum = Long.valueOf(Long.parseLong(field.getDecimalMinimum()));
				maximum = Long.valueOf(Long.parseLong(field.getDecimalMaximum()));
			} catch (Exception e) {
				hasRange = false;
				minimum = Long.valueOf(Integer.MIN_VALUE);
				maximum = Long.valueOf(Integer.MAX_VALUE);
			}
		} else {
			hasRange = false;
			minimum = Long.valueOf(Integer.MIN_VALUE);
			maximum = Long.valueOf(Integer.MAX_VALUE);
		}

		isNumeric = field.getCharacterSet().matches("^[0-9\\-*\\[\\]]*$");
		intBitLength = field.getBitLength() == null ? 0 : field.getBitLength()
				.intValue();
		String charSet = "^"
				+ (field.getCharacterSet().endsWith("*") ? field
						.getCharacterSet() : field.getCharacterSet() + "*")
				+ "$";
		charSetRegex = Pattern.compile(charSet);
		intLength = field.getLength() == null ? Integer.MAX_VALUE : field
				.getLength().intValue();
	}

	/**
	 * Check if the supplied value matches the character set
	 *
	 * @param value
	 *            string value
	 * @throws TdtTranslationException
	 */
	void checkCharacterSet(String value) throws TdtTranslationException {
		if (!charSetRegex.matcher(value).matches()) {
			throw new TdtTranslationException(
					"Invalid charakter in value field " + getId());
		}
	}

	/**
	 * Check if the supplied value (as string value) falls into the specified
	 * range
	 *
	 * @param value
	 *            string value
	 * @throws TdtTranslationException
	 */
	void checkRange(String value) throws TdtTranslationException {
		if (!hasRange)
			return;
		Long intValue;
		try {
			intValue = Long.valueOf(Long.parseLong(value));
		} catch (Exception e) {
			throw new TdtTranslationException("Cannot convert " + value
					+ " to ulong in field " + getId());
		}
		checkRange(intValue);
	}

	/**
	 * Check if the supplied value (as string value) falls into the specified
	 * range
	 *
	 * @param value
	 *            string value
	 * @throws TdtTranslationException
	 */
	void checkRange(Long value) throws TdtTranslationException {
		if (hasRange) {
			if ((value.compareTo(minimum) < 0)
					|| (value.compareTo(maximum) > 0)) {
				throw new TdtTranslationException("Value {" + value
						+ "} out of range [{" + minimum + "},{" + maximum
						+ "}] in field " + getId());
			}
		}
	}

	/**
	 * Check if the supplied value matches the given character set
	 *
	 * @param value
	 * @return true if match, false otherwise
	 */
	boolean valueCheckMatch(String value) {
		if (value.length() > intLength) {
			return false;
		}
		if (hasRange) {
			Long ulongValue = Long.valueOf(Long.parseLong(value));
			if ((ulongValue.compareTo(minimum) < 0)
					|| (ulongValue.compareTo(maximum) > 0)) {
				return false;
			}
		}
		return true;
	}

	public String getDecimalMinimum() {
		return field.getDecimalMinimum();
	}

	public Object getDecimalMaximum() {
		return field.getDecimalMaximum();
	}

	public String getCharacterSet() {
		return field.getCharacterSet();
	}

}