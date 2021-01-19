package havis.middleware.tdt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExtensiveTests {

	Barcode clazz;

	PackedObjectInvestigator investigator;
	Barcode barcode;

	@Before
	public void init() throws IOException, URISyntaxException {
		this.clazz = new Barcode(getTestData());

		this.investigator = new PackedObjectInvestigator(getTestData());
		this.barcode = new Barcode(investigator);
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

	@Test
	public void testDecodeGs1128BarcodeAll() throws IOException, URISyntaxException {
		// + decode 1 AI of each table entry
		LinkedHashMap<String, LinkedHashMap<String, String>> actual = getBarcodesTestData();
		for (String barcode : actual.keySet())
			clazz.decodeGs1128Barcode(barcode);
	}

	@SuppressWarnings("unchecked")
	private LinkedHashMap<String, LinkedHashMap<String, String>> getBarcodesTestData() throws URISyntaxException, JsonParseException, JsonMappingException,
			IOException {
		URL resource = getClass().getResource("barcodes.json");
		File file = new File(resource.toURI());
		ObjectMapper mapper = new ObjectMapper();
		LinkedHashMap<String, LinkedHashMap<String, String>> data = mapper.readValue(file, LinkedHashMap.class);
		return data;
	}

	@Test
	public void testEnDecodeAll() throws JsonParseException, JsonMappingException, URISyntaxException, IOException {
		// + decode 1 AI of each table entry
		LinkedHashMap<String, LinkedHashMap<String, String>> actual = getBarcodesTestData();
		for (String b : actual.keySet()) {

			List<Map.Entry<String, String>> entryList = barcode.decodeGs1128Barcode(b);
			PackedObjects packedObjects = new PackedObjects(this.investigator);
			String oid = "urn:oid:1.0.15961.9."
					+ (entryList.get(0).getKey().length() > 1 && entryList.get(0).getKey().startsWith("0") ? entryList.get(0).getKey().substring(1) : entryList
							.get(0).getKey());
			packedObjects.getDataElements().add(new SimpleEntry<String, String>(oid, entryList.get(0).getValue()));
			if (oid.equals("urn:oid:1.0.15961.9.2")) {
				packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.37", "1"));
			} else if (oid.equals("urn:oid:1.0.15961.9.37")) {
				packedObjects.getDataElements().add(0, new SimpleEntry<String, String>("urn:oid:1.0.15961.9.2", "12345678901234"));
			} else if (oid.equals("urn:oid:1.0.15961.9.254")) {
				packedObjects.getDataElements().add(0, new SimpleEntry<String, String>("urn:oid:1.0.15961.9.414", "1234567890123"));
			} else if (oid.equals("urn:oid:1.0.15961.9.415")) {
				packedObjects.getDataElements().add(new SimpleEntry<String, String>("urn:oid:1.0.15961.9.8020", "1234567890123"));
			} else if (oid.equals("urn:oid:1.0.15961.9.8020")) {
				packedObjects.getDataElements().add(0, new SimpleEntry<String, String>("urn:oid:1.0.15961.9.415", "1234567890123"));
			}

			List<Map.Entry<String, String>> expected = new ArrayList<Map.Entry<String, String>>(packedObjects.getDataElements());
			assertEquals(expected, UserMemoryDecode.decode(investigator, UserMemoryEncode.encode(investigator, packedObjects)).getDataElements());
		}
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
				throw new AssertionError("Expected size is " + expected.size() + " but actual size is " + actual.size() + ". Expected:" + expected.toString()
						+ ". Actual:" + actual.toString());
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
}
