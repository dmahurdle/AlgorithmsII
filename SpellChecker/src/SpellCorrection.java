

public class SpellCorrection {
	private int startIndex;
	private int length;
	private String[] suggestions;
	
	public SpellCorrection(int startIndex, int length, String[] suggestions) {
		this.startIndex = startIndex;
		this.length = length;
		this.suggestions = suggestions;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public int getLength() {
		return length;
	}
	
	public String[] getSuggestions() {
		return suggestions;
	}
}
