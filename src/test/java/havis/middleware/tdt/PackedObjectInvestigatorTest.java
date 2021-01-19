package havis.middleware.tdt;

import havis.middleware.tdt.PackedObjectInvestigator.ColumnName;
import havis.middleware.tdt.PackedObjectInvestigator.TableHeader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public final class PackedObjectInvestigatorTest {

	private String idTableData;

	public PackedObjectInvestigatorTest(String idTableData) {
		this.idTableData = idTableData;
	}

	public String getIdTableData() {
		return idTableData;
	}

	public void testF9B0HeaderFail() throws Exception {
		// Date start = new Date();
		String tableID = "ZUGHGG";

		PackedObjectInvestigator objectsTest = new PackedObjectInvestigator(idTableData);

		String value = objectsTest.getHeader(tableID, TableHeader.K_ROOT_OID);

		TestCase.assertNull(value);
	}

	public void testF9B0Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9B0";

		String[] expected = { "GS1 AI ID Table for ISO/IEC 15961 Format 9", "1.00", "05", "Primary Base Table", "urn:oid:1.0.15961.9",
				"90" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_MAIN, TableHeader.K_VERSION, TableHeader.K_ISO15434, TableHeader.K_TEXT_SUB,
				TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S00Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S00";

		String[] expected = { "Sec. IDT - Net weight, kilograms or pounds or troy oz (Variable Measure Trade Item)", "urn:oid:1.0.15961.9",
				"4" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S01Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S01";

		String[] expected = { "Sec. IDT - Length of first dimension (Variable Measure Trade Item)", "urn:oid:1.0.15961.9", "4" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S02Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S02";

		String[] expected = { "Sec. IDT - Width, diameter, or second dimension (Variable Measure Trade Item)", "urn:oid:1.0.15961.9", "4" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S03Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S03";

		String[] expected = { "Sec. IDT - Depth, thickness, height, or third dimension (Variable Measure Trade Item)",
				"urn:oid:1.0.15961.9", "4" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S04Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S04";

		String[] expected = { "Sec. IDT - Area (Variable Measure Trade Item)", "urn:oid:1.0.15961.9", "4" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S05Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S05";

		String[] expected = { "Sec. IDT - Net volume (Variable Measure Trade Item)", "urn:oid:1.0.15961.9", "8" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S06Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S06";

		String[] expected = { "Sec. IDT - Logistic Volume", "urn:oid:1.0.15961.9", "8" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S07Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S07";

		String[] expected = { "Sec. IDT - Logistic Area", "urn:oid:1.0.15961.9", "4" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S08Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S08";

		String[] expected = { "Sec. IDT - Coupon Codes", "urn:oid:1.0.15961.9", "8" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S09Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S09";

		String[] expected = { "Sec. IDT - Length or first dimension", "urn:oid:1.0.15961.9", "4" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S10Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S10";

		String[] expected = { "Sec. IDT - Width, diameter, or second dimension", "urn:oid:1.0.15961.9", "4" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S11Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S11";

		String[] expected = { "Sec. IDT - Depth, thickness, height, or third dimension", "urn:oid:1.0.15961.9", "4" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9S12Header() throws Exception {
		// Date start = new Date();
		String tableID = "F9S12";

		String[] expected = { "Sec. IDT - Additional AIs", "urn:oid:1.0.15961.9", "128" };
		TableHeader[] tabHeaders = { TableHeader.K_TEXT_SUB, TableHeader.K_ROOT_OID, TableHeader.K_ID_SIZE };

		testHeader(tableID, expected, tabHeaders);
	}

	public void testF9B0Fail() throws Exception {
		String tableID = "F9B0";

		PackedObjectInvestigator objectsTest = new PackedObjectInvestigator(idTableData);
		String value = objectsTest.getData(tableID, ColumnName.FORMAT_9_AI_AIS, 85);

		TestCase.assertNotNull(value);

		value = objectsTest.getData(tableID, ColumnName.FORMAT_9_AI_AIS, 86);

		TestCase.assertNull(value);
	}

	public void testF9B0() throws Exception {
		// Date start = new Date();
		String tableID = "F9B0";

		String[][] vals = { { "0", "1", "0", "0", "SSCC (Serial Shipping Container Code)", "SSCC", "18n" },
				{ "1", "2", "1", "1", "Global Trade Item Number", "GTIN", "14n" },
				{ "02 + 37", "3", "(2)(37)", "(02)(37)", "GTIN + Count of trade items contained in a logistic unit", "CONTENT + COUNT",
						"(14n)(1*8n)" },
				{ "10", "4", "10", "10", "Batch or lot number", "BATCH/LOT", "1*20an" },
				{ "11", "5", "11", "11", "Production date (YYMMDD)", "PROD DATE", "6n" },
				{ "12", "6", "12", "12", "Due date (YYMMDD)", "DUE DATE", "6n" },
				{ "13", "7", "13", "13", "Packaging date (YYMMDD)", "PACK DATE", "6n" },
				{ "15", "8", "15", "15", "Best before date (YYMMDD)", "BEST BEFORE OR SELL BY", "6n" },
				{ "17", "9", "17", "17", "Expiration date (YYMMDD)", "USE BY OR EXPIRY", "6n" },
				{ "20", "10", "20", "20", "Product variant", "VARIANT", "2n" },
				{ "21", "11", "21", "21", "Serial number", "SERIAL", "1*20an" },
				{ "22", "12", "22", "22", "Secondary data for specific health industry products ** DEPRICATED as of GS1GS13.0 **",
						"QTY/DATE/BATCH", "1*29an" },
				{ "240", "13", "240", "240", "Additional product identification assigned by the manufacturer", "ADDITIONAL ID", "1*30an" },
				{ "241", "14", "241", "241", "Customer part number", "CUST. PART NO.", "1*30an" },
				{ "242", "15", "242", "242", "Made-to-Order Variation Number", "VARIATION NUMBER", "1*6n" },
				{ "250", "16", "250", "250", "Secondary serial number", "SECONDARY SERIAL", "1*30an" },
				{ "251", "17", "251", "251", "Reference to source entity", "REF. TO SOURCE", "1*30an" },
				{ "253", "18", "253", "253", "Global Document Type Identifier", "DOC. ID", "13n 0*17an" },
				{ "30", "19", "30", "30", "Variable count", "VAR. COUNT", "1*8n" },
				{ "310n 320n etc", "20", "K-Secondary = S00", "",
						"Net weight, kilograms or pounds or troy oz (Variable Measure Trade Item)", "", "" },
				{ "311n 321n etc", "21", "K-Secondary = S01", "", "Length of first dimension (Variable Measure Trade Item)", "", "" },
				{ "312n 324n etc", "22", "K-Secondary = S02", "", "Width, diameter, or second dimension (Variable Measure Trade Item)", "",
						"" },
				{ "313n 327n etc", "23", "K-Secondary = S03", "",
						"Depth, thickness, height, or third dimension (Variable Measure Trade Item)", "", "" },
				{ "314n 350n etc", "24", "K-Secondary = S04", "", "Area (Variable Measure Trade Item)", "", "" },
				{ "315n 316n etc", "25", "K-Secondary = S05", "", "Net volume (Variable Measure Trade Item)", "", "" },
				{ "330n or 340n", "26", "330%x30-36 / 340%x30-36", "330%x30-36 / 340%x30-36", "Logistic weight, kilograms or pounds",
						"GROSS WEIGHT (kg) or (lb)", "6n / 6n" },
				{ "331n, 341n, etc", "27", "K-Secondary = S09", "", "Length or first dimension", "", "" },
				{ "332n, 344n, etc", "28", "K-Secondary = S10", "", "Width, diameter, or second dimension", "", "" },
				{ "333n, 347n, etc", "29", "K-Secondary = S11", "", "Depth, thickness, height, or third dimension", "", "" },
				{ "334n 353n etc", "30", "K-Secondary = S07", "", "Logistic Area", "", "" },
				{ "335n 336n etc", "31", "K-Secondary = S06", "335%x30-36", "Logistic volume", "", "" },
				{ "337(***)", "32", "337%x30-36", "337%x30-36", "Kilograms per square metre", "KG PER m²", "6n" },
				{ "390n or 391n", "33", "390%x30-39 / 391%x30-39", "390%x30-39 / 391%x30-39",
						"Amount payable - single monetary area or with ISO currency code", "AMOUNT", "1*15n / 4*18n" },
				{ "392n or 393n", "34", "392%x30-39 / 393%x30-39", "392%x30-39 / 393%x30-39",
						"Amount payable for Variable Measure Trade Item - single monetary unit or ISO cc", "PRICE", "1*15n / 4*18n" },
				{ "400", "35", "400", "400", "Customer's purchase order number", "ORDER NUMBER", "1*30an" },
				{ "401", "36", "401", "401", "Global Identification Number for Consignment", "GINC", "1*30an" },
				{ "402", "37", "402", "402", "Global Shipment Identification Number", "GSIN", "17n" },
				{ "403", "38", "403", "403", "Routing code", "ROUTE", "1*30an" },
				{ "410", "39", "410", "410", "Ship to - deliver to Global Location Number", "SHIP TO LOC", "13n" },
				{ "411", "40", "411", "411", "Bill to - invoice to Global Location Number", "BILL TO", "13n" },
				{ "412", "41", "412", "412", "Purchased from Global Location Number", "PURCHASE FROM", "13n" },
				{ "413", "42", "413", "413", "Ship for - deliver for - forward to Global Location Number", "SHIP FOR LOC", "13n" },
				{ "414 and 254", "43", "(414) [254]", "(414) [254]", "Identification of a physical location GLN, and optional Extension",
						"LOC No + GLN EXTENSION", "(13n) [1*20an]" },
				{ "415 and 8020", "44", "(415) (8020)", "(415) (8020)",
						"Global Location Number of the Invoicing Party and Payment Slip Reference Number", "PAY + REF No",
						"(13n) (1*25an)" },
				{ "420 or 421", "45", "(420/421)", "(420/421)", "Ship to - deliver to postal code", "SHIP TO POST", "(1*20an / 3n 1*9an)" },
				{ "422", "46", "422", "422", "Country of origin of a trade item", "ORIGIN", "3n" },
				{ "423", "47", "423", "423", "Country of initial processing", "COUNTRY - INITIAL PROCESS.", "3*15n" },
				{ "424", "48", "424", "424", "Country of processing", "COUNTRY - PROCESS.", "3n" },
				{ "425", "49", "425", "425", "Country of disassembly", "COUNTRY - DISASSEMBLY", "3n" },
				{ "426", "50", "426", "426", "Country covering full process chain", "COUNTRY - FULL PROCESS", "3n" },
				{ "7001", "51", "7001", "7001", "NATO stock number", "NSN", "13n" },
				{ "7002", "52", "7002", "7002", "UN/ECE meat carcasses and cuts classification", "MEAT CUT", "1*30an" },
				{ "7003", "53", "7003", "7003", "Expiration Date and Time", "EXPIRY DATE/TIME", "10n" },
				{ "7004", "54", "7004", "7004", "Active Potency", "ACTIVE POTENCY", "1*4n" },
				{ "703s", "55", "7030", "7030", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "703s", "56", "7031", "7031", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "703s", "57", "7032", "7032", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "703s", "58", "7033", "7033", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "703s", "59", "7034", "7034", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "703s", "60", "7035", "7035", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "703s", "61", "7036", "7036", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "703s", "62", "7037", "7037", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "703s", "63", "7038", "7038", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "703s", "64", "7039", "7039", "Approval number of processor with ISO country code", "PROCESSOR # s", "3n 1*27an" },
				{ "8001", "65", "8001", "8001", "Roll products - width, length, core diameter, direction, and splices", "DIMENSIONS",
						"14n" },
				{ "8002", "66", "8002", "8002", "Electronic serial identifier for cellular mobile telephones", "CMT No", "1*20an" },
				{ "8003", "67", "8003", "8003", "Global Returnable Asset Identifier", "GRAI", "14n 0*16an" },
				{ "8004", "68", "8004", "8004", "Global Individual Asset Identifier", "GIAI", "1*30an" },
				{ "8005", "69", "8005", "8005", "Price per unit of measure", "PRICE PER UNIT", "6n" },
				{ "8006", "70", "8006", "8006", "Identification of the component of a trade item", "GCTIN", "18n" },
				{ "8007", "71", "8007", "8007", "International Bank Account Number", "IBAN", "1*30an" },
				{ "8008", "72", "8008", "8008", "Date and time of production", "PROD TIME", "8*12n" },
				{ "8018", "73", "8018", "8018", "Global Service Relation Number - Recipient", "GSRN - RECIPIENT", "18n" },
				{ "8100 8101 etc", "74", "K-Secondary = S08", "", "Coupon Codes", "", "" },
				{ "90", "75", "90", "90", "Information mutually agreed between trading partners (including FACT DIs)", "INTERNAL",
						"1*30an" },
				{ "91", "76", "91", "91", "Company internal information", "INTERNAL", "1*30an" },
				{ "92", "77", "92", "92", "Company internal information", "INTERNAL", "1*30an" },
				{ "93", "78", "93", "93", "Company internal information", "INTERNAL", "1*30an" },
				{ "94", "79", "94", "94", "Company internal information", "INTERNAL", "1*30an" },
				{ "95", "80", "95", "95", "Company internal information", "INTERNAL", "1*30an" },
				{ "96", "81", "96", "96", "Company internal information", "INTERNAL", "1*30an" },
				{ "97", "82", "97", "97", "Company internal information", "INTERNAL", "1*30an" },
				{ "98", "83", "98", "98", "Company internal information", "INTERNAL", "1*30an" },
				{ "99", "84", "99", "99", "Company internal information", "INTERNAL", "1*30an" },
				{ "nnn", "85", "K-Secondary = S12", "", "Additional AIs", "", "" } };

		testTableContent(tableID, vals);
	}

	public void testF9S00() throws Exception {
		// Date start = new Date();
		String tableID = "F9S00";

		String[][] vals = {
				{ "310(***)", "0", "310%x30-36", "310%x30-36", "Net weight, kilograms (Variable Measure Trade Item)", "NET WEIGHT (kg)",
						"6n" },
				{ "320(***)", "1", "320%x30-36", "320%x30-36", "Net weight, pounds (Variable Measure Trade Item)", "NET WEIGHT (lb)",
						"6n" },
				{ "356(***)", "2", "356%x30-36", "356%x30-36", "Net weight, troy ounces (Variable Measure Trade Item)", "NET WEIGHT (t)",
						"6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S01() throws Exception {
		// Date start = new Date();
		String tableID = "F9S01";

		String[][] vals = {
				{ "311(***)", "0", "311%x30-36", "311%x30-36", "Length of first dimension, metres (Variable Measure Trade Item)",
						"LENGTH (m)", "6n" },
				{ "321(***)", "1", "321%x30-36", "321%x30-36", "Length or first dimension, inches (Variable Measure Trade Item)",
						"LENGTH (i)", "6n" },
				{ "322(***)", "2", "322%x30-36", "322%x30-36", "Length or first dimension, feet (Variable Measure Trade Item)",
						"LENGTH (f)", "6n" },
				{ "323(***)", "3", "323%x30-36", "323%x30-36", "Length or first dimension, yards (Variable Measure Trade Item)",
						"LENGTH (y)", "6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S02() throws Exception {
		// Date start = new Date();
		String tableID = "F9S02";

		String[][] vals = {
				{ "312(***)", "0", "312%x30-36", "312%x30-36", "Width, diameter, or second dimension, metres (Variable Measure Trade Item)",
						"WIDTH (m)", "6n" },
				{ "324(***)", "1", "324%x30-36", "324%x30-36", "Width, diameter, or second dimension, inches (Variable Measure Trade Item)",
						"WIDTH (i)", "6n" },
				{ "325(***)", "2", "325%x30-36", "325%x30-36", "Width, diameter, or second dimension, (Variable Measure Trade Item)",
						"WIDTH (f)", "6n" },
				{ "326(***)", "3", "326%x30-36", "326%x30-36", "Width, diameter, or second dimension, yards (Variable Measure Trade Item)",
						"WIDTH (y)", "6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S03() throws Exception {
		// Date start = new Date();
		String tableID = "F9S03";

		String[][] vals = {
				{ "313(***)", "0", "313%x30-36", "313%x30-36",
						"Depth, thickness, height, or third dimension, metres (Variable Measure Trade Item)", "HEIGHT (m)", "6n" },
				{ "327(***)", "1", "327%x30-36", "327%x30-36",
						"Depth, thickness, height, or third dimension, inches (Variable Measure Trade Item)", "HEIGHT (i)", "6n" },
				{ "328(***)", "2", "328%x30-36", "328%x30-36",
						"Depth, thickness, height, or third dimension, feet (Variable Measure Trade Item)", "HEIGHT (f)", "6n" },
				{ "329(***)", "3", "329%x30-36", "329%x30-36",
						"Depth, thickness, height, or third dimension, yards (Variable Measure Trade Item)", "HEIGHT (y)", "6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S04() throws Exception {
		// Date start = new Date();
		String tableID = "F9S04";

		String[][] vals = {
				{ "314(***)", "0", "314%x30-36", "314%x30-36", "Area, square metres (Variable Measure Trade Item)", "AREA (m2)", "6n" },
				{ "350(***)", "1", "350%x30-36", "350%x30-36", "Area, square inches (Variable Measure Trade Item)", "AREA (i2)", "6n" },
				{ "351(***)", "2", "351%x30-36", "351%x30-36", "Area, square feet (Variable Measure Trade Item)", "AREA (f2)", "6n" },
				{ "352(***)", "3", "352%x30-36", "352%x30-36", "Area, square yards (Variable Measure Trade Item)", "AREA (y2)", "6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S05() throws Exception {
		// Date start = new Date();
		String tableID = "F9S05";

		String[][] vals = {
				{ "315(***)", "0", "315%x30-36", "315%x30-36", "Net volume, litres (Variable Measure Trade Item)", "NET VOLUME (l)", "6n" },
				{ "316(***)", "1", "316%x30-36", "316%x30-36", "Net volume, cubic metres (Variable Measure Trade Item)", "NET VOLUME (m3)",
						"6n" },
				{ "357(***)", "2", "357%x30-36", "357%x30-36", "Net weight (or volume), ounces (Variable Measure Trade Item)",
						"NET VOLUME (oz)", "6n" },
				{ "360(***)", "3", "360%x30-36", "360%x30-36", "Net volume, quarts (Variable Measure Trade Item)", "NET VOLUME (q)", "6n" },
				{ "361(***)", "4", "361%x30-36", "361%x30-36", "Net volume, gallons U.S. (Variable Measure Trade Item)", "NET VOLUME (g)",
						"6n" },
				{ "364(***)", "5", "364%x30-36", "364%x30-36", "Net volume, cubic inches", "VOLUME (i3), log", "6n" },
				{ "365(***)", "6", "365%x30-36", "365%x30-36", "Net volume, cubic feet (Variable Measure Trade Item)", "VOLUME (f3), log",
						"6n" },
				{ "366(***)", "7", "366%x30-36", "366%x30-36", "Net volume, cubic yards (Variable Measure Trade Item)", "VOLUME (y3), log",
						"6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S06() throws Exception {
		// Date start = new Date();
		String tableID = "F9S06";

		String[][] vals = { { "335(***)", "0", "335%x30-36", "335%x30-36", "Logistic volume, litres", "VOLUME (l), log", "6n" },
				{ "336(***)", "1", "336%x30-36", "336%x30-36", "Logistic volume, cubic meters", "VOLUME (m3), log", "6n" },
				{ "362(***)", "2", "362%x30-36", "362%x30-36", "Logistic volume, quarts", "VOLUME (q), log", "6n" },
				{ "363(***)", "3", "363%x30-36", "363%x30-36", "Logistic volume, gallons", "VOLUME (g), log", "6n" },
				{ "367(***)", "4", "367%x30-36", "367%x30-36", "Logistic volume, cubic inches", "VOLUME (q), log", "6n" },
				{ "368(***)", "5", "368%x30-36", "368%x30-36", "Logistic volume, cubic feet", "VOLUME (g), log", "6n" },
				{ "369(***)", "6", "369%x30-36", "369%x30-36", "Logistic volume, cubic yards", "VOLUME (i3), log", "6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S07() throws Exception {
		// Date start = new Date();
		String tableID = "F9S07";

		String[][] vals = { { "334(***)", "0", "334%x30-36", "334%x30-36", "Area, square metres", "AREA (m2), log", "6n" },
				{ "353(***)", "1", "353%x30-36", "353%x30-36", "Area, square inches", "AREA (i2), log", "6n" },
				{ "354(***)", "2", "354%x30-36", "354%x30-36", "Area, square feet", "AREA (f2), log", "6n" },
				{ "355(***)", "3", "355%x30-36", "355%x30-36", "Area, square yards", "AREA (y2), log", "6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S08() throws Exception {
		// Date start = new Date();
		String tableID = "F9S08";

		String[][] vals = { { "8100", "0", "8100", "8100", "GS1-128 Coupon Extended Code - NSC + Offer Code", "-", "6n" },
				{ "8101", "1", "8101", "8101", "GS1-128 Coupon Extended Code - NSC + Offer Code + end of offer code", "-", "10n" },
				{ "8102", "2", "8102", "8102", "GS1-128 Coupon Extended Code - NSC", "-", "2n" },
				{ "8110", "3", "8110", "8110", "Coupon Code Identification for Use in North America", "", "1*70an" } };

		testTableContent(tableID, vals);
	}

	public void testF9S09() throws Exception {
		// Date start = new Date();
		String tableID = "F9S09";

		String[][] vals = { { "331(***)", "0", "331%x30-36", "331%x30-36", "Length or first dimension, metres", "LENGTH (m), log", "6n" },
				{ "341(***)", "1", "341%x30-36", "341%x30-36", "Length or first dimension, inches", "LENGTH (i), log", "6n" },
				{ "342(***)", "2", "342%x30-36", "342%x30-36", "Length or first dimension, feet", "LENGTH (f), log", "6n" },
				{ "343(***)", "3", "343%x30-36", "343%x30-36", "Length or first dimension, yards", "LENGTH (y), log", "6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S10() throws Exception {
		// Date start = new Date();
		String tableID = "F9S10";

		String[][] vals = {
				{ "332(***)", "0", "332%x30-36", "332%x30-36", "Width, diameter, or second dimension, metres", "WIDTH (m), log", "6n" },
				{ "344(***)", "1", "344%x30-36", "344%x30-36", "Width, diameter, or second dimension", "WIDTH (i), log", "6n" },
				{ "345(***)", "2", "345%x30-36", "345%x30-36", "Width, diameter, or second dimension", "WIDTH (f), log", "6n" },
				{ "346(***)", "3", "346%x30-36", "346%x30-36", "Width, diameter, or second dimension", "WIDTH (y), log", "6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S11() throws Exception {
		// Date start = new Date();
		String tableID = "F9S11";

		String[][] vals = {
				{ "333(***)", "0", "333%x30-36", "333%x30-36", "Depth, thickness, height, or third dimension, metres", "HEIGHT (m), log",
						"6n" },
				{ "347(***)", "1", "347%x30-36", "347%x30-36", "Depth, thickness, height, or third dimension", "HEIGHT (i), log", "6n" },
				{ "348(***)", "2", "348%x30-36", "348%x30-36", "Depth, thickness, height, or third dimension", "HEIGHT (f), log", "6n" },
				{ "349(***)", "3", "349%x30-36", "349%x30-36", "Depth, thickness, height, or third dimension", "HEIGHT (y), log", "6n" } };

		testTableContent(tableID, vals);
	}

	public void testF9S12() throws Exception {
		// Date start = new Date();
		String tableID = "F9S12";

		String[][] vals = { { "243", "0", "243", "243", "Packaging Component Number", "PCN", "1*20an" },
				{ "255", "1", "255", "255", "Global Coupon Number", "GCN", "13*25n" },
				{ "427", "2", "427", "427", "Country Subdivision of Origin Code for a Trade Item", "ORIGIN SUBDIVISION", "1*3an" },
				{ "710", "3", "710", "710", "National Healthcare Reimbursement Number - Germany (PZN)", "NHRN PZN", "3n 1*27an" },
				{ "711", "4", "711", "711", "National Healthcare Reimbursement Number - France (CIP)", "NHRN CIP", "3n 1*27an" },
				{ "712", "5", "712", "712", "National Healthcare Reimbursement Number - Spain (CN)", "NHRN CN", "3n 1*27an" },
				{ "713", "6", "713", "713", "National Healthcare Reimbursement Number - Brazil (DRN)", "NHRN DRN", "3n 1*27an" },
				{ "8010", "7", "8010", "8010", "Component / Part Identifier", "CPID", "1*30an" },
				{ "8011", "8", "8011", "8011", "Component / Part Identifier Serial Number", "CPID Serial", "1*12n" },
				{ "8017", "9", "8017", "8017", "Global Service Relation Number - Provider", "GSRN - PROVIDER", "18n" },
				{ "8019", "10", "8019", "8019", "Service Relation Instance Number", "SRIN", "1*10n" },
				{ "8200", "11", "8200", "8200", "Extended Packaging URL", "PRODUCT URL", "1*70an" } };

		testTableContent(tableID, vals);
	}

	public void testGetOids() {
		PackedObjectInvestigator investigator = new PackedObjectInvestigator(idTableData);
		Map<String, Map<ColumnName, String>> result = investigator.getOids("urn:oid:1.0.15961.9");
		TestCase.assertEquals(499, result.size());
		for (Map.Entry<String, Map<ColumnName, String>> entry : result.entrySet())
			TestCase.assertEquals(0, entry.getValue().size());

		for (ColumnName columnName : ColumnName.values()) {
			result = investigator.getOids("urn:oid:1.0.15961.9", columnName);
			TestCase.assertEquals(499, result.size());
			for (Map.Entry<String, Map<ColumnName, String>> entry : result.entrySet()) {
				TestCase.assertEquals(1, entry.getValue().size());
				TestCase.assertTrue(entry.getValue().get(columnName) != null);
				List<Map.Entry<String, Integer>> entries = investigator.getOidEntry(entry.getKey());
				TestCase.assertEquals(investigator.getData(entries.get(entries.size() - 1).getKey(), columnName,
						entries.get(entries.size() - 1).getValue()), entry.getValue().get(columnName));
			}
		}

		result = investigator.getOids("urn:oid:1.0.15961.9", ColumnName.FORMAT_9_DATA_TITLE, ColumnName.FORMAT_9_NAME);
		TestCase.assertEquals(499, result.size());
		for (Map.Entry<String, Map<ColumnName, String>> entry : result.entrySet()) {
			TestCase.assertEquals(2, entry.getValue().size());
			TestCase.assertTrue(entry.getValue().get(ColumnName.FORMAT_9_DATA_TITLE) != null);
			TestCase.assertTrue(entry.getValue().get(ColumnName.FORMAT_9_NAME) != null);

			List<Map.Entry<String, Integer>> entries = investigator.getOidEntry(entry.getKey());
			TestCase.assertEquals(investigator.getData(entries.get(entries.size() - 1).getKey(), ColumnName.FORMAT_9_DATA_TITLE,
					entries.get(entries.size() - 1).getValue()), entry.getValue().get(ColumnName.FORMAT_9_DATA_TITLE));
			TestCase.assertEquals(investigator.getData(entries.get(entries.size() - 1).getKey(), ColumnName.FORMAT_9_NAME,
					entries.get(entries.size() - 1).getValue()), entry.getValue().get(ColumnName.FORMAT_9_NAME));
		}
	}

	@SuppressWarnings("serial")
	public void testGetOidEntry() throws Exception {
		PackedObjectInvestigator investigator = new PackedObjectInvestigator(idTableData);

		try {
			investigator.getOidEntry("urn:oid:1.0.15961.10.0");
			TestCase.fail("Exception expected.");
		} catch (UnsupportedOperationException expected) {
			TestCase.assertEquals(expected.getMessage(), "Unsupported data format.");
		}
		try {
			investigator.getOidEntry("urn:oid:2.5.111.10.0");
			TestCase.fail("Exception expected.");
		} catch (UnsupportedOperationException expected) {
			TestCase.assertEquals(expected.getMessage(), "Unknown data format.");
		}

		try {
			investigator.getOidEntry("asdasd");
			TestCase.fail("Exception expected.");
		} catch (IllegalArgumentException expected) {
			TestCase.assertEquals(expected.getMessage(), "The oid is invalid. It does not conform the RFC 3061 specification.");
		}

		try {
			investigator.getOidEntry(null);
			TestCase.fail("Exception expected.");
		} catch (IllegalArgumentException expected) {
			TestCase.assertEquals(expected.getMessage(), "The oid must not be null.");
		}

		try {
			investigator.getOidEntry("urn:oid:1.0.15961.9.5000");
			TestCase.fail("Exception expected.");
		} catch (IllegalArgumentException expected) {
			TestCase.assertEquals(expected.getMessage(), "The OID 'urn:oid:1.0.15961.9.5000' does not exist.");
		}

		List<Map.Entry<String, Integer>> expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 1));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.0"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 2));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.1"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 3));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.2"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 3));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.37"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 4));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.10"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 5));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.11"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 6));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.12"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 7));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.13"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 8));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.15"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 9));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.17"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 20));
				add(new SimpleEntry<String, Integer>("F9S00", 1));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.3103"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 74));
				add(new SimpleEntry<String, Integer>("F9S08", 3));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.8102"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 33));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.3918"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 43));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.254"));

		expected = new ArrayList<Map.Entry<String, Integer>>() {
			{
				add(new SimpleEntry<String, Integer>(Constants.getBaseTableID(9), 45));
			}
		};
		assertEquals(expected, investigator.getOidEntry("urn:oid:1.0.15961.9.421"));
	}

	public void testGetOidEntryPerformance() throws Exception {
		PackedObjectInvestigator investigator = new PackedObjectInvestigator(idTableData);
		int runs = 200;
		long millis = System.currentTimeMillis();
		for (int i = 0; i < runs; i++) {
			investigator.getOids("urn:oid:1.0.15961.9.421");
		}
		System.out.println("testGetOidEntryPerformance:" + (System.currentTimeMillis() - millis) / runs);
	}
	
	public void testDecodePackedObjects() {		
		PackedObjectInvestigator investigator = new PackedObjectInvestigator(idTableData);
		investigator.decodePackedObjects("8985CD4B6B7D284ED02C398A07890047DC7EC132C47DC7EC132C7B1674E79C5FE423C4800000000000002C49B7293D5E172A832F9E8CF66B222011265C00");
	}

	public void testEncodePackedObjects() {
		PackedObjectInvestigator investigator = new PackedObjectInvestigator(idTableData);
		PackedObjects packedObjects = new PackedObjects(investigator);
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
		investigator.encodePackedObjects(packedObjects);
	}

	private void testTableContent(String tableID, String[][] expected) throws Exception {
		System.out.println("start 'tableID'");
		PackedObjectInvestigator objectsTest = new PackedObjectInvestigator(idTableData);

		for (int column = 0; column < expected.length; column++) {
			int tabCol = column + 1;

			for (int row = 0; row < expected[column].length; row++) {
				ColumnName columnName = getColumnName(row);

				String value = objectsTest.getData(tableID, columnName, tabCol);

				TestCase.assertEquals("Not equal at: [" + tabCol + "," + columnName.toString() + "]", expected[column][row],
						new String(value.getBytes(), "UTF-8"));
			}
		}

		System.out.println("Content count: " + expected.length);
		System.out.println("complete '" + tableID + "'.");
	}

	private void testHeader(String tableID, String expected[], TableHeader[] tabHeaders) throws Exception {
		PackedObjectInvestigator objectsTest = new PackedObjectInvestigator(idTableData);

		for (int i = 0; i < tabHeaders.length; i++) {
			TableHeader header = tabHeaders[i];
			String value = objectsTest.getHeader(tableID, header);

			TestCase.assertEquals("Not equal at: [" + i + ", " + header.toString() + "]", expected[i],
					new String(value.getBytes(), "UTF-8"));
		}
	}

	private ColumnName getColumnName(int num) {
		for (ColumnName columnName : ColumnName.values()) {
			if (columnName.getNum() == num) {
				return columnName;
			}
		}

		return null;
	}

	/**
	 * Asserts that two Sets are equal. If they are not, an
	 * {@link AssertionError} is thrown.
	 * 
	 * @param expected
	 * @param actual
	 */
	void assertEquals(List<Map.Entry<String, Integer>> expected, List<Map.Entry<String, Integer>> actual) {
		if (expected == null && actual != null)
			throw new AssertionError("Expected should be null but actual is not null");
		if (expected != null && actual == null)
			throw new AssertionError("Expected should be not null but actual is null");
		if (expected != null && actual != null) {
			if (expected.size() != actual.size())
				throw new AssertionError("Expected size is " + expected.size() + " but actual size is " + actual.size() + ".");
			Iterator<Map.Entry<String, Integer>> expectedI = expected.iterator();
			Iterator<Map.Entry<String, Integer>> actualI = actual.iterator();
			while (expectedI.hasNext()) {
				Map.Entry<String, Integer> expectedE = expectedI.next();
				Map.Entry<String, Integer> actualE = actualI.next();
				TestCase.assertEquals(expectedE.getKey(), actualE.getKey());
				TestCase.assertEquals(expectedE.getValue(), actualE.getValue());
			}
		}
	}
}
