

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class SpellCheckerImpl implements SpellChecker {
	private SoundexWordPreparer p = new SoundexWordPreparer();
	private Dictionary d = new Dictionary(p, p);
	private EditDistanceCalculator edc = new EditDistanceCalculator();
	
	@Override
	public SpellCorrection checkWord(String word) {
		SpellCorrection sc = null;
		if ( !d.hasWord(word) ) {
			// add soundex possibilities
			Set<Word> similarSoundingWords = d.getRelatedWords(word);
			PriorityQueue<Candidate> candidates = new PriorityQueue<Candidate>();
			for ( Word similar : similarSoundingWords ) {
				Candidate c = new Candidate(similar, edc.editDistance(word, similar.getWord()));
				candidates.add(c);
			}

			String[] suggestions = new String[Math.min(5, candidates.size())];
			for ( int i = 0; i < suggestions.length; i++ ) {
				suggestions[i] = candidates.poll().word.getWord();
			}
			
			sc = new SpellCorrection(0, word.length(), suggestions);
		}
		return sc;
	}

	private static class Candidate implements Comparable<Candidate> {
		final Word word;
		final int editDistance;
		
		public Candidate(Word word, int editDistance) {
			this.word = word;
			this.editDistance = editDistance;
		}

		@Override
		public int compareTo(Candidate that) {
			return new Integer(this.editDistance).compareTo(that.editDistance);
		}
	}
}
