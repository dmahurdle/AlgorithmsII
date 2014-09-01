import java.util.ArrayList;
import java.util.List;


public class DotsAndBoxes {
	Graph g;
	int rows, cols;
	private int scores[] = new int[2];
	
	private List<Integer> edges = new ArrayList<Integer>();
	
	public DotsAndBoxes(int rows, int columns)
	{
		g = new Graph((rows+1) * (columns+1));
		this.rows = rows + 1;
		this.cols = columns + 1;
		
		//Setup original connections
		for(int r = 1; r < rows; r ++)
		{
			for (int c = 1; c < cols - 1; c ++)
			{
				g.add2Edge(getIndex(r, c), getIndex(r-1, c), 1);
				g.add2Edge(getIndex(r, c), getIndex(r+1, c), 1);
				g.add2Edge(getIndex(r, c), getIndex(r, c-1), 1);
				g.add2Edge(getIndex(r, c), getIndex(r, c+1), 1);
			}
		}
		
		for(int r = 0; r < this.rows; r ++)
		{
			edges.add(getIndex(r, 0));
			edges.add(getIndex(r, cols-1));
		}
		for(int c = 1; c < cols-1; c ++)
		{
			edges.add(getIndex(0, c));
			edges.add(getIndex(this.rows-1, c));
		}
		//Now to get the edges for simplified edge detection for scoring.
		if(true)
		{
			System.out.print("");
		}
	}
	

	public int getIndex(int row, int col)
	{
		return row * cols + col;
	}
	public int drawLine(int player, int x1, int y1, int x2, int y2)
	{
		//First make sure this is a legal move...
		if ((Math.abs(x1 - x2) + Math.abs(y1 - y2) != 1))
		{
			return -1;
		}
		// draws a line from (x1, y1) to (x2, y2) (0,0) is in the upper-left corner, returning how many points were earned, if any
		
		// Since my implementation is coins and strings, it CUTS a line between the two coins based on their relation to each other...
		
		int coins[] = new int[2];
		if (x1 == x2)
		{
			int maxY = Math.max(y1, y2);
			g.remove2Edge(getIndex(x1, maxY), getIndex(x1 + 1, maxY));
			coins[0] = getIndex(x1, maxY);
			coins[1] = getIndex(x1 + 1, maxY);
		}
		else
		{
			int maxX = Math.max(x1, x2);
			g.remove2Edge(getIndex(maxX, y1), getIndex(maxX, y1 + 1));
			coins[0] = getIndex(maxX, y1);
			coins[1] = getIndex(maxX, y1+1);
		}
		//The line has been drawn and the coins the string connected are in coins[].
		//The following line doesn't work for edges.
		int points = ((g.degree(coins[0]) == 0 && !isEdge(coins[0]))? 1 : 0) + ((g.degree(coins[1]) == 0 && !isEdge(coins[1]))? 1 : 0);
		scores[player] += points;
		return points;
	}
	
	
	private boolean isEdge(int v)
	{
		return edges.contains(v); 
	}
	
	public int score(int player)
	{
		return scores[player];
	}
	public boolean areMovesLeft()
	{
		// returns whether or not there are any lines to be drawn
		// returns whether or not there are any strings left to cut.
		return (g.ecount() != 0);
	}
	public int countDoubleCrosses()
	{
		//A double cross is a connected component containing exactly 2 coins. That's it. Easy.
		DfsGraphTraversal dfs = new DfsGraphTraversal();
		List<List<Integer>> traversal = dfs.traverse(g);
		//traversal now contains all the connected components. Count the number of them with size() == 2.
		int count = 0;
		for(int i = 0 ;i < traversal.size(); i ++)
		{
			if (traversal.get(i).size() == 2)
			{
				count ++;
			}
		}
		return count;
	}
	
	//cycles are chains that circle back on themselves. They are also connected components.
	public int countCycles()
	{
		int count = 0;
		DfsGraphTraversal dfs = new DfsGraphTraversal();
		List<List<Integer>> trav = dfs.traverse(g);
		for(int i = 0; i < trav.size(); i ++)
		{
			if (isCycle(trav.get(i)))
			{
				count ++;
			}
		}
		
		return count;
	}
	
	private boolean isCycle(List<Integer> l)
	{
		//If it is a cycle, every g.degree(v) == 2.
		//Is there ANY other case where EVERY element has a degree of two?
		//No.
		boolean cycle = true;
		for(int i = 0 ; i < l.size() && cycle; i ++)
		{
			if (g.degree(l.get(i)) != 2)
			{
				cycle = false;
			}
		}
		return cycle;
	}
	
	//A chain is open iff both end vertices have a degree > 1.
	//How do we find the end vertices? Well, they're the only vertices without a degree of 2.
	//How do we avoid including cycles in our chains? We could just check if it isCycle.
	
	public int countOpenChains()
	{
		// returns the number of open chains on the board
		int count = 0;
		
		DfsGraphTraversal dfs = new DfsGraphTraversal();
		List<List<Integer>> traverse = dfs.traverse(g);
		List<Integer> alreadyGotChainsWith = new ArrayList<Integer>();
		for(List<Integer> l : traverse)
		{
			if (!isCycle(l))
			{
				for(int i = 0; i < l.size(); i ++)
				{
					if (g.degree(l.get(i)) == 2)
					{
						//if you haven't already grabbed the chain this is in...
						if (!alreadyGotChainsWith.contains(l.get(i)))
						{
							//At this point, we know it's not a cycle and that we don't already have it.
							//We can now find chain.
							List<Integer> thisChain = getChain(l.get(i));
							//It's only a true chain it it's 3 or more.
							if (thisChain.size() > 3)
							{
								//Not necessarily an open chain. Ensure all members of chain.degree > 1 || isEdge.
								
								boolean isOpen = true;
								for(int j = 0; j < thisChain.size() && isOpen; j ++)
								{
									if (g.degree(thisChain.get(j)) == 1 && !isEdge(thisChain.get(j)))
									{
										isOpen = false;
									}
								}
								for(int j = 0; j < thisChain.size(); j ++)
								{
									alreadyGotChainsWith.add(thisChain.get(j));
								}
								if (isOpen)
								{
									count ++;
									
								}
								
							}
						}
					}
				}
			}
		}
		return count;
	}
	
	
	private List<Integer> getChain(int v)
	{
		//returns a list containing all the verts involved in the chain containing v.
		List<Integer> list = new ArrayList<Integer>();
		chainHelper(list, v);
		return list;
	}
	
	private void chainHelper(List<Integer> l, int v)
	{
		if (g.degree(v) > 2)
		{
			//This coin is not involved in the chain.
			return;
		}
		else if (g.degree(v) == 1)
		{
			//It's either an edge on an open chain or a cap on a closed chain. Either way, add it.
			l.add(v);
		}
		else
		{
			//This coin IS involved in the chain.
			l.add(v);
			//We know v has two siblings.
			int first = g.first(v);
			int next = g.next(v, first);
			//For each of those, if it's not in l already, do chainHelper on it.
			if (!l.contains(first))
			{
				chainHelper(l, first);
			}
			if (!l.contains(next))
			{
				chainHelper(l, next);
			}
		}
	}
	
	
	public void drawMe()
	{
		//Draw the graph. Coins and strings. That's easier than drawing dots and boxes.
		//Each row will be three lines high, each col three spaces wide.
		//Let's do this with a char array. Much easier, and we don't care about costs for this.
		System.out.println();
		System.out.println();
		char[][] c = new char[3 * cols][3*rows];
		
		for(int i = 0 ; i < c.length; i ++)
		{
			for(int j = 0; j < c[i].length; j ++)
			{
				c[i][j] = ' ';
			}
		}
		for(int y = 0; y < rows; y ++)
		{
			for(int x = 0; x < cols; x ++)
			{
				int index = getIndex(y, x);
				int up = getIndex(y-1, x);
				int down = getIndex(y+1, x);
				int right = getIndex(y, x + 1);
				int left = getIndex(y, x -1);
				
				c[x*3][y*3] = 'X';
				
				if (y > 0)
				{
					//up.
					if (g.isEdge(index, up))
					{
						c[x*3][3 * y-1] = '-';
					}
				}
				if (y < rows - 1)
				{
					//down
					if (g.isEdge(index, down))
					{
						c[x*3][3*y+1] = '-';
					}
				}
				if (x > 0)
				{
					if (g.isEdge(index, left))
					{
						c[3*x-1][y*3] = '|';
					}
				}
				if (x < cols - 1)
				{
					if (g.isEdge(index, right))
					{
						c[3*x+1][y*3] = '|';
					}
				}
			}
		}
		
		for(int i = 0 ; i< c.length; i ++)
		{
			for(int j = 0; j < c[0].length; j ++)
			{
				System.out.print(c[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
