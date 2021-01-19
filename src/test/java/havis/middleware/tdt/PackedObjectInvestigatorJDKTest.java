package havis.middleware.tdt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Date;

import junit.framework.TestCase;

public class PackedObjectInvestigatorJDKTest extends TestCase {

	private PackedObjectInvestigatorTest packedObjectInvestigatorTest;

	@Override
	public void setUp() throws Exception {
		String data = getTestData();
		packedObjectInvestigatorTest = new PackedObjectInvestigatorTest(data);
	}

	public void testF9B0HeaderFail() throws Exception {
		packedObjectInvestigatorTest.testF9B0HeaderFail();
	}

	public void testF9B0Header() throws Exception {
		packedObjectInvestigatorTest.testF9B0Header();
	}

	public void testF9S00Header() throws Exception {
		packedObjectInvestigatorTest.testF9S00Header();
	}

	public void testF9S01Header() throws Exception {
		packedObjectInvestigatorTest.testF9S01Header();
	}

	public void testF9S02Header() throws Exception {
		packedObjectInvestigatorTest.testF9S02Header();
	}

	public void testF9S03Header() throws Exception {
		packedObjectInvestigatorTest.testF9S03Header();
	}

	public void testF9S04Header() throws Exception {
		packedObjectInvestigatorTest.testF9S04Header();
	}

	public void testF9S05Header() throws Exception {
		packedObjectInvestigatorTest.testF9S05Header();
	}

	public void testF9S06Header() throws Exception {
		packedObjectInvestigatorTest.testF9S06Header();
	}

	public void testF9S07Header() throws Exception {
		packedObjectInvestigatorTest.testF9S07Header();
	}

	public void testF9S08Header() throws Exception {
		packedObjectInvestigatorTest.testF9S08Header();
	}

	public void f9S09Header() throws Exception {
		packedObjectInvestigatorTest.testF9S09Header();
	}

	public void testF9S10Header() throws Exception {
		packedObjectInvestigatorTest.testF9S10Header();
	}

	public void testF9S11Header() throws Exception {
		packedObjectInvestigatorTest.testF9S11Header();
	}

	public void testF9S12Header() throws Exception {
		packedObjectInvestigatorTest.testF9S12Header();
	}

	public void testPerformance() throws Exception {

		// ###########################################init###########################################
		Date initStart = new Date();
		PackedObjectInvestigator objectsTest = new PackedObjectInvestigator(getTestData());
		Date initEnd = new Date();

		long initDiff = initEnd.getTime() - initStart.getTime();
		System.out.println("Init time: " + initDiff + "ms");

		TestCase.assertTrue(initDiff < 20);
		// ###########################################init###########################################

		Thread.sleep(100);

		// #######################################large
		// table########################################
		Date lTStart = new Date();
		String lTValue = objectsTest.getData("F9B0", PackedObjectInvestigator.ColumnName.FORMAT_9_FORMAT_STRING, 85);
		Date lTEnd = new Date();

		long lTSDiff = lTEnd.getTime() - lTStart.getTime();
		System.out.println("Last entry of table F9B0 time: " + lTSDiff + "ms");

		TestCase.assertTrue(lTSDiff < 20);
		TestCase.assertNotNull(lTValue);
		// #######################################large
		// table########################################

		Thread.sleep(100);

		// #######################################last
		// entry#########################################
		Date lEStart = new Date();
		String lEValue = objectsTest.getData("F9S12", PackedObjectInvestigator.ColumnName.FORMAT_9_OIDS, 12);
		Date lEEnd = new Date();

		long lEDiff = lEEnd.getTime() - lEStart.getTime();
		System.out.println("Last table and last entry time: " + lEDiff + "ms");

		TestCase.assertTrue(lEDiff < 20);
		TestCase.assertNotNull(lEValue);
		// #######################################last
		// entry#########################################
	}

	public void testF9B0Fail() throws Exception {
		packedObjectInvestigatorTest.testF9B0Fail();
	}

	public void testF9B0() throws Exception {
		packedObjectInvestigatorTest.testF9B0();
	}

	public void testF9S00() throws Exception {
		packedObjectInvestigatorTest.testF9S00();
	}

	public void testF9S01() throws Exception {
		packedObjectInvestigatorTest.testF9S01();
	}

	public void testF9S02() throws Exception {
		packedObjectInvestigatorTest.testF9S02();
	}

	public void testF9S03() throws Exception {
		packedObjectInvestigatorTest.testF9S03();
	}

	public void testF9S04() throws Exception {
		packedObjectInvestigatorTest.testF9S04();
	}

	public void testF9S05() throws Exception {
		packedObjectInvestigatorTest.testF9S05();
	}

	public void testF9S06() throws Exception {
		packedObjectInvestigatorTest.testF9S06();
	}

	public void testF9S07() throws Exception {
		packedObjectInvestigatorTest.testF9S07();
	}

	public void testF9S08() throws Exception {
		packedObjectInvestigatorTest.testF9S08();
	}

	public void testF9S09() throws Exception {
		packedObjectInvestigatorTest.testF9S09();
	}

	public void testF9S10() throws Exception {
		packedObjectInvestigatorTest.testF9S10();
	}

	public void testF9S11() throws Exception {
		packedObjectInvestigatorTest.testF9S11();
	}

	public void testF9S12() throws Exception {
		packedObjectInvestigatorTest.testF9S12();
	}

	public void testGetOids() {
		packedObjectInvestigatorTest.testGetOids();		
	}
	
	public void testGetOidEntry() throws Exception {
		packedObjectInvestigatorTest.testGetOidEntry();
	}
	
	public void testGetOidEntryPerformance() throws Exception {
		packedObjectInvestigatorTest.testGetOidEntryPerformance();
	}

	public void testDecodePackedObjects() throws Exception {
		packedObjectInvestigatorTest.testDecodePackedObjects();
	}
	
	public void testEncodePackedObjects() throws Exception {
		packedObjectInvestigatorTest.testEncodePackedObjects();
	}

	private String getTestData() throws Exception {
		StringBuffer data = new StringBuffer();
		final String EOL = PackedObjectInvestigator.EOL;

		URL resource = getClass().getResource("table-for-data.csv");
		File file = new File(resource.toURI());
		FileReader in = new FileReader(file);
		BufferedReader reader = new BufferedReader(in);
		String line = "";

		while ((line = reader.readLine()) != null) {
			data.append(line + EOL);
		}

		reader.close();

		return data.toString();
	}
}
