package havis.middleware.tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Load the Tag Data Translation (TDT) Scheme specification files into memory
 * and give read access to its components
 */
public class TdtDefinitions {
	/**
	 * Access the Binary Level of one scheme with the EPC Header field
	 */
	private HashMap<Byte, LevelX> listBinaryPrefix;

	/**
	 * List of all Tdt Definitions
	 */
	private ArrayList<EpcTagDataTranslationX> tdtDefinitions;

	public TdtDefinitions() {
		tdtDefinitions = new ArrayList<EpcTagDataTranslationX>();
		listBinaryPrefix = new HashMap<Byte, LevelX>();
	}

	public List<EpcTagDataTranslationX> getDefinitions() {
		return tdtDefinitions;
	}

	public void add(EpcTagDataTranslation translation) {
		tdtDefinitions.add(new EpcTagDataTranslationX(this, translation));
	}

	/**
	 * Get the matching option to the given epcIdentifier
	 *
	 * @param epcIdentifier
	 *            valid EPC identifier as urn string
	 * @param taglength
	 *            only needed if epcIdentifier not contains length information
	 *            about the tag, otherwise may be blank string
	 * @return matching option
	 */
	OptionX getEncodingOption(String epcIdentifier, int taglength) {
		OptionX option = null;
		for (EpcTagDataTranslationX epcTagDataTranslation : tdtDefinitions) {
			option = epcTagDataTranslation.getEncodingOption(epcIdentifier,
					taglength);
			if (option != null) {
				break;
			}
		}
		return option;
	}

	/**
	 * Get the matching binary level object for the EPC Header in buffer
	 *
	 * @param buffer
	 *            EPC field read from transponder
	 * @return matching binary level
	 */
	LevelX getLevelForBuffer(byte[] buffer) {
		return listBinaryPrefix.get(Byte.valueOf(buffer[0]));
	}

	/**
	 * Get the scheme object for the given name.
	 *
	 * @param name
	 *            scheme name, must contain tag length information
	 * @return The scheme
	 */
	SchemeX getSchemeByName(String name) {
		for (EpcTagDataTranslationX translation : tdtDefinitions) {
			for (SchemeX scheme : translation.getSchemes()) {
				if (scheme.getName().equals(name))
					return scheme;
				break;
			}
		}
		return null;
	}

	void putBinaryPrefix(byte parseShort, LevelX levelX) {
		listBinaryPrefix.put(Byte.valueOf(parseShort), levelX);
	}
}