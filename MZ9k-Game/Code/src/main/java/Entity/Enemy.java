package Entity;

import Tile.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Enemy extends Entity
{
	//An enemy will move every moveDelay ticks. The game runs at 30 FPS. Ex. If moveDelay = 10, enemy moves 3.75 tiles/second.
	protected final int moveDelay;

	public Enemy(Point p, BufferedImage sprite, int moveDelay)
	{
		super(p, sprite);
		this.moveDelay = moveDelay;
	}

	abstract public void logicLoop(Point playerPos, ArrayList<Tile> tiles, long tick);
}
