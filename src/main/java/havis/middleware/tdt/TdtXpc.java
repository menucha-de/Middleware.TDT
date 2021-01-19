package havis.middleware.tdt;

/**
 * Access to the Extended Protocol Control Bits of an EPCglobal coded RFID Tag
 */
class TdtXpc {

	@SuppressWarnings("unused")
	private byte[] xpcData;

	@SuppressWarnings("unused")
	private TdtPc tdtPc;

	/**
	 * Decode the XPC field
	 */
	void decode(TdtPc tdtPc, byte[] xpcData) throws TdtTranslationException {
		this.tdtPc = tdtPc;
		this.xpcData = xpcData;
		if (tdtPc == null) {
			throw new TdtTranslationException("Cannot decode XPC : no PC Field");
		}

		if (xpcData == null) {
			throw new TdtTranslationException("Cannot decode XPC : no XPC-Data");
		}
	}
}