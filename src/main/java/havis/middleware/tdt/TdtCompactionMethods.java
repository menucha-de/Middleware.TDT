package havis.middleware.tdt;

import java.util.regex.Pattern;

/**
 * This class implements the methods for compaction and decompaction of fields
 * in the binary representation of the EPC-Field as described in Document
 * EPCglobal Tag Data Translation (TDT) 1.4 chapter 3.11 and ISO 15962
 */
class TdtCompactionMethods {

	private static Pattern isNumericString = Pattern.compile("^[0-9]*$");

	/**
	 * Compact one field into the binary EPC-Buffer at postion and bitlength
	 * from field to it's binary representation Note: Implemented are only the
	 * methods used in the shemes for TDT 1.4 for EPC-Field length greater 64
	 * Bits.
	 */
	static String compactItem(String value, FieldX field)
			throws TdtTranslationException {
		String result = "";
		switch (field.getCompaction()) {
		case _16BIT:
			methodNotImplemented(field.getCompaction());
			break;
		case _32BIT:
			result = compactItem32Bit(field, value);
			break;
		case _5BIT:
			methodNotImplemented(field.getCompaction());
			break;
		case _6BIT:
			methodNotImplemented(field.getCompaction());
			break;
		case _7BIT:
			result = compactItem7Bit(field, value);
			break;
		case _8BIT:
			result = compactItem8Bit(field, value);
			break;
		default:
			methodNotImplemented(field.getCompaction());
			break;
		}
		return result;
	}

	/**
	 * Decompact one field of the binary EPC-Buffer at postion and bitlength
	 * from field to it's string representation Note: Implemented are only the
	 * methods used in the shemes for TDT 1.4 for EPC-Field length greater 64
	 * Bits.
	 *
	 * @return value as string
	 */
	static String decompactItem(String epcIdentifier, FieldX field)
			throws TdtTranslationException {
		String value = epcIdentifier.substring(field.getBitPosition(),
				field.getBitPosition() + field.getIntBitLength());
		String result = "";
		switch (field.getCompaction()) {
		case _16BIT:
			methodNotImplemented(field.getCompaction());
			break;
		case _32BIT:
			result = decompactItem32Bit(field, value);
			break;
		case _5BIT:
			methodNotImplemented(field.getCompaction());
			break;
		case _6BIT:
			methodNotImplemented(field.getCompaction());
			break;
		case _7BIT:
			result = decompactItem7Bit(field, value);
			break;
		case _8BIT:
			result = decompactItem8Bit(field, value);
			break;
		default:
			throw new TdtTranslationException("no valid compaction method");
		}
		return result;
	}

	private static String compactItem32Bit(FieldX field, String value)
			throws TdtTranslationException {
		if (value.isEmpty()) {
			value = "0";
		}
		// Check if value contains only numeric characters
		if (!isNumericString.matcher(value).matches()) {
			throw new TdtTranslationException(
					"Non numeric character in field '" + field.getName() + "'");
		}
		field.checkRange(value);
		String result = Long.toBinaryString(Long.parseLong(value));
		if (result.length() < field.getIntBitLength()) {
			for (int i = field.getIntBitLength() - result.length(); i > 0; i--)
				result = '0' + result;
		} else if (result.length() > field.getIntBitLength()) {
			result = result
					.substring(result.length() - field.getIntBitLength());
			throw new TdtTranslationException("Overflow occured. Field length exceeded.");
		}
		return result;
	}

	private static String compactItem7Bit(FieldX field, String value) {
		StringBuilder result = new StringBuilder();
		for (char c : value.toCharArray()) {
			result.append(TdtTranslateCodeTable.getBitString(c, 7));
		}

		if (field.getBitPadDir() != null) {
			int requiredLength = field.getIntBitLength();
			int currentLength = result.length();
			if (currentLength < requiredLength) {
				StringBuilder sb = new StringBuilder();
				for (int i = requiredLength - result.length(); i > 0; i--)
					sb.append('0');
				if (field.getBitPadDir() == PadDirectionList.LEFT) {
					result.insert(0, sb);
				} else {
					result.append(sb);
				}
			}
		}
		return result.toString();
	}

	private static String compactItem8Bit(FieldX field, String value) {
		if (field.getPadDir() != null) {
		    StringBuilder sb = new StringBuilder(value);
			for (int i = field.getIntLength() - value.length(); i > 0; i--)
			    sb.insert(0, ' ');

			value = sb.toString();
		}
		StringBuilder current = new StringBuilder();
		for (char c : value.toCharArray()) {
			current.append(TdtTranslateCodeTable.getBitString(c, 8));
		}
		return current.toString();
	}

	private static String decompactItem32Bit(FieldX field, String value)
			throws TdtTranslationException {
		Long lvalue = Long.valueOf(Long.parseLong(value, 2));
		field.checkRange(lvalue);
		return lvalue.toString();
	}

	private static String decompactItem7Bit(FieldX field, String value) {
		StringBuilder result = new StringBuilder();
		int mod = value.length() % 7;
		if (mod > 0) {
			int newSize = value.length() + 7 - mod;
			for (int i = newSize - value.length(); i > 0; i--)
				value += '0';
		}
		for (int pos = 0; pos < field.getIntBitLength(); pos += 7) {
			result.append(TdtTranslateCodeTable.getCharacter(Integer
					.toHexString(Integer.parseInt(value.substring(pos, pos + 7), 2))));
		}
		return result.toString();
	}

	private static String decompactItem8Bit(FieldX field, String value) {
		StringBuilder result = new StringBuilder();
		for (int pos = 0; pos < field.getIntBitLength(); pos += 8) {
			String part8 = Integer.toHexString(Integer.parseInt(
					value.substring(pos, pos + 8), 2));
			result.append(TdtTranslateCodeTable.getCharacter(part8));
		}
		return result.toString().replace(" ", "");
	}

	static private void methodNotImplemented(CompactionMethodList method)
			throws TdtTranslationException {
		throw new TdtTranslationException(
				"Method Compact 'TDTCompactionMethods." + method
						+ "' not implemented");
	}
}
