

import java.util.Iterator;
import java.util.function.Consumer;


public class Hashtable<K, V> {
	private Entry<K,V>[] values;
	private int size;
	
	public Hashtable(int initialCapacity) {
		values = (Entry<K,V>[])new Entry[initialCapacity];
	}
	
	/**
	 * #3b. Implement this (1 point)
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		//Calc the hash
		//If no collision, add it there.
		//Else, find the end of the list and add it.
		int h = key.hashCode() % values.length;
		if (values[h] == null)
		{
			values[h] = new Entry(key, value);
			size ++;
		}
		else
		{
			Entry<K, V> cur = values[h];
			boolean broke = false;
			while (cur.next != null && !broke)
			{
				cur = cur.next;
				if (cur.key.equals(key))
				{
					//Break. Don't bother adding anything.
					broke = true;
				}
			}
			if (!broke)
			{
				cur.next = new Entry(key, value);
				size ++;
			}
			
		}
		
	}
	
	/**
	 * #3b. Implement this (1 point)
	 * @param key
	 * @return
	 */
	public V get(K key) {
		int h = key.hashCode() % values.length;
		Entry<K, V> cur = values[h];
		while (cur != null && !cur.key.equals(key))
		{
			cur = cur.next;
		}
		if (cur == null)
		{
			return null;
		}
		else
		{
			return cur.data;
		}
	}

	/**
	 * #3c.  Implement this. (1 point)
	 * 
	 * @param key
	 * @return
	 */
	public V remove(K key) {
		int h = key.hashCode() % values.length;
		if (values[h] == null)
		{
			return null;
		}
		if (values[h].key.equals(key))
		{
			V data = values[h].data;
			values[h] = values[h].next;
			return data;
		}
		Entry<K, V> cur = values[h];
		
		while(cur.next != null && !cur.next.key.equals(key))
		{
			cur = cur.next;
		}
		if (cur.next == null)
		{
			return null;
		}
		else
		{
			V data = cur.next.data;
			cur.next = cur.next.next;
			size--;
			return data;
		}
	}
	
	public int size() {
		return size;
	}
	
	public boolean containsKey(K key) {
		return this.get(key) != null; 
	}

	public Iterator<V> values() {
		return new Iterator<V>() {
			private int count = 0;
			private Entry<K, V> currentEntry;
			
			{
				while ( ( currentEntry = values[count] ) == null && count < values.length ) {
					count++;
				}
			}
			
			@Override
			public void forEachRemaining(Consumer<? super V> arg0) {
			}

			@Override
			public boolean hasNext() {
				return count < values.length;
			}

			@Override
			public V next() {
				V toReturn = currentEntry.data;
				currentEntry = currentEntry.next;
				while ( currentEntry == null && ++count < values.length && (currentEntry = values[count]) == null );
				return toReturn;
			}

			@Override
			public void remove() {
			}
			
		};
	}
	
	private static class Entry<K, V> {
		private K key;
		private V data;
		private Entry<K,V> next;
		
		public Entry(K key, V data) {
			this.key = key;
			this.data = data;
		}
		
		public String toString() {
			return "{" + key + "=" + data + "}";
		}
		public Entry<K, V> getNext()
		{
			return next;
		}
	}
}