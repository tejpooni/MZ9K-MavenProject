package Entity;
import Game.Game;
import Placeable.Placeable;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class Entity extends Placeable
{
	protected final int speed = Game.CELL_SIZE;

	public Entity(Point p, BufferedImage sprite)
	{
		super(p, sprite);
	}
}
