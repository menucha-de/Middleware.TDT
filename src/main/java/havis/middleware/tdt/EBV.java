package havis.middleware.tdt;

import java.util.logging.Logger;

enum EBV {

	EBV3(2),
	EBV6(5);

	private final static Logger log = Logger.getLogger(EBV.class.getName());

	private int value;

	private EBV(int value) {
		this.value = value;
	}

	/**
	 * Determines the integer value.
	 * 
	 * @param buffer
	 * @return The integer value
	 * @throws Exception
	 */
	int valueOf(Buffer buffer) {
		int threshold = 8 - value;
		int mask = 0xff >> threshold;
		log.fine("M: " + mask + " T: " + threshold);
		int result = 0;
		int x;
		do {
			x = buffer.b >> (7 - buffer.pos) & 0x1;
			log.fine("X: " + x);
			buffer.pos++;
			log.fine("R+P: " + result + " P: " + buffer.pos);
			if (buffer.pos > threshold) {
				result <<= 8 - buffer.pos;
				result += buffer.b & mask >> (buffer.pos - threshold);
				log.fine("R11: " + result + "P: " + buffer.pos);
				buffer.pos -= threshold;
				result <<= buffer.pos;
				buffer.next();
				result += buffer.b >> (8 - buffer.pos) & mask >> (value - buffer.pos);
				log.fine("R12: " + result);
			} else {
				result <<= value;
				log.fine("R21: " + result + " P: " + buffer.pos + " M: " + mask);
				buffer.pos = threshold - buffer.pos;
				result += ((buffer.b >> buffer.pos) & mask);
				log.fine("R22: " + result + " P: " + buffer.pos);
				if (buffer.pos == 0)
					buffer.next();
				else
					buffer.pos = 8 - buffer.pos;
			}
		} while (x == 1);
		return result;
	}

	/**
	 * Converts the integer value to an EBV string.
	 * 
	 * @param ebv
	 *            The type of EBV
	 * @param value
	 *            The integer value
	 * @return The EBV value
	 * @see EPC Tag Data Standard v1.9 - I.5.2 Length Information
	 */
	public static String getEbvValue(EBV ebv, int value) {
		String v = DataTypeConverter.convert(value, 10, 2, 0);

		while (v.length() % ebv.value > 0) {
			v = "0" + v;
		}
		String result = "";
		for (int i = v.length(); i > ebv.value; i -= ebv.value) {
			result += "1" + v.substring(0, ebv.value);
			v = v.substring(ebv.value);
		}
		result += "0" + v;
		return result;
	}
}