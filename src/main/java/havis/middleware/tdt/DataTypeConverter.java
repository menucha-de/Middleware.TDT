package havis.middleware.tdt;

import java.math.BigInteger;

public class DataTypeConverter {

	/**
	 * Converts a hex-string to a byte array
	 * 
	 * @param s
	 * @return The byte array.
	 */
	public static byte[] hexStringToByteArray(String s) {
		int length = s.length();
		byte[] data = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}

		return data;
	}

	/**
	 * Converts a byte array to a hex-string.
	 * 
	 * @param bytes
	 * @return The hex-string.
	 */
	public static String byteArrayToHexString(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Converts a byte array to a binary string.
	 * 
	 * @param bytes
	 * @return The binary string.
	 */
	public static String byteArrayToBinaryString(byte[] bytes) {
		return byteArrayToBinaryString(bytes, 0);
	}

	/**
	 * Converts a byte array to a binary string.
	 * 
	 * @param bytes
	 * @param bits
	 * @return The binary string.
	 */
	public static String byteArrayToBinaryString(byte[] bytes, int bits) {
		String result = "";
		for (byte b : bytes) {
			for (int i = 0; i < 8; i++) {
				result += (b << i & 0x80) == 128 ? "1" : "0";
			}
		}
		if (bits > 0) {
			result = result.substring(result.length() - bits);
		}
		return result;
	}

	/**
	 * Converts a integer in the specified radix with the number of characters
	 * defined by the size.
	 * 
	 * @param value
	 * @param inputRadix
	 * @param outputRadix
	 * @param size
	 * @return The converted value.
	 */
	public static String convert(int value, int inputRadix, int outputRadix, int size) {
		return convert(value + "", inputRadix, outputRadix, size);
	}

	/**
	 * Converts a string in the specified radix with the number of characters
	 * defined by the size.
	 * 
	 * @param value
	 * @param inputRadix
	 * @param outputRadix
	 * @param size
	 * @return The converted value.
	 */
	public static String convert(String value, int inputRadix, int outputRadix, int size) {
		return convert(new BigInteger(value, inputRadix), outputRadix, size);
	}

	/**
	 * Converts a BigInteger in the specified radix with the number of
	 * characters defined by the size.
	 * 
	 * @param value
	 * @param outputRadix
	 * @param size
	 * @return The converted value.
	 */
	public static String convert(BigInteger value, int outputRadix, int size) {
		String result = value.toString(outputRadix);
		if (size < 1)
			return result;
		while (result.length() < size)
			result = "0" + result;
		if (result.length() > size)
			result = result.substring(result.length() - size);
		return result;
	}

	/**
	 * Return the number of bits that are required to represent the value.
	 * 
	 * @param value
	 * @return The number of required bits.
	 */
	public static int getNumberOfRequiredBits(int value) {
		if (value < 0) {
			throw new UnsupportedOperationException("Values less than zero are not supported.");
		}
		return DataTypeConverter.convert(value, 10, 2, 0).length();
	}

	/**
	 * Converts a string which shall be encoded to base74 to a binary string.
	 * 
	 * @param value
	 * @return The binary representation.
	 */
	public static String convertBase74ToBinary(String value) {
		if (value.equals(""))
			return "";
		BigInteger result = new BigInteger("0", 10);
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			int index = -1;
			if ((index = Constants.indexOfChar(Constants.Base74Set, c)) > -1) {
				result = result.multiply(new BigInteger("74", 10));
				result = result.add(new BigInteger("" + index, 10));
			} else {
				throw new IllegalArgumentException("Character '" + c + "' is not a member of base " + 74 + " set.");
			}
		}
		return convert(result, 2, Constants.Base74[value.length()]);
	}
}
