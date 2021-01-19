package havis.middleware.tdt;

/**
 * Decoded value for one field of the transponder
 */
class TdtField {

	private String name;
	private String value;

	/**
	 * Constructor
	 */
	TdtField(String name, String value) {
		this.name = name;
		this.value = value;
	}

	String getValue() {
		return value;
	}

	String getName() {
		return name;
	}

	@Override
    public String toString() {
		return "{'" + name + "' : '" + value + "'}";
	}
}