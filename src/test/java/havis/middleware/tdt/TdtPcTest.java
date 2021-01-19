package havis.middleware.tdt;

import org.junit.Assert;
import org.junit.Test;

public class TdtPcTest {

	@Test
	public void getLength() {
		byte[] data1 = { (byte) 0xE2 };
		TdtPc pc1 = new TdtPc(data1);
		Assert.assertEquals(28, pc1.getLength());

		byte[] data2 = { (byte) 0x00 };
		TdtPc pc2 = new TdtPc(data2);
		Assert.assertEquals(0, pc2.getLength());

		byte[] data3 = new byte[0];
		TdtPc pc3 = new TdtPc(data3);
		Assert.assertEquals(0, pc3.getLength());

		byte[] data4 = null;
		TdtPc pc4 = new TdtPc(data4);
		Assert.assertEquals(0, pc4.getLength());
	}

	@Test
	public void getToggleBit() {
		byte[] data1 = { (byte) 0x3 };
		TdtPc pc1 = new TdtPc(data1);
		Assert.assertTrue(pc1.getToggleBit());

		byte[] data2 = { (byte) 0x1 };
		TdtPc pc2 = new TdtPc(data2);
		Assert.assertTrue(pc2.getToggleBit());

		byte[] data3 = { (byte) 0xFF };
		TdtPc pc3 = new TdtPc(data3);
		Assert.assertTrue(pc3.getToggleBit());

		byte[] data4 = { (byte) 0x4 };
		TdtPc pc4 = new TdtPc(data4);
		Assert.assertFalse(pc4.getToggleBit());

		byte[] data5 = { (byte) 0x0 };
		TdtPc pc5 = new TdtPc(data5);
		Assert.assertFalse(pc5.getToggleBit());

		byte[] data6 = new byte[0];
		TdtPc pc6 = new TdtPc(data6);
		Assert.assertFalse(pc6.getToggleBit());

		byte[] data7 = null;
		TdtPc pc7 = new TdtPc(data7);
		Assert.assertFalse(pc7.getToggleBit());
	}

	@Test
	public void getXpcIndicator() {
		byte[] data1 = { (byte) 0x3 };
		TdtPc pc1 = new TdtPc(data1);
		Assert.assertTrue(pc1.getXpcIndicator());

		byte[] data2 = { (byte) 0x2 };
		TdtPc pc2 = new TdtPc(data2);
		Assert.assertTrue(pc2.getXpcIndicator());

		byte[] data3 = { (byte) 0xFF };
		TdtPc pc3 = new TdtPc(data3);
		Assert.assertTrue(pc3.getXpcIndicator());

		byte[] data4 = { (byte) 0x4 };
		TdtPc pc4 = new TdtPc(data4);
		Assert.assertFalse(pc4.getXpcIndicator());

		byte[] data5 = { (byte) 0x0 };
		TdtPc pc5 = new TdtPc(data5);
		Assert.assertFalse(pc5.getXpcIndicator());

		byte[] data6 = new byte[0];
		TdtPc pc6 = new TdtPc(data6);
		Assert.assertFalse(pc6.getXpcIndicator());

		byte[] data7 = null;
		TdtPc pc7 = new TdtPc(data7);
		Assert.assertFalse(pc7.getXpcIndicator());
	}

	@Test
	public void getUserMemoryIndicator() {
		byte[] data1 = { (byte) 0x5 };
		TdtPc pc1 = new TdtPc(data1);
		Assert.assertTrue(pc1.getUserMemoryIndicator());

		byte[] data2 = { (byte) 0x4 };
		TdtPc pc2 = new TdtPc(data2);
		Assert.assertTrue(pc2.getUserMemoryIndicator());

		byte[] data3 = { (byte) 0xFF };
		TdtPc pc3 = new TdtPc(data3);
		Assert.assertTrue(pc3.getUserMemoryIndicator());

		byte[] data4 = { (byte) 0xB };
		TdtPc pc4 = new TdtPc(data4);
		Assert.assertFalse(pc4.getUserMemoryIndicator());

		byte[] data5 = { (byte) 0x0 };
		TdtPc pc5 = new TdtPc(data5);
		Assert.assertFalse(pc5.getUserMemoryIndicator());

		byte[] data6 = new byte[0];
		TdtPc pc6 = new TdtPc(data6);
		Assert.assertFalse(pc6.getUserMemoryIndicator());

		byte[] data7 = null;
		TdtPc pc7 = new TdtPc(data7);
		Assert.assertFalse(pc7.getUserMemoryIndicator());
	}

	@Test
	public void getHazardousMaterial() {
		byte[] data1 = { (byte) 0x0, (byte) 0x3 };
		TdtPc pc1 = new TdtPc(data1);
		Assert.assertTrue(pc1.getHazardousMaterial());

		byte[] data2 = { (byte) 0x0, (byte) 0x1 };
		TdtPc pc2 = new TdtPc(data2);
		Assert.assertTrue(pc2.getHazardousMaterial());

		byte[] data3 = { (byte) 0x0, (byte) 0xFF };
		TdtPc pc3 = new TdtPc(data3);
		Assert.assertTrue(pc3.getHazardousMaterial());

		byte[] data4 = { (byte) 0x0, (byte) 0x2 };
		TdtPc pc4 = new TdtPc(data4);
		Assert.assertFalse(pc4.getHazardousMaterial());

		byte[] data5 = { (byte) 0x0, (byte) 0x0 };
		TdtPc pc5 = new TdtPc(data5);
		Assert.assertFalse(pc5.getHazardousMaterial());

		byte[] data6 = { (byte) 0x0 };
		TdtPc pc6 = new TdtPc(data6);
		Assert.assertFalse(pc6.getHazardousMaterial());

		byte[] data7 = null;
		TdtPc pc7 = new TdtPc(data7);
		Assert.assertFalse(pc7.getHazardousMaterial());
	}
}
