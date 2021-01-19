package havis.middleware.tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackedObjectInvestigator {

	final static String EOL = "\n";
	final static String DELIMITER = ",";

	private String[] data;
	private TreeMap<String, Integer[]> tableMap = new TreeMap<String, Integer[]>();

	private final static String PATTERN = // Delimiters.
	"(\\" + DELIMITER + "|\\r?\\n|\\r|^)" +
			// Quoted fields.
			"(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
			// Standard fields.
			"([^\"\\" + DELIMITER + "\\r\\n]*))";

	/**
	 * Pattern to validate an OID
	 */
	private final static Pattern OID = Pattern.compile("^urn:oid:\\d+(\\.\\d+)*(\\.\\*)?$");

	public PackedObjectInvestigator(String data) {
		if ((data == null) || (data.trim().length() == 0)) {
			throw new IllegalArgumentException("data must not be null or empty");
		}

		this.data = data.split(EOL);
		init();
	}

	public PackedObjectInvestigator(String[] data) {
		if ((data == null) || (data.length == 0)) {
			throw new IllegalArgumentException("data must not be null or empty");
		}

		this.data = data;
		init();
	}

	/**
	 * Returns the table header name value
	 * 
	 * @param tableID
	 *            TableID
	 * @param tableHeader
	 *            Table header name
	 * 
	 * @return table header name value
	 */
	public String getHeader(String tableID, TableHeader tableHeader) {
		String result = null;
		String delimiters = DELIMITER + DELIMITER + DELIMITER + DELIMITER + DELIMITER + DELIMITER;

		// Get information about where table content is starting
		Integer[] table = tableMap.get(tableID);

		if ((table != null) && (table.length == 2) && (tableHeader != null)) {
			// determine start index of table content minus the
			// tableHeader.getNum()
			int headerRow = table[0] - tableHeader.getNum();

			if ((headerRow >= 0) && (headerRow < data.length)) {
				String header = data[headerRow];

				if (header.contains(tableHeader.getValue())) {
					String[] headerKey = header.split("=");

					if (table.length == 2) {
						result = headerKey[1].replace("\"", "").replace(delimiters, "").trim();
					}
				}
			}
		}

		return result;
	}

	/**
	 * Returns the cell content of the given table name, column and line
	 * informations
	 * 
	 * @param tableID
	 *            TableID
	 * @param columnname
	 *            Column
	 * @param index
	 *            Line
	 * 
	 * @return Cell content
	 */
	public String getData(String tableID, ColumnName columnname, int index) {

		// Get information about where table content is starting
		Integer[] table = tableMap.get(tableID);
		String result = null;

		if ((table != null) && (table.length == 2) && (index > 0)) {
			// row number of the table
			int tableRow = table[0] + index;

			if ((tableRow < table[1]) && (tableRow < data.length)) {
				String rowContent = data[tableRow];
				String group;

				// find column
				Pattern regExp = Pattern.compile(PATTERN, 0);
				Matcher matcher = regExp.matcher(rowContent);

				for (int next = 0; matcher.find(); next++) {
					group = matcher.group();

					if (next == columnname.getNum()) {
						// column was found
						result = group;

						if (result != null) {
							result = result.replace("\"", "");
							result = result.startsWith(DELIMITER) ? result.replaceFirst(DELIMITER, "").replace("\"", "").trim()
									: result.trim();

							break;
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Remember TableID, in which line of the table is the beginning of the
	 * content
	 */
	private void init() {
		final String eq = "=";
		final String tableEndFlag = "TableEnd";

		for (int i = 0; i < data.length; i++) {
			String row = data[i];
			if (row.contains(TableHeader.K_TABLE_ID.getValue())) {
				String[] table = row.split(eq);

				if (table.length == 2) {
					// retrieve table id
					String tableID = table[1].replace(DELIMITER, "").trim();
					// determine table start index and switch i to this index
					// also
					int tableStart = i = i + TableHeader.K_TABLE_ID.getNum();

					// determine table end index
					while (i < (data.length - 1)) {
						i++;
						row = data[i];

						// is table end reached? ...
						if ((row.contains(tableEndFlag) && row.contains(tableID))) {
							// ... yes
							int tableEnd = i;
							// remember start and end index for table
							// 'trimmedTableID'
							tableMap.put(tableID, new Integer[] { tableStart, tableEnd });
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Validates the oid.
	 * 
	 * @param oid
	 * @return The number of the data format
	 * @throws UnsupportedOperationException
	 *             If the data format is not supported or is unknown.
	 * @throws IllegalArgumentException
	 *             If the oid does not conform the RFC 3061 specification.
	 */
	int validateOid(String oid) throws UnsupportedOperationException, IllegalArgumentException {
		if (oid == null)
			throw new IllegalArgumentException("The oid must not be null.");
		if (!OID.matcher(oid).find())
			throw new IllegalArgumentException("The oid is invalid. It does not conform the RFC 3061 specification.");
		// Remove prefix
		oid = oid.substring("urn:oid:".length());
		if (oid.startsWith("1.0.15434.") || oid.startsWith("1.0.6523.") || oid.startsWith("1.0.15459.") || oid.startsWith("1.0.15961.10.")
				|| oid.startsWith("1.0.15961.11.") || oid.startsWith("1.0.15961.12."))
			throw new UnsupportedOperationException("Unsupported data format.");
		else if (!oid.startsWith("1.0.15961.9."))
			throw new UnsupportedOperationException("Unknown data format.");
		return 9;
	}

	/**
	 * Returns a list of TableID and the index which is part of the OID entry.
	 * The first entry is the base table and the second entry is the secondary
	 * table if it is required.
	 * 
	 * @param oid
	 * @return list of OID entries
	 * @throws UnsupportedOperationException
	 *             If the oid contains an unknown or unsupported data format.
	 * @throws IllegalArgumentException
	 *             If the oid does not conform the RFC 3061 specification. If
	 *             oid does not exist.
	 */
	public List<Map.Entry<String, Integer>> getOidEntry(String oid) throws UnsupportedOperationException, IllegalArgumentException {
		int dataFormat = validateOid(oid);
		List<Map.Entry<String, Integer>> result;
		result = getOidEntry(dataFormat, Constants.getBaseTableID(dataFormat), oid.substring("urn:oid:1.0.15961.9.".length()));		
		if (result == null || result.size() < 1)
			throw new IllegalArgumentException("The OID '" + oid + "' does not exist.");
		return result;
	}

	/**
	 * Returns a Map of TableIDs and the index which is part of the OID entry.
	 * 
	 * @param tableId
	 * @param oid
	 * @return list of OID entries
	 */
	private List<Map.Entry<String, Integer>> getOidEntry(int dataFormat, String tableId, String finalArc) {
		switch (dataFormat) {
		case 9:// (ean-ucc)
			List<Map.Entry<String, Integer>> result = new ArrayList<Map.Entry<String, Integer>>();
			int tableSize = Integer.parseInt(getHeader(tableId, TableHeader.K_ID_SIZE));
			for (int i = 1; i <= tableSize; i++) {
				String oidsCell = getData(tableId, ColumnName.FORMAT_9_OIDS, i);
				if (oidsCell == null) {
					break;
				}
				if (Entry.hasSecondary(oidsCell)) {
					String secondaryTableId = "F9" + Entry.getSecondaryTableId(oidsCell);
					List<Map.Entry<String, Integer>> temp = getOidEntry(dataFormat, secondaryTableId, finalArc);
					if (temp != null) {
						result.add(new SimpleEntry<String, Integer>(tableId, i));
						result.addAll(temp);
						return result;
					}
				} else {
					if (Entry.containsOid(oidsCell, finalArc)) {
						result.add(new SimpleEntry<String, Integer>(tableId, i));
						return result;
					}
				}
			}
		default:
			return null;
		}
	}

	/**
	 * Verifies if the value fits the format string of the OID
	 * 
	 * @param oid
	 * @param value
	 * @return true if valid otherwise false
	 * @throws UnsupportedOperationException
	 *             If the oid contains an unknown or unsupported data format
	 * @throws IllegalArgumentException
	 *             If the oid does not conform the RFC 3061 specification. If
	 *             oid does not exist.
	 */
	public boolean validateDataElement(String oid, String value) throws UnsupportedOperationException, IllegalArgumentException {
		return new Entry(this, oid).validate(value);
	}

	/**
	 * Returns a list of all OIDs of the defined table and the requested column
	 * entries
	 * 
	 * @param rootOid
	 * @param columns
	 * @return A list of all OIDs of the defined table and the requested column
	 *         entries.
	 * @throws UnsupportedOperationException
	 *             If the data format is not supported or is unknown.
	 * @throws IllegalArgumentException
	 *             If the OID does not conform the RFC 3061 specification.
	 */
	public Map<String, Map<ColumnName, String>> getOids(String rootOid, ColumnName... columns)
			throws UnsupportedOperationException, IllegalArgumentException {
		int dataFormat = validateOid(rootOid + ".*");
		return getOids(dataFormat, Constants.getBaseTableID(dataFormat), columns);
	}

	/**
	 * Returns a list of all OIDs of the defined table and the requested column
	 * entries.
	 * 
	 * @param dataFormat
	 * @param tableId
	 * @param columns
	 * @return A list of all OIDs of the defined table and the requested column
	 *         entries
	 */
	private Map<String, Map<ColumnName, String>> getOids(int dataFormat, String tableId, ColumnName... columns) {
		if (dataFormat == 9) {
			String prefix = "urn:oid:1.0.15961.9.";
			int tableSize = Integer.parseInt(getHeader(tableId, TableHeader.K_ID_SIZE));
			Map<String, Map<ColumnName, String>> result = new LinkedHashMap<String, Map<ColumnName, String>>();
			for (int i = 1; i <= tableSize; i++) {
				String oidsCell = getData(tableId, ColumnName.FORMAT_9_OIDS, i);
				if (oidsCell == null) {
					break;
				}
				if (Entry.hasSecondary(oidsCell)) {
					String secondaryTableId = "F9" + Entry.getSecondaryTableId(oidsCell);
					Map<String, Map<ColumnName, String>> temp = getOids(dataFormat, secondaryTableId, columns);
					if (temp != null)
						result.putAll(temp);
				} else {
					Map<ColumnName, String> columnValues = new HashMap<ColumnName, String>();
					for (ColumnName c : columns)
						columnValues.put(c, getData(tableId, c, i));
					List<String> oids = Entry.getAllFinalArcs(oidsCell);
					for (String oid : oids) {
						result.put(prefix + oid, new HashMap<ColumnName, String>(columnValues));
					}
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * Decodes the given hex string
	 * 
	 * @param hex
	 * @return packed objects
	 */
	public PackedObjects decodePackedObjects(String hex) {
		return UserMemoryDecode.decode(this, hex);
	}

	/**
	 * Decodes the given data
	 * 
	 * @param data
	 * @return packed objects
	 */
	public PackedObjects decodePackedObjects(byte[] data) {
		return UserMemoryDecode.decode(this, data);
	}

	/**
	 * Encodes the packed objects.
	 * 
	 * @param packedObjects
	 *            The data which shall be encoded.
	 * @return The encoded hex string.
	 * @throws UnsupportedOperationException
	 *             The packedObjects specifies an unsupported or unknown data
	 *             format, or an unsupported object info format.
	 * @throws IllegalArgumentException
	 *             The packedObjects contains data elements with invalid values,
	 *             or data elements which not belongs to the specified data
	 *             format.
	 */
	public String encodePackedObjects(PackedObjects packedObjects) throws UnsupportedOperationException, IllegalArgumentException {
		return UserMemoryEncode.encode(this, packedObjects);
	}

	/**
	 * Return the format string which belongs to the specified oid.
	 * 
	 * @param oid
	 *            The OID
	 * @return The format string of the specified OID
	 * @throws UnsupportedOperationException
	 *             If the data format is not supported or is unknown.
	 * @throws IllegalArgumentException
	 *             If the oid does not conform the RFC 3061 specification. If
	 *             the oid does not exist.
	 */
	public String getFormatString(String oid) throws UnsupportedOperationException, IllegalArgumentException {
		return new Entry(this, oid).getFormatString();
	}

	public static enum ColumnName {
		FORMAT_9_AI_AIS("AI or AIs", 0),
		FORMAT_9_ID_VALUE("IDvalue", 1),
		FORMAT_9_OIDS("OIDs", 2),
		FORMAT_9_ID_STRING("IDstring", 3),
		FORMAT_9_NAME("Name", 4),
		FORMAT_9_DATA_TITLE("Data Title", 5),
		FORMAT_9_FORMAT_STRING("FormatString", 6);

		private String name;
		private Integer num;

		private ColumnName(String name, Integer num) {
			this.name = name;
			this.num = num;
		}

		public String getValue() {
			return name;
		}

		public Integer getNum() {
			return num;
		}
	}

	public static enum TableHeader {
		K_TEXT_MAIN("K-Text", 7),
		K_VERSION("K-Version", 6),
		K_ISO15434("K-ISO15434", 5),
		K_TEXT_SUB("K-Text", 4),
		K_TABLE_ID("K-TableID", 3),
		K_ROOT_OID("K-RootOID", 2),
		K_ID_SIZE("K-IDsize", 1);

		private String name;
		private Integer num;

		private TableHeader(String name, Integer num) {
			this.name = name;
			this.num = num;
		}

		public String getValue() {
			return name;
		}

		public Integer getNum() {
			return num;
		}
	}
}