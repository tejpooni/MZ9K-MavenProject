package Tile;
import Placeable.Placeable;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The <code>Tile</code> class is the parent class for non-dynamic level elements.
 * It has a field, <code>solid</code>, that determines if the <code>Tile</code> is can be moved through.
 */
abstract public class Tile extends Placeable
{
	boolean solid;

	public Tile(Point p, BufferedImage sprite, boolean solid)
	{
		super(p, sprite);
		this.solid = solid;
	}

	public boolean isSolid()
	{
		return solid;
	}
}
