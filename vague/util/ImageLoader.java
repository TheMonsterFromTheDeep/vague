package vague.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Provides a method for loading images from a file. Used for loading resource images for the program
 * and user images.
 * @author TheMonsterFromTheDeep
 */
public final class ImageLoader {
    /**
     * Loads an image from specified path.
     * @param path The path to the image.
     * @return The image as loaded from the path.
     * @throws IOException 
     */
    public static BufferedImage load(String path) throws IOException {
        BufferedImage b = ImageIO.read(ImageLoader.class.getResource(path));
        return b;
    }
    
    /**
     * Loads an image from specified path. Protected with a catch statement but might result in NullPointerExceptions.
     * @param path The path to the image.
     * @return The image as loaded from the path.
     */
    public static BufferedImage loadProtected(String path) {
        try {
            BufferedImage b = ImageIO.read(ImageLoader.class.getResource(path));
            return b;
        }
        catch (IOException e) {
            System.err.println("Error loading image from " + path + ": " + e.getLocalizedMessage());
            return null;
        }
    }
}