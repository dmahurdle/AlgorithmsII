


public class Word {
	private String word;
	private Soundex soundex;
	
	public Word(String word) {
		this(word, new Soundex(word));
	}
	
	public Word(String word, Soundex soundex) {
		if ( word == null ) {
			throw new IllegalArgumentException("word must not be null!");
		}
		
		this.word = word;
		this.soundex = soundex;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Soundex getSoundex() {
		return soundex;
	}
	
	public int hashCode() {
		return word.hashCode();
	}
	
	public boolean equals(Object that) {
		if ( that instanceof Word ) {
			return this.word == null ? ((Word)that).word == null : this.word.equals(((Word)that).word);
		}
		return false;
	}
	
	public String toString() {
		return word;
	}
}
