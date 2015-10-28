package vague.workspace;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
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
    
    private boolean moveable;
    private boolean moving;
    
    private Vector movePos; //Stores the mouse position when the mouse starts moving
    private Vector startPos; //Stores the position to jump back to if a move fails
    
    private Module child; //The WorkTool contains a single Module child which does what it needs to do
    private boolean active; //Stores whether the child Module is being controlled by the user
    
    private Workspace workspace;
    
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
        if(closable) {
            closable = false;
        }
        redraw();
    }
    
    @Override
    public boolean retainFocus() { return retaining || child.retainFocus(); }
    
    /**
     * Sets the Workspace parent of the WorkTool for more efficient code.
     * @param w The new Workspace object to reference
     */
    public void setWorkspace(Workspace w) { this.workspace = w; }
    
    @Override
    public void mouseMove(Vector pos, Vector dif) {
        if(moving) {
            locate(position().getSum(pos.getDif(movePos)));
            drawParent();
        }
        if(!child.retainFocus()) {
            boolean previous = active; //Stores the previous active state of the child
            active = child.containsPoint(pos); //Update whether child Module is active
            if(previous != active) {
                if(active) { child.onFocus(); } //Update the focus if it changed
                else { child.onUnfocus(); }
            }
            
            if(!active) { //If the child is not active, check for the various controls of the WorkTool
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
                
                if((pos.x > BORDER_WIDTH && pos.x < width() - BORDER_WIDTH) || (pos.y > BORDER_WIDTH && pos.y < height() - BORDER_WIDTH)) {
                    moveable = true;
                }
                else {
                    if(moveable) {
                        moveable = false;
                        redraw();
                    }
                }
            }
        }
        if(active) { //If the child is active, it should be updated no matter what
            child.mouseMove(pos.getDif(child.position()), dif.getDif(child.position()));
        }

    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(active) {
            child.mouseDown(e);
        }
        else if(closable) {
            workspace.removeChild(this);
        }
        else if(moveable) {
            workspace.beginMoving(this);
            moving = true;
            keepFocus();
            movePos = mousePosition();
            startPos = position();
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(active) {
            child.mouseUp(e);
        }
        if(moving) {
            workspace.stopMoving();
            moving = false;
            releaseFocus();
        }
    }
    
    //Called by the parent Workspace when it is detected that this intersects with another module
    public void resetMovePosition() {
        locate(startPos);
    }
    
    @Override
    public void mouseClick(MouseEvent e) { child.mouseClick(e); }
    @Override
    public void mouseScroll(MouseWheelEvent e) { child.mouseScroll(e); }
    
    @Override
    public void keyDown(KeyEvent e) { child.keyDown(e); }
    @Override
    public void keyUp(KeyEvent e) { child.keyUp(e); }
    @Override
    public void keyType(KeyEvent e) { child.keyType(e); }
    
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