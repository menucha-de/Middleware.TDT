package havis.middleware.tdt;

public class Utils {

	/**
	 * Prepends the specified string with padChars until it reaches the required
	 * length
	 * 
	 * @param string
	 *            the string
	 * @param length
	 *            the length
	 * @param padChar
	 *            the char to prepend
	 * @return the string with the prepended characters, e.g. padLeft("111", 4,
	 *         '0') = "0111"
	 */
	public static String padLeft(String string, int length, char padChar) {
		if (string == null || string.length() >= length) {
			return string;
		}

		StringBuilder sb = new StringBuilder();
		int count = length - string.length();
		for (int i = 0; i < count; i++) {
			sb.append(padChar);
		}
		return sb.append(string).toString();
	}

	/**
	 * Appends the specified string with padChars until it reaches the required
	 * length
	 * 
	 * @param string
	 *            the string
	 * @param length
	 *            the length
	 * @param padChar
	 *            the char to append
	 * @return the string with the appended characters, e.g. padRight("111", 4,
	 *         '0') = "1110"
	 */
	public static String padRight(String string, int length, char padChar) {
		if (string == null || string.length() >= length) {
			return string;
		}

		StringBuilder sb = new StringBuilder(string);
		int count = length - string.length();
		for (int i = 0; i < count; i++) {
			sb.append(padChar);
		}
		return sb.toString();
	}
}
