package havis.middleware.tdt;

/**
 * The PC (Protocol Control) Bits of an C1G2 Tag, For more Information see
 * EPCglobal EPC Tag Data Standard (TDS) Version 1.5, August 18, 2010, Figure 16
 * and Table 5.
 */
class TdtPc {

	/**
	 * Bytes 0x10 - 0x1F of a C1G2 Tag holds the PC-Field The meaning of the
	 * bits is described in the document EPCglobal EPC Tag Data Standard Version
	 * 1.5 chapter 15.1.1
	 */
	private byte[] pc;

	/**
	 * Constructor
	 */
	TdtPc(byte[] pc) {
		this.pc = pc;
	}

	/**
	 * Represents the number of 16-bit words comprising the PC field and the EPC
	 * field
	 */
	short getLength() {
		return ((pc != null) && (pc.length > 0)) ? ((short) ((pc[0] & 0xFF) >> 3))
				: (short) 0;
	}

	/**
	 * If false indicates the EPC bank contains an EPCglobal Application, if
	 * true indicates a non-EPCglobal application and the following bits contain
	 * an ISO Application Family Identifier (AFI) followed by an Unique Item
	 * Identifier (UII)
	 */
	boolean getToggleBit() {
		return (pc != null) && (pc.length > 0) && ((pc[0] & 0x1) == 0x1) ? true
				: false;
	}

	/**
	 * Indicates whether the user memory bank is present and contains data
	 */
	boolean getUserMemoryIndicator() {
		return (pc != null) && (pc.length > 0) && ((pc[0] & 0x4) == 0x4) ? true
				: false;
	}

	/**
	 * Indicates whether an XPC (extended protocol bits) is present
	 */
	boolean getXpcIndicator() {
		return (pc != null) && (pc.length > 0) && ((pc[0] & 0x2) == 0x2) ? true
				: false;
	}

	/**
	 * True indicates the tag is affixed to hazardous material, false provides
	 * no such indication
	 */
	boolean getHazardousMaterial() {
		return (getToggleBit() == false) && (pc != null) && (pc.length > 1)
				&& ((pc[1] & 0x1) == 0x1) ? true : false;
	}
}