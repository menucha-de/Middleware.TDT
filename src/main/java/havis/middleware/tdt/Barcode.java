package havis.middleware.tdt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Barcode {

	/**
	 * Group separator
	 */
	private final static char FNC1 = 29;

	private PackedObjectInvestigator investigator;

	/**
	 * Contains all OIDs for data format 9 (ean-ucc)
	 */
	private Set<String> oids;

	public Barcode(PackedObjectInvestigator investigator) {
		this.investigator = investigator;
		oids = investigator.getOids("urn:oid:1.0.15961.9").keySet();
	}

	public Barcode(String data) {
		this(new PackedObjectInvestigator(data));
	}

	public Barcode(String[] data) {
		this(new PackedObjectInvestigator(data));
	}

	/**
	 * Does not check for invalid characters or the check digit. The group
	 * separator is the ASCII character with value 29.
	 * 
	 * @param barcode
	 * @return List
	 * @throws IllegalArgumentException
	 *             If barcode is an invalid GS1-128 barcode
	 * @throws IOException
	 */
	public List<Map.Entry<String, String>> decodeGs1128Barcode(String barcode) throws IllegalArgumentException {
		if (barcode == null || barcode.trim().isEmpty())
			throw new IllegalArgumentException("An empty string is no valid barcode.");
		ArrayList<Map.Entry<String, String>> result = new ArrayList<Map.Entry<String, String>>();
		for (int i = 1, beginIndex = 0; i <= barcode.length(); i++) {
			// Get application identifier
			String ai = barcode.substring(beginIndex, i);
			String oid = "urn:oid:1.0.15961.9." + (ai.startsWith("0") ? ai.substring(1) : ai);
			boolean exists = false;
			if (ai.length() > 1 && (exists = oids.contains(oid))) {
				beginIndex = i;
				Entry entry = new Entry(investigator, oid);
				/* get min length */
				int min = entry.getMinLength();
				/* get max length */
				int max = entry.getMaxLength();
				// Prepare value variable
				String value;
				String exMessage = "Barcode is invalid. The value for the Application Identifier " + ai
						+ " does not conform the specification.";
				String exMessageMissingFnc1 = "Maybe the FNC1 character (ASCII value " + (int) FNC1 + ") is missing.";
				if (min == max) { // field with fixed length
					if (i + max > barcode.length())
						throw new IllegalArgumentException(
								exMessage + " Length should be " + max + " but is " + (barcode.length() - beginIndex) + ".");
					value = barcode.substring(beginIndex, i += max);
				} else { // field with variable length
					int fnc1 = barcode.indexOf(FNC1, i);
					if (fnc1 < 0)
						throw new IllegalArgumentException(exMessage + " " + exMessageMissingFnc1);
					if (min > fnc1 - beginIndex)
						throw new IllegalArgumentException(
								exMessage + " The length should be at least " + min + " but is " + (fnc1 - beginIndex) + ".");
					if (max < fnc1 - beginIndex)
						throw new IllegalArgumentException(exMessage + " The length should be at most " + max + " but is "
								+ (fnc1 - beginIndex) + ". " + exMessageMissingFnc1);

					value = barcode.substring(beginIndex, fnc1);
					i = ++fnc1;
				}
				// add entry to list
				result.add(new SimpleEntry<String, String>(ai, value));
				beginIndex = i;
			} else if (i - beginIndex > 4 || (i == barcode.length() && (i - beginIndex < 2 || !exists))) {
				throw new IllegalArgumentException(
						"Barcode is invalid. Application Identifier could not be found at position " + (beginIndex + 1) + ".");
			}
		}
		return result;
	}
}
