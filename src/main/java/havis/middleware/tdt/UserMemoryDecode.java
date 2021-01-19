package havis.middleware.tdt;

import havis.middleware.tdt.PackedObjectInvestigator.TableHeader;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UserMemoryDecode {

	/**
	 * Checks if the end of data has been reached.
	 * 
	 * @param buffer
	 * @param lengthInformation
	 * @param offset
	 * @param bitsToRead
	 * @param pad
	 * @return {@code true} if end of data has been reached. {@code false} if
	 *         still data is left.
	 */
	private static boolean reachedEndOfData(Buffer buffer, int lengthInformation, int offset, int bitsToRead, int pad) {
		int bitsInLastByte = 8;
		if (pad == 1) {
			byte b = buffer.get(lengthInformation);
			String binary = DataTypeConverter.byteArrayToBinaryString(new byte[] { b });
			bitsInLastByte = binary.lastIndexOf("1");
		}
		int maxBitsToRead = (lengthInformation - 1) * 8 + bitsInLastByte;
		int pos = offset + bitsToRead;

		if (pos > maxBitsToRead) {
			throw new IllegalArgumentException("Decoding failed: End has been reached.");
		} else if (pos == maxBitsToRead) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Reverse the string.
	 * 
	 * @param value
	 *            The string
	 * @return The reversed string.
	 */
	private static String reverse(String value) {
		String result = "";
		for (int i = value.length() - 1; i > -1; i--) {
			result += value.charAt(i);
		}
		return result;
	}

	/**
	 * Ascertains the access method.
	 * 
	 * @param buffer
	 * @return The access method that defines the manner how the data is mapped.
	 * @see ISO/IEC-15961_2004 - 7.1.2.3 StorageFormat (TypeReference and
	 *      elementName)<br>
	 *      and<br>
	 *      ISO/IEC-15961_2004 - 7.1.2.4 accessMethod (elementName)<br>
	 *      and<br>
	 *      ISO/IEC-15962_2004 - 7.2 Defining the system information<br>
	 *      and<br>
	 *      ISO/IEC-15962_2004 - 7.2.5 Storage Format
	 */
	private static int getAccessMethod(Buffer buffer) {
		return buffer.getInt(2);
	}

	/**
	 * Ascertains the extended bit.
	 * 
	 * @param buffer
	 * @return reserved for future use
	 * @see ISO/IEC-15962_2004 - 7.2 Defining the system information<br>
	 *      and<br>
	 *      ISO/IEC-15962_2004 - 7.2.5 Storage Format
	 */
	private static int getExtendedBit(Buffer buffer) {
		return buffer.getBit();
	}

	/**
	 * Ascertains the data format.
	 * 
	 * @param buffer
	 * @return The data format that defines the types of object identifier being
	 *         stored in the data.
	 * @see ISO/IEC-15961_2004 - 7.1.2.3 StorageFormat (TypeReference and
	 *      elementName)<br>
	 *      and<br>
	 *      ISO/IEC-15961_2004 - 7.1.2.5 dataFormat (elementName)<br>
	 *      and</br>
	 *      ISO/IEC-15962_2004 - 8.3.2 Formatting the objectId<br>
	 *      and<br>
	 *      ISO/IEC-15962_2004 - 7.2 Defining the system information<br>
	 *      and<br>
	 *      ISO/IEC-15962_2004 - 7.2.5 Storage Format
	 */
	private static int getDataFormat(Buffer buffer) {
		return buffer.getInt(5);
	}

	/**
	 * Analyzes the format flags section
	 * 
	 * @param buffer
	 * @return {@code true} if more data must be read. {@code false} if no more
	 *         packed objects follow.
	 * @throws UnsupportedOperationException
	 *             If the format flag is not supported.
	 * @see EPC Tag Data Standard v1.9 - I.4 Format Flags section
	 */
	private static boolean analyzeFormatFlagsSection(Buffer buffer) throws UnsupportedOperationException {
		int flags = buffer.b & 0xFF;
		switch (flags) {
		case 0: // Termination Pattern
			return false;
		case 8: // IDLPO DEFAULT
			buffer.next();
			return true;
		default:
			if ((buffer.b & 0xFC) > 3) { // is first octet of an IDLPO
				return true;
			} else {
				throw new UnsupportedOperationException("Decoding failed: Format flags are not fully supported.");
			}
		}
	}

	/**
	 * Ascertains the length of the Packed-Object
	 * 
	 * @param buffer
	 * @return The number of octets in the current Object, plus a last octet pad
	 *         indicator.
	 * @see EPC Tag Data Standard v1.9 - I.5.2 Length Information
	 */
	private static int getLengthInformation(Buffer buffer) {
		return EBV.EBV6.valueOf(buffer);
	}

	/**
	 * Ascertains the value of the pad indicator.
	 * 
	 * @param buffer
	 * @return '0' if there are no padding bits in the last byte of the Packed
	 *         Object. If is set to '1', then bitwise padding begins with the
	 *         least-significants or rightmost '1' bit of the last byte.
	 * @see EPC Tag Data Standard v1.9 - I.5.2 Length Information
	 */
	private static int getPadIndicator(Buffer buffer) {
		return buffer.getBit();
	}

	/**
	 * Ascertains the number of IDs in the Packed-Object.
	 * 
	 * @param buffer
	 * @return Number of ID Values in the current Object
	 * @see EPC Tag Data Standard v1.9 - I.5.1.1 IDLPO default Object Info
	 *      format
	 */
	private static int getNumberOfIDs(Buffer buffer) {
		// The EBV3 value contains the number of ID Values in the object (minus
		// one).
		return EBV.EBV3.valueOf(buffer) + 1;
	}

	/**
	 * Ascertains the IDs of the Packed-Object.
	 * 
	 * @param buffer
	 * @param numberOfIDs
	 * @return A single list of ID Values; value size depends on registered Data
	 *         Format
	 * @throws IllegalArgumentException
	 *             If {@code numberOfIDs} is 0
	 * @see EPC Tag Data Standard v1.9 - I.5.4 ID Values representation in an ID
	 *      Value-list Packed Object<br>
	 *      and</br>
	 *      EPC Tag Data Standard v1.9 - I.5.3 General description of ID values
	 */
	private static List<Integer> getIDValues(PackedObjectInvestigator investigator, Buffer buffer, int dataFormat, int numberOfIDs) {
		if (numberOfIDs < 1)
			throw new IllegalArgumentException("Decoding failed: The number of IDs must not be zero.");				
		int tableSize = Integer.valueOf(investigator.getHeader(Constants.getBaseTableID(dataFormat), TableHeader.K_ID_SIZE));
		for(int i = 0; i < Constants.MaxNumberOfTableEntries.length; i++) {
			if(tableSize <= Constants.MaxNumberOfTableEntries[i]){				
				List<Integer> idValues = new ArrayList<Integer>();
				int base =  Constants.BaseOfTableEntries[i];
				int pairBits = Constants.BitsOfPairIDValues[i];
				int singleBits = Constants.BitsOfSingleIDValue[i];
				for (int j = 0; j < numberOfIDs / 2; j++) {
					int id = buffer.getInt(pairBits);
					idValues.add(id / base);
					idValues.add(id % base);
				}
				if (numberOfIDs % 2 > 0) {
					int id = buffer.getInt(singleBits);
					idValues.add(id);
				}
				return idValues;
			}
		}
		throw new IllegalArgumentException("Table size '" + tableSize + "' exceeds the maximum number of permitted table entries.");		
	}

	/**
	 * Reads the aux format section.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the specified aux format is not supported.
	 * @see EPC Tag Data Standard v1.9 - I.7 Aux Format section<br>
	 *      and<br>
	 *      EPC Tag Data Standard v1.9 - I.7.2 Support for the Packed-Object
	 *      compaction method
	 */
	private static void readAuxFormatSection(Buffer buffer, List<Entry> entries, Entry lastNonNumeric)
			throws UnsupportedOperationException {
		int auxFormat = buffer.getBit();
		if (auxFormat == 1) {
			// read variable length
			for (Entry entry : entries) {
				int range = entry.getRange();
				if (entry.equals(lastNonNumeric)) {
					entry.valueLength = -1;
				} else if (range > 0) {
					if (entry.isSingleNumeric() || entry.isSingleNonNumeric()) {
						entry.valueLength = getVariableLength(buffer, range, entry.getMinLength());
					} else {
						entry.valueLength = entry.getFormatValues().get(0).getValue().getKey()
								+ getVariableLength(buffer, range, entry.getFormatValues().get(1).getValue().getKey());
					}
				} else {
					entry.valueLength = entry.getMinLength();
				}
			}
		} else {
			throw new UnsupportedOperationException("Decoding failed: The specified aux format is not supported.");
		}
	}

	/**
	 * Ascertains the variable length.
	 * 
	 * @param buffer
	 * @param range
	 * @param minLength
	 * @return The value of the variable length
	 * @see EPC Tag Data Standard v1.9 - I.7.2 Support for the Packed-Object
	 *      compaction method
	 */
	private static int getVariableLength(Buffer buffer, int range, int minLength) {
		if (range < 8 || range > 44) {
			int bits = DataTypeConverter.convert(range, 10, 2, 0).length();
			return buffer.getInt(bits) + minLength;
		} else {
			int length = buffer.getInt(4);
			if (length > 14) {
				length += buffer.getInt(4);
			}
			if (length > 29) {
				length += buffer.getInt(4);
			}
			if (length > 44) {
				length += EBV.EBV6.valueOf(buffer);
			}
			return length + minLength;
		}
	}

	/**
	 * Reads the known length numeric section.
	 * 
	 * @param buffer
	 * @param entries
	 * @see EPC Tag Data Standard v1.9 - I.8.1 Known-length-Numerics subsection
	 *      of the Data Section
	 */
	public static void readKnownLengthNumericSection(Buffer buffer, List<Entry> entries) {
		for (Entry entry : entries) {
			if (entry.isSingleNumeric() || !entry.isSingleNonNumeric()) {
				int size = 0;
				if (entry.isSingleNumeric()) {
					size = entry.valueLength;
				} else {
					size = entry.getFormatValues().get(0).getValue().getKey();
				}
				entry.value = "";
				while (size > 0) {
					int length = size >= Constants.Base10.length ? Constants.Base10.length - 1 : size;
					size -= length;
					String value = buffer.getBigInteger(Constants.Base10[length]).toString(10);
					while (value.length() < length) {
						value = '0' + value;
					}
					entry.value += value;
				}
			}
		}
	}

	/**
	 * Ascertains the specified base of the non numeric section.
	 * 
	 * @param buffer
	 * @return The base which has been chosen for the non-numeric data
	 * @see EPC Tag Data Standard v1.9 - I.8.2.1 A/N Header Bits
	 */
	private static int getNonNumericBase(Buffer buffer) {
		int bit = buffer.getBit();
		if (bit == 0) {
			// '0' indicates that Base 30 was chosen for the non-numeric Base
			return 30;
		} else {
			bit = buffer.getBit();
			if (bit == 0) {
				// '10' indicates that Base 74 was chosen for the non-numeric
				// Base
				return 74;
			} else {
				// '11' indicates that Base 256 was chosen for the non-numeric
				// Base
				return 256;
			}
		}
	}

	/**
	 * Reads the prefix or suffix section.
	 * 
	 * @param buffer
	 * @param nonNumericBase
	 * @return List&lt;Base, Number of characters&gt; or {@code null} if no
	 *         prefix or suffix run has been specified.
	 * @see EPC Tag Data Standard v1.9 - I.8.2.3 Prefix and Suffix Run-Length
	 *      encoding
	 */
	private static List<Map.Entry<Integer, Integer>> readPrefixSuffixRuns(Buffer buffer, int nonNumericBase) {
		if (buffer.getBit() == 0) {
			return null;
		} else {
			List<Map.Entry<Integer, Integer>> result = new ArrayList<Map.Entry<Integer, Integer>>();
			int moreRuns = 0;
			int base = 0;
			do {
				moreRuns = buffer.getBit();
				switch (buffer.getInt(2)) {
				case 0:
					base = 10;
					break;
				case 1:
					base = 13;
					break;
				case 2:
					base = nonNumericBase;
					break;
				case 3:
					switch (nonNumericBase) {
					case 30:
					case 256:
						base = 40;
						break;
					case 74:
						base = 84;
						break;
					default:
						throw new UnsupportedOperationException("Decoding failed: Base " + nonNumericBase + " is not supported.");
					}
					break;
				default:
					break;
				}
				int length = 6 + 2 * buffer.getInt(3);
				result.add(new SimpleEntry<Integer, Integer>(base, length));
			} while (moreRuns == 0);
			return result;
		}
	}

	/**
	 * Ascertains the bits that must be read for the number of characters.
	 * 
	 * @param numberOfBaseCharacters
	 * @return The number of bits.
	 */
	private static int getBitsToRead(HashMap<Integer, Integer> numberOfBaseCharacters) {
		int result = 0;
		if (numberOfBaseCharacters.containsKey(10)) {
			result += getBitsToRead(Constants.Base10, numberOfBaseCharacters.get(10));
		}
		if (numberOfBaseCharacters.containsKey(13)) {
			result += getBitsToRead(Constants.Base13, numberOfBaseCharacters.get(13));
		}
		if (numberOfBaseCharacters.containsKey(30)) {
			result += getBitsToRead(Constants.Base30, numberOfBaseCharacters.get(30));
		}
		if (numberOfBaseCharacters.containsKey(40)) {
			result += getBitsToRead(Constants.Base40, numberOfBaseCharacters.get(40));
		}
		if (numberOfBaseCharacters.containsKey(74)) {
			result += getBitsToRead(Constants.Base74, numberOfBaseCharacters.get(74));
		}
		if (numberOfBaseCharacters.containsKey(84)) {
			result += getBitsToRead(Constants.Base84, numberOfBaseCharacters.get(84));
		}
		if (numberOfBaseCharacters.containsKey(256)) {
			result += numberOfBaseCharacters.get(256) * 8;
		}
		return result;
	}

	/**
	 * Ascertains the bits that must be read for the number of characters of the
	 * specified base.
	 * 
	 * @param base
	 * @param number
	 * @return The number of bits.
	 */
	private static int getBitsToRead(int[] base, int number) {
		int result = 0;
		while (number >= base.length - 1) {
			result += base[base.length - 1];
			number -= base.length - 1;
		}
		if (number > 0) {
			result += base[number];
		}
		return result;
	}

	/**
	 * Adds the number of characters to the list which must be read.
	 * 
	 * @param numberOfBaseCharacters
	 * @param prefixSuffixRun
	 */
	private static void addNumberOfBaseCharacters(HashMap<Integer, Integer> numberOfBaseCharacters,
			List<Map.Entry<Integer, Integer>> prefixSuffixRun) {
		if (prefixSuffixRun != null) {
			for (Map.Entry<Integer, Integer> entry : prefixSuffixRun) {
				int base = entry.getKey();
				int characters = entry.getValue();
				characters += numberOfBaseCharacters.get(base);
				numberOfBaseCharacters.put(base, characters);
			}
		}
	}

	/**
	 * Reads the an header.
	 * 
	 * @param buffer
	 * @param lengthInformation
	 * @param pad
	 * @param nonNumericBase
	 * @param numberOfBaseCharacters
	 * @return The AN header bits
	 * @see EPC Tag Data Standard v1.9 - I.8.2.1 A/N Header Bits<br>
	 *      and<br>
	 *      EPC Tag Data Standard v1.9 - I.8.2.2 Dual-base Character-map
	 *      encoding
	 */
	private static String getAnHeaderBits(Buffer buffer, int lengthInformation, int pad, int nonNumericBase,
			HashMap<Integer, Integer> numberOfBaseCharacters) {
		String anHeaderBits = "";
		// offset is current position minus length of DSFID
		int offset = buffer.getPos() - 8;
		while (!reachedEndOfData(buffer, lengthInformation, offset + anHeaderBits.length(), getBitsToRead(numberOfBaseCharacters), pad)) {
			int bit = buffer.getBit();
			anHeaderBits += bit;
			if (bit == 0) {
				numberOfBaseCharacters.put(10, numberOfBaseCharacters.get(10) + 1);
			} else {
				numberOfBaseCharacters.put(nonNumericBase, numberOfBaseCharacters.get(nonNumericBase) + 1);
			}
		}
		return anHeaderBits;
	}

	/**
	 * <u>Note:</u> The integer values of the characters represents the index in
	 * the specified base set.
	 * 
	 * @param buffer
	 * @param base
	 * @param baseBits
	 * @param characters
	 * @return The data of the binary base segment
	 */
	private static String readBaseData(Buffer buffer, int base, int[] baseBits, int characters) {
		String result = "";
		while (characters > 0) {
			int length = characters >= baseBits.length ? baseBits.length - 1 : characters;
			characters -= length;
			BigInteger value = buffer.getBigInteger(baseBits[length]);
			String temp = "";
			for (int i = 0; i < length; i++) {
				temp += (char) value.mod(new BigInteger(base + "")).intValue();
				value = value.divide(new BigInteger(base + ""));
			}
			while (temp.length() < length) {
				temp += (char) 0;
			}
			result += reverse(temp);
		}
		return result;
	}

	/**
	 * Reads the extended numeric binary segment of the alpha-numeric subsection
	 * 
	 * <u>Note:</u> The integer values of the characters represents the index in
	 * the specified base set.
	 * 
	 * @param buffer
	 * @param numberOfBaseCharacters
	 * @return The data of the extended numeric binary segment
	 * @see EPC Tag Data Standard v1.9 - I.8.2.4 Encoding into Binary Segments
	 */
	private static String readExtendedNumeric(Buffer buffer, HashMap<Integer, Integer> numberOfBaseCharacters) {
		return readBaseData(buffer, 13, Constants.Base13, numberOfBaseCharacters.get(13));
	}

	/**
	 * Reads the extended non numeric binary segment of the alpha-numeric
	 * subsection
	 * 
	 * <u>Note:</u> The integer values of the characters represents the index in
	 * the specified base set.
	 * 
	 * @param buffer
	 * @param numberOfBaseCharacters
	 * @return The data of the extended non numeric binary segment
	 * @see EPC Tag Data Standard v1.9 - I.8.2.4 Encoding into Binary Segments
	 */
	private static String readExtendedNonNumeric(Buffer buffer, HashMap<Integer, Integer> numberOfBaseCharacters) {
		if (numberOfBaseCharacters.get(40) > 0) {
			return readBaseData(buffer, 40, Constants.Base40, numberOfBaseCharacters.get(40));
		} else if (numberOfBaseCharacters.get(84) > 0) {
			return readBaseData(buffer, 84, Constants.Base84, numberOfBaseCharacters.get(84));
		} else {
			return "";
		}
	}

	/**
	 * Reads the base 10 numeric binary segment of the alpha-numeric subsection
	 * 
	 * @param buffer
	 * @param numberOfBaseCharacters
	 * @return The data of the base 10 numeric binary segment
	 * @see EPC Tag Data Standard v1.9 - I.8.2.4 Encoding into Binary Segments
	 */
	private static String readBase10(Buffer buffer, HashMap<Integer, Integer> numberOfBaseCharacters) {
		return readBaseData(buffer, 10, Constants.Base10, numberOfBaseCharacters.get(10));
	}

	/**
	 * Reads the non numeric binary segment of the alpha-numeric subsection
	 * 
	 * @param buffer
	 * @param nonNumericBase
	 * @param numberOfBaseCharacters
	 * @return The data of the non numeric binary segment.
	 * @see EPC Tag Data Standard v1.9 - I.8.2.4 Encoding into Binary Segments
	 */
	private static String readNonNumeric(Buffer buffer, int nonNumericBase, HashMap<Integer, Integer> numberOfBaseCharacters) {
		switch (nonNumericBase) {
		case 30:
			return readBaseData(buffer, 30, Constants.Base30, numberOfBaseCharacters.get(30));
		case 74:
			return readBaseData(buffer, 74, Constants.Base74, numberOfBaseCharacters.get(74));
		case 256:
			int characters = numberOfBaseCharacters.get(256);
			String data = "";
			for (; characters > 0; characters--) {
				data += (char) buffer.getInt(8);				
			}
			return data;
		default:
			throw new UnsupportedOperationException("Decoding failed: Base " + nonNumericBase + " is not supported.");
		}
	}

	/**
	 * Converts the integer values of the characters to the character of the
	 * specified base.
	 * 
	 * @param base
	 * @param data
	 * @return The real character string.
	 */
	private static String convert(int base, String data) {
		int length = data.length();
		String dataString = "";
		switch (base) {
		case 10:
			for (int i = 0; i < length; i++) {
				dataString += Constants.Base10Set[data.charAt(i)];
			}
			break;
		case 13:
			for (int i = 0; i < length; i++) {
				char c = data.charAt(i);
				switch (c) {
				case 10:
					c = data.charAt(++i);
					dataString += Constants.Base13Shift1Set[c];
					break;
				case 11:
					c = data.charAt(++i);
					dataString += Constants.Base13Shift2Set[c];
					break;
				case 12:
					c = data.charAt(++i);
					dataString += Constants.Base13Shift3Set[c];
					break;
				default:
					dataString += Constants.Base13BasicSet[c];
					break;
				}
			}
			break;
		case 30:
			for (int i = 0; i < length; i++) {
				char c = data.charAt(i);
				switch (c) {
				case 27:
					c = data.charAt(++i);
					dataString += Constants.Base30Shift1Set[c];
					break;
				case 28:
					c = data.charAt(++i);
					dataString += Constants.Base30Shift2Set[c];
					break;
				default:
					dataString += Constants.Base30BasicSet[c];
					break;
				}
			}
			break;
		case 40:
			for (int i = 0; i < length; i++) {
				char c = data.charAt(i);
				switch (c) {
				case 27:
					c = data.charAt(++i);
					dataString += Constants.Base30Shift1Set[c];
					break;
				case 28:
					c = data.charAt(++i);
					dataString += Constants.Base30Shift2Set[c];
					break;
				default:
					dataString += Constants.Base40BasicSet[c];
					break;
				}
			}
			break;
		case 74:
			for (int i = 0; i < length; i++) {
				dataString += Constants.Base74Set[data.charAt(i)];
			}
			break;
		case 84:
			for (int i = 0; i < length; i++) {
				dataString += Constants.Base84Set[data.charAt(i)];
			}
			break;
		case 256:
			return data;
		default:
			throw new UnsupportedOperationException("Decoding failed: Base " + base + " is not supported.");
		}
		return dataString;
	}

	/**
	 * Decodes the hex string.
	 * 
	 * @param investigator
	 * @param hexValue
	 * @return The decoded data as PackedObjects
	 */
	public static PackedObjects decode(PackedObjectInvestigator investigator, String hexValue) {
		return UserMemoryDecode.decode(investigator, DataTypeConverter.hexStringToByteArray(hexValue));
	}

	/**
	 * Decodes the byte array.
	 * 
	 * @param investigator
	 * @param byteValue
	 * @return The decoded data as PackedObjects
	 * @throws UnsupportedOperationException
	 *             Extended bit is not supported. The specified base is not
	 *             supported. The data format is not supported. The access
	 *             method is not supported.
	 */
	public static PackedObjects decode(PackedObjectInvestigator investigator, byte[] byteValue) throws UnsupportedOperationException {
		PackedObjects packedObjects = new PackedObjects(investigator);
		Buffer buffer = new Buffer(byteValue);

		// READ DSFID
		int accessMethod = getAccessMethod(buffer);
		int extended = getExtendedBit(buffer);
		int dataFormat = getDataFormat(buffer);

		if (extended > 0) {
			throw new UnsupportedOperationException("Decoding failed: Extended bit is not supported.");
		}

		switch (accessMethod) {
		case 2: // Packed Objects
			switch (dataFormat) {
			case 9: // ean-ucc
				if (analyzeFormatFlagsSection(buffer)) {
					int lengthInformation = getLengthInformation(buffer);
					int pad = getPadIndicator(buffer);
					int numberOfIDs = getNumberOfIDs(buffer);
					List<Integer> idValues = getIDValues(investigator, buffer, dataFormat, numberOfIDs);
					List<Entry> entries = Entry.getEntries(investigator, buffer, dataFormat, idValues);
					Entry lastNonNumeric = Entry.getLastNonNumericEntry(entries);
					readAuxFormatSection(buffer, entries, lastNonNumeric);
					readKnownLengthNumericSection(buffer, entries);

					if (!reachedEndOfData(buffer, lengthInformation, buffer.getPos() - 8, 0, pad)) {
						// readAlphaNumericSection
						int nonNumericBase = getNonNumericBase(buffer);
						List<Map.Entry<Integer, Integer>> prefixRuns = readPrefixSuffixRuns(buffer, nonNumericBase);
						List<Map.Entry<Integer, Integer>> suffixRuns = readPrefixSuffixRuns(buffer, nonNumericBase);

						HashMap<Integer, Integer> numberOfBaseCharacters = new HashMap<Integer, Integer>();
						numberOfBaseCharacters.put(10, 0);
						numberOfBaseCharacters.put(13, 0);
						numberOfBaseCharacters.put(30, 0);
						numberOfBaseCharacters.put(40, 0);
						numberOfBaseCharacters.put(74, 0);
						numberOfBaseCharacters.put(84, 0);
						numberOfBaseCharacters.put(256, 0);
						addNumberOfBaseCharacters(numberOfBaseCharacters, prefixRuns);
						addNumberOfBaseCharacters(numberOfBaseCharacters, suffixRuns);

						String anHeaderBits = getAnHeaderBits(buffer, lengthInformation, pad, nonNumericBase, numberOfBaseCharacters);
						// read extended numeric value
						String extendedNumericData = readExtendedNumeric(buffer, numberOfBaseCharacters);
						// read extended-NonNumeric encoding
						String extendedNonNumericData = readExtendedNonNumeric(buffer, numberOfBaseCharacters);
						// read Base10
						String base10Data = readBase10(buffer, numberOfBaseCharacters);
						// read nonNumeric
						String nonNumericData = readNonNumeric(buffer, nonNumericBase, numberOfBaseCharacters);

						String dataString = "";

						// read prefix data
						if (prefixRuns != null) {
							for (Map.Entry<Integer, Integer> run : prefixRuns) {
								int base = run.getKey();
								int characters = run.getValue();
								switch (base) {
								case 10:
									dataString += convert(base, base10Data.substring(0, characters));
									base10Data = base10Data.substring(characters);
									break;
								case 13:
									dataString += convert(base, extendedNumericData.substring(0, characters));
									extendedNumericData = extendedNumericData.substring(characters);
									break;
								case 40:
								case 84:
									dataString += convert(base, extendedNonNumericData.substring(0, characters));
									extendedNonNumericData = extendedNonNumericData.substring(characters);
									break;
								case 30:
								case 74:
								case 256:
									dataString += convert(base, nonNumericData.substring(0, characters));
									nonNumericData = nonNumericData.substring(characters);
									break;
								default:
									throw new UnsupportedOperationException("Decoding failed: Base " + base + " is not supported.");
								}
							}
						}

						// read char map data
						for (int i = 0; i < anHeaderBits.length(); i++) {
							char anHeaderBit = anHeaderBits.charAt(i);
							if (anHeaderBit == '0') {
								dataString += convert(10, base10Data.substring(0, 1));
								base10Data = base10Data.substring(1);
							} else {
								switch (nonNumericBase) {
								case 30:
									int c = nonNumericData.charAt(0);
									if (c == 27 || c == 28) {
										dataString += convert(nonNumericBase, nonNumericData.substring(0, 2));
										nonNumericData = nonNumericData.substring(2);
										i++;
									} else {
										dataString += convert(nonNumericBase, nonNumericData.substring(0, 1));
										nonNumericData = nonNumericData.substring(1);
									}
									break;
								case 74:
									dataString += convert(nonNumericBase, nonNumericData.substring(0, 1));
									nonNumericData = nonNumericData.substring(1);
									break;
								case 256:
									dataString += convert(nonNumericBase, nonNumericData.substring(0, 1));
									nonNumericData = nonNumericData.substring(1);
									break;
								default:
									throw new UnsupportedOperationException(
											"Decoding failed: Base " + nonNumericBase + " is not supported.");
								}
							}
						}

						// read suffix data
						if (suffixRuns != null) {
							for (Map.Entry<Integer, Integer> run : suffixRuns) {
								int base = run.getKey();
								int characters = run.getValue();
								switch (base) {
								case 10:
									dataString += convert(base, base10Data.substring(0, characters));
									base10Data = base10Data.substring(characters);
									break;
								case 13:
									dataString += convert(base, extendedNumericData.substring(0, characters));
									extendedNumericData = extendedNumericData.substring(characters);
									break;
								case 40:
								case 84:
									dataString += convert(base, extendedNonNumericData.substring(0, characters));
									extendedNonNumericData = extendedNonNumericData.substring(characters);
									break;
								case 30:
								case 74:
								case 256:
									dataString += convert(base, nonNumericData.substring(0, characters));
									nonNumericData = nonNumericData.substring(characters);
									break;
								default:
									throw new UnsupportedOperationException("Decoding failed: Base " + base + " is not supported.");
								}
							}
						}

						// assign the read data to the entries
						for (Entry entry : entries) {
							if (!entry.isSingleNumeric()) {
								if (entry.value == null) {
									entry.value = "";
								}
								if (entry.equals(lastNonNumeric)) {
									entry.value += dataString;
								} else {
									int length = entry.valueLength - entry.value.length();
									entry.value += dataString.substring(0, length);
									dataString = dataString.substring(length);
								}
							}
						}
					}
					for (Entry entry : entries) {
						packedObjects.getDataElements().add(new SimpleEntry<String, String>(entry.oid, entry.value));
					}
				}
				buffer.next();
				break;
			default:
				throw new UnsupportedOperationException("Decoding failed: Dataformat " + dataFormat + " is not supported.");
			}
			break;
		default:
			throw new UnsupportedOperationException("Decoding failed: Access method " + accessMethod + " is not supported.");
		}
		packedObjects.setNumberOfReadBytes(buffer.next);
		return packedObjects;
	}
}