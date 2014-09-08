import java.awt.Color;

import edu.neumont.ui.Picture;

public class SeamCarver {
	Picture pic;
		public SeamCarver(Picture pic)
		{
			this.pic = pic;
		}
		Picture getPicture ()
		{
			// get the current image
			return pic;
		}
		int width()
		{
			return pic.width();
		}
		int height()
		{
			return pic.height();
		}
		
		int wrap(int value, int max)
		{
			//max is the width or height of the image, making it exclusive.
			//This is constant, so it won't handle numbers more than max * 2 or less than - max.
			//Not a problem for our uses here.
			if (value < 0)
			{
				return value + max;
			}
			else if (value >= max)
			{
				return value - max;
			}
			else 
			{
				return value;
			}
		}
		
		double energy(int x, int y)
		{
			// the energy of a pixel at (x,y)
			Color cUp = pic.get(x, wrap(y-1, height()));
			Color cDown = pic.get(x, wrap(y+1, height()));
			Color cLeft = pic.get(wrap(x-1, width()), y);
			Color cRight = pic.get(wrap(x+1, width()), y);
			
			int rx = cLeft.getRed() - cRight.getRed();			
			int gx = cLeft.getGreen() - cRight.getGreen();			
			int bx = cLeft.getBlue() - cRight.getBlue();
			
			int ry = cUp.getRed() - cDown.getRed();			
			int gy = cUp.getGreen() - cDown.getGreen();			
			int by = cUp.getBlue() - cDown.getBlue();
			
			int xEnergy = rx * rx + gx * gx + bx * bx;
			int yEnergy = ry * ry + gy * gy + by * by;
			
			return xEnergy + yEnergy;
		}
		
		int[] findVerticalSeam()
		{
			//This uses a VERY fake topo sort.
			
			//Here's the deal: When finding a horizontal seam, we're moving from left to right.
			//This means that the pixels in transposed natural order (ie, first column, second column, third column, ...)
			//is already topologically sorted.
			//Meaning we can do a VERY VERY fast shortest path algorithm on it all without any sorting overhead. It's already sorted.
			//It also means we don't need a queue as we would with Djkstra's.
			
			TopoPixel[][] map = new TopoPixel[width()][height()];
			//Generate first row of Topo pixels. We'll generate the rest as we go.
			for(int i = 0; i < width(); i ++)
			{
				map[i][0] = new TopoPixel(energy(i, 0), -1, 0);
			}
			

			for (int y = 0; y < height()-1; y ++)
			{
				for(int x = 0 ; x < width(); x ++)
				{
					//The two for loops represent the topological sort. For each one, there will be a constant number of operations.
					//Three at most.
					//That's the O(WH).
					
					//Upper.
					if (x > 0)
					{
						if (map[x-1][y+1] == null)
						{
							map[x-1][y+1] = new TopoPixel(energy(x-1, y+1), x, map[x][y].costFromSource);
						}
						else
						{
							//compare them.
							if (map[x-1][y+1].parentCost > map[x][y].costFromSource)
							{
								//change it.
								map[x-1][y+1].parentCost = map[x][y].costFromSource;
								map[x-1][y+1].costFromSource = map[x][y].costFromSource + map[x-1][y+1].energy;
								map[x-1][y+1].parent = x;
							}
						}
					}
					//Center (will always happen)
					if (map[x][y+1] == null)
					{
						map[x][y+1] = new TopoPixel(energy(x, y+1), x, map[x][y].costFromSource);
					}
					else
					{
						//compare them.
						if (map[x][y+1].parentCost > map[x][y].costFromSource)
						{
							//change it.
							map[x][y+1].parentCost = map[x][y].costFromSource;
							map[x][y+1].costFromSource = map[x][y].costFromSource + map[x][y+1].energy;
							map[x][y+1].parent = x;
						}
					}
					//Lower.
					if (x < width() - 1)
					{
						if (map[x+1][y+1] == null)
						{
							map[x+1][y+1] = new TopoPixel(energy(x+1, y+1), x, map[x][y].costFromSource);
						}
						else
						{
							//compare them.
							if (map[x+1][y+1].parentCost > map[x][y].costFromSource)
							{
								//change it.
								map[x+1][y+1].parentCost = map[x][y].costFromSource;
								map[x+1][y+1].costFromSource = map[x][y].costFromSource + map[x+1][y+1].energy;
								map[x+1][y+1].parent = x;
							}
						}
					}
				}
			}
			
			//So after that, all the TopoPixels have costs from the source (which include their own energies) and parent pointers all the way home.
			//So we go through the last column to find the lowest costFromSource.
			
			TopoPixel cheapest = map[0][height() - 1];
			int cheapX = 0;
			for(int i =1 ; i < width(); i ++)
			{
				//System.out.println(i + " : " + map[width() -1][i].costFromSource);
				if (map[i][height()-1].costFromSource < cheapest.costFromSource)
				{
					cheapest = map[i][height()-1];
					cheapX = i;
				}
			}
			//Now we follow the parent pointers and add them into an int.
			int[] vertSeam = new int[height()];
			TopoPixel cur = cheapest;
			vertSeam[vertSeam.length - 1] = cheapX;
			for(int i = 1 ; i < vertSeam.length; i ++)
			{
				cur = map[cur.parent][height()-i-1];
				vertSeam[height() - i - 1] = cur.parent;
			}
			return vertSeam;
		}
		
		private static class TopoPixel
		{
			double costFromSource;
			double energy = -1;
			double parentCost;
			int parent; ///This is either the x or the y of the parent of this pixel depending on the direction of seam carving. -1 if no parent.
			public TopoPixel(double energy, int parent, double parentCost)
			{
				this.parent = parent;
				this.energy = energy;
				this.parentCost = parentCost; //To speed up comparisons.
				this.costFromSource = energy + parentCost;
				this.parent = parent;
			}
			
			
		}
		
		public int[] findHorizontalSeam()
		{
			//This uses a VERY fake topo sort.
			
			//Here's the deal: When finding a horizontal seam, we're moving from left to right.
			//This means that the pixels in transposed natural order (ie, first column, second column, third column, ...)
			//is already topologically sorted.
			//Meaning we can do a VERY VERY fast shortest path algorithm on it all without any sorting overhead. It's already sorted.
			//It also means we don't need a queue as we would with Djkstra's.
			
			TopoPixel[][] map = new TopoPixel[width()][height()];
			//Generate first row of Topo pixels. We'll generate the rest as we go.
			for(int i = 0; i < height(); i ++)
			{
				map[0][i] = new TopoPixel(energy(0, i), -1, 0);
			}
			
			for(int x = 0 ; x < width() - 1; x ++)
			{
				for (int y = 0; y < height(); y ++)
				{
					//The two for loops represent the topological sort. For each one, there will be a constant number of operations.
					//Three at most.
					//That's the O(WH).
					
					//Upper.
					if (y > 0)
					{
						if (map[x+1][y-1] == null)
						{
							map[x+1][y-1] = new TopoPixel(energy(x+1, y-1), y, map[x][y].costFromSource);
						}
						else
						{
							//compare them.
							if (map[x+1][y-1].parentCost > map[x][y].costFromSource)
							{
								//change it.
								map[x+1][y-1].parentCost = map[x][y].costFromSource;
								map[x+1][y-1].costFromSource = map[x][y].costFromSource + map[x+1][y-1].energy;
								map[x+1][y-1].parent = y;
							}
						}
					}
					//Center (will always happen)
					if (map[x+1][y] == null)
					{
						map[x+1][y] = new TopoPixel(energy(x+1, y), y, map[x][y].costFromSource);
					}
					else
					{
						//compare them.
						if (map[x+1][y].parentCost > map[x][y].costFromSource)
						{
							//change it.
							map[x+1][y].parentCost = map[x][y].costFromSource;
							map[x+1][y].costFromSource = map[x][y].costFromSource + map[x+1][y].energy;
							map[x+1][y].parent = y;
						}
					}
					//Lower.
					if (y < height() - 1)
					{
						if (map[x+1][y+1] == null)
						{
							map[x+1][y+1] = new TopoPixel(energy(x+1, y+1), y, map[x][y].costFromSource);
						}
						else
						{
							//compare them.
							if (map[x+1][y+1].parentCost > map[x][y].costFromSource)
							{
								//change it.
								map[x+1][y+1].parentCost = map[x][y].costFromSource;
								map[x+1][y+1].costFromSource = map[x][y].costFromSource + map[x+1][y+1].energy;
								map[x+1][y+1].parent = y;
							}
						}
					}
						
				}
			}
			
			//So after that, all the TopoPixels have costs from the source (which include their own energies) and parent pointers all the way home.
			//So we go through the last column to find the lowest costFromSource.
			
			TopoPixel cheapest = map[width() - 1][0];
			int cheapY = 0;
			for(int i =1 ; i < height(); i ++)
			{
				//System.out.println(i + " : " + map[width() -1][i].costFromSource);
				if (map[width() -1][i].costFromSource < cheapest.costFromSource)
				{
					cheapest = map[width() -1][i];
					cheapY = i;
				}
			}
			//Now we follow the parent pointers and add them into an int.
			int[] horSeam = new int[width()];
			TopoPixel cur = cheapest;
			horSeam[horSeam.length - 1] = cheapY;
			for(int i = 1 ; i < horSeam.length; i ++)
			{
				cur = map[width() - i - 1][cur.parent];
				horSeam[width() - i - 1] = cur.parent;
			}
			return horSeam;
		}
		
		
		
		void removeHorizontalSeam(int[] indices)
		{
			//So this will copy the original into a new array, skipping the Y values that = indicies[x].
			Picture carved = new Picture(width(), height()-1);
			
			int currenty = 0;
			for(int x = 0; x < width()-1; x ++)
			{
				for(int y = 0 ; y < height(); y ++)
				{
					if (y != indices[x+1])
					{
						carved.set(x, currenty, pic.get(x, y));
						currenty ++;
					}
				}
				currenty = 0;
			}
			this.pic = carved;
		}
		
		
		void removeVerticalSeam(int[] indices)
		{
			Picture carved = new Picture(width()-1, height());
			
			int currentx = 0;
			for(int y = 0; y < height() - 1; y ++)
			{
				for(int x = 0 ; x < width(); x ++)
				{
					if (x != indices[y+1])
					{
						carved.set(currentx, y, pic.get(x, y));
						currentx ++;
					}
				}
				currentx = 0;
			}
			this.pic = carved;
		}
		
		void save(String name)
		{
			this.pic.save(name);
		}
	}