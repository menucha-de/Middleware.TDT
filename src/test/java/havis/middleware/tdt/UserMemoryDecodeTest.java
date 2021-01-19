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

public class UserMemoryDecodeTest {

	PackedObjectInvestigator investigator;
	Barcode barcode;

	@Before
	public void init() throws IOException, URISyntaxException {
		this.investigator = new PackedObjectInvestigator(getTestData());
		this.barcode = new Barcode(investigator);
	}

	@Test
	public void testPerformance() {
		int runs = 500;
		long millis = System.currentTimeMillis();
		for (int i = 0; i < runs; i++)
			UserMemoryDecode.decode(investigator,
					"8985CD4B6B7D284ED02C398A07890047DC7EC132C47DC7EC132C7B1674E79C5FE423C4800000000000002C49B7293D5E172A832F9E8CF66B222011265C00");
		System.out.println((System.currentTimeMillis() - millis) / runs);
	}

	@Test
	public void testDecodeSmokeTest() {
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "AAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892A0247FC13C0E38E797000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3372", "123456"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.414", "1234567890123"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.254", "1234567890123"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.415", "1234567890123"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8020", "1234567890123"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.421", "123456789"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "12345678901234"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.37", "1"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.420", "1234567890123"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3370", "123456"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"8985CD4B6B7D284ED02C398A07890047DC7EC132C47DC7EC132C7B1674E79C5FE423C4800000000000002C49B7293D5E172A832F9E8CF66B222011265C00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "999999999999999999"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "88888888888888"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.37", "77777777"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.17", "666666"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.253", "5555555555555lllkkkjjj"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.251", "1KBN8Q+LKOppphhh"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.241", "MMMABCDEFGHJ555666777"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898865205D19E1823E7C3782DACE9D8FFFFE86C0752C71C4A2CB71A2C2A50D80EA58E3AFEB86A0D18C3414F639A503A8FFC849A20C06847B3566816259FD5DFCF59C24D89AA49B00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "010803589570995176"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892A00C099873ADAB68FA200").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "IkMtw0E2"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892A0263E811DBC1471C5000").getDataElements());
	}

	@Test
	public void testDecodeEmpty() {
		PackedObjects expected = new PackedObjects(this.investigator);
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "8900").getDataElements());
	}

	@Test
	public void testDecodeFormatFlags() {
		// Format Flag IDLPO DEFAULT = 00001000
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7004", "9"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "8908101B4900").getDataElements());
	}

	@Test
	public void testDecodeFixedNumeric() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single component in base table
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "123456789012345678"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892A00C6DA6D2E98C3CD3A00").getDataElements());

		// Single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.1", "12345678901234"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892601459D39E717F94000").getDataElements());

		// Single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.11", "123456"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891602C7890200").getDataElements());

		// Single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.20", "12"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891205464000").getDataElements());

		// Series in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3371", "123456"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A1018F1204000").getDataElements());

		// Selection(1st) and Series in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3305", "123456"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A0D2C78902000").getDataElements());

		// Selection(2nd) and Series in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3400", "480231"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A0D45D4F9E000").getDataElements());

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Single component in base table
		// 2nd: Single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "123456789012345678"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.1", "12345678901234"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "894640B91B69B4BA630F34E1674E79C5FE5000").getDataElements());

		// ###################################################################################################
		// THREE ENTRIES
		// ###################################################################################################

		// 1st: Single component in base table
		// 2nd: Single component in base table
		// 3th: Single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "123456789012345678"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.1", "12345678901234"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.11", "123456"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895280B81636D36974C61E69C2CE9CF38BFC87890200").getDataElements());
	}

	@Test
	public void testDecodeFixedNumericSecondaryTable() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single component in secondary table
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8017", "123456789012345678"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892E2A898DB4DA5D31879A7400").getDataElements());

		// Single component in secondary table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8100", "123456"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A2508F1204000").getDataElements());

		// Series component in secondary table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3106", "123456"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A0A1A3C481000").getDataElements());

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Single component in secondary table
		// 2nd: Series component in secondary table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8100", "555555"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3106", "123456"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892474300D87A231E24000").getDataElements());

		// ###################################################################################################
		// THREE ENTRIES
		// ###################################################################################################

		// 1st: Single component in secondary table
		// 2nd: Series component in secondary table
		// 3th: Single component in secondary table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8100", "555555"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3106", "123456"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8017", "123456789012345678"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "894EB43154184E1E88C789006DA6D2E98C3CD3A000").getDataElements());
	}

	@Test
	public void testDecodeVariableNumeric() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single component in base table
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.30", "123456"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A09E8F1204000").getDataElements());

		// Selection and Series in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3900", "123456789012345"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892A1083C38244306EFBCC00").getDataElements());

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Selection and Series in base table
		// 2nd: Single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3900", "123456789012345"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.30", "123456"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "893A575A0F51C12218377DE478902000").getDataElements());
	}

	@Test
	public void testDecodeVariableNumericSecondaryTable() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single component in secondary table
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8011", "123456"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891E2A88A8F1204000").getDataElements());

		// Single component in secondary table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8019", "0"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89162A8A804000").getDataElements());

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Single component in secondary table
		// 2nd: Single component in secondary table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8011", "2389746"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8019", "9998887771"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89347C6E2055A491DBCA53FAEB5B00").getDataElements());
	}

	@Test
	public void testDecodeVariableNumericMinMax() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891017C07B00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "1234"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891617C44D2800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "12345"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891617C8607300").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123456"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A17CC78902000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "1234567"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A17D04B5A1E00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "12345678"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891E17D45E30A74000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123456789"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891E17D875BCD15800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "1234567890"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891C17DC499602D200").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "12345678901"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892217E05BFB8386B000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123456789012"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892217E472FA64685200").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "1234567890123"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892617E847DC7EC132E000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "12345678901234"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892617EC59D39E717F9400").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123456789012345"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892417F07048860DDF7900").getDataElements());
	}

	@Test
	public void testDecodeNumericCombinations() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single Combination in base table
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.414", "1234567890123"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892215A23EE3F6099700").getDataElements());

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Pair Combination in base table
		// 2nd: Pair Combination in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "12345678901234"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.37", "12345"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892E01E0B3A73CE2FF2181CC00").getDataElements());
	}

	@Test
	public void testDecodeFixedAlphanumeric() {
		// There is no alphanumeric field in data format 9 with fixed length

		// There are only combinations with fixed numeric + variable
		// alphanumeric value

		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// single component in base table
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.253", "1234567890123"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892209447DC7EC132E00").getDataElements());

		// single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.253", "1234567890123A"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892609447DC7EC132C4300").getDataElements());

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: single component in base table
		// 2nd: single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7038", "123a"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7039", "123a"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89266CCD01EC7B8DB8B000").getDataElements());

		// 1st: single component in base table
		// 2nd: single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7038", "1235ac13"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7039", "123a7l8"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89366CCD41EC7B865322CA4D851900").getDataElements());

		// 1st: single component in base table
		// 2nd: single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7038", "1235ac135ac135ac135ac135ac13ac"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7039", "123a7l8a7l8a7l8a7l8a7l8a7l8a7l"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"8984F9B337EC7B1EE18C6319D555555425EBCA5E76EA7159FD7BE0D848A1018E60A0B596BFB7E1F123AF1C1547B33DD400")
						.getDataElements());

		// 1st: single component in base table
		// 2nd: single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.421", "123456789"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7038", "1235ac135ac135ac135ac135ac13ac"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89766023A8F63DC00C6318CD8C33A5A5DB077F22975B44BD7AF8F280756700").getDataElements());
	}

	@Test
	public void testDecodeFixedAlphanumericSecondaryTable() {
		// There is no alphanumeric field in data format 9 with fixed length

		// There are only combinations with fixed numeric + variable
		// alphanumeric value

		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// single component in base table
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.710", "1234"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A2A838F609000").getDataElements());

		// single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.710", "1234567890123A"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892C2A838F6000A2088F196100").getDataElements());
	}

	@Test
	public void testDecodeVariableAlphanumeric() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// single component in base table
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "AAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892A0247FC13C0E38E797000").getDataElements());

		// single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "123456AAAAAA123456"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "893A02401F80397D1DE4800BFC985C00").getDataElements());

		// ###################################################################################################
		// THREE ENTRIES
		// ###################################################################################################

		// 1st: Single component in base table
		// 2nd: Single component in base table
		// 3th: Single component in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "1234567890"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "1234567890"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "1234567890"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895A82E637320000000018EE90FF6C373E0EE4E3F0AD2800").getDataElements());
	}

	@Test
	public void testDecodeVariableAlphanumericSecondaryTable() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// single component in base table (Base 30 basic set)
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "ABCDEFGHIJKLMOPQRSTUVWXYZ"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89582A8B8FFFFFF83A35BE48D473683D46E43912A4757000").getDataElements());
	}

	@Test
	public void testDecodeVariableAlphanumericBaseSets() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// (characters of Base 30 shift set 1 are not allowed in data format 9 )
		// single component in base table (Base 30 shift set 1)
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "[]{}"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892205F3D6D75EDF6000").getDataElements());

		// single component in base table (Base 30 shift set 2)
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "!\"%&'()*+"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89462A8B8FFFFE57956C6885C3E46FD9575700").getDataElements());

		// single component in base table (Base 30 shift set 2)
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "!\"%&'()*+,-./:;<=>?"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "898418AA2E3FFFFFFFFF7974A4B84E5F85D8E69C3BB8D1E058A07DAED5FA984BC7D800").getDataElements());

		// single component in base table (Base 74 set)
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "abcdefghijklmnopqrstu"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895E2A8BC7FFFFDB2A604EECFA81493D7A52EF153137AD5C00").getDataElements());

		// single component in base table (Base 256 set)
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "@sA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891E0273A039A0C000").getDataElements());
	}

	@Test
	public void testDecodeVariableAlphanumericMinMax() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "A"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89162A8B886000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89142A8B8C1F00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891A2A8B8E0E8E00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "891E2A8B8F06D1B800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89222A8B8F83324AE000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89202A8B8FC17F930B00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89262A8B8FE0B3CCED2E00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892A2A8B8FF054480F2CB800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892E2A8B8FF82781C71CF2E000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "892C2A8B8FFC1284D55591CB00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89322A8B8FFE115C880038AE5C00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "89362A8B8FFF08235FC01A91B97000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "893A2A8B8FFF83D094E20C744EE5C000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "893A2A8B8FFFC1C9C5C9F5D684FB9700").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "893E2A8B8FFFE0D694B6AB3C8E55EE5C00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89422A8B8FFFF06495B5A04462B847B97000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89462A8B8FFFF82F262D23200E46619EE5C000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89462A8B8FFFFC1619E5287706B0FDC27B9700").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "894A2A8B8FFFFE0A5C236AF7CB22F6F329EE5C00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "894E2A8B8FFFFF04DB309A24273863C1FBA7B97000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89522A8B8FFFFF8246BEC840F2626EC2EDF69EE5C000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89522A8B8FFFFFC22252DBBCE33C47D6BF1734F72E00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89562A8B8FFFFFE10016D7008A8441ACA992E0D3DCB800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895A2A8B8FFFFFF0780AB4C840EDFEC8EF7CD9634F72E000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89582A8B8FFFFFF8384504BDDE6F8F6E304285E68D3DCB00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895E2A8B8FFFFFFC1A605A3900444B3BA69F2EC41234F72E00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89622A8B8FFFFFFE0C5D2A4AB8200343F61A9DEBE888D3DCB800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89662A8B8FFFFFFF05CBABD3064F0187DB5C7A069500234F72E000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89642A8B8FFFFFFF82B7788AEAF508B7AED3593315D8108D3DCB00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "896A2A8B8FFFFFFFC14600811E22DC1619F311CFF23D47C234F72E00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "896E2A8B8FFFFFFFE098D03C8620572A5C29F059798CB9A308D3DCB800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89722A8B8FFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72E000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89702A8B8FFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B96100").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "89762A8B8FFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB07E00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "897A2A8B8FFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE583A3800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "897E2A8B8FFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1B46E000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "897C2A8B8FFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960CC92B00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "898418AA2E3FFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C17F930B800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "898428AA2E3FFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960B3CCED2E000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "898420AA2E3FFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB054480F2CB00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "898438AA2E3FFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE582781C71CF2E00").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "898448AA2E3FFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1284D55591CB800").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "898458AA2E3FFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B96115C880038AE5C000").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(), UserMemoryDecode
				.decode(investigator, "898458AA2E3FFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB08235FC01A91B9700").getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "898468AA2E3FFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE583D094E20C744EE5C00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "898478AA2E3FFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1C9C5C9F5D684FB97000")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator, "898488AA2E3FFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960D694B6AB3C8E55EE5C000")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator, "898488AA2E3FFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB06495B5A04462B847B9700")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator, "898498AA2E3FFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE582F262D23200E46619EE5C00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator, "8984A8AA2E3FFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1619E5287706B0FDC27B97000")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator, "8984A8AA2E3FFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1619E5287706B0FDC27B97000")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator, "8984B8AA2E3FFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960A5C236AF7CB22F6F329EE5C000")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator, "8984B8AA2E3FFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB04DB309A24273863C1FBA7B9700")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"8984C8AA2E3FFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE58246BEC840F2626EC2EDF69EE5C00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"8984D8AA2E3FFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C22252DBBCE33C47D6BF1734F72E000")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"8984D0AA2E3FFFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B9610016D7008A8441ACA992E0D3DCB00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"8984E8AA2E3FFFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB0780AB4C840EDFEC8EF7CD9634F72E00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"8984F8AA2E3FFFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE58384504BDDE6F8F6E304285E68D3DCB800")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898508AA2E3FFFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1A605A3900444B3BA69F2EC41234F72E000")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898500AA2E3FFFFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960C5D2A4AB8200343F61A9DEBE888D3DCB00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898518AA2E3FFFFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB05CBABD3064F0187DB5C7A069500234F72E00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898528AA2E3FFFFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE582B7788AEAF508B7AED3593315D8108D3DCB800")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898538AA2E3FFFFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C14600811E22DC1619F311CFF23D47C234F72E000")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898530AA2E3FFFFFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B96098D03C8620572A5C29F059798CB9A308D3DCB00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898548AA2E3FFFFFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB047A19C5EDF28DBDB33A8A9F0F9F7046C234F72E00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898558AA2E3FFFFFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE5823D0CE2F6F946DED99D454F87CFB823611A7B961800")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898568AA2E3FFFFFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB07E000")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898560AA2E3FFFFFFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B9608F4338BDBE51B7B6675153E1F3EE08D8469EE583A300")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898578AA2E3FFFFFFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1B46E00")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898588AA2E3FFFFFFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE5823D0CE2F6F946DED99D454F87CFB823611A7B960CC92B800")
						.getDataElements());

		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"898598AA2E3FFFFFFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB05FE4C2E000")
						.getDataElements());
	}

	@Test
	public void testDecodeAlphanumericCombinations() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		PackedObjects expected = new PackedObjects(this.investigator);

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Single Combination in base table
		// 2nd: Optional Combination in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.414", "1234567890123"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.254", "123456789"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "893A15E23EE3F609960003ADE68AC000").getDataElements());

		// 1st: Pair Combination in base table
		// 2nd: Pair Combination in base table
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.415", "1234567890123"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8020", "123456789"));
		assertEquals(expected.getDataElements(), UserMemoryDecode.decode(investigator, "893416447DC7EC132C00075BCD1500").getDataElements());
	}

	@Test
	public void testPrefixAndSuffixRuns() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// 1 prefix run base 30
		PackedObjects expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "AAAAAAAAA555555555BB"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "894205DC500708E8D71845722000E2BD5000").getDataElements());

		// 1 suffix run base 30
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "BB555555555AAAAAAAAA"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "894205CE380308E8D7188AD07F1E36F97000").getDataElements());

		// 1 prefix run base 74
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "sassAsasa555555555as"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "894A05EE280384746B8E30BE115F197963257000").getDataElements());

		// 1 suffix run base 74
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "as555555555sassAsasa"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "894A05E71C0184746B8D99116F08F270E27BB000").getDataElements());

		// 1 prefix run base 256
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "@@@sAsa@@555555555@s"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895205FE280384746B8D010101CD05CD85010101CE00").getDataElements());

		// 1 suffix run base 256
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "@s555555555@@@sAsa@@"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895205F71C0184746B8D01CD010101CD05CD85010200").getDataElements());

		// 1 prefix run base 13 shift set 1
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99A999999999999999A"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "893A05DB8C92B159B2BFD3AA9290C000").getDataElements());
		
		// 1 prefix run base 13 shift set 2
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99N999999999999999A"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "893A05DB8C936732FF2A715E9790C000").getDataElements());
		
		// 1 prefix run base 13 shift set 3
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99+999999999999999A"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "893A05DB8C9462FD68F92457D990C000").getDataElements());

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################
		// 1 suffix run base 13 shift set 1
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12AA67B01CC456D99"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99A999999999999999A"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "896A42E7F13799310C1A495785120C899C5075FFFB399C18003CA800").getDataElements());
		
		// 1 suffix run base 13 shift set 2
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12AA67B01CC456D99"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99N999999999999999A"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "896A42E7F13799310C32ABDEF84DFA3157F475FFFB399C18003CA800").getDataElements());
		
		// 1 suffix run base 13 shift set 3
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12AA67B01CC456D99"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99+999999999999999A"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "896A42E7F13799310C546F5F70799205E59C75FFFB399C18003CA800").getDataElements());

		// ###################################################################################################
		// THREE ENTRIES
		// ###################################################################################################
		// 2 prefix runs base10
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "1234567890"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "1234567890"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "1234567890"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895282e63732853863ba43fdb0dcf83b938fc2b4a000").getDataElements());

		// 2 Suffix runs base10
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "1234567890"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "1234567890"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "1234567890"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895282E63732429C63BA43FDB0DCF83B938FC2B4A000").getDataElements());

		// 1 prefix run base10
		// 1 suffix run base10
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "1234567890"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "1234567890"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "1234567890"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode.decode(investigator, "895282e63732c59c63ba43fdb0dcf83b938fc2b4a000").getDataElements());

		// 2 prefix runs:
		// 1st: base 84
		// 2nd: base 74
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "ssss9sssssssssssssssssssssssss"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99s9999999ssssssssss"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12aa67b01cc456d99"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"8985BA24E84FF7A56B7101FF99312F857463DC978B596B5E3AF0C86E61651C3212E3D7F47A6A22622CB0D85832117C8FC7E3D15858D9E7077053910800")
						.getDataElements());

		// 2 suffix runs:
		// 1st: base 74
		// 2nd: base 84
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12aa67b01cc456d99"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99s9999999ssssssssss"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "sssssssssssssssssssssssss9ssss"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"8985AA0B98DF8FA4AFF9931080B43A499B2745623769E39F6F3EAB376515FAACCC509E2CFFEC229069E48F7FE0A5B8B05311B3EEE30E8F1F8F30B800")
						.getDataElements());

		// 2 prefix runs:
		// 1st: base 256
		// 2nd: base 40
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "+#$[]{}AABBCC"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99s9999999ssssssssss"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12aa67b01cc456d99"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"89852A24E84E7A7439101FF9931210D31BC87DD6BC75E190DCC2CA32B23245B5D7B7373737373737373737373616162636364800")
						.getDataElements());

		// 2 prefix runs:
		// 1st: base 256
		// 2nd: base 40 shift set 1
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "+#$[]{}AABBCC"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99[9999999ssssssssss"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12aa67b01cc456d99"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"89852A24E84E7A7439101FF9931210D31BC87DD6BC75E190DCC2CA32B23245B5D7B5B73737373737373737373616162636364800")
						.getDataElements());
		
		// 2 prefix runs:
		// 1st: base 256
		// 2nd: base 40 shift set 2
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "+#$[]{}AABBCC"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99#9999999ssssssssss"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12aa67b01cc456d99"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"89852A24E84E7A7439101FF9931210D31BC87DD6BC75E190DCC2CA32B23245B5D7B2373737373737373737373616162636364800")
						.getDataElements());
		
		// 2 suffix runs:
		// 1st: base 256
		// 2nd: base 40
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12aa67b01cc456d99"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99s9999999ssssssssss"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "+#$[]{}AABBCC"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"89851A0B98DF8FA6ABC19310808321C87D8AFD5666284F167FF61616263636473737373737373737373732B23245B5D7B7D800")
						.getDataElements());
		
		// 2 suffix runs:
		// 1st: base 256
		// 2nd: base 40 shift set 1
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12aa67b01cc456d99"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99[9999999ssssssssss"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "+#$[]{}AABBCC"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"89851A0B98DF8FA6ABC19310808321C87D8AFD5666284F167FF6161626363645B737373737373737373732B23245B5D7B7D800")
						.getDataElements());
		
		// 2 suffix runs:
		// 1st: base 256
		// 2nd: base 40 shift set 2
		expected = new PackedObjects(this.investigator);
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "12aa67b01cc456d99"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "99#9999999ssssssssss"));
		expected.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "+#$[]{}AABBCC"));
		assertEquals(expected.getDataElements(),
				UserMemoryDecode
						.decode(investigator,
								"89851A0B98DF8FA6ABC19310808321C87D8AFD5666284F167FF61616263636423737373737373737373732B23245B5D7B7D800")
						.getDataElements());
	}
	
	@Test
	public void testNumberOfReadBytes(){
		
		PackedObjects actual = UserMemoryDecode.decode(investigator, "8900");
		Assert.assertEquals(2, actual.getNumberOfReadBytes());
		
		// (urn:oid:1.0.15961.9.10", "AAAAAAAAA")
		actual = UserMemoryDecode.decode(investigator, "892A0247FC13C0E38E797000");
		Assert.assertEquals(12, actual.getNumberOfReadBytes());
		
		actual = UserMemoryDecode.decode(investigator, "89851A0B98DF8FA6ABC19310808321C87D8AFD5666284F167FF61616263636423737373737373737373732B23245B5D7B7D800");
		Assert.assertEquals(51, actual.getNumberOfReadBytes());
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
			throw new AssertionError("Expected should be null but actual is not null.");
		if (expected != null && actual == null)
			throw new AssertionError("Expected should not be null but actual is null. Expected:" + expected.toString());
		if (expected != null && actual != null) {
			if (expected.size() != actual.size())
				throw new AssertionError("Expected size is " + expected.size() + " but actual size is " + actual.size() + ". Expected:"
						+ expected.toString() + ". Actual:" + actual.toString());
			Iterator<Map.Entry<String, String>> expectedI = expected.iterator();
			Iterator<Map.Entry<String, String>> actualI = actual.iterator();
			while (expectedI.hasNext()) {
				Map.Entry<String, String> expectedE = expectedI.next();
				Map.Entry<String, String> actualE = actualI.next();
				try {
					Assert.assertEquals(expectedE.getKey(), actualE.getKey());
					Assert.assertEquals(expectedE.getValue(), actualE.getValue());
				} catch (AssertionError e) {
					System.out.println("Expected:" + expected.toString() + ". Actual:" + actual.toString());
					throw e;
				}
			}
		}
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
}