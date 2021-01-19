package havis.middleware.tdt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BarcodeTest.class, EBVTest.class, PackedObjectInvestigatorJDKTest.class, RuleXTest.class, TdtCompactionMethodsTest.class, TdtEpcTest.class, TdtPcTest.class, TdtTagInfoTest.class, TdtTidTest.class, TdtTranslateCodeTableTest.class, TdtTranslatorTest.class, UserMemoryDecodeTest.class, UserMemoryEncodeTest.class })
public class TestSuite {
}