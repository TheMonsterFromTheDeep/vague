package vague.editor;

import java.awt.Color;
import module.paint.GraphicsHandle;
import module.util.Vector;

/**
 * The context for the image being edited. Contains all image data and stuff.
 * 
 * TODO: Figure out what the heck I'm talking about
 * 
 * I think that I will need to modify this in the future; it should keep track of all objects
 * and be able to render them.
 * @author TheMonsterFromTheDeep
 */
public class Context {
    public static final int DEFAULT_SIZE = 32;
    public static final int DEFAULT_SIZE_DIV = DEFAULT_SIZE + 1;
    
    private int width;
    private int height;
    
    private static Context context;
    
    private Context() {
        width = DEFAULT_SIZE;
        height = DEFAULT_SIZE;
    }
    
    public static Context getContext() {
        if(context == null) {
            context = new Context();
        }
        return context;
    }
    
    public int getOffsetX() {
        return width / -2;
    }
    
    public int getOffsetY() {
        return height / -2;
    }
    
    public void expand(int x, int y) {
        width += x * DEFAULT_SIZE;
        height += y * DEFAULT_SIZE;
    }
        
    public void drawBorder(GraphicsHandle handle, int x, int y) {
        Color tmp = handle.getColor();
        handle.setColor(Color.BLACK);
        handle.drawRect(x, y, width, height);
        handle.setColor(tmp);
    }
    
    public void render(GraphicsHandle handle, int x, int y) {
        //handle.drawImage(data, x, y);
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}