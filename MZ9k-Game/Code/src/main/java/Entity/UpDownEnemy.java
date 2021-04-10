package Entity;

import Game.Game;
import Tile.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class UpDownEnemy extends Enemy
{
	boolean movingUp;

	public UpDownEnemy(Point p, BufferedImage sprite, int moveDelay)
	{
		super(p, sprite, moveDelay);

		int randomNum = ThreadLocalRandom.current().nextInt(0, 100);

		//Randomly set the direction the enemy will go.
		movingUp = (randomNum % 2 == 0);
	}

	public void logicLoop(Point playerPos, ArrayList<Tile> tiles, long tick)
	{
		if(tick % moveDelay == 0)
		{
			//Update position
			if(movingUp)
			{position.y -= speed;}
			else
			{position.y += speed;}

			for(Tile i : tiles)
			{
				//If there is a wall...
				if(collide(i.getPosition()) && i.isSolid())
				{
					//Return to the previous position
					if(movingUp)
					{position.y += speed;}
					else
					{position.y -= speed;}

					//Turn around
					movingUp = !movingUp;
				}
			}
		}
	}
}
