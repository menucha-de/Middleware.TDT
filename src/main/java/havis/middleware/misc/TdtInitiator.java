package havis.middleware.misc;

import havis.middleware.tdt.EpcTagDataTranslation;
import havis.middleware.tdt.TdtDefinitions;
import havis.middleware.tdt.TdtTagInfo;
import havis.middleware.tdt.TdtTranslator;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <code>
 * {@link TdtTranslator} translator = new {@link TdtTranslator}();
 * <br /><br />
 * {@link TdtDefinitions} definitions = translator.getTdtDefinitions();
 * <br /><br />
 * {@link EpcTagDataTranslation} translation;
 * <br /><br />
 * translation = TdtInitiator.get(SCHEME.SGTIN_96);
 * <br />
 * definitions.add(translation);
 * <br /><br />
 * translation = TdtInitiator.get(SCHEME.GRAI_96);
 * <br />
 * definitions.add(translation);
 * <br /><br />
 * String uri = "(8003)042602574600881631";
 * <br /><br />
 * uri = translator.translate(uri, "filter=1;gs1companyprefixlength=9", "TAG_ENCODING");
 * <br /><br />
 * {@link TdtTagInfo} info = translator.translate(uri);
 * <br />
 * uri = info.getUriTag()
 * </code>
 */
public class TdtInitiator {

	public enum SCHEME {
		GDTI_96, GDTI_113, GIAI_64, GIAI_96, GIAI_202, GID_96, GRAI_64, GRAI_96, GRAI_170, GSRN_96, SGLN_64, SGLN_96, SGLN_195, SGTIN_64, SGTIN_96, SGTIN_198, SSCC_64, SSCC_96
	}

	public static EpcTagDataTranslation get(SCHEME scheme) throws IOException {
	    ObjectMapper mapper = new ObjectMapper();
		String name = "/"
				+ EpcTagDataTranslation.class.getPackage().getName().replace('.', '/')
				+ '/' + scheme.name().toLowerCase().replace('_', '-').concat(".json");
		InputStream stream = TdtInitiator.class.getResourceAsStream(name);
		try {
		    EpcTagDataTranslation translation =  mapper.readValue(stream, EpcTagDataTranslation.class);
			return translation;
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
}