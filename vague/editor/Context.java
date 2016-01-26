package vague.editor;

import java.awt.Color;
import java.awt.image.BufferedImage;
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
    private static final int DEFAULT_SIZE = 32;
    private static final int DEFAULT_SIZE_DIV = DEFAULT_SIZE + 1;
    
    int width;
    int height;
    
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
    
    /**
     * @return the new position that the thing should be located at
     */
    public Vector expand(Vector position, Vector target) {
        int x = target.x - position.x;
        int y = target.y - position.y;
        
        int tileCountX = 0;
        int tileCountY = 0;
        
        Vector newPos = new Vector(position);
        
        if (x > width) {
            x -= width;
            tileCountX = (x / DEFAULT_SIZE_DIV) + 1;
            
        }
        else if(x < 0) {
            x = -x;
            tileCountX = (x / DEFAULT_SIZE_DIV) + 1;
            newPos.x -= tileCountX * 32;
        }
        
        if(y > height) {
            y -= height;
            tileCountY = (y / DEFAULT_SIZE_DIV) + 1;
            
        }
        else if(y < 0) {
            y = -y;
            tileCountY = (y / DEFAULT_SIZE_DIV) + 1;
            newPos.y -= tileCountY * 32;
        }
        
        width += tileCountX * 32;
        height += tileCountY * 32;
        
        return newPos;
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
}