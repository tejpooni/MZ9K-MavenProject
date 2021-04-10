package Level;

import Game.ErrorMessage;
import Game.ErrorType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

class LevelFileLoader
{
	String filePath;
	InputStream textFileToRead;
	ArrayList<String> levelInfo;

	LevelFileLoader()
	{
		levelInfo = new ArrayList<>();
	}

	boolean loadLevelFile(String levelFileName, ArrayList<String> parameters, ArrayList<String> layout)
	{
		filePath = levelFileName;
		return loadTextFile() && readAndCloseTextFile() && splitLevelInfo(parameters, layout);
	}

	/**
	 * Loads a file from disk to use as a map. It uses an <code>InputStream</code> to do so.
	 * @return <code>true</code> if the file was found successfully loaded.
	 * <code>false</code> if the level file was not found or if a <code>NullPointerException</code> was thrown.
	 */
	private boolean loadTextFile()
	{
		boolean success = true;

		try
		{
			//How to use InputStream: https://www.tutorialspoint.com/java/lang/classloader_getresourceasstream.htm
			textFileToRead = getClass().getClassLoader().getResourceAsStream(filePath);
		}
		catch (NullPointerException e)
		{
			ErrorMessage.showError(ErrorType.loadFile_NullPtrEx);
			e.printStackTrace();
			success = false;
		}

		if (textFileToRead == null)
		{
			ErrorMessage.showError(ErrorType.loadFile_null, filePath);
			success = false;
		}

		return success;
	}

	/**
	 * Reads the file it loaded from disk and moves the text lines it finds into an <code>ArrayList</code>.
	 * It uses a <code>BufferedReader</code> to read data from the <code>InputStream</code>.
	 * It then tries to close the file regardless if the read was successful.
	 * <p>In the file to read, lines that start with <code>//</code> are seen as comments and are ignored.</p>
	 * @return <code>false</code> if, while trying to read the data, an <code>IOException</code> is thrown.
	 * <code>true</code> otherwise.
	 */
	private boolean readAndCloseTextFile()
	{
		boolean success = true;

		//Read the file.
		BufferedReader reader = new BufferedReader(new InputStreamReader(textFileToRead));
		try
		{
			String lineToRead;
			while ((lineToRead = reader.readLine()) != null)
			{
				//If the line to read has a length of 2 or more, and it starts with //, the line is a comment. Ignore it.
				if (lineToRead.length() < 2 || !lineToRead.startsWith("//"))
				{
					levelInfo.add(lineToRead);
				}
			}
		}
		catch (IOException e)
		{
			ErrorMessage.showError(ErrorType.readAndCloseFile_IOEx, filePath);
			e.printStackTrace();
			success = false;
		}
		finally
		{
			closeTextFile();
		}

		return success;
	}

	/**
	 * Closes the file that was loaded and read from disk. Closing a file can throw an <code>IOException</code>.
	 * This function can handle it. Does not return anything, no matter how the result of the file close.
	 */
	private void closeTextFile()
	{
		try
		{
			textFileToRead.close();
		}
		catch (IOException e)
		{
			ErrorMessage.showError(ErrorType.closeFile_IOEx, filePath);
			e.printStackTrace();
		}
	}

	/**
	 * Splits the <code>ArrayList</code> of data it read from the file into two
	 * <code>ArrayList</code>s: level parameters and level layout. The level file must have a line that only contains
	 * <code>layout</code>; this line indicates the end of parameters and the start of the layout.
	 * The function <code>findLayoutLine</code> tries to find that line.
	 * @return <code>true</code> if the <code>layout</code> line was found; <code>false</code> otherwise.
	 * @see LevelFileLoader#findLayoutLine
	 */
	private boolean splitLevelInfo(ArrayList<String> parameters, ArrayList<String> layout)
	{
		boolean success = true;

		int layoutLine = findLayoutLine();
		if(layoutLine < 0)
		{
			success = false;
		}
		else
		{
			//Split level info into levelParameters.
			parameters.addAll(levelInfo.subList(0, layoutLine));

			//Split level info into levelLayout.
			layout.addAll(levelInfo.subList(layoutLine, levelInfo.size()));
			layout.remove(0);	//Remove the line that says layout.
		}

		return success;
	}

	/**
	 * Finds a line in the level file that only contains the word <code>layout</code>.
	 * @return -1, an error value, of the <code>layout</code> line was not found; the line number of the <code>layout</code> line otherwise.
	 * @see LevelFileLoader#splitLevelInfo
	 */
	private int findLayoutLine()
	{
		int layoutLine = 0;

		boolean foundLayout = false;

		for (int i = 0; i < levelInfo.size(); i++)
		{
			if(levelInfo.get(i).equals("layout"))
			{
				foundLayout = true;
				layoutLine = i;
			}
		}

		if(!foundLayout)
		{
			ErrorMessage.showError(ErrorType.findLayoutLine_noLine, filePath);
			layoutLine = -1;
		}

		return layoutLine;
	}
}
