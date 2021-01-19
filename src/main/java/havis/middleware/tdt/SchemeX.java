package havis.middleware.tdt;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * The header entry for one scheme of the EPCglobal Tag Data Translation (TDS)
 * specification. (For more information see Document EPCglobal Tag Data
 * Translation (TDT) 1.4, Ratified Standard, June 10,2009. Latest version 1.4.,
 * chapter 3).
 */
public class SchemeX {

	private Scheme scheme;

	private int intTagLength;

	/**
	 * Helper table for fast access to level
	 */
	private LinkedHashMap<LevelTypeList, LevelX> levels;

	/**
	 * Reference to the top level object of the TDT-Specification
	 */
	private EpcTagDataTranslationX epcTagDataTranslation;

	SchemeX(Scheme scheme, EpcTagDataTranslationX epcTagDataTranslation) {
		this.scheme = scheme;
		this.epcTagDataTranslation = epcTagDataTranslation;
		init();
	}

	public Collection<LevelX> getLevels() {
		return levels.values();
	}

	/**
	 * Initialize object and all sub objects
	 */
	private void init() {
		intTagLength = scheme.getTagLength().intValue();
		levels = new LinkedHashMap<LevelTypeList, LevelX>();
		for (Level level : scheme.getLevel()) {
			levels.put(level.getType(), new LevelX(level, this));
		}
	}

	/**
	 * Unique ID of this scheme
	 */
	String getId() {
		return scheme.getName();
	}

	int getIntTagLength() {
		return intTagLength;
	}

	EpcTagDataTranslationX getEpcTagDataTranslation() {
		return epcTagDataTranslation;
	}

	String getOptionKey() {
		return scheme.getOptionKey();
	}

	String getName() {
		return scheme.getName();
	}

	/**
	 * Get the requested level object
	 */
	LevelX getLevel(LevelTypeList levelType) throws TdtTranslationException {
		try {
			return levels.get(levelType);
		} catch (Exception e) {
			throw new TdtTranslationException("Level '" + levelType
					+ "' not found in scheme '" + getId() + "'");
		}
	}

	/**
	 * Get the requested object object
	 * 
	 * @param levelType
	 * @param getOptionKey
	 * @return Object object
	 * @throws TdtTranslationException
	 */
	OptionX getOption(LevelTypeList levelType, String getOptionKey)
			throws TdtTranslationException {
		LevelX level = getLevel(levelType);
		for (OptionX option : level.getOptions()) {
			if (option.getOptionKey().equals(getOptionKey)) {
				return option;
			}
		}
		return null;
	}

	/**
	 * Get the Object object from the epcIdentifier
	 * 
	 * @param epcIdentifier
	 * @return Object object
	 */
	OptionX getEncodingOption(String epcIdentifier) {
		for (LevelX level : levels.values()) {
			OptionX option = level.getEncodingOption(epcIdentifier);
			if (option != null)
				return option;
		}
		return null;
	}
}