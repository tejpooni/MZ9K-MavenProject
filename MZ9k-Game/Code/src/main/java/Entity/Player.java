package Entity;

import Game.*;
import Tile.Tile;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.ArrayList;


public class Player extends Entity
{
	//This JLabel handles key bindings since they don't work for stuff that aren't in Swing or AWT.
	//JLabel sets its fields if keys are pressed, Player reads its fields to see what to do, and acts accordingly.
	private final PlayerMover myMover;
	private int turboMovementTick = 0;

	private boolean holdingUpKey = false;
	private boolean holdingDownKey = false;
	private boolean holdingLeftKey = false;
	private boolean holdingRightKey = false;
	private boolean holdingSpaceKey = false;

	//Used to make the player move only once if they are holding a key.
	private boolean movedUpOnce = false;
	private boolean movedDownOnce = false;
	private boolean movedLeftOnce = false;
	private boolean movedRightOnce = false;

	private Point oldPos;

	//Constructor
	public Player(Point p, BufferedImage sprite, PlayerMover mover)
	{
		super(p, sprite);
		oldPos = p;
		myMover = mover;
	}

	public void logicLoop(ArrayList<Tile> tiles)
	{
		checkKeyBindings();
		move();
		checkForWall(tiles);
	}

	//Look at pressed keys and move to a new position, but remember the old position in case you move into a wall.
	private void move()
	{
		oldPos = (Point) position.clone();

		//Turbo movement: press and hold space to constantly move from tile to tile.
		//You will only move every 6 ticks, so spamming a movement button is faster.
		if(holdingSpaceKey)
		{
			if(turboMovementTick % 6 == 0)
			{
				if (holdingUpKey)
				{
					position.y -= speed;
				}
				if (holdingDownKey)
				{
					position.y += speed;
				}
				if (holdingLeftKey)
				{
					position.x -= speed;
				}
				if (holdingRightKey)
				{
					position.x += speed;
				}
			}
			++turboMovementTick;
		}
		//Single movement: press and hold a key to move one tile. Holding does nothing.
		else
		{
			if (holdingUpKey && !movedUpOnce)
			{
				position.y -= speed;
				movedUpOnce = true;
			}
			if (holdingDownKey && !movedDownOnce)
			{
				position.y += speed;
				movedDownOnce = true;
			}
			if (holdingLeftKey && !movedLeftOnce)
			{
				position.x -= speed;
				movedLeftOnce = true;
			}
			if (holdingRightKey && !movedRightOnce)
			{
				position.x += speed;
				movedRightOnce = true;
			}
		}
	}

	private void checkKeyBindings()
	{
		if(myMover.getInputUp())
		{
			holdingUpKey = true;
		}
		else
		{
			holdingUpKey = false;
			movedUpOnce = false;
		}
		if(myMover.getInputDown())
		{
			holdingDownKey = true;
		}
		else
		{
			holdingDownKey = false;
			movedDownOnce = false;
		}
		if(myMover.getInputLeft())
		{
			holdingLeftKey = true;
		}
		else
		{
			holdingLeftKey = false;
			movedLeftOnce = false;
		}
		if(myMover.getInputRight())
		{
			holdingRightKey = true;
		}
		else
		{
			holdingRightKey = false;
			movedRightOnce = false;
		}
		if(myMover.getInputSpace())
		{
			holdingSpaceKey = true;
		}
		else
		{
			holdingSpaceKey = false;
			turboMovementTick = 0;
		}
	}

	//If the new position puts you into a wall, move to the old position.
	private void checkForWall(ArrayList<Tile> tiles)
	{
		for (Tile i: tiles)
		{
			//Player's position == Tile's position
			if (collide(i.getPosition()) && i.isSolid())
			{
				position = oldPos;		//Move the player's position back if there is a collision.
				break;						//There is a collision with a wall, so stop checking.
			}
		}
	}
}
