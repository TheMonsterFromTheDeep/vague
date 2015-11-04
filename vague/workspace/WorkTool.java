package vague.workspace;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import vague.input.Controls;
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
    static final Color BORDER_COLOR = new Color(0); //The color of the actual border
    
    static final Color DISMISS_COLOR = new Color(0xe74f4f); //Stores the colors of the button which dismisses a WorkTool
    static final Color DISMISS_COLOR_HIGH = new Color(0xf96666); //The dismiss button when the mouse is over it
    
    static final int INSET_WIDTH = 20; //Stores the width of the insets between the black border and the child module
    
    /*
    The action integers are used so that the WorkTool can keep track of the actions it is taking and the
      actions it is going to take without much difficulty. nextAction stores the next action the WorkTool
      is going to take; so, for example, if its nextAction is ACTION_CLOSE, then if the mouse is pressed,
      the WorkTool should be dismissed. action stores the action that the WorkTool *is* taking; so, for
      example, if action is ACTION_MOVE, then the WorkTool is being moved around the Workspace and should
      not be updating its child or anything.
    */   
    static final byte ACTION_NONE = 0; //The WorkTool is taking no particular action
    static final byte ACTION_CHILD = 1; //The WorkTool is communicating events to the child
    static final byte ACTION_CLOSE = 2; //The WorkTool will close
    static final byte ACTION_MOVE = 3; //The WorkTool is moving   
    /*
    NOTE: In the future, there may also be ACTION_CHILD, meaning simply that the child is active (the mouse is INSIDE
      the child / the child is retaining focus). This would replace the active boolean, making things slightly less
      cluttered.
    */
    
    private byte nextAction = ACTION_NONE; //Stores the next action that the WorkTool *can* take, based on mouse position
    private byte action = ACTION_NONE; //Stores the action that the WorkTool is currently taking
    
    private Vector movePos; //Stores the mouse position when the mouse starts moving
    private Vector startPos; //Stores the position to jump back to if a move fails
    
    private Module child; //The WorkTool contains a single Module child which does what it needs to do
    private boolean active; //Stores whether the child Module is being controlled by the user
    
    private Workspace workspace; //Stores a class reference to the Workspace that this WorkTool is a child of.
    //This is so the child can reference special methods of the Workspace (such as beginMoving()) without casting
    //its parent to a Workspace.
    
    private WorkTool(Vector start, Vector end) {
        bgColor = BG_COLOR; //Set the Module background color to the static background color
        
        /*
        This caluclates the position and size of the WorkTool similarly to how 'start' and
          'size' are calculated at Workspace/WORKTOOL VECTOR ORGANIZATION. The position ('pos')
          consists of the smallest x and y passed through the 'start' and 'end' Vectors. The 
          size is then calculated by subtracting the smaller x / y from the larger x / y.
        */
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
        
        initialize(pos,size); //Initialize the size and position of the WorkTool
    }
    
    public static WorkTool create(Vector start, Vector end) {
        WorkTool w = new WorkTool(start,end); //Create the WorkTool based on the start and end Vectors
        
        w.child = Module.create(); //This makes sure that the child module is initialized.
        
        //TEST PURPOSES ONLY:
        w.child = TestModule.create(0,0); //THIS IS USED TO TEST THE WorkTool AND IN THE FUTURE WILL NOT BE THERE
        //IN THE FUTURE, PLANS ARE TO HAVE A MENU APPEAR TO SELECT WHAT TYPE OF MODULE THIS WorkTool SHOULD REPRESENT
        //END TEST
        
        w.child.setParent(w); //Make sure there are no NullPointerExceptions thrown because of a loss of a parent     
        
        w.child.resize(w.width() - 2 * INSET_WIDTH, w.height() - 2 * INSET_WIDTH); //The borders around the child module are 20 px thick
        w.child.locate(INSET_WIDTH,INSET_WIDTH); //The child is located past the insets
        w.child.draw(); //Make sure to draw the child module
        return w;
    }
    
    @Override
    public void onResize(Vector newSize) {
        //When the WorkTool is resized, the child needs to be resized to match:
        //the child Module is resized to the size of the WorkTool minus the insets
        //on both sides, therefore 2 * the inset width is subtracted from each dimension.
        child.resize(newSize.x - 2 * INSET_WIDTH, newSize.y - 2 * INSET_WIDTH);
    }
    
    @Override
    public void onFocus() {
        bgColor = BG_COLOR_HIGH;
        redraw();
    }
    
    @Override
    public void onUnfocus() {
        bgColor = BG_COLOR;
        if(nextAction == ACTION_CLOSE) {
            nextAction = ACTION_NONE;
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
        if(action == ACTION_MOVE) {
            Vector newPos = position().getSum(pos.getDif(movePos));
            if(Controls.bank.status(Controls.WORKSPACE_GRID_SNAP)) {
                newPos.x = (newPos.x / Workspace.GRID_SIZE) * Workspace.GRID_SIZE; //If ctrl is down, snap to increments of Workspace.GRID_SIZE
                newPos.y = (newPos.y / Workspace.GRID_SIZE) * Workspace.GRID_SIZE;
            }
            locate(newPos);
            drawParent();
        }
        if(!child.retainFocus()) {
            boolean previous = active; //Stores the previous active state of the child
            active = child.containsPoint(pos); //Update whether child Module is active
            if(previous != active) {
                if(active) { nextAction = ACTION_CHILD; child.onFocus(); } //Update the focus if it changed
                else { child.onUnfocus(); }
            }
            
            if(!active) { //If the child is not active, check for the various controls of the WorkTool
                //Check if the WorkTool is closable using so-called "magic numbers" - this checks
                //to see if the mouse is in the top-right corner (within 20 pixels of it)
                if(pos.x > width() - INSET_WIDTH && pos.x < width() && pos.y > 0 && pos.y < INSET_WIDTH) {
                    nextAction = ACTION_CLOSE;
                    redraw();
                }               
                else if((pos.x > INSET_WIDTH && pos.x < width() - INSET_WIDTH) || (pos.y > INSET_WIDTH && pos.y < height() - INSET_WIDTH)) {
                    nextAction = ACTION_MOVE;
                    redraw();
                }
                else {
                    if(nextAction != ACTION_NONE) {
                        nextAction = ACTION_NONE;
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
        else if(nextAction == ACTION_CLOSE) {
            workspace.removeChild(this);
        }
        else if(nextAction == ACTION_MOVE) {
            workspace.beginMoving(this);
            action = ACTION_MOVE;
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
        if(action == ACTION_MOVE) {
            workspace.stopMoving();
            action = ACTION_NONE;
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
    public void keyDown() {
        child.keyDown(); 
    }
    @Override
    public void keyUp() {
        child.keyUp(); 
    }
    @Override
    public void keyType(KeyEvent e) { child.keyType(e); }
    
    @Override
    public void draw() {     
        graphics.setColor(bgColor);
        graphics.fillRect(1, 1, width() - 2, height() - 2);
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(0,0,width() - 1,height() - 1);
        graphics.setColor((nextAction == ACTION_CLOSE) ? DISMISS_COLOR_HIGH : DISMISS_COLOR); //Set the dismiss color based on whether the Module is closable
        graphics.fillRect(width() - INSET_WIDTH, 0, INSET_WIDTH, INSET_WIDTH);
        
        graphics.drawImage(child.render(), INSET_WIDTH, INSET_WIDTH, null);
    }
}