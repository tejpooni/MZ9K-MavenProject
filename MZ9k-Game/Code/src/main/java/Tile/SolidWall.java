package Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A <code>SolidWall</code> is a <code>Placeable</code> that no <code>Entity</code> can move through.
 * They must be placed on the edge the map to prevent any <code>Entity</code> from moving out of bounds.
 * They can also be placed on the inside of the map to make moving around more difficult for the player.
 */
public class SolidWall extends Tile
{
	public SolidWall(Point p, BufferedImage sprite, boolean solid)
	{
		super(p, sprite, solid);
	}
}
