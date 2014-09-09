

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
//import java.util.Hashtable;

public class SoundexWordPreparer implements WordPreparer, WordComparer {
	private Hashtable<Soundex, Set<Word>> wordsBySoundex = new Hashtable<Soundex, Set<Word>>(26000);
	
	@Override
	public Word prepare(String word) {
		Soundex s = new Soundex(word);
		Word w = new Word(word, s);
		Set<Word> words = wordsBySoundex.get(s);
		if ( words == null ) {
			words = new HashSet<Word>();
			wordsBySoundex.put(s, words);
		}
		words.add(w);
		return w;
	}
	
	public Set<Word> getRelatedWords(String word) {
		Set<Word> words = wordsBySoundex.get(new Soundex(word));
		if ( words == null ) 
		{
			words = new HashSet<Word>();
		}
		return Collections.unmodifiableSet(words);
	}
		

}
