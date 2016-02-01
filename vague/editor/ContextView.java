package vague.editor;

import java.awt.Color;
import module.paint.GraphicsHandle;
import module.util.Vector;
import vague.editor.tool.Tool;

/**
 * The ContextView is a handle to the Context that stores a position and zoom.
 * @author TheMonsterFromTheDeep
 */
public class ContextView {
    private int x; //Stores the top-left corner position of the context in this view.
    private int y;
    
    private int zoom; //Stores the zoom level - a whole number
    private float zoomSize; //Equals 2 ^ zoom - the multiplier that the zoom actually does.
    
    Context context;
    
    private void calculateZoom() {
        zoomSize = (float)Math.pow(2, zoom);
    }
    
    public ContextView() {
        this(0, 0);
    }
    
    public ContextView(int x, int y) {
        this.x = x;
        this.y = y;
        
        zoom = 0;
        calculateZoom();
        
        context = Context.getContext();
    }
    
    public void panX(int x) {
        this.x += x;
    }
    
    public void panY(int y) {
        this.y += y;
    }
    
    public void panXScalar(int x) {
        this.x += (int)(x * (1 + zoomSize));
    }
    
    public void panYScalar(int y) {
        this.y += (int)(y * (1 + zoomSize));
    }
    
    public void zoom(int amount) {
        this.zoom += amount;
        calculateZoom();
    }
    
    public boolean moveMouseTool(Tool t, Vector pos, Vector dif) {
        //TODO: Implement floating-point vectors cuz why not
        Vector prPos = new Vector((int)((pos.x - x) / zoomSize), (int)((pos.y - y) / zoomSize));
        Vector prDif = new Vector((int)(dif.x * zoomSize), (int)(dif.y * zoomSize));
        
        return t.mouseMove(prPos, prDif);
    }
    
    public boolean checkMouse(Vector mousePos) {
        double width = (context.getWidth() * zoomSize);
        double height = (context.getHeight() * zoomSize);
        
        int addTileX = 0;
        int addTileY = 0;
        
        if(mousePos.x < x) {
            addTileX = (int)((x - mousePos.x) / (Context.DEFAULT_SIZE * zoomSize)) + 1;
            x -= (int)(addTileX * Context.DEFAULT_SIZE * zoomSize);
        }
        else if(mousePos.x > x + width) {
            addTileX = (int)((mousePos.x - (x + width)) / (Context.DEFAULT_SIZE * zoomSize)) + 1;
        }
        if(mousePos.y < y) {
            addTileY = (int)((y - mousePos.y) / (Context.DEFAULT_SIZE * zoomSize)) + 1;
            y -= (int)(addTileY * Context.DEFAULT_SIZE * zoomSize);
        }
        else if(mousePos.y > y + height) {
            addTileY = (int)((mousePos.y - (y + height)) / (Context.DEFAULT_SIZE * zoomSize)) + 1;
        }
        
        if(addTileX == 0 && addTileY == 0) {
            return false;
        }
        context.expand(addTileX, addTileY);
        return true;
    }
    
    public void draw(GraphicsHandle handle) {
        Color oldColor = handle.getColor();
        
        int width = context.getWidth();
        int height = context.getHeight();
        
        handle.setColor(Color.BLACK);
        handle.drawRect(x, y, (int)(width * zoomSize), (int)(height * zoomSize));
        context.render(handle, x, y, zoomSize);
        
        handle.setColor(oldColor);
    }
}