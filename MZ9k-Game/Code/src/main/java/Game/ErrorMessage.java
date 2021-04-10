package Game;

import javax.swing.*;

public class ErrorMessage
{
	private final static String impError = "Implementation error";
	private final static String regError = "Error";

	public static void showError(ErrorType e)
	{
		String message = "";
		boolean implementationError = false;

		switch (e)
		{
			case loadFile_NullPtrEx:
				message = "String passed to getClass.getClassLoader.getResourceAsStream in Level.loadFile was null. Check the implementation of Level.loadFile";
				implementationError = true;
				break;
			case MusicPlayer_play_JLayerEx:
				message = "Encountered a JavaLayerException while trying to play music";
				break;
			case Level_closeMusic_failThreadJoin:
				message = "Encountered an InterruptedException while trying to join the Thread that plays music (non-fatal error)";
				break;
		}

		System.out.println((implementationError ? impError : regError) + ": " + message);
		JOptionPane.showMessageDialog(null, message,(implementationError ? impError : regError), JOptionPane.ERROR_MESSAGE);
	}

	public static void showError(ErrorType e, String arg1)
	{
		String message = "";
		boolean implementationError = false;

		switch (e)
		{
			case loadFile_null:
				message = "Failed to find the level file \"" + arg1 + "\"";
				break;
			case readAndCloseFile_IOEx:
				message = "Encountered an IOException while reading a line in the level file \"" + arg1 + "\"";
				break;
			case closeFile_IOEx:
				message = "Encountered an IOException while trying to close the level file \"" + arg1 + "\" (non-fatal error)";
				break;
			case findLayoutLine_noLine:
				message = "Level file \"" + arg1 + "\" is malformed. Requires a line that only contains the word \"layout\" to signal the end of level parameters and the start of level layout";
				break;
			case ImageSet_nullStr:
				message = "Did not find the image \"" + arg1 + "\" in Level.imageSet. Check the usage of imageSet.get in Level.loadLayout";
				implementationError = true;
				break;
			case MusicPlayer_load_JLayerEx:
				message = "Encountered a JavaLayerException while trying to load the music \"" + arg1 + "\"";
				break;
			case SFXPlayer_nullStr:
				message = "Did not find the sound \"" + arg1 + "\" in Level.sfxPlayer. Check the usage of sfxPlayer.play in Level";
				implementationError = true;
				break;
			case StartMenu_loadResources_NullPtrEx:
				message = "Could not find the following image for the start menu: " + arg1;
				break;
			case StartMenu_loadResources_IOEx:
				message = "Encountered an IOException while trying to load the image for the start menu: " + arg1;
				break;
		}

		System.out.println((implementationError ? impError : regError) + ": " + message);
		JOptionPane.showMessageDialog(null, message,(implementationError ? impError : regError), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 *
	 * @param e The type of error
	 * @param arg1 Path to the level file (parameter), name of the type of file (asset loading)
	 * @param arg2 Name of the parameter (parameter), name of the file (asset loading)
	 */
	public static void showError(ErrorType e, String arg1, String arg2)
	{
		String message = "";

		switch (e)
		{
			case assignParameter_appearMulti:
				message = "In level file \"" + arg1 + "\":\n\tLevel parameter \"" + arg2 + "\" appears more than once";
				break;
			case assignParameter_noValForList:
				message = "In level file \"" + arg1 + "\":\n\tNo values found for level parameter \"" + arg2 + "\"";
				break;
			case assignParameter_negVal:
				message = "In level file \"" + arg1 + "\":\n\tNegative value(s) found for level parameter \"" + arg2 + "\"";
				break;
			case assignParameter_unknParamName:
				message = "In level file \"" + arg1 + "\":\n\tUnknown level parameter \"" + arg2 + "\" found";
				break;
			case loadParameter_unsetParam:
				message = "In level file \"" + arg1 + "\":\n\tLevel parameter \"" + arg2 + "\" was not found";
				break;
			case loadResources_missingFile:
				message = "Could not find the following " + arg1 + ":\n" + arg2;
				break;
			case Level_loadLayout_twoStartEnd:
				message = "In level file \"" + arg1 + "\":\n\tFound more than one " + arg2 + "; expected 1";
				break;
		}

		System.out.println(regError + ": " + message);
		JOptionPane.showMessageDialog(null, message,regError, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 *
	 * @param e The type of error
	 * @param arg1 Path to the level file
	 * @param arg2 The unknown value found for the parameter
	 * @param arg3 The name of the parameter
	 */
	public static void showError(ErrorType e, String arg1, String arg2, String arg3)
	{
		String message = "";

		switch (e)
		{
			case loadResources_otherEx:
				message = "Encountered an " + arg1 + " while trying to load the " + arg2 + " \"" + arg3 + "\"";
				break;
			case assignParameter_unknVal:
				message = "In level file \"" + arg1 + "\":\n\tUnknown value \"" + arg2 + "\" found for level parameter \"" + arg3 + "\"";
				break;
			case Level_loadLayout_uneqRowNum:
				message = "In level file \"" + arg1 + "\":\n\tFound " + arg2 + " lines after the layout line; expected " + arg3;
				break;
		}

		System.out.println(regError + ": " + message);
		JOptionPane.showMessageDialog(null, message,regError, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 *
	 * @param e The type of error
	 * @param arg1 Path to the level file with the error
	 * @param arg2 Name of the parameter
	 * @param arg3 Expected number of values
	 * @param arg4 Found number of values
	 */
	public static void showError(ErrorType e, String arg1, String arg2, String arg3, String arg4)
	{
		String message = "";

		switch (e)
		{
			case assignParameter_uneqVal:
				message = "In level file \"" + arg1 + "\":\n\tLevel parameter \"" + arg2 + "\" expected " + arg3 + " value(s); found " + arg4;
				break;
			case Level_loadLayout_uneqEleNum:
				message = "In level file \"" + arg1 + "\":\n\tFound " + arg2 + " characters in row " + arg3 + "; expected " + arg4;
				break;
			case Level_loadLayout_unknChar:
				message = "In level file \"" + arg1 + "\":\n\tUnrecognized character '" + arg2 + "' found at position (" + arg3 + ", " + arg4 + ") of the layout";
				break;
		}

		System.out.println(regError + ": " + message);
		JOptionPane.showMessageDialog(null, message,regError, JOptionPane.ERROR_MESSAGE);
	}
}
