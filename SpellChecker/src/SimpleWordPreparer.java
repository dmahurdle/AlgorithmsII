import java.util.HashSet;
import java.util.Set;



public class SimpleWordPreparer implements WordPreparer, WordComparer {

	@Override
	public Word prepare(String word) {
		return new Word(word);
	}

	@Override
	public Set<Word> getRelatedWords(String word) {
		return new HashSet<Word>();
	}

	
}
