package vague;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import vague.util.Cursor;
import vague.util.TextDrawer;

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
    //--Class WorkTool
    public BufferedImage WORKTOOL_RESIZE_TL; //Images used to draw resize controls
    public BufferedImage WORKTOOL_RESIZE_TL_HIGH; //HIGH = mouse is over control
    public BufferedImage WORKTOOL_RESIZE_BL; //TL = top left, BL = bottom left, BR = bottom right
    public BufferedImage WORKTOOL_RESIZE_BL_HIGH;
    public BufferedImage WORKTOOL_RESIZE_BR;
    public BufferedImage WORKTOOL_RESIZE_BR_HIGH;
    public BufferedImage WORKTOOL_REFRESH;
    public BufferedImage WORKTOOL_REFRESH_HIGH;
    public BufferedImage WORKTOOL_FLIP;
    public BufferedImage WORKTOOL_FLIP_HIGH;
    public BufferedImage WORKTOOL_WARNING;
    //--Tool Options
    //---Color Setting
    public BufferedImage SETTING_COLOR_HSV_CURSOR;
    public BufferedImage SETTING_COLOR_VALUE_CURSOR;
    
    
    //Text
    public TextDrawer text;
    
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
        
        ////LOAD CONTROLS
        popup.updateLoadingMessage("images/buttons");
        WORKTOOL_RESIZE_TL = loadImageRelative("/img/worktool/resize_tl.png");
        WORKTOOL_RESIZE_TL_HIGH = loadImageRelative("/img/worktool/resize_tl_high.png");
        WORKTOOL_RESIZE_BL = loadImageRelative("/img/worktool/resize_bl.png");
        WORKTOOL_RESIZE_BL_HIGH = loadImageRelative("/img/worktool/resize_bl_high.png");
        WORKTOOL_RESIZE_BR = loadImageRelative("/img/worktool/resize_br.png");
        WORKTOOL_RESIZE_BR_HIGH = loadImageRelative("/img/worktool/resize_br_high.png");
        WORKTOOL_REFRESH = loadImageRelative("/img/worktool/refresh.png");
        WORKTOOL_REFRESH_HIGH = loadImageRelative("/img/worktool/refresh_high.png");
        WORKTOOL_FLIP = loadImageRelative("/img/worktool/flip.png");
        WORKTOOL_FLIP_HIGH = loadImageRelative("/img/worktool/flip_high.png");
        WORKTOOL_WARNING = loadImageRelative("/img/worktool/warning.png");
        
        SETTING_COLOR_HSV_CURSOR = loadImageRelative("/img/tool/options/color/hsvtarget.png");
        SETTING_COLOR_VALUE_CURSOR = loadImageRelative("/img/tool/options/color/valuetarget.png");
        
        ////LOAD CURSORS       
        popup.updateLoadingMessage("images/cursors"); //Update the loading message
        TEST_CURSOR = new Cursor(loadImageRelative("/img/test_cursor.png"),-8,-8); //Load cursors
        
        ////LOAD TEXT
        popup.updateLoadingMessage("text");
        text = new TextDrawer(loadImageRelative("/img/text/char.png"));
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
