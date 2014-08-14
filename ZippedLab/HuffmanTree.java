import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.PriorityQueue;

import edu.neumont.io.Bits;


public class HuffmanTree {
	static class Node implements Comparable<Node>
	{
		//value stores the value of a leaf node or null if node is a hub node.
		Byte value = null;
		//if node is leaf, left and right are null.
		int freq;
		Node left = null;
		Node right = null;
		public Node(byte val, int freq)
		{
			this.value = val;
			this.freq = freq;
		}
		public Node(Node left, Node right, int freq)
		{
			this.left = left;
			this.right = right;
			this.freq = freq;
		}
		
		public boolean isLeaf()
		{
			return value != null;
		}
		@Override
		public int compareTo(Node other) {
			//return (this.freq == other.freq? 0 : this.freq - other.freq);
			return (this.freq > other.freq ? 1 : this.freq == other.freq ? 0 : -1);
		}
	}
	
	Node root = null;
	
	int count[] = new int[256];
	
	public HuffmanTree(int[] frequencyData)
	{
		//Same as the other constructor, but takes an array of frequencies ordered from -128 to 127.
		for (int i = 0 ; i < 256; i ++)
		{
			//Tricky... Need to modify the indices.
			count[(byte)(i - 128)&0xff] = frequencyData[i];
			//I think that's it...
		}
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		
		double total = 0;
		for(int b = 0; b < count.length; b ++)
		{
			if (count[b] > 0)
			{
				total += count[b];
			}
		}
		for(int b = 0; b < count.length; b ++)
		{
			if (count[b] > 0)
			{
				//queue.add(new Node((byte)b, count[b]));
				queue.add(new Node((byte)(b - 128), count[(byte)(b - 128)&0xff]));
			}
		}
		
		Node[] ns = new Node[queue.size()];
		int ind = 0;
		for(Node n : queue)
		{
			ns[ind++] = n;
		}
		//Arrays.sort(ns);

		//System.out.println(frequencyData.length);
		//System.out.println(total);
		/*
		for(Node n : ns)
		{
			char[] c = getHexRep(n.value);
			System.out.println("" + c[0] + c[1] + " | " + n.freq);
		}
		*/
		while (queue.size() > 1)
		{
			Node n1 = queue.poll();
			Node n2 = queue.poll();
			int freqSum = n1.freq + n2.freq;
			Node newNode = new Node(n1, n2, freqSum);
			queue.offer(newNode);
		}
		root = queue.poll();
	}
	
	public HuffmanTree(byte[] data)
	{
		//Making the tree is n + n worst case, n + 1 best case. Always Theta(n)
		for(int i = 0; i < data.length; i ++)
		{
			count[(int)(data[i] & 0xff)] ++;
		}
		//count[] now holds all the frequencies of all the bytes by index.
		//Now it's time to build a priority queue from these bytes.
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		
		float total = 0;
		for(int b = 0; b < count.length; b ++)
		{
			if (count[b] > 0)
			{
				total += count[b];
			}
		}
		
		//System.out.println(total);
		for(int b = 0; b < count.length; b ++)
		{
			if (count[b] > 0)
			{
				queue.add(new Node((byte)b, count[b]));
			}
		}
		
		Node[] ns = new Node[queue.size()];
		int ind = 0;
		for(Node n : queue)
		{
			ns[ind++] = n;
		}
		/*
		Arrays.sort(ns);
		
		for(Node n : ns)
		{
			char[] c = getHexRep(n.value);
			System.out.println("" + c[0] + c[1] + " | " + n.freq);
		}
		*/
		while (queue.size() > 1)
		{
			Node n1 = queue.poll();
			Node n2 = queue.poll();
			int freqSum = n1.freq + n2.freq;
			Node newNode = new Node(n1, n2, freqSum);
			queue.add(newNode);
		}
		root = queue.poll();
	}
	
	
	public byte toByte(Bits bits)
	{
		//Average case is ~logn. 
		return go(root, bits);
	}
	
	public void fromByte(byte b, Bits bits)
	{
		//Need to traverse the tree trending right.
		//When you find the leaf that has the byte value b, you need to pop it's path back up...
		//It'll return it in reverse order, so you'll have to reverse it by hand once it's here.
		
		//find + pathLength (average of < logn)
		//logn + logn = 2logn = Theta(logn)
		Bits bitsToAdd = new Bits();
		if (find(root, b, bitsToAdd))
		{
			Boolean[] bools = new Boolean[bitsToAdd.size()];
			bitsToAdd.toArray(bools);
			for(int i = bools.length - 1; i >= 0; i --)
			{
				bits.add(bools[i]);
			}
		}
		else
		{
			System.out.println("CAN'T FIND BYTE : " + b);
		}
	}
	
	private boolean find(Node r, byte b, Bits bits)
	{
		//Calculating the path to byte b in tree r and placing it in bits.
		//worst case n (byte b is the last byte you check).
		//Best case 1 (byte b is the first right node in the tree.
		//Average case is logn because of probability and tree shape.
		if (r.isLeaf())
		{
			return r.value == b;
		}
		else if (find(r.left, b, bits)){
			bits.add(false);
			return true;
		}
		else if (find(r.right, b, bits)){
			bits.add(true);
			return true;
		}
		else return false;
	}
	
	private byte go(Node r, Bits bits)
	{
		//Finds the next byte represented in bits.
		//Worst case is n/2
		//Best case is 1.
		//Average case is logn
		if (r.isLeaf())
		{
			return r.value;
		}
		boolean right = bits.poll();
		Node next = right? r.right : r.left;
		if (next == null)
		{
			System.out.println("ERROR! HUFFMAN TREE CORRUPTION! NOT LEAF BUT HAS NO " + (right? "right" : "left") + " child!");
			return 0;
		}
		else
		{
			return go(next, bits);
		}
	}
	
	public void printMe()
	{
		int layers = getLayerCount();
		char arr[][] = new char[ 4 * (int) Math.pow(2, layers)][ layers * 3 + 10 ];
		for(int i = 0; i < arr.length; i ++)
		{
			for (int j = 0; j < arr[i].length; j ++)
			{
				arr[i][j] = ' ';
			}
		}
		printBranch(root, arr, arr.length / 2,0, layers);
		//System.out.println();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("printOut.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int x = 0; x < arr[0].length; x ++)
		{
			for (int y = 0; y < arr.length; y ++)
			{
				if (arr.length < 100)
				{
					System.out.print(arr[y][x]);
				}
				writer.print(arr[y][x]);
			}
			if (arr.length < 100)
			{
				System.out.println();
			}
			writer.println();
		}
		if (arr.length >= 100)
		{
			
		}
	}
	
	int deepestLayer;
	public int getLayerCount()
	{
		//Traverse the path and find the least one...
		
		traverse(root, 1);
		return deepestLayer;
	}
	public void traverse(Node r, int depth)
	{
		if (depth > deepestLayer)
		{
			deepestLayer = depth;
		}
		if (r.right != null)
		{
			traverse(r.right, depth + 1);
		}
		if (r.left != null)
		{
			traverse(r.left, depth + 1);
		}
	}
	
	public void printBranch(Node r, char[][] arr, int startX, int startY, int level)
	{
		//Place this node at the startx, starty.
		if (!r.isLeaf())
		{
			arr[startX][startY+1] = 'N';
			arr[startX+1][startY + 1] = 'N';
			
			//Now you have to print the nodes at left and right.
			//First get the distance to the next nodes. It should be level ^ 2.
			int dist = (int) Math.pow(2, level);
			if (r.left != null)
			{
				int newx = startX - dist;
				int newy = startY + 3;
				//Print the line to it.
				for(int i = 0; i < dist; i ++)
				{
					arr[startX - 1 -i][startY + 1 + (i < dist/3 ? 1 : i < 2*dist/3 ? 2 : 3)] = '/';
				}
				printBranch(r.left, arr, newx, newy, level - 1);
			}
			if (r.right != null)
			{
				int newx = startX + dist;
				int newy = startY + 3;
				for(int i = 0; i < dist; i ++)
				{
					arr[startX + 1 +i][startY + 1 + (i < dist/3 ? 1 : i < 2*dist/3 ? 2 : 3)] = '\\';
				}
				printBranch(r.right, arr, newx, newy, level - 1);
			}
		}
		else
		{
			//get the two-character representation of the byte.
			char[] rep = getHexRep(r.value);
			arr[startX][startY+1] = rep[0];
			arr[startX+1][startY+1] = rep[1];
		}
		
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public char[] getHexRep(byte b)
	{
		char[] c = new char[2];
		int v = (b & 0xFF);
		c[0] = hexArray[v >>>4];
		c[1] = hexArray[v & 0x0F];
		return c;
	}
}
