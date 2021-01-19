package havis.middleware.tdt;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the header information for one Scheme of the EPCglobal Tag
 * Data Translation (TDT) Definition Markup Files. The first partial class was
 * generated from the EpcTagDataTranslation.xsd file. (For more information see
 * Document EPCglobal Tag Data Translation (TDT) 1.4, Ratified Standard, June
 * 10,2009. Latest version 1.4., chapter 3).
 */
public class EpcTagDataTranslationX {

	private EpcTagDataTranslation translation;

	/**
	 * Reference to the main TDT-Definitions object
	 */
	private TdtDefinitions definitions;

	private List<SchemeX> schemes;

	EpcTagDataTranslationX(TdtDefinitions definitions,
			EpcTagDataTranslation translation) {
		this.definitions = definitions;
		this.translation = translation;

		init();
	}

	/**
	 * Call initialization Methods of all members
	 * 
	 * @param definitions
	 *            The definitions
	 */
	private void init() {
		schemes = new ArrayList<SchemeX>();
		for (Scheme loopScheme : translation.getScheme()) {
			schemes.add(new SchemeX(loopScheme, this));
		}
	}

	/**
	 * Gets the option object for one EPC-Identifier string
	 * 
	 * @param epcIdentifier
	 *            EPC-identifier as URN-string
	 * @param taglength
	 *            For EPC-identifiers not containing information about the
	 *            desired taglength (legacy-encoding, pure-id ...) this
	 *            information must be given in the parameter taglength,
	 *            otherwise (tag-encoding, binary) this parameter may be 0.
	 * @return requested Option object
	 */
	OptionX getEncodingOption(String epcIdentifier, int taglength) {
		for (SchemeX scheme : schemes) {
			if ((taglength == 0) || (scheme.getIntTagLength() == taglength)) {
				return scheme.getEncodingOption(epcIdentifier);
			}
		}
		return null;
	}

	TdtDefinitions getTdtDefinitions() {
		return definitions;
	}

	public Iterable<SchemeX> getSchemes() {
		return schemes;
	}
}
