package module.container;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import module.Module;
import module.Window;
import module.paint.GraphicsHandle;
import module.util.Vector;

/**
 * The Container class is a basic Module which contains other Modules.
 * 
 * In the style of a highly controllable UI, most Modules will be in Containers
 * which are controllable and splittable by the user.
 * @author TheMonsterFromTheDeep
 */
public class Container extends Module {
    protected Module[] children; //Stores the child Modules of the container
    protected Module activeChild; //Stores a reference to the active child
    protected int activeIndex; //Stores the index of the active child: equal to -1 if there is no active child
    
    protected Container(Module[] children) {
        if(children.length < 1) {
            this.children = new Module[0]; //If there are no children, then there are no children
            clearActiveChild(); //Clear the active child because there can't be an active child
            initialize(new Vector(),new Vector()); //If there is no child modules, there is no reason for this to have any size
        }
        else {
            this.children = children;
            setActiveChild(0); //Set an active child so that nothing weird happens
            int width = 0; //Initialize the width and height to zero because there is no guarantee the child modules take up space
            int height = 0;
            for(int i = 0; i < children.length; i++) { //This calculates the smallest size  needed to hold the child modules
                if(children[i].right() > width) { //It checks to see if a child module does not fit inside the current size
                    width = children[i].right();  //If a child does not fit, the size needs to be expanded to fit that child
                }
                if(children[i].bottom() > height) {
                    height = children[i].bottom();
                }
            }
            //Initialize the module to zero,zero with the calculate size
            initialize(new Vector(),new Vector(width,height));
        }
    }
    
    protected Container(int width, int height, Module[] children) {
        initialize(new Vector(0,0), new Vector(width,height)); //Initialize the Container with the given width and height (children's height does not matter; they can go outside Container)
        if(children.length < 1) {
            this.children = new Module[0]; //If there are no children, then there are no children
            clearActiveChild(); //Clear the active child because there can't be an active child
        }
        else {
            this.children = children; //Set the children of this Container to the given array of Modules
            setActiveChild(0); //Set an active child so that nothing weird happens
        }
    }
    
    //Conforms to the Module.create() format - also used to safely pass an object after it is done construction
    public static Container create(Module[] children) {
        Container c = new Container(children);
        
        for(int i = 0; i < c.children.length; i++) { //Set the parent of all child modules so that they function properly
            c.children[i].setParent(c);
            c.children[i].sizedata.update(c.children[i].width(), c.width(), c.children[i].height(), c.height()); //Generate data for child modules so they can be resized / repoisitoned correctly
            c.children[i].posdata.update(c.children[i].x(), c.width(), c.children[i].y(), c.height());
        }
        
        return c;
    }
    
    //Conforms to the Module.create() format
    public static Container create(int width, int height, Module[] children) {
        Container c = new Container(width,height,children);
        
        for(int i = 0; i < c.children.length; i++) { //Set the parent of all child modules
            c.children[i].setParent(c);
            c.children[i].sizedata.update(c.children[i].width(), c.width(), c.children[i].height(), c.height());
            c.children[i].posdata.update(c.children[i].x(), c.width(), c.children[i].y(), c.height());
        }
        
        return c;
    }
    
    /**
     * Makes sure that every child object has a useful percentage. This is not guaranteed to update every
     * percentage object to the version it needs to be.
     */
    protected final void synchronizePercents() {
        for(int i = 0; i < children.length; i++) { //Iterate through all children and update them if they do not have a useful percentage
            if(!children[i].sizedata.initialized) { //If the child does not have any size data yet, create it
                children[i].sizedata.update(children[i].width(), width(), children[i].height(), height());
            }
            if(!children[i].posdata.initialized) { //If the child does not have any position data yet, create it
                children[i].posdata.update(children[i].x(), width(), children[i].y(), height());
            }
        }
    }
    
    /**
     * Recalculates the percentage for EVERY Module.
     * 
     * Will lose precision if the size / position of a Module is smaller than it was originally.
     */
    protected final void updateAllPercents() {
        for(int i = 0; i < children.length; i++) { //Loop throught all the children and re-generate their size/position percentage metadata.
            children[i].sizedata.update(children[i].width(), width(), children[i].height(), height());
            children[i].posdata.update(children[i].x(), width(), children[i].y(), height());
        }
    }
    
    /**
     * Recalculates the percentage for a single specified child, without losing data
     * for any of the other child Modules.
     * @param index The index of the child to update.
     */
    protected final void updatePercent(int index) {
        //Re-generate the position and size metadata for the child with the specified index.
        children[index].sizedata.update(children[index].width(), width(), children[index].height(), height());
        children[index].posdata.update(children[index].x(), width(), children[index].y(), height());
    }
    
    /**
     * Clears the active child of the Container. The Container will have an activeIndex of -1, meaning it has no active child,
     * and will act upon a dummy child as its active child.
     */
    protected final void clearActiveChild() {
        activeIndex = -1; //An activeIndex of -1 indicates there is no active child
        activeChild = Module.create(); //Set the activeChild to a dummy Module so nothing bad happens
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
     * Adds a new child to the Container.
     * @param m The child to add to the Container.
     */
    protected final void addChild(Module m) {
        m.setParent(this); //Set the parent of the child
        
        Module[] tmp = children; //Expand the children array and add the new child.
        children = new Module[children.length + 1];
        System.arraycopy(tmp, 0, children, 0, tmp.length); //Copy over the old values so they will be retained.
        children[tmp.length] = m;    
    }
    
    /**
     * Removes the specified child module from the Container. Should ONLY be called by child modules / the
     * container itself in order to remove themselves / children.
     * @param m The Module to remove.
     */
    public final void removeChild(Module m) {
        Module[] tmp = new Module[children.length - 1]; //Creates a temporary array to hold the children without the child to delete
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
    public boolean retainFocus() { return retaining || activeChild.retainFocus(); }
    
    /*
    When the Container is resized it needs to reposition and resize its children.
    
    This is done using the percentages stored inside the child module, which are then used to update their
    size with the most accurate possible information.
    */
    @Override
    public void onResize(Vector newSize) {
        //In general, when a container is resized, its children should be re-sized and re-positioned to scale
        for(int i = 0; i < children.length; i++) {
            children[i].resize(children[i].sizedata.getFactor(newSize)); //Scale and position children based on their percent metadata
            children[i].locate(children[i].posdata.getFactor(newSize));
        }
    }
    
    @Override
    public void mouseMove(Vector mousePos, Vector mouseDif) {
        if(!activeChild.retainFocus()) { //If the active child is not retaining focus, checck to see if it no longer has focus
            if (!activeChild.containsPoint(mousePos) || !activeChild.visible()) { //If the active child does not contain the mouse or it not visible, it is no longer in focus
                activeChild.onUnfocus(); //Call the unfocus event of the active child, as it is no longer going to be active
                boolean updated = false; //Stores whether a new active Module was discovered
                int i = 0;
                while(!updated && i < children.length) { //Iterate through child Modules to see if any are active
                    if(children[i].containsPoint(mousePos)) { //If the Module contains the mouse, it should be active
                        updated = true; //A Module has been made active
                        setActiveChild(i); //Set the active child
                        children[i].onFocus(); //Call the focus event of the new child, as it has recently come into focus
                    }                
                    i++; //Increment i so the loop will loop through all the children and eventually end
                }
                if(!updated) { //If the active child hasn't been updated, then it should be cleared
                    clearActiveChild();
                }
            }
        }
        //Pass mouse coordinates onto child module but where the coordinates passed will have an origin
        //at the top left corner of the child module
        activeChild.mouseMove(mousePos.getDif(activeChild.position()),mouseDif); 
    }

    @Override
    public void mouseDown(MouseEvent e) { activeChild.mouseDown(e); }
    @Override
    public void mouseUp(MouseEvent e) { activeChild.mouseUp(e); }
    @Override
    public void mouseClick(MouseEvent e) { activeChild.mouseClick(e); }
    @Override
    public void mouseScroll(MouseWheelEvent e) { activeChild.mouseScroll(e); }

    @Override
    public void keyDown() { activeChild.keyDown(); }
    @Override
    public void keyUp() { activeChild.keyUp(); }
    @Override
    public void keyType(KeyEvent e) { activeChild.keyType(e); }
    
    /**
     * Draws all the child Modules of the container. To be used in the draw() method by subclasses.
     */
    protected final void drawChildren() {
        for(int i = 0; i < children.length; i++) { //Iterate through all the children
            if(children[i].visible()) { //If the child isn't visible, there is no point in drawing it, so make sure that it is visible
                children[i].repaint();
            }
        }
    }
    
    @Override
    public void paint(GraphicsHandle g) {
        for(int i = 0; i < children.length; i++) { //Iterate through children modules
            if(children[i].visible()) { //If a child is visible, draw it; otherwise, don't
                children[i].repaint();
            }
        }
    }
}
