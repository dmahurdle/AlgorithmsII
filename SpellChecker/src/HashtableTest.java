import java.util.Arrays;
//import java.util.Hashtable;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;



/**
 * NOTE:  DON'T FORGET TO REMOVE THE ABOVE IMPORT STATEMENT WHEN YOU ARE READY TO TEST YOUR IMPLEMENTATION
 * 
 * @author jcummings
 *
 */
public class HashtableTest {
	@Test
	public void testEmpty() {
		Hashtable<Soundex, List<Word>> wordsBySoundex = new Hashtable<Soundex, List<Word>>(1);
		Assert.assertNull(wordsBySoundex.get(new Soundex("bobsyeruncle")));
		Assert.assertEquals(0, wordsBySoundex.size());
		Assert.assertNull(wordsBySoundex.remove(new Soundex("bobsyeruncle")));
	}
	
	@Test
	public void testPutAndGet() {
		Hashtable<Soundex, List<Word>> wordsBySoundex = new Hashtable<Soundex, List<Word>>(1);
		Word w = new Word("misspelled");
		List<Word> words = Arrays.asList(w);
		wordsBySoundex.put(w.getSoundex(), words);
		Assert.assertEquals(words, wordsBySoundex.get(w.getSoundex()));
	}
	
	@Test
	public void testRemove() {
		Hashtable<Soundex, List<Word>> wordsBySoundex = new Hashtable<Soundex, List<Word>>(1);
		Word w = new Word("misspelled");
		List<Word> words = Arrays.asList(w);
		wordsBySoundex.put(w.getSoundex(), words);
		Assert.assertEquals(words, wordsBySoundex.get(w.getSoundex()));
		wordsBySoundex.remove(w.getSoundex());
		Assert.assertNull(wordsBySoundex.get(w.getSoundex()));
	}
	
}