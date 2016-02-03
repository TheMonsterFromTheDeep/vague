package vague.editor;

import java.awt.Color;
import module.paint.GraphicsHandle;
import module.util.FloatVector;
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
    public static final int DEFAULT_SIZE_HALF = DEFAULT_SIZE / 2;
    public static final int DEFAULT_SIZE_DIV = DEFAULT_SIZE + 1;
    
    private int width;
    private int height;
    
    FloatVector center;
    Vector topLeft;
    Vector bottomRight;
    
    public Tool activeTool; //A handle to the currently active tool.
    public Editor activeEditor; //If an editor is currently active, this holds a handle to it.
    
    private static Context context;
    
    private Shape[] shapes; //Stores the various data for the image.
    
    private void calculateDims() {
        width = bottomRight.x - topLeft.x;
        height = bottomRight.y - topLeft.y;
        center.x = topLeft.x + (width / 2.0f);
        center.y = topLeft.y + (height / 2.0f);
    }
    
    private Context() {
        topLeft = new Vector(-DEFAULT_SIZE_HALF, -DEFAULT_SIZE_HALF);
        bottomRight = new Vector(DEFAULT_SIZE_HALF, DEFAULT_SIZE_HALF);
        
        center = new FloatVector(0, 0);
        
        activeTool = new PencilTool();
        
        //TODO: Optimize shape array stuffz
        shapes = new Shape[0];
        
        calculateDims();
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
    
    public int getX() { return topLeft.x; }
    
    public int getY() { return topLeft.y; }
    
    public void expand(int x, int y) {
        //width += x * DEFAULT_SIZE;
        //height += y * DEFAULT_SIZE;
        
        //TODO: Prevent expansion beyond min/max int size
        
        if(x < 0) {
            topLeft.x += x * DEFAULT_SIZE;
        } else if(x > 0) {
            bottomRight.x += x * DEFAULT_SIZE;
        }
        if(y < 0) {
            topLeft.y += y * DEFAULT_SIZE;
        } else if(y > 0) {
            bottomRight.y += y * DEFAULT_SIZE;
        }
        
        calculateDims();
    }
        
    public void drawBorder(GraphicsHandle handle, int x, int y) {
        Color tmp = handle.getColor();
        handle.setColor(Color.BLACK);
        handle.drawRect(x, y, width, height);
        handle.setColor(tmp);
    }
    
    /**
     * Renders the Context using the specified handle at the specified x and y with the specified scale.
     * 
     * The x and y SHOULD BE THE TOP LEFT CORNER WHERE THIS IS BEING RENDERED.
     * @param handle The handle to render the Context with.
     * @param x The x of the top-left corner of the render.
     * @param y The y of the top-left corner of the render.
     * @param scale The scale of the render.
     */
    public void render(GraphicsHandle handle, int x, int y, float scale) {
        //handle.drawImage(data, x, y);
        
        //int dx = x + (int)(topLeft.x * scale);
        //int dy = y + (int)(topLeft.y * scale);
        
        GraphicsHandle clippedHandle = handle.getClip(x, y, (int)(width * scale), (int)(height * scale));
        
        for(Shape s : shapes) {
            s.draw(-topLeft.x, -topLeft.y, clippedHandle, scale);
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
    
    public void setEditor(Editor e) {
        activeEditor = e;
    }
    
    public void clearEditor(Editor e) {
        if(activeEditor == e) {
            activeEditor = null;
        }
    }
}