package havis.middleware.tdt;

import havis.middleware.tdt.PackedObjectInvestigator.TableHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UserMemoryEncode {

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
	 * Sorts the entries. Validates the values. Checks for duplicates.
	 * Ascertains the required IDValues. Ascertains the required secondary
	 * IDValues and flags.
	 * 
	 * @param investigator
	 * @param dataFormat
	 * @param elements
	 * @param baseIDValues
	 * @param secondaryIdSection
	 * @return The ordered and validated list of entries.
	 * @throws UnsupportedOperationException
	 *             If OIDs column format string is not supported.
	 * @throws IllegalArgumentException
	 *             If an OID is defined more than once. If a value is invalid.
	 * @see ISO/IEC-15961_2004 - J.2.2 OIDs and IDstring columns (Optional)<br>
	 *      and<br>
	 *      ISO/IEC-15961_2004 - J.2.3 FormatString column (Optional)<br>
	 *      and<br>
	 *      ISO/IEC-15961_2004 - J.3.1 Semantics for OIDs, IDString, and
	 *      FormatString Columns<br>
	 *      and<br>
	 */
	private static List<Entry> prepare(PackedObjectInvestigator investigator, int dataFormat, List<Map.Entry<String, String>> elements,
			List<Integer> baseIDValues, List<String> secondaryIdSection) throws UnsupportedOperationException, IllegalArgumentException {
		String prefix = "";
		if(dataFormat == 9){
			prefix = "urn:oid:1.0.15961.9.";
		}
		List<String> oids = new ArrayList<String>();
		List<Entry> entries = new ArrayList<Entry>();
		// convert elements to Entry
		// validate OID
		// validate value
		// check for duplicates
		for (Map.Entry<String, String> element : elements) {
			if (oids.contains(element.getKey())) {
				throw new IllegalArgumentException("Encoding failed: OID '" + element.getKey() + "' cannot be defined twice.");
			} else {
				oids.add(element.getKey());
			}
			investigator.validateOid(element.getKey());
			Entry entry = new Entry(investigator, element.getKey(), element.getValue());
			if (!entry.validate()) {
				throw new IllegalArgumentException(
						"Encoding failed: The value '" + entry.value + "' is not conform with the GS1 General Specifiaction of OID '" + entry.oid + "'.");
			}
			entries.add(entry);
		}

		// checks if all required values are member of the list and places the
		// combinations in the right order.
		List<Entry> result = new ArrayList<Entry>();
		while (entries.size() > 0) {
			Entry entry = entries.get(0);
			if (entry.isSingleComponent()) {
				// add entry to list
				result.add(entry);
				// add ID Value to the list of base IDs
				baseIDValues.add(entry.baseTableIDValue);
				// if entry is in secondary table add secondary entry
				if (entry.hasSecondary()) {
					int secondaryIdValue = entry.secondaryTableIDValue;
					int tableSize = Entry.getTableSize(investigator, entry.secondaryTableID);
					int bits = DataTypeConverter.getNumberOfRequiredBits(tableSize - 1);
					secondaryIdSection.add(DataTypeConverter.convert(secondaryIdValue, 10, 2, bits));
				}
				entries.remove(entry);
			} else if (entry.isSeriesOfComponents()) {
				// add entry to list
				result.add(entry);
				// add ID Value to the list of base IDs
				baseIDValues.add(entry.baseTableIDValue);
				// if entry is in secondary table add secondary entry
				if (entry.hasSecondary()) {
					int secondaryIdValue = entry.secondaryTableIDValue;
					int tableSize = Entry.getTableSize(investigator, entry.secondaryTableID);
					int bits = DataTypeConverter.getNumberOfRequiredBits(tableSize - 1);
					secondaryIdSection.add(DataTypeConverter.convert(secondaryIdValue, 10, 2, bits));
				}
				List<String> components = entry.getSeriesComponents();
				int numberOfSeries = components.size();
				int indexOfSeries = components.indexOf(entry.getFinalArc());
				int bits = DataTypeConverter.convert(numberOfSeries, 10, 2, 0).length();
				secondaryIdSection.add(DataTypeConverter.convert(indexOfSeries, 10, 2, bits));

				entries.remove(entry);
			} else if (entry.isSelectionOfComponents()) {
				// Does not support a selection which is a combination e.g.
				// (123)(124) / (14)[51]
				result.add(entry);
				baseIDValues.add(entry.baseTableIDValue);
				if (entry.hasSecondary()) {
					int secondaryIdValue = entry.secondaryTableIDValue;
					int tableSize = Entry.getTableSize(investigator, entry.secondaryTableID);
					int bits = DataTypeConverter.getNumberOfRequiredBits(tableSize - 1);
					secondaryIdSection.add(DataTypeConverter.convert(secondaryIdValue, 10, 2, bits));
				}

				List<String> selections = entry.getSelectionComponents();
				int numberOfSelections = selections.size();
				for (int indexOfSelection = 0; indexOfSelection < selections.size(); indexOfSelection++) {
					if (Entry.isSeriesOfComponents(selections.get(indexOfSelection))) {
						List<String> seriesComponents = Entry.getSeriesComponents(selections.get(indexOfSelection));
						int numberOfSeries = seriesComponents.size();
						int indexOfSeries = seriesComponents.indexOf(entry.getFinalArc());
						if (indexOfSeries > -1) {
							int bits = DataTypeConverter.getNumberOfRequiredBits(numberOfSelections - 1);
							secondaryIdSection.add(DataTypeConverter.convert(indexOfSelection, 10, 2, bits));
							bits = DataTypeConverter.getNumberOfRequiredBits(numberOfSeries - 1);
							secondaryIdSection.add(DataTypeConverter.convert(indexOfSeries, 10, 2, bits));
							break;
						}
					} else {
						throw new UnsupportedOperationException("Encoding failed: Unsupported column format.");
					}
				}
				entries.remove(entry);
			} else if (entry.isCombinationComponent()) {
				baseIDValues.add(entry.baseTableIDValue);
				if (entry.hasSecondary()) {
					int secondaryIdValue = entry.secondaryTableIDValue;
					int tableSize = Entry.getTableSize(investigator, entry.secondaryTableID);
					int bits = DataTypeConverter.getNumberOfRequiredBits(tableSize - 1);
					secondaryIdSection.add(DataTypeConverter.convert(secondaryIdValue, 10, 2, bits));
				}
				List<String> combinationComponents = entry.getCombinationComponents();
				List<String> essentials = Entry.getEssentials(combinationComponents);
				// checks if all required components are member of the list.
				for (String essential : essentials) {
					if (Entry.isSingleComponent(essential)) {
						boolean found = false;
						for (Entry e : entries) {
							if (e.getFinalArc().equals(essential)) {
								result.add(e);
								entries.remove(e);
								found = true;
								break;
							}
						}
						if (!found) {
							throw new IllegalArgumentException(
									"Encoding failed: OID '" + entry.oid + "' requires OID '" + prefix + essential + "'.");
						}
					} else if (Entry.isSelectionOfComponents(essential)) {
						if (essentials.size() == 1) {
							result.add(entry);

							List<String> selections = Entry.getSelectionComponents(essential);
							int numberOfSelections = selections.size();
							int bits = DataTypeConverter.getNumberOfRequiredBits(numberOfSelections - 1);
							int indexOfSelection = selections.indexOf(entry.getFinalArc());
							secondaryIdSection.add(DataTypeConverter.convert(indexOfSelection, 10, 2, bits));

							boolean found = false;
							for (Entry e : entries) {
								for (String selection : selections) {
									if (e.getFinalArc().equals(selection)) {
										found = true;
										break;
									}
								}
								if (found == true) {
									break;
								}
							}
							if (!found) {
								String exception = "Encoding failed: OID '" + entry.oid + "' requires OID '" + prefix + selections.get(0) + "'";
								for (int i = 1; i < selections.size(); i++) {
									exception += " or '" + prefix + selections.get(i) + "'";
								}
								throw new IllegalArgumentException(exception);
							}
							entries.remove(entry);
						} else {
							throw new UnsupportedOperationException("Encoding failed: Unsupported column format.");
						}
					}
				}
				List<String> optionals = Entry.getOptionals(combinationComponents);
				Map<String, Character> flags = new HashMap<String, Character>();
				for (String optional : optionals) {
					flags.put(optional, '0');
					for (Entry e : entries) {
						if (e.getFinalArc().equals(optional)) {
							result.add(e);
							flags.put(optional, '1');
							entries.remove(e);
							break;
						}
					}
				}
				String flag = "";
				for (Map.Entry<String, Character> f : flags.entrySet()) {
					flag += f.getValue();
				}
				if (flag.length() > 0) {
					secondaryIdSection.add(flag);
				}
			} else {
				throw new UnsupportedOperationException("Encoding failed: Unsupported column format.");
			}

		}
		return result;
	}

	/**
	 * Format flags not supported. Only 00001000 may be used
	 * 
	 * @param exception
	 *            Set value {@code true} if length of the Packed-Object is < 4
	 * @return empty format string
	 * @see EPC Tag Data Standard v1.9 - I.4 Format Flags section<br>
	 *      and<br>
	 *      EPC Tag Data Standard v1.9 - I.4.2 Format Flag section starting bit
	 *      patterns<br>
	 *      and<br>
	 *      EPC Tag Data Standard v1.9 - I.4.3 IDLPO Format Flags
	 */
	private static String getFormatFlags(boolean exception) {
		if (exception) {
			return "00001000";
		} else {
			return "";
		}
	}

	/**
	 * Builds the EBV3 value of the number of IDs
	 * 
	 * @return The number of ID Values as EBV3 value
	 * @see EPC Tag Data Standard v1.9 - I.5.1.1 IDLPO default Object Info
	 *      format
	 */
	private static String getNumberOfIDsEBV3(int numberOfIDs) {
		return EBV.getEbvValue(EBV.EBV3, numberOfIDs - 1);
	}

	/**
	 * Concatenates the list of secondary section values.
	 * 
	 * @param secondaryIDs
	 * @return The binary secondary section.
	 */
	private static String getSecondaryIdSection(List<String> secondaryIDs) {
		String result = "";
		for (String value : secondaryIDs) {
			result += value;
		}
		return result;
	}

	/**
	 * Builds the binary ID Listing.
	 * 
	 * @param investigator
	 * @param dataFormat
	 * @param idValues
	 * @return The binary ID Listing section
	 * @see EPC Tag Data Standard v1.9 - I.5.4 ID Values representation in an ID
	 *      Value-list Packed Object<br>
	 *      and</br>
	 *      EPC Tag Data Standard v1.9 - I.5.3 General description of ID values
	 */
	private static String getIdListing(PackedObjectInvestigator investigator, int dataFormat, List<Integer> idValues) {
		int tableSize = Integer.valueOf(investigator.getHeader(Constants.getBaseTableID(dataFormat), TableHeader.K_ID_SIZE));
		for(int i = 0; i < Constants.MaxNumberOfTableEntries.length; i++) {
			if(tableSize <= Constants.MaxNumberOfTableEntries[i]){				
				int base =  Constants.BaseOfTableEntries[i];
				int pairBits = Constants.BitsOfPairIDValues[i];
				int singleBits = Constants.BitsOfSingleIDValue[i];
				String idListing = "";
				int numberOfIDs = idValues.size();
				for (int j = 0; j < numberOfIDs / 2; j++) {
					int idValue_1 = idValues.get(j * 2);
					int idValue_2 = idValues.get(j * 2 + 1);
					int value = idValue_1 * base + idValue_2;
					idListing += DataTypeConverter.convert(value, 10, 2, pairBits);
				}
				if (numberOfIDs % 2 > 0) {
					int idValue_1 = idValues.get(numberOfIDs - 1);
					idListing += DataTypeConverter.convert(idValue_1, 10, 2, singleBits);
				}
				return idListing;
			}
		}
		throw new IllegalArgumentException("Table size '" + tableSize + "' exceeds the maximum number of permitted table entries.");				
	}

	/**
	 * Builds the aux format section. Includes the variable length information
	 * of the entries.
	 * 
	 * @param entries
	 * @return The binary aux format section
	 * @see EPC Tag Data Standard v1.9 - I.7 Aux Format section<br>
	 *      and<br>
	 *      EPC Tag Data Standard v1.9 - I.7.2 Support for the Packed-Object
	 *      compaction method
	 */
	private static String getAuxFormatSection(List<Entry> entries) {
		// Packed-Object is the only supported method.
		String compactParam = "1";
		// auxiliaryInformation is not fully supported
		String auxiliaryInformation = "";
		String variableLength = "";

		Entry lastNonNumericEntry = Entry.getLastNonNumericEntry(entries);
		for (Entry entry : entries) {
			if (!entry.equals(lastNonNumericEntry)) {
				int range = entry.getRange();
				if (range > 0) {
					int relativeLength = entry.valueLength - entry.getMinLength();
					String length = "";
					if (range < 8 || range > 44) {
						int bits = DataTypeConverter.convert(range, 10, 2, 0).length();
						length = DataTypeConverter.convert(relativeLength, 10, 2, bits);
					} else {
						if (relativeLength < 15) {
							length = DataTypeConverter.convert(relativeLength, 10, 2, 4);
						} else if (relativeLength < 30) {
							relativeLength -= 15;
							length += "1111" + DataTypeConverter.convert(relativeLength, 10, 2, 4);

						} else if (relativeLength < 45) {
							relativeLength -= 30;
							length += "11111111" + DataTypeConverter.convert(relativeLength, 10, 2, 4);
						} else {
							relativeLength -= 44;
							length += "111111111111" + EBV.getEbvValue(EBV.EBV6, relativeLength);
						}
					}
					variableLength += length;
				}
			}
		}
		auxiliaryInformation += variableLength;
		return compactParam + auxiliaryInformation;
	}

	/**
	 * Builds the data section.
	 * 
	 * @param entries
	 * @return The binary data section
	 * @see EPC Tag Data Standard v1.9 - I.8 Data section
	 */
	private static String getDataSection(List<Entry> entries) {
		return getKnownLengthNumericSection(entries) + getAlphaNumericSection(entries);
	}

	/**
	 * Builds the known length numerics subsection.
	 * 
	 * @param entries
	 * @return The binary known length numerics subsection.
	 * @see EPC Tag Data Standard v1.9 - I.8.1 Known-length-Numerics subsection
	 *      of the Data Section
	 */
	private static String getKnownLengthNumericSection(List<Entry> entries) {
		String knownLengthNumeric = "";

		for (Entry entry : entries) {
			if (entry.isSingleNumeric()) {
				String value = entry.value;
				while (value.length() > 0) {
					int length = value.length() >= Constants.Base10.length ? Constants.Base10.length - 1 : value.length();
					knownLengthNumeric += DataTypeConverter.convert(value.substring(0, length), 10, 2, Constants.Base10[length]);
					value = value.substring(length);
				}
			} else if (!entry.isSingleNonNumeric()) {
				String value = entry.value;
				List<Map.Entry<String, Map.Entry<Integer, Integer>>> values = entry.getFormatValues();
				if ("n".equals(values.get(0).getKey())) {
					value = value.substring(0, values.get(0).getValue().getKey());
					while (value.length() > 0) {
						int length = value.length() >= Constants.Base10.length ? Constants.Base10.length - 1 : value.length();
						knownLengthNumeric += DataTypeConverter.convert(value.substring(0, length), 10, 2, Constants.Base10[length]);
						value = value.substring(length);
					}
				}
			}
		}

		return knownLengthNumeric;
	}

	/**
	 * Ascertains the required base of the data.
	 * 
	 * @param data
	 * @return The required base.
	 */
	private static int getNonNumericBase(String data) {
		int base = 30;
		for (int i = 0; i < data.length(); i++) {
			char c = data.charAt(i);
			if (base < 31 && Constants.indexOfChar(Constants.Base30BasicSet, c) > -1) {
				base = 30;
			} else if (base < 31 && Constants.indexOfChar(Constants.Base30Shift1Set, c) > -1) {
				base = 30;
			} else if (base < 31 && Constants.indexOfChar(Constants.Base30Shift2Set, c) > -1) {
				base = 30;
			} else if (base < 75 && Constants.indexOfChar(Constants.Base74Set, c) > -1) {
				if (base != 74) {
					base = 74;
					i = -1;
				}
			} else if (c < 257) {
				base = 256;
				break;
			} else {
				throw new UnsupportedOperationException("Encoding failed: Character " + c + " cannot be encoded.");
			}
		}
		return base;
	}

	/**
	 * Builds the character map
	 * 
	 * @param base
	 * @param value
	 * @return The binary character map.
	 * @see EPC Tag Data Standard v1.9 - I.8.2.2 Dual-base Character-map
	 *      encoding
	 */
	private static String getCharMap(int base, String value) {
		String charMap = "";
		switch (base) {
		case 30:
			for (int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				if (Constants.indexOfChar(Constants.Base10Set, c) > -1) {
					charMap += "0";
				} else if (Constants.indexOfChar(Constants.Base30BasicSet, c) > -1) {
					charMap += "1";
				} else if (Constants.indexOfChar(Constants.Base30Shift1Set, c) > -1) {
					charMap += "11";
				} else if (Constants.indexOfChar(Constants.Base30Shift2Set, c) > -1) {
					charMap += "11";
				} else {
					throw new IllegalArgumentException("Encoding failed: Character '" + value.charAt(i) + "' is not a member of base " + base + " set.");
				}
			}
			break;
		case 74:
			for (int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				if (Constants.indexOfChar(Constants.Base10Set, c) > -1) {
					charMap += "0";
				} else if (Constants.indexOfChar(Constants.Base74Set, c) > -1) {
					charMap += "1";
				} else {
					throw new IllegalArgumentException("Encoding failed: Character '" + value.charAt(i) + "' is not a member of base " + base + " set.");
				}
			}
			break;
		case 256:
			for (int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				if (Constants.indexOfChar(Constants.Base10Set, c) > -1) {
					charMap += "0";
				} else if (c < 256) {
					charMap += "1";
				} else {
					throw new IllegalArgumentException("Encoding failed: Character '" + value.charAt(i) + "' is not a member of base " + base + " set.");
				}
			}
			break;
		default:
			throw new IllegalArgumentException("Encoding failed: Base " + base + " is not supported.");
		}
		return charMap;
	}

	/**
	 * Builds the AN Header.
	 * 
	 * @param base
	 * @param charMap
	 * @param prefix
	 * @param suffix
	 * @return The AN header bits. Includes the character map.
	 * @see EPC Tag Data Standard v1.9 - I.8.2.1 A/N Header Bits
	 */
	private static String getANHeader(int base, String charMap, String prefix, String suffix) {
		String anHeader = "";
		switch (base) {
		case 30:
			anHeader += "0";
			break;
		case 74:
			anHeader += "10";
			break;
		case 256:
			anHeader += "11";
			break;
		default:
			throw new UnsupportedOperationException("Encoding failed: Base " + base + " is not valid in alpha-numeric header.");
		}
		anHeader += prefix + suffix + charMap;
		return anHeader;
	}

	/**
	 * Builds the alpha-numeric section.
	 * 
	 * <u>Note:</u> Does not support prefix and suffix runs.
	 * 
	 * @param entries
	 * @return The binary String of the data section
	 * @see EPC Tag Data Standard v1.9 - I.8 Data section<br>
	 *      and<br>
	 *      EPC Tag Data Standard v1.9 - I.8.2.4 Encoding into Binary Segments
	 *      <br>
	 *      and<br>
	 *      EPC Tag Data Standard v1.9 - I.8.2.3 Prefix and Suffix Run-Length
	 *      encoding
	 */
	@SuppressWarnings("unused")
	private static String getAlphaNumericSection(List<Entry> entries) {
		String result = "";
		String dataString = "";
		for (Entry entry : entries) {
			if (!entry.isSingleNumeric()) {
				if (entry.isSingleNonNumeric()) {
					dataString += entry.value;
				} else {
					List<Map.Entry<String, Map.Entry<Integer, Integer>>> values = entry.getFormatValues();
					dataString += entry.value.substring(values.get(0).getValue().getKey());
				}
			}
		}
		if (dataString.length() > 0) {
			Map<Integer, String> binaryDataSegments = new HashMap<Integer, String>();
			binaryDataSegments.put(10, "");
			binaryDataSegments.put(13, "");
			binaryDataSegments.put(30, "");
			binaryDataSegments.put(40, "");
			binaryDataSegments.put(74, "");
			binaryDataSegments.put(84, "");
			binaryDataSegments.put(256, "");

			// Determine prefix and suffix runs
			// !!! is not supported

			String nonNumericRest = "";
			for (int i = 0; i < dataString.length(); i++) {
				char c = dataString.charAt(i);
				if (Constants.indexOfChar(Constants.Base10Set, c) > -1) {
					binaryDataSegments.put(10, binaryDataSegments.get(10) + c);
					continue;
				} else {
					nonNumericRest += c;
				}
			}
			int base = getNonNumericBase(nonNumericRest);

			// build anHeader / no prefix runs / no suffix runs
			String anHeader = getANHeader(base, getCharMap(base, dataString), "0", "0");

			dataString = nonNumericRest;

			for (int i = 0; i < dataString.length(); i++) {
				char c = dataString.charAt(i);
				switch (base) {
				case 30:
					binaryDataSegments.put(30, binaryDataSegments.get(30) + c);
					break;
				case 74:
					binaryDataSegments.put(74, binaryDataSegments.get(74) + c);
					break;
				case 256:
					binaryDataSegments.put(256, binaryDataSegments.get(256) + c);
					break;
				default:
					throw new IllegalArgumentException("Encoding failed: Base " + base + " is not supported.");
				}
			}

			// add suffix runs to binaryDataSegments
			// !!! is not supported

			// build result(binary dataSegments)
			result += anHeader;

			// !!! is not supported. Only required for prefix and suffix runs
			String extendedNumeric = binaryDataSegments.get(13);
			// TODO Convert extendedNumeric to binary

			// !!! is not supported. Only required for prefix and suffix runs
			String extendedNonNumeric = "";
			if (base == 30 || base == 256) {
				extendedNonNumeric = binaryDataSegments.get(40);
				// TODO Convert extendedNonNumeric to binary
			} else if (base == 74) {
				extendedNonNumeric = binaryDataSegments.get(84);
				// TODO Convert extendedNonNumeric to binary
			} else {
				throw new IllegalArgumentException("Encoding failed: Base " + base + " is not supported.");
			}

			// base 10
			String base10 = extendedNonNumeric = binaryDataSegments.get(10);
			while (base10.length() > 0) {
				int length = base10.length() >= Constants.Base10.length ? Constants.Base10.length - 1 : base10.length();
				result += DataTypeConverter.convert(base10.substring(0, length), 10, 2, Constants.Base10[length]);
				base10 = base10.substring(length);
			}

			// nonNumeric binary
			if (base == 30) {
				String nonNumeric = binaryDataSegments.get(30);
				String base30 = "";
				for (int i = 0; i < nonNumeric.length(); i++) {
					int charIndex = -1;
					char c = nonNumeric.charAt(i);
					if ((charIndex = Constants.indexOfChar(Constants.Base30BasicSet, c)) > -1) {
						base30 += DataTypeConverter.convert(charIndex, 10, 30, 0);
					} else if ((charIndex = Constants.indexOfChar(Constants.Base30Shift1Set, c)) > -1) {
						base30 += DataTypeConverter.convert(27, 10, 30, 0) + DataTypeConverter.convert(charIndex, 10, 30, 0);
					} else if ((charIndex = Constants.indexOfChar(Constants.Base30Shift2Set, c)) > -1) {
						base30 += DataTypeConverter.convert(28, 10, 30, 0) + DataTypeConverter.convert(charIndex, 10, 30, 0);
					} else {
						throw new IllegalArgumentException("Encoding failed: Character '" + c + "' is not a member of base " + base + " set.");
					}
				}
				while (base30.length() > 0) {
					int length = base30.length() >= Constants.Base30.length ? Constants.Base30.length - 1 : base30.length();
					result += DataTypeConverter.convert(base30.substring(0, length), 30, 2, Constants.Base30[length]);
					base30 = base30.substring(length);
				}
			} else if (base == 74) {
				String nonNumeric = binaryDataSegments.get(74);
				while (nonNumeric.length() > 0) {
					int length = nonNumeric.length() >= Constants.Base74.length ? Constants.Base74.length - 1 : nonNumeric.length();
					result += DataTypeConverter.convertBase74ToBinary(nonNumeric.substring(0, length));
					nonNumeric = nonNumeric.substring(length);
				}
			} else if (base == 256) {
				String nonNumeric = binaryDataSegments.get(256);
				result += DataTypeConverter.byteArrayToBinaryString(nonNumeric.getBytes());
			} else {
				throw new IllegalArgumentException("Encoding failed: Base " + base + " is not supported.");
			}
		}
		return result;
	}

	/**
	 * Adds to the Packed-Object the length information and the pad indicator.
	 * 
	 * @param formatFlags
	 * @param object
	 * @return The complete Packed-Object.
	 * @see EPC Tag Data Standard v1.9 - I.5.2 Length Information
	 */
	public static String addLengthInformationAndPad(String formatFlags, String object) {
		String pad = "0";
		object = pad + object;
		int length = object.length();
		int bytes = length / 8 + (length % 8 > 0 ? 1 : 0);
		String lengthInformation = EBV.getEbvValue(EBV.EBV6, bytes);
		while (((formatFlags + lengthInformation + object).length() / 8
				+ ((formatFlags + lengthInformation + object).length() % 8 > 0 ? 1 : 0)) > bytes) {
			bytes++;
			lengthInformation = EBV.getEbvValue(EBV.EBV6, bytes);
		}
		if ((lengthInformation + object).length() % 8 > 0) {
			object = "1" + object.substring(1) + "1";
			while ((lengthInformation + object).length() % 8 > 0) {
				object += "0";
			}
		}
		return formatFlags + lengthInformation + object;
	}

	/**
	 * Encodes the PackedObjects.
	 * 
	 * @param investigator
	 * @param packedObjects
	 * @return The encoded Packed-Objects as hex-string
	 */
	public static String encode(PackedObjectInvestigator investigator, PackedObjects packedObjects) {
		byte[] dsfid = DataTypeConverter.hexStringToByteArray(packedObjects.getDsfid());

		Buffer buffer = new Buffer(dsfid);

		// READ DSFID
		int accessMethod = getAccessMethod(buffer);
		int extended = getExtendedBit(buffer);
		int dataFormat = getDataFormat(buffer);

		if (extended > 0) {
			throw new UnsupportedOperationException("Encoding failed: Extended bit is not supported.");
		}

		String result = DataTypeConverter.byteArrayToHexString(new byte[] { dsfid[0] });

		switch (accessMethod) {
		case 2: // Packed Objects
			switch (dataFormat) {
			case 9: // ean-ucc
				switch (packedObjects.getObjectInfoFormat()) {
				case IDLPO_DEFAULT:
					switch (packedObjects.getCompaction()) {
					case PACKED_OBJECT:
						List<Integer> baseIDValues = new ArrayList<Integer>();
						List<String> secondaryIDs = new ArrayList<String>();
						List<Entry> entries = prepare(investigator, dataFormat, packedObjects.getDataElements(), baseIDValues,
								secondaryIDs);
						if (entries.size() > 0) {
							String formatFlags = getFormatFlags(false);
							String numberOfIDsEBV3 = getNumberOfIDsEBV3(baseIDValues.size());
							String idListing = getIdListing(investigator, dataFormat, baseIDValues);
							String secondaryIdSection = getSecondaryIdSection(secondaryIDs);
							String auxFormatSection = getAuxFormatSection(entries);
							String dataSection = getDataSection(entries);

							String object = numberOfIDsEBV3 + idListing + secondaryIdSection + auxFormatSection + dataSection;

							object = addLengthInformationAndPad(formatFlags, object);
							if (formatFlags.isEmpty() && object.startsWith("0000")) { // special case
								formatFlags = getFormatFlags(true);
								object = numberOfIDsEBV3 + idListing + secondaryIdSection + auxFormatSection + dataSection;
								object = addLengthInformationAndPad(formatFlags, object);
							}

							// Convert to hex
							result += DataTypeConverter.convert(object, 2, 16, object.length() / 4 + (object.length() % 4 > 0 ? 1 : 0))
									.toUpperCase();

						}
						// Add termination pattern
						result += "00";
						break;
					default:
						throw new UnsupportedOperationException("Encoding failed: Unsupported compaction method.");
					}
					break;
				default:
					throw new UnsupportedOperationException("Encoding failed: Unsupported object info format");
				}
				break;
			default:
				throw new UnsupportedOperationException("Encoding failed: Dataformat " + dataFormat + " is not supported.");
			}
			break;
		default:
			throw new UnsupportedOperationException("Encoding failed: Access method " + accessMethod + " is not supported.");
		}
		return result;
	}

}