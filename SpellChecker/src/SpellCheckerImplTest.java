


import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class SpellCheckerImplTest {
	@Test
	public void testCheckWord() {
		SpellChecker sp = new SpellCheckerImpl();
		Assert.assertNull(sp.checkWord("cat"));
	}
	
	@Test
	public void testCheckWordMisspelled() {
		SpellChecker sp = new SpellCheckerImpl();
		System.out.println(Arrays.asList(sp.checkWord("catt").getSuggestions()));
	}
	
	@Test
	public void testCheckWordCorrectWord() throws Exception {
		SpellChecker sc = new SpellCheckerImpl();
		SpellCorrection correction = sc.checkWord("fish");
		if ( !(correction == null || correction.getSuggestions() == null || correction.getSuggestions().length == 0 || correction.getSuggestions()[0] == null || correction.getSuggestions()[0].equals("fish")) ) {
			Assert.fail("Shouldn't give suggestions for correct word.");
		}		
	}
	
	@Test
	public void testCheckWordMisspelledWord() throws Exception {
		SpellChecker sc = new SpellCheckerImpl();
		SpellCorrection correction = sc.checkWord("mispelled");
		Assert.assertNotNull(correction.getSuggestions());
		Assert.assertEquals(5, correction.getSuggestions().length);
		Assert.assertNotNull(correction.getSuggestions()[0]);
	}

	@Test
	public void testCheckWordMisspelledWordSuggestionsInOrder() throws Exception {
		SpellChecker sc = new SpellCheckerImpl();
		SpellCorrection correction = sc.checkWord("mispelled");
		Assert.assertNotNull(correction.getSuggestions());
		
		int min = -1;
		EditDistanceCalculator edc = new EditDistanceCalculator();		
		for ( String suggestion : correction.getSuggestions() ) {
			int d = edc.editDistance("mispelled", suggestion);
			System.out.println(suggestion + ":" + d);
			if ( d >= min ) {
				min = d;
			} else {
				Assert.fail("Should be in order of edit distance");
			}
		}
	}
	
	@Test
	public void testCheckWordMisspelledWordHasSpecificSuggestion() throws Exception {
		SpellChecker sc = new SpellCheckerImpl();
		SpellCorrection correction = sc.checkWord("mispelled");
		Assert.assertNotNull(correction.getSuggestions());
		Assert.assertTrue(Arrays.asList(correction.getSuggestions()).contains("misspelled"));
	}

}
