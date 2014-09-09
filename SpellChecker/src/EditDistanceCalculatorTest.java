import org.junit.Assert;
import org.junit.Test;


public class EditDistanceCalculatorTest {
	@Test
	public void testEditDistance() {
		EditDistanceCalculator edc = new EditDistanceCalculator();
		Assert.assertEquals(1, edc.editDistance("mispelled", "misspelled"));
		Assert.assertEquals(3, edc.editDistance("algorithms", "logarithms"));
	}
}
