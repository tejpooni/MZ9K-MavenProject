package Placeable;


import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * <code>Placeable</code> is the top-level parent class that all level elements use. If an element will be placed on a level, it must be a <code>Placeable</code>.
 */
abstract public class Placeable
{
	protected Point position;
	protected BufferedImage sprite;

	/**
	 * Create a new object to place on the map.
	 * @param p Position where the object will spawn at when the level starts
	 * @param sprite Sprite for the object to use
	 */
	public Placeable(Point p, BufferedImage sprite)
	{
		this.position = p;
		this.sprite = sprite;
	}

	/**
	 * Returns the position of <code>this Placeable</code>.
	 * @return the position of <code>this Placeable</code>.
	 */
	public Point getPosition()
	{
		return position;
	}

	/**
	 * Renders the sprite of <code>this Placeable</code> at its current position.
	 * @param g The <code>Graphics</code> object to render to
	 */
	public void render(Graphics g)
	{
		g.drawImage(sprite, position.x, position.y, null);
	}

	/**
	 * Checks if the positions of two <code>Placeable</code> overlap.
	 * @param myPos First position to check
	 * @param otherPos Second position to check
	 * @return <code>true</code> if there is an overlap. <code>false</code> otherwise.
	 */
	public boolean checkIfPointsOverlap(Point myPos, Point otherPos)
	{
		//The only coordinate that matters for Placeables (if using rough movement) is their top-left corner.
		//If the position of my top-left corner is equal to the other's, then there is an overlap.
		return myPos.x == otherPos.x && myPos.y == otherPos.y;
	}

	/**
	 * Checks if the position of another <code>Placeable</code> overlaps with <code>this</code>.
	 * @param otherPos Position of the other <code>Placeable</code> to check
	 * @return <code>true</code> if there is an overlap. <code>false</code> otherwise.
	 */
	public boolean collide(Point otherPos)
	{
		//The only coordinate that matters for Placeables (if using rough movement) is their top-left corner.
		//If the position of my top-left corner is equal to the other's, then there is an overlap.
		return position.x == otherPos.x && position.y == otherPos.y;
	}

}