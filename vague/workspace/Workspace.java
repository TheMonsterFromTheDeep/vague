package vague.workspace;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import vague.Resources;
import module.util.Rectangle;
import vague.input.Controls;
import module.Module;
import module.Window;
import module.container.Container;
import module.paint.GraphicsHandle;
import module.util.Vector;

/**
 * The Workspace defines a container in which the user can create and move small containers which
 * contain Modules.
 * @author TheMonsterFromTheDeep
 */
public final class Workspace extends Module {
    public static final Color TOOL_BORDER_COLOR = new Color(0xbbbbdd); //Colors used when drawing the WorkTools
    public static final Color TOOL_FILL_COLOR = new Color(0xc3c3dd);
    public static final Color BAD_TOOL_BORDER_COLOR = new Color(0xff5858);
    public static final Color BAD_TOOL_FILL_COLOR = new Color(0xef7d7d);
    
    public static int MIN_SIZE = 60; //Stores the minimum creatable size of WorkTool
    
    private static final int BG_WIDTH = 800; //Width/height of bgimage
    private static final int BG_HEIGHT = 600;
    
    private Vector toolStart; //Used when the Workspace is creating a tool - toolStart is anchored and toolEnd
    private Vector toolEnd;   //moves to match the mouse
    
    private boolean createTool; //Stores whether the Workspace is currently being used to create a tool
    //NOTE: May be changed in the future to a integer which stores the function that the Container is
    //currently performing.
    
    private WorkTool moveTool; //Stores any child Module that is being moved. Used in order to draw them specially.
    
    private BufferedImage workspace; //Stores a secondary buffer of the current workspace
    //such that it does not have to be re-drawn every time new tools are created
    
    public static final int GRID_SIZE = 40; //This size of the grid that WorkTools shoudl snap to when snapping them to grid
                                            //(may be dynamic in future)
    
    private WorkTool[] children;
    
    int activeIndex; //Similar to the Container's activeIndex, stores the index in 'children' of the active child Module. -1 if there is none.
    WorkTool activeChild; //Similar to the Container's activeChild, stores a handle to the active child Module.
    
    private Workspace(Window w, int width, int height) {
        super(w, width, height);
        
        workspace = getValidBuffer(size());
        
        children = new WorkTool[0];
        
        clearActiveChild();
        
        this.bgColor = new Color(0xcfcfcf);
    }
    
    //Conform to Module.create() syntax
    public static Workspace create(Window w, int width, int height, Module[] children) {
        return new Workspace(w, width, height);
    }
    
    /**
     * Adds a new child to the Container.
     * @param m The child to add to the Container.
     */
    protected final void addChild(WorkTool m) {
        m.setParent(this); //Set the parent of the child
        
        WorkTool[] tmp = children; //Expand the children array and add the new child.
        children = new WorkTool[children.length + 1];
        System.arraycopy(tmp, 0, children, 0, tmp.length); //Copy over the old values so they will be retained.
        children[tmp.length] = m;    
    }
    
    /**
     * Clears the active child of the Container. The Container will have an activeIndex of -1, meaning it has no active child,
     * and will act upon a dummy child as its active child.
     */
    protected final void clearActiveChild() {
        activeIndex = -1; //An activeIndex of -1 indicates there is no active child
        activeChild = null; //Set the activeChild to a dummy Module so nothing bad happens
    }
    
    /**
     * Sets the active child of the Container to the specified child. All operations to the active child will now apply to this
     * child.
     * @param index The index of the child to become active.
     */
    protected final void setActiveChild(int index) {
        activeIndex = index; //Set the active index of the child.
        activeChild = children[index]; //Set the active child to the child at the specified index.
    }
    
    /**
     * Removes the specified child module from the Container. Should ONLY be called by child modules / the
     * container itself in order to remove themselves / children.
     * @param m The Module to remove.
     */
    public final void removeChild(WorkTool m) {
        WorkTool[] tmp = new WorkTool[children.length - 1]; //Creates a temporary array to hold the children without the child to delete
        int i = 0; //Iterate through the children before the removed child and copy them over
        while(children[i] != m && i < children.length) {
            tmp[i] = children[i];
            i++;
        }
        if(i == activeIndex) { clearActiveChild(); } //If the removed child was active, then the child needs to be cleared, because
                                                     //no child should then be active
        i++;
        while(i < children.length) { //Iterate through the children after the removed child and copy them over
            tmp[i - 1] = children[i];
            i++;
        }
        children = tmp; //Set the children of this Container to the array without the removed child
        
        repaint(); //The container will likely have a changed graphical state
    }
    
    @Override
    public void onResize(Vector newSize) {
        //When the Workspace is resized, it needs to update its 'workspace' buffer because that buffer
        //needs to be as big as the module
        workspace = getValidBuffer(newSize);
        
        //it does not have to worry about copying over the old data because the workspace only needs to be
        //used when a WorkTool is being moved / resized, and if that is happening then Workspace cannot
        //be resized by the user under any normal circumstances.
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(activeIndex == -1) { //If there is no active tool, mouse down creates new tools
            createTool = true; //Set the tool creation flag so that the mouseMove() method will do tool creation
            toolStart = new Vector(mousePosition()); //Copy the mousePosition into both the toolStart and toolEnd
            toolEnd = new Vector(toolStart); //both are equal to mousePosition because there is no other info yet
            
            
            repaint();
            updateBuffer();
            //draw(); //Draw to update the workspace buffer
        }
        else { //If there is an active tool (activeIndex > -1), then it should be updated
            activeChild.mouseDown(e);
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(activeIndex == -1) { //If the activeIndex is -1, a tool may be being created
            if(createTool) { //If a tool was being created:
                createTool = false; //update createTool so mouseMove() will behave properly
                createTool(); //Create the new tool child and add it to the Workspace
            }
        }
        else { //If there is an active tool (activeIndex > -1), then that tool should be updated
            activeChild.mouseUp(e);
        }      
    }
    
    @Override
    public void mouseScroll(MouseWheelEvent e) {
        if(activeIndex != -1) {
            activeChild.mouseScroll(e);
        }
    }
    
    @Override
    public void keyDown() {
        if(activeIndex != -1) { //If there is an active tool (activeIndex > -1), then it's key down method should be called
            activeChild.keyDown();
        }
    }
    
    @Override
    public void keyUp() {
        if(activeIndex != -1) { //If there is an active tool (activeIndex > -1), then it's key up method should be updated
            activeChild.keyUp();
        }
    }
    
    /**
     * Called by child modules when they are being moved or resized. Alerts the Workspace to draw them differently
     * than other modules.
     * @param m 
     */
    public void beginChanging(WorkTool m) {
        moveTool = m; //If a Module is moving, it needs to be treated specially - special treatment
                      //is given to the module stored within moveTool
        repaint(); //The module needs to be re-drawn so that the 'workspace' buffer can be updated
        updateBuffer();
    }
    
    public void stopMoving() {
        for(WorkTool wt : children) { //Reset the position of the moved tool if it intersects any other tools
            if(wt != moveTool) { //If the module is not the moveTool, check for intersection
                if(wt.intersects(moveTool)) {
                    moveTool.resetMovePosition(); //If the tool being moved is intersecting others, it should be reset
                }
            }
        }
        moveTool = null; //Nullify the move tool so no tool will be given special graphical / other treatment
        repaint(); //Redraw the module because tools have been updated
        updateBuffer();
    }
    
    public void stopResizing() {
        if(!validSize(moveTool)) {
            moveTool.resetResize();
        }
        moveTool = null;
        repaint();
        updateBuffer();
    }
    
    public boolean validPosition(WorkTool t) {
        boolean valid = true;
        for(WorkTool wt : children) {
            if(wt != t) {
                if(t.intersects(wt)) { valid = false; }
            }
        }
        return valid;
    }
    
    public boolean validSize(WorkTool t) {
        return t.width() >= MIN_SIZE && t.height() >= MIN_SIZE && validPosition(t);
    }
           
    public void createTool() {
        if(Controls.bank.status(Controls.WORKSPACE_GRID_SNAP)) { //If the snapping control is activated, the new tool needs to be snapped to grid
            toolStart.snap(GRID_SIZE); //Snaps the tool start to grid
            toolEnd.snap(GRID_SIZE); //Snaps the tool end to grid
        }
        WorkTool newTool = WorkTool.create(getHandle(), toolStart,toolEnd); //Create the new tool
        /*
        Check if new tool is legitimate - this requires two checks:
        - The new tool has a size that is at least the minimum size
        - The new tool does not intersect any currently existing tools
        */
        if(newTool.width() >= MIN_SIZE && newTool.height() >= MIN_SIZE) {
            boolean create = true; //Stores whether the new tool should be added
            for(WorkTool wt : children) {
                if(newTool.intersects(wt)) { create = false; } //If the new tool intersects an existing tool, it should not be created
            }
            if(create) { //Create is only true if the new tool did not intersect any other tools
                newTool.setWorkspace(this); //Allow the new WorkTool to use move and resize functions
                addChild(newTool); //Add the new tool as a child
                //children[children.length - 1].draw(); //if a child was added, it needs to be drawn initially
            }
        }
        repaint(); //If createTool, the Workspace always needs to be re-drawn because even if a tool
                  //wasn't created, the red square needs to be un-drawn
    }
    
    private void drawTool() {
        ///argh this wording tho
        GraphicsHandle handle = beginDraw();
        handle.drawImage(workspace,0,0,null); //Draw the current buffer of the workspace

        Vector start = new Vector(), size = new Vector(); //Store where the being created tool should be drawn
        /* WORKTOOL VECTOR ORGANIZATION
        Vector start - stores the start draw position of the tool
        Vector size - stores the size of the tool being draw

        The start will consist of the lesser x and y out of toolStart and toolEnd.
        The size will consist of the size required to draw based on this - it will
          consist of the difference between the greater coordinate and the lesser
          coordinate becauase that is the size of the tool.
        */
        if(toolStart.x < toolEnd.x) {
            start.x = toolStart.x;
            size.x = toolEnd.x - toolStart.x;
        }
        else {
            start.x = toolEnd.x;
            size.x = toolStart.x - toolEnd.x;
        }

        if(toolStart.y < toolEnd.y) {
            start.y = toolStart.y;
            size.y = toolEnd.y - toolStart.y;
        }
        else {
            start.y = toolEnd.y;
            size.y = toolStart.y - toolEnd.y;
        }

        /*
        How snapping works with new tools -

        The toolStart and toolEnd are not themselves snapped because
          if the snapping control becomes inactive, toolStart and toolEnd
          will need to revert to what they were before snapping started.
          When drawing the tools, however, if they are being snapped, the
          Vectors used for drawing ('start' and 'size') do need to be snapped
          such that an accurate picture of the new tool is drawn.

        If the snapping control continues to be active when the tool is actually
          created, then both toolStart and toolEnd will be snapped because they had
          not been snapped previously.
        */
        if(Controls.bank.status(Controls.WORKSPACE_GRID_SNAP)) {
            start.snap(GRID_SIZE); //Snap the drawing Vectors if the snapping control is active
            size.snap(GRID_SIZE);
        }

        /*
        The drawn rectangle should appear blue if the tool is valid and red 
          if the tool is invalid. The only cases where the tool is invalid
          (currently) are if it is smaller than the minimum size or if it
          intersects a currently existing tool. Therefore, before drawaing the
          tools, it is checked if they are valid to present a real-time picture
          to the user if the tool is valid or not.
        */

        boolean valid = true; //If true, the tool is valid and is drawn blue; otherwise, it is invalid and drawn red (and not created when the mouse is released.)
        if (size.x >= MIN_SIZE && size.y >= MIN_SIZE) { //Checks to see if the tool is of a valid size
            Rectangle validator = new Rectangle(start,size); //Create a rectangle with the potential bounds to check for intersections
            for(WorkTool wt : children) { //Check to see if the bounds of the potential tool intersect any existing tools
                if(wt.intersects(validator)) {
                    valid = false; //if the bounds intersect and existing tool, the tool should be drawn as red
                }
            }
        }
        else {
            valid = false; //If the tool is not of a valid size, it should be drawn red
        }

        handle.setColor(valid ? TOOL_FILL_COLOR : BAD_TOOL_FILL_COLOR); //Set the fill color of the tool based on whether the tool is valid or not
        handle.fillRect(start.x + 1,start.y + 1,size.x - 2,size.y - 2); //Fill the projected bounds of the created tool
        handle.setColor(valid ? TOOL_BORDER_COLOR : BAD_TOOL_BORDER_COLOR); //Set the border color based on whether the tool is valid or not
        handle.drawRect(start.x, start.y, size.x - 1, size.y - 1); //Border in the projected bounds of the created tool
        endDraw(handle);
    }
    
    private void updateActiveChild(Vector mousePosition) {
        boolean updated = false; //Stores whether a new active Module was discovered
        int i = 0; //The index currently being checked for active-ness
        while(!updated && i < children.length) { //Iterate through child Modules to see if any are active
            if(children[i].containsPoint(mousePosition)) { //If the Module contains the mouse, it should be active
                updated = true; //A Module has been made active
                setActiveChild(i); //Set the active child
                children[i].onFocus(); //The child has been focused, so call its onFocus method
            }                
            i++; //Update i so that all children will be iterated through
        }
        if(!updated) { //If the active child hasn't been updated, then it should be cleared
            clearActiveChild();
        }
    }
    
    @Override
    public void mouseMove(Vector pos, Vector dif) {
        if(activeIndex > -1) { //If the activeIndex is greater than 1, the active Module needs to be checked for
                               //activeness retaint and if so, updated with its own mouseMove()
            if(!activeChild.retainFocus()) { //If the child is retaining focus, there is no point in checking if it is no longer
                                             //active, because it wants to retain focus even if the mouse is no longer inside its bounds.
                if (!activeChild.containsPoint(pos) || !activeChild.visible()) { //if the child is not retaining focus, check to make sure that
                                                                                 //that child should still have focus
                    activeChild.onUnfocus(); //If the child is no longer focused, it's onUnfocus() method should be called
                    updateActiveChild(pos); //Update the active child in case a different one is now active
                }
            }
            //Pass mouse coordinates onto child module but where the coordinates passed will have an origin
            //at the top left corner of the child module
            if(activeIndex > -1) {
                activeChild.mouseMove(pos.getDif(activeChild.position()),dif); //Pass the mouse movement on to the active child (or the new active child, if it was just updated)
            }
        }
        else if(createTool) { //If there is not an active child, but tools are being created, nothing other than tool creation should be happending
            if(Controls.bank.status(Controls.WORKSPACE_SQUARE_TOOL)) { //If the square tool control is active, make the tool square
                toolEnd = new Vector(pos.x,pos.x + (toolStart.y - toolStart.x)); //Right now, squaring the tool is done using the x coordinate
                ////TODO: Make much better square tool creation algorithm (need to consider the most user-friendly way to do so)
            }
            else {
                toolEnd = pos; //The toolEnd is updated the mouse position and the toolStart remains anchored
            }

            drawTool(); //Draw the tool that is potentialaly going to be created
        }
        else { //If absolutely nothing else is happening, update the active child because the mouse may have moved into focus of a child module
            updateActiveChild(pos);
        }
    }
    
    private void updateBuffer() {
        //As explained under 'WORKSPACE' BUFFER EXPLANATION, the static children are drawn to the 'workspace' buffer
        //as the Workspace takes up the entire screen, this can be done just by getting the screen buffer
        workspace = getHandle().getBuffer();
    }
    
    @Override
    public void paint(GraphicsHandle graphics) {
        //this.fillBackground(); //Fill the background with color just in case
        
        for(int x = 0; x < width(); x+= BG_WIDTH) { //Draw the tiled grey stone background image
            for(int y = 0; y < height(); y+= BG_HEIGHT) {
                graphics.drawImage(Resources.bank.BACKGROUND, x, y);
            }
        }
        /* 'WORKSPACE' BUFFER EXPLANATION
        All child children except for one currently being moved are drawn because this Workspace's own
          buffer is then drawn to the 'workspace' buffer, which is a buffer of all Modules not being 
          moved / resized. Doing it like this cuts down on the amount of graphical operations greatly
          because instead of drawing all children to both buffers, all static children can be drawn to the Module
          buffer and then that buffer can be drawn to the 'workspace' buffer, containing all the static
          children but not the dynamic child. The dynamaic child is then drawn onto this buffer because it
          needs to, just like every other child.
        */
        for(WorkTool wt : children) {
            if(wt != moveTool) {
                wt.repaint();
            }
        }
        
        
        
        if(moveTool != null) { //If a tool is being moved, it should not be drawn to the static buffer, but does need to be drawn to the normal
                               //graphics buffer
            moveTool.repaint();
        }
    }
}