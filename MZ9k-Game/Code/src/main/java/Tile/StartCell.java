package Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The position of the <code>StartCell</code> is where the player spawns when the level starts.
 * <p>There can only be one on each level. It is non-solid so the player can move through it.</p>
 * <p>If it is placed along the outer map barrier, it requires 3 extra <code>SolidWall</code> to prevent the player from moving out of bounds.
 * Configure where these 3 walls go using the parameter <code>endPointWallDir</code> in the level file.
 * Use the values <code>up</code>, <code>down</code>, <code>left</code>, and <code>right</code> to place walls
 * above, below, to the left, or to the right of the <code>EndCell</code>.</p>
 * <p>If it is not placed on the outer map barrier, use the value <code>none</code> so no walls spawn beside it.</p>
 */
public class StartCell extends Tile
{
	public StartCell(Point p, BufferedImage sprite, boolean solid)
	{
		super(p, sprite, solid);
	}
}
