package Level;

import Game.ErrorMessage;
import Game.ErrorType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ImageSet
{
	private final HashMap<String, BufferedImage> sprites;
	private String missingFiles = "";
	private boolean encounteredIOException = false;

	ImageSet()
	{
		sprites = new HashMap<>();
	}

	boolean loadAssets()
	{
		boolean success = true;
		final String backgroundFolder = "backgrounds/";
		final String spritesFolder = "sprites/";

		loadImage(backgroundFolder, "stats_1");
		loadImage(spritesFolder, "grass");

		loadImage(spritesFolder, "solid_wall");
		loadImage(spritesFolder, "starting_point");
		loadImage(spritesFolder, "ending_point");

		loadImage(spritesFolder, "player");
		loadImage(spritesFolder, "tracking_enemy");
		loadImage(spritesFolder, "lr_enemy");
		loadImage(spritesFolder, "ud_enemy");

		loadImage(spritesFolder, "regular_reward");
		loadImage(spritesFolder, "bonus_reward");
		loadImage(spritesFolder, "punishment");

		if(!missingFiles.isEmpty())
		{
			ErrorMessage.showError(ErrorType.loadResources_missingFile, "images", missingFiles);
			success = false;
		}
		else if(encounteredIOException)
		{
			success = false;
		}

		return success;
	}

	private void loadImage(String filePath, String fileName)
	{
		String imagePath = filePath + fileName + ".png";

		try
		{
			BufferedImage imageToAdd = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(imagePath)));
			sprites.put(fileName, imageToAdd);
		}
		catch (NullPointerException e)
		{
			missingFiles += "\t" + imagePath + "\n";
		}
		catch (IOException e)
		{
			ErrorMessage.showError(ErrorType.loadResources_otherEx, "IOException", "image", imagePath);
			e.printStackTrace();
			encounteredIOException = true;
		}
	}

	BufferedImage get(String image)
	{
		BufferedImage imageToReturn = sprites.get(image);

		if(imageToReturn == null)
		{
			ErrorMessage.showError(ErrorType.ImageSet_nullStr, image);
			System.exit(-1);
		}

		return imageToReturn;
	}
}
