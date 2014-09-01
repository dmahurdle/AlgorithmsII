import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class BfsGraphTraversal {
	public List<List<Integer>> traverse(Graph g)
	{
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		bfs(g, list);
		
		return list;
	}
	
	private void bfs(Graph g, List<List<Integer>> l)
	{
		for(int i = 0 ; i < g.vcount(); i ++)
		{
			g.setMark(i, 0);
		}
		for(int i = 0 ; i < g.vcount(); i ++)
		{
			
			if (g.getMark(i) == 0)
			{
				List<Integer> nl = new ArrayList<Integer>();
				nl.add(i);
				bfvs(g, i, nl);
				l.add(nl);
			}
		}
	}
	
	private void bfvs(Graph g, int v, List<Integer> l)
	{
		g.setMark(v, 1);
		Queue<Integer> q = new LinkedList<Integer>();
		
		q.add(v);
		while(q.size() > 0)
		{
			//for each adjacent vertex...
			int cur = g.first(q.peek());
			while (cur < g.vcount())
			{
				if (g.getMark(cur) == 0)
				{
					g.setMark(cur, 1);
					l.add(cur);
					q.add(cur);
				}
				cur = g.next(q.peek(), cur);
			}
			q.poll();
		}
	}
}
