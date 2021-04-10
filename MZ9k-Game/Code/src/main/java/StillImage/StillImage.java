package StillImage;
import Placeable.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A <code>StillImage</code> is an image that doesn't do anything.
 * The only thing it does is render; no game logic uses it.
 * It is used for backgrounds.
 */
public class StillImage extends Placeable
{
	public StillImage(Point p, BufferedImage sprite)
	{
		super(p, sprite);
	}
}
