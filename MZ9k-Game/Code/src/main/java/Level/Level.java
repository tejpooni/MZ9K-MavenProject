package Level;
import Game.*;
import Item.BonusReward;
import Item.Punishment;
import Item.RegularReward;
import StillImage.StillImage;
import Tile.*;
import Entity.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The <code>Level</code> class contains all methods and fields necessary to load and play a level.
 * It has level loading, level element logic, and level rendering logic.
 */
public class Level
{
	//Level loading elements
	String pathToRead = "";
	LevelFileLoader levelFileLoader = new LevelFileLoader();
	ArrayList<String> levelParameters = new ArrayList<>();
	ArrayList<String> levelLayout = new ArrayList<>();

	//Level parameters to set
	private final int UNSET_CONSTANT = -1;	//Used by single Integer parameters to determine their success.
	private final int ERROR_CONSTANT = -2;

	int levelNumber = UNSET_CONSTANT;
	Direction startPointWallDir = Direction.UNSET_CONSTANT;
	Direction endPointWallDir = Direction.UNSET_CONSTANT;
	ArrayList<Integer> bonusAppearOnTickTimes = new ArrayList<>();
	ArrayList<Integer> bonusLifetimeTickTimes = new ArrayList<>();
	int maxTimeBonusScore = UNSET_CONSTANT;
	int ticksUntil0TimeBonus = UNSET_CONSTANT;

	//Level resources (images and sounds)
	ImageSet imageSet = null;
	SFXPlayer sfxPlayer = null;
	MusicPlayer musicPlayer = null;
	WindowAdapter windowChecker;

	//Level values/constants
	PlayerState playerState = PlayerState.ALIVE;
	long tick;
	int playerScore;
	int regularRewardsCollected;
	int bonusRewardsCollected;

	private final int regularRewardValue = 5;
	private final int bonusRewardValue = 15;
	private final int punishmentValue = -25;

	//Level elements
	StillImage statsBackground;
	StillImage gameBackground;

	Player playerCharacter;
	PlayerMover playerMover = new PlayerMover();
	StartCell startPoint;
	EndCell endPoint;

	private final int trackingEnemyMoveDelay = 8;
	private final int lr_udEnemyMoveDelay = 10;

	private int enemySoundTimer = Game.FRAME_RATE * 10;	//Don't play any enemy sounds for the first 10 seconds of the level.
	boolean trackingEnemyExists = false;
	boolean lrEnemyExists = false;
	boolean udEnemyExists = false;

	ArrayList<Tile> solidWalls = new ArrayList<>();
	ArrayList<Enemy> enemies = new ArrayList<>();
	ArrayList<RegularReward> regularRewards = new ArrayList<>();
	ArrayList<BonusReward> bonusRewards = new ArrayList<>();
	ArrayList<Punishment> punishments = new ArrayList<>();

	/**
	 * Initializes the <code>Level</code>'s <code>ArrayList</code>s and
	 * readies the <code>Level</code> for loading a new level.
	 */
	public Level()
	{
		windowChecker = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				super.windowClosing(e);
				closeMusic();
				closeSFX();
			}
		};
	}

	/**
	 * Contains all logic needed to load a map. It loads a file, reads and closes a file,
	 * splits the info it found into parameters and level layout, loads the level parameters, loads the level resources,
	 * and loads the layout. If any of these methods return false, the level loading is unsuccessful.
	 * @param levelFile the name of the level file to load
	 * @return <code>true</code> if the level was successfully loaded and no step failed along the way.
	 * <code>false</code> otherwise.
	 */
	public boolean loadLevel(String levelFile)
	{
		pathToRead = "levels/" + levelFile + ".txt";
		boolean success = false;

		if (levelFileLoader.loadLevelFile(pathToRead, levelParameters, levelLayout) && loadParameters() && loadResources() && loadLayout())
		{
			musicPlayer.start();
			success = true;
		}

		return success;
	}

	/**
	 * Iterates through the <code>ArrayList</code> that has the level parameters and tokenizes each element in the list.
	 * It then uses <code>assignParameter</code> to use the tokens it has found to configure the level.
	 * This method also checks it see if necessary level parameters, such as <code>startPointWallDir</code> and
	 * <code>endPointWallDirFound</code>, were present in the level file.
	 * @return <code>false</code> if <code>assignParameter</code> returned <code>false</code>, or if any parameter was
	 * not found and thus, not set. <code>true</code> otherwise.
	 * @see Level#assignParameter(ArrayList)
	 */
	private boolean loadParameters()
	{
		boolean success = true;
		for (String i : levelParameters)
		{
			ArrayList<String> parameterValues = new ArrayList<>(Arrays.asList(i.split(" ")));

			if(!assignParameter(parameterValues))
			{
				success = false;
			}
		}

		//Check if any level parameters were unset.
		if(levelNumber == UNSET_CONSTANT)
		{
			ErrorMessage.showError(ErrorType.loadParameter_unsetParam, pathToRead, "levelNumber");
			success = false;
		}
		if(startPointWallDir == Direction.UNSET_CONSTANT)
		{
			ErrorMessage.showError(ErrorType.loadParameter_unsetParam, pathToRead, "startPointWallDir");
			success = false;
		}
		if(endPointWallDir == Direction.UNSET_CONSTANT)
		{
			ErrorMessage.showError(ErrorType.loadParameter_unsetParam, pathToRead, "endPointWallDir");
			success = false;
		}
		if(bonusAppearOnTickTimes.isEmpty())
		{
			ErrorMessage.showError(ErrorType.loadParameter_unsetParam, pathToRead, "bonusRewardAppearOnTickTimes");
			success = false;
		}
		if(bonusLifetimeTickTimes.isEmpty())
		{
			ErrorMessage.showError(ErrorType.loadParameter_unsetParam, pathToRead, "bonusRewardLifetimeTickTimes");
			success = false;
		}
		if(ticksUntil0TimeBonus == UNSET_CONSTANT)
		{
			ErrorMessage.showError(ErrorType.loadParameter_unsetParam, pathToRead, "ticksUntil0TimeBonus");
			success = false;
		}
		if(maxTimeBonusScore == UNSET_CONSTANT)
		{
			ErrorMessage.showError(ErrorType.loadParameter_unsetParam, pathToRead, "maxTimeBonusScore");
			success = false;
		}

		return success;
	}

	/**
	 * Iterates through the <code>ArrayList</code> that contains the tokens for a level parameter and checks if it is
	 * a valid parameter. If it is, it sets the parameter for the level accordingly with its values.
	 * <p><code>startPointWallDir</code>: where the 3 <code>SolidWall</code> should appear beside the <code>StartCell</code>
	 * to prevent any <code>Entity</code> from going out of bounds. Valid values: <code>up</code>,
	 * <code>down</code>, <code>left</code>, <code>right</code> (place the walls beside <code>StartCell</code> in that
	 * direction, or <code>none</code> for no 3 <code>SolidWall</code>s.</p>
	 * <p><code>endPointWallDir</code>: Same functionality and values as <code>startPointWallDir</code>, but for the
	 * <code>EndCell</code> instead.</p>
	 * <p><code>bonusRewardAppearOnTickTimes</code>: the ticks that need to pass from the start of the level
	 * until a given <code>BonusReward</code> appears.</p>
	 * <p><code>bonusRewardLifetimeTickTimes</code>: the ticks that need to pass from the start of the level
	 * until a given <code>BonusReward</code> disappears permanently.</p>
	 * <p><code>ticksUntil0TimeBonus</code>: the ticks that need to pass until the player gets a 0 for their time bonus
	 * score.</p>
	 * <p><code>maxTimeBonusScore</code>: the maximum time bonus score awarded to the player</p>
	 * @param parameterTokens the list containing the parameter name and parameter values.
	 * @return <code>false</code> if any parameter has too many values, not enough values, or doesn't appear;
	 * or if an unknown parameter appears. <code>true</code> otherwise.
	 * @see Level#loadParameters()
	 */
	private boolean assignParameter(ArrayList<String> parameterTokens)
	{
		boolean success = true;

		//Which parameter are we going to set?
		switch (parameterTokens.get(0))
		{
			case "":	//Blank line
				break;
			case "levelNumber":
				levelNumber = setSingleIntegerParameter(parameterTokens, levelNumber);
				if(levelNumber == ERROR_CONSTANT)
				{success = false;}
				break;
			case "startPointWallDir":
				//Passing the parameter into the method lets us check if we are setting the parameter twice.
				startPointWallDir = setDirectionParameter(parameterTokens, startPointWallDir);
				if(startPointWallDir == Direction.ERROR_CONSTANT)
				{success = false;}
				break;
			case "endPointWallDir":
				endPointWallDir = setDirectionParameter(parameterTokens, endPointWallDir);
				if(endPointWallDir == Direction.ERROR_CONSTANT)
				{success = false;}
				break;
			case "bonusRewardAppearOnTickTimes":
				if(!setListIntegerParameter(parameterTokens, bonusAppearOnTickTimes))
				{success = false;}
				break;
			case "bonusRewardLifetimeTickTimes":
				if(!setListIntegerParameter(parameterTokens, bonusLifetimeTickTimes))
				{success = false;}
				break;
			case "ticksUntil0TimeBonus":
				ticksUntil0TimeBonus = setSingleIntegerParameter(parameterTokens, ticksUntil0TimeBonus);
				if(ticksUntil0TimeBonus == ERROR_CONSTANT)
				{success = false;}
				break;
			case "maxTimeBonusScore":
				maxTimeBonusScore = setSingleIntegerParameter(parameterTokens, maxTimeBonusScore);
				if(maxTimeBonusScore == ERROR_CONSTANT)
				{success = false;}
				break;
			default:
				ErrorMessage.showError(ErrorType.assignParameter_unknParamName, pathToRead, parameterTokens.get(0));
				success = false;
				break;
		}

		return success;
	}

	Direction setDirectionParameter(ArrayList<String> parameterTokens, Direction directionToCheck)
	{
		//Things that cause errors for Direction parameters (error precedence below):
		// Incorrect # of values > non-Direction value > second appearance of parameter

		Direction toReturn;
		final int correctNumOfValues = 2;

		//If an incorrect number of values is found, error.
		if(parameterTokens.size() != correctNumOfValues)
		{
			ErrorMessage.showError(ErrorType.assignParameter_uneqVal, pathToRead, parameterTokens.get(0), String.valueOf(correctNumOfValues-1), String.valueOf(parameterTokens.size()-1));
			return Direction.ERROR_CONSTANT;
		}
		else if (directionToCheck == Direction.UNSET_CONSTANT)
		{
			switch (parameterTokens.get(1))
			{
				case "up":
					toReturn = Direction.up;
					break;
				case "down":
					toReturn = Direction.down;
					break;
				case "left":
					toReturn = Direction.left;
					break;
				case "right":
					toReturn = Direction.right;
					break;
				case "none":
					toReturn = Direction.none;
					break;
				//If a non-Direction value is found, error.
				default:
					ErrorMessage.showError(ErrorType.assignParameter_unknVal, pathToRead, parameterTokens.get(1), parameterTokens.get(0));
					return Direction.ERROR_CONSTANT;
			}
		}
		//If the parameter appears more than once in the level file, error.
		else
		{
			ErrorMessage.showError(ErrorType.assignParameter_appearMulti, pathToRead, parameterTokens.get(0));
			return Direction.ERROR_CONSTANT;
		}

		return toReturn;
	}
	
	boolean setListIntegerParameter(ArrayList<String> parameterTokens, ArrayList<Integer> listToChange)
	{
		//Things that cause errors: no values for the parameter, non-number parameter, parameter <= 0, second appearance
		//Things that cause errors for ArrayList<Integer> parameters (error precedence below):
		// Less than 2 values > non-number value OR overflow value > negative value > second appearance of parameter
		
		boolean success = true;
		final int minNumberOfValues = 2;

		//If this is the first time the parameter is being set, the list is empty.
		if(listToChange.isEmpty())
		{
			String testNaNValue = "";	//Keep the value to add as a String so that if the value isn't an integer, we can display it in an error message.
			int integerToAdd;
			try
			{
				if(parameterTokens.size() < minNumberOfValues)
				{throw new IndexOutOfBoundsException();}

				for(int i = 1; i < parameterTokens.size(); i++)
				{
					testNaNValue = parameterTokens.get(i);
					integerToAdd = Integer.parseInt(parameterTokens.get(i));

					if(integerToAdd < 0)
					{throw new ArithmeticException();}
					else
					{listToChange.add(integerToAdd);}
				}
			}
			//If no value for the list parameter is found (just the name of parameter, no numbers after), error.
			catch (IndexOutOfBoundsException e)
			{
				ErrorMessage.showError(ErrorType.assignParameter_noValForList, pathToRead, parameterTokens.get(0));
				success = false;
			}
			//If a non-number value is found, or the value exceeds 2147483647, error. This is thrown by parseInt.
			catch (NumberFormatException e)
			{
				ErrorMessage.showError(ErrorType.assignParameter_unknVal, pathToRead, testNaNValue, parameterTokens.get(0));
				success = false;
			}
			//If a number with a value < 0 is found, error.
			catch (ArithmeticException e)
			{
				ErrorMessage.showError(ErrorType.assignParameter_negVal, pathToRead, parameterTokens.get(0));
				success = false;
			}
		}
		//If the parameter is being set a second time, the list will not be empty. Error.
		else
		{
			ErrorMessage.showError(ErrorType.assignParameter_appearMulti, pathToRead, parameterTokens.get(0));
			success = false;
		}

		//If there was an error and no elements could be added to the list, add the error constant.
		// This prevents the loadParameter_unsetParam error from showing up.
		if(!success && listToChange.isEmpty())
		{
			listToChange.add(ERROR_CONSTANT);
		}

		return success;
	}

	/**
	 * Sets the value of level parameters whose type is {@code Integer}.
	 *	<p>{@code Integer} parameters come in this format: {@literal <parameter_name> <parameter_value>}</p>
	 *	<p>The {@code Integer} parameters are {@code ticksUntil0TimeBonus} and {@code maxTimeBonusScore}.</p>
	 * @param parameterTokens
	 * @param valueToCheck
	 * @return -2 if an error occurred while trying to set the parameter.
	 */
	int setSingleIntegerParameter(ArrayList<String> parameterTokens, int valueToCheck)
	{
		//Things that cause errors for Integer parameters (error precedence below):
		// Incorrect # of values > non-number value OR overflow value > negative value > second appearance of parameter

		int toReturn;
		final int correctNumOfValues = 2;

		//If an incorrect number of values is found, error.
		if(parameterTokens.size() != correctNumOfValues)
		{
			ErrorMessage.showError(ErrorType.assignParameter_uneqVal, pathToRead, parameterTokens.get(0), String.valueOf(correctNumOfValues-1), String.valueOf(parameterTokens.size()-1));
			return ERROR_CONSTANT;
		}
		else if(valueToCheck == UNSET_CONSTANT)
		{
			String testNaNValue = "";
			try
			{
				testNaNValue = parameterTokens.get(1);
				toReturn = Integer.parseInt(parameterTokens.get(1));

				if(toReturn < 0)
				{throw new ArithmeticException();}
			}
			//If a non-number value is found, or the value exceeds 2147483647, error. This is thrown by parseInt.
			catch (NumberFormatException e)
			{
				ErrorMessage.showError(ErrorType.assignParameter_unknVal, pathToRead, testNaNValue, parameterTokens.get(0));
				return ERROR_CONSTANT;
			}
			//If a negative number value is found, error. This is thrown by the if statement.
			catch (ArithmeticException e)
			{
				ErrorMessage.showError(ErrorType.assignParameter_negVal, pathToRead, parameterTokens.get(0));
				return ERROR_CONSTANT;
			}
		}
		//If the parameter appears more than once in the level file, error.
		else
		{
			ErrorMessage.showError(ErrorType.assignParameter_appearMulti, pathToRead, parameterTokens.get(0));
			return ERROR_CONSTANT;
		}

		return toReturn;
	}

	/**
	 * Loads the resources used by the <code>Level</code>. Resources consist of sprites for game elements and images for
	 * backgrounds. Resources are of the type <code>BufferedImage</code>.
	 * @return <code>true</code> if all <code>BufferedImage</code>s were found and successfully loaded.
	 * <code>false</code> if an exception <code>NullPointerException</code> or <code>IOException</code> was thrown while
	 * trying to load the <code>BufferedImage</code>.
	 */
	private boolean loadResources()
	{
		//NOTE: If you rename a file to an incorrect name, try to load a level,
		//then rename it back to a correct name, you need to load a level twice.
		//After renaming it, the first level load will display an error message. The second load will not.

		imageSet = new ImageSet();
		musicPlayer = new MusicPlayer();
		sfxPlayer = new SFXPlayer();

		//Randomly choose music. Load only one piece from disk and keep it in memory.
		int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
		String musicChoice = "";
		switch (randomNum)
		{
			case 0: musicChoice = "Hidden Agenda"; break;
			case 1: musicChoice = "Monkeys Spinning Monkeys"; break;
			case 2: musicChoice = "Scheming Weasel faster"; break;
			case 3: musicChoice = "Sneaky Snitch"; break;
		}

		return imageSet.loadAssets() && musicPlayer.loadAssets(musicChoice) && sfxPlayer.loadAssets();
	}

	/**
	 * Iterates through the <code>ArrayList</code> that contains the level layout and sets the positions of
	 * <code>Placeable</code>. Also sets up their sprites and parameters. The level layout is a 28 x 20 box of characters in the text file.
	 * The layout beings when the line "layout" is found. No other line can appear after the 20-row layout.
	 * <p><code>#</code> for <code>SolidWall</code>, a wall that no <code>Entity</code> can move through.</p>
	 * <p><code>S</code> for <code>TrackingEnemy</code>, an enemy which follows the player.</p>
	 * <p><code>L</code> for <code>LeftRightEnemy</code>, an enemy that moves horizontally, switching direction of it finds a wall.</p>
	 * <p><code>U</code> for <code>UpDownEnemy</code>, an enemy that moves vertically, switching direction of it finds a wall.</p>
	 * <p><code>P</code> for <code>StartCell</code>, the place where the <code>Player</code> appears when the level starts.</p>
	 * <p><code>E</code> for <code>EndCell</code>, the place where the <code>Player</code> must go to after collecting all regular rewards to win the level.</p>
	 * <p><code>C</code> for <code>RegularReward</code>, a reward worth 5 points that all must be collected to win.</p>
	 * <p><code>G</code> for <code>BonusReward</code>, an optional reward worth 15 points that appears and disappears at given times.</p>
	 * <p><code>B</code> for <code>Punishment</code>, a bomb that takes away 25 points of the <code>Player</code> steps on it.</p>
	 * <p>An empty space,<code> </code>, for an empty cell.</p>
	 * @return <code>false</code> if the level layout isn't the right size or if lines appear after the layout;
	 * or if an unrecognized character appears in the layout. <code>true</code> otherwise.
	 * @see Level#makeWallForStartEndPoint
	 */
	private boolean loadLayout()
	{
		final boolean LOADING_FAILURE = false;
		final boolean LOADING_SUCCESS = true;

		if(!checkLevelLayoutDimensions())
		{
			return LOADING_FAILURE;
		}
		else
		{
			//Start parsing each String in levelRows. Set up positions of walls, enemies, player, etc.
			int currentRow = 0;	//This is the Y position of the character in the TXT file.
			int bonusRewardLifetimesIndex = 0;
			int bonusRewardAppearOnIndex = 0;

			for (String row: levelLayout)
			{
				//Analyze each character in a row to determine what element it is.
				int currentColumn = 0;	//This is the X position of the character in the TXT file.
				char[] it = row.toCharArray();

				for(char element : it)
				{
					Point elementPos = new Point(Game.LEVEL_X_OFFSET + currentColumn * Game.CELL_SIZE,Game.LEVEL_Y_OFFSET + currentRow * Game.CELL_SIZE);

					switch (element)
					{
						case '#':	//It is the most square-looking character in the ASCII char set, so it is a wall.
							solidWalls.add(new SolidWall(elementPos, imageSet.get("solid_wall"), true));
							break;
						case 'S':	//S for Snake, which is what the moving enemy is
							enemies.add(new TrackingEnemy(elementPos, imageSet.get("tracking_enemy"), trackingEnemyMoveDelay));
							trackingEnemyExists = true;
							break;
						case 'L':	//L for left-right enemy.
							enemies.add(new LeftRightEnemy(elementPos, imageSet.get("lr_enemy"), lr_udEnemyMoveDelay));
							lrEnemyExists = true;
							break;
						case 'U':	//L for up-down enemy.
							enemies.add(new UpDownEnemy(elementPos, imageSet.get("ud_enemy"), lr_udEnemyMoveDelay));
							udEnemyExists = true;
							break;
						case ' ':
							//Empty cell, do nothing.
							break;
						case 'P':	//P for Player
							if(startPoint == null)
							{
								startPoint = new StartCell(elementPos, imageSet.get("starting_point"), false);
								makeWallForStartEndPoint(elementPos, startPointWallDir);
								//Clone the point or else the start point will move with the player.
								playerCharacter = new Player((Point)elementPos.clone(), imageSet.get("player"), playerMover);
							}
							else
							{
								ErrorMessage.showError(ErrorType.Level_loadLayout_twoStartEnd, pathToRead, "start point");
								return LOADING_FAILURE;
							}
							break;
						case 'E':	//E for End cell
							if(endPoint == null)
							{
								endPoint = new EndCell(elementPos, imageSet.get("ending_point"), false);
								makeWallForStartEndPoint(elementPos, endPointWallDir);
							}
							else
							{
								ErrorMessage.showError(ErrorType.Level_loadLayout_twoStartEnd, pathToRead, "end point");
								return LOADING_FAILURE;
							}
							break;
						case 'C':	//C for Coin, which is what the regular reward is.
							regularRewards.add(new RegularReward(elementPos, imageSet.get("regular_reward"), regularRewardValue));
							break;
						case 'G':	//G for Gem, which is what the bonus reward is.
							bonusRewards.add(new BonusReward(elementPos, imageSet.get("bonus_reward"), bonusRewardValue,
									  bonusAppearOnTickTimes.get(bonusRewardAppearOnIndex),
									  bonusLifetimeTickTimes.get(bonusRewardLifetimesIndex)));
							//Increment the index of the list. The next reward will get the value at that new index.
							//If the index is out of bounds, set the index back to zero.
							bonusRewardAppearOnIndex = (++bonusRewardAppearOnIndex % bonusAppearOnTickTimes.size());
							bonusRewardLifetimesIndex = (++bonusRewardLifetimesIndex % bonusLifetimeTickTimes.size());
							break;
						case 'B':	//B for Bomb, which is what the punishment is.
							punishments.add(new Punishment(elementPos, imageSet.get("punishment"), punishmentValue));
							break;
						default:
							ErrorMessage.showError(ErrorType.Level_loadLayout_unknChar, pathToRead, String.valueOf(element),
									  String.valueOf(currentRow+1), String.valueOf(currentColumn+1));
							return LOADING_FAILURE;
					}

					++currentColumn;
				}

				++currentRow;
			}
		}

		statsBackground = new StillImage(new Point(0, 0), imageSet.get("stats_1"));
		gameBackground = new StillImage(new Point(Game.LEVEL_X_OFFSET, Game.LEVEL_Y_OFFSET), imageSet.get("grass"));

		return LOADING_SUCCESS;
	}

	private boolean checkLevelLayoutDimensions()
	{
		boolean success = true;

		//Check if the level has 20 lines since levels are 20 cells tall. If there aren't 20 lines, error.
		if(levelLayout.size() != Game.LEVEL_CELL_HEIGHT)
		{
			ErrorMessage.showError(ErrorType.Level_loadLayout_uneqRowNum, pathToRead,
					  String.valueOf(levelLayout.size()), String.valueOf(Game.LEVEL_CELL_HEIGHT));
			success = false;
		}
		else
		{
			//Check if each row has 28 characters since levels are 28 cells wide. If a row doesn't have 28 chars, error.
			for(int i = 0; i < levelLayout.size(); i++)
			{
				if(levelLayout.get(i).length() != Game.LEVEL_CELL_WIDTH)
				{
					ErrorMessage.showError(ErrorType.Level_loadLayout_uneqEleNum, pathToRead,
							  String.valueOf(levelLayout.get(i).length()), String.valueOf(i+1), String.valueOf(Game.LEVEL_CELL_WIDTH));
					success = false;
					break;
				}
			}
		}

		return success;
	}

	/**
	 * <code>StartCell</code> and <code>EndCell</code> mainly lie along the game barrier. Since the points are non-solid, they can be moved through.
	 * So, they require walls to keep <code>Entity</code> from going out of bounds. This method creates up to 3 <code>SolidWall</code>
	 * beside the <code>StartCell</code> and <code>EndCell</code> to keep things in.
	 * It uses the parameters <code>startPointWallDir</code> and <code>endPointWallDir</code> to determine where to place those walls for each cell.
	 * @param p the position of the <code>StartCell</code> or <code>EndCell</code>.
	 * @param directionForWalls If true, this method will place walls with respect to the <code>StartCell</code>. NEED WORK
	 * If <code>false</code>, this method will place walls with respect to the <code>EndCell</code>.
	 */
	private void makeWallForStartEndPoint(Point p, Direction directionForWalls)
	{
		switch (directionForWalls)
		{
			case up:
				//	# # #
				//	  S
				solidWalls.add(new SolidWall(new Point(p.x - Game.CELL_SIZE, p.y - Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				solidWalls.add(new SolidWall(new Point(p.x, p.y - Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				solidWalls.add(new SolidWall(new Point(p.x + Game.CELL_SIZE, p.y - Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				break;
			case down:
				//   S
				//	# # #
				solidWalls.add(new SolidWall(new Point(p.x - Game.CELL_SIZE, p.y + Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				solidWalls.add(new SolidWall(new Point(p.x, p.y + Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				solidWalls.add(new SolidWall(new Point(p.x + Game.CELL_SIZE, p.y + Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				break;
			case left:
				// #
				// # S
				// #
				solidWalls.add(new SolidWall(new Point(p.x - Game.CELL_SIZE, p.y - Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				solidWalls.add(new SolidWall(new Point(p.x - Game.CELL_SIZE, p.y), imageSet.get("solid_wall"), true));
				solidWalls.add(new SolidWall(new Point(p.x - Game.CELL_SIZE, p.y + Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				break;
			case right:
				//   #
				// S #
				//   #
				solidWalls.add(new SolidWall(new Point(p.x + Game.CELL_SIZE, p.y - Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				solidWalls.add(new SolidWall(new Point(p.x + Game.CELL_SIZE, p.y), imageSet.get("solid_wall"), true));
				solidWalls.add(new SolidWall(new Point(p.x + Game.CELL_SIZE, p.y + Game.CELL_SIZE), imageSet.get("solid_wall"), true));
				break;
			case none:
				//Make no wall. Do nothing
				break;
		}
	}

	/**
	 * Renders all the sprites and images that a map uses.
	 * @param g the <code>Graphics</code> object to render to
	 */
	public void renderLoop(Graphics g)
	{
		statsBackground.render(g);
		gameBackground.render(g);
		startPoint.render(g);
		endPoint.render(g);

		for(Tile i : solidWalls)
		{i.render(g);}

		for(RegularReward i : regularRewards)
		{
			if(!i.isDisappeared())
			{i.render(g);}
		}

		for(BonusReward i : bonusRewards)
		{
			if(i.shouldIAppear(tick) && !i.isDisappeared())
			{i.render(g);}
		}

		for(Punishment i : punishments)
		{
			if(!i.isDisappeared())
			{i.render(g);}
		}

		playerCharacter.render(g);

		for(Enemy i : enemies)
		{i.render(g);}
	}

	/**
	 * Handles the logic for game logic. Does game ticks, <code>Player</code> movement, enemy movement, collisions between
	 * <code>Entity</code> and <code>SolidWall</code> + <code>Item</code>, collisions between <code>Player</code> and
	 * enemies, and <code>BonusReward</code> spawning and de-spawning.
	 * Also handles the player state; if they are dead, alive, or have won.
	 */
	public void logicLoop()
	{
		//Player logic
		playerCharacter.logicLoop(solidWalls);

		//Enemy logic
		for(Enemy i : enemies)
		{
			i.logicLoop(playerCharacter.getPosition(), solidWalls, tick);
			if(i.collide(playerCharacter.getPosition()))
			{
				sfxPlayer.play("death");
				playerState = PlayerState.DEAD;
			}
		}

		//Regular reward logic
		for(RegularReward i : regularRewards)
		{
			if(!i.isDisappeared())
			{
				i.logicLoop(playerCharacter.getPosition());

				if(i.isCollected())
				{
					sfxPlayer.play("regular_collect");
					playerScore += i.getPointsValue();
					++regularRewardsCollected;
					i.setDisappeared(true);
				}
			}
		}

		//Bonus reward logic
		for(BonusReward i : bonusRewards)
		{
			if(i.shouldIAppear(tick) && !i.isDisappeared())
			{
				i.logicLoop(playerCharacter.getPosition());

				if (i.isCollected())
				{
					sfxPlayer.play("bonus_collect");
					playerScore += i.getPointsValue();
					++bonusRewardsCollected;
					i.setDisappeared(true);
				}
			}

			if(tick == i.getAppearOnTick())
			{sfxPlayer.play("bonus_appear");}
			if(tick == i.getDisappearAfterTick() && !i.isDisappeared())
			{sfxPlayer.play("bonus_expire");}
		}

		//Punishment logic
		for(Punishment i : punishments)
		{
			if(!i.isDisappeared())
			{
				i.logicLoop(playerCharacter.getPosition());

				if(i.isCollected())
				{
					sfxPlayer.play("punishment");
					playerScore += i.getPointsValue();
					i.setDisappeared(true);
				}

				//If player score goes below 0, player is dead!
				if(playerScore < 0)
				{
					playerState = PlayerState.DEAD;
					sfxPlayer.play("death");
				}
			}
		}

		//End point logic
		if(regularRewardsCollected == regularRewards.size() && endPoint.collide(playerCharacter.getPosition()) && playerState != PlayerState.DEAD)
		{
			sfxPlayer.play("victory");
			playerState = PlayerState.VICTORY;
		}

		playEnemySound();

		//Stop the music
		if(playerState != PlayerState.ALIVE)
		{
			closeMusic();
		}

		++tick;
	}

	/**
	 * Returns the player mover, a <code>JLabel</code> that uses <code>InputMap</code> and <code>ActionMap</code> to
	 * handle player movement. Called in the main game loop so the <code>levelPanel JPanel</code> can add the player mover.
	 * @return The player mover
	 * @see PlayerMover
	 */
	public PlayerMover getPlayerMover()
	{
		return playerMover;
	}

	/**
	 * Returns the player state, which the <code>levelPanel</code> and <code>statsPanel</code> use to determine which
	 * screen/info they should display.
	 * @return The player state
	 * @see PlayerState
	 */
	public PlayerState getPlayerState()
	{
		return playerState;
	}

	/**
	 * Returns level statistics. These statistics are used by {@code StatsPanel} to update the values it displays.
	 * <p>The stats returned are the level tick, level number, player score, number of bonus rewards collected,
	 * number of bonus rewards total, and the time bonus.</p>
	 * <p>The time bonus is calculated using the formula: </p>
	 * <p>time_bonus = maxTimeBonusScore * (ticksUntil0TimeBonus - tick) / ticksUntil0TimeBonus.</p>
	 * <p>If {@code ticksUntil0TimeBonus} is set to 0 from the level parameter {@code ticksUntil0TimeBonus}, this method sets it to 1 to prevent division by 0.
	 * If the formula calculates a negative number, the time bonus returned is 0.</p>
	 * @return A {@code StatsStruct} containing level statistics.
	 */
	public StatsStruct getStats()
	{
		//Prevent a division by 0 error.
		if(ticksUntil0TimeBonus == 0)
		{ticksUntil0TimeBonus = 1;}

		long timeBonus = maxTimeBonusScore * (ticksUntil0TimeBonus - tick) / ticksUntil0TimeBonus;
		if(timeBonus <= 0)
		{timeBonus = 0;}

		return new StatsStruct(tick, levelNumber, playerScore, bonusRewardsCollected, bonusRewards.size(), timeBonus);
	}

	public WindowAdapter getWindowChecker()
	{
		return windowChecker;
	}

	private void playEnemySound()
	{
		if(enemySoundTimer != 0)
		{
			--enemySoundTimer;
		}
		else
		{
			//Play a sound...
			int randomSound = ThreadLocalRandom.current().nextInt(0, 3);

			if(trackingEnemyExists && randomSound == 0)
			{sfxPlayer.play("snake");}
			else if(lrEnemyExists && randomSound == 1)
			{sfxPlayer.play("rat");}
			else if(udEnemyExists && randomSound == 2)
			{sfxPlayer.play("bat");}

			//then wait 10 to 24 seconds before playing the another sound.
			enemySoundTimer = ThreadLocalRandom.current().nextInt(Game.FRAME_RATE * 10, Game.FRAME_RATE * 24);
		}
	}

	private void closeMusic()
	{
		if(musicPlayer != null)
		{
			musicPlayer.stopMusic();

			try
			{musicPlayer.join();}
			catch (InterruptedException e)
			{
				ErrorMessage.showError(ErrorType.Level_closeMusic_failThreadJoin);
				e.printStackTrace();
			}
		}
	}

	public void closeSFX()
	{
		if(sfxPlayer != null)
		{sfxPlayer.close();}
	}
}
