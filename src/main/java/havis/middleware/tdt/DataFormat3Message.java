package havis.middleware.tdt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DataFormat3Message extends ItemData {

	public static final byte DSFID = (byte) 0x03;

	public static final byte PRECURSOR = (byte) 0x46;

	public static final String URN_PREFIX = "urn:oid:1.0.15434.3.";

	public enum Bank {
		EPC_BANK(1), USER_BANK(3);

		private int id;

		private Bank(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static Bank getBank(int id) {
			for (Bank b : Bank.values()) {
				if (id == b.getId()) {
					return b;
				}
			}
			return null;
		}
	}
	
	private static class Ref {
		public int value;
	}

	private Bank bank;

	public DataFormat3Message() {
		super();
	}

	public DataFormat3Message(Bank bank) {
		super(new ArrayList<Map.Entry<String, String>>());
		this.bank = bank;
	}

	public DataFormat3Message(Bank bank, List<Map.Entry<String, String>> dataElements) {
		super(dataElements);
		this.bank = bank;
	}

	public Bank getBank() {
		return this.bank;
	}

	@Override
	public byte[] encode() throws EncoderException {
		List<String> strings = toStringList(getDataElements());
		switch (this.bank) {
		case EPC_BANK:
			if (strings.size() == 0)
				return new byte[0];
			else if (strings.size() == 1)
				return String6bitEncoderDecoder.INSTANCE.encode(strings);
			else
				throw new EncoderException("Failed to encode: EPC bank only allows a single UII");
		case USER_BANK:
			Ref expectedBitLength = new Ref();
			byte[] byteCount = calculateByteCount(strings, expectedBitLength);
			byte[] encodedData;
			if (strings.size() == 0)
				encodedData = new byte[0];
			else
				encodedData = String6bitEncoderDecoder.INSTANCE.encode(strings, expectedBitLength.value);

			int headerLength = 2 + byteCount.length;
			byte[] data = new byte[headerLength + encodedData.length];
			data[0] = DSFID;
			data[1] = PRECURSOR;
			System.arraycopy(byteCount, 0, data, 2, byteCount.length);
			System.arraycopy(encodedData, 0, data, headerLength, encodedData.length);
			return data;
		}
		return null;
	}

	private byte[] calculateByteCount(List<String> strings, Ref expectedBitLength) throws EncoderException {
		int byteCount = String6bitEncoderDecoder.INSTANCE.calculateMinBitLength(strings) / 8;
		int byteCountInFullWords = byteCount;
		byte[] result = null;
		if (byteCount <= 127) {
			if ((byteCount & 1) == 0) {
				// even amount of bytes, but 3 header bytes,
				// add additional padding at the end
				byteCountInFullWords += 1;
			}
			result = new byte[] { (byte) byteCount };
		} else if (byteCount <= 16383) {
			if ((byteCount & 1) != 0) {
				// odd amount of bytes, but 4 header bytes,
				// add additional padding at the end
				byteCountInFullWords += 1;
			}

			// move data after indicator bit
			int value = byteCount << 1;

			// convert to byte array
			result = new byte[2];
			result[0] = (byte) ((value >> 8) & 0xFF);
			result[1] = (byte) (value & 0xFF);

			// set indicator bit
			result[0] = (byte) ((result[0] & 0xFF) | 1 << 7);
			// move data after indicator bit
			result[1] = (byte) ((result[1] & 0xFF) >> 1);
		}
		if (result == null)
			throw new EncoderException("Failed to encode: Encoded data must not exceed 16383 bytes");

		expectedBitLength.value = byteCountInFullWords * 8;
		return result;
	}

	private List<String> toStringList(List<Entry<String, String>> dataElements) {
		List<String> result = new ArrayList<>();
		for (Entry<String, String> entry : dataElements) {
			if (entry.getKey() != null && entry.getKey().startsWith(URN_PREFIX) && entry.getValue() != null) {
				String encodingType = entry.getKey().substring(URN_PREFIX.length());
				String str = entry.getValue();
				if (!str.startsWith(encodingType)) {
					// the actual encoded string starts with the
					// encoding type (i.e. 37S)
					// prepend if not already prepended
					str = encodingType + str;
				}
				result.add(str);
			}
		}
		return result;
	}

	public static boolean isSupportedAfi(byte afi) {
		// currently we only support:
		// ANS MH10.8.2 Data Identifiers for unique identifier for items
		return afi == (byte) 0xA1 || afi == (byte) 0xA4;
	}

	public static boolean isSupportedDsfid(byte dsfid) {
		return dsfid == DSFID;
	}

	@Override
	public String toString() {
		return "{ \"bank\" : \"" + bank + "\", \"dataElements\" : " + dataElements.toString() + " }";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataFormat3Message other = (DataFormat3Message) obj;
		if (bank != other.bank)
			return false;
		return true;
	}

	@Override
	public DataFormat3Message clone() {
		DataFormat3Message clone = new DataFormat3Message();
		clone.bank = this.bank;
		clone.numberOfReadBytes = this.numberOfReadBytes;
		if (this.dataElements != null) {
			clone.dataElements = new ArrayList<>();
			for (Map.Entry<String, String> entry : this.dataElements) {
				clone.dataElements.add(new SimpleEntry<>(entry.getKey(), entry.getValue()));
			}
		}
		return clone;
	}
}