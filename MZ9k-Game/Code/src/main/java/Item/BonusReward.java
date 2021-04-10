package Item;

import Placeable.Placeable;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A reward worth 15 points that appears and disappears at given times. Its appear and disappear times are based on
 * the level parameters <code>bonusRewardAppearOnTickTimes</code> and <code>bonusRewardLifetimeTickTimes</code>.
 */
public class BonusReward extends ScoreChanger
{
	private final int appearOnTick;
	private final int disappearAfterTick;

	/**
	 * Sets the position, sprite, points value, appear on tick value, and lifetime tick value of <code>this</code>.
	 * @param p Position where the object will spawn at when the level starts
	 * @param sprite Sprite for the object to use
	 * @param value The value, in points, of the <code>ScoreChanger</code>.
	 * @param appearOnTick The time, in ticks, that <code>this</code> must wait before it appears.
	 * @param lifetimeTick The time, in ticks, that <code>this</code> will exist for after it appears. Once the ticks have passed, <code>this</code> disappears permanently.
	 * @see ScoreChanger#ScoreChanger
	 */
	public BonusReward(Point p, BufferedImage sprite, int value, int appearOnTick, int lifetimeTick)
	{
		super(p, sprite, value);
		this.appearOnTick = appearOnTick;
		this.disappearAfterTick = appearOnTick + lifetimeTick;
	}

	/**
	 *
	 * @param levelTicks How many ticks have passed in the level
	 * @return <code>true</code> if the value of <code>levelTicks</code> is between
	 * <code>appearOnTick</code> and <code>appearOnTick + lifetimeTick</code>. <code>false</code> otherwise
	 */
	public boolean shouldIAppear(long levelTicks)
	{
		return levelTicks >= appearOnTick && levelTicks < disappearAfterTick;
	}

	public int getAppearOnTick()
	{
		return appearOnTick;
	}

	public int getDisappearAfterTick()
	{
		return disappearAfterTick;
	}
}
