package java.util.regex;

import com.google.gwt.regexp.shared.RegExp;

public class Pattern {

	public static final int CASE_INSENSITIVE = 0x02;
	public static final int MULTILINE = 0x08;
	public static final int UNICODE_CASE = 0x40;

	RegExp regExp;

	protected Pattern(RegExp regExp) {
		this.regExp = regExp;
	}

	public static Pattern compile(String regex) {
		return new Pattern(RegExp.compile(regex));
	}

	public static Pattern compile(String regex, int flag) {
		String gwtFlag;

		switch (flag) {
		case 0:
			gwtFlag = "g";
			break;
		case CASE_INSENSITIVE:
		case UNICODE_CASE:
			gwtFlag = "i";
			break;
		case MULTILINE:
			gwtFlag = "m";
			break;

		default:
			gwtFlag = "";
		}

		return new Pattern(RegExp.compile(regex, gwtFlag));
	}

	public Matcher matcher(CharSequence input) {
		if (regExp.getGlobal()) {
			regExp.setLastIndex(0);
		}
		return new Matcher(this, regExp.exec(input.toString()));
	}

	public static boolean matches(String regex, CharSequence input) {
		return compile(regex).matcher(input).matches();
	}
}
