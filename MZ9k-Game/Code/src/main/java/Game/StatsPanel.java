package Game;

import Level.PlayerState;
import Level.StatsStruct;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel
{
	//InputStream customFont;

	Font labelFont;
	Color labelRegularColor;
	Color labelWinColor;
	Color labelLoseColor;

	JLabel levelNumLabel;
	JLabel timeLabel;
	JLabel scoreLabel;
	JLabel bonusRewardLabel;
	JLabel timeBonusLabel;
	JLabel finalScoreLabel;

	String timeDisplay;
	int currentTime;
	private PlayerState playerState;

	StatsPanel()
	{
		//6 rows, 1 column
		super(new GridLayout(6, 1));


		//Set up the JPanel
		this.setBackground(new Color(0, 0, 0, 153));
		this.setVisible(true);

		//Set up font and color
		labelFont = new Font("Arial", Font.BOLD | Font.PLAIN, 32);//font
		labelRegularColor = new Color(0, 255, 255);
		labelWinColor = new Color(0, 255, 85);
		labelLoseColor = new Color(255, 91, 91);

		//1st row: level number
		levelNumLabel = new JLabel("<html>Level:<br>-</html>", SwingConstants.CENTER);
		levelNumLabel.setForeground(labelRegularColor);
		levelNumLabel.setFont(labelFont);
		levelNumLabel.setVisible(true);
		this.add(levelNumLabel);

		//2nd row: time
		timeLabel = new JLabel("<html>Time:<br>--:--</html>", SwingConstants.CENTER);
		timeLabel.setForeground(labelRegularColor);
		timeLabel.setFont(labelFont);
		timeLabel.setVisible(true);
		this.add(timeLabel);

		//3rd row: score
		scoreLabel = new JLabel("<html>Score:<br>0</html>", SwingConstants.CENTER);
		scoreLabel.setForeground(labelRegularColor);
		scoreLabel.setFont(labelFont);
		scoreLabel.setVisible(true);
		this.add(scoreLabel);

		//4th row: bonuses
		bonusRewardLabel = new JLabel("<html>Bonuses:<br>0/?</html>", SwingConstants.CENTER);
		bonusRewardLabel.setForeground(labelRegularColor);
		bonusRewardLabel.setFont(labelFont);
		bonusRewardLabel.setVisible(true);
		this.add(bonusRewardLabel);

		//5th row: time bonus
		timeBonusLabel = new JLabel("TEST5:", SwingConstants.CENTER);
		timeBonusLabel.setForeground(labelRegularColor);
		timeBonusLabel.setFont(labelFont);
		timeBonusLabel.setVisible(false);
		this.add(timeBonusLabel);

		//6th row: final score
		finalScoreLabel = new JLabel("TEST6:", SwingConstants.CENTER);
		finalScoreLabel.setForeground(labelRegularColor);
		finalScoreLabel.setFont(labelFont);
		finalScoreLabel.setVisible(true);
		//this.add(finalScoreLabel);
	}

	//There's an order to how you update the JLabels. https://stackoverflow.com/a/25124469
	void updateStats(StatsStruct stats)
	{
		this.removeAll();

		if(playerState == PlayerState.VICTORY)
		{changeLabelColor(labelWinColor);}
		else if(playerState == PlayerState.DEAD)
		{changeLabelColor(labelLoseColor);}

		//Update level number. Really shouldn't be updating it every frame but no better solution has come to mind!
		levelNumLabel.setText("<html>Level:<br>" + stats.levelNum + "</html>");
		this.add(levelNumLabel);

		//Update timer. Show time in minutes and seconds like this: https://stackoverflow.com/a/54117599
		if(stats.tick % Game.FRAME_RATE == 0)
		{
			if(stats.tick != 0)
			{
				++currentTime;
			}

			timeDisplay = String.format("%02d:%02d", currentTime / 60, currentTime % 60);
			timeLabel.setText("<html>Time:<br>" + timeDisplay + "</html>");
		}
		this.add(timeLabel);

		//Update score.
		scoreLabel.setText("<html>Score:<br>" + stats.score + "</html>");
		this.add(scoreLabel);

		//Update bonus rewards collected counter.
		bonusRewardLabel.setText("<html>Bonus:<br>" + stats.bonusCollected + "/" + stats.bonusTotal + "</html>");
		this.add(bonusRewardLabel);

		if(playerState == PlayerState.VICTORY)
		{
			//Show the time bonus if the player wins.
			timeBonusLabel.setText("<html>Time bonus:<br>+" + stats.timeBonus + "</html>");
			timeBonusLabel.setVisible(true);
			this.add(timeBonusLabel);

			//Show the final score (score from items + time bonus) if the player wins.
			finalScoreLabel.setText("<html>Final score:<br>" + (stats.score + stats.timeBonus) + "</html>");
			finalScoreLabel.setVisible(true);
			this.add(finalScoreLabel);
		}

		//Don't call repaint() down here since it already happens in the Game game loop.
		//Apparently you don't need to call revalidate either. This is app-triggered painting.
		//See https://www.oracle.com/java/technologies/painting.html#callback:~:text=The%20program%20determines
		//this.revalidate();
		//this.repaint();
	}

	void reset()
	{
		this.removeAll();

		changeLabelColor(labelRegularColor);

		//Reset level number. Really shouldn't be updating it every frame but no better solution has come to mind!
		levelNumLabel.setText("<html>Level:<br>-</html>");
		this.add(levelNumLabel);

		//Reset timer.
		currentTime = 0;
		timeLabel.setText("<html>Time:<br>00:00</html>");
		this.add(timeLabel);

		//Reset time bonus label.
		playerState = PlayerState.ALIVE;
		timeBonusLabel.setVisible(false);
		this.add(timeBonusLabel);

		//Reset final score label.
		finalScoreLabel.setVisible(false);
		this.add(finalScoreLabel);


		this.revalidate();
	}

	//Use a PlayerState to determine if extra stats should show.
	public void setPlayerState(PlayerState state)
	{
		this.playerState = state;
	}

	public void changeLabelColor(Color newColor)
	{
		levelNumLabel.setForeground(newColor);
		timeLabel.setForeground(newColor);
		scoreLabel.setForeground(newColor);
		bonusRewardLabel.setForeground(newColor);
		timeBonusLabel.setForeground(newColor);
		finalScoreLabel.setForeground(newColor);
	}
}
