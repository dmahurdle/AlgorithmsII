import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class HeapBasedPriorityQueue<T extends Comparable<T>> {
	
	public T[] array =  (T[]) (new Comparable[16]);
	
	//Heap dynamically re-sizes. Standard rules.
	
	private int size = 0;
	
	
	public int size()
	{
		return size;
	}
	public boolean offer(T data)
	{
		//logn
		//Resize is amortized constant
		//Add to end of array, which is tracked by size.
		//System.out.println("offer " + size);
		array[size] = data;
		//Now percolate it up.
		percolate(size);
		size ++;
		if (size == array.length)
		{
			increaseSize();
		}
		//System.out.println(toString());
		return true;
	}
	
	public void remove(int index)
	{
		//logn (percolate)
		//Swap with last element.
		swap(index, size-1);
		//Now reduce size by 1.
		size --;
		//Now percolate the element at index down.
		percolateDown(index);
		
		//int newIndex = forceSink(index);
		//swap(newIndex, size-1);
		//size --;
		
		
	}
	public void percolateDown(int index)
	{
		//Worst case: logn.
		int[] children = getChildrenIndices(index);
		
		if (children[0] >= size && children[1] >= size)
		{
			//This is the bottom. Stop sinking.
			return;
		}
		
		int c = children[0];
		if (children[0] >= size)
		{
			c = children[1];
		}
		else if (children[1] < size && array[children[1]].compareTo(array[children[0]]) > 0)
		{
			c = children[1];
		}
		if (array[c].compareTo(array[index]) > 0)
		{
			swap(c, index);
			percolateDown(c);
		}
	}
	
	public int getIndexOf(T data)
	{
		for(int i = 0; i < size; i ++)
		{
			if (array[i].compareTo(data) == 0)
			{
				return i;
			}
		}
		return -1;
	}
	
	private int[] getChildrenIndices(int parent)
	{
		int[] c = new int[2];
		c[0] = (parent * 2) + 1;
		c[1] = (parent * 2) + 2;
		return c;
	}
	
	public boolean contains(T data)
	{
		for(int i = 0 ;i < size; i ++)
		{
			if (array[i].compareTo(data) == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	private void percolate(int index)
	{
		//Worst case (percolates ALL the way up) = logn.
		//Get parent.
		if (index == 0)
		{
			return;
		}
		int parent = getParent(index);
		if (array[parent] == null)
		{
			System.out.println("Broke at index : " + index + " parent : " + parent);
		}
		if (array[index].compareTo(array[parent]) > 0)
		{
			//Swap them.
			swap(index, parent);
			//Then percolate again at the new index.
			percolate(parent);
		}
	}
	
	private void swap(int a, int b)
	{
		T temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	private int getParent(int childIndex)
	{
		return (int)(childIndex - 1) / 2;
	}
	
	private void increaseSize()
	{
		T[] newArr = (T[])(new Comparable[array.length * 2]);
		for(int i = 0; i < array.length; i ++)
		{
			newArr[i] = array[i];
		}
		array = newArr;
	}
	
	public T peek()
	{
		//return first element.
		return array[0];
	}
	
	public T poll()
	{
		//logn
		T tmp = array[0];
		remove(0);
		return tmp;
	}
	
	public String toString()
	{
		String res = "";
		for(int i= 0 ; i < size; i ++)
		{
			res += array[i].toString() + (i == size - 1? "" : ", ");
		}
		return res;
	}
	
}
