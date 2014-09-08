import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;


public class TopologicalSort {
	
	Integer[] topo;
	int index = 0;
	private Stack<Integer> s;
	public List<Integer> sort(Graph g)
	{
		topo = new Integer[g.vcount()];
		index = topo.length - 1;
		s = new Stack<Integer>();
		//clear marks.
		for(int i =0 ; i < g.vcount(); i ++)
		{
			g.setMark(i, 0);
		}

		//traverse the graph and add the vertices to topo[] on popping.
		boolean worked = dft(g);
		
		return Arrays.asList(topo);
		
	}
	
	private boolean dft(Graph g)
	{
		for(int i = 0; i < g.vcount(); i ++)
		{
			if (g.getMark(i) == 0)
			{
				if (!dfvt(g, i))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean dfvt(Graph g, int v)
	{
		System.out.println("Visit " + v);
		if (s.contains(v))
		{
			System.out.println("Broke on " + v);
			return false;
		}
		s.push(v);
		int cur = g.first(v);
		while(cur < g.vcount())
		{
			if (g.getMark(cur) == 0)
			{
				if (!dfvt(g, cur))
				{
					return false;
				}
			}
			cur = g.next(v, cur);
		}
		//This is where v is popped.
		topo[index--] = v;
		g.setMark(v, 1);
		s.pop();
		System.out.println("Worked on "  + v);
		return true;
	}
}
