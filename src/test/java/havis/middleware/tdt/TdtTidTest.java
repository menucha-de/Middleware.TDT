package havis.middleware.tdt;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Assert;
import org.junit.Test;

public class TdtTidTest {
	
	private String TdTExcString = "TDT-Error : cannot decode TID, ";

	@Test
	public void decode() throws TdtTranslationException {
		TdtPc pc = new TdtPc(null);
		TdtTid tid = new TdtTid();
		// only the first 4 bytes matter for the decode method
		byte[] data = { (byte) 0xE2, (byte) 0xFF, (byte) 0xF0, (byte) 0x40 };
		tid.decode(pc, data);

		Assert.assertEquals((short) 0xFFF, tid.getMaskDesignerId());
		Assert.assertEquals((short) 0x040, tid.getTagModelNumber());
	}
	
	@Test
	public void noTdtPc() throws TdtTranslationException {
		final TdtTid tid = new TdtTid();
		
		try {
			tid.decode(null, null);
		} catch (TdtTranslationException e) {
			Assert.assertEquals(TdTExcString + "PC not initialized", e.getMessage());
		}	
	}
	
	@Test
	public void hasToggleBit(@Mocked final TdtPc pc) throws TdtTranslationException {
		final TdtTid tid = new TdtTid();
		
		new NonStrictExpectations() {{
			pc.getToggleBit();
			result = true; 
		}};
		
		try {
			tid.decode(pc, null);
		} catch (TdtTranslationException e) {
			Assert.assertEquals(TdTExcString + "toggle bit in PC is set", e.getMessage());
		}	
	}
	
	@Test
	public void noTidData() throws TdtTranslationException {
		final TdtTid tid = new TdtTid();
		
		try {
			tid.decode(new TdtPc(null), null);
		} catch (TdtTranslationException e) {
			Assert.assertEquals(TdTExcString + "no TID-data", e.getMessage());
		}	
	}
	
	@Test
	public void firstByteInvalid() throws TdtTranslationException {
		final TdtTid tid = new TdtTid();
		byte[] tidData = {(byte) 0xE1, (byte) 0xFA, (byte) 0xF3, (byte) 0xA1 };
		
		try {
			tid.decode(new TdtPc(null), tidData);
		} catch (TdtTranslationException e) {
			Assert.assertEquals(TdTExcString + "first byte of TID-data invalid", e.getMessage());
		}	
	}
}
