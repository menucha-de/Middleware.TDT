package java.util.regex;

public class Matcher {

	com.google.gwt.regexp.shared.MatchResult result;
	Pattern pattern;

	protected Matcher(Pattern pattern, com.google.gwt.regexp.shared.MatchResult matchResult) {
		this.result = matchResult;
		this.pattern = pattern;
	}

	public boolean matches() {
		return result != null;
	}

	public int groupCount() {
		return result.getGroupCount();
	}

	public String group(int group) {
		return result.getGroup(group);
	}
	
	public String group() {
		String group = group(0);
		this.result = pattern.regExp.exec(this.result.getInput());

		return group;
	}
	
	public boolean find() {
		return matches();
	}
}