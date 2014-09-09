import java.util.Arrays;
import java.util.List;

import org.junit.Assert;


public class Hashing {

	public static int hash(String str)
	{
		int c = 0 ;
		for(int i = 0; i < str.length(); i ++)
		{
			c += (256 * i) * str.charAt(i);
		}
		return c;
	}
	
	public static void main(String[] args)
	{
		Hashtable<Soundex, List<Word>> wordsBySoundex = new Hashtable<Soundex, List<Word>>(1);
		Word w = new Word("misspelled");
		List<Word> words = Arrays.asList(w);
		wordsBySoundex.put(w.getSoundex(), words);
		boolean equal = words.equals(wordsBySoundex.get(w.getSoundex()));
		
	}
}
