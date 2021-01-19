package havis.middleware.tdt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BarcodeTest {

	Barcode clazz;

	@Before
	public void init() throws IOException, URISyntaxException {
		this.clazz = new Barcode(getTestData());
	}

	@SuppressWarnings("serial")
	@Test
	public void testDecodeGs1128Barcode() throws IllegalArgumentException, NullPointerException, IOException, URISyntaxException {
		/**
		 * INFO:
		 * 
		 * There are no alphanumeric fields with fixed length.
		 */

		// 1 Numeric fixed length value (valid)
		List<Map.Entry<String, String>> expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("01", "12345678901231"));
			}
		};
		// (01)12345678901231
		assertEquals(expected, clazz.decodeGs1128Barcode("0112345678901231"));
//"1012345371234"
		// 1 Numeric fixed length value (to short)
		try {
			// (01)1234567890123
			clazz.decodeGs1128Barcode("011234567890123");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 01 does not conform the specification. Length should be 14 but is 13.",
					expectedEx.getMessage());
		}

		// 1 Numeric fixed length value (to long)
		try {
			// (01)123456789012312
			clazz.decodeGs1128Barcode("01123456789012312");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals("Barcode is invalid. Application Identifier could not be found at position 17.", expectedEx.getMessage());
		}

		// 1 Numeric variable length value (valid)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "12345"));
			}
		};
		// (37)12345{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("3712345"));

		// 1 Numeric variable length value (valid min length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "1"));
			}
		};
		// (37)1{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("371"));

		// 1 Numeric variable length value (valid max length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "12345678"));
			}
		};
		// (37)12345678{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("3712345678"));

		// 1 Numeric variable length value (to short)
		try {
			// (37){GS}
			clazz.decodeGs1128Barcode("37");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 37 does not conform the specification. The length should be at least 1 but is 0.",
					expectedEx.getMessage());
		}

		// 1 Numeric variable length value (to short & no Group separator)
		try {
			// (37)
			clazz.decodeGs1128Barcode("37");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 37 does not conform the specification. Maybe the FNC1 character (ASCII value 29) is missing.",
					expectedEx.getMessage());
		}

		// 1 Numeric variable length value (to long)
		try {
			// (37)123456789{GS}
			clazz.decodeGs1128Barcode("37123456789");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 37 does not conform the specification. The length should be at most 8 but is 9. Maybe the FNC1 character (ASCII value 29) is missing.",
					expectedEx.getMessage());
		}

		// 1 Numeric variable length value (to long & no Group separator)
		try {
			// (37)123456789
			clazz.decodeGs1128Barcode("37123456789");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 37 does not conform the specification. Maybe the FNC1 character (ASCII value 29) is missing.",
					expectedEx.getMessage());
		}

		// 1 Numeric variable length value (no Group separator)
		try {
			// (37)123456
			clazz.decodeGs1128Barcode("37123456");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 37 does not conform the specification. Maybe the FNC1 character (ASCII value 29) is missing.",
					expectedEx.getMessage());
		}

		// 1 AlphaNumeric variable length value (valid)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "12345a"));
			}
		};
		// (10)12345{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("1012345a"));

		// 1 AlphaNumeric variable length value (valid min length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "a"));
			}
		};
		// (10)1{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("10a"));

		// 1 AlphaNumeric variable length value (valid max length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "1234567890123456789a"));
			}
		};
		// (10)12345678901234567890{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("101234567890123456789a"));

		// 1 AlphaNumeric variable length value (to short)
		try {
			// (10){GS}
			clazz.decodeGs1128Barcode("10");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 10 does not conform the specification. The length should be at least 1 but is 0.",
					expectedEx.getMessage());
		}

		// 1 AlphaNumeric variable length value (to short & no Group separator)
		try {
			// (10)
			clazz.decodeGs1128Barcode("10");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 10 does not conform the specification. Maybe the FNC1 character (ASCII value 29) is missing.",
					expectedEx.getMessage());
		}

		// 1 AlphaNumeric variable length value (to long)
		try {
			// (10)123456789012345678901{GS}
			clazz.decodeGs1128Barcode("10123456789012345678901");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 10 does not conform the specification. The length should be at most 20 but is 21. Maybe the FNC1 character (ASCII value 29) is missing.",
					expectedEx.getMessage());
		}

		// 1 AlphaNumeric variable length value (to long & no Group separator)
		try {
			// (10)123456789012345678901
			clazz.decodeGs1128Barcode("10123456789012345678901");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 10 does not conform the specification. Maybe the FNC1 character (ASCII value 29) is missing.",
					expectedEx.getMessage());
		}

		// 1 AlphaNumeric variable length value (no Group separator)
		try {
			// (10)1234567890123456789
			clazz.decodeGs1128Barcode("101234567890123456789");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
			Assert.assertEquals(
					"Barcode is invalid. The value for the Application Identifier 10 does not conform the specification. Maybe the FNC1 character (ASCII value 29) is missing.",
					expectedEx.getMessage());
		}

		// 2 Numeric fixed length value (valid)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("01", "12345678901231"));
				add(new SimpleEntry<String, String>("00", "123456789012345678"));
			}
		};
		// (01)12345678901231(00)123456789012345678
		assertEquals(expected, clazz.decodeGs1128Barcode("011234567890123100123456789012345678"));

		// 2 Numeric fixed length value (2nd value to short)
		try {
			// (01)12345678901231(00)12345678901234567
			clazz.decodeGs1128Barcode("01123456789012310012345678901234567");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
		}

		// 2 Numeric fixed length value (2nd value to long)
		try {
			// (01)12345678901231(00)1234567890123456789
			clazz.decodeGs1128Barcode("0112345678901231001234567890123456789");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
		}

		// 2 Numeric variable length value (valid)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "1234567"));
				add(new SimpleEntry<String, String>("242", "123456"));
			}
		};
		// (37)1234567{GS}(242)123456{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("371234567242123456"));

		// 2 Numeric variable length value (1 min length & 2 valid)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "1"));
				add(new SimpleEntry<String, String>("242", "12345"));
			}
		};
		// (37)1{GS}(242)12345{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("37124212345"));

		// 2 Numeric variable length value (1 max length & 2 valid)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "12345678"));
				add(new SimpleEntry<String, String>("242", "12345"));
			}
		};
		// (37)12345678{GS}(242)12345{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("371234567824212345"));

		// 2 Numeric variable length value (1 valid & 2 min length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "1234567"));
				add(new SimpleEntry<String, String>("242", "1"));
			}
		};
		// (37)1234567{GS}(242)1{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("3712345672421"));

		// 2 Numeric variable length value (1 valid & 2 max length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "1234567"));
				add(new SimpleEntry<String, String>("242", "123456"));
			}
		};
		// (37)1234567{GS}(242)123456{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("371234567242123456"));

		// 2 Numeric variable length value (1 min length & 2 min length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "1"));
				add(new SimpleEntry<String, String>("242", "1"));
			}
		};
		// (37)1{GS}(242)1{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("3712421"));

		// 2 Numeric variable length value (1 min length & 2 max length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "1"));
				add(new SimpleEntry<String, String>("242", "123456"));
			}
		};
		// (37)1{GS}(242)123456{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("371242123456"));

		// 2 Numeric variable length value (1 max length & 2 min length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "12345678"));
				add(new SimpleEntry<String, String>("242", "1"));
			}
		};
		// (37)12345678{GS}(242)1{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("37123456782421"));

		// 2 Numeric variable length value (1 max length & 2 max length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("37", "12345678"));
				add(new SimpleEntry<String, String>("242", "123456"));
			}
		};
		// (37)12345678{GS}(242)123456{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("3712345678242123456"));

		// 2 Numeric variable length value (1 valid & 2 to short)
		try {
			// (37)1234567{GS}(242){GS}
			clazz.decodeGs1128Barcode("371234567242");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
		}

		// 2 Numeric variable length value (1 valid & 2 to long)
		try {
			// (37)1234567{GS}(242){GS}
			clazz.decodeGs1128Barcode("3712345672421234567");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
		}

		// 2 AlphaNumeric variable length value (valid)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "12345678a"));
				add(new SimpleEntry<String, String>("21", "123456a"));
			}
		};
		// (10)12345678a{GS}(21)123456a{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("1012345678a21123456a"));

		// 2 AlphaNumeric variable length value (1 min length & 2 valid)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "a"));
				add(new SimpleEntry<String, String>("21", "123456a"));
			}
		};
		// (10)a{GS}(21)123456a{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("10a21123456a"));

		// 2 AlphaNumeric variable length value (1 max length & 2 valid)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "1234567890123456789a"));
				add(new SimpleEntry<String, String>("21", "123456a"));
			}
		};
		// (10)1234567890123456789a{GS}(21)123456a{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("101234567890123456789a21123456a"));

		// 2 AlphaNumeric variable length value (1 valid & 2 min length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "12345678a"));
				add(new SimpleEntry<String, String>("21", "a"));
			}
		};
		// (10)12345678a{GS}(21)123456a{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("1012345678a21a"));

		// 2 AlphaNumeric variable length value (1 valid & 2 max length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "12345678a"));
				add(new SimpleEntry<String, String>("21", "1234567890123456789a"));
			}
		};
		// (10)12345678a{GS}(21)1234567890123456789a{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("1012345678a211234567890123456789a"));

		// 2 AlphaNumeric variable length value (1 min length & 2 min length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "a"));
				add(new SimpleEntry<String, String>("21", "a"));
			}
		};
		// (10)a{GS}(21)a{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("10a21a"));

		// 2 AlphaNumeric variable length value (1 min length & 2 max length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "a"));
				add(new SimpleEntry<String, String>("21", "1234567890123456789a"));
			}
		};
		// (10)a{GS}(21)1234567890123456789a{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("10a211234567890123456789a"));

		// 2 AlphaNumeric variable length value (1 max length & 2 min length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "1234567890123456789a"));
				add(new SimpleEntry<String, String>("21", "a"));
			}
		};
		// (10)1234567890123456789a{GS}(21)a{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("101234567890123456789a21a"));

		// 2 AlphaNumeric variable length value (1 max length & 2 max length)
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("10", "1234567890123456789a"));
				add(new SimpleEntry<String, String>("21", "1234567890123456789a"));
			}
		};
		// (10)1234567890123456789a{GS}(21)1234567890123456789a{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("101234567890123456789a211234567890123456789a"));

		// 2 AlphaNumeric variable length value (1 valid & 2 to short)
		try {
			// (10)1234567890123456789a{GS}(21){GS}
			clazz.decodeGs1128Barcode("101234567890123456789a21");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
		}

		// 2 AlphaNumeric variable length value (1 valid & 2 to long)
		try {
			// (10)1234567890123456789a{GS}(21)1234567890123456789aa{GS}
			clazz.decodeGs1128Barcode("101234567890123456789a211234567890123456789aa");
			Assert.fail("Exception expected.");
		} catch (IllegalArgumentException expectedEx) {
		}

		// 3 Numeric variable length (valid)
		// 3 Numeric fixed length (valid)
		// 3 AlphaNumeric variable length values (valid)
		// 2 Numeric variable length 1 AlphaNumeric variable length values
		// (valid)
		// 1 Numeric variable length 2 AlphaNumeric variable length values
		// (valid)
		// 2 Numeric fixed length 1 AlphaNumeric variable length values (valid)
		// 1 Numeric fixed length 2 AlphaNumeric variable length values (valid)

		// Test long barcode string
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("02", "12345678901234"));
				add(new SimpleEntry<String, String>("37", "1234"));
				add(new SimpleEntry<String, String>("253", "12345678901231234567890"));
			}
		};
		// (02)12345678901234(37)1234{GS}(253)12345678901231234567890{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode("021234567890123437123425312345678901231234567890"));

		// Test long barcode string
		expected = new ArrayList<Map.Entry<String, String>>() {
			{
				add(new SimpleEntry<String, String>("02", "12345678901234"));
				add(new SimpleEntry<String, String>("37", "1234"));
				add(new SimpleEntry<String, String>("253", "1234567890123"));
				add(new SimpleEntry<String, String>("3305", "123456"));
				add(new SimpleEntry<String, String>("3402", "123456"));
				add(new SimpleEntry<String, String>("3903", "1"));
				add(new SimpleEntry<String, String>("3915", "12345687"));
				add(new SimpleEntry<String, String>("414", "1234567890123"));
				add(new SimpleEntry<String, String>("254", "123"));
				add(new SimpleEntry<String, String>("420", "555"));
				add(new SimpleEntry<String, String>("421", "1115"));
				add(new SimpleEntry<String, String>("7039", "1116"));
			}
		};
		// (02)12345678901234(37)1234{GS}(253)1234567890123{GS}(3305)123456(3402)123456(3903)1{GS}(3915)12345687{GS}(414)1234567890123(254)123{GS}(420)555{GS}(421)1115{GS}(7039)1116{GS}
		assertEquals(expected, clazz.decodeGs1128Barcode(
				"0212345678901234371234253123456789012333051234563402123456390313915123456874141234567890123254123420555421111570391116"));
	}

	@Test
	public void testDecodeGs1128BarcodePerformance()
			throws IllegalArgumentException, NullPointerException, IOException, URISyntaxException {
		int runs = 500;
		long millis = System.currentTimeMillis();
		for (int i = 0; i < runs; i++)
			clazz.decodeGs1128Barcode("0112345678901231");
		System.out.println((System.currentTimeMillis() - millis) / runs);

		// runs = 1;
		millis = System.currentTimeMillis();
		for (int i = 0; i < runs; i++)
			clazz.decodeGs1128Barcode("1012345");
		System.out.println((System.currentTimeMillis() - millis) / runs);

		// runs = 1;
		millis = System.currentTimeMillis();
		for (int i = 0; i < runs; i++)
			clazz.decodeGs1128Barcode(
					"0212345678901234371234253123456789012333051234563402123456390313915123456874141234567890123254123420555421111570391116");
		System.out.println((System.currentTimeMillis() - millis) / runs);
	}

	private String[] getTestData() throws IOException, URISyntaxException {
		List<String> data = new ArrayList<String>();

		URL resource = getClass().getResource("table-for-data.csv");
		File file = new File(resource.toURI());
		FileReader in = new FileReader(file);
		BufferedReader reader = new BufferedReader(in);
		String line = "";

		while ((line = reader.readLine()) != null) {
			data.add(line);
		}

		reader.close();

		return data.toArray(new String[] {});
	}

	/**
	 * Asserts that two Sets are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 * 
	 * @param expected
	 * @param actual
	 */
	private void assertEquals(List<Map.Entry<String, String>> expected, List<Map.Entry<String, String>> actual) {
		if (expected == null && actual != null)
			throw new AssertionError("Expected should be null but actual is not null");
		if (expected != null && actual == null)
			throw new AssertionError("Expected should be not null but actual is null");
		if (expected != null && actual != null) {
			if (expected.size() != actual.size())
				throw new AssertionError("Expected size is " + expected.size() + " but actual size is " + actual.size() + ".");
			Iterator<Map.Entry<String, String>> expectedI = expected.iterator();
			Iterator<Map.Entry<String, String>> actualI = actual.iterator();
			while (expectedI.hasNext()) {
				Map.Entry<String, String> expectedE = expectedI.next();
				Map.Entry<String, String> actualE = actualI.next();
				Assert.assertEquals(expectedE.getKey(), actualE.getKey());
				Assert.assertEquals(expectedE.getValue(), actualE.getValue());
			}
		}
	}
}
