package Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class PlayerMover extends JLabel
{
	//Used by Player to check which key is being inputted
	private boolean inputUp;
	private boolean inputDown;
	private boolean inputLeft;
	private boolean inputRight;
	private boolean inputSpace;

	public PlayerMover()
	{
		super();	//Constructor of JLabel

		this.setBackground(new Color(255, 255, 255, 0));
		this.setBounds(32, 32, 32, 32);
		this.setOpaque(false);

		//Use the version of getKeyStroke that lets you specify if the key is pressed or released.
		//https://docs.oracle.com/javase/7/docs/api/javax/swing/KeyStroke.html#getKeyStroke(int,%20int,%20boolean)
		//Also, input requires WHEN_IN_FOCUSED_WINDOW to work. https://stackoverflow.com/a/42173447

		//Player presses/releases W (for up)
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0,false),"pressUp");
		this.getActionMap().put("pressUp", new PressUp());
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0,true),"releaseUp");
		this.getActionMap().put("releaseUp", new ReleaseUp());

		//Player presses/releases S (for down)
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0,false),"pressDown");
		this.getActionMap().put("pressDown", new PressDown());
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0,true),"releaseDown");
		this.getActionMap().put("releaseDown", new ReleaseDown());

		//Player presses/releases A (for left)
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,false),"pressLeft");
		this.getActionMap().put("pressLeft", new PressLeft());
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,true),"releaseLeft");
		this.getActionMap().put("releaseLeft", new ReleaseLeft());

		//Player presses/releases D (for right)
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,false),"pressRight");
		this.getActionMap().put("pressRight", new PressRight());
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,true),"releaseRight");
		this.getActionMap().put("releaseRight", new ReleaseRight());

		//Player presses/releases Space (for turbo movement)
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,false),"pressSpace");
		this.getActionMap().put("pressSpace", new PressSpace());
		this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,true),"releaseSpace");
		this.getActionMap().put("releaseSpace", new ReleaseSpace());
	}

	boolean getInputUp() {return inputUp;}

	boolean getInputDown() {return inputDown;}

	boolean getInputLeft() {return inputLeft;}

	boolean getInputRight() {return inputRight;}

	boolean getInputSpace() {return inputSpace;}

	private class PressUp extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputUp = true;
		}
	}

	private class ReleaseUp extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputUp = false;
		}
	}

	private class PressDown extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputDown = true;
		}
	}

	private class ReleaseDown extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputDown = false;
		}
	}

	private class PressLeft extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputLeft = true;
		}
	}

	private class ReleaseLeft extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputLeft = false;
		}
	}

	private class PressRight extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputRight = true;
		}
	}

	private class ReleaseRight extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputRight = false;
		}
	}

	private class PressSpace extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputSpace = true;
		}
	}

	private class ReleaseSpace extends AbstractAction
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			inputSpace = false;
		}
	}

}
