package havis.middleware.tdt;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

/**
 * Option property of EPCglobal Tag Data Translation (TDT) Specification. (For
 * more information see Document EPCglobal Tag Data Translation (TDT) 1.4,
 * Ratified Standard, June 10,2009. Latest version 1.4., chapter 3).
 */
public class OptionX {

	private Option option;

	/**
	 * Reference to the level definition this option belangs to
	 */
	private LevelX level;

	/**
	 * Precompiled regular expression
	 */
	private Pattern pattern;

	/**
	 * Indexed list of fields of this option. (Index is fieldname)
	 */
	private LinkedHashMap<String, FieldX> fields;

	OptionX(Option option, LevelX level) {
		this.option = option;
		this.level = level;
		init();
	}

	/**
	 * Unique ID of this option
	 */
	String getId() {
		return level.getId() + "." + option.getOptionKey();
	}

	/**
	 * Init Option object
	 */
	void init() {
		fields = new LinkedHashMap<String, FieldX>();
		for (Field field : option.getField()) {
			fields.put(field.getName(), new FieldX(field, this));
		}
		if (level.getType().equals(LevelTypeList.BINARY)) {
			initBitPositions();
		}
		initRegex();
	}

	/**
	 * If the level this option belongs to is type binary calculate for each
	 * field the starting bitposition in then input/output buffer
	 */
	private void initBitPositions() {
		int position = 0;
		for (String fieldname : option.getGrammar().split(" ")) {
			if (fieldname.startsWith("'")) {
				position += fieldname.length() - 2;
			} else {
				FieldX myField = fields.get(fieldname);
				myField.setBitPosition(position);
				position += myField.getIntBitLength();
			}
		}
	}

	/**
	 * Init regular expression for finding the matching option
	 */
	private void initRegex() {
		String pattern = option.getPattern();
		if ((pattern != null) && !pattern.isEmpty()) {
			this.pattern = Pattern.compile("^" + pattern + "$");
		}
	}

	public Collection<FieldX> getFields() {
		return fields.values();
	}

	/**
	 * Get the field object for the named field
	 * 
	 * @param fieldname
	 *            name of the requested field
	 */
	FieldX getField(String fieldname) {
		return fields.get(fieldname);
	}

	/**
	 * Check if this option matches the supplied epcIdentifier
	 * 
	 * @return Option option
	 */
	OptionX getEncodingOption(String epcIdentifier) {
		if (pattern.matcher(epcIdentifier).matches()) {
			return this;
		}
		return null;
	}

	LevelX getLevel() {
		return level;
	}

	String getOptionKey() {
		return option.getOptionKey();
	}

	String getGrammar() {
		return option.getGrammar();
	}

	Pattern getPattern() {
		return pattern;
	}
}