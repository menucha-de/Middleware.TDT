package havis.middleware.tdt;

class TdtUserMemory {

	@SuppressWarnings("unused")
	private TdtPc tdtPc;

	private byte[] userMemory;

	// DSFID : Data Storage Format Identifier (TDS 1.5 ch 17)
	private byte getDsfid() {
		return (userMemory != null && userMemory.length > 0) ? userMemory[0]
				: (byte) 0;
	}

	// The first two bits of the dsfid field are the access method
	private byte getAccessMethod() {
		return (byte) ((getDsfid() & 0xFF) >> 6);
	}

	// AccessMethod 2 is PackedObjects
	@SuppressWarnings("unused")
	private boolean isPackedObjects() {
		return (getDsfid() != 0) && (getAccessMethod() == 2) ? true : false;
	}

	// The last 5 bits of the dsfid field indicate the dataformat
	private byte getDataformat() {
		return (getDsfid() != 0) ? (byte) (getDsfid() & 0x1F) : (byte) 0;
	}

	// Check for GS1 data format 09
	@SuppressWarnings("unused")
	private boolean isGs1DataFormat09() {
		return getDataformat() == 9 ? true : false;
	}

	// public void DecodeUserMemory(TdtPc pc, byte[] userMemory)
	// {
	// _tdtPc = pc;
	// _userMemory = userMemory;
	// if (_tdtPc == null)
	// {
	// throw new
	// TdtTranslationException("Cannot decode user memory : PC-Field missing");
	// }
	// if (!_tdtPc.getUserMemoryIndicator())
	// {
	// throw new
	// TdtTranslationException("Cannot decode user memory : PC User Memory Indicator not set");
	// }
	//
	// if (IsGs1DataFormat09())
	// {
	// DecodeUserMemoryGs1DataFormat09();
	// }
	// else
	// {
	// DecodeUserMemoryISO();
	// }
	// }
	//
	//
	//
	// // Decoding the user memory as packed objects as
	// // described in TDS 1.5 and ISO 15961/15962
	// private void DecodeUserMemoryGs1DataFormat09()
	// {
	// // Start Position is first byte after the dsfid field
	// int bitPosition = 8;
	// BitArray bitsUserMemory = makeBitArrayFromByteArray(_userMemory);
	//
	// boolean terminationPattern = checkTerminationPattern(bitsUserMemory, ref
	// bitPosition);
	//
	// long formatFlagsSection = getFormatFlagsSection(bitsUserMemory, ref
	// bitPosition);
	// getObjectInfo(bitsUserMemory, ref bitPosition);
	// }
	//
	// private BitArray makeBitArrayFromByteArray(Byte[] byteArray)
	// {
	// BitArray bitArray = new BitArray(byteArray.Length * 8);
	// int pos = 0;
	// for (Byte b: byteArray)
	// {
	// for (int i = 0; i < 8; i++)
	// {
	// if ((b & 1 << 7 - i) != 0)
	// {
	// bitArray.Set(pos, true);
	// }
	// pos++;
	// }
	// }
	//
	// return bitArray;
	// }
	//
	// private boolean checkTerminationPattern(BitArray bitsUserMemory, ref int
	// bitPosition)
	// {
	// bool terminate = false;
	// if (GetValueFromUserMemory(bitsUserMemory, bitPosition, 8) == 0)
	// {
	// terminate = true;
	// bitPosition += 8;
	// }
	// return terminate;
	// }
	//
	// private long getFormatFlagsSection(BitArray bitsUserMemory, ref int
	// bitPosition)
	// {
	// // A Bit pattern starting with 0000 indicates a formate flags field
	// ulong formatFlagsSection = 0;
	// if (GetValueFromUserMemory(bitsUserMemory, bitPosition, 4) == 0)
	// {
	// // TODO: implementieren wie in TDS 1.5 Appendix I.4
	// throw new
	// TdtTranslationException("Not implemented method getFormatFlagsSection");
	// }
	// return formatFlagsSection;
	// }
	//
	// private void getObjectInfo(BitArray bitsUserMemory, ref int bitPosition)
	// {
	// // Decode Object Info of a packed Object, see TDS 1.5 Appendix I.5
	// ulong objectLength = GetEbvValue(bitsUserMemory, ref bitPosition, 6);
	// bool padIndicator = bitsUserMemory.Get(bitPosition++);
	// int numberOfIds = (int)GetEbvValue(bitsUserMemory, ref bitPosition, 3) +
	// 1;
	//
	// //TEST*******
	// //for (int size = 4; size <= 11; size++)
	// int size = 7;
	// {
	// int bp = bitPosition;
	// Console.Write("Size = " + size.ToString() + " : ");
	// for (int i = 0; i < numberOfIds; i++)
	// {
	// try
	// {
	// int id = (int)GetEbvValue(bitsUserMemory, ref bp, (ushort)size);
	// Console.Write(" ID=" + id);
	// }
	// catch(Exception e)
	// {
	// }
	// }
	// for (bool b: bitsUserMemory)
	// {
	// Console.Write((char)(b==true?'1':'0'));
	// }
	// Console.WriteLine("");
	// }
	// }
	//
	// private static long GetValueFromUserMemory(BitArray bitsUserMemory, int
	// bitPosition, int length)
	// {
	// long value = 0;
	// for (int i = bitPosition; i < (bitPosition + length); i++)
	// {
	// value = value << 1;
	// if (bitsUserMemory.Get(i))
	// {
	// value |= 0x1;
	// }
	// }
	// return value;
	// }
	//
	// private static long GetEbvValue(BitArray bitsUserMemory, ref int
	// bitPosition, ushort bitlength)
	// {
	// ulong value = 0;
	// bool extensionFlag;
	// do
	// {
	// extensionFlag = bitsUserMemory.Get(bitPosition);
	// ulong part = GetValueFromUserMemory(bitsUserMemory, bitPosition + 1,
	// bitlength - 1);
	// bitPosition += bitlength;
	// value = value << (bitlength - 1) | part;
	// } while (extensionFlag);
	// return value;
	// }

	@SuppressWarnings("unused")
	private void decodeUserMemoryISO() {

	}
}