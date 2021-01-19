package havis.middleware.tdt;

import org.junit.Assert;
import org.junit.Test;

public class TdtTranslateCodeTableTest {

	@Test
	public void encodeURIForm() throws TdtTranslationException {
		Assert.assertEquals(
				"!%22%25%26'()*+,-.%2F0123456789:;%3C=%3E%3FABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz",
				TdtTranslateCodeTable
						.encodeURIForm("!\"%&'()*+,-./0123456789:;<=>?ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz"));
	}

	@Test
	public void decodeURIForm() throws TdtTranslationException {
		Assert.assertEquals(
				"!\"%&'()*+,-./0123456789:;<=>?ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz",
				TdtTranslateCodeTable
						.decodeURIForm("!%22%25%26'()*+,-.%2F0123456789:;%3C=%3E%3FABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz"));
	}
}
