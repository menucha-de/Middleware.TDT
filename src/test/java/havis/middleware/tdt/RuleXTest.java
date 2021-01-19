package havis.middleware.tdt;

import org.junit.Assert;
import org.junit.Test;

public class RuleXTest {

	@Test
	public void getValueGs1Checksum() throws TdtTranslationException {
		// see http://www.gs1.org/check-digit-calculator
		Assert.assertEquals("0", getGs1Checksum("433401100907"));
		Assert.assertEquals("1", getGs1Checksum("433401100900"));
		Assert.assertEquals("2", getGs1Checksum("433401100903"));
		Assert.assertEquals("3", getGs1Checksum("433401100906"));
		Assert.assertEquals("4", getGs1Checksum("320000000377"));
		Assert.assertEquals("5", getGs1Checksum("433401100902"));
		Assert.assertEquals("6", getGs1Checksum("433401100905"));
		Assert.assertEquals("7", getGs1Checksum("433401100908"));
		Assert.assertEquals("8", getGs1Checksum("433401100901"));
		Assert.assertEquals("9", getGs1Checksum("433401100904"));
	}

	private String getGs1Checksum(String barcodeValue)
			throws TdtTranslationException {
		Rule rule = new Rule();
		rule.setFunction("gs1checksum(fieldA)");
		rule.setLength("1");
		RuleX ruleX = new RuleX(rule, null);

		TdtFields fields = new TdtFields();
		fields.add("fieldA", barcodeValue);

		return ruleX.getValue(fields);
	}

	@Test
	public void getValueSubString() throws TdtTranslationException {
		Assert.assertEquals("433", getSubString("433401100907", "0,3", "3"));
		Assert.assertEquals("43300", getSubString("433401100907", "0,3", "5"));
		Assert.assertEquals("33401", getSubString("433401100907", "1,5", "5"));
	}

	private String getSubString(String barcodeValue, String range, String length)
			throws TdtTranslationException {
		Rule rule = new Rule();
		rule.setFunction("substr(fieldA," + range + ")");
		rule.setLength(length);
		RuleX ruleX = new RuleX(rule, null);

		TdtFields fields = new TdtFields();
		fields.add("fieldA", barcodeValue);

		return ruleX.getValue(fields);
	}
}
