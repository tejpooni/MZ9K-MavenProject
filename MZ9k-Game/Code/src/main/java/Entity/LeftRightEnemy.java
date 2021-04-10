package Entity;

import Game.Game;
import Tile.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class LeftRightEnemy extends Enemy
{
	boolean movingLeft;

	public LeftRightEnemy(Point p, BufferedImage sprite, int moveDelay)
	{
		super(p, sprite, moveDelay);

		int randomNum = ThreadLocalRandom.current().nextInt(0, 100);

		//Randomly set the direction the enemy will go.
		movingLeft = (randomNum % 2 == 0);
	}

	public void logicLoop(Point playerPos, ArrayList<Tile> tiles, long tick)
	{
		if(tick % moveDelay == 0)
		{
			//Update position
			if(movingLeft)
			{position.x -= speed;}
			else
			{position.x += speed;}

			for(Tile i : tiles)
			{
				//If there is a wall...
				if(collide(i.getPosition()) && i.isSolid())
				{
					//Return to the previous position
					if(movingLeft)
					{position.x += speed;}
					else
					{position.x -= speed;}

					//Turn around
					movingLeft = !movingLeft;
				}
			}
		}
	}
}
