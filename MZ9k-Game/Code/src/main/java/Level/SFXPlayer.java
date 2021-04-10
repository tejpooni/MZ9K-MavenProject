package Level;

import Game.ErrorMessage;
import Game.ErrorType;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class SFXPlayer
{
	private final HashMap<String, Clip> clips;
	private String missingFiles = "";
	private boolean encounteredException = false;

	SFXPlayer()
	{
		clips = new HashMap<>();
	}

	boolean loadAssets()
	{
		boolean success = true;

		loadSound("bat");
		loadSound("bonus_appear");
		loadSound("bonus_collect");
		loadSound("bonus_expire");
		loadSound("death");
		loadSound("punishment");
		loadSound("rat");
		loadSound("regular_collect");
		loadSound("snake");
		loadSound("victory");

		if(!missingFiles.isEmpty())
		{
			ErrorMessage.showError(ErrorType.loadResources_missingFile, "sounds", missingFiles);
			success = false;
		}
		else if(encounteredException)
		{
			success = false;
		}

		return success;
	}

	private void loadSound(String fileName)
	{
		String soundPath = "sounds/" + fileName + ".wav";

		try
		{
			InputStream test1 = getClass().getClassLoader().getResourceAsStream(soundPath);
			AudioInputStream soundToAdd = AudioSystem.getAudioInputStream(Objects.requireNonNull(test1));

			Clip clipToAdd = AudioSystem.getClip();
			clipToAdd.open(soundToAdd);
			clips.put(fileName, clipToAdd);
		}
		catch (NullPointerException e)
		{
			missingFiles += "\t" + soundPath + "\n";
		}
		catch (IOException e)
		{
			ErrorMessage.showError(ErrorType.loadResources_otherEx, "IOException","sound", soundPath);
			e.printStackTrace();
			encounteredException = true;
		}
		catch (UnsupportedAudioFileException e)
		{
			ErrorMessage.showError(ErrorType.loadResources_otherEx, "UnsupportedAudioFileException", "sound", soundPath);
			encounteredException = true;
			e.printStackTrace();
		}
		catch (LineUnavailableException e)
		{
			ErrorMessage.showError(ErrorType.loadResources_otherEx, "LineUnavailableException", "sound", soundPath);
			encounteredException = true;
			e.printStackTrace();
		}
	}

	void play(String sound)
	{
		if(clips.get(sound) == null)
		{
			ErrorMessage.showError(ErrorType.SFXPlayer_nullStr, sound);
			System.exit(-1);
		}

		if(clips.get(sound).isRunning())
		{
			clips.get(sound).stop();
		}

		clips.get(sound).setFramePosition(0);
		clips.get(sound).start();
	}

	void close()
	{
		for (Clip i : clips.values())
		{i.close();}
	}
}
