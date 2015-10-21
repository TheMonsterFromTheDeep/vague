package vague;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import vague.util.Cursor;

/**
 * The ImageLoader class is almost a Singleton container that contains all the image resources for the
 * application.
 * 
 * However, it also contains the  methods needed to load said images.
 * @author TheMonsterFromTheDeep
 */
public class ImageLoader {
    ////General
    public BufferedImage ICON; //The app's icon TODO: Make icon
    
    ////Cursors
    public Cursor TEST_CURSOR;
    
    //Easy name for easy access
    public static ImageLoader IMG;
    
    public ImageLoader() {
        TEST_CURSOR = new Cursor(loadRel("/img/test_cursor.png"),-16,-16);
    }
    
    /**
     * The load method simply returns a BufferedImage loaded from a *relative* path.
     * 
     * The method catches the IOException thrown by ImageIO, but returns null when an exception is encountered.
     * Therefore, if an image fails to load, a NullPointerException will be thrown when the image is used.
     * @param path The relative path to an image.
     * @return The image loaded from the relative path, or null if there was no image.
     */
    public static BufferedImage loadRel(String path) {
        try {
            return ImageIO.read(ImageLoader.class.getResource(path));
        }
        catch(IOException e) {
            System.err.println("There was an error loading an image from " + path + ". There may be consequences.");
            return null;
        }
    }
}
