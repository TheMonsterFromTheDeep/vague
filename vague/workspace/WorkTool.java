package vague.workspace;

import java.awt.Color;
import java.awt.event.MouseEvent;
import vague.module.Module;
import vague.module.TestModule;
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
    
    static final int BORDER_WIDTH = 20;
   
    private boolean closable; //Stores whether the WorkTool should dismiss on mouse down
    //(meaning that the mouse must be located in the dismiss button)
    
    private Module child; //The WorkTool contains a single Module child which does what it needs to do
    private boolean active; //Stores whether the child Module is being controlled by the user
    
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
               
        child = new Module();
        
        //TEST PURPOSES ONLY:
        child = new TestModule(0,0);
        //END TEST
        
        child.setParent(this); //Make sure there are no NullPointerExceptions thrown because of a loss of a parent     
        
        child.resize(size.x - 2 * BORDER_WIDTH, size.y - 2 * BORDER_WIDTH); //The borders around the child module are 20 px thick
        child.locate(BORDER_WIDTH,BORDER_WIDTH); //The child is located at 20,20
        child.draw();
        
        resize(size); //Resize this module last because it requires references to the child in onResize()
        locate(pos);
    }  
    
    @Override
    public void onResize(Vector newSize) {
        child.resize(newSize.x - 2 * BORDER_WIDTH, newSize.y - 2 * BORDER_WIDTH);
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
        if(!child.retainFocus) {
            active = child.containsPoint(pos); //Update whether child Module is active
        }
        if(active) {
            child.mouseMove(pos.getDif(child.position()), dif.getDif(child.position()));
        }
        else { //If the child is not active, check for the various controls of the WorkTool
            //Check if the WorkTool is closable using so-called "magic numbers" - this checks
            //to see if the mouse is in the top-right corner (within 20 pixels of it)
            if(pos.x > width() - BORDER_WIDTH && pos.x < width() && pos.y > 0 && pos.y < BORDER_WIDTH) {
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
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(active) {
            child.mouseDown(e);
        }
        else if(closable) {
            if(parent instanceof Container) {
                ((Container)parent).removeChild(this);
            }
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(active) {
            child.mouseUp(e);
        }
    }
    
    @Override
    public void draw() {     
        graphics.setColor(bgColor);
        graphics.fillRect(1, 1, width() - 2, height() - 2);
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(0,0,width() - 1,height() - 1);
        graphics.setColor(closable ? DISMISS_COLOR_HIGH : DISMISS_COLOR); //Set the dismiss color based on whether the Module is closable
        graphics.fillRect(width() - BORDER_WIDTH, 0, BORDER_WIDTH, BORDER_WIDTH);
        
        graphics.drawImage(child.render(), BORDER_WIDTH, BORDER_WIDTH, null);
    }
}