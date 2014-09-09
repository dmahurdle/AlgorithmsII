


import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;

public class DictionaryTest {
	@Test
	public void testDictionary() {
		Dictionary d = new Dictionary();
		Assert.assertEquals(97316,  d.size());
	}
	
	@Test
	public void testDictionaryInputStream() {
		Dictionary d = new Dictionary(new ByteArrayInputStream("apple\npear\nbanana".getBytes()));
		Assert.assertEquals(3, d.size());
	}
	
	
	@Test
	public void testDictionaryAddWord() {
		Dictionary d = new Dictionary(new ByteArrayInputStream(new byte[0]));
		Assert.assertEquals(0, d.size());
		d.add("apple");
		Assert.assertEquals(1, d.size());
	}
}
