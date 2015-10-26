package vague.workspace;

import java.awt.Color;
import vague.module.Module;
import vague.util.Vector;

/**
 * The WorkTool class defines a Module that can be created through the Workspace. It is fundamentally
 * different from a "Tool", which is a user-controlled device for image manipulation. (TODO: Make Tool class)
 * 
 * The user can create and edit WorkTool classes in order to customize the UI. It will be one of several different
 * container classes for controlling the user interface.
 * @author TheMonsterFromTheDeep
 */
public class WorkTool extends Module {
    static final Color BG_COLOR = new Color(0xbcbcdd); //The color of the WorkTool when it is not active
    static final Color BORDER_COLOR = new Color(0xb4b4dd);
    static final Color BG_COLOR_HIGH = new Color(0xbfbfdd); //The color of the WorkTool when it is active
    static final Color BORDER_COLOR_HIGH = new Color(0xb7b7dd);
    
    private Color borderColor; //Stores the current border color of the WorkTool
    
    public WorkTool(Vector start, Vector end) {
        bgColor = BG_COLOR;
        borderColor = BORDER_COLOR;
        
        Vector pos = new Vector(), size = new Vector();
        if(start.x < end.x) {
            pos.x = start.x;
            size.x = end.x - start.x;
        }
        else {
            pos.x = end.x;
            size.x = start.x - end.x;
        }
        
        if(start.y < end.y) {
            pos.y = start.y;
            size.y = end.y - start.y;
        }
        else {
            pos.y = end.y;
            size.y = start.y - end.y;
        }
        resize(size);
        locate(pos);
    }  
    
    @Override
    public void onFocus() {
        bgColor = BG_COLOR_HIGH;
        borderColor = BORDER_COLOR_HIGH;
        
        redraw();
    }
    
    @Override
    public void onUnfocus() {
        bgColor = BG_COLOR;
        borderColor = BORDER_COLOR;
        
        redraw();
    }
    
    @Override
    public void draw() {
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(0,0,width(),height());
        graphics.setColor(bgColor);
        graphics.fillRect(1,1,width()-1,height()-1);
    }
}