package vague.editor;

import java.awt.Color;
import module.paint.GraphicsHandle;
import module.util.Vector;
import vague.editor.shape.Shape;
import vague.editor.tool.PencilTool;
import vague.editor.tool.NullTool;
import vague.editor.tool.Tool;

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
    
    public Tool activeTool; //A handle to the currently active tool.
    
    private static Context context;
    
    private Shape[] shapes; //Stores the various data for the image.
    
    private Context() {
        width = DEFAULT_SIZE;
        height = DEFAULT_SIZE;
        
        activeTool = new PencilTool();
        
        //TODO: Optimize shape array stuffz
        shapes = new Shape[0];
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
    
    public void render(GraphicsHandle handle, int x, int y, float scale) {
        //handle.drawImage(data, x, y);
        
        GraphicsHandle clippedHandle = handle.getClip(x, y, (int)(width * scale), (int)(height * scale));
        
        for(Shape s : shapes) {
            s.draw(clippedHandle, scale);
        }
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public void addShape(Shape s) {
        Shape[] tmp = shapes;
        shapes = new Shape[shapes.length + 1];
        System.arraycopy(tmp, 0, shapes, 0, tmp.length);
        shapes[tmp.length] = s;
    }
}