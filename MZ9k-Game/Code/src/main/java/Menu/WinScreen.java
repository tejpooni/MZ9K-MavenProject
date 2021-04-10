package Menu;

import Game.*;

import java.awt.*;
import javax.swing.*;

/**
 * The <code>LoseScreen</code> appears if a player wins.
 * Victory occurs if the player collects all the regular rewards on the map then
 * steps onto the <code>EndPoint</code>.
 */
public class WinScreen extends JPanel
{
	JLabel victoryLabel;
	JButton playAgainButton;
	JButton returnButton;
	JButton exitButton;

	Font buttonFont;
	Font labelFont;

	/**
	 * Sets up the winning screen's <code>JPanel</code>, <code>JLabel</code>, and 3 <code>JButton</code>s.
	 * <p>The <code>JPanel</code> holds all of the elements and has a green background.</p>
	 * <p>The <code>JLabel</code> tells the player that they won.</p>
	 * <p>The 3 <code>JButtons</code> let the player replay the level, return to the main menu, or exit the game.</p>
	 */
	public WinScreen()
	{
		super(new FlowLayout(FlowLayout.CENTER, Game.CELL_SIZE, (int) (Game.CELL_SIZE * 3.5)));

		//Set up the JPanel
		this.setBackground(new Color(96, 255, 0, 84));
		this.setVisible(false);

		//Set up the font
		labelFont = new Font("Arial", Font.BOLD | Font.PLAIN, 72);//font
		buttonFont = new Font("Arial", Font.BOLD | Font.PLAIN, 20);//font

		//Set up the victory label
		victoryLabel = new JLabel("You won!", SwingConstants.CENTER);
		victoryLabel.setForeground(Color.white);
		victoryLabel.setPreferredSize(new Dimension(Game.LEVEL_CELL_WIDTH * Game.CELL_SIZE, Game.CELL_SIZE * 5));
		victoryLabel.setFont(labelFont);
		victoryLabel.setVisible(true);
		this.add(victoryLabel);

		//Set up the playAgainButton
		playAgainButton = new JButton("Play again");
		playAgainButton.setBackground(new Color(238, 240, 242));
		playAgainButton.setPreferredSize(new Dimension(Game.CELL_SIZE * 6, Game.CELL_SIZE * 3));
		playAgainButton.setFont(buttonFont);
		playAgainButton.setVisible(true);
		playAgainButton.addActionListener(e -> Game.setGameState(GameScreenState.RESTART_LEVEL));
		this.add(playAgainButton);

		//Set up the returnButton.
		//Use HTML to have two lines https://stackoverflow.com/a/685631
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
