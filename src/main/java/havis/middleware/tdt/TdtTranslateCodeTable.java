package havis.middleware.tdt;

class TdtTranslateCodeTable {

	static String getCharacter(String hexValue) {
		final String character = "";
		switch (Integer.valueOf(hexValue, 16).intValue()) {
		case 0x20:
			return " ";
		case 0x21:
			return "!";
		case 0x22:
			return "\"";
		case 0x25:
			return "%";
		case 0x26:
			return "&";
		case 0x27:
			return "'";
		case 0x28:
			return "(";
		case 0x29:
			return ")";
		case 0x2A:
			return "*";
		case 0x2B:
			return "+";
		case 0x2C:
			return ",";
		case 0x2D:
			return "-";
		case 0x2E:
			return ".";
		case 0x2F:
			return "/";
		case 0x30:
			return "0";
		case 0x31:
			return "1";
		case 0x32:
			return "2";
		case 0x33:
			return "3";
		case 0x34:
			return "4";
		case 0x35:
			return "5";
		case 0x36:
			return "6";
		case 0x37:
			return "7";
		case 0x38:
			return "8";
		case 0x39:
			return "9";
		case 0x3A:
			return ":";
		case 0x3B:
			return ";";
		case 0x3C:
			return "<";
		case 0x3D:
			return "=";
		case 0x3E:
			return ">";
		case 0x3F:
			return "?";
		case 0x41:
			return "A";
		case 0x42:
			return "B";
		case 0x43:
			return "C";
		case 0x44:
			return "D";
		case 0x45:
			return "E";
		case 0x46:
			return "F";
		case 0x47:
			return "G";
		case 0x48:
			return "H";
		case 0x49:
			return "I";
		case 0x4A:
			return "J";
		case 0x4B:
			return "K";
		case 0x4C:
			return "L";
		case 0x4D:
			return "M";
		case 0x4E:
			return "N";
		case 0x4F:
			return "O";
		case 0x50:
			return "P";
		case 0x51:
			return "Q";
		case 0x52:
			return "R";
		case 0x53:
			return "S";
		case 0x54:
			return "T";
		case 0x55:
			return "U";
		case 0x56:
			return "V";
		case 0x57:
			return "W";
		case 0x58:
			return "X";
		case 0x59:
			return "Y";
		case 0x5A:
			return "Z";
		case 0x5F:
			return "_";
		case 0x61:
			return "a";
		case 0x62:
			return "b";
		case 0x63:
			return "c";
		case 0x64:
			return "d";
		case 0x65:
			return "e";
		case 0x66:
			return "f";
		case 0x67:
			return "g";
		case 0x68:
			return "h";
		case 0x69:
			return "i";
		case 0x6A:
			return "j";
		case 0x6B:
			return "k";
		case 0x6C:
			return "l";
		case 0x6D:
			return "m";
		case 0x6E:
			return "n";
		case 0x6F:
			return "o";
		case 0x70:
			return "p";
		case 0x71:
			return "q";
		case 0x72:
			return "r";
		case 0x73:
			return "s";
		case 0x74:
			return "t";
		case 0x75:
			return "u";
		case 0x76:
			return "v";
		case 0x77:
			return "w";
		case 0x78:
			return "x";
		case 0x79:
			return "y";
		case 0x7A:
			return "z";
		}
		// TODO: throw exception ??
		return character;
	}

	static String getBitString(char c, int numberOfBits) {
		StringBuilder s = new StringBuilder(Integer.toBinaryString(c));
		while (s.length() < numberOfBits)
			s.insert(0, '0');
		return s.toString();
	}

	static String encodeURIForm(String value) throws TdtTranslationException {
		StringBuilder uriForm = new StringBuilder();

		for (char c : value.toCharArray()) {
			switch (c) {
			case '!':
			case '\'':
			case '(':
			case ')':
			case '*':
			case '+':
			case ',':
			case '-':
			case '.':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case ':':
			case ';':
			case '=':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
			case '_':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z':
				uriForm.append(c);
				break;
			case '"':
				uriForm.append("%22");
				break;
			case '%':
				uriForm.append("%25");
				break;
			case '&':
				uriForm.append("%26");
				break;
			case '/':
				uriForm.append("%2F");
				break;
			case '<':
				uriForm.append("%3C");
				break;
			case '>':
				uriForm.append("%3E");
				break;
			case '?':
				uriForm.append("%3F");
				break;
			default:
				throw new TdtTranslationException("Invalid Character in urn '"
						+ c + "'");
			}
		}
		return uriForm.toString();
	}

	static String decodeURIForm(String value) throws TdtTranslationException {
		StringBuilder stringForm = new StringBuilder();
		short ignore = 0;
		short pos = 0;
		for (char c : value.toCharArray()) {
			if (c == '%') {
			    int hexStart = pos + 1;
				String uriCode = value.substring(hexStart, hexStart + 2);
				switch (Integer.valueOf(uriCode, 16).intValue()) {
				case 0x22:
					stringForm.append('"');
					break;
				case 0x25:
					stringForm.append('%');
					break;
				case 0x26:
					stringForm.append('&');
					break;
				case 0x2F:
					stringForm.append('/');
					break;
				case 0x3C:
					stringForm.append('<');
					break;
				case 0x3E:
					stringForm.append('>');
					break;
				case 0x3F:
					stringForm.append('?');
					break;
				default:
					throw new TdtTranslationException("Invalid %sequence '"
							+ uriCode + "' in decoding urn");
				}
				ignore += 2;
			} else if (ignore > 0) {
				ignore--;
			} else {
				stringForm.append(c);
			}
			pos++;
		}
		return stringForm.toString();
	}
}
