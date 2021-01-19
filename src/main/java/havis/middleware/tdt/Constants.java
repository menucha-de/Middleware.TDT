package havis.middleware.tdt;

class Constants {

	/**
	 * @see EPC Tag Data Standard v1.5.3 - General description of ID values
	 */
	final static int[] MaxNumberOfTableEntries = new int[] { 16, 22, 32, 45, 64, 90, 128, 256, 512, 1024, 2048, 4096 };

	/**
	 * @see EPC Tag Data Standard v1.5.3 - General description of ID values
	 */
	final static String[] IDSizeBitPattern = new String[] { "000", "001", "010", "011", "100", "101", "110", "1110", "111100", "111101",
			"111110", "111111" };

	/**
	 * @see EPC Tag Data Standard v1.5.3 - General description of ID values
	 */
	final static int[] BaseOfTableEntries = new int[] { 16, 22, 32, 45, 64, 90, 128, 256, 512, 1024, 2048, 4096 };

	/**
	 * @see EPC Tag Data Standard v1.5.3 - General description of ID values
	 */
	final static int[] BitsOfSingleIDValue = new int[] { 4, 5, 5, 6, 6, 7, 7, 8, 9, 10, 11, 12 };

	/**
	 * @see EPC Tag Data Standard v1.5.3 - General description of ID values
	 */
	final static int[] BitsOfPairIDValues = new int[] { 8, 9, 10, 11, 12, 13, 14, 16, 18, 20, 22, 24 };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static int[] Base10 = new int[] {
			// 0 - 9
			0, 4, 7, 10, 14, 17, 20, 24, 27, 30,
			// 10 - 19
			34, 37, 40, 44, 47, 50, 54, 57, 60, 64,
			// 20 - 29
			67, 70, 74, 77, 80, 84, 87, 90, 94, 97,
			// 30 - 39
			100, 103, 107, 110, 113, 117, 120, 123, 127, 130,
			// 40 - 48
			133, 137, 140, 143, 147, 150, 153, 157, 160 };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static int[] Base13 = new int[] {
			// 0 - 9
			0, 4, 8, 12, 15, 19, 23, 26, 30, 34,
			// 10 - 19
			38, 41, 45, 49, 52, 56, 60, 63, 67, 71,
			// 20 - 29
			75, 78, 82, 86, 89, 93, 97, 100, 104, 108,
			// 30 - 39
			112, 115, 119, 123, 126, 130, 134, 137, 141, 145,
			// 40 - 43
			149, 152, 156, 160 };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static int[] Base30 = new int[] {
			// 0 - 9
			0, 5, 10, 15, 20, 25, 30, 35, 40, 45,
			// 10 - 19
			50, 54, 59, 64, 69, 74, 79, 84, 89, 94,
			// 20 - 29
			99, 104, 108, 113, 118, 123, 128, 133, 138, 143,
			// 30 - 32
			148, 153, 158 };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static int[] Base40 = new int[] {
			// 0 - 9
			0, 6, 11, 16, 22, 27, 32, 38, 43, 48,
			// 10 - 19
			54, 59, 64, 70, 75, 80, 86, 91, 96, 102,
			// 20 - 29
			107, 112, 118, 123, 128, 134, 139, 144, 150, 155,
			// 30
			160 };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static int[] Base74 = new int[] {
			// 0 - 9
			0, 7, 13, 19, 25, 32, 38, 44, 50, 56,
			// 10 - 19
			63, 69, 75, 81, 87, 94, 100, 106, 112, 118,
			// 20 - 25
			125, 131, 137, 143, 150, 156 };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static int[] Base84 = new int[] {
			// 0 - 9
			0, 7, 13, 20, 26, 32, 39, 45, 52, 58,
			// 10 - 19
			64, 71, 77, 84, 90, 96, 103, 109, 116, 122,
			// 20 - 25
			128, 135, 141, 148, 154, 160 };

	final static char INVALID = 0xffff;

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base10Set = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base13BasicSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', INVALID, INVALID, INVALID };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base13Shift1Set = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M' };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base13Shift2Set = new char[] { 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base13Shift3Set = new char[] { ' ', '$', '%', '&', '*', '+', ',', '-', '.', '/', '?', '_', 29 };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base30BasicSet = new char[] { INVALID, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', INVALID, INVALID, INVALID };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base30Shift1Set = new char[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 23, 27, 28, 29, 30, 31,
			INVALID, INVALID, INVALID, '[', ']', '{', '}' };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base30Shift2Set = new char[] { ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
			':', ';', '<', '=', '>', '?', '@', '\\', '^', '_', '`', '|', '~', INVALID };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base40BasicSet = new char[] { INVALID, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', INVALID, INVALID, INVALID, '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9' };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base74Set = new char[] { 29, '!', '"', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=',
			'>', '?', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', ' ' };

	/**
	 * @see EPC Tag Data Standard v1.9 - Appendix K Packed Objects Encoding
	 *      tables
	 */
	final static char[] Base84Set = new char[] { 29, '!', '"', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=',
			'>', '?', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * Returns the index of the character in the basic set or -1 if the
	 * character is not a member of the set.
	 * 
	 * @param basicSet
	 * @param c
	 * @return index of the character or -1
	 */
	static int indexOfChar(char[] basicSet, char c) {
		for (int i = 0; i < basicSet.length; i++) {
			if (c == basicSet[i])
				return i;
		}
		return -1;
	}

	/**
	 * @param dataFormat
	 * @return The base table ID of the defined data format.
	 * @throws UnsupportedOperationException
	 *             If data format is not supported.
	 */
	static String getBaseTableID(int dataFormat) throws UnsupportedOperationException {
		switch (dataFormat) {
		case 9:
			return "F9B0";
		default:
			throw new UnsupportedOperationException("Data format '" + dataFormat + "' is not supported.");
		}
	}
}