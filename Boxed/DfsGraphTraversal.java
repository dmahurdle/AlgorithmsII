import java.util.ArrayList;
import java.util.List;


public class DfsGraphTraversal {

	public List<List<Integer>> traverse(Graph g)
	{
		// returns all the connected components of a graph, listed in their order of visitation
		for(int i =0 ; i < g.vcount(); i ++)
		{
			g.setMark(i, 0);
		}
		
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		
		dft(g, list);
		return list;
	}
	
	private void dft(Graph g, List<List<Integer>> l)
	{
		for(int i = 0; i < g.vcount(); i ++)
		{
			if (g.getMark(i) == 0)
			{
				ArrayList<Integer> al = new ArrayList<Integer>();
				dfvt(g, i, al);
				if (al.size() > 0)
				{
					l.add(al);
				}
			}
		}
	}
	
	private void dfvt(Graph g, int v, List<Integer> l)
	{
		g.setMark(v, 1);
		if (!l.contains(v))
		{
			l.add(v);
		}
		int cur = g.first(v);
		while(cur < g.vcount())
		{
			//
			if (g.getMark(cur) == 0)
			{
				l.add(cur);
				dfvt(g, cur, l);
			}
			cur = g.next(v, cur);
		}
		
	}
}
