package havis.middleware.tdt;

import havis.middleware.tdt.TdtResources;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.resources.client.TextResource;

public class PackedObjectInvestigatorGWTTest extends GWTTestCase {
	
	PackedObjectInvestigatorTest packedObjectInvestigatorTest;
	
	@Override
	public void gwtSetUp() {
		String data = getTestData();
		packedObjectInvestigatorTest = new PackedObjectInvestigatorTest(data);
	}

	@Override
	public String getModuleName() {
		return "havis.middleware.tdt.TDT";
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

	public void testF9S09Header() throws Exception {
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

	private String getTestData() {
		TextResource tableForData = TdtResources.INSTANCE.tableForData();
		String data = tableForData.getText();

		return data;
	}
}
