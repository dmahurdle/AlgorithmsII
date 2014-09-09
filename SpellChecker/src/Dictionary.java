

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;
import java.util.Hashtable;

public class Dictionary implements Iterable<Word> {
	protected final Hashtable<String, Word> words = new Hashtable<String, Word>(10000);
	protected final WordPreparer wp;
	protected final WordComparer wc;
	
	public Dictionary() {
		this(new SimpleWordPreparer(), new SimpleWordPreparer());
	}
	
	public Dictionary(WordPreparer wp, WordComparer wc) {
		this(
				new MultiInputStream(	
						Dictionary.class.getClassLoader().getResourceAsStream("english-words.10"),
						Dictionary.class.getClassLoader().getResourceAsStream("english-words.20"),
						Dictionary.class.getClassLoader().getResourceAsStream("english-words.35"),
						Dictionary.class.getClassLoader().getResourceAsStream("english-words.40"),
						Dictionary.class.getClassLoader().getResourceAsStream("english-words.50"),
						Dictionary.class.getClassLoader().getResourceAsStream("english-words.55"),
						Dictionary.class.getClassLoader().getResourceAsStream("english-words.60")
				), wp, wc);
	}
	
	/**
	 * Send in your own dictionary (one word per line).  Nice for debugging.
	 * 
	 * @param file
	 */
	public Dictionary(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}
	
	public Dictionary(InputStream is) {
		this(is, new SimpleWordPreparer(), new SimpleWordPreparer());
	}
	
	public Dictionary(InputStream is, WordPreparer wp, WordComparer wc) {
		if ( is == null ) {
			throw new IllegalArgumentException("Empty input stream! If you are using the default constructor, make sure that the file is in your classpath");
		}

		this.wp = wp;
		this.wc = wc;
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String word = null;
			
			while ( ( word = reader.readLine() ) != null ) {
				Word prepared = wp.prepare(word);
				words.put(word, prepared);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Could not read input stream: " + is);
		}
	}

	/** Add word to the dictionary manually **/
	public void add(String word) {
		words.put(word, wp.prepare(word));
	}
	
	public Word get(String word) {
		return words.get(word);
	}
	
	public boolean hasWord(String word) {
		return words.containsKey(word);
	}
	
	public Iterator<Word> iterator() {
		return words.values().iterator();
	}
	
	public int size() {
		return words.size();
	}
	
	public Set<Word> getRelatedWords(String word) {
		return wc.getRelatedWords(word);
	}
}