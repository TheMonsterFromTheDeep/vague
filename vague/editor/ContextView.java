package vague.editor;

import java.awt.Color;
import module.paint.GraphicsHandle;
import module.util.FloatVector;
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
    
    public void zoom(int amount, Vector anchor) {
        this.zoom += amount;
        
        double change = zoomSize;
        
        calculateZoom();
        
        change = zoomSize / change; //The difference in the zoom
        
        this.x = anchor.x + (int)((this.x - anchor.x) * change);
        this.y = anchor.y + (int)((this.y - anchor.y) * change);
    }
    
    public boolean moveMouseTool(Tool t, Vector pos, Vector dif) {
        //TODO: Implement floating-point vectors cuz why not
        FloatVector prPos = new FloatVector((pos.x - x) / zoomSize, (pos.y - y) / zoomSize);
        FloatVector prDif = new FloatVector(dif.x * zoomSize, dif.y * zoomSize);
        
        return t.mouseMove(prPos, prDif);
    }
    
    public boolean checkMouse(Vector mousePos) {
        double width = (context.getWidth() * zoomSize);
        double height = (context.getHeight() * zoomSize);
        
        int addTileX = 0;
        int addTileY = 0;
        
        int compx = x + (int)(context.getX() * zoomSize);
        int compy = y + (int)(context.getY() * zoomSize);
        
        if(mousePos.x < compx) {
            addTileX = (int)((mousePos.x - compx) / (Context.DEFAULT_SIZE * zoomSize)) - 1;
            //x -= (int)(addTileX * Context.DEFAULT_SIZE * zoomSize);
        }
        else if(mousePos.x > compx + width) {
            addTileX = (int)((mousePos.x - (x + width)) / (Context.DEFAULT_SIZE * zoomSize)) + 1;
        }
        if(mousePos.y < compy) {
            addTileY = (int)((mousePos.y - compy) / (Context.DEFAULT_SIZE * zoomSize)) - 1;
            //y -= (int)(addTileY * Context.DEFAULT_SIZE * zoomSize);
        }
        else if(mousePos.y > compy + height) {
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
        handle.drawRect(x + (int)(context.getX() * zoomSize), y + (int)(context.getY() * zoomSize), (int)(width * zoomSize), (int)(height * zoomSize));
        context.render(handle, x, y, zoomSize);
        
        handle.setColor(oldColor);
    }
}