package havis.middleware.tdt;

import havis.middleware.misc.TdtInitiator;
import havis.middleware.misc.TdtInitiator.SCHEME;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;

import javax.xml.bind.DatatypeConverter;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TdtEpcTest {

	private static TdtTranslator tdtTranslator;	

	@Before
	public void init() throws Exception {
		tdtTranslator = new TdtTranslator();

		for (SCHEME scheme : TdtInitiator.SCHEME.values())
			tdtTranslator.getTdtDefinitions().add(TdtInitiator.get(scheme));
	}

	@Test
	public void getUriRawHex() throws TdtTranslationException {
		String uri = "urn:epc:tag:sgtin-96:3.0614141.812345.6789";
		TdtTagInfo info = tdtTranslator.translate(uri);
		Assert.assertEquals("urn:epc:raw:96.x3074257BF7194E4000001A85",
				info.getUriRawHex());
	}
	
	@Test
	public void getUriRawHexNotGlobal() throws TdtTranslationException {
		TdtTagInfo info = tdtTranslator.translate(DatatypeConverter
				.parseHexBinary("445566778899AABBCCDDEEFF"));
		Assert.assertEquals("urn:epc:raw:96.x445566778899AABBCCDDEEFF",
				info.getUriRawHex());
	}

	@Test
	public void getUriRawDecimal() throws TdtTranslationException {
		String uri = "urn:epc:tag:sgtin-96:3.0614141.812345.6789";
		TdtTagInfo info = tdtTranslator.translate(uri);
		Assert.assertEquals("urn:epc:raw:96.14995692880814596164774009477",
				info.getUriRawDecimal());
	}

	@Test
	public void getUriRawHexFromBinary() throws TdtTranslationException {
		String uri = "001100000111010000100101011110111111011100011001010011100100000000000000000000000001101010000101";
		TdtTagInfo info = tdtTranslator.translate(uri);
		Assert.assertEquals("urn:epc:raw:96.x3074257BF7194E4000001A85",
				info.getUriRawHex());
	}
	
	@Test
	public void testgetTdtFields() {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");
		fields.add("testfield", "0001");
		Deencapsulation.setField(tdtEpc, "fields", fields);
		
		TdtFields currFields = tdtEpc.getTdtFields();
		Assert.assertEquals(currFields.getFieldNames(), fields.getFieldNames());
	}
	
	@Test
	public void testgetUriIdWithNotNull() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "uriId", "urn:epc:id:sgtin:01234.5678.9");
		
		String uriId = tdtEpc.getUriId();
		Assert.assertEquals("urn:epc:id:sgtin:01234.5678.9", uriId);
	}
	
	@Test
	public void testgetUriIdWithNull() throws TdtTranslationException {
		String uri = "001100000111010000100101011110111111011100011001010011100100000000000000000000000001101010000101";
		TdtTagInfo info = tdtTranslator.translate(uri);
		Assert.assertEquals("urn:epc:id:sgtin:0614141.812345.6789",
				info.getUriId());
	}
	
	@Test
	public void testgetUriTagWithNotNull() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "uriTag", "urn:epc:tag:grai-96:1.234.56789.98765");
		
		String uriTag = tdtEpc.getUriTag();
		Assert.assertEquals("urn:epc:tag:grai-96:1.234.56789.98765", uriTag);
	}
	
	@Test
	public void testgetUriTagWithNull() throws TdtTranslationException {
		String uri = "001100000111010000100101011110111111011100011001010011100100000000000000000000000001101010000101";
		TdtTagInfo info = tdtTranslator.translate(uri);
		Assert.assertEquals("urn:epc:tag:sgtin-96:3.0614141.812345.6789",
				info.getUriTag());
	}
	
	@Test
	public void testgetUriLegacyWithNotNull() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "uriLegacy", "gdti=0073796251024651842211");
		
		String uriLegacy = tdtEpc.getUriLegacy();
		Assert.assertEquals("gdti=0073796251024651842211", uriLegacy);
	}

	@Test
	public void testgetUriLegacyWithNull() throws TdtTranslationException {
		String uri = "001100000111010000100101011110111111011100011001010011100100000000000000000000000001101010000101";
		TdtTagInfo info = tdtTranslator.translate(uri);
		Assert.assertEquals("gtin=80614141123458;serial=6789",
				info.getUriLegacy());
	}
	
	@Test
	public void testgetUriLegacyAltWithNotNull() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "uriLegacyAlt", "grai13=0073796251024651842211");
		
		String uriLegacyAlt = tdtEpc.getUriLegacyAlt();
		Assert.assertEquals("grai13=0073796251024651842211", uriLegacyAlt);
	}
	
	@Test
	public void testgetUriLegacyAltWithNull() throws TdtTranslationException {
		String uri = "urn:epc:tag:grai-96:1.00000050.2437.547365";
		TdtTagInfo info = tdtTranslator.translate(uri);
		Assert.assertEquals("grai13=0000005024377547365",
				info.getUriLegacyAlt());
	}
	
	@Test
	public void testgetUriLegacyAiWithNotNull() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "uriLegacyAi", "(253)0073796251024651842211");
		
		String uriLegacyAi = tdtEpc.getUriLegacyAi();
		Assert.assertEquals("(253)0073796251024651842211", uriLegacyAi);
	}
	
	@Test
	public void testgetUriLegacyAiWithNull() throws TdtTranslationException {
		String uri = "urn:epc:tag:grai-96:1.00000050.2437.547365";
		TdtTagInfo info = tdtTranslator.translate(uri);
		Assert.assertEquals("(8003)00000005024377547365",
				info.getUriLegacyAi());
	}
	
	@Test
	public void testgetUriOnsHostnameWithNotNull() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "uriOnsHostname", "025102.0073796.sgtin.id.onsepc.com");
		
		String uriOnsHostname = tdtEpc.getUriOnsHostname();
		Assert.assertEquals("025102.0073796.sgtin.id.onsepc.com", uriOnsHostname);
	}
	
	@Test
	public void testgetUriOnsHostnameWithNull() throws TdtTranslationException {
		String uri = "001100000111010000100101011110111111011100011001010011100100000000000000000000000001101010000101";
		TdtTagInfo info = tdtTranslator.translate(uri);
		Assert.assertEquals("812345.0614141.sgtin.id.onsepc.com",
				info.getUriOnsHostname());
	}
	
	@Test
	public void testgetUriRawHexNotEpcGlobalWithNotNull() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		String epcIdentifier = "001110100111010000000100100000010001000011000100";
		byte[] epcData;
		
		int len = epcIdentifier.length() / 8
				+ (((epcIdentifier.length() % 8) != 0) ? 1 : 0);
		epcData = new byte[len];
		StringBuilder sb = new StringBuilder();
		if (len % 8  > 0) {
			sb.append(Utils.padRight(epcIdentifier, (len * 8), '0'));
		}
		String s = (sb.length() != 0) ? sb.toString() : epcIdentifier;
		for (int i = 0; i < len; i++) {
			String sub = s.substring(i * 8, i * 8 + 8);
			epcData[i] = (byte) Short.parseShort(sub, 2);
		}
		
		Deencapsulation.setField(tdtEpc, "epcData", epcData);
	
		String uriRawHexNotEpcGlobal = tdtEpc.getUriRawHexNotEpcGlobal();
		Assert.assertEquals("urn:epc:raw:48.x3a7404ffffff8110ffffffc4", uriRawHexNotEpcGlobal);
	}
	
	@Test
	public void testgetLength(@Mocked final LevelX levelx) {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "inputLevel", levelx);
		
		new NonStrictExpectations() {{
			levelx.getScheme().getIntTagLength();
			result = 1;
		}};
		
		int len = tdtEpc.getLength();
		Assert.assertEquals(1, len);
	}
	
	@Test
	public void testgetStatusText() {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "statusText", "SUCCESS");
		
		Assert.assertEquals("SUCCESS", tdtEpc.getStatusText());
	}
	
	@Test
	public void testgetEpcData() throws TdtTranslationException, UnsupportedEncodingException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		
		String uriBinary = "0001011100000001000011110010";
		Deencapsulation.setField(tdtEpc, "uriBinary", uriBinary);

		byte[] epcData = tdtEpc.getEpcData();
		String sEpcData = Arrays.toString(epcData);
		Assert.assertEquals("[23, 1, 15, 32]", sEpcData);
	}
	
	@Test
	public void testdecodeTwoStringsWithEpcGlobal() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(new TdtDefinitions());
		
		tdtEpc.decode("00011010101101000011010101011001", "");
		Assert.assertEquals("00011010101101000011010101011001", Deencapsulation.getField(tdtEpc, "epcIdentifier"));
		Assert.assertEquals("", Deencapsulation.getField(tdtEpc, "parameterList"));
	}
	
	@Test
	public void testdecodeByteArrayInputLevelNull() {
		final TdtEpc tdtEpc = new TdtEpc(new TdtDefinitions());
		byte[] epcData = {1,2,3};
		
		tdtEpc.decode(epcData);		
		Assert.assertEquals("Could not find encoding scheme", Deencapsulation.getField(tdtEpc, "statusText"));
	}
	
	@Test
	public void testdecodeByteArrayEpcIdentifierLengthSmaller() {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		byte[] epcData = {58,60,4,127,32,111,5,26,87};		
		
		tdtEpc.decode(epcData);
		
		Assert.assertEquals("001110100011110000000100011111110010000001101111000001010001101001010111", Deencapsulation.getField(tdtEpc, "epcIdentifier"));
		Assert.assertNotNull(Deencapsulation.getField(tdtEpc, "inputLevel"));
		Assert.assertEquals("Could not find encoding scheme", Deencapsulation.getField(tdtEpc, "statusText"));
	}
	
	@Test
	public void testdecodeByteArrayEpcIdentifierLengthGreater(@Mocked final TdtDefinitions tdtDefinitions) {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final byte[] epcData = {58,60,4,127,32,111,5,26,87};
		
		final Scheme scheme = new Scheme();
		scheme.setTagLength(new BigInteger("70"));
		final LevelX levelX = new LevelX(new Level(), new SchemeX(scheme, new EpcTagDataTranslationX(new TdtDefinitions(), new EpcTagDataTranslation())));
		
		new NonStrictExpectations() {{
			tdtDefinitions.getLevelForBuffer(epcData);
			result = levelX;
		}};
		
		tdtEpc.decode(epcData);
		Assert.assertEquals("001110100011110000000100011111110010000001101111000001010001101001010111".substring(0,70), Deencapsulation.getField(tdtEpc, "epcIdentifier"));
	}
	
	@Test
	public void testdecodeByteArrayInputOptionNull(@Mocked final TdtDefinitions tdtDefinitions) {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final byte[] epcData = {58,60,4,127,32,111,5,26,87};
		
		final Scheme scheme = new Scheme();
		scheme.setTagLength(new BigInteger("70"));
		final LevelX levelX = new LevelX(new Level(), new SchemeX(scheme, new EpcTagDataTranslationX(new TdtDefinitions(), new EpcTagDataTranslation())));
		
		new NonStrictExpectations() {{
			tdtDefinitions.getLevelForBuffer(epcData);
			result = levelX;
		}};
		
		tdtEpc.decode(epcData);
		Assert.assertEquals("Could not find encoding scheme", Deencapsulation.getField(tdtEpc, "statusText"));
	}

	@Test 
	public void testdecodeByteArrayOptionTagEncodingPadRight(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final LevelX levelX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);		
		final byte[] epcData = {58,60,4,127,32,111,5,26,87};
		
		final Scheme scheme = new Scheme();
		scheme.setTagLength(new BigInteger("70"));
		
		Field iOptionField = new Field();
		iOptionField.setName("test");
		iOptionField.setCharacterSet("test");
		iOptionField.setCompaction(CompactionMethodList._32BIT);
		iOptionField.setPadDir(PadDirectionList.RIGHT);
		iOptionField.setLength(new BigInteger("32"));
		iOptionField.setPadChar("0");
		
		Option iOption = new Option();
		iOption.getField().add(iOptionField);
		
		final OptionX inputOption = new OptionX(iOption, new LevelX(new Level(), new SchemeX(scheme, new EpcTagDataTranslationX(tdtDefinitions, new EpcTagDataTranslation()))));
		FieldX inputOptionFieldX = inputOption.getField("test");
		inputOptionFieldX.setBitPosition(0);
		inputOptionFieldX.setIntBitLength(32); 
		
		final OptionX optionTagEncoding = new OptionX(iOption, new LevelX(new Level(), new SchemeX(scheme, new EpcTagDataTranslationX(tdtDefinitions, new EpcTagDataTranslation()))));
		
		new NonStrictExpectations() {{
			tdtDefinitions.getLevelForBuffer(epcData);
			result = levelX;
			
			levelX.getScheme().getIntTagLength();
			result = 70;
			
			levelX.getEncodingOption(anyString);
			result = inputOption;
					
			levelX.getScheme().getLevel(LevelTypeList.TAG_ENCODING).getOptionByOptionKey(anyString);
			result = optionTagEncoding;
		}};
		
		tdtEpc.decode(epcData);
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");

		Assert.assertTrue(fields.containsKey("test"));	
		Assert.assertEquals("97701183900000000000000000000000", fields.getFieldValue("test"));
		Assert.assertEquals(32, fields.getFieldValue("test").length());
		Assert.assertTrue((boolean) Deencapsulation.getField(tdtEpc, "isEpcGlobal"));
	}
	
	@Test 
	public void testdecodeByteArrayOptionTagEncodingPadLeft(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final LevelX levelX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);		
		final byte[] epcData = {58,60,4,127,32,111,5,26,87};
		
		final Scheme scheme = new Scheme();
		scheme.setTagLength(new BigInteger("70"));
		
		Field iOptionField = new Field();
		iOptionField.setName("test");
		iOptionField.setCharacterSet("test");
		iOptionField.setCompaction(CompactionMethodList._32BIT);
		iOptionField.setPadDir(PadDirectionList.LEFT);
		iOptionField.setLength(new BigInteger("32"));
		iOptionField.setPadChar("0");
		
		Option iOption = new Option();
		iOption.getField().add(iOptionField);
		
		final OptionX inputOption = new OptionX(iOption, new LevelX(new Level(), new SchemeX(scheme, new EpcTagDataTranslationX(tdtDefinitions, new EpcTagDataTranslation()))));
		FieldX inputOptionFieldX = inputOption.getField("test");
		inputOptionFieldX.setBitPosition(0);
		inputOptionFieldX.setIntBitLength(32); 
		
		final OptionX optionTagEncoding = new OptionX(iOption, new LevelX(new Level(), new SchemeX(scheme, new EpcTagDataTranslationX(tdtDefinitions, new EpcTagDataTranslation()))));
		
		new NonStrictExpectations() {{
			tdtDefinitions.getLevelForBuffer(epcData);
			result = levelX;
			
			levelX.getScheme().getIntTagLength();
			result = 70;
			
			levelX.getEncodingOption(anyString);
			result = inputOption;
					
			levelX.getScheme().getLevel(LevelTypeList.TAG_ENCODING).getOptionByOptionKey(anyString);
			result = optionTagEncoding;
		}};
		
		tdtEpc.decode(epcData);
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");

		Assert.assertTrue(fields.containsKey("test"));	
		Assert.assertEquals("00000000000000000000000977011839", fields.getFieldValue("test"));
		Assert.assertEquals(32, fields.getFieldValue("test").length());
		Assert.assertTrue((boolean) Deencapsulation.getField(tdtEpc, "isEpcGlobal"));
	}
	
	@Test
	public void testdecodeByteArrayTdtTranslationException(@Mocked final TdtDefinitions tdtDefinitions) {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);		
		final byte[] epcData = {58,60,4,127,32,111,5,26,87};
		
		new NonStrictExpectations() {{
			tdtDefinitions.getLevelForBuffer(epcData);
			result = new TdtTranslationException("Test");
		}};	
		
		tdtEpc.decode(epcData);
		Assert.assertEquals("TDT-Error : Test", Deencapsulation.getField(tdtEpc, "statusText"));	
	}
	
	@Test
	public void testdecodeByteArrayException(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final LevelX levelX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);		
		final byte[] epcData = {58,60,4,127,32,111,5,26,87};
		
		final Scheme scheme = new Scheme();
		scheme.setTagLength(new BigInteger("70"));
		
		final Field field = new Field();
		field.setCharacterSet("abc");
		field.setPadDir(PadDirectionList.LEFT);
		
		final Option option = new Option();
		option.setOptionKey("");
		
		final OptionX optionX = new OptionX(option, new LevelX(new Level(),new SchemeX(scheme, new EpcTagDataTranslationX(new TdtDefinitions(), new EpcTagDataTranslation()))));
		LinkedHashMap<String, FieldX> hashMap = new LinkedHashMap<String, FieldX>();
		hashMap.put("test", new FieldX(field, new OptionX(new Option(), new LevelX(new Level(), new SchemeX(scheme, new EpcTagDataTranslationX(new TdtDefinitions(), new EpcTagDataTranslation()))))));
		Deencapsulation.setField(optionX, "fields", hashMap);
		
		final FieldX testfield = new FieldX(field, optionX);
		testfield.setBitPosition(32);
		testfield.setIntBitLength(16);
		
		new NonStrictExpectations() {{
			tdtDefinitions.getLevelForBuffer(epcData);
			result = levelX;
			
			levelX.getScheme().getIntTagLength();
			result = 70;
			
			levelX.getEncodingOption(anyString);
			result = optionX;
		}};
		
		tdtEpc.decode(epcData);
		Assert.assertEquals("Exception Type java.lang.NumberFormatException", Deencapsulation.getField(tdtEpc, "statusText"));
	}
	
	@Test
	public void testdecodeParamNoValue() {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());	
		String parameterList = "test=";
		try {
			tdtEpc.decodeParam(parameterList);
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Value for parameter 'test' is missing in the parameter list", e.getMessage());
		}
	}
	
	@Test
	public void testdecodeParamMissingType() {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());	
		String parameterList = "serial=123";
		try {
			tdtEpc.decodeParam(parameterList);
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Parameter 'type' missing in parameter list! Only accepting parameter list, e.g. type=GRAI-96;gs1companyprefixlength=8;filter=1;gs1companyprefix=0050;assettype=2437;serial=547365;", e.getMessage());
		}
	}
	
	@Test
	public void testdecodeParamSchemeNotFound() {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());	
		String parameterList = "type=TEST";
		try {
			tdtEpc.decodeParam(parameterList);
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Scheme 'TEST' not found", e.getMessage());
		}
	}
	
	@Test
	public void testdecodeParamMissingParameter(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final SchemeX schemeX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		String parameterList = "type=SGTIN-96;gs1companyprefix=0400";
		
		Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("test");
		
		Option option = new Option();
		option.getField().add(field);
		option.setPattern("test");
		
		Level level = new Level();
		level.getOption().add(option);
		level.setType(LevelTypeList.TAG_ENCODING);
		
		final LevelX levelX = new LevelX(level, schemeX);
		
		new NonStrictExpectations() {{
			tdtDefinitions.getSchemeByName("SGTIN-96");
			result = schemeX;
			
			schemeX.getLevel(LevelTypeList.TAG_ENCODING);
			result = levelX;
			
			schemeX.getOptionKey();
			result = null;
		}};
		
		try {
			tdtEpc.decodeParam(parameterList);
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Parameter 'testfield' is missing", e.getMessage());
		}		
	}
	
	@Test
	public void testdecodeParamNoOptionKeySuppliedNoMatch(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final SchemeX schemeX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		String parameterList = "type=SGTIN-96;gs1companyprefix=0400;testfield=test";
		
		Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("test");
		
		Option option = new Option();
		option.getField().add(field);
		option.setPattern("test");
		option.setGrammar("'00011100'");
		
		Level level = new Level();
		level.getOption().add(option);
		level.setType(LevelTypeList.TAG_ENCODING);
		
		final LevelX levelX = new LevelX(level, schemeX);
			
		new NonStrictExpectations() {{
			tdtDefinitions.getSchemeByName("SGTIN-96");
			result = schemeX;
				
			schemeX.getLevel(LevelTypeList.TAG_ENCODING);
			result = levelX;
				
			schemeX.getOptionKey();
			result = null;
		}};
		
		tdtEpc.decodeParam(parameterList);		
	}
	
	@Test
	public void testdecodeParamNoOptionKeySuppliedMatchWithValidOption(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final SchemeX schemeX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		String parameterList = "type=SGTIN-96;gs1companyprefix=0400;testfield=test;testfieldname=00011100";
		
		Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("test");
		
		Field field2 = new Field();
		field2.setName("testfieldname");
		field2.setCharacterSet("test");
		field2.setPadDir(PadDirectionList.LEFT);
		field2.setLength(new BigInteger("0"));
		
		Option option = new Option();
		option.getField().add(field);
		option.getField().add(field2);
		option.setPattern("test");
		option.setGrammar("testfieldname");
		
		Level level = new Level();
		level.getOption().add(option);
		level.setType(LevelTypeList.TAG_ENCODING);
		
		final LevelX levelX = new LevelX(level, schemeX);
			
		new NonStrictExpectations() {{
			tdtDefinitions.getSchemeByName("SGTIN-96");
			result = schemeX;
				
			schemeX.getLevel(LevelTypeList.TAG_ENCODING);
			result = levelX;
				
			schemeX.getOptionKey();
			result = null;
		}};
		try {
			tdtEpc.decodeParam(parameterList);
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : No matching option found for parameter list", e.getMessage());
		}	
			
	}

	@Test
	public void testdecodeParamOptionNotFound(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final SchemeX schemeX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		String parameterList = "type=SGTIN-96;gs1companyprefix=0400;optionKey=testKey";
		
		Field field = new Field();
		field.setName("testKey");
		field.setCharacterSet("test");
		
		Option option = new Option();
		option.getField().add(field);
		option.setPattern("test");
		
		Level level = new Level();
		level.getOption().add(option);
		level.setType(LevelTypeList.TAG_ENCODING);
		
		final LevelX levelX = new LevelX(level, schemeX);
		
		new NonStrictExpectations() {{
			tdtDefinitions.getSchemeByName("SGTIN-96");
			result = schemeX;
			
			schemeX.getLevel(LevelTypeList.TAG_ENCODING);
			result = levelX;
		}};
		
		try {
			tdtEpc.decodeParam(parameterList);
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : No matching option found for parameter list", e.getMessage());
		}		
	}

	@Test
	public void testdecodeParamConstuctURNWithParams(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final SchemeX schemeX,
			@Mocked final LevelX levelX, @Mocked final OptionX optionX, @Mocked final FieldX fieldX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);		
		String parameterList = "type=SGTIN-96;serial=153;optionKey=testKey;testfield=000111001111;";
		
		new NonStrictExpectations() {{
			tdtDefinitions.getSchemeByName("SGTIN-96");
			result = schemeX;
			
			schemeX.getLevel(LevelTypeList.TAG_ENCODING);
			result = levelX;
			
			levelX.getOptionByOptionKey("testKey");
			result = optionX;
			
			optionX.getGrammar();
			result = "testfield";
			
			optionX.getField("testfield");
			result = fieldX;
			
			fieldX.getPadDir();
			result = PadDirectionList.LEFT;
			
			fieldX.getIntLength();
			result = 32;
			
			fieldX.getPadChar();
			result = '0';
		}};
		
		
		tdtEpc.decodeParam(parameterList);
		Assert.assertTrue((boolean) Deencapsulation.getField(tdtEpc, "isEpcGlobal"));
		
		byte[] epcData = Deencapsulation.getField(tdtEpc, "epcData");
		String epcDataString = Arrays.toString(epcData);		
		Assert.assertEquals("[0, 0, 1, -49]", epcDataString);
	}
	
	@Test
	public void testdecodeParamConstuctURNWithParamsPadRight(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final SchemeX schemeX,
			@Mocked final LevelX levelX, @Mocked final OptionX optionX, @Mocked final FieldX fieldX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);		
		String parameterList = "type=SGTIN-96;serial=153;optionKey=testKey;testfield=000111001111;";
		
		new NonStrictExpectations() {{
			tdtDefinitions.getSchemeByName("SGTIN-96");
			result = schemeX;
			
			schemeX.getLevel(LevelTypeList.TAG_ENCODING);
			result = levelX;
			
			levelX.getOptionByOptionKey("testKey");
			result = optionX;
			
			optionX.getGrammar();
			result = "testfield";
			
			optionX.getField("testfield");
			result = fieldX;
			
			fieldX.getPadDir();
			result = PadDirectionList.RIGHT;
			
			fieldX.getIntLength();
			result = 32;
			
			fieldX.getPadChar();
			result = '0';
		}};
		
		
		tdtEpc.decodeParam(parameterList);
		Assert.assertTrue((boolean) Deencapsulation.getField(tdtEpc, "isEpcGlobal"));
		
		byte[] epcData = Deencapsulation.getField(tdtEpc, "epcData");
		String epcDataString = Arrays.toString(epcData);		
		Assert.assertEquals("[28, -16, 0, 0]", epcDataString);
	}
	
	@Test
	public void testdecodeParameterListInvalidValue() {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		String parameterList = "type=";
		
		try {
			tdtEpc.decode("01010101", parameterList);
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Invalid value in parameterlist", e.getMessage());
		}
	}
	
	@Test
	public void testdecodeParameterListValidValue() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		String parameterList = "type=SGTIN-96;serial=123456";

		tdtEpc.decode("01010101", parameterList);
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");
		Assert.assertTrue(fields.containsKey("type"));
		Assert.assertEquals("SGTIN-96", fields.getFieldValue("type"));
		Assert.assertTrue(fields.containsKey("serial"));
		Assert.assertEquals("123456", fields.getFieldValue("serial"));
	}
	
	@Test
	public void testfindEncodingInputOptionNotEpcGlobal() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		TdtFields fields = new TdtFields();
		fields.add("taglength", "8");
		Deencapsulation.setField(tdtEpc, "fields", fields);
		
		tdtEpc.decode("01010101", "");
		Assert.assertFalse((boolean) Deencapsulation.getField(tdtEpc, "isEpcGlobal"));
		
	}
	
	@Test
	public void testfindEncodingInputOptionTdtTranslationException() throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		
		try {
			tdtEpc.decode("01010101A", "");
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find encoding scheme", e.getMessage());
		}		
	}
	
	@Test
	public void testfindEncodingParsingParamMissing(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX optionX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		final String epcIdentifier = "urn:epc:tag:sgtin-96:3.0614141";
		final int taglength = 0;
				
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = optionX;
			
			optionX.getLevel().getRequiredParsingParameters();
			result = "type";
		}};
		
		try {
			tdtEpc.decode(epcIdentifier, "");
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : required parsing parameter 'type' missing", e.getMessage());
		}		
	}
	
	@Test
	public void testfindEncodingInputLevel(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX optionX, @Mocked final LevelX levelX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "urn:epc:tag:sgtin-96:3.0614141";
		final int taglength = 0;
		
		TdtFields fields = new TdtFields();
		fields.add("fieldOptionKey", "test");
		Deencapsulation.setField(tdtEpc, "fields", fields);
		
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = optionX;
			
			optionX.getLevel().getRequiredParsingParameters();
			result = null;
			
			optionX.getLevel().getScheme().getOptionKey();
			result = "fieldOptionKey";
			
			optionX.getOptionKey();
			result = null;
			
			optionX.getLevel().getOptionByOptionKey(anyString);
			result = optionX;
			
			optionX.getLevel();
			result = levelX;
			
			levelX.getType();
			result = LevelTypeList.TAG_ENCODING;
		}};
		
		tdtEpc.decode(epcIdentifier, "");		
		Assert.assertEquals(levelX, Deencapsulation.getField(tdtEpc, "inputLevel"));
	}
	
	@Test
	public void testfindEncodingInputLevelWithOptionkey(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX optionX, @Mocked final LevelX levelX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "urn:epc:tag:sgtin-96:3.0614141";
		final int taglength = 0;
		
		TdtFields fields = new TdtFields();
		fields.add("fieldOptionKey", "test");
		Deencapsulation.setField(tdtEpc, "fields", fields);
		
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = optionX;
			
			optionX.getLevel().getRequiredParsingParameters();
			result = null;
			
			optionX.getLevel().getScheme().getOptionKey();
			result = "fieldOptionKey";
			
			optionX.getOptionKey();
			result = "test2";
			
			optionX.getLevel().getOptionByOptionKey(anyString);
			result = optionX;
			
			optionX.getLevel();
			result = levelX;
			
			levelX.getType();
			result = LevelTypeList.TAG_ENCODING;
		}};
		
		tdtEpc.decode(epcIdentifier, "");		
		Assert.assertEquals(levelX, Deencapsulation.getField(tdtEpc, "inputLevel"));
	}
	
	//test includes decodeEpcIdentifierBinary 
	@Test
	public void testdecodeEpcIdentifierCaseBinary(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX inputOption,
			@Mocked final LevelX inputLevel) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "11110000101010101111111100000001";
		final int taglength = 0;
		
		final Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("01");
		field.setBitLength(new BigInteger("32"));
		field.setCompaction(CompactionMethodList._32BIT);
		
		Field fieldTE = new Field();
		fieldTE.setCharacterSet("01");
		fieldTE.setPadDir(PadDirectionList.LEFT);
		fieldTE.setLength(new BigInteger("32"));
		fieldTE.setPadChar("0");
		
		final FieldX fieldX = new FieldX(field, inputOption);
		fieldX.setBitPosition(0);
		
		final FieldX fieldTagEncoding = new FieldX(fieldTE, inputOption);
		
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = inputOption;
			
			inputOption.getLevel().getRequiredParsingParameters();
			result = null;
			
			inputOption.getLevel().getScheme().getOptionKey();
			result = "missingfieldname";
			
			inputOption.getLevel();
			result = inputLevel;
			
			inputLevel.getType();
			result = LevelTypeList.BINARY;
			
			inputLevel.getScheme().getOption(LevelTypeList.TAG_ENCODING, anyString);
			result = inputOption;
			
			inputOption.getFields();
			result = fieldX;
			
			inputOption.getField("testfield");
			result = fieldTagEncoding;
		}};
		
		tdtEpc.decode(epcIdentifier, "");
		
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");
		
		Assert.assertNotNull(Deencapsulation.getField(tdtEpc, "epcData"));
		Assert.assertTrue(fields.containsKey("taglength"));
		Assert.assertEquals("32", fields.getFieldValue("taglength"));
		Assert.assertTrue(fields.containsKey("testfield"));
		Assert.assertEquals("00000000000000000000004037738241", fields.getFieldValue("testfield"));
		Assert.assertEquals(32, fields.getFieldValue("testfield").length());
	}
	
	//test includes decodeEpcIdentifierUrn 
	@Test
	public void testdecodeEpcIdentifierCaseTagencoding(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX inputOption,
			@Mocked final LevelX inputLevel, @Mocked final Matcher matcher) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "urn:epc:tag:sgtin-96:3.0614141.812345.6789";
		final int taglength = 0;
			
		final Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("01");
		field.setSeq(new BigInteger("16"));
			
		final FieldX fieldX = new FieldX(field, inputOption);
		fieldX.setBitPosition(0);
			
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = inputOption;
			
			inputOption.getLevel().getRequiredParsingParameters();
			result = null;
			
			inputOption.getLevel().getScheme().getOptionKey();
			result = "missingfieldname";
			
			inputOption.getLevel();
			result = inputLevel;
				
			inputLevel.getType();
			result = LevelTypeList.TAG_ENCODING;
			
			inputLevel.getScheme().getIntTagLength();
			result = 32;
				
			inputOption.getPattern().matcher(epcIdentifier);
			result = matcher;
				
			matcher.matches();
			result = true;
				
			inputOption.getFields();
			result = fieldX;
				
			matcher.group(anyInt);
			result = "16";				
		}};
			
		tdtEpc.decode(epcIdentifier, "");
	
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");
			
		Assert.assertTrue(fields.containsKey("taglength"));
		Assert.assertEquals("32", fields.getFieldValue("taglength"));
		Assert.assertTrue(fields.containsKey("testfield"));
		Assert.assertEquals("16", fields.getFieldValue("testfield"));
	}
	
	//test includes decodeEpcIdentifierUrn 
	@Test
	public void testdecodeEpcIdentifierCasePureidentity(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX inputOption,
			@Mocked final LevelX inputLevel, @Mocked final Matcher matcher) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "urn:epc:sgtin-96:3.0614141.812345.6789";
		final int taglength = 0;
			
		final Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("01");
		field.setSeq(new BigInteger("1"));
			
		final FieldX fieldX = new FieldX(field, inputOption);
		fieldX.setBitPosition(0);
			
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = inputOption;
			
			inputOption.getLevel().getRequiredParsingParameters();
			result = null;
			
			inputOption.getLevel().getScheme().getOptionKey();
			result = "missingfieldname";
			
			inputOption.getLevel();
			result = inputLevel;
				
			inputLevel.getType();
			result = LevelTypeList.PURE_IDENTITY;
				
			inputOption.getPattern().matcher(epcIdentifier);
			result = matcher;
				
			matcher.matches();
			result = true;
				
			inputOption.getFields();
			result = fieldX;
				
			matcher.group(anyInt);
			result = "16%2F1";				
		}};
			
		tdtEpc.decode(epcIdentifier, "");
	
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");

		Assert.assertTrue(fields.containsKey("testfield"));
		Assert.assertEquals("16/1", fields.getFieldValue("testfield"));
	}
	
	//test includes decodeEpcIdentifierLegacy 
	@Test
	public void testdecodeEpcIdentifierCaseLegacy(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX inputOption,
			@Mocked final LevelX inputLevel, @Mocked final Matcher matcher) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "urn:epc:sgtin-96:3.0614141.812345.6789";
		final int taglength = 0;
			
		final Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("01");
		field.setSeq(new BigInteger("1"));
			
		final FieldX fieldX = new FieldX(field, inputOption);
		fieldX.setBitPosition(0);
			
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = inputOption;
			
			inputOption.getLevel().getRequiredParsingParameters();
			result = null;
				
			inputOption.getLevel().getScheme().getOptionKey();
			result = "missingfieldname";
			
			inputOption.getLevel();
			result = inputLevel;
				
			inputLevel.getType();
			result = LevelTypeList.LEGACY;
			
			inputOption.getPattern().matcher(epcIdentifier);
			result = matcher;
			
			matcher.matches();
			result = true;
			
			inputOption.getFields();
			result = fieldX;
			
			matcher.groupCount();
			result = 10;
			
			matcher.group(1);
			result = "test";
		}};
				
		tdtEpc.decode(epcIdentifier, "");
	
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");
		Assert.assertTrue(fields.containsKey("testfield"));
		Assert.assertEquals("test", fields.getFieldValue("testfield"));
	}
	
	//test includes decodeEpcIdentifierLegacy 
	@Test
	public void testdecodeEpcIdentifierCaseLegacyAi(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX inputOption,
			@Mocked final LevelX inputLevel, @Mocked final Matcher matcher) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "urn:epc:sgtin-96:3.0614141.812345.6789";
		final int taglength = 0;
			
		final Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("01");
		field.setSeq(new BigInteger("1"));
			
		final FieldX fieldX = new FieldX(field, inputOption);
		fieldX.setBitPosition(0);
			
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = inputOption;
			
			inputOption.getLevel().getRequiredParsingParameters();
			result = null;
				
			inputOption.getLevel().getScheme().getOptionKey();
			result = "missingfieldname";
			
			inputOption.getLevel();
			result = inputLevel;
				
			inputLevel.getType();
			result = LevelTypeList.LEGACY_AI;
			
			inputOption.getPattern().matcher(epcIdentifier);
			result = matcher;
			
			matcher.matches();
			result = true;
			
			inputOption.getFields();
			result = fieldX;
			
			matcher.groupCount();
			result = 10;
			
			matcher.group(1);
			result = "test";
		}};
				
		tdtEpc.decode(epcIdentifier, "");
	
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");
		Assert.assertTrue(fields.containsKey("testfield"));
		Assert.assertEquals("test", fields.getFieldValue("testfield"));
	}
	
	//test includes decodeEpcIdentifierLegacy 
	@Test
	public void testdecodeEpcIdentifierCaseLegacyAlt(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX inputOption,
			@Mocked final LevelX inputLevel, @Mocked final Matcher matcher) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "urn:epc:sgtin-96:3.0614141.812345.6789";
		final int taglength = 0;
			
		final Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("01");
		field.setSeq(new BigInteger("1"));
			
		final FieldX fieldX = new FieldX(field, inputOption);
		fieldX.setBitPosition(0);
			
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = inputOption;
			
			inputOption.getLevel().getRequiredParsingParameters();
			result = null;
				
			inputOption.getLevel().getScheme().getOptionKey();
			result = "missingfieldname";
			
			inputOption.getLevel();
			result = inputLevel;
				
			inputLevel.getType();
			result = LevelTypeList.LEGACY_ALT;
			
			inputOption.getPattern().matcher(epcIdentifier);
			result = matcher;
			
			matcher.matches();
			result = true;
			
			inputOption.getFields();
			result = fieldX;
			
			matcher.groupCount();
			result = 10;
			
			matcher.group(1);
			result = "test";
		}};
				
		tdtEpc.decode(epcIdentifier, "");
	
		TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");
		Assert.assertTrue(fields.containsKey("testfield"));
		Assert.assertEquals("test", fields.getFieldValue("testfield"));
	}
	
	@Test
	public void testdecodeEpcIdentifierCaseONSHostname(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX inputOption,
			@Mocked final LevelX inputLevel) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "urn:epc:sgtin-96:3.0614141.812345.6789";
		final int taglength = 0;
			
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = inputOption;
			
			inputOption.getLevel().getRequiredParsingParameters();
			result = null;
				
			inputOption.getLevel().getScheme().getOptionKey();
			result = "missingfieldname";
			
			inputOption.getLevel();
			result = inputLevel;
				
			inputLevel.getType();
			result = LevelTypeList.ONS_HOSTNAME;
		}};
		try {
			tdtEpc.decode(epcIdentifier, "");
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Level Type ONS_HOSTNAME cannot be decoded", e.getMessage());
		}		
	}
	
	@Test
	public void testdecodeEpcIdentifierBinaryException(@Mocked final TdtDefinitions tdtDefinitions, @Mocked final OptionX inputOption,
			@Mocked final LevelX inputLevel) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "11110000101010101111111100000001";
		final int taglength = 0;
		
		final Field field = new Field();
		field.setName("testfield");
		field.setCharacterSet("01");
		field.setBitLength(new BigInteger("32"));
		field.setCompaction(CompactionMethodList._32BIT);
		
		final FieldX fieldX = new FieldX(field, inputOption);
		fieldX.setBitPosition(0);
		
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = inputOption;
			
			inputOption.getLevel().getRequiredParsingParameters();
			result = null;
			
			inputOption.getLevel().getScheme().getOptionKey();
			result = "missingfieldname";
			
			inputOption.getLevel();
			result = inputLevel;
			
			inputLevel.getType();
			result = LevelTypeList.BINARY;
			
			inputLevel.getScheme().getOption(LevelTypeList.TAG_ENCODING, anyString);
			result = new TdtTranslationException();
		}};
		try {
			tdtEpc.decode(epcIdentifier, "");
		} catch (Exception e) {
			TdtFields fields = Deencapsulation.getField(tdtEpc, "fields");
			
			Assert.assertNotNull(Deencapsulation.getField(tdtEpc, "epcData"));
			Assert.assertFalse(fields.containsKey("taglength"));
			Assert.assertFalse(fields.containsKey("testfield"));
			Assert.assertFalse((boolean) Deencapsulation.getField(tdtEpc, "isEpcGlobal"));
			Assert.assertEquals("", Deencapsulation.getField(tdtEpc, "statusText"));
		}		
	}
	
	@Test
	public void testcopyEpcIdentifierToEpcData(@Mocked final TdtDefinitions tdtDefinitions) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtDefinitions);
		final String epcIdentifier = "111100001010101011111111000000011";
		final int taglength = 0;
		
		new NonStrictExpectations() {{
			tdtDefinitions.getEncodingOption(epcIdentifier, taglength);
			result = null;
		}};
		tdtEpc.decode(epcIdentifier, "");
		
		byte[] epcData = Deencapsulation.getField(tdtEpc, "epcData");
		String epcDataString = Arrays.toString(epcData);
		Assert.assertEquals(5, epcData.length);
		Assert.assertEquals("[-16, -86, -1, 1, -128]", epcDataString);
	}
	
	@Test
	public void testencodeEpcIdentifierUrnValueLengthMoreThanFieldLength(@Mocked final LevelX levelX,
			@Mocked final OptionX optionX, @Mocked final FieldX fieldX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
	
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		Deencapsulation.setField(tdtEpc, "inputOption", optionX);
			
		TdtFields fields = new TdtFields();
		fields.add("testfieldname", "0100011001");
	
		Deencapsulation.setField(tdtEpc, "fields", fields);
	
		new NonStrictExpectations() {{
		levelX.getScheme().getLevel(LevelTypeList.PURE_IDENTITY);
		result = levelX;
		
		levelX.getOptionByOptionKey(anyString);
		result = optionX;
		
		levelX.getRuleList();
		result = null;
		
		optionX.getGrammar();
		result = "testfieldname";
		
		optionX.getField(anyString);
			result = fieldX;
			
			fieldX.getLength();
			result = new BigInteger("1");
			
			fieldX.getIntLength();
			result = 8;
			
			fieldX.getPadDir();
			result = null;
			
			fieldX.isNumeric();
			result = true;
		}};
		
		String uriId = tdtEpc.getUriId();
		Assert.assertEquals("00011001", uriId);
	}
	
	@Test
	public void testencodeEpcIdentifierUrnFieldIntLengthNull(@Mocked final LevelX levelX,
			@Mocked final OptionX optionX, @Mocked final FieldX fieldX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		Deencapsulation.setField(tdtEpc, "inputOption", optionX);
		
		TdtFields fields = new TdtFields();
		fields.add("testfieldname", "0100011001");
		
		Deencapsulation.setField(tdtEpc, "fields", fields);
		
		new NonStrictExpectations() {{
			levelX.getScheme().getLevel(LevelTypeList.PURE_IDENTITY);
			result = levelX;
			
			levelX.getOptionByOptionKey(anyString);
			result = optionX;
			
			levelX.getRuleList();
			result = null;
			
			optionX.getGrammar();
			result = "testfieldname";
			
			optionX.getField(anyString);
			result = fieldX;
			
			fieldX.getLength();
			result = new BigInteger("1");
			
			fieldX.getIntLength();
			result = 0;
		}};
		
		String uriId = tdtEpc.getUriId();
		Assert.assertEquals("", uriId);
	}
	
	@Test
	public void testencodeEpcIdentifierUrnPatDirRight(@Mocked final LevelX levelX,
			@Mocked final OptionX optionX, @Mocked final FieldX fieldX) throws TdtTranslationException {
		
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		Deencapsulation.setField(tdtEpc, "inputOption", optionX);
		
		TdtFields fields = new TdtFields();
		fields.add("testfieldname", "0100011001");
		
		Deencapsulation.setField(tdtEpc, "fields", fields);
		
		new NonStrictExpectations() {{
			levelX.getScheme().getLevel(LevelTypeList.PURE_IDENTITY);
			result = levelX;
			
			levelX.getOptionByOptionKey(anyString);
			result = optionX;
			
			levelX.getRuleList();
			result = null;
			
			optionX.getGrammar();
			result = "testfieldname";
			
			optionX.getField(anyString);
			result = fieldX;
			
			fieldX.getIntLength();
			result = 32;
			
			fieldX.getPadDir();
			result = PadDirectionList.RIGHT;
			
			fieldX.getPadChar();
			result = '#';
			
			fieldX.isNumeric();
			result = true;
		}};
		
		String uriId = tdtEpc.getUriId();
		Assert.assertEquals("0100011001######################", uriId);
	}
	
	@Test
	public void testencodeEpcIdentifierUrnPatDirLeft(@Mocked final LevelX levelX,
			@Mocked final OptionX optionX, @Mocked final FieldX fieldX) throws TdtTranslationException {
		
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		Deencapsulation.setField(tdtEpc, "inputOption", optionX);
		
		TdtFields fields = new TdtFields();
		fields.add("testfieldname", "0100011001");
		
		Deencapsulation.setField(tdtEpc, "fields", fields);
		
		new NonStrictExpectations() {{
			levelX.getScheme().getLevel(LevelTypeList.PURE_IDENTITY);
			result = levelX;
			
			levelX.getOptionByOptionKey(anyString);
			result = optionX;
			
			levelX.getRuleList();
			result = null;
			
			optionX.getGrammar();
			result = "testfieldname";
			
			optionX.getField(anyString);
			result = fieldX;
			
			fieldX.getIntLength();
			result = 32;
			
			fieldX.getPadDir();
			result = PadDirectionList.LEFT;
			
			fieldX.getPadChar();
			result = '#';
			
			fieldX.isNumeric();
			result = true;
		}};
		
		String uriId = tdtEpc.getUriId();
		Assert.assertEquals("######################0100011001", uriId);
	}
	
	@Test
	public void testencodeEpcIdentifierUrnNotNumeric(@Mocked final LevelX levelX,
			@Mocked final OptionX optionX, @Mocked final FieldX fieldX) throws TdtTranslationException {
		
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		Deencapsulation.setField(tdtEpc, "inputOption", optionX);
		
		TdtFields fields = new TdtFields();
		fields.add("testfieldname", "?00011001");
		
		Deencapsulation.setField(tdtEpc, "fields", fields);
		
		new NonStrictExpectations() {{
			levelX.getScheme().getLevel(LevelTypeList.PURE_IDENTITY);
			result = levelX;
			
			levelX.getOptionByOptionKey(anyString);
			result = optionX;
			
			levelX.getRuleList();
			result = null;
			
			optionX.getGrammar();
			result = "testfieldname";
			
			optionX.getField(anyString);
			result = fieldX;
			
			fieldX.getIntLength();
			result = 32;
			
			fieldX.getPadDir();
			result = null;
			
			fieldX.isNumeric();
			result = false;
			
			optionX.getLevel().getType();
			result = LevelTypeList.TAG_ENCODING;
		}};
		
		String uriId = tdtEpc.getUriId();
		Assert.assertEquals("%3F00011001", uriId);
	}

	@Test
	public void testencodeUriPartRawBinaryWithRemainder(@Mocked final LevelX levelX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		
		Deencapsulation.setField(tdtEpc, "uriBinary",
				"001110100111010011");
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		
		new NonStrictExpectations() {{
			levelX.getScheme().getIntTagLength();
			result = 17;
		}};
		
		String uriRawHexField = tdtEpc.getUriRawHex();
		Assert.assertEquals("urn:epc:raw:" + "17" + ".x" + "3A74C0", uriRawHexField);
	}
	
	@Test
	public void testfindOuputOptionNoLevel(@Mocked final LevelX levelX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		
		new NonStrictExpectations() {{
			levelX.getScheme().getLevel(LevelTypeList.BINARY);
			result = null;
			
			levelX.getScheme().getId();
			result = "test";
		}};
		
		try {
			tdtEpc.getUriBinary();
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'BINARY' for scheme 'test'", e.getMessage());
		}		
	}
	
	@Test
	public void testfindOuputOptionNoOption(@Mocked final LevelX levelX, @Mocked final OptionX optionX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		Deencapsulation.setField(tdtEpc, "inputOption", optionX);
		
		new NonStrictExpectations() {{
			levelX.getScheme().getLevel(LevelTypeList.BINARY);
			result = levelX;
			
			levelX.getOptionByOptionKey(anyString);
			result = null;
			
			levelX.getScheme().getId();
			result = "test";
		}};
		
		try {
			tdtEpc.getUriBinary();
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find output option for level type 'BINARY' for scheme 'test'", e.getMessage());
		}		
	}
	
	@Test
	public void testencodeUriBinaryValueNotFound(@Mocked final LevelX levelX, @Mocked final OptionX optionX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		Deencapsulation.setField(tdtEpc, "inputOption", optionX);
		
		new NonStrictExpectations() {{
			levelX.getScheme().getLevel(LevelTypeList.BINARY);
			result = levelX;
			
			levelX.getOptionByOptionKey(anyString);
			result = optionX;
			
			levelX.getScheme().getIntTagLength();
			result = 96;
			
			optionX.getGrammar();
			result = "testfieldname";
		}};
		
		try {
			tdtEpc.getUriBinary();
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Value not found, fieldname='testfieldname'", e.getMessage());
		}
	}
	
	@Test
	public void testcheckRequiredFormattingParametersMissingParam(@Mocked final LevelX levelX,
			@Mocked final OptionX optionX) throws TdtTranslationException {
		final TdtEpc tdtEpc = new TdtEpc(tdtTranslator.getTdtDefinitions());
		Deencapsulation.setField(tdtEpc, "inputLevel", levelX);
		Deencapsulation.setField(tdtEpc, "inputOption", optionX);
		
		new NonStrictExpectations() {{
			levelX.getScheme().getLevel(LevelTypeList.BINARY);
			result = levelX;
			
			levelX.getOptionByOptionKey(anyString);
			result = optionX;
			
			levelX.getRequiredFormattingParameters();
			result = "testParameterName";
		}};
		
		try {
			tdtEpc.getUriBinary();
			Assert.fail("TdtTranslationException expected");
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : required formatting parameter 'testParameterName' missing", e.getMessage());
		}
	}
}
