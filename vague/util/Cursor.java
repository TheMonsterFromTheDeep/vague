package vague.util;

import java.awt.image.BufferedImage;

/**
 * The Cursor class defines a cursor that should be drawn at the location of the mouse.
 * 
 * Cursors can have specific x and y positions that they need to be drawn at so that they
 * will be drawn accurately to where they will make the most sense.
 * @author TheMonsterFromTheDeep
 */
public class Cursor {
    public BufferedImage image; //Stores the image which represents the cursor
    private int offsetx; //Stores the offset from the mouse position to draw the cursor (e.g. -4)
    private int offsety;
    
    public Cursor(BufferedImage image, int offsetx, int offsety) {
        this.image = image;
        this.offsetx = offsetx;
        this.offsety = offsety;
    }
    
    public int getDrawX(int mousex) {
        return mousex + offsetx;
    }
    
    public int getDrawY(int mousey) {
        return mousey + offsety;
    }
}