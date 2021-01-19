package havis.middleware.tdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class String6bitEncoderDecoder {

	private final static Logger log = Logger.getLogger(String6bitEncoderDecoder.class.getName());

	public static final String6bitEncoderDecoder INSTANCE = new String6bitEncoderDecoder();

	private static final byte EOT = (byte) 0b10000100;
	private static final byte GS = (byte) 0b01111000;

	private static final Map<Character, Byte> ENCODINGS = new HashMap<>();
	private static final Map<Byte, Character> DECODINGS = new HashMap<>();
	static {
		add(' ', 0b10000000);
		add('"', 0b10001000);
		add('%', 0b10010100);
		add('&', 0b10011000);
		add('\'', 0b10011100);
		add('(', 0b10100000);
		add(')', 0b10100100);
		add('*', 0b10101000);
		add('+', 0b10101100);
		add(',', 0b10110000);
		add('-', 0b10110100);
		add('.', 0b10111000);
		add('/', 0b10111100);
		add('0', 0b11000000);
		add('1', 0b11000100);
		add('2', 0b11001000);
		add('3', 0b11001100);
		add('4', 0b11010000);
		add('5', 0b11010100);
		add('6', 0b11011000);
		add('7', 0b11011100);
		add('8', 0b11100000);
		add('9', 0b11100100);
		add(':', 0b11101000);
		add(';', 0b11101100);
		add('<', 0b11110000);
		add('=', 0b11110100);
		add('>', 0b11111000);
		add('?', 0b11111100);
		add('@', 0b00000000);
		add('A', 0b00000100);
		add('B', 0b00001000);
		add('C', 0b00001100);
		add('D', 0b00010000);
		add('E', 0b00010100);
		add('F', 0b00011000);
		add('G', 0b00011100);
		add('H', 0b00100000);
		add('I', 0b00100100);
		add('J', 0b00101000);
		add('K', 0b00101100);
		add('L', 0b00110000);
		add('M', 0b00110100);
		add('N', 0b00111000);
		add('O', 0b00111100);
		add('P', 0b01000000);
		add('Q', 0b01000100);
		add('R', 0b01001000);
		add('S', 0b01001100);
		add('T', 0b01010000);
		add('U', 0b01010100);
		add('V', 0b01011000);
		add('W', 0b01011100);
		add('X', 0b01100000);
		add('Y', 0b01100100);
		add('Z', 0b01101000);
		add('[', 0b01101100);
		add('\\', 0b01110000);
		add(']', 0b01110100);
	}

	private static void add(char character, int code) {
		Character c = Character.valueOf(character);
		Byte b = Byte.valueOf((byte) code);
		ENCODINGS.put(c, b);
		DECODINGS.put(b, c);
	}

	private String6bitEncoderDecoder() {
	}

	private static boolean isSet(byte b, int index) {
		return (b & (1 << (7 - index))) != 0;
	}

	private static byte set(byte b, int index, boolean state) {
		if (state)
			return b |= 1 << (7 - index);
		else
			return b &= ~(1 << (7 - index));
	}

	public int calculateMinBitLength(List<String> strings) {
		return calculateLength(strings, false);
	}

	public byte[] encode(List<String> strings) throws EncoderException {
		return encode(strings, -1);
	}

	public byte[] encode(List<String> strings, int expectedBitLength) throws EncoderException {
		if (expectedBitLength > 0 && (expectedBitLength % 8 != 0 || expectedBitLength < calculateLength(strings, false)))
			throw new EncoderException("Failed to encode: Illegal expected bit length " + expectedBitLength);

		int length = expectedBitLength > 0 ? expectedBitLength : calculateLength(strings, true);
		byte[] data = new byte[length / 8];
		int position = 0;
		for (String string : strings) {
			if (position > 0) {
				setBits(data, position, GS);
				position += 6;
			}
			for (char c : string.toCharArray()) {
				encodeChar(c, data, position);
				position += 6;
			}
		}

		// add EOT
		setBits(data, position, EOT);
		position += 6;

		// fill remaining 2, 4, 6, 8, 10, or 14 bits with EOT
		while (position < length) {
			setBits(data, position, EOT, Math.min(6, length - position));
			position += 6;
		}

		return data;
	}

	public List<String> decode(byte[] data) {
		return decode(data, -1, true);
	}

	public List<String> decode(byte[] data, int expectedBitLength, boolean expectEOT) {
		List<String> result = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		int length = data.length * 8;
		if (expectedBitLength > 0 && length < expectedBitLength) {
			log.fine("Failed to decode: Insufficient data was read for decoding, expected " + expectedBitLength + " bits, but received only " + length
					+ " bits.");
			return null;
		}
		boolean wasTerminated = false;
		boolean error = false;
		int readBits = 0;
		for (int position = 0; position < length; position += 6) {
			int bitLength = Math.min(6, length - position);
			byte b = extractBits(data, position, bitLength);
			readBits = position + bitLength;

			if (b == EOT) {
				if (current.length() == 0) {
					error = true;
					log.fine("Failed to decode: EOT without actual data");
				}
				wasTerminated = true;
				break;
			} else if (b == GS) {
				if (current.length() == 0) {
					error = true;
					log.fine("Failed to decode: GS without actual data");
					break;
				}
				result.add(current.toString());
				current.setLength(0);
				continue;
			}

			// only decode 6 bits
			if (bitLength == 6) {
				Character c = decodeChar(b);
				if (c == null) {
					error = true;
					break;
				}
				current.append(c.charValue());
			}
		}

		// round up to next full bytes
		readBits = roundUp(readBits, false);

		if (!error && current.length() > 0)
			result.add(current.toString());

		if (!error && expectedBitLength > 0) {
			if (readBits < expectedBitLength) {
				error = true;
				log.fine("Failed to decode: Insufficient data was parsed while decoding, expected " + expectedBitLength + " bits, but parsed only " + readBits
						+ " bits.");
			} else if (readBits > expectedBitLength) {
				error = true;
				log.fine("Failed to decode: More data was parsed while decoding, expected " + expectedBitLength + " bits, but parsed " + readBits + " bits.");
			}
		}

		if (!error && expectEOT && !wasTerminated) {
			error = true;
			log.fine("Failed to decode: Missing expected EOT");
		}

		if (error)
			return null;

		return result;
	}

	private byte extractBits(byte[] data, int position, int length) {
		byte b = 0;
		for (int i = 0; i < length; i++) {
			int absoluteBitIndex = position + i;
			int byteIndex = absoluteBitIndex / 8;
			int bitIndex = absoluteBitIndex - (byteIndex * 8);
			b = set(b, i, isSet(data[byteIndex], bitIndex));
		}
		return b;
	}

	private Character decodeChar(byte b) {
		Character decoded = DECODINGS.get(Byte.valueOf(b));
		if (decoded == null)
			log.fine("Failed to decode: Illegal bit sequence '" + toBinary(b) + "'");
		return decoded;
	}

	private static String toBinary(byte b, int length) {
		return fill(Integer.toBinaryString(b & 0xFF), length);
	}

	private static String toBinary(byte b) {
		return toBinary(b, 6);
	}

	private static String fill(String string, int length) {
		StringBuilder result = new StringBuilder(length);
		result.append(string);
		while (result.length() < length)
			result.insert(0, '0');
		return result.toString();
	}

	private int calculateLength(List<String> strings, boolean fullWords) {
		int dataLength = 0;
		for (String string : strings)
			dataLength += string.length();
		int length = (dataLength + (strings.size() - 1) /* GS */+ 1 /* EOT */) * 6;
		return roundUp(length, fullWords);
	}

	private int roundUp(int value, boolean fullWords) {
		int roundUpTo = fullWords ? 16 : 8;
		int remainder = value % roundUpTo;
		if (remainder > 0)
			remainder = roundUpTo - remainder;
		return value + remainder;
	}

	private void encodeChar(char c, byte[] data, int position) throws EncoderException {
		Byte encoded = ENCODINGS.get(Character.valueOf(c));
		if (encoded == null)
			throw new EncoderException("Failed to encode: Illegal character '" + c + "'");

		setBits(data, position, encoded.byteValue());
	}

	private void setBits(byte[] data, int position, byte dataToSet) {
		setBits(data, position, dataToSet, 6);
	}

	private void setBits(byte[] data, int position, byte dataToSet, int length) {
		for (int i = 0; i < length; i++) {
			int absoluteBitIndex = position + i;
			int byteIndex = absoluteBitIndex / 8;
			int bitIndex = absoluteBitIndex - (byteIndex * 8);
			data[byteIndex] = set(data[byteIndex], bitIndex, isSet(dataToSet, i));
		}
	}
}
