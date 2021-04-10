package Menu;

import Game.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * The <code>StartMenu</code> appears every time the game is launched or when the player returns to the main menu from a level.
 * <p>From here, the player can choose a level to play or close the game.</p>
 */
public class StartMenu extends JPanel
{
	private final Font levelSelectFieldFont;

	private final JLabel title;
	private final JButton playButton;
	private final JButton creditsButton;
	private final JButton exitButton;
	private JTextField levelSelectField;

	private BufferedImage background;

	/**
	 * Sets up the <code>StartMenu</code>'s <code>JPanel</code>, <code>JLabel</code>, 2 <code>JButtons</code>, and <code>JTextField</code>.
	 * <p>The <code>JPanel</code> contains all of the menu's elements.</p>
	 * <p>The <code>JLabel</code> is the title of the game.</p>
	 * <p>The <code>JButton</code>s let the player play a level and close the game.</p>
	 * <p>The <code>JTextField</code> lets the player type in a number to play the level that corresponds to a level.
	 * This is the only way to choose a level.</p>
	 */
	public StartMenu()
	{
		super(null);			//Call constructor of JPanel

		//Set up fonts.
		//List of fonts: https://alvinalexander.com/blog/post/jfc-swing/swing-faq-list-fonts-current-platform/
		Font titleFont = new Font("Impact", Font.PLAIN, 72);
		Font buttonFont = new Font("Arial", Font.BOLD | Font.PLAIN, 40);
		Font smallButtonFont = new Font("Arial", Font.BOLD | Font.PLAIN, 16);
		levelSelectFieldFont = new Font("Arial", Font.BOLD | Font.PLAIN, 16);

		//Set up title
		title = new JLabel("MAZE GAME 9000", SwingConstants.CENTER);
		title.setFont(titleFont);
		title.setBackground(new Color(0, 0, 0, 114));
		title.setBounds(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT/2);
		title.setForeground(Color.YELLOW);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);	//Align x-axis of label to center of screen
		this.add(title);

		//Set up play button
		playButton = new JButton("Play level");
		playButton.setFont(buttonFont);
		playButton.setBackground(Color.YELLOW);
		playButton.setSize(Game.CELL_SIZE * 8, Game.CELL_SIZE * 3);
		playButton.setBounds((Game.SCREEN_WIDTH/2) - playButton.getWidth()/2, (int) (Game.SCREEN_HEIGHT/2.5), Game.CELL_SIZE * 8, Game.CELL_SIZE * 3);
		playButton.addActionListener(e ->
		{
			Game.setLevelToLoad(levelSelectField.getText());
			Game.setGameState(GameScreenState.LOADING_LEVEL);
		});
		this.add(playButton);

		//Set up the level selection field
		setUpLevelSelectField();

		//Set up exit button
		exitButton = new JButton("Exit");
		exitButton.setFont(buttonFont);
		exitButton.setBackground(Color.LIGHT_GRAY);
		exitButton.setSize(Game.CELL_SIZE * 7, (int) (Game.CELL_SIZE * 2.5));
		exitButton.setBounds((Game.SCREEN_WIDTH/2) - exitButton.getWidth()/2, (int) (Game.SCREEN_HEIGHT/1.5) , Game.CELL_SIZE * 7, (int) (Game.CELL_SIZE * 2.5));
		exitButton.addActionListener(e -> System.exit(0));
		this.add(exitButton);

		//Set up credits button
		creditsButton = new JButton("Credits");
		creditsButton.setFont(smallButtonFont);
		creditsButton.setBackground(Color.LIGHT_GRAY);
		creditsButton.setSize(Game.CELL_SIZE * 3, Game.CELL_SIZE);
		creditsButton.setLocation(Game.SCREEN_WIDTH/2 - creditsButton.getWidth()/2,(int) (Game.SCREEN_HEIGHT * 0.85));
		JLabel creditsList = new JLabel("<html><div style='text-align: center;'>" + "MAZE GAME 9000 was created by:<br>Jay Reyes<br>Tej Singh Pooni<br>Pranav Sawhney<br>Bob Lu" + "</div></html>");
		creditsList.setHorizontalAlignment(SwingConstants.CENTER);
		creditsList.setVerticalAlignment(SwingConstants.CENTER);
		creditsButton.addActionListener(e -> JOptionPane.showMessageDialog(null, creditsList, "Credits", JOptionPane.PLAIN_MESSAGE));
		this.add(creditsButton);
	}

	/**
	 * Renders the <code>StartMenu</code>'s background.
	 * @param g The <code>Graphics</code> object to render to.
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		g.drawImage(background, 0, 0, null);
	}

	/**
	 * Initializes and sets up the <code>JTextField</code> that asks for input and chooses which level to load.
	 */
	private void setUpLevelSelectField()
	{
		levelSelectField = new JTextField();
		levelSelectField.setHorizontalAlignment(JTextField.CENTER);
		levelSelectField.setSize(Game.CELL_SIZE * 6, Game.CELL_SIZE * 2);
		levelSelectField.setBounds((Game.SCREEN_WIDTH/2) - levelSelectField.getWidth()/2, (int) (Game.SCREEN_HEIGHT/2.5) + playButton.getHeight(), Game.CELL_SIZE * 6, (int) (Game.CELL_SIZE * 1.5));
		levelSelectField.setFont(levelSelectFieldFont);

		this.add(levelSelectField);
	}

	/**
	 * Load the resources used by the <code>StartMenu</code>.
	 * <code>StartMenu</code> only has 1 resource, which is its background image.
	 * Resources are of the type <code>BufferedImage</code>.
	 * @return <code>true</code> if the <code>BufferedImage</code> was found and successfully loaded.
	 * <code>false</code> if an exception <code>NullPointerException</code> or <code>IOException</code> was thrown while
	 * trying to load the <code>BufferedImage</code>.
	 */
	public boolean loadResources()
	{
		String filePath = "start_menu_bg.jpg";
		boolean success = true;

		try
		{
			background = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(filePath)));
		}
		catch(NullPointerException e)
		{
			ErrorMessage.showError(ErrorType.StartMenu_loadResources_NullPtrEx, "\n\t" + filePath);
			success = false;
		}
		catch (IOException e)
		{
			ErrorMessage.showError(ErrorType.StartMenu_loadResources_IOEx, filePath);
			e.printStackTrace();
			success = false;
		}

		return success;
	}
}