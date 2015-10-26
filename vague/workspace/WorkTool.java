package vague.workspace;

import java.awt.Color;
import java.awt.event.MouseEvent;
import vague.module.Module;
import vague.module.container.Container;
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
    static final Color BG_COLOR_HIGH = new Color(0xbfbfdd); //The color of the WorkTool when it is active
    static final Color BORDER_COLOR = new Color(0);
    
    private Module child; //The WorkTool contains a single Module child which does what it needs to do
    
    public WorkTool(Vector start, Vector end) {
        bgColor = BG_COLOR;
        
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
        redraw();
    }
    
    @Override
    public void onUnfocus() {
        bgColor = BG_COLOR;
        redraw();
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(parent instanceof Container) {
            ((Container)parent).removeChild(this);
        }
    }
    
    @Override
    public void draw() {        
        graphics.setColor(bgColor);
        graphics.fillRect(1, 1, width() - 2, height() - 2);
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(0,0,width() - 1,height() - 1);
    }
}