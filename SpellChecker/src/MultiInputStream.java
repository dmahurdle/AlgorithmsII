

import java.io.IOException;
import java.io.InputStream;

/** A utility function for combining multiple streams into one.
 * 
 * Useful for taking many spelling lists and processing them as if they were one stream.
 * 
 * @author jcummings
 *
 */
public class MultiInputStream extends InputStream {
	private InputStream[] is;
	private int currentIndex;
	
	public MultiInputStream(InputStream... is) {
		this.is = is;
	}
	
	public void close() {
		for ( InputStream i : is ) {
			try {
				i.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int read() throws IOException {
		int read = is[currentIndex].read();
		if ( read == -1 && currentIndex + 1 < is.length ) {
			currentIndex++;
			read = is[currentIndex].read();
		}
		return read;
	}
}
