package havis.middleware.tdt;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.client.GWT;

/**
 * <code>
 * {@link EpcTagDataTranslationCodec} codec = {@link EpcTagDataTranslationCodec}.INSTANCE;
 * <br /><br />
 * {@link TdtTranslator} translator = new {@link TdtTranslator}();
 * <br /><br />
 * {@link TdtDefinitions} definitions = translator.getTdtDefinitions();
 * <br /><br />
 * {@link EpcTagDataTranslation} translation;
 * <br /><br />
 * translation = codec.decode({@link TdtResources}.INSTANCE.sgtin96().getText());
 * <br />
 * definitions.add(translation);
 * <br /><br />
 * translation = codec.decode({@link TdtResources}.INSTANCE.grai96().getText());
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
public interface EpcTagDataTranslationCodec extends
		JsonEncoderDecoder<EpcTagDataTranslation> {

	public static final EpcTagDataTranslationCodec INSTANCE = GWT
			.create(EpcTagDataTranslationCodec.class);
}