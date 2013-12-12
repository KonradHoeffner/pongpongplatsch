package de.pongpongplatsch;

import java.util.Random;
import org.eclipse.jdt.annotation.NonNull;

public class Map
{
	protected final int DEFAULT_ROWS = 10;
	protected final int DEFAULT_COLUMNS = 10;
	static protected Random r = new Random();

	@NonNull public final Cell[][] cells;

	public Map()
	{
		cells = new Cell[10][10];
	}

	static int nextGaussian(int left, int right)
	{		
		int mid = left+(right-left)/2;		
		return Math.max(Math.min((int)((r.nextGaussian()*(right-left)/8+mid)),right),0);
	}

	public static Map createRandomMap()
	{
		Map map = new Map();
		for(int i=0;i<50;i++)
		{
			map.cells[nextGaussian(0,map.cells.length-1)][nextGaussian(0,map.cells[0].length-1)] = Tiles.Standard.createCell();
		}
		return map;
	}
	
	public static Map createFullMap()
	{
		Map map = new Map();
		for(int x=0;x<map.cells[0].length;x++)
			for(int y=0;y<map.cells.length;y++)
			{
				map.cells[y][x]=Tiles.Standard.createCell();
			}
		return map;
	}
	
}