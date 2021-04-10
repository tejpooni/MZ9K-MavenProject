package Game;
import Menu.*;
import Level.Level;
import Level.PlayerState;

import javax.swing.*;
import java.awt.*;

//A JPanel is a container that stores components.
public class GamePanel extends JPanel
{
	private final int panelWidth;
	private final int panelHeight;

	private Level currentLevel;

	WinScreen winScreen;
	LoseScreen loseScreen;

	boolean updateStatsOnceMore = false;
	StatsPanel statsPanel;

	Font buttonFont;

	public GamePanel(int panelWidth, int panelHeight)
	{
		//null layout means use absolute positioning of components, rather than something like BoxLayout.
		super(null);
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;

		buttonFont = new Font("Monospaced", Font.PLAIN, 20);

		this.addWinScreen();
		this.addLoseScreen();
		this.addStatsPanel();
	}

	public void setCurrentLevel(Level level)
	{
		currentLevel = level;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(panelWidth, panelHeight);
	}

	//repaint() in the main game loop calls this function. ONLY use it to paint things. Don't use it for game logic.
	//See https://stackoverflow.com/a/34830679
	@Override
	public void paintComponent(Graphics g)
	{
		currentLevel.renderLoop(g);
	}

	//In here, do your game logic. The game loop in Game does this.
	public void doLevelLogic()
	{
		if (currentLevel.getPlayerState() == PlayerState.ALIVE)
		{
			currentLevel.logicLoop();
		}
		else if (currentLevel.getPlayerState() == PlayerState.DEAD)
		{
			loseScreen.setVisible(true);
		}
		else if (currentLevel.getPlayerState() == PlayerState.VICTORY)
		{
			winScreen.setVisible(true);
		}
	}

	private void addWinScreen()
	{
		//Don't need to construct JPanel with a layout manager if you won't add components to it.
		winScreen = new WinScreen();
		winScreen.setBounds(Game.LEVEL_X_OFFSET, Game.LEVEL_Y_OFFSET,
				  Game.LEVEL_CELL_WIDTH * Game.CELL_SIZE, Game.LEVEL_CELL_HEIGHT * Game.CELL_SIZE);

		winScreen.setVisible(false);
		this.add(winScreen);
	}

	private void addLoseScreen()
	{
		loseScreen = new LoseScreen();
		loseScreen.setBounds(Game.LEVEL_X_OFFSET, Game.LEVEL_Y_OFFSET,
				  Game.LEVEL_CELL_WIDTH * Game.CELL_SIZE, Game.LEVEL_CELL_HEIGHT * Game.CELL_SIZE);

		loseScreen.setVisible(false);
		this.add(loseScreen);
	}

	private void addStatsPanel()
	{
		statsPanel = new StatsPanel();
		statsPanel.setBounds(Game.STATS_X_OFFSET, Game.STATS_Y_OFFSET,
				  Game.STATS_CELL_WIDTH * Game.CELL_SIZE, Game.STATS_CELL_HEIGHT * Game.CELL_SIZE);

		statsPanel.setVisible(true);
		this.add(statsPanel);
	}

	public void updateStatsPanel()
	{
		if (currentLevel.getPlayerState() == PlayerState.ALIVE)
		{
			statsPanel.updateStats(currentLevel.getStats());
		}
		else if(!updateStatsOnceMore)
		{
			//If the player has won or is dead, stop updating the stats panel. This stops the flickering.
			statsPanel.setPlayerState(currentLevel.getPlayerState());

			//Update the stats panel one more time.
			//If the player has won, this last update can let it can show the time bonus and final score.
			updateStatsOnceMore = true;
			statsPanel.updateStats(currentLevel.getStats());
		}
	}

	//Resets the visibility of the pause button, win screen, and lose screen. Also closes the level's SFX player.
	public void reset()
	{
		updateStatsOnceMore = false;
		winScreen.setVisible(false);
		loseScreen.setVisible(false);
		currentLevel.closeSFX();
		statsPanel.reset();
	}
}
