package havis.middleware.tdt;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Assert;
import org.junit.Test;

public class TdtCompactionMethodsTest {

	@Test
	public void compactItem7Bit(@Mocked final FieldX field)
			throws TdtTranslationException {
		new NonStrictExpectations() {
			{
				field.getCompaction();
				result = CompactionMethodList._7BIT;
			}
		};
		String compacted = TdtCompactionMethods.compactItem("test", field);
		Assert.assertEquals("1110100110010111100111110100", compacted);
	}

	@Test
	public void compactItem7BitWithPadLeft(@Mocked final FieldX field)
			throws TdtTranslationException {
		new NonStrictExpectations() {
			{
				field.getCompaction();
				result = CompactionMethodList._7BIT;

				field.getBitPadDir();
				result = PadDirectionList.LEFT;

				field.getIntBitLength();
				result = Integer.valueOf(30);
			}
		};
		String compacted = TdtCompactionMethods.compactItem("test", field);
		Assert.assertEquals("001110100110010111100111110100", compacted);
	}

	@Test
	public void compactItem7BitWithPadRight(@Mocked final FieldX field)
			throws TdtTranslationException {
		new NonStrictExpectations() {
			{
				field.getCompaction();
				result = CompactionMethodList._7BIT;

				field.getBitPadDir();
				result = PadDirectionList.RIGHT;

				field.getIntBitLength();
				result = Integer.valueOf(30);
			}
		};
		String compacted = TdtCompactionMethods.compactItem("test", field);
		Assert.assertEquals("111010011001011110011111010000", compacted);
	}

	@Test
	public void compactItem8Bit(@Mocked final FieldX field)
			throws TdtTranslationException {
		new NonStrictExpectations() {
			{
				field.getCompaction();
				result = CompactionMethodList._8BIT;
			}
		};
		String compacted = TdtCompactionMethods.compactItem("test", field);
		Assert.assertEquals("01110100011001010111001101110100", compacted);
	}

	@Test
	public void decompactItem7Bit(@Mocked final FieldX field)
			throws TdtTranslationException {
		new NonStrictExpectations() {
			{
				field.getCompaction();
				result = CompactionMethodList._7BIT;

				field.getBitPosition();
				result = Integer.valueOf(0);

				field.getIntBitLength();
				result = Integer.valueOf(28);
			}
		};
		String decompacted = TdtCompactionMethods.decompactItem(
				"1110100110010111100111110100", field);
		Assert.assertEquals("test", decompacted);
	}

	@Test
	public void decompactItem8Bit(@Mocked final FieldX field)
			throws TdtTranslationException {
		new NonStrictExpectations() {
			{
				field.getCompaction();
				result = CompactionMethodList._8BIT;

				field.getBitPosition();
				result = Integer.valueOf(0);

				field.getIntBitLength();
				result = Integer.valueOf(32);
			}
		};
		String decompacted = TdtCompactionMethods.decompactItem(
				"01110100011001010111001101110100", field);
		Assert.assertEquals("test", decompacted);
	}
}
