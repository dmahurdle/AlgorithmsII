import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import apple.laf.JRSUIUtils.Tree;

public class AVLBasedPriorityQueue<T extends Comparable<T>> {

	public static class Node<T>
	{
		public T data;
		public Node<T> left;
		public Node<T> right;
		public Node<T> parent;
		public int balanceFactor;
		public Node(T data, Node<T> parent)
		{
			this.data = data;
			this.left = null;
			this.right = null;
			this.parent = parent;
			balanceFactor = 0;
		}
		
		public boolean isLeaf()
		{
			return (left == null && right == null);
		}
	}

	private Node<T> treeRoot;
	
	private int size;
	
	public T poll()
	{
		//logn + log^2 n
		//log^2 n. Because of my getHeight function.
		Node<T> minNode = getMinNode(treeRoot);
		//Now remove this node. Since it's the min, all we need to do is move min.right up (if it's not null)
		//and then balance.
		if (minNode.parent != null)
		{
			minNode.parent.left = minNode.right; //This will work even if minNode.right == null.
			if (minNode.parent.left != null)
			{
				minNode.parent.left.parent = minNode.parent;
			}
		
			//Now rebalance.
			//We know minNode.right (aka minNode.parent.left) is balanced already. Don't bother balancing it.
			//log^2 n
			balance(minNode.parent);
		}
		
		else
		{
			//minNode is the treeRoot
			if (minNode.right != null)
			{
				treeRoot = minNode.right;
				minNode.right.parent = null;
				size--;
				return minNode.data;
				
				/*
				T d = treeRoot.right.data;
				treeRoot.right = null;
				size--;
				return d;
				*/
				
			}
			else
			{
				size --;
				treeRoot = null;
				return minNode.data;
			}
		}
		size --;
		//printMe();
		return minNode.data;
		//That should be it... Theoretically.
	}
	
	private Node<T> getMinNode(Node<T> tree)
	{
		Node<T> currentNode = tree;
		while (currentNode.left != null)
		{
			currentNode = currentNode.left;
		}
		return currentNode;
	}
	
	private void leftRotate(Node<T> root)
	{
		Node<T> c = root.right;
		root.right = c.left;
		c.left = root;
		if (root.right != null)
		root.right.parent = root;
		c.parent = root.parent;
		if (root.parent != null)
		{
			if (root.parent.left == root)
			{
				root.parent.left = c;
			}
			else {
				root.parent.right = c;
			}
		}
		else
		{
			treeRoot = c;
		}
		c.left.parent = c;
		c.balanceFactor = getBalanceFactor(c);
		root.balanceFactor = getBalanceFactor(root);
	}
	
	private void rightRotate(Node<T> root)
	{
		Node<T> c = root.left;
		root.left = c.right;
		c.right = root;
		if (root.left != null)
			root.left.parent = root;
		c.parent = root.parent;
		if (root.parent != null)
		{
			if (root.parent.left == root)
			{
				root.parent.left = c;
			}
			else {
				root.parent.right = c;
			}
		}
		else
		{
			treeRoot = c;
		}
		c.right.parent = c;

		c.balanceFactor = getBalanceFactor(c);
		root.balanceFactor = getBalanceFactor(root);
	}
	
	private void rlRotate(Node<T> root)
	{
		rightRotate(root.right);
		leftRotate(root);
	}
	private void lrRotate(Node<T> root)
	{
		leftRotate(root.left);
		rightRotate(root);
	}
	
	private int getBalanceFactor(Node<T> node)
	{
		return getHeight(node.left) - getHeight(node.right);
	}
	
	private int getHeight(Node<T> node)
	{
		if (node == null)
		{
			return 0;
		}
		if (node.left == null && node.right == null)
		{
			return 1;
		}
		else
		{
			return Math.max(getHeight(node.left) + 1, getHeight(node.right) + 1);
		}
	}
	
	private Node<T> insert(T data, Node<T> tree)
	{
		//logn
		//Follows down the tree.
		if (data.compareTo(tree.data) > 0)
		{
			if (tree.right == null)
			{
				tree.right = new Node<T>(data, tree);
				return tree.right;
			}
			else
			{
				return insert(data, tree.right);
			}
		}
		else
		{
			if (tree.left == null)
			{
				tree.left = new Node<T>(data, tree);
				return tree.left;
			}
			else
			{
				return insert(data, tree.left);
			}
		}
	}
	
	private void balance(Node<T> node)
	{
		// 2logn * logn (balance factor * recurse) + constant.
		//That makes this log^2 (n).
		
		//newNode is a leaf. Follow the parents.
		//Go up until you find a 2.
		//REMEBER!!!: - means the right is heavy. + means the left is heavy.
		node.balanceFactor = getBalanceFactor(node);
		if (node.balanceFactor == 2)
		{
			if (node.left.balanceFactor >= 0)
			{
				//Standard right rotation case.
				rightRotate(node);
			}
			else
			{
				//LR case.
				lrRotate(node);
			}
		}
		else if (node.balanceFactor == -2)
		{
			if (node.right.balanceFactor <= 0)
			{
				//Left rotate case.
				leftRotate(node);
			}
			else
			{
				//Right/Left rotate case.
				rlRotate(node);
				//If you do any rotations, you need to recalculate this nodes balance factor.
			}
		}
		//Then we'll want to balance the higher up whether this one balanced or not.
		if (node.parent != null)
		{
			balance(node.parent);
		}
	}
	
	public T peek()
	{
		//Just return the smallest node.
		//logn
		return getMinNode(treeRoot).data;
	}
	
	public boolean offer(T data)
	{
		//Remember, all rotations are constant time.
		//This is insert + balance.
		//balance is log^2 (n) (it follows up the tree doing constant time rotations here and there)
		//insert is logn as well (it follows down the tree doing constant time rotations here and there).
		//!!!     log^2 (n)    !!!!
		//System.out.println("offer " + data.toString());
		
		if (treeRoot == null)
		{
			treeRoot = new Node<T>(data, null);
			size ++;
			return true;
		}
		else
		{
			Node<T> inserted = insert(data, treeRoot);
			if (inserted == null)
			{
				return false;
			}
			
			balance(inserted);
			size++;
			return true;
		}
	}
	
	public int size()
	{
		return size;
	}
	
	public void printMe()
	{
		int layers = getLayerCount();
		char arr[][] = new char[ 10 * (int) Math.pow(2, layers)][ layers * 3 + 10 ];
		for(int i = 0; i < arr.length; i ++)
		{
			for (int j = 0; j < arr[i].length; j ++)
			{
				arr[i][j] = ' ';
			}
		}
		printBranch(treeRoot, arr, arr.length / 2,0, layers);
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
				if (arr.length < 800)
				{
					System.out.print(arr[y][x]);
				}
				writer.print(arr[y][x]);
			}
			if (arr.length < 800)
			{
				System.out.println();
			}
			writer.println();
		}
		if (arr.length >= 800)
		{
			System.out.println("Tree too large to print in console. See printOut.txt");
		}
	}
	
	int deepestLayer;
	public int getLayerCount()
	{
		//Traverse the path and find the least one...
		
		//traverse(treeRoot, 1);
		//return deepestLayer;
		return (int) Math.ceil((Math.log(size) / Math.log(2)));
	}
	public void traverse(Node<T> r, int depth)
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
	
	public void printBranch(Node<T> r, char[][] arr, int startX, int startY, int level)
	{
		//Place this node at the startx, starty.
		if (!r.isLeaf())
		{
			//no...
			char[] rep = r.data.toString().toCharArray();
			arr[startX][startY+1] = rep[0];
			if (rep.length > 1)
			arr[startX+1][startY+1] = rep[1];
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
			char[] rep = r.data.toString().toCharArray();
			arr[startX][startY+1] = rep[0];
			if (rep.length > 1)
			arr[startX+1][startY+1] = rep[1];
		}
	}
}
