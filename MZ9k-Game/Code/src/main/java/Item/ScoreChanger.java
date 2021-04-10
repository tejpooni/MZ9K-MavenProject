package Item;

import Placeable.Placeable;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The parent class for all rewards and punishments.
 */
abstract public class ScoreChanger extends Placeable
{
	protected int pointsValue;
	protected boolean collected = false;
	protected boolean disappeared = false;

	/**
	 * Sets the position, sprite, and the points value of <code>this</code>.
	 * @param p Position where the object will spawn at when the level starts
	 * @param sprite Sprite for the object to use
	 * @param value The value, in points, of the <code>ScoreChanger</code>.
	 * @see Placeable#Placeable
	 */
	public ScoreChanger(Point p, BufferedImage sprite, int value)
	{
		super(p, sprite);
		this.pointsValue = value;
	}

	/**
	 * Checks if the player's position overlaps with the position of <code>this</code>. If there is an overlap, the
	 * field <code>collected</code> is set to true.
	 * @param playerPos The player's position
	 */
	public void logicLoop(Point playerPos)
	{
		if(checkIfPointsOverlap(position, playerPos))
		{
			collected = true;
		}
	}

	/**
	 * Returns the <code>pointsValue</code> of <code>this</code>, which <code>Level</code> uses to change the player's score.
	 * @return The <code>pointsValue</code> of <code>this</code>
	 */
	public int getPointsValue()
	{
		return pointsValue;
	}

	/**
	 * Returns the value of <code>collected</code> of <code>this</code>, which <code>Level</code> uses to check if <code>this</code>
	 * should change the player score and disappear after.
	 * @return the value of <code>collected</code>
	 */
	public boolean isCollected()
	{
		return collected;
	}

	/**
	 * Returns the value of <code>disappeared</code> of <code>this</code>, which <code>Level</code> uses to check if <code>this</code>
	 * should be rendered and have its collision and timing logic (for <code>BonusReward</code> only) done.
	 * @return the value of <code>collected</code>
	 */
	public boolean isDisappeared()
	{
		return disappeared;
	}

	/**
	 * Sets the value of <code>disappeared</code> of <code>this</code>, which <code>Level</code> uses to stop rendering
	 * and logic of <code>this</code>.
	 * @param disappeared The new value of <code>disappeared</code>
	 */
	public void setDisappeared(boolean disappeared)
	{
		this.disappeared = disappeared;
	}
}
