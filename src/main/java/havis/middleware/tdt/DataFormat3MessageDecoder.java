package havis.middleware.tdt;

import havis.middleware.tdt.DataFormat3Message.Bank;

import java.util.List;

public class DataFormat3MessageDecoder {

	public static final DataFormat3MessageDecoder INSTANCE = new DataFormat3MessageDecoder();

	private DataFormat3MessageDecoder() {
	}

	public ItemData decode(int bank, byte[] data) {
		DataFormat3Message message = null;
		Bank b = DataFormat3Message.Bank.getBank(bank);
		if (b != null && data != null && data.length > 0) {
			switch (b) {
			case EPC_BANK:
				if (DataFormat3Message.isSupportedAfi(data[0])) {
					if (data.length > 1) {
						byte[] dataToDecode = new byte[data.length - 1];
						System.arraycopy(data, 1, dataToDecode, 0, dataToDecode.length);
						message = decode(b, dataToDecode, -1);
					}
				}
				break;
			default:
				if (data.length > 3 && data[0] == DataFormat3Message.DSFID && (data[1] == DataFormat3Message.PRECURSOR || data[1] == 0x00)) {
					int byteCount = -1;
					int byteCountLength = 0;
					if ((data[2] & (1 << 7)) != 0) {
						// indicator set, check next byte
						if ((data[3] & (1 << 7)) == 0) {
							// Looks like byte count is encoded in exactly two
							// bytes. If the indicator bit was set, another byte
							// would follow, which is currently not supported by
							// this implementation.
							byte[] byteCountData = new byte[2];
							System.arraycopy(data, 2, byteCountData, 0, byteCountData.length);
							// turn off indicator
							byteCountData[0] &= ~(1 << 7);
							// move data to first bit
							byteCountData[1] = (byte) ((byteCountData[1] & 0xFF) << 1);
							// to integer
							byteCount = byteCountData[1] & 0xFF;
							byteCount = (byteCount << 8) | (byteCountData[0] & 0xFF);
							// realign data
							byteCount = byteCount >> 1;
							byteCountLength = 2;
						}
					} else {
						// byte count is encoded in one byte
						byteCount = data[2] & 0xFF;
						byteCountLength = 1;
					}

					if (byteCount > -1) {
						int headerLength = 2 /* DSFID + PRECURSOR */+ byteCountLength;
						byte[] dataToDecode = new byte[data.length - headerLength];
						System.arraycopy(data, headerLength, dataToDecode, 0, dataToDecode.length);
						message = decode(b, dataToDecode, byteCount * 8);
					}
				}
				break;
			}
		}
		return message;
	}

	private DataFormat3Message decode(Bank bank, byte[] data, int expectedBitLength) {
		DataFormat3Message result = null;

		if (isEmptyDataOnly(data) && expectedBitLength <= 0) {
			// looks like an initialized bank
			result = new DataFormat3Message(bank);
		} else {
			List<String> decoded = String6bitEncoderDecoder.INSTANCE.decode(data, expectedBitLength, bank == Bank.USER_BANK);
			if (decoded != null) {
				result = new DataFormat3Message(bank);
				for (String string : decoded) {
					if (string.length() > 1) {
						StringBuilder di = new StringBuilder();
						StringBuilder element = new StringBuilder();
						boolean foundDi = false;
						for (char c : string.toCharArray()) {
							if (!foundDi)
								di.append(c);
							else
								element.append(c);

							if (!foundDi && c >= 'A' && c <= 'Z')
								foundDi = true;
						}

						if (di.length() <= 4 && element.length() > 0)
							result.getDataElements().add(new SimpleEntry<>(DataFormat3Message.URN_PREFIX + di.toString(), element.toString()));
					}
				}
			}
		}

		return result;
	}

	private boolean isEmptyDataOnly(byte[] data) {
		for (byte b : data)
			if (b != 0x00)
				return false;
		return true;
	}
}
