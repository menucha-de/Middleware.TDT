package havis.middleware.tdt;

/**
 * This class holds the information from the Tag Identification (TID) Memory
 * Bank (Bank 2) of an C1G2 RFID-Tag. For more Information see document
 * EPCglobal EPC Tag Data Standard Version 1.5, ratified August 18, 2010,
 * chapter 16
 */
class TdtTid {

	@SuppressWarnings("unused")
	private byte[] tidData;

	@SuppressWarnings("unused")
	private TdtPc tdtPc;

	/**
	 * The Mask Designer ID is the EPGglobal assigned unique ID of the mask
	 * designer (Tag manufacturer)
	 */
	private short maskDesignerId;

	/**
	 * Each mask designer assign a unique id to its tag types
	 */
	private short tagModelNumber;

	short getMaskDesignerId() {
		return maskDesignerId;
	}

	short getTagModelNumber() {
		return tagModelNumber;
	}

	/**
	 * Decode the TID Bits
	 */
	void decode(TdtPc tdtPc, byte[] tidData) throws TdtTranslationException {
		this.tdtPc = tdtPc;
		this.tidData = tidData;
		if (tdtPc == null) {
			throw new TdtTranslationException(
					"cannot decode TID, PC not initialized");
		}
		if (tdtPc.getToggleBit()) {
			throw new TdtTranslationException(
					"cannot decode TID, toggle bit in PC is set");
		}
		if (tidData == null || tidData.length < 4) {
			throw new TdtTranslationException("cannot decode TID, no TID-data");
		}
		if ((tidData[0] & 0xFF) != 0xE2) {
			throw new TdtTranslationException(
					"cannot decode TID, first byte of TID-data invalid");
		}
		// Byte 1 to Byte 3 contain a 12 bit Memory Designer Id (MDID) and
		// a 12 Bit Tag Model Number (TMN)
		maskDesignerId = (short) ((tidData[1] & 0xFF) << 4 | (tidData[2] & 0xFF) >> 4);
		tagModelNumber = (short) (((tidData[2] & 0xFF) & 0x0F) << 8 | (tidData[3] & 0xFF));
	}
}