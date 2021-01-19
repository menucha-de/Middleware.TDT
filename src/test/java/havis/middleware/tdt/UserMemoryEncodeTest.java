package havis.middleware.tdt;

import havis.middleware.tdt.PackedObjects.ObjectInfoFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserMemoryEncodeTest {

	PackedObjectInvestigator investigator;

	@Before
	public void init() throws IOException, URISyntaxException {
		this.investigator = new PackedObjectInvestigator(getTestData());
	}

	@Test
	public void testPerformance(){
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3372", "123456"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.254", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.415", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.421", "123456789"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.37", "1"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.420", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.414", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "12345678901234"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3370", "123456"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8020", "1234567890123"));
		
		int runs = 500;
		long millis = System.currentTimeMillis();
		for (int i = 0; i < runs; i++)
			UserMemoryEncode.encode(investigator, packedObjects);
		System.out.println((System.currentTimeMillis() - millis) / runs);
	}
	
	@Test
	public void testEncodeSmokeTest() {
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3372", "123456"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.254", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.415", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.421", "123456789"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.37", "1"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.420", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.414", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "12345678901234"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3370", "123456"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8020", "1234567890123"));
		Assert.assertEquals(
				"8985CD4B6B7D284ED02C398A07890047DC7EC132C47DC7EC132C7B1674E79C5FE423C4800000000000002C49B7293D5E172A832F9E8CF66B222011265C00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "123456789012345678"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.1", "12345678901234"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "12345678901234"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.37", "1"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "123456sagbhloN"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.11", "000000"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.242", "555"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.253", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3105", "999999"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3201", "888888"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3300", "333555"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3400", "222111"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3372", "123456"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.414", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.254", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.415", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8020", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.420", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.421", "123456789"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7038", "1235ac135ac135ac135ac135ac13ac"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7039", "123a7l8a7l8a7l8a7l8a7l8a7l8a7l"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.427", "ac1"));
		Assert.assertEquals(
				"89941D880B81120E899A0E449447A53FFECCD54A90854146A0CCC5FBFB1B69B4BA630F34E1674E79C5FE42CE9CF38BFC84000022B11F71FB04CBF423FD9038516F33639F1E24011F71FB04CB11F71FB04CB1EC7B1EE00FF00000000000318C633AAAAAAB8567FDB2CFA8EF45863CCF5A4CA5B68EAFA56EB01F240C9961B099BFA9A97CDAD8FC2E1281AA36572C784789DC647DEAC0A5CD4057BE9E0AEAFDA194701E00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.253", "12345678901231234"));
		Assert.assertEquals("892E09447DC7EC132C009A5000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "999999999999999999"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "88888888888888"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.37", "77777777"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.17", "666666"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.253", "5555555555555lllkkkjjj"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.251", "1KBN8Q+LKOppphhh"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.241", "MMMABCDEFGHJ555666777"));
		Assert.assertEquals(
				"89889D205D19E1823E7C3782DACE9D8FFFFE86C0752C71C4A2CB71A2C2A50D80EA58E38FFBBFFFFF8008A40202B2EF99DF736A76AD618C247DC5968848F1018841077365205967E126E000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "010803589570995176"));
		Assert.assertEquals("892A00C099873ADAB68FA200", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "IkMtw0E2"));
		Assert.assertEquals("892A0263E811DBC1471C5000", UserMemoryEncode.encode(investigator, packedObjects));
	}

	@Test
	public void testEncodeEmpty() {
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		Assert.assertEquals("8900", UserMemoryEncode.encode(investigator, packedObjects));
	}

	@Test
	public void testEncodeFormatFlags() {
		// Format Flag IDLPO DEFAULT = 00001000
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7004", "9"));
		Assert.assertEquals("8908101B4900", UserMemoryEncode.encode(investigator, packedObjects));
	}

	/**
	 * no prefix and suffix runs
	 * 
	 * @
	 */
	@Test
	public void testEncodeFixedNumeric() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single component in base table
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "123456789012345678"));
		Assert.assertEquals("892A00C6DA6D2E98C3CD3A00", UserMemoryEncode.encode(investigator, packedObjects));

		// Single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.1", "12345678901234"));
		Assert.assertEquals("892601459D39E717F94000", UserMemoryEncode.encode(investigator, packedObjects));

		// Single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.11", "123456"));
		Assert.assertEquals("891602C7890200", UserMemoryEncode.encode(investigator, packedObjects));

		// Single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.20", "12"));
		Assert.assertEquals("891205464000", UserMemoryEncode.encode(investigator, packedObjects));

		// Series in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3371", "123456"));
		Assert.assertEquals("891A1018F1204000", UserMemoryEncode.encode(investigator, packedObjects));

		// Selection and Series in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3305", "123456"));
		Assert.assertEquals("891A0D2C78902000", UserMemoryEncode.encode(investigator, packedObjects));

		// Selection(2nd) and Series in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3400", "480231"));
		Assert.assertEquals("891A0D45D4F9E000", UserMemoryEncode.encode(investigator, packedObjects));

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Single component in base table
		// 2nd: Single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "123456789012345678"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.1", "12345678901234"));
		Assert.assertEquals("894640B91B69B4BA630F34E1674E79C5FE5000", UserMemoryEncode.encode(investigator, packedObjects));

		// ###################################################################################################
		// THREE ENTRIES
		// ###################################################################################################

		// 1st: Single component in base table
		// 2nd: Single component in base table
		// 3th: Single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "123456789012345678"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.1", "12345678901234"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.11", "123456"));
		Assert.assertEquals("895280B81636D36974C61E69C2CE9CF38BFC87890200", UserMemoryEncode.encode(investigator, packedObjects));
	}

	/**
	 * no prefix and suffix runs
	 * 
	 * @
	 */
	@Test
	public void testEncodeFixedNumericSecondaryTable() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single component in secondary table
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8017", "123456789012345678"));
		Assert.assertEquals("892E2A898DB4DA5D31879A7400", UserMemoryEncode.encode(investigator, packedObjects));

		// Single component in secondary table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8100", "123456"));
		Assert.assertEquals("891A2508F1204000", UserMemoryEncode.encode(investigator, packedObjects));

		// Series component in secondary table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3106", "123456"));
		Assert.assertEquals("891A0A1A3C481000", UserMemoryEncode.encode(investigator, packedObjects));

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Single component in secondary table
		// 2nd: Series component in secondary table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8100", "555555"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3106", "123456"));
		Assert.assertEquals("892474300D87A231E24000", UserMemoryEncode.encode(investigator, packedObjects));

		// ###################################################################################################
		// THREE ENTRIES
		// ###################################################################################################

		// 1st: Single component in secondary table
		// 2nd: Series component in secondary table
		// 3th: Single component in secondary table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8100", "555555"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3106", "123456"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8017", "123456789012345678"));
		Assert.assertEquals("894EB43154184E1E88C789006DA6D2E98C3CD3A000", UserMemoryEncode.encode(investigator, packedObjects));

	}

	@Test
	public void testEncodeVariableNumeric() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single component in base table
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.30", "123456"));
		Assert.assertEquals("891A09E8F1204000", UserMemoryEncode.encode(investigator, packedObjects));

		// Selection and Series in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3900", "123456789012345"));
		Assert.assertEquals("892A1083C38244306EFBCC00", UserMemoryEncode.encode(investigator, packedObjects));

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Selection and Series in base table
		// 2nd: Single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.3900", "123456789012345"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.30", "123456"));
		Assert.assertEquals("893A575A0F51C12218377DE478902000", UserMemoryEncode.encode(investigator, packedObjects));

	}

	@Test
	public void testEncodeVariableNumericSecondaryTable() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single component in secondary table
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8011", "123456"));
		Assert.assertEquals("891E2A88A8F1204000", UserMemoryEncode.encode(investigator, packedObjects));

		// Single component in secondary table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8019", "0"));
		Assert.assertEquals("89162A8A804000", UserMemoryEncode.encode(investigator, packedObjects));

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Single component in secondary table
		// 2nd: Single component in secondary table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8011", "2389746"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8019", "9998887771"));
		Assert.assertEquals("89347C6E2055A491DBCA53FAEB5B00", UserMemoryEncode.encode(investigator, packedObjects));

	}

	/**
	 * no prefix and suffix runs
	 * 
	 * @
	 */
	@Test
	public void testEncodeVariableNumericMinMax() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123"));
		Assert.assertEquals("891017C07B00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "1234"));
		Assert.assertEquals("891617C44D2800", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "12345"));
		Assert.assertEquals("891617C8607300", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123456"));
		Assert.assertEquals("891A17CC78902000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "1234567"));
		Assert.assertEquals("891A17D04B5A1E00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "12345678"));
		Assert.assertEquals("891E17D45E30A74000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123456789"));
		Assert.assertEquals("891E17D875BCD15800", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "1234567890"));
		Assert.assertEquals("891C17DC499602D200", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "12345678901"));
		Assert.assertEquals("892217E05BFB8386B000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123456789012"));
		Assert.assertEquals("892217E472FA64685200", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "1234567890123"));
		Assert.assertEquals("892617E847DC7EC132E000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "12345678901234"));
		Assert.assertEquals("892617EC59D39E717F9400", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.423", "123456789012345"));
		Assert.assertEquals("892417F07048860DDF7900", UserMemoryEncode.encode(investigator, packedObjects));
	}

	@Test
	public void testEncodeNumericCombinations() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// Single Combination in base table
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.414", "1234567890123"));
		Assert.assertEquals("892215A23EE3F6099700", UserMemoryEncode.encode(investigator, packedObjects));

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Pair Combination in base table
		// 2nd: Pair Combination in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "12345678901234"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.37", "12345"));
		Assert.assertEquals("892E01E0B3A73CE2FF2181CC00", UserMemoryEncode.encode(investigator, packedObjects));
	}

	@Test
	public void testEncodeFixedAlphanumeric() {
		// There is no alphanumeric field in data format 9 with fixed length

		// There are only combinations with fixed numeric + variable
		// alphanumeric value

		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// single component in base table
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.253", "1234567890123"));
		Assert.assertEquals("892209447DC7EC132E00", UserMemoryEncode.encode(investigator, packedObjects));

		// single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.253", "1234567890123A"));
		Assert.assertEquals("892609447DC7EC132C4300", UserMemoryEncode.encode(investigator, packedObjects));

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: single component in base table
		// 2nd: single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7038", "123a"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7039", "123a"));
		Assert.assertEquals("89266CCD01EC7B8DB8B000", UserMemoryEncode.encode(investigator, packedObjects));

		// 1st: single component in base table
		// 2nd: single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7038", "1235ac13"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7039", "123a7l8"));
		Assert.assertEquals("89366CCD41EC7B865322CA4D851900", UserMemoryEncode.encode(investigator, packedObjects));

		// 1st: single component in base table
		// 2nd: single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7038", "1235ac135ac135ac135ac135ac13ac"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7039", "123a7l8a7l8a7l8a7l8a7l8a7l8a7l"));
		Assert.assertEquals("8984F9B337EC7B1EE18C6319D555555425EBCA5E76EA7159FD7BE0D848A1018E60A0B596BFB7E1F123AF1C1547B33DD400",
				UserMemoryEncode.encode(investigator, packedObjects));

		// 1st: single component in base table
		// 2nd: single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.421", "123456789"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.7038", "1235ac135ac135ac135ac135ac13ac"));
		Assert.assertEquals("89766023A8F63DC00C6318CD8C33A5A5DB077F22975B44BD7AF8F280756700",
				UserMemoryEncode.encode(investigator, packedObjects));
	}

	@Test
	public void testEncodeFixedAlphanumericSecondaryTable() {
		// There is no alphanumeric field in data format 9 with fixed length

		// There are only combinations with fixed numeric + variable
		// alphanumeric value

		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// single component in base table
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.710", "1234"));
		Assert.assertEquals("891A2A838F609000", UserMemoryEncode.encode(investigator, packedObjects));

		// single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.710", "1234567890123A"));
		Assert.assertEquals("892C2A838F6000A2088F196100", UserMemoryEncode.encode(investigator, packedObjects));
	}

	@Test
	public void testEncodeVariableAlphanumeric() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// single component in base table
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "AAAAAAAAA"));
		Assert.assertEquals("892A0247FC13C0E38E797000", UserMemoryEncode.encode(investigator, packedObjects));

		// single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "123456AAAAAA123456"));
		Assert.assertEquals("893A02401F80397D1DE4800BFC985C00", UserMemoryEncode.encode(investigator, packedObjects));

		// ###################################################################################################
		// THREE ENTRIES
		// ###################################################################################################

		// 1st: Single component in base table
		// 2nd: Single component in base table
		// 3th: Single component in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "1234567890"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "1234567890"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.240", "1234567890"));
		Assert.assertEquals("895A82E637320000000018EE90FF6C373E0EE4E3F0AD2800", UserMemoryEncode.encode(investigator, packedObjects));
	}

	@Test
	public void testEncodeVariableAlphanumericSecondaryTable() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// single component in base table (Base 30 basic set)
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "ABCDEFGHIJKLMOPQRSTUVWXYZ"));
		Assert.assertEquals("89582A8B8FFFFFF83A35BE48D473683D46E43912A4757000", UserMemoryEncode.encode(investigator, packedObjects));
	}

	@Test
	public void testEncodeVariableAlphanumericBaseSets() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		// (characters of Base 30 shift set 1 are not allowed in data format 9 )
		// single component in base table (Base 30 shift set 2)
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "!\"%&'()*+"));
		Assert.assertEquals("89462A8B8FFFFE57956C6885C3E46FD9575700", UserMemoryEncode.encode(investigator, packedObjects));

		// single component in base table (Base 30 shift set 2)
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "!\"%&'()*+,-./:;<=>?"));
		Assert.assertEquals("898418AA2E3FFFFFFFFF7974A4B84E5F85D8E69C3BB8D1E058A07DAED5FA984BC7D800",
				UserMemoryEncode.encode(investigator, packedObjects));

		// single component in base table (Base 74 set)
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "abcdefghijklmnopqrstu"));
		Assert.assertEquals("895E2A8BC7FFFFDB2A604EECFA81493D7A52EF153137AD5C00", UserMemoryEncode.encode(investigator, packedObjects));

		try {
			// single component in base table (characters of Base 256)
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.10", "@sA"));
			Assert.assertEquals("891E0273A039A0C000", UserMemoryEncode.encode(investigator, packedObjects));
		} catch (Exception e) {
			// TODO ONLY AN INFORMATION FOR TESTING
			// Assert.fail("Read comment!");
			// @ is no valid character for a value of data format 9. (See GS1
			// General Specifications Release 16.0, Ratified, Jan 2016 - 3.2 GS1
			// Application Identifiers in numerical order)

			// Therefore you must remove the validation of the values to check
			// the correct behavior of encoding in base256
		}
	}

	/**
	 * no prefix and suffix runs
	 * 
	 * @
	 */
	@Test
	public void testEncodeVariableAlphanumericMinMax() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		PackedObjects packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "A"));
		Assert.assertEquals("89162A8B886000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AA"));
		Assert.assertEquals("89142A8B8C1F00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAA"));
		Assert.assertEquals("891A2A8B8E0E8E00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAA"));
		Assert.assertEquals("891E2A8B8F06D1B800", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAA"));
		Assert.assertEquals("89222A8B8F83324AE000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAA"));
		Assert.assertEquals("89202A8B8FC17F930B00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAA"));
		Assert.assertEquals("89262A8B8FE0B3CCED2E00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAA"));
		Assert.assertEquals("892A2A8B8FF054480F2CB800", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAA"));
		Assert.assertEquals("892E2A8B8FF82781C71CF2E000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAA"));
		Assert.assertEquals("892C2A8B8FFC1284D55591CB00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAA"));
		Assert.assertEquals("89322A8B8FFE115C880038AE5C00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAA"));
		Assert.assertEquals("89362A8B8FFF08235FC01A91B97000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAA"));
		Assert.assertEquals("893A2A8B8FFF83D094E20C744EE5C000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAA"));
		Assert.assertEquals("893A2A8B8FFFC1C9C5C9F5D684FB9700", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAA"));
		Assert.assertEquals("893E2A8B8FFFE0D694B6AB3C8E55EE5C00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89422A8B8FFFF06495B5A04462B847B97000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89462A8B8FFFF82F262D23200E46619EE5C000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89462A8B8FFFFC1619E5287706B0FDC27B9700", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("894A2A8B8FFFFE0A5C236AF7CB22F6F329EE5C00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("894E2A8B8FFFFF04DB309A24273863C1FBA7B97000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89522A8B8FFFFF8246BEC840F2626EC2EDF69EE5C000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89522A8B8FFFFFC22252DBBCE33C47D6BF1734F72E00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89562A8B8FFFFFE10016D7008A8441ACA992E0D3DCB800", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("895A2A8B8FFFFFF0780AB4C840EDFEC8EF7CD9634F72E000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89582A8B8FFFFFF8384504BDDE6F8F6E304285E68D3DCB00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("895E2A8B8FFFFFFC1A605A3900444B3BA69F2EC41234F72E00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89622A8B8FFFFFFE0C5D2A4AB8200343F61A9DEBE888D3DCB800", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89662A8B8FFFFFFF05CBABD3064F0187DB5C7A069500234F72E000", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89642A8B8FFFFFFF82B7788AEAF508B7AED3593315D8108D3DCB00", UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("896A2A8B8FFFFFFFC14600811E22DC1619F311CFF23D47C234F72E00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("896E2A8B8FFFFFFFE098D03C8620572A5C29F059798CB9A308D3DCB800",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89722A8B8FFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72E000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89702A8B8FFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B96100",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("89762A8B8FFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB07E00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("897A2A8B8FFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE583A3800",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("897E2A8B8FFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1B46E000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("897C2A8B8FFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960CC92B00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898418AA2E3FFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C17F930B800",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898428AA2E3FFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960B3CCED2E000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898420AA2E3FFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB054480F2CB00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898438AA2E3FFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE582781C71CF2E00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898448AA2E3FFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1284D55591CB800",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898458AA2E3FFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B96115C880038AE5C000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898458AA2E3FFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB08235FC01A91B9700",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898468AA2E3FFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE583D094E20C744EE5C00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898478AA2E3FFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1C9C5C9F5D684FB97000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898488AA2E3FFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960D694B6AB3C8E55EE5C000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898488AA2E3FFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB06495B5A04462B847B9700",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898498AA2E3FFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE582F262D23200E46619EE5C00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("8984A8AA2E3FFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1619E5287706B0FDC27B97000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("8984A8AA2E3FFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1619E5287706B0FDC27B97000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("8984B8AA2E3FFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960A5C236AF7CB22F6F329EE5C000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("8984B8AA2E3FFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB04DB309A24273863C1FBA7B9700",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("8984C8AA2E3FFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE58246BEC840F2626EC2EDF69EE5C00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements()
				.add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("8984D8AA2E3FFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C22252DBBCE33C47D6BF1734F72E000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("8984D0AA2E3FFFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B9610016D7008A8441ACA992E0D3DCB00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("8984E8AA2E3FFFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB0780AB4C840EDFEC8EF7CD9634F72E00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("8984F8AA2E3FFFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE58384504BDDE6F8F6E304285E68D3DCB800",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898508AA2E3FFFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1A605A3900444B3BA69F2EC41234F72E000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(
				new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898500AA2E3FFFFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B960C5D2A4AB8200343F61A9DEBE888D3DCB00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898518AA2E3FFFFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB05CBABD3064F0187DB5C7A069500234F72E00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898528AA2E3FFFFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE582B7788AEAF508B7AED3593315D8108D3DCB800",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898538AA2E3FFFFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C14600811E22DC1619F311CFF23D47C234F72E000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898530AA2E3FFFFFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B96098D03C8620572A5C29F059798CB9A308D3DCB00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals("898548AA2E3FFFFFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB047A19C5EDF28DBDB33A8A9F0F9F7046C234F72E00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals(
				"898558AA2E3FFFFFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE5823D0CE2F6F946DED99D454F87CFB823611A7B961800",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals(
				"898568AA2E3FFFFFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB07E000",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals(
				"898560AA2E3FFFFFFFFFFFFFFFF823D0CE2F6F946DED99D454F87CFB823611A7B9608F4338BDBE51B7B6675153E1F3EE08D8469EE583A300",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals(
				"898578AA2E3FFFFFFFFFFFFFFFFC11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C1B46E00",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals(
				"898588AA2E3FFFFFFFFFFFFFFFFE08F4338BDBE51B7B6675153E1F3EE08D8469EE5823D0CE2F6F946DED99D454F87CFB823611A7B960CC92B800",
				UserMemoryEncode.encode(investigator, packedObjects));

		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8200",
				"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		Assert.assertEquals(
				"898598AA2E3FFFFFFFFFFFFFFFFF047A19C5EDF28DBDB33A8A9F0F9F7046C234F72C11E86717B7CA36F6CCEA2A7C3E7DC11B08D3DCB05FE4C2E000",
				UserMemoryEncode.encode(investigator, packedObjects));
	}

	@Test
	public void testEncodeAlphanumericCombinations() {
		// ###################################################################################################
		// ONE ENTRY
		// ###################################################################################################

		PackedObjects packedObjects = new PackedObjects(this.investigator);

		// ###################################################################################################
		// TWO ENTRIES
		// ###################################################################################################

		// 1st: Single Combination in base table
		// 2nd: Optional Combination in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.414", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.254", "123456789"));
		Assert.assertEquals("893A15E23EE3F609960003ADE68AC000", UserMemoryEncode.encode(investigator, packedObjects));

		// 1st: Pair Combination in base table
		// 2nd: Pair Combination in base table
		packedObjects = new PackedObjects(this.investigator);
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.415", "1234567890123"));
		packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8020", "123456789"));
		Assert.assertEquals("893416447DC7EC132C00075BCD1500", UserMemoryEncode.encode(investigator, packedObjects));

	}
	
	@Test
	public void testExceptions() {
		PackedObjects packedObjects = new PackedObjects(this.investigator);
		// invalid access method
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.setDsfid("00");
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
		// invalid access method
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.setDsfid("40");
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
		// invalid access method
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.setDsfid("C0");
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
		// invalid extended bit
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.setDsfid("A0");
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
		// invalid data format
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.setDsfid("8A");
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
		
		// invalid Compaction method
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.setObjectInfoFormat(ObjectInfoFormat.IDLPO_NON_DEFAULT);
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
		
		// invalid Compaction method
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.setObjectInfoFormat(ObjectInfoFormat.IDMPO);
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
		
		// Invalid data
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.21", "€"));
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
		
		// OID duplicate
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "123456789012345678"));
			packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.0", "123456789012345678"));
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
				
		// Missing combination
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "12345678901234"));
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
		}
		
		// Missing combination
		try {
			packedObjects = new PackedObjects(this.investigator);
			packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.254", "12345678901234"));
			UserMemoryEncode.encode(investigator, packedObjects);
			Assert.fail("Exception has been expected.");
		} catch (Exception e){
			// success
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