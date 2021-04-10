package Level;
import Game.ErrorMessage;
import Game.ErrorType;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.InputStream;
import java.util.Objects;

public class MusicPlayer extends Thread
{
	private javazoom.jl.player.Player musicPlayer;

	MusicPlayer() {}

	@Override
	public void run()
	{
		super.run();
		playMusic();
	}

	boolean loadAssets(String musicName)
	{
		boolean success = true;

		String musicPath = "music/" + musicName + ".mp3";

		try
		{
			InputStream fis = getClass().getClassLoader().getResourceAsStream(musicPath);
			musicPlayer = new Player(Objects.requireNonNull(fis));
		}
		catch (NullPointerException e)
		{
			ErrorMessage.showError(ErrorType.loadResources_missingFile, "music", musicPath);
			success = false;
		}
		catch (JavaLayerException e)
		{
			ErrorMessage.showError(ErrorType.MusicPlayer_load_JLayerEx, musicPath);
			e.printStackTrace();
			success = false;
		}

		return success;
	}

	private void playMusic()
	{
		try
		{
			//The music player blocks the thread (thread is stuck playing music) so lines after the one below won't execute
			musicPlayer.play();
		}
		catch (JavaLayerException e)
		{
			ErrorMessage.showError(ErrorType.MusicPlayer_play_JLayerEx);
			e.printStackTrace();
		}
	}

	void stopMusic()
	{
		musicPlayer.close();
	}
}
