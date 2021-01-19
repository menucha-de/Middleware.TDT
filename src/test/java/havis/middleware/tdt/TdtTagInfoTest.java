package havis.middleware.tdt;

import havis.middleware.misc.TdtInitiator;
import havis.middleware.misc.TdtInitiator.SCHEME;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TdtTagInfoTest {
	
	private static TdtTranslator tdtTranslator;
	private Map<String, String> fields = new LinkedHashMap<String, String>();

	
	@Before
	public void init() throws Exception {
		tdtTranslator = new TdtTranslator();

		for (SCHEME scheme : TdtInitiator.SCHEME.values())
			tdtTranslator.getTdtDefinitions().add(TdtInitiator.get(scheme));
	}
	
	@Test
	public void checkGetter() throws TdtTranslationException {
		final TdtTagInfo tagInfo = new TdtTagInfo(tdtTranslator.getTdtDefinitions());		
		final TdtEpc epc =  createEPC(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tagInfo, "tdtEpc", epc);
		
		//for isEpcGlobal = false
		//isEpcGlobal()
		Assert.assertFalse(tagInfo.isEpcGlobal());
		
		//getFields()
		Assert.assertNull(tagInfo.getFields());
		
		//getLength()
		Deencapsulation.setField(epc, "epcBinaryData", "0000");
		Assert.assertEquals(4, tagInfo.getLength());
		Deencapsulation.setField(epc, "epcBinaryData", null);
		
		//getEpcData()
		Assert.assertEquals("[-30, -1, -16, 64]", Arrays.toString((tagInfo.getEpcData())));
		
		//getUriId()
		Assert.assertEquals("urn:epc:raw:0", tagInfo.getUriId());
				
		//getUriTag()
		Assert.assertEquals("urn:epc:raw:0", tagInfo.getUriTag());
		
		//getUriLegacy()
		Assert.assertEquals("urn:epc:raw:0", tagInfo.getUriLegacy());
				
		//getUriLegacyAlt()
		Assert.assertEquals("urn:epc:raw:0", tagInfo.getUriLegacyAlt());
				
		//getUriLegacyAi()
		Assert.assertEquals("urn:epc:raw:0", tagInfo.getUriLegacyAi());
				
		//getUriOnsHostname()
		Assert.assertEquals("urn:epc:raw:0", tagInfo.getUriOnsHostname());
		
		//getUriRawHex()
		Assert.assertEquals("urn:epc:raw:0", tagInfo.getUriRawHex());
				
		//getUriRawDecimal()
		Assert.assertEquals("urn:epc:raw:0", tagInfo.getUriRawDecimal());
		

		//for isEpcGlobal = true
		tagInfo.setEpcGlobal(true);
		
		//getFields()
		Assert.assertEquals("foo", (tagInfo.getFields()).getFieldValue("boo"));
		
		//getUriBinary()
		Assert.assertEquals(fields.get("uriBinary"), tagInfo.getUriBinary());
		
		//getUriId()
		Assert.assertEquals(fields.get("uriId"), tagInfo.getUriId());
		
		//getUriTag()
		Assert.assertEquals(fields.get("uriTag"), tagInfo.getUriTag());
		
		//getUriLegacy()
		Assert.assertEquals(fields.get("uriLegacy"), tagInfo.getUriLegacy());
		
		//getUriLegacyAlt()
		Assert.assertEquals(fields.get("uriLegacyAlt"), tagInfo.getUriLegacyAlt());
		
		//getUriLegacyAi()
		Assert.assertEquals(fields.get("uriLegacyAi"), tagInfo.getUriLegacyAi());
		
		//getUriOnsHostname()
		Assert.assertEquals(fields.get("uriOnsHostname"), tagInfo.getUriOnsHostname());
		
		//getUriRawHex()
		Assert.assertEquals(fields.get("uriRawHexField"), tagInfo.getUriRawHex());
		
		//getUriRawDecimal()
		Assert.assertEquals(fields.get("uriRawDecimalField"), tagInfo.getUriRawDecimal());
		
		//getLength()
		Assert.assertEquals(32, tagInfo.getLength());		
	}
	
	@Test
	public void testDecodeEpcData() throws TdtTranslationException {
		final TdtTagInfo tagInfo = new TdtTagInfo(tdtTranslator.getTdtDefinitions());
		byte[] data = { (byte) 0xE2, (byte) 0xFF, (byte) 0xF0, (byte) 0x40 };
		
		tagInfo.decode(data);
		TdtEpc tdtEpc = Deencapsulation.getField(tagInfo, "tdtEpc");
		Assert.assertNotNull(tdtEpc);
		Assert.assertNotNull(Deencapsulation.getField(tagInfo, "tdtPc"));
		Assert.assertNotNull(Deencapsulation.getField(tagInfo, "tdtXpc"));
		Assert.assertNotNull(Deencapsulation.getField(tagInfo, "tdtTid"));
		Assert.assertFalse((boolean) Deencapsulation.getField(tagInfo, "isEpcGlobal"));
		Assert.assertEquals("Could not find encoding scheme", Deencapsulation.getField(tdtEpc, "statusText"));
		Assert.assertEquals("11100010111111111111000001000000", Deencapsulation.getField(tdtEpc, "epcIdentifier"));
	}
	
	@Test
	public void testDecodeAllBanks() throws TdtTranslationException {
		final TdtTagInfo tagInfo = new TdtTagInfo(tdtTranslator.getTdtDefinitions());
		byte[] data = { (byte) 0xE2, (byte) 0xFF, (byte) 0xF0, (byte) 0x40 };
		
		tagInfo.decode(data, data, data, data, data);
		
		TdtEpc tdtEpc = Deencapsulation.getField(tagInfo, "tdtEpc");
		Assert.assertNotNull(tdtEpc);
		Assert.assertEquals("Could not find encoding scheme", Deencapsulation.getField(tdtEpc, "statusText"));
		Assert.assertEquals("11100010111111111111000001000000", Deencapsulation.getField(tdtEpc, "epcIdentifier"));
		
		TdtPc tdtPc = Deencapsulation.getField(tagInfo, "tdtPc");
		Assert.assertNotNull(tdtPc);
		
		TdtXpc tdtXpc = Deencapsulation.getField(tagInfo, "tdtXpc");
		Assert.assertNotNull(tdtXpc);
		Assert.assertEquals(tdtPc, Deencapsulation.getField(tdtXpc, "tdtPc"));
		
		TdtTid tdtTid = Deencapsulation.getField(tagInfo, "tdtTid");
		Assert.assertNotNull(tdtTid);
		Assert.assertEquals(tdtPc, Deencapsulation.getField(tdtTid, "tdtPc"));
		Assert.assertEquals((short) 0xFFF, tdtTid.getMaskDesignerId());
		Assert.assertEquals((short) 0x040, tdtTid.getTagModelNumber());
	}
	
	@Test
	public void testDecodeEpcDataException(@Mocked final TdtEpc epc) throws TdtTranslationException {
		final TdtTagInfo tagInfo = new TdtTagInfo(tdtTranslator.getTdtDefinitions());
		final byte[] data = { (byte) 0xE2, (byte) 0xFF, (byte) 0xF0, (byte) 0x40 };
		
		new NonStrictExpectations() {{
			epc.decode(data);
			result = new Exception();
		}};
		
		tagInfo.decode(data);
		Assert.assertFalse((boolean) Deencapsulation.getField(tagInfo, "isEpcGlobal"));
	}
	
	@Test
	public void testDecodeUrnString() throws TdtTranslationException {
		final TdtTagInfo tagInfo = new TdtTagInfo(tdtTranslator.getTdtDefinitions());
		String epcIdentifier = "urn:epc:id:sgtin:123456789.9876.54321";
		String parameterList = "";
		
		tagInfo.decode(epcIdentifier, parameterList);
		Assert.assertTrue(tagInfo.isEpcGlobal());
	}
	
	@Test
	public void testDecodeParameterlist(@Mocked final TdtDefinitions definitions, @Mocked final SchemeX schemeX,
			@Mocked final LevelX levelX, @Mocked final OptionX optionX) throws TdtTranslationException {
		final TdtTagInfo tagInfo = new TdtTagInfo(tdtTranslator.getTdtDefinitions());
		String parameterList = "type=sgtin_96;optionKey=testKey";
		
		new NonStrictExpectations() {{
			definitions.getSchemeByName("sgtin_96");
			result = schemeX;
			
			schemeX.getLevel(LevelTypeList.TAG_ENCODING);
			result = levelX;
			
			levelX.getOptionByOptionKey("testKey");
			result = optionX;
			
			optionX.getGrammar();
			result = " ";
		}};
		
		tagInfo.decodeParam(parameterList);
		Assert.assertTrue(tagInfo.isEpcGlobal());
	}
	
	@Test
	public void testUriNotEpcGlobal() throws TdtTranslationException {
		final TdtTagInfo tagInfo = new TdtTagInfo(tdtTranslator.getTdtDefinitions());
		final TdtEpc epc =  createEPC(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tagInfo, "tdtEpc", epc);
		Deencapsulation.setField(epc, "epcIdentifier", "000000010000111");
		Deencapsulation.setField(epc, "epcBinaryData", "000000010000111");
		
		Assert.assertEquals("urn:epc:raw:15.0000000100001110", tagInfo.getUriBinary());		
	}
	
	@Test
	public void testUriNotEpcGlobalEpcIdentifierNull() throws TdtTranslationException {
		final TdtTagInfo tagInfo = new TdtTagInfo(tdtTranslator.getTdtDefinitions());
		final TdtEpc epc =  createEPC(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tagInfo, "tdtEpc", epc);
		
		Assert.assertEquals("urn:epc:raw:0", tagInfo.getUriBinary());	
	}

	private TdtEpc createEPC(TdtDefinitions tdtDefinitions) throws TdtTranslationException {
		TdtEpc epc = new TdtEpc(tdtDefinitions);
		String[] epcFields = {
				"uriId=urn:epc:id:sgtin:123456789.9876.54321",
				"uriTag=urn:epc:tag:sgtin:123456789.9876.54321",
				"uriBinary=00011100000000000000000000000000001100010011101010001110010100011",
				"uriLegacy=gdti=0073796251024651842211",
				"uriLegacyAlt=grai13=0073796251024651842211",
				"uriLegacyAi=(253)0073796251024651842211",
				"uriRawHexField=urn:epc:raw:96.x3330000019000c8000085a25",
				"uriOnsHostname=025102.0073796.sgtin.id.onsepc.com",
				"uriRawDecimalField=urn:epc:raw:96.3330000019000c8000085a25",
				};
		for (String field : epcFields){
			String[] keyValue = field.split("=");
			fields.put(keyValue[0], keyValue[1]);
			Deencapsulation.setField(epc ,keyValue[0], keyValue[1]);
		}
		
		TdtFields fields = new TdtFields();
		fields.add("boo", "foo");
		Deencapsulation.setField(epc, "fields", fields);
		
		Scheme scheme = new Scheme();
		scheme.setTagLength(new BigInteger("32"));		
		SchemeX schemeX = new SchemeX(scheme, new EpcTagDataTranslationX(tdtDefinitions, new EpcTagDataTranslation()));		
		LevelX inputLevel = new LevelX(new Level(), schemeX);
		Deencapsulation.setField(epc, "inputLevel", inputLevel);
		
		byte[] data = { (byte) 0xE2, (byte) 0xFF, (byte) 0xF0, (byte) 0x40 }; // [-30, -1, -16, 64]
		Deencapsulation.setField(epc, "epcData", data);
		return epc;
	}

	@Test
	public void getUriRawHex() throws TdtTranslationException {
		TdtTagInfo info = tdtTranslator.translate(new byte[] { (byte) 0xFE, (byte) 0xFE, (byte) 0xFE });
		Assert.assertEquals("urn:epc:raw:24.xFEFEFE", info.getUriRawHex());
	}

	@Test
	public void getUriRawDecimal() throws TdtTranslationException {
		TdtTagInfo info = tdtTranslator.translate(new byte[] { (byte) 0xFE, (byte) 0xFE, (byte) 0xFE });
		Assert.assertEquals("urn:epc:raw:24.16711422", info.getUriRawDecimal());
	}
	
	@Test
	public void getUriBinary() throws TdtTranslationException {
		TdtTagInfo info = tdtTranslator.translate(new byte[] { (byte) 0xFE, (byte) 0xFE, (byte) 0xFE });
		Assert.assertEquals("urn:epc:raw:24.111111101111111011111110", info.getUriBinary());
	}
}
