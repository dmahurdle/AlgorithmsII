
public class Graph {

	private int[][] matrix;
	private int edgeCount;
	private int[] mark;
	public Graph(int v)
	{
		// initializes a graph of v vertices with no edges
		mark = new int[v];
		matrix = new int[v][v];
		edgeCount = 0;
	}
	int vcount()
	{
		return mark.length;
	}
	int ecount()
	{
		return edgeCount;
	}
	int first(int v)
	{
		// returns the first vertex (in natural order) connected to vertex v.  If there are none, then vcount() is returned
		for(int i =0 ; i < matrix.length; i ++)
		{
			if (matrix[v][i] != 0)
			{
				return i;
			}
		}
		return mark.length;
	}
	int next(int v, int w)
	{
		// returns the vertex (in natural order) connected to vertex v after vertex w.  If there are no more edges after w, vcount() is returned
		for(int i = w + 1; i < matrix.length; i ++)
		{
			if (matrix[v][i] != 0)
			{
				return i;
			}
		}
		return mark.length;
	}
	void addEdge(int v, int w, int wt)
	{
		// adds an edge between vertex v and vertex w.
		if (matrix[v][w] == 0)
		{
			edgeCount++;
		}
		matrix[v][w] = wt;
	}
	void removeEdge(int v, int w)
	{
		// removes edge between vertex v and vertex w.
		if (matrix[v][w] != 0)
		{
			edgeCount --;
		}
		matrix[v][w] = 0;
	}
	
	void add2Edge(int x, int w, int wt)
	{
		addEdge(x, w, wt);
		addEdge(w, x, wt);
	}
	void remove2Edge(int x, int w)
	{
		removeEdge(x, w);
		removeEdge(w, x);
	}
	boolean isEdge(int v, int w)
	{
		// returns whether there is a connection between vertex v and vertex w
		return (matrix[v][w] != 0);
	}
	int degree(int v)
	{
		// returns how many edges depart from vertex v
		int count = 0;
		for (int i = 0; i < matrix[v].length; i ++)
		{
			if (matrix[v][i] != 0)
			{
				count++;
			}
		}
		return count;
	}
	int getMark(int v)
	{
		// returns any graph coloring for this vertex
		return (mark[v]);
	}
	void setMark(int v, int m)
	{
		// colors vertex v color m
		mark[v] = m;
	}

}
