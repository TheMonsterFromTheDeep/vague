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
    
    static final Color DISMISS_COLOR = new Color(0xe74f4f); //Stores the colors of the button which dismisses a WorkTool
    static final Color DISMISS_COLOR_HIGH = new Color(0xf96666);
   
    private boolean closable; //Stores whether the WorkTool should dismiss on mouse down
    //(meaning that the mouse must be located in the dismiss button)
    
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
    public void mouseMove(Vector pos, Vector dif) {
        //Check if the WorkTool is closable using so-called "magic numbers" - this checks
        //to see if the mouse is in the top-right corner (within 20 pixels of it)
        if(pos.x > right() - 21 && pos.x < right() && pos.y > y() && pos.y < y() + 21) {
            closable = true;
            redraw();
        }
        else {
            if(closable) {
                closable = false;
                redraw();
            }
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(closable) {
            if(parent instanceof Container) {
                ((Container)parent).removeChild(this);
            }
        }
    }
    
    @Override
    public void draw() {     
        graphics.setColor(bgColor);
        graphics.fillRect(1, 1, width() - 2, height() - 2);
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(0,0,width() - 1,height() - 1);
        graphics.setColor(closable ? DISMISS_COLOR_HIGH : DISMISS_COLOR); //Set the dismiss color based on whether the Module is closable
        graphics.fillRect(width() - 21, 1, 20, 20);
    }
}