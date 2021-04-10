package Item;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A reward worth 5 points that the player must collect all of to be able to win the game.
 */
public class RegularReward extends ScoreChanger
{
	public RegularReward(Point p, BufferedImage sprite, int value)
	{
		super(p, sprite, value);
	}
}
