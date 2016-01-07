package vague.workspace;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import vague.Resources;
import vague.input.Controls;
import module.Module;
import vague.module.TestModule;
import vague.util.Cursor;
import vague.util.Vector;
import vague.workspace.menu.SmartMenu;

/**
 * The WorkTool class defines a Module that can be created through the Workspace. It is fundamentally
 * different from a "Tool", which is a user-controlled device for image manipulation. (TODO: Make Tool class)
 * 
 * The user can create and edit WorkTool classes in order to customize the UI. It will be one of several different
 * container classes for controlling the user interface.
 * @author TheMonsterFromTheDeep
 */
public class WorkTool extends Module {
    static final Color BG_COLOR = new Color(0xbcbcdd); //The color of the WorkTool
    static final Color BORDER_COLOR = new Color(0); //The color of the actual border
    static final Color BORDER_COLOR_INACTIVE = new Color(0x5f5f5f);
    static final Color BORDER_COLOR_CHANGING = new Color(0xffffff); //The color of the WorkTool when it is being moved / resized
    
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
    
    In general, nextAction stores the action that will take place *once the mouse is clicked.* 'action' will
      then take the value of nextAction once the mouse is clicked. For ACTION_CLOSE, however, 'action' has
      no need to be updated, becauase the Module needs to be closed anyways.
    */   
    static final byte ACTION_NONE = 0; //The WorkTool is taking no particular action
    static final byte ACTION_CHILD = 1; //The WorkTool is communicating events to the child
    static final byte ACTION_CLOSE = 2; //The WorkTool will close
    static final byte ACTION_MOVE = 3; //The WorkTool is moving
    static final byte ACTION_RESIZE_TL = 4; //The WorkTool is being resized (by the top-left corner)
    static final byte ACTION_RESIZE_BL = 5; //The WorkTool is being resized (by the bottom-left corner)
    static final byte ACTION_RESIZE_BR = 6; //The WorkTool is being resized (by the bottom-right corner)
    static final byte ACTION_REFRESH = 7; //The WorkTool can be refreshed (a new control can be selected)
    /*
    NOTE: In the future, there may also be ACTION_CHILD, meaning simply that the child is active (the mouse is INSIDE
      the child / the child is retaining focus). This would replace the active boolean, making things slightly less
      cluttered.
    */
    
    private byte nextAction = ACTION_NONE; //Stores the next action that the WorkTool *can* take, based on mouse position
    private byte action = ACTION_NONE; //Stores the action that the WorkTool is currently taking
    
    private boolean retainAction = false; //Stores whether the WorkTool should avoid changing lastAction / action
    
    private boolean valid = true; //Used to store whether the WorkTool should display as invalid (when being resized / moved)
    
    private Vector anchorVec; //Stores the anchor mouse position for when the WorkTool is moved / resized
    private Vector startPos; //Stores the position to jump to if a move / resize fails
    private Vector startSize; //Stores the size to reset to if a resize fails
    
    private Module child; //The WorkTool contains a single Module child which does what it needs to do
    private boolean active; //Stores whether the child Module is being controlled by the user
    
    private Workspace workspace; //Stores a class reference to the Workspace that this WorkTool is a child of.
    //This is so the child can reference special methods of the Workspace (such as beginMoving()) without casting
    //its parent to a Workspace.
    
    private boolean focused; //Stores whether the WorkTool has focus
    
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
        //w.child = TestModule.create(0,0); //THIS IS USED TO TEST THE WorkTool AND IN THE FUTURE WILL NOT BE THERE
        //IN THE FUTURE, PLANS ARE TO HAVE A MENU APPEAR TO SELECT WHAT TYPE OF MODULE THIS WorkTool SHOULD REPRESENT
        //END TEST
        
        w.child = SmartMenu.create(w);
        
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
        focused = true;
        redraw();
    }
    
    @Override
    public void onUnfocus() {
        focused = false;
        nextAction = ACTION_NONE; //The WorkTool is taking no actions
        action = ACTION_NONE;
        //Changing nextAction will also change how the WorkTool is drawn if the mouse was over one of the controls
        redraw(); //Re-draw the WorkTool because it has changed graphical state
    }
    
    //Needs to be overridden to also includle whether the child is retaining focus
    //If the child is retaining focus, then this WorkTool needs to retain focus as well
    @Override
    public boolean retainFocus() { return retaining || child.retainFocus(); }
    
    /**
     * Sets the Workspace parent of the WorkTool for more efficient code.
     * @param w The new Workspace object to reference
     */
    public void setWorkspace(Workspace w) { this.workspace = w; }
    
    @Override
    public void mouseMove(Vector pos, Vector dif) {
        if(action == ACTION_MOVE) { //If the action is ACTION_MOVE, the Module needs to move
            //Gets the new position that the Module is moving to:
            //it adds the difference of the mouse position and the starting mouse position to
            //the current position
            Vector newPos = position().getSum(pos.getDif(anchorVec));
            
            //If the snapping control is active, then the move position should be snapped (increments of Workspace.GRID_SIZE)
            if(Controls.bank.status(Controls.WORKSPACE_GRID_SNAP)) {
                newPos.snap(Workspace.GRID_SIZE);
            }
            
            locate(newPos); //Locates the WorkTool at the new location
            
            valid = workspace.validPosition(this);
            drawBorder(); //Draw the border in case it's graphical state has changed
            
            drawParent(); //Re-draws its parent (if it's parent is a Workspace, then it will know to draw this Module specially using a buffer for all the others alone)
        }
        if(action == ACTION_RESIZE_TL) {
            Vector newCorner = position().getSum(pos.getDif(anchorVec)); //The new corner is the upper left (so, raw position)
            
            if(Controls.bank.status(Controls.WORKSPACE_GRID_SNAP)) {
                newCorner.snap(Workspace.GRID_SIZE);
            }
            
            Vector newSize = bounds().bottomRight().getDif(newCorner); //This stores the new size based on that corner
            
            if(Controls.bank.status(Controls.WORKSPACE_GRID_SNAP)) {           
                newSize.snap(Workspace.GRID_SIZE);
            }
            
            locate(newCorner);
            resize(newSize);
            
            valid = workspace.validSize(this);
            drawBorder(); //Draw the border in case it's graphical state has changed
            
            drawParent();
        }
        if(action == ACTION_RESIZE_BL) {
            Vector newCorner = bounds().bottomLeft().getSum(pos.getDif(anchorVec)); //The new bottom-left corner
            
            Vector newPosition = new Vector(newCorner.x,y());
            
            if(Controls.bank.status(Controls.WORKSPACE_GRID_SNAP)) {
                newPosition.snap(Workspace.GRID_SIZE);
            }
            
            Vector newSize = new Vector(bounds().bottomRight().x - newPosition.x, pos.y);
            
            if(Controls.bank.status(Controls.WORKSPACE_GRID_SNAP)) {           
                newSize.snap(Workspace.GRID_SIZE);
            }
            
            locate(newPosition);
            resize(newSize);
            
            valid = workspace.validSize(this);
            drawBorder(); //Draw the border in case it's graphical state has changed
            
            drawParent();
        }
        if(action == ACTION_RESIZE_BR) {
            Vector newPosition = position();
            
            Vector newSize = new Vector(pos.x, pos.y);
            
            if(Controls.bank.status(Controls.WORKSPACE_GRID_SNAP)) {
                newPosition.snap(Workspace.GRID_SIZE);
                newSize.snap(Workspace.GRID_SIZE);
            }
            
            locate(newPosition);
            resize(newSize);
            
            valid = workspace.validSize(this);
            drawBorder(); //Draw the border in case it's graphical state has changed
            
            drawParent();
        }
        if(!child.retainFocus()) { //If the child is not retaining focus, check for various updates in the child's state
            boolean previous = active; //Stores the previous active state of the child
            active = child.containsPoint(pos); //Update whether child Module is active
            if(previous != active) {
                if(active) {
                    if(!retainAction) {
                        nextAction = ACTION_CHILD; 
                        child.onFocus();
                        redraw();  //Re-draw in case the graphical state of another control changed
                    }
                } //Update the focus if it changed
                else { child.onUnfocus(); } //If the child is no longer active, it's focus is lost
            }
            
            if(!active) { //If the child is not active, check for the various controls of the WorkTool
                /*
                In order to check for the actions of the WorkTool, multiple positions are considered:
                - Within the top-right corner of the insets, with the same width and height as INSET_WIDTH, lies
                  the dismiss button. Therefore, if the mouse is located here, the module becomes dismissable (ACTION_CLOSE).
                - The areas between the corners, when clicked, allow the user to drag the WorkTool around. Therefore,
                  if the mouse is in one of these areas, the next action becomes ACTION_MOVE, and once the mouse is
                  clicked, 'action' itself will become ACTION_MOVE.
                - The INSET_WIDTH square area right next to the close button contains the refresh button, which causes
                  the WorkTool to fill itself with a new SmartMenu so a new control can be selected.
                - If the mouse is in any of the corners other than the close button, the WorkTool can be resized. In the future,
                  the close / refresh buttons may be moved over and a new resize button located where the close button currently
                  lies.
                */
                if(!retainAction) {
                    int oldAction = nextAction;
                    
                    if(pos.y <= INSET_WIDTH) {
                        if(pos.x > INSET_WIDTH) {
                            if(pos.x >= width() - (2 * INSET_WIDTH)) {
                                if(pos.x >= width() - INSET_WIDTH) {
                                    nextAction = ACTION_CLOSE;
                                }
                                else {
                                    nextAction = ACTION_REFRESH;
                                }
                            }
                            else {
                                nextAction = ACTION_MOVE;
                            }
                        }
                        else {
                            nextAction = ACTION_RESIZE_TL;
                        }
                    }
                    else if(pos.y >= height() - INSET_WIDTH) {
                        if(pos.x > INSET_WIDTH) {
                            if(pos.x >= width() - INSET_WIDTH) {
                                nextAction = ACTION_RESIZE_BR;
                            }
                            else {
                                nextAction = ACTION_MOVE;
                            }
                        }
                        else {
                            nextAction = ACTION_RESIZE_BL;
                        }
                    }
                    else {
                        if(pos.x <= INSET_WIDTH || pos.x >= width() - INSET_WIDTH) {
                            nextAction = ACTION_MOVE;
                        }
                        else {
                            nextAction = ACTION_CHILD;
                        }
                    }
                    
                    if(oldAction != nextAction) {
                        redraw();
                    }
                }
            }
        }
        if(active) { //If the child is active, it should be updated no matter what
            //Pass the mouseMove method onto the child with the updated position
            child.mouseMove(pos.getDif(child.position()), dif);
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(active) { //if the child is active, it's mouseDown event should be called
            child.mouseDown(e);
        }
        else if(nextAction == ACTION_CLOSE) {
            workspace.removeChild(this); //If the nextAction was ACTION_CLOSE, this WorkTool needs to be dismissed
        }
        else if(nextAction == ACTION_REFRESH) {
            this.fill(SmartMenu.create(this));
        }
        else if(nextAction == ACTION_MOVE) { //If the nextAction is ACTION_MOVE, start moving the WorkTool
            startPos = position(); //The start position, used to reset the WorkTool's position if it is moved invalidly,
                                   //needs to be set so that it can be reset if necessary
            workspace.beginChanging(this); //Tell the Workspace parent to begin moving so this Module's graphical
                                         //state will be updated correctly
            action = ACTION_MOVE; //Set the action to ACTION_MOVE so that mouseMove() will work correctly
            keepFocus(); //Start keeping focus, because the mouse is going to leave the WorkTool but it needs
                         //to continue moving
            anchorVec = mousePosition(); //The move position (the position that stores FROM WHERE the module is being dragged
                                       //needs to be initalized so that the WorkTool will move logically to the user)
            
        }
        else if(nextAction == ACTION_RESIZE_TL || nextAction == ACTION_RESIZE_BL || nextAction == ACTION_RESIZE_BR) {
            startPos = position();
            startSize = size();
            
            workspace.beginChanging(this);
            
            action = nextAction;
            keepFocus();
            retainAction = true;
            
            anchorVec = mousePosition();
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(active) { //If the child is active, the mouseUp event needs to be passed on down
            child.mouseUp(e);
        }
        if(action == ACTION_MOVE) { //If the WorkTool is currently moving (*action* is ACTION_MOVE),
                                    //then when the mouse is lifted it needs to stop moving
            workspace.stopMoving(); //Tell the parent to stop treating this WorkTool specially because it no longer
                                    //needs special treatment for movement
            action = ACTION_NONE; //The action is ACTION_NONE, as although it is *possible* for the WorkTool to move,
                                  //it is not *actually happening*.
            releaseFocus(); //Stop retaining focus because it is no longer necessary
            /*
            NOTE: releaseFocus() will not cause problems, because although something else *could* need to retain focus,
            it couldn't have possibly been updated while this finished executing.
            */
            
            valid = true;          
            redraw();
        }
        if(nextAction == ACTION_RESIZE_TL || nextAction == ACTION_RESIZE_BL || nextAction == ACTION_RESIZE_BR) {        
            workspace.stopResizing();
            action = ACTION_NONE;
            
            releaseFocus();
            retainAction = false;
            
            valid = true;
            
            redraw();
        }
    }
    
    //Called by the parent Workspace when it is detected that this intersects with another module
    public void resetMovePosition() {
        locate(startPos); //Change the position back to the position stored before the Module began moving
    }
    
     //Called by the parent Workspace when it detects that this Module is resized incorrectly
    public void resetResize() {
        locate(startPos); //Change the position back to the position stored before the Module began moving
        resize(startSize);
    }
    
    /**
     * Changes the child of the WorkTool to the specified Module. Should be called by SmartMenu
     * when the user is specifying what this WorkTool should represent.
     * @param m The new Module to become the child.
     */
    public void fill(Module m) {
        this.child = m;
        child.setParent(this); //Set the parent of the child so it can call drawParent() and other methods
        //Resize the new child Module to fill the WorkTool
        child.resize(width() - 2 * INSET_WIDTH, height() - 2 * INSET_WIDTH);
        child.locate(INSET_WIDTH, INSET_WIDTH); //Locate the child inside the insets
        child.draw(); //Set initial graphical state of child
        redraw(); //Update WorkTool's graphical state to include that of the new child
    }
    
    /*
    These mouse events, not used by WorkTool, need to be passed on down the event chain
    */
    @Override
    public void mouseClick(MouseEvent e) { child.mouseClick(e); }
    @Override
    public void mouseScroll(MouseWheelEvent e) { child.mouseScroll(e); }
    
    /*
    Key methods, not used by this tool through these methods (rather through the Control bank), need to
      be passed down the event chain
    */
    @Override
    public void keyDown() { child.keyDown(); }
    @Override
    public void keyUp() { child.keyUp(); }
    @Override
    public void keyType(KeyEvent e) { child.keyType(e); }
       
    //Draws the border of the Module; called to update graphical state in move / resize classes
    private void drawBorder() {           
        graphics.setColor(
                (action != ACTION_NONE) 
                ? (valid)
                    ? BORDER_COLOR_CHANGING 
                    : Workspace.BAD_TOOL_BORDER_COLOR
                : (focused) ?
                    BORDER_COLOR
                    : BORDER_COLOR_INACTIVE);
        graphics.drawRect(0,0,width() - 1,height() - 1);
    }
    
    @Override
    public void draw() {     
        graphics.setColor(bgColor);  //Fill the background color of the WorkTool; mostly used for insets
        graphics.fillRect(1, 1, width() - 2, height() - 2);
        drawBorder();
        graphics.setColor((nextAction == ACTION_CLOSE) ? DISMISS_COLOR_HIGH : DISMISS_COLOR); //Set the dismiss color based on whether the Module is closable
        graphics.fillRect(width() - INSET_WIDTH, 0, INSET_WIDTH, INSET_WIDTH);
        
        //Draw the resize controls
        graphics.drawImage(
                (nextAction == ACTION_RESIZE_TL || action == ACTION_RESIZE_TL) 
                ? Resources.bank.WORKTOOL_RESIZE_TL_HIGH
                : Resources.bank.WORKTOOL_RESIZE_TL,
                0,
                0, null);
        graphics.drawImage(
                (nextAction == ACTION_RESIZE_BL || action == ACTION_RESIZE_BL) 
                ? Resources.bank.WORKTOOL_RESIZE_BL_HIGH
                : Resources.bank.WORKTOOL_RESIZE_BL,
                0,
                height() - INSET_WIDTH, null);
        graphics.drawImage(
                (nextAction == ACTION_RESIZE_BR || action == ACTION_RESIZE_BR) 
                ? Resources.bank.WORKTOOL_RESIZE_BR_HIGH
                : Resources.bank.WORKTOOL_RESIZE_BR,
                width() - INSET_WIDTH,
                height() - INSET_WIDTH, null);
        graphics.drawImage(
                (nextAction == ACTION_REFRESH)
                ? Resources.bank.WORKTOOL_REFRESH_HIGH
                : Resources.bank.WORKTOOL_REFRESH,
                width() - (2 * INSET_WIDTH),
                0, null);
        
        graphics.drawImage(child.render(), INSET_WIDTH, INSET_WIDTH, null); //Draw the child module at its position (right inside the insets)
    }
}