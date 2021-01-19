package havis.middleware.tdt;

import havis.middleware.tdt.PackedObjectInvestigator.ColumnName;
import havis.middleware.tdt.PackedObjectInvestigator.TableHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Entry {
	
	private final static String FORMAT_9_OID_PREFIX = "urn:oid:1.0.15961.9.";

	/**
	 * Example: 5*6an<br>
	 * <br>
	 * Group 0 = 5*6an<br>
	 * Group 1 = 5*6
	 * Group 2 = fix length<br>
	 * Group 3 = minimum length<br>
	 * Group 4 = maximum length<br>
	 * Group 5 = numeric or alpha-numeric
	 */
	private final static Pattern FIX_MIN_MAX_ALPHANUMERIC_PATTERN = Pattern.compile("((\\d+)|(\\d+)\\*(\\d+))(a?n)\\s?", 0);
	
	private final static Pattern IS_SINGLE_PATTERN = Pattern.compile("^\\s*\\d+\\s*$");
	
	private final static Pattern GET_SINGLE_PATTERN = Pattern.compile("^\\s*(\\d+)\\s*$", 0);
	
	private final static Pattern IS_SERIES = Pattern.compile("^\\s*\\d+%x\\d+\\-\\d+\\s*$");
	/**
	 * Example: 12%x31-39<br>
	 * <br>
	 * Group 0: 12%x31-39<br>
	 * Group 1: 12<br>
	 * Group 2: 31<br>
	 * Group 3: 39
	 */
	private final static Pattern GET_SERIES_PATTERN = Pattern.compile("^\\s*(\\d+)%x(\\d+)\\-(\\d+)\\s*$", 0);
	
	private final static Pattern IS_COMBINATION_PATTERN = Pattern.compile("^(\\s*(\\([\\s\\d\\/]+\\)|\\[[\\s\\d\\/]+\\])\\s*)+$");
	
	private final static Pattern GET_COMBINATION_PATTERN = Pattern.compile("\\([\\s\\d\\/]+\\)|\\[[\\s\\d\\/]+\\]", 0);
	
	private final static Pattern GET_COMBINATION_FORMAT_PATTERN = Pattern.compile("(\\([\\d\\/an\\s\\*]+\\)|\\[[\\dan\\s\\*]+\\])", 0);
	
	private final static String SINGLE_COMPONENT = "\\s*\\d+\\s*";
	
	private final static String SERIES_COMPONENT = "\\s*\\d+%x\\d+\\-\\d+\\s*";
	
	private final static String COMBINATION_COMPONENT = "(\\s*(\\([\\s\\d\\/]+\\)|\\[[\\s\\d\\/]+\\])\\s*)+";
	
	private final static Pattern GET_SELECTIONS_PATTERN = Pattern.compile("((" + SERIES_COMPONENT + ")|(" + COMBINATION_COMPONENT + ")|(" + SINGLE_COMPONENT + "))\\/?", 0);
	
	/**
	 * Only numeric chars are allowed
	 */
	private final static String NUMERIC_PATTERN = "[0-9]";
	/**
	 * See GS1 General Specifications Release 16.0 - Figure 7.11-1
	 */
	private final static String FORMAT_9_ALPHANUMERIC = "[!\"%&'\\(\\)\\*\\+,\\-\\.\\/0-9:;<=>\\?A-Z_a-z]";

	PackedObjectInvestigator investigator;

	private String baseTableID;

	String secondaryTableID;

	int baseTableIDValue = -1;

	int secondaryTableIDValue = -1;

	String oid;

	String value;

	int valueLength = -1;

	private ColumnName oidsColumn;
	
	private ColumnName formatStringColumn;
	
	private String formatString;

	private Pattern pattern;

	// List<N/AN, <MIN Length, MAX Length>>
	private List<Map.Entry<String, Map.Entry<Integer, Integer>>> formatValues;

	Entry(PackedObjectInvestigator investigator) {
		this.investigator = investigator;
	}

	Entry(PackedObjectInvestigator investigator, String oid) {
		this(investigator, oid, "");
	}

	Entry(PackedObjectInvestigator investigator, String oid, String value) {
		this.investigator = investigator;
		this.oid = oid;
		this.value = value;
		this.valueLength = value.length();
		init(oid);
	}

	private void init(String oid) {
		List<Map.Entry<String, Integer>> entries = investigator.getOidEntry(oid);
		setBaseTableID(entries.get(0).getKey());
		baseTableIDValue = entries.get(0).getValue();
		if (entries.size() > 1) {
			secondaryTableID = entries.get(1).getKey();
			secondaryTableIDValue = entries.get(1).getValue() - 1;
		}
	}

	/**
	 * @return The base table ID of this entry.
	 */
	String getBaseTableID() {
		return baseTableID;
	}

	/**
	 * Sets the base table ID of this entry.
	 * 
	 * @param baseTableID The base table ID of this entry.
	 */
	void setBaseTableID(String baseTableID) {
		this.baseTableID = baseTableID;		
		if(Constants.getBaseTableID(9).equals(baseTableID)) {
			oidsColumn = ColumnName.FORMAT_9_OIDS;
			formatStringColumn = ColumnName.FORMAT_9_FORMAT_STRING;
		} else {
			throw new UnsupportedOperationException("Packed Object ID Table with ID '" + baseTableID + "' is not supported.");
		}		
	}

	/**
	 * @return The final arc of the OID
	 */
	String getFinalArc() {
		return oid.split("\\.")[oid.split("\\.").length - 1];
	}

	/**
	 * @return The corresponding format string of the specified OID
	 */
	String getFormatString() {
		if (formatString == null) {
			String cellOIDs = null;
			String cellFormatString = null;
			if (secondaryTableID == null) {
				cellOIDs = investigator.getData(baseTableID, oidsColumn, baseTableIDValue);
				cellFormatString = investigator.getData(baseTableID, formatStringColumn, baseTableIDValue);
			} else {
				cellOIDs = investigator.getData(secondaryTableID, oidsColumn, secondaryTableIDValue + 1);
				cellFormatString = investigator.getData(secondaryTableID, formatStringColumn, secondaryTableIDValue + 1);
			}
			
			formatString = getFormatString(cellOIDs, cellFormatString, getFinalArc());

			formatValues = new ArrayList<>();
			Matcher matcher = FIX_MIN_MAX_ALPHANUMERIC_PATTERN.matcher(formatString);
			while (matcher.find()) {
				if (matcher.group(1) != null) {
					int min = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : Integer.parseInt(matcher.group(3));
					int max = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : Integer.parseInt(matcher.group(4));
					if ("n".equals(matcher.group(5))) {
						// numeric
						formatValues.add(
								new SimpleEntry<String, Map.Entry<Integer, Integer>>("n", new SimpleEntry<Integer, Integer>(min, max)));
					} else {
						// non numeric
						formatValues.add(
								new SimpleEntry<String, Map.Entry<Integer, Integer>>("an", new SimpleEntry<Integer, Integer>(min, max)));
					}
				} else {
					throw new UnsupportedOperationException("Format string is not supported.");
				}
				// FIX FOR GWT
				matcher.group();
			}
		}
		return formatString;
	}

	/**
	 * @param cellOIDs
	 * @param cellFormatString
	 * @param finalArc
	 * @return The format string of the {@code finalArc}
	 */
	private static String getFormatString(String cellOIDs, String cellFormatString, String finalArc) {
		if (isCombinationComponent(cellOIDs)) {
			List<String> oidsCombinationComponents = getCombinationComponents(cellOIDs);
			List<String> oidsCombinations = getEssentials(oidsCombinationComponents);
			oidsCombinations.addAll(getOptionals(oidsCombinationComponents));

			List<String> formatStringCombinationComponents = getCombinationComponentsFormatString(cellFormatString);
			List<String> formatStringCombinations = getEssentials(formatStringCombinationComponents);
			formatStringCombinations.addAll(getOptionals(formatStringCombinationComponents));
			int i = 0;
			for (; i < oidsCombinations.size(); i++) {
				List<String> oids = Entry.getAllFinalArcs(oidsCombinations.get(i));
				if (oids.contains(finalArc)) {
					break;
				}
			}
			return getFormatString(oidsCombinations.get(i), formatStringCombinations.get(i), finalArc);
		} else if (isSelectionOfComponents(cellOIDs)) {
			List<String> oidsSelections = getSelectionComponents(cellOIDs);
			List<String> formatStringSelections = getSelectionComponentsFormatString(cellFormatString);
			int i = 0;
			for (; i < oidsSelections.size(); i++) {
				List<String> oids = Entry.getAllFinalArcs(oidsSelections.get(i));
				if (oids.contains(finalArc)) {
					break;
				}
			}
			return getFormatString(oidsSelections.get(i), formatStringSelections.get(i), finalArc);
		} else {
			return cellFormatString;
		}
	}

	/**
	 * @param cellFormatString
	 * @return The list of format strings of the combinations
	 */
	private static List<String> getCombinationComponentsFormatString(String cellFormatString) {
		List<String> result = new ArrayList<String>();
		Matcher matcher = GET_COMBINATION_FORMAT_PATTERN.matcher(cellFormatString);
		while (matcher.find()) {
			if (matcher.group(1) != null)
				result.add(matcher.group(1));
			// FIX FOR GWT
			matcher.group();
		}
		return result;
	}

	/**
	 * @param cellFormatString
	 * @return The list of format strings of the combinations
	 */
	private static List<String> getSelectionComponentsFormatString(String cellFormatString) {
		List<String> result = new ArrayList<String>();
		String[] values = cellFormatString.split("\\/");
		for (String v : values)
			result.add(v);
		return result;
	}

	/**
	 * Validates the value.
	 * 
	 * @return {@code true} if value. {@code false} if invalid.
	 */
	boolean validate() {
		return validate(value);
	}

	/**
	 * Validates the value.
	 * 
	 * @param value
	 *            The value which shall be validated.
	 * @return {@code true} if value. {@code false} if invalid.
	 */
	boolean validate(String value) {
		// Determines and builds pattern for validation.
		if (pattern == null) {
			if (formatValues == null) {
				getFormatString();
			}
			String valuePattern = "^";
			for (Map.Entry<String, Map.Entry<Integer, Integer>> v : formatValues) {
				if ("n".equals(v.getKey())) {
					valuePattern += NUMERIC_PATTERN;
				} else if(Constants.getBaseTableID(9).equals(baseTableID)){
					valuePattern += FORMAT_9_ALPHANUMERIC;
				} else {
					valuePattern += "[\\.]";
				}
				valuePattern += "{" + v.getValue().getKey() + "," + v.getValue().getValue() + "}";
			}
			valuePattern += "$";
			pattern = Pattern.compile(valuePattern);
		}
		return pattern.matcher(value).find();
	}

	/**
	 * @return {@code true} if format string is defined as 13n or 2n or 70n and
	 *         so on
	 */
	boolean isSingleNumeric() {
		if (formatValues == null) {
			getFormatString();
		}
		if (formatValues.size() > 1) {
			return false;
		} else {
			if ("n".equals(formatValues.get(0).getKey())) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * @return {@code true} if format string is defined as 13an or 2an or 70an
	 *         and so on
	 */
	boolean isSingleNonNumeric() {
		if (formatValues == null) {
			getFormatString();
		}
		if (formatValues.size() > 1) {
			return false;
		} else {
			if ("an".equals(formatValues.get(0).getKey())) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Contains two entries if the format string defines a fixed length value
	 * followed by a variable length value e.g.<br>
	 * 14n 1*13an<br>
	 * The result would be [ {"n":{"14":"14"}}, {"an":{"1":"13"}} ]
	 * 
	 * @return List&lt;Entry&lt;N/AN, Entry&lt;MIN Length, MAX
	 *         Length&gt;&gt;&gt;
	 */
	List<Map.Entry<String, Map.Entry<Integer, Integer>>> getFormatValues() {
		if (formatValues == null) {
			getFormatString();
		}
		return formatValues;
	}

	/**
	 * @return The range (defined as the maximum length minus the minimum
	 *         length) of the value.
	 */
	int getRange() {
		getFormatValues();
		int range = 0;
		for (Map.Entry<String, Map.Entry<Integer, Integer>> v : formatValues) {
			// max - min
			range = v.getValue().getValue() - v.getValue().getKey();
		}
		return range;
	}

	/**
	 * @return The minimum length of the value.
	 */
	int getMinLength() {
		getFormatValues();
		int minLength = 0;
		for (Map.Entry<String, Map.Entry<Integer, Integer>> v : formatValues) {
			minLength += v.getValue().getKey();
		}
		return minLength;
	}

	/**
	 * @return The maximum length of the value.
	 */
	int getMaxLength() {
		getFormatValues();
		int maxLength = 0;
		for (Map.Entry<String, Map.Entry<Integer, Integer>> v : formatValues) {
			maxLength += v.getValue().getValue();
		}
		return maxLength;
	}

	/**
	 * @return {@code true} if entry refers to a secondary table.
	 */
	boolean hasSecondary() {
		if (secondaryTableID == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return {@code true} if OIDs cell contains a single component.
	 *         {@code false} if OIDs cell does not contain a single component.
	 */
	boolean isSingleComponent() {
		if (!hasSecondary()) {
			return isSingleComponent(investigator.getData(baseTableID, oidsColumn, baseTableIDValue));
		} else {
			return isSingleComponent(investigator.getData(secondaryTableID, oidsColumn, secondaryTableIDValue + 1));
		}
	}

	/**
	 * @return {@code true} if OIDs cell is a selection. {@code false} if OIDs
	 *         cell does not contain a selection.
	 */
	boolean isSelectionOfComponents() {
		if (!hasSecondary()) {
			return isSelectionOfComponents(investigator.getData(baseTableID, oidsColumn, baseTableIDValue));
		} else {
			return isSelectionOfComponents(investigator.getData(secondaryTableID, oidsColumn, secondaryTableIDValue + 1));
		}
	}

	/**
	 * @return The list of selection components
	 */
	List<String> getSelectionComponents() {
		if (!hasSecondary()) {
			return getSelectionComponents(investigator.getData(baseTableID, oidsColumn, baseTableIDValue));
		} else {
			return getSelectionComponents(investigator.getData(secondaryTableID, oidsColumn, secondaryTableIDValue + 1));
		}
	}

	/**
	 * @return {@code true} if cellOIDs is a series. {@code false} if cellOIDs
	 *         is not a series
	 */
	boolean isSeriesOfComponents() {
		if (!hasSecondary()) {
			return isSeriesOfComponents(investigator.getData(baseTableID, oidsColumn, baseTableIDValue));
		} else {
			return isSeriesOfComponents(investigator.getData(secondaryTableID, oidsColumn, secondaryTableIDValue + 1));
		}
	}

	/**
	 * @return The list of series values which are part of OIDs cell string or
	 *         null if OIDs cell does not contain a series.
	 */
	List<String> getSeriesComponents() {
		if (!hasSecondary()) {
			return getSeriesComponents(investigator.getData(baseTableID, oidsColumn, baseTableIDValue));
		} else {
			return getSeriesComponents(investigator.getData(secondaryTableID, oidsColumn, secondaryTableIDValue + 1));
		}
		
	}

	/**
	 * @return {@code true} if OIDs cell defines a combination component.
	 */
	boolean isCombinationComponent() {
		if (!hasSecondary()) {
			return isCombinationComponent(investigator.getData(baseTableID, oidsColumn, baseTableIDValue));
		} else {
			return isCombinationComponent(investigator.getData(secondaryTableID, oidsColumn, secondaryTableIDValue + 1));
		}
	}

	/**
	 * @return The list of combination components
	 */
	List<String> getCombinationComponents() {
		if (!hasSecondary()) {
			return getCombinationComponents(investigator.getData(baseTableID, oidsColumn, baseTableIDValue));
		} else {
			return getCombinationComponents(investigator.getData(secondaryTableID, oidsColumn, secondaryTableIDValue + 1));
		}
	}

	/**
	 * @param entry
	 * @return A partial copy
	 */
	private static Entry copy(Entry entry) {
		Entry result = new Entry(entry.investigator);
		result.baseTableID = entry.baseTableID;
		result.secondaryTableID = entry.secondaryTableID;
		result.baseTableIDValue = entry.baseTableIDValue;
		result.secondaryTableIDValue = entry.secondaryTableIDValue;
		result.oidsColumn = entry.oidsColumn;
		result.formatStringColumn = entry.formatStringColumn;
		result.oid = entry.oid;		
		return result;
	}

	@Override
	public String toString() {
		return "{" + "\t\n\"baseTableID\":\"" + this.baseTableID + "\"," + "\t\n\"baseTableIDValue\":\"" + this.baseTableIDValue + "\""
				+ "\t\n\"secondaryTableID\":\"" + this.secondaryTableID + "\"" + "\t\n\"secondaryTableIDValue\":\""
				+ this.secondaryTableIDValue + "\"" + "\t\n\"oid\":\"" + this.oid + "\"" + "\t\n\"value\":\"" + this.value + "\""
				+ "\t\n\"valueLength\":\"" + this.valueLength + "\"" + "\n}";
	}

	/**
	 * Ascertains all OIDs stored in the buffer. Reads secondary ID section if
	 * required.
	 * 
	 * @param investigator
	 * @param buffer
	 * @param dataFormat
	 * @param idValues
	 * @return All entries of the Packed-Object
	 */
	static List<Entry> getEntries(PackedObjectInvestigator investigator, Buffer buffer, int dataFormat, List<Integer> idValues) {
		List<Entry> entries = new ArrayList<Entry>();
		switch (dataFormat) {
		case 9:			
			for (Integer idValue : idValues) {		
				String cellOIDs = investigator.getData(Constants.getBaseTableID(dataFormat), ColumnName.FORMAT_9_OIDS, idValue);
		
				Entry entry = new Entry(investigator);
				entry.setBaseTableID(Constants.getBaseTableID(dataFormat));
				entry.baseTableIDValue = idValue;
		
				if (hasSecondary(cellOIDs)) {
					String secondaryTableID = "F9" + getSecondaryTableId(cellOIDs);
					entry.secondaryTableID = secondaryTableID;
		
					int tableSize = getTableSize(investigator, secondaryTableID);
					int bits = DataTypeConverter.getNumberOfRequiredBits(tableSize - 1);
					entry.secondaryTableIDValue = buffer.getInt(bits);
		
					cellOIDs = investigator.getData(secondaryTableID, ColumnName.FORMAT_9_OIDS, entry.secondaryTableIDValue + 1);
				}
		
				entries.addAll(getEntries(investigator, buffer, entry, cellOIDs));
			}
			for(Entry entry : entries){
				entry.oid = FORMAT_9_OID_PREFIX + entry.oid;
			}
			return entries;		
		default:
			throw new UnsupportedOperationException("Data format " + dataFormat + " is not supported.");
		}		
	}

	/**
	 * Ascertains all OIDs stored in the buffer. Reads secondary ID section if
	 * required.
	 * 
	 * @param investigator
	 * @param buffer
	 * @param entry
	 * @param cellOIDs
	 * @return All entries of the Packed-Object
	 */
	private static List<Entry> getEntries(PackedObjectInvestigator investigator, Buffer buffer, Entry entry, String cellOIDs) {
		List<Entry> entries = new ArrayList<Entry>();
		if (isSingleComponent(cellOIDs)) {
			entry.oid = getSingleComponent(cellOIDs);
			entries.add(entry);
			return entries;
		} else if (isSeriesOfComponents(cellOIDs)) {
			List<String> oids = getSeriesComponents(cellOIDs);
			int numberOfSeries = oids.size();
			int bits = DataTypeConverter.getNumberOfRequiredBits(numberOfSeries - 1);
			int indexOfSeries = buffer.getInt(bits);
			entry.oid = oids.get(indexOfSeries);
			entries.add(entry);
			return entries;
		} else if (isSelectionOfComponents(cellOIDs)) {
			List<String> selections = getSelectionComponents(cellOIDs);
			int bits = DataTypeConverter.getNumberOfRequiredBits(selections.size() - 1);
			String selection = selections.get(buffer.getInt(bits));
			entries.addAll(getEntries(investigator, buffer, entry, selection));
			return entries;
		} else if (isCombinationComponent(cellOIDs)) {
			List<String> combinationComponents = getCombinationComponents(cellOIDs);
			List<String> essentials = getEssentials(combinationComponents);
			for (String essential : essentials) {
				entries.addAll(getEntries(investigator, buffer, Entry.copy(entry), essential));
			}
			List<String> optionals = getOptionals(combinationComponents);
			for (String optional : optionals) {
				if (buffer.getInt(1) == 1) {
					entries.addAll(getEntries(investigator, buffer, Entry.copy(entry), optional));
				}
			}
			return entries;
		} else {
			throw new UnsupportedOperationException("Unknown OIDs column format.");
		}
	}

	/**
	 * @param cellOIDs
	 * @return {@code true} if {@code cellOIDs} refers to a secondary table.
	 *         {@code false} if {@code cellOIDs} does not refer to a secondary
	 *         table.
	 * @see EPC Tag Data Standard v1.9 - J.2.2 OIDs and IDstring columns
	 *      (Optional)
	 */
	static boolean hasSecondary(String cellOIDs) {
		if (cellOIDs.startsWith("K-Secondary = "))
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param cellOIDs
	 * @return The K-TableID of the secondary table
	 * @see EPC Tag Data Standard v1.9 - J.2.2 OIDs and IDstring columns
	 *      (Optional)
	 */
	static String getSecondaryTableId(String cellOIDs) {
		if (hasSecondary(cellOIDs)) {
			return cellOIDs.substring("K-Secondary = ".length());
		} else {
			return null;
		}
	}

	/**
	 * @param investigator
	 * @param kTableID
	 * @return The size of the secondary table or -1 it the base table entry
	 *         does not refer to a secondary table
	 */
	static int getTableSize(PackedObjectInvestigator investigator, String kTableID) {
		return Integer.valueOf(investigator.getHeader(kTableID, TableHeader.K_ID_SIZE));
	}

	/**
	 * @param cellOIDs
	 * @return {@code true} if {@code cellOIDs} contains a single component.
	 *         {@code false} if {@code cellOIDs} does not contain a single
	 *         component.
	 * @see EPC Tag Data Standard v1.9 - J.3.1 Semantics for OIDs, IDString, and
	 *      FormatString Columns
	 */
	static boolean isSingleComponent(String cellOIDs) {
		return IS_SINGLE_PATTERN.matcher(cellOIDs).find();
	}

	/**
	 * <u>Example:</u> 20
	 * 
	 * @param cellOIDs
	 * @return The single component value or null if {@code cellOIDs} does not
	 *         contain a single component.
	 * @see EPC Tag Data Standard v1.9 - J.3.1 Semantics for OIDs, IDString, and
	 *      FormatString Columns
	 */
	static String getSingleComponent(String cellOIDs) {
		if (isSingleComponent(cellOIDs)) {
			Matcher matcher = GET_SINGLE_PATTERN.matcher(cellOIDs);
			if (matcher.find())
				return matcher.group(1);
			else
				return null;
		} else {
			return null;
		}
	}

	/**
	 * <u>Example:</u> 330%x30-36
	 * 
	 * <u><b>NOTE:</b> This method does not completely support the possible
	 * values.</u>
	 * 
	 * @param cellOIDs
	 * @return {@code true} if {@code cellOIDs} is a series. {@code false} if
	 *         {@code cellOIDs} is not a series
	 * @see EPC Tag Data Standard v1.9 - J.3.1 Semantics for OIDs, IDString, and
	 *      FormatString Columns
	 */
	static boolean isSeriesOfComponents(String cellOIDs) {
		return IS_SERIES.matcher(cellOIDs).find();		
	}
	
	/**
	 * <u><b>NOTE:</b> This method does not completely support the possible
	 * values.</u>
	 * 
	 * @param cellOIDs
	 * @return The list of series values which are part of {@code cellOIDs} or
	 *         null if {@code cellOIDs} does not contain a series.
	 * @see EPC Tag Data Standard v1.9 - J.3.1 Semantics for OIDs, IDString, and
	 *      FormatString Columns
	 */
	static List<String> getSeriesComponents(String cellOIDs) {
		if (isSeriesOfComponents(cellOIDs)) {
			List<String> result = new ArrayList<String>();
			Matcher matcher = GET_SERIES_PATTERN.matcher(cellOIDs);			
			if (matcher.find()) {
				String prefix = matcher.group(1);
				int suffixStart = Integer.valueOf(matcher.group(2), 16);
				int suffixEnd = Integer.valueOf(matcher.group(3), 16);
				for (; suffixStart <= suffixEnd; suffixStart++) {
					result.add(prefix + (char) suffixStart);
				}
				return result;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * (A)/(B) is a selection<br>
	 * A/B is a selection<br>
	 * (A/B) is not a selection. First check combination
	 * 
	 * @param cellOIDs
	 * @return {@code true} if {@code cellOIDs} is a selection. {@code false} if
	 *         {@code cellOIDs} does not contain a selection.
	 */
	static boolean isSelectionOfComponents(String cellOIDs) {
		if (cellOIDs.contains("/")) {
			String[] values = cellOIDs.split("\\/");
			if (isSingleComponent(values[0]) || isSeriesOfComponents(values[0]) || isCombinationComponent(values[0])) {
				return true;
			}
		}
		return false;
		// ALTERNATIVE
//		String regexSingleComponent = "\\s*\\d+\\s*";
//		String regexSeriesComponent = "\\s*\\d+%x\\d+\\-\\d+\\s*";
//		String regexCombinationComponent = "(\\s*(\\([\\s\\d\\/]+\\)|\\[[\\s\\d\\/]+\\])\\s*)+";
//		String regex = "^(("+regexSeriesComponent+")|("+regexCombinationComponent+")|("+regexSingleComponent+"))(\\/(("+regexSeriesComponent+")|("+regexCombinationComponent+")|("+regexSingleComponent+")))+$";
//		// SET FLAG FOR GWT
//		Matcher matcher = Pattern.compile(regex, 0).matcher(cellOIDs);
//		if (matcher.find())
//			return true;
//		else
//			return false;
	}
	
	/**
	 * @param cellOIDs
	 * @return The list of selection components
	 */
	static List<String> getSelectionComponents(String cellOIDs) {
		if (isSelectionOfComponents(cellOIDs)) {
			List<String> result = new ArrayList<String>();
			Matcher matcher = GET_SELECTIONS_PATTERN.matcher(cellOIDs);
			while (matcher.find()) {
				if (matcher.group(1) != null)
					result.add(matcher.group(1));
				// FIX FOR GWT
				matcher.group();
			}
			return result;
		} else {
			return null;
		}
	}
	
	/**
	 * @param cellOIDs
	 * @return {@code true} if cellOIDs defines a combination component.
	 * @see EPC Tag Data Standard v1.9 - J.3.1 Semantics for OIDs, IDString, and
	 *      FormatString Columns
	 */
	static boolean isCombinationComponent(String cellOIDs) {
		return IS_COMBINATION_PATTERN.matcher(cellOIDs).find();		
	}
	
	/**
	 * @param cellOIDs
	 * @return The list of combination components
	 */
	static List<String> getCombinationComponents(String cellOIDs) {
		if (isCombinationComponent(cellOIDs)) {
			List<String> result = new ArrayList<String>();
			Matcher matcher = GET_COMBINATION_PATTERN.matcher(cellOIDs);
			while (matcher.find()) {
				if (matcher.group(0) != null)
					result.add(matcher.group(0));
				// FIX FOR GWT
				matcher.group();
			}
			return result;
		} else {
			return null;
		}
	}

	/**
	 * @param combinationComponents
	 * @return The list of essential components
	 */
	static List<String> getEssentials(List<String> combinationComponents) {
		if (combinationComponents == null || combinationComponents.size() < 1) {
			return null;
		} else {
			List<String> result = new ArrayList<String>();
			for (String v : combinationComponents) {
				if (v.startsWith("(") && v.endsWith(")"))
					result.add(v.substring(1, v.length() - 1));
			}
			return result;
		}
	}

	/**
	 * @param combinationComponents
	 * @return The list of optional components.
	 */
	static List<String> getOptionals(List<String> combinationComponents) {
		if (combinationComponents == null || combinationComponents.size() < 1) {
			return null;
		} else {
			List<String> result = new ArrayList<String>();
			for (String v : combinationComponents) {
				if (v.startsWith("[") && v.endsWith("]"))
					result.add(v.substring(1, v.length() - 1));
			}
			return result;
		}
	}

	/**
	 * Ascertains the last alphaNumeric
	 * 
	 * @param entries
	 *            The entries in the Packed-Object
	 * @return the last alphaNumeric or null
	 */
	static Entry getLastNonNumericEntry(List<Entry> entries) {
		Entry lastNonNumeric = null;
		for (Entry entry : entries) {
			if (entry.isSingleNumeric()) {
				continue;
			} else if (entry.isSingleNonNumeric()) {
				if (entry.getRange() > 0) {
					lastNonNumeric = entry;
				} else {
					lastNonNumeric = null;
				}
				continue;
			} else {
				List<Map.Entry<String, Map.Entry<Integer, Integer>>> values = entry.getFormatValues();
				for (Map.Entry<String, Map.Entry<Integer, Integer>> v : values) {
					if ("n".equals(v.getKey())) {
						continue;
					} else if ("an".equals(v.getKey())) {
						if (entry.getRange() > 0) {
							lastNonNumeric = entry;
						} else {
							lastNonNumeric = null;
						}
					}
				}
			}
		}
		return lastNonNumeric;
	}
	
	/**
	 * @return All members in {@code cellOIDs}
	 */
	static List<String> getAllFinalArcs(String cellOIDs){
		List<String> result = new ArrayList<String>();
		if(Entry.isSingleComponent(cellOIDs)){
			result.add(Entry.getSingleComponent(cellOIDs));			
			return result;			
		} else if(Entry.isSeriesOfComponents(cellOIDs)){
			List<String> series = Entry.getSeriesComponents(cellOIDs);
			for(String oid : series){
				result.add(oid);
			}			
			return result;			
		} else if(Entry.isSelectionOfComponents(cellOIDs)){
			List<String> selections = Entry.getSelectionComponents(cellOIDs);
			for(String selection :  selections){
				result.addAll(getAllFinalArcs(selection));
			}	
			return result;		
		} else if(Entry.isCombinationComponent(cellOIDs)){
			List<String> combinationComponents = Entry.getCombinationComponents(cellOIDs);
			List<String> essentials = Entry.getEssentials(combinationComponents);
			for (String essential : essentials) {
				result.addAll(getAllFinalArcs(essential));
			}
			List<String> optionals = Entry.getOptionals(combinationComponents);
			for (String optional : optionals) {
				result.addAll(getAllFinalArcs(optional));
			}
			return result;		
		}  else {
			throw new UnsupportedOperationException("Unknown OIDs column format.");
		}
	}
	
	/**
	 * @return {@code true} if finalarc is a member in the {@code cellOIDs}
	 */
	static boolean containsOid(String cellOIDs, String finalArc){
		if(Entry.isSingleComponent(cellOIDs)){
			if(Entry.getSingleComponent(cellOIDs).equals(finalArc)){
				return true;
			}
		} else if(Entry.isSeriesOfComponents(cellOIDs)){
			List<String> series = Entry.getSeriesComponents(cellOIDs);
			for(String oid : series){
				if(oid.equals(finalArc)){
					return true;
				}
			}		
		} else if(Entry.isSelectionOfComponents(cellOIDs)){
			List<String> selections = Entry.getSelectionComponents(cellOIDs);
			for(String selection :  selections){
				if(containsOid(selection, finalArc)){
					return true;
				}
			}				
		} else if(Entry.isCombinationComponent(cellOIDs)){
			List<String> combinationComponents = Entry.getCombinationComponents(cellOIDs);
			List<String> essentials = Entry.getEssentials(combinationComponents);
			for (String essential : essentials) {
				if(containsOid(essential, finalArc)){
					return true;
				}
			}
			List<String> optionals = Entry.getOptionals(combinationComponents);
			for (String optional : optionals) {
				if(containsOid(optional, finalArc)){
					return true;
				}
			}	
		}  else {
			throw new UnsupportedOperationException("Unknown OIDs column format.");
		}
		return false;
	}
}
