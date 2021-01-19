package havis.middleware.tdt;

import havis.middleware.misc.TdtInitiator;
import havis.middleware.misc.TdtInitiator.SCHEME;
import mockit.Deencapsulation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TdtTranslatorTest {

    private static TdtTranslator tdtTranslator;

    @Before
    public void init() throws Exception {
        tdtTranslator = new TdtTranslator();

        for (SCHEME scheme : TdtInitiator.SCHEME.values())
            tdtTranslator.getTdtDefinitions().add(TdtInitiator.get(scheme));
    }

    @Test
    public void translate() throws TdtTranslationException {
        String uri = "urn:epc:tag:sgtin-96:3.0652642.102400.9";
        TdtTagInfo info = tdtTranslator.translate(uri);
        byte[] bytes = info.getEpcData();
        info = tdtTranslator.translate(bytes);
        Assert.assertEquals(uri, info.getUriTag());
    }

    @Test
    public void translateGrai96() throws TdtTranslationException {
        String uri = "urn:epc:tag:grai-96:1.426025746.900.9136";
        TdtTagInfo info = tdtTranslator.translate(uri);
        byte[] bytes = info.getEpcData();

        StringBuilder data = new StringBuilder();
        for (byte b : bytes) {
            data.append(String.format("%02X", Byte.valueOf(b)));
        }

        Assert.assertEquals("332D964A3120E100000023B0", data.toString());

        info = tdtTranslator.translate(bytes);
        Assert.assertEquals(uri, info.getUriTag());
    }

	@Test
	public void translateGiai202() throws TdtTranslationException {
		byte[] data = new byte[] { 0x38, 0x00, 0x72, (byte) 0xFA, 0x64, 0x68, 0x51, (byte) 0x8B, 0x26, 0x6D, 0x1A, (byte) 0xB6, 0x6E, (byte) 0xE1, (byte) 0xCB, 0x06, 0x2C, (byte) 0x99, (byte) 0xB4, 0x6A, (byte) 0xD9, (byte) 0xBB, (byte) 0x80, 0x00, 0x00, 0x00 };
		TdtTagInfo info = tdtTranslator.translate(data);
		Assert.assertEquals("urn:epc:tag:giai-202:0.123456789012.123456789012345678", info.getUriTag());
	}

	@Test
	public void translateSgtin96WithAdditionalData() throws TdtTranslationException {
		byte[] data = new byte[] { 0x30, 0x08, 0x33, (byte) 0xB2, (byte) 0xDD, (byte) 0xD9, 0x01, 0x40, 0x00, 0x00, 0x00, 0x02, 0x11, 0x11, 0x22, 0x22 };
		TdtTagInfo info = tdtTranslator.translate(data);
		Assert.assertEquals("urn:epc:tag:sgtin-96:0.0867360217.005.2", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:128.x300833B2DDD901400000000211112222", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:128.63845530746705484270986434139749687842", info.getUriRawDecimal());
	}

    @Test
    public void translateIntoBinary() throws TdtTranslationException {
    	String epcIdentifier = "urn:epc:tag:sgtin-96:3.0652642.102400.9";
    	String outputFormat = "BINARY";
    	
    	Assert.assertEquals("001100000111010000100111110101011000100001100100000000000000000000000000000000000000000000001001", tdtTranslator.translate(epcIdentifier, "", outputFormat));
    }
    
    @Test
    public void translateIntoTag() throws TdtTranslationException {
    	String epcIdentifier = "001100000111010000100111110101011000100001100100000000000000000000000000000000000000000000001001";
    	String outputFormat = "TAG_ENCODING";
    	
    	Assert.assertEquals("urn:epc:tag:sgtin-96:3.0652642.102400.9", tdtTranslator.translate(epcIdentifier, "", outputFormat));
    }
    
    @Test
    public void translateIntoPureIdentity() throws TdtTranslationException {
    	String epcIdentifier = "001100000111010000100111110101011000100001100100000000000000000000000000000000000000000000001001";
    	String outputFormat = "PURE_IDENTITY";
    	
    	Assert.assertEquals("urn:epc:id:sgtin:0652642.102400.9", tdtTranslator.translate(epcIdentifier, "", outputFormat));
    }
    
    @Test
    public void translateIntoLegacy() throws TdtTranslationException {
    	String epcIdentifier = "urn:epc:id:sgtin:0652642.102400.9";
    	String outputFormat = "LEGACY";
    	
    	Assert.assertEquals("gtin=10652642024004;serial=9", tdtTranslator.translate(epcIdentifier, "", outputFormat));
    }
    
    @Test
    public void translateIntoLegacyAlt() throws TdtTranslationException {
    	String epcIdentifier = "urn:epc:tag:grai-96:1.00000050.2437.547365";
    	String outputFormat = "LEGACY_ALT";
    	
    	Assert.assertEquals("grai13=0000005024377547365", tdtTranslator.translate(epcIdentifier, "", outputFormat));
    }
    
    @Test
    public void translateIntoLegacyAi() throws TdtTranslationException {
    	String epcIdentifier = "urn:epc:tag:grai-96:1.00000050.2437.547365";
    	String outputFormat = "LEGACY_AI";
    	
    	Assert.assertEquals("(8003)00000005024377547365", tdtTranslator.translate(epcIdentifier, "", outputFormat));
    }
    
    @Test
    public void translateIntoOnsHostname() throws TdtTranslationException {
    	String epcIdentifier = "001100000111010000100111110101011000100001100100000000000000000000000000000000000000000000001001";
    	String outputFormat = "ONS_HOSTNAME";
    	
    	Assert.assertEquals("102400.0652642.sgtin.id.onsepc.com", tdtTranslator.translate(epcIdentifier, "", outputFormat));
    }
    
    @Test
    public void translateParam() throws TdtTranslationException {
    	TdtTagInfo info = tdtTranslator.translateParam("type=GRAI-96;gs1companyprefixlength=8;filter=1;gs1companyprefix=0050;assettype=2437;serial=547365;");
    	TdtEpc epc = Deencapsulation.getField(info, "tdtEpc");
    	Assert.assertEquals("urn:epc:tag:grai-96:1.00000050.2437.547365", Deencapsulation.getField(epc, "epcIdentifier"));
    }
    
    @Test
    public void refreshTranslations() {
    	try {
			tdtTranslator.refreshTranslations();
		} catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Method not implemented : refreshTranslations", e.getMessage());
		}
    }
    
    @Test
    public void translateSgtin198WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x36, 0x34, 0x25, 0x7B, (byte) 0xF4, 0x6D, (byte) 0xB6, 0x5A, 0x30, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("gtin=10614141123459;serial=400", info.getUriLegacy());
    	Assert.assertEquals("(01)10614141123459(21)400", info.getUriLegacyAi());
    	Assert.assertEquals("001101100011010000100101011110111111010001101101101101100101101000110000011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:sgtin:0614141.112345.400", info.getUriId());
		Assert.assertEquals("urn:epc:tag:sgtin-198:1.0614141.112345.400", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:200.x3634257BF46DB65A3060000000000000000000000000000000", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:200.340242120273815735515695952136406209102975186750694469140480", info.getUriRawDecimal());
		Assert.assertEquals("112345.0614141.sgtin.id.onsepc.com", info.getUriOnsHostname());
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'SGTIN-198'", e.getMessage());
		}
    }
  
    @Test
    public void translateSscc96WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x31,0x74,0x25,0x7B,(byte) 0xF4,0x49,(byte) 0x96,0x02,(byte) 0xD2,0x00,0x00,0x00};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("sscc=106141412345678908", info.getUriLegacy());
    	Assert.assertEquals("(00)106141412345678908", info.getUriLegacyAi());
    	Assert.assertEquals("001100010111010000100101011110111111010001001001100101100000001011010010000000000000000000000000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:sscc:0614141.1234567890", info.getUriId());
		Assert.assertEquals("urn:epc:tag:sscc-96:3.0614141.1234567890", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:96.x3174257BF4499602D2000000", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:96.15305177890433358152339554304", info.getUriRawDecimal());
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'SSCC-96'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'SSCC-96'", e.getMessage());
		}
    }
    
    @Test
    public void translateSgln96WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x32,0x14,0x25,0x7B,(byte) 0xF4,0x60,0x72,0x00,0x00,0x00,0x01,(byte) 0x90};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("gln=0614141123452;serial=400", info.getUriLegacy());
    	Assert.assertEquals("(414)0614141123452(254)400", info.getUriLegacyAi());
    	Assert.assertEquals("001100100001010000100101011110111111010001100000011100100000000000000000000000000000000110010000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:sgln:0614141.12345.400", info.getUriId());
		Assert.assertEquals("urn:epc:tag:sgln-96:0.0614141.12345.400", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:96.x3214257BF460720000000190", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:96.15498606021578133150225138064", info.getUriRawDecimal());
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'SGLN-96'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'SGLN-96'", e.getMessage());
		}
    }
    
    @Test
    public void translateSgln195WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x39, 0x14, 0x25, 0x7B, (byte) 0xF4, 0x60, 0x72, (byte) 0xD1, (byte) 0x83, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("gln=0614141123452;serial=400", info.getUriLegacy());
    	Assert.assertEquals("(414)0614141123452(254)400", info.getUriLegacyAi());
    	Assert.assertEquals("001110010001010000100101011110111111010001100000011100101101000110000011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:sgln:0614141.12345.400", info.getUriId());
		Assert.assertEquals("urn:epc:tag:sgln-195:0.0614141.12345.400", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:200.x3914257BF46072D18300000000000000000000000000000000", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:200.358288787762976719775507493178251486241920912558290901139456", info.getUriRawDecimal());
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'SGLN-195'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'SGLN-195'", e.getMessage());
		}
    }
    
    @Test
    public void translateGrai96WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x33,0x14,0x25,0x7B,(byte) 0xF4,0x0C,0x0E,0x40,0x00,0x00,0x01,(byte) 0x90};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("grai=00614141123452400", info.getUriLegacy());
    	Assert.assertEquals("(8003)00614141123452400", info.getUriLegacyAi());
    	Assert.assertEquals("001100110001010000100101011110111111010000001100000011100100000000000000000000000000000110010000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:grai:0614141.12345.400", info.getUriId());
		Assert.assertEquals("urn:epc:tag:grai-96:0.0614141.12345.400", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:96.x3314257BF40C0E4000000190", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:96.15808091031375724644621353360", info.getUriRawDecimal());
		Assert.assertEquals("grai13=0614141123452400", info.getUriLegacyAlt());
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'GRAI-96'", e.getMessage());
		}
    }  
    
    @Test
    public void translateGrai170WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x37, 0x14, 0x25, 0x7B, (byte) 0xF4, 0x0C, 0x0E, 0x5A, 0x30, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("grai=00614141123452400", info.getUriLegacy());
    	Assert.assertEquals("(8003)00614141123452400", info.getUriLegacyAi());
    	Assert.assertEquals("00110111000101000010010101111011111101000000110000001110010110100011000001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:grai:0614141.12345.400", info.getUriId());
		Assert.assertEquals("urn:epc:tag:grai-170:0.0614141.12345.400", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:176.x3714257BF40C0E5A3060000000000000000000000000", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:176.20607387083275411281233370441387565389886411352571904", info.getUriRawDecimal());
		Assert.assertEquals("grai13=0614141123452400", info.getUriLegacyAlt());
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'GRAI-170'", e.getMessage());
		}
    } 
    
    @Test
    public void translateGiai96WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x34,0x14,0x25,0x7B,(byte) 0xF4,0x00,0x00,0x00,0x00,(byte) 0xBC,0x60,0x38};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("giai=061414112345400", info.getUriLegacy());
    	Assert.assertEquals("(8004)061414112345400", info.getUriLegacyAi());
    	Assert.assertEquals("001101000001010000100101011110111111010000000000000000000000000000000000101111000110000000111000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:giai:0614141.12345400", info.getUriId());
		Assert.assertEquals("urn:epc:tag:giai-96:0.0614141.12345400", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:96.x3414257BF400000000BC6038", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:96.16117576041193676345597255736", info.getUriRawDecimal());
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'GIAI-96'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'GIAI-96'", e.getMessage());
		}
    }  
    
    @Test
    public void translateGiai202WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x38, 0x14, 0x25, 0x7B, (byte) 0xF5, (byte) 0x8B, 0x26, 0x6D, 0x1A, (byte) 0xB4, 0x60, (byte) 0xC0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("giai=061414112345400", info.getUriLegacy());
    	Assert.assertEquals("(8004)061414112345400", info.getUriLegacyAi());
    	Assert.assertEquals("0011100000010100001001010111101111110101100010110010011001101101000110101011010001100000110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:giai:0614141.12345400", info.getUriId());
		Assert.assertEquals("urn:epc:tag:giai-202:0.0614141.12345400", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:208.x3814257BF58B266D1AB460C00000000000000000000000000000", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:208.90114991623499602854091383074987498984023780757564061611196416", info.getUriRawDecimal());
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'GIAI-202'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'GIAI-202'", e.getMessage());
		}
    }  
    
    @Test
    public void translateGsrn96WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x2D,0x14,0x25,0x7B,(byte) 0xF4,(byte) 0x49,(byte) 0x96,0x02,(byte) 0xD2,0x00,0x00,0x00};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("gsrn=061414112345678902", info.getUriLegacy());
    	Assert.assertEquals("(8018)061414112345678902", info.getUriLegacyAi());
    	Assert.assertEquals("001011010001010000100101011110111111010001001001100101100000001011010010000000000000000000000000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:gsrn:0614141.1234567890", info.getUriId());
		Assert.assertEquals("urn:epc:tag:gsrn-96:0.0614141.1234567890", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:96.x2D14257BF4499602D2000000", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:96.13951180972464973476668637184", info.getUriRawDecimal());
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'GSRN-96'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'GSRN-96'", e.getMessage());
		}
    }  
    
    @Test
    public void translateGdti96WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x2C, 0x14, 0x25, 0x7B, (byte) 0xF4, 0x60, 0x72, 0x00, 0x00, 0x00, 0x1A, (byte) 0xBF };
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("gdti=06141411234526847", info.getUriLegacy());
    	Assert.assertEquals("(253)06141411234526847", info.getUriLegacyAi());
    	Assert.assertEquals("001011000001010000100101011110111111010001100000011100100000000000000000000000000001101010111111", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:gdti:0614141.12345.6847", info.getUriId());
		Assert.assertEquals("urn:epc:tag:gdti-96:0.0614141.12345.6847", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:96.x2C14257BF460720000001ABF", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:96.13641695962650062737876458175", info.getUriRawDecimal());
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'GDTI-96'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'GDTI-96'", e.getMessage());
		}
    }  
    
    @Test
    public void translateGdti113WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x3A, 0x14, 0x25, 0x7B, (byte) 0xF4, 0x60, 0x72, 0x00, 0x00, 0x00, 0x00, 0x00, 0x20, (byte) 0xE7, (byte) 0x80};
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("gdti=06141411234526847", info.getUriLegacy());
    	Assert.assertEquals("(253)06141411234526847", info.getUriLegacyAi());
    	Assert.assertEquals("00111010000101000010010101111011111101000110000001110010000000000000000000000000000000000000000000100000111001111", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:gdti:0614141.12345.6847", info.getUriId());
		Assert.assertEquals("urn:epc:tag:gdti-113:0.0614141.12345.6847", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:120.x3A14257BF46072000000000020E780", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:120.301561835791195621766331553854580608", info.getUriRawDecimal());
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'GDTI-113'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'GDTI-113'", e.getMessage());
		}
    }
    
    @Test
    public void translateGid96WithAdditionalData() throws TdtTranslationException{
    	byte[] data = new byte[] {0x35, 0x5A, (byte) 0xB1, (byte) 0xC6, 0x00, 0x03, 0x03, (byte) 0x90, 0x00, 0x00, 0x01, (byte) 0x90 };
    	
    	TdtTagInfo info = tdtTranslator.translate(data);
    	
    	Assert.assertEquals("generalmanager=95100000;objectclass=12345;serial=400", info.getUriLegacy());
    	Assert.assertEquals("001101010101101010110001110001100000000000000011000000111001000000000000000000000000000110010000", info.getUriBinary());
    	Assert.assertEquals("urn:epc:id:gid:95100000.12345.400", info.getUriId());
		Assert.assertEquals("urn:epc:tag:gid-96:95100000.12345.400", info.getUriTag());
		Assert.assertEquals("urn:epc:raw:96.x355AB1C60003039000000190", info.getUriRawHex());
		Assert.assertEquals("urn:epc:raw:96.16512348355620248131771302288", info.getUriRawDecimal());
		try{
			Assert.assertEquals("", info.getUriLegacyAi());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_AI' for scheme 'GID-96'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriLegacyAlt());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'LEGACY_ALT' for scheme 'GID-96'", e.getMessage());
		}
		try{
			Assert.assertEquals("", info.getUriOnsHostname());
		}catch (TdtTranslationException e) {
			Assert.assertEquals("TDT-Error : Could not find level type 'ONS_HOSTNAME' for scheme 'GID-96'", e.getMessage());
		}
    }  
}