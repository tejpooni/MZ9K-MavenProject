package Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The <code>EndCell</code> is the place on the level where the player must stand on after they collect all coins to
 * finish and win the level.
 * <p>There can only be one on each level. It is non-solid so the player can move through it.</p>
 * <p>If it is placed along the outer map barrier, it requires 3 extra <code>SolidWall</code> to prevent the player from moving out of bounds.
 * Configure where these 3 walls go using the parameter <code>endPointWallDir</code> in the level file.
 * Use the values <code>up</code>, <code>down</code>, <code>left</code>, and <code>right</code> to place walls
 * above, below, to the left, or to the right of the <code>EndCell</code>.</p>
 * <p>If it is not placed on the outer map barrier, use the value <code>none</code> so no walls spawn beside it.</p>
 */
public class EndCell extends Tile
{
	public EndCell(Point p, BufferedImage sprite, boolean solid)
	{
		super(p, sprite, solid);
	}
}
