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
public class Resources {
    ////General
    public BufferedImage ICON; //The app's icon
    public BufferedImage BACKGROUND; //The background of the Workspace
    
    ////Cursors
    public Cursor TEST_CURSOR;
    
    //Easy name for easy access
    public static Resources bank;
    
    public Resources() { }
    
    /**
     * Loads all the resources for the application. The WaitPopup communicates what is being loaded
     * at the current time.
     * @param popup The WaitPopup to display loading messages to the user.
     */
    public void loadResources(WaitPopup popup) {
        ////LOAD GENERAL IMAGES
        popup.updateLoadingMessage("images"); //Update the loading message
        ICON = loadImageRelative("/img/icon.png"); //Load images
        BACKGROUND = loadImageRelative("/img/background.png");
        
        ////LOAD CURSORS       
        popup.updateLoadingMessage("images/cursors"); //Update the loading message
        TEST_CURSOR = new Cursor(loadImageRelative("/img/test_cursor.png"),-8,-8); //Load cursors
    }
    
    /**
     * The load method simply returns a BufferedImage loaded from a *relative* path.
     * 
     * The method catches the IOException thrown by ImageIO, but returns null when an exception is encountered.
     * Therefore, if an image fails to load, a NullPointerException will be thrown when the image is used.
     * @param path The relative path to an image.
     * @return The image loaded from the relative path, or null if there was no image.
     */
    public static BufferedImage loadImageRelative(String path) {
        try {
            return ImageIO.read(Resources.class.getResource(path));
        }
        catch(IOException e) {
            System.err.println("There was an error loading an image from " + path + ". There may be consequences.");
            return null;
        }
    }
}
