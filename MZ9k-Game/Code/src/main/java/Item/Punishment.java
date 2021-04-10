package Item;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A dangerous punishment that, if the player steps on, takes away 25 points. If the score becomes negative after points
 * are taken away, the player dies.
 */
public class Punishment extends ScoreChanger
{
	public Punishment(Point p, BufferedImage sprite, int value)
	{
		super(p, sprite, value);
	}
}
