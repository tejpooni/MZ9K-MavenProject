package Level;

public class StatsStruct
{
	public final long tick;
	public final int levelNum;
	public final int score;
	public final int bonusCollected;
	public final int bonusTotal;
	public final long timeBonus;

	StatsStruct(long tick, int levelNum, int score, int bonusCollected, int bonusTotal, long timeBonus)
	{
		this.tick = tick;
		this.levelNum = levelNum;
		this.score = score;
		this.bonusCollected = bonusCollected;
		this.bonusTotal = bonusTotal;
		this.timeBonus = timeBonus;
	}
}
