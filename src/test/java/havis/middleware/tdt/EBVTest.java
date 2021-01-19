package havis.middleware.tdt;

import org.junit.Assert;
import org.junit.Test;

public class EBVTest {

	@Test
	public void testGetEbvValue() {
		// EBV 3
		Assert.assertEquals("000", EBV.getEbvValue(EBV.EBV3, 0));
		Assert.assertEquals("001", EBV.getEbvValue(EBV.EBV3, 1));
		Assert.assertEquals("010", EBV.getEbvValue(EBV.EBV3, 2));
		Assert.assertEquals("011", EBV.getEbvValue(EBV.EBV3, 3));
		Assert.assertEquals("101000", EBV.getEbvValue(EBV.EBV3, 4));
		Assert.assertEquals("101001", EBV.getEbvValue(EBV.EBV3, 5));
		Assert.assertEquals("101010", EBV.getEbvValue(EBV.EBV3, 6));
		Assert.assertEquals("101011", EBV.getEbvValue(EBV.EBV3, 7));
		Assert.assertEquals("110000", EBV.getEbvValue(EBV.EBV3, 8));
		Assert.assertEquals("110001", EBV.getEbvValue(EBV.EBV3, 9));
		Assert.assertEquals("110010", EBV.getEbvValue(EBV.EBV3, 10));
		Assert.assertEquals("110011", EBV.getEbvValue(EBV.EBV3, 11));
		Assert.assertEquals("111000", EBV.getEbvValue(EBV.EBV3, 12));
		Assert.assertEquals("111001", EBV.getEbvValue(EBV.EBV3, 13));
		Assert.assertEquals("111010", EBV.getEbvValue(EBV.EBV3, 14));
		Assert.assertEquals("111011", EBV.getEbvValue(EBV.EBV3, 15));
		Assert.assertEquals("101100000", EBV.getEbvValue(EBV.EBV3, 16));
		Assert.assertEquals("101100001", EBV.getEbvValue(EBV.EBV3, 17));
				
		// EBV 6
		Assert.assertEquals("000000", EBV.getEbvValue(EBV.EBV6, 0));
		Assert.assertEquals("000001", EBV.getEbvValue(EBV.EBV6, 1));
		Assert.assertEquals("000010", EBV.getEbvValue(EBV.EBV6, 2));
		Assert.assertEquals("000100", EBV.getEbvValue(EBV.EBV6, 4));
		Assert.assertEquals("001000", EBV.getEbvValue(EBV.EBV6, 8));
		Assert.assertEquals("010000", EBV.getEbvValue(EBV.EBV6, 16));
		Assert.assertEquals("011111", EBV.getEbvValue(EBV.EBV6, 31));
		Assert.assertEquals("100001000000", EBV.getEbvValue(EBV.EBV6, 32));
		Assert.assertEquals("100010000000", EBV.getEbvValue(EBV.EBV6, 64));
		Assert.assertEquals("100100000000", EBV.getEbvValue(EBV.EBV6, 128));
		Assert.assertEquals("101000000000", EBV.getEbvValue(EBV.EBV6, 256));
		Assert.assertEquals("110000000000", EBV.getEbvValue(EBV.EBV6, 512));
		Assert.assertEquals("111111011111", EBV.getEbvValue(EBV.EBV6, 1023));
		Assert.assertEquals("100001100000000000", EBV.getEbvValue(EBV.EBV6, 1024));
	}
	
	@Test
	public void testValueOf(){
		Assert.assertEquals(0, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("00000000", 2, 16, 2)))));
		Assert.assertEquals(1, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("00100000", 2, 16, 0)))));
		Assert.assertEquals(2, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("01000000", 2, 16, 0)))));
		Assert.assertEquals(3, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("01100000", 2, 16, 0)))));
		Assert.assertEquals(4, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("10100000", 2, 16, 0)))));
		Assert.assertEquals(5, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("10100100", 2, 16, 0)))));
		Assert.assertEquals(6, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("10101000", 2, 16, 0)))));
		Assert.assertEquals(7, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("10101100", 2, 16, 0)))));
		Assert.assertEquals(8, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("11000000", 2, 16, 0)))));
		Assert.assertEquals(9, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("11000100", 2, 16, 0)))));
		Assert.assertEquals(10, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("11001000", 2, 16, 0)))));
		Assert.assertEquals(11, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("11001100", 2, 16, 0)))));
		Assert.assertEquals(12, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("11100000", 2, 16, 0)))));
		Assert.assertEquals(13, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("11100100", 2, 16, 0)))));
		Assert.assertEquals(14, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("11101000", 2, 16, 0)))));
		Assert.assertEquals(15, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("11101100", 2, 16, 0)))));
		Assert.assertEquals(16, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("1011000000000000", 2, 16, 0)))));
		Assert.assertEquals(17, EBV.EBV3.valueOf(new Buffer(DataTypeConverter.hexStringToByteArray(DataTypeConverter.convert("1011000010000000", 2, 16, 0)))));
	}

}
