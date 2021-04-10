package Game;

import Level.*;
import Menu.*;
import java.awt.*;
import javax.swing.*;

public class Game
{
	//Constants
	public final static int CELL_SIZE = 32;
	public final static int CELL_COUNT_WIDTH = 40;		//Width of the window in cells
	public final static int CELL_COUNT_HEIGHT = 24;		//Height of the window in cells

	public final static int SCREEN_WIDTH = CELL_COUNT_WIDTH * CELL_SIZE;		//Width of the window in pixels
	public final static int SCREEN_HEIGHT = CELL_COUNT_HEIGHT * CELL_SIZE;	//Height of the window in pixels
	public final static int FRAME_RATE = 30;		//Determines how long a frame is. 1000ms / FRAME_RATE = length of a frame in ms.

	public final static int LEVEL_X_OFFSET = 10 * CELL_SIZE;		//How far the game field is from the left side of the window
	public final static int LEVEL_Y_OFFSET = 2 * CELL_SIZE;		//How far the game field is from the top side of the window
	public final static int LEVEL_CELL_WIDTH = 28;					//Width of the game field in cells
	public final static int LEVEL_CELL_HEIGHT = 20;					//Height of the game field in cells

	public final static int STATS_X_OFFSET = 2 * CELL_SIZE;		//How far the stats panel is from the left side of the window
	public final static int STATS_Y_OFFSET = 2 * CELL_SIZE;		//How far the stats panel is from the top side of the window
	public final static int STATS_CELL_WIDTH = 6;					//Width of the stats panel in cells
	public final static int STATS_CELL_HEIGHT = 20;					//Height of the stats panel field in cells

	Level currentLevel = null;
	private static String levelToLoad = "";
	private static GameScreenState state;

	public Game()
	{
		StartMenu testMenu = new StartMenu();

		if(!testMenu.loadResources())
		{return;}

		GamePanel levelPanel = new GamePanel(SCREEN_WIDTH, SCREEN_HEIGHT);

		JFrame mainFrame = new JFrame();
		Container contentPane = mainFrame.getContentPane();	//Set the container of testFrame to contentPane
		CardLayout screenSwitch = new CardLayout();				//Lets you switch between menu and game screen

		contentPane.setLayout(screenSwitch);
		contentPane.add(testMenu, "start_menu");		//Add the start menu using the name "start_menu". Use this name to refer to it in the game loop
		contentPane.add(levelPanel, "in_game");			//Do the same as above

		/*	Order of making a window:
		 * 1. Make a JPanel (override its method getPreferredSize to set its size). Here, that JPanel is levelPanel.
		 * 2. Pass JPanel into JFrame's method setContentPane
		 * 3. Call JFrame's method pack. This will let the JFrame size itself based on size of its content
		 * See https://stackoverflow.com/a/13316131 for pack.
		 * See https://stackoverflow.com/a/13734319 on overloading getPreferredSize
		 */
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);	//Center the window when it comes up
		mainFrame.setResizable(false);		//If it is false, you cannot drag the border to resize the window.
		mainFrame.setVisible(true);
		mainFrame.setFocusable(true);

		//Main game loop
		while(true)
		{
			if(state == GameScreenState.START_MENU)
			{
				screenSwitch.show(contentPane, "start_menu");
			}
			else if(state == GameScreenState.LOADING_LEVEL)
			{
				if(!loadLevel(levelToLoad))
				{
					state = GameScreenState.START_MENU;
				}
				else
				{
					levelPanel.setCurrentLevel(currentLevel);
					levelPanel.add(currentLevel.getPlayerMover());
					mainFrame.addWindowListener(currentLevel.getWindowChecker());
					state = GameScreenState.IN_GAME;
				}
			}
			else if(state == GameScreenState.IN_GAME)
			{
				screenSwitch.show(contentPane, "in_game");
				levelPanel.doLevelLogic();
				levelPanel.updateStatsPanel();
			}
			else if(state == GameScreenState.RETURN_TO_MENU)
			{
				screenSwitch.show(contentPane, "in_game");
				levelPanel.remove(currentLevel.getPlayerMover());
				mainFrame.removeWindowListener(currentLevel.getWindowChecker());
				levelPanel.reset();
				screenSwitch.show(contentPane, "start_menu");
				state = GameScreenState.START_MENU;
			}
			else if(state == GameScreenState.RESTART_LEVEL)
			{
				screenSwitch.show(contentPane, "in_game");
				levelPanel.remove(currentLevel.getPlayerMover());
				mainFrame.removeWindowListener(currentLevel.getWindowChecker());
				levelPanel.reset();
				state = GameScreenState.LOADING_LEVEL;
			}
			else if(state == GameScreenState.EXIT_GAME)
			{
				levelPanel.reset();	//Close the level's SFX player
				System.exit(0);
			}

			//Call the drawing method THEN sleep. See below, the section "THE ANIMATION LOOP".
			//http://underpop.online.fr/j/java/help/images-d-graphics-and-animation-java.html.gz,
			mainFrame.repaint();

			try
			{
				Thread.sleep(1000/FRAME_RATE);
			}
			catch (InterruptedException e)
			{
				System.out.println("Warning: the main thread was interrupted");
				e.printStackTrace();
			}
		}
	}

	//Used by buttons to change the game state. Can exit game, choose level, restart level, etc.
	public static void setGameState(GameScreenState newState)
	{
		state = newState;
	}

	public static void setLevelToLoad(String levelFieldText)
	{
		levelToLoad = levelFieldText;
	}

	public boolean loadLevel(String levelFile)
	{
		//Levels are 28 * 20 cells large.
		boolean success = true;
		currentLevel = new Level();

		if(!currentLevel.loadLevel(levelFile))
		{
			success = false;
			System.out.println("Error: Failed to load level \"" + levelFile + "\"");
		}
		else
		{
			System.out.println("Successfully loaded level \"" + levelFile + "\"");
		}

		return success;
	}
}
