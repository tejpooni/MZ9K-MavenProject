package Menu;

import Game.*;

import java.awt.*;
import javax.swing.*;

//This is almost an exact duplicate of WinScreen.
/**
 * The <code>LoseScreen</code> appears if a player dies. Death occurs if the player's score becomes negative due
 * to stepping on a <code>Punishment</code>, or if the <code>Player</code> comes into contact with a
 * <code>TrackingEnemy</code>, <code>LeftRightEnemy</code>, or an <code>UpDownEnemy</code>.
 */
public class LoseScreen extends JPanel
{
	JLabel deadLabel;
	JButton playAgainButton;
	JButton returnButton;
	JButton exitButton;

	Font buttonFont;
	Font labelFont;

	/**
	 * Sets up the losing screen's <code>JPanel</code>, <code>JLabel</code>, and 3 <code>JButton</code>s.
	 * <p>The <code>JPanel</code> holds all of the elements and has a red background.</p>
	 * <p>The <code>JLabel</code> tells the player that they lost.</p>
	 * <p>The 3 <code>JButtons</code> let the player retry the level, return to the main menu, or exit the game.</p>
	 */
	public LoseScreen()
	{
		super(new FlowLayout(FlowLayout.CENTER, Game.CELL_SIZE, (int) (Game.CELL_SIZE * 3.5)));

		//Set up the JPanel
		this.setBackground(new Color(255, 73, 73, 112));
		this.setVisible(false);

		//Set up the font
		labelFont = new Font("Arial", Font.BOLD | Font.PLAIN, 72);//font
		buttonFont = new Font("Arial", Font.BOLD | Font.PLAIN, 20);//font

		//Set up the dead label
		deadLabel = new JLabel("You died!", SwingConstants.CENTER);
		deadLabel.setForeground(Color.white);
		deadLabel.setPreferredSize(new Dimension(Game.LEVEL_CELL_WIDTH * Game.CELL_SIZE, Game.CELL_SIZE * 5));
		deadLabel.setFont(labelFont);
		deadLabel.setVisible(true);
		this.add(deadLabel);

		//Set up the playAgainButton
		playAgainButton = new JButton("Try again");
		playAgainButton.setBackground(new Color(238, 240, 242));
		playAgainButton.setPreferredSize(new Dimension(Game.CELL_SIZE * 6, Game.CELL_SIZE * 3));
		playAgainButton.setFont(buttonFont);
		playAgainButton.setVisible(true);
		playAgainButton.addActionListener(e -> Game.setGameState(GameScreenState.RESTART_LEVEL));
		this.add(playAgainButton);

		//Set up the returnButton.
		returnButton = new JButton("<html>Return to<br>menu</html>");
		returnButton.setHorizontalAlignment(SwingConstants.CENTER);
		returnButton.setBackground(new Color(238, 240, 242));
		returnButton.setPreferredSize(new Dimension(Game.CELL_SIZE * 6, Game.CELL_SIZE * 3));
		returnButton.setFont(buttonFont);
		returnButton.setVisible(true);
		returnButton.addActionListener(e -> Game.setGameState(GameScreenState.RETURN_TO_MENU));
		this.add(returnButton);

		//Set up the exitButton
		exitButton = new JButton("Exit game");
		exitButton.setBackground(new Color(238, 240, 242));
		exitButton.setPreferredSize(new Dimension(Game.CELL_SIZE * 6, Game.CELL_SIZE * 3));
		exitButton.setFont(buttonFont);
		exitButton.setVisible(true);
		exitButton.addActionListener(e -> Game.setGameState(GameScreenState.EXIT_GAME));
		this.add(exitButton);
	}
}
