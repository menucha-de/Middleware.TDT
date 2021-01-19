package havis.middleware.tdt;

import java.math.BigInteger;

class Buffer {
	
	int pos, b, next;
	byte[] byteBuffer;

	Buffer(byte[] bytes, int pos) {
		byteBuffer = bytes;
		next = 0;
		next();
		this.pos = pos;
	}

	Buffer(byte[] bytes) {
		this(bytes, 0);
	}

	Buffer(int capacity) {
		byteBuffer = new byte[capacity];
		next = 0;
	}

	void next() {
		if (next < byteBuffer.length) {
			b = byteBuffer[next] & 0xff;
			next++;
		}
	}

	int getBit() {
		try {
			return b >> (7 - pos++) & 0x1;
		} finally {
			if (pos == 8) {
				next();
				pos = 0;
			}
		}
	}

	int getPos() {
		return (next - 1) * 8 + pos;
	}

	byte get(int index) {
		return byteBuffer[index];
	}

	BigInteger getBigInteger(int size) {
		String value = "";
		while (size > 0) {
			value += getBit();
			size--;
		}
		return new BigInteger(value, 2);
	}

	long getLong(int size) {
		long result = 0;
		while (size > 0) {
			if (size > 7) {
				result <<= 8 - pos;
				result += b & 0xff >> pos;
				size -= 8 - pos;
				pos = 0;
				if (size >= 0)
					next();
			} else {
				if (pos + size >= 8) {
					result += (b & 0xff >> pos);
					size -= 8 - pos;
					pos = 0;
					next();
				} else {
					result <<= size;
					if (pos > 0) {
						result += (b & 0xff >> pos) >> 8 - pos - size;
					} else {
						result += b >> 8 - size;
					}
					pos += size;
					size = 0;
				}
			}
		}
		return result;
	}

	int getInt(int size) {
		return (int) getLong(size);
	}
}