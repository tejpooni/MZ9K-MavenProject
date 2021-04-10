package Entity;

import Game.Game;
import Tile.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackingEnemy extends Enemy
{
	public TrackingEnemy(Point p, BufferedImage sprite, int moveDelay)
	{
		super(p, sprite, moveDelay);
	}

	public void logicLoop(Point playerPos, ArrayList<Tile> tiles, long tick)
	{
		if(tick % moveDelay == 0)
		{
			List<Point> path = search(playerPos, tiles);

			//Move function
			if (path.size() > 1)
			{
				this.position = path.get(1);
			}
		}
	}

	private ArrayList<Point> search(Point playerPos, ArrayList<Tile> tiles)
	{
		//<CurrentPoint, PreviousPoint>
		ArrayList<Map<Point, Point>> mapList = new ArrayList<>();
		Map<Point, Point> map = new HashMap<>();
		map.put(this.position, null);
		mapList.add(map);

		Point found = null;
		boolean hasMoreCells = true;

		while (found == null && hasMoreCells)
		{
			Map<Point, Point> map0 = mapList.get(mapList.size() - 1);
			map = new HashMap<>();
			mapList.add(map);

			hasMoreCells = false;
			for (Point p0 : map0.keySet())
			{
				Point[] points = new Point[]{
						  new Point(p0.x + speed, p0.y),
						  new Point(p0.x - speed, p0.y),
						  new Point(p0.x, p0.y + speed),
						  new Point(p0.x, p0.y - speed),
				};
				for (Point p : points)
				{
					//if reach visited block
					if (mapList.stream().flatMap(m -> m.keySet().stream()).anyMatch(m -> m.equals(p)))
					{
						continue;
					}

					//if reach walls
					if (tiles.stream().anyMatch(m -> checkIfPointsOverlap(p, m.getPosition()) && m.isSolid()))
					{
						continue;
					}

					hasMoreCells = true;
					map.put(p, p0);
					//if reach player
					if (checkIfPointsOverlap(p, playerPos))
					{
						found = p;
					}
				}
			}
		}

		ArrayList<Point> results = new ArrayList<>();
		if (found != null)
		{
			results.add(found);
			for (int i = mapList.size() - 1; i >= 0; i--)
			{
				found = mapList.get(i).get(found);
				if (found != null)
				{
					results.add(0, found);
				}
			}
		}

		return results;
	}
}
