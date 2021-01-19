package havis.middleware.tdt;

import java.util.HashMap;
import java.util.Set;

class TdtFields {

	/**
	 * Get a list of all Fieldnames
	 */
	private HashMap<String, TdtField> fields;

	/** Constructor */
	TdtFields() {
		fields = new HashMap<String, TdtField>();
	}

	/**
	 * Add one new fieldname - value pair to the list of fields, throws
	 * exception if fieldname exists
	 */
	void add(String fieldname, String value) {
		fields.put(fieldname, new TdtField(fieldname, value));
	}

	/**
	 * Add one new fieldname - value pair to the list of fields, replace it if
	 * it allready exists
	 */
	void addOrReplace(String fieldname, String value) {
		if (fields.containsKey(fieldname)) {
			fields.remove(fieldname);
		}
		add(fieldname, value);
	}

	/**
	 * Try to add one new fieldname - value pair to the list of fields, ignore
	 * call if one entry for fieldname exists
	 */
	void tryAdd(String fieldname, String value) {
		if (!fields.containsKey(fieldname)) {
			add(fieldname, value);
		}
	}

	/**
	 * Get the TdtField object for the given fieldname
	 */
	TdtField getField(String fieldname) {
		TdtField field = fields.get(fieldname);
		return field;
	}

	/**
	 * Get the value of the field as string
	 */
	String getFieldValue(String fieldname) {
		TdtField field = getField(fieldname);
		return (field == null) ? null : field.getValue();
	}

	/**
	 * Check if there is one entry for the fieldname
	 */
	boolean containsKey(String fieldname) {
		return fields.containsKey(fieldname);
	}

	/**
	 * Gets the field names.
	 * 
	 * @return The field names
	 */
	Set<String> getFieldNames() {
		return fields.keySet();
	}
}