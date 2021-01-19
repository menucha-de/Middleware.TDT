package havis.middleware.tdt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class holds the level definition for one level in the tag data
 * definition (TDT). (For more information see Document EPCglobal Tag Data
 * Translation (TDT) 1.4, Ratified Standard, June 10,2009. Latest version 1.4.,
 * chapter 3).
 */
public class LevelX {

	private Level level;

	/**
	 * Reference to the scheme definition the level belongs to.
	 */
	private SchemeX scheme;

	/**
	 * Number of characters of the prefixMatch field
	 */
	// private int lengthPrefixMatch;

	/**
	 * Precompiled regular expression for the prefixMatch field
	 */
	// private Pattern regexPrefixMatch;
	private String prefixMatch;

	/**
	 * Helper table for fast access to the option keys
	 */
	private LinkedHashMap<String, OptionX> options;

	private List<RuleX> rules;

	LevelX(Level level, SchemeX scheme) {
		this.level = level;
		this.scheme = scheme;
		init();
	}

	/**
	 * The Id of the level
	 */
	String getId() {
		return scheme.getId() + "." + level.getType();
	}

	public Iterable<OptionX> getOptions() {
		return options.values();
	}

	Iterable<RuleX> getRules() {
		return rules;
	}

	/**
	 * Init this level object
	 */
	private void init() {
		options = new LinkedHashMap<String, OptionX>();
		for (Option option : level.getOption()) {
			options.put(option.getOptionKey(), new OptionX(option, this));
		}
		rules = new ArrayList<RuleX>();
		for (Rule rule : level.getRule()) {
			rules.add(new RuleX(rule, this));
		}
		addTableBinaryPrefix();
		initRegexPrefixMatch();
	}

	/**
	 * For faster access when input is byte array we build an access table
	 */
	private void addTableBinaryPrefix() {
		if (level.getType() != LevelTypeList.BINARY)
			return;
		// Add new entry for fast access to list of binary prefixes
		scheme.getEpcTagDataTranslation()
				.getTdtDefinitions()
				.putBinaryPrefix(
						(byte) Short.parseShort(level.getPrefixMatch(), 2),
						this);
	}

	/**
	 * Precompile regular expression for prefixMatch
	 */
	private void initRegexPrefixMatch() {
		prefixMatch = level.getPrefixMatch();
		if ((prefixMatch == null) || prefixMatch.isEmpty())
			return;
		// regexPrefixMatch = Pattern.compile(prefixMatch);
		// lengthPrefixMatch = prefixMatch.length();
	}

	/**
	 * Get the matching option for the given epcIdentifier
	 * 
	 * @param epcIdentifier
	 *            epcIdentifier as urn or bitstring
	 * @return Option option
	 */
	OptionX getEncodingOption(String epcIdentifier) {
		if (prefixMatch != null
				&& epcIdentifier.length() >= prefixMatch.length()) {
			if (epcIdentifier.startsWith(prefixMatch)) {
				for (OptionX loopOption : options.values()) {
					OptionX myOption = loopOption
							.getEncodingOption(epcIdentifier);

					if (myOption != null) {
						return myOption;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get the matching option for the given option key
	 * 
	 * @param optionKey
	 * @return Option option
	 */
	OptionX getOptionByOptionKey(String optionKey) {
		return options.get(optionKey);
	}

	public LevelTypeList getType() {
		return level.getType();
	}

	String getRequiredParsingParameters() {
		return level.getRequiredParsingParameters();
	}

	String getRequiredFormattingParameters() {
		return level.getRequiredFormattingParameters();
	}

	List<Rule> getRuleList() {
		return level.getRule();
	}

	SchemeX getScheme() {
		return scheme;
	}

	public String getPrefixMatch() {
		return level.getPrefixMatch();
	}
}