package havis.middleware.tdt;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void padLeft() {
		Assert.assertNull(Utils.padLeft(null, 4, '0'));
		Assert.assertEquals("0000", Utils.padLeft("", 4, '0'));
		Assert.assertEquals("0001", Utils.padLeft("1", 4, '0'));
		Assert.assertEquals("0011", Utils.padLeft("11", 4, '0'));
		Assert.assertEquals("0111", Utils.padLeft("111", 4, '0'));
		Assert.assertEquals("1111", Utils.padLeft("1111", 4, '0'));
		Assert.assertEquals("11111", Utils.padLeft("11111", 4, '0'));
	}

	@Test
	public void padRight() {
		Assert.assertNull(Utils.padRight(null, 4, '0'));
		Assert.assertEquals("0000", Utils.padRight("", 4, '0'));
		Assert.assertEquals("1000", Utils.padRight("1", 4, '0'));
		Assert.assertEquals("1100", Utils.padRight("11", 4, '0'));
		Assert.assertEquals("1110", Utils.padRight("111", 4, '0'));
		Assert.assertEquals("1111", Utils.padRight("1111", 4, '0'));
		Assert.assertEquals("11111", Utils.padRight("11111", 4, '0'));
	}
}
