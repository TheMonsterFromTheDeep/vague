package vague.module;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Each section of the UI is contained in a module. The modules are then drawn on to the screen.
 * @author TheMonsterFromTheDeep
 */
public abstract class Module {
    protected Module parent; //Stores the parent of the module
    protected Module[] children = { }; //Stores the child modules - mostly only relevant for container-type modules
    
    protected Module activeChild = null; //Stores a reference to the active child module
    protected int activeIndex = -1; //Stores the index of the active child
    
    protected boolean hasParent = false; //Stores whether the module has parents / children
    protected boolean hasChildren = false; 
    
    public BufferedImage lastRender; //Stores the image written the last time the module was rendered
    protected Graphics graphics; //Stores the graphics object which draws onto lastRender
    
    public int x = 0; //Stores the position of the module inside its parent module
    public int y = 0;
    
    public int width = 200; //Stores the width and height of the module
    public int height = 200;
    
    public boolean retainFocus; //If the module should retain focus even when the mouse moves out of it, this should be true.
    
    private void doRenderComps() { //called whenever the module is resized. This resizes lastRender so it can fit all the new data of the object.
        lastRender = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); //Resize lastRender
        graphics = lastRender.createGraphics(); //recreate the graphics object.
    }
    
    public Module() { doRenderComps(); } //The default constructor uses the default values (declared above): x and y: 0, width and height: 200, and simply does the render comps.
    public Module(int width, int height) { //Constructs a module of the specified width and height
        this.width = width; //Set width and height
        this.height = height;
        
        doRenderComps(); //Create lastRender and graphics objects so that rendering will work properly
    }
    
    protected void resizeComponent(int width, int height) { } //To be overloaded by subclasses - called when the component is resized.
    
    protected int getMouseX() {
        if(hasParent) { return parent.getMouseX() - x; }
        return 0;
    }
    
    protected int getMouseY() {
        if(hasParent) { return parent.getMouseY() - y; }
        return 0;
    }
    
    /**
     * Resizes the module. This method should be called instead of setting width and height directly.
     * Setting width and height directly will not reset rendering component or call resizeComponent.
     * 
     * @param width The new width of the module.
     * @param height The new height of the module.
     */
    public final void resize(int width, int height) { //Called when the component is resized
        resizeComponent(width, height); //Call the resizeComponent method first so subclasses can use this.width and height before they are reset
        
        this.width = width; //Set new width and height
        this.height = height;       
        
        doRenderComps(); //Update the rendering data
        
        /**
         * Thoughts: Maybe subclasses should be responsible for re-drawing, 
         * and width/height should be set, and then old width/height passed through resizeComponent()?
         */
        drawLimited(); //Rendering higher-up should be taken care of by container classes
    }
    
    public void locate(int x, int y) { this.x = x; this.y = y; } //Relocates the component - used in case special cases happen in future
    
    /*
    * Abstract methods are overloaded by subclasses. These methods are all mandatory. (may be changed
    * in future)
    */
    public abstract void mouseMove(MouseData mouseData);
    public abstract void mouseDown(MouseEvent e);
    public abstract void mouseUp(MouseEvent e);
    public abstract void keyDown(KeyEvent e);
    public abstract void keyUp(KeyEvent e);
    protected abstract void render(Graphics g);
    
    public final boolean hasChildren() { return hasChildren; } //Returns whether the module has children
    public final boolean hasParent() { return hasParent; } //Returns whether the module has a parent module
    
    public final Module activeChild() { return activeChild; } //Returns the active child of the module
    
    public final void addChild(Module m) { //Adds a module to the list of children. Sets it to active if it is the first module added.
        Module[] tmp = children; //Copy the child modules to a temp array so children can be expanded
        children = new Module[children.length + 1]; //Expand children to new empty array with one more element
        System.arraycopy(tmp,0,children,0,tmp.length); //Copy old children over
        children[tmp.length] = m; //Set at tmp.length which is last index in new array
        
        m.setParent(this); //Set the parent of the child to this because this is now its parent
        
        if(children.length == 1) { setActiveChild(0); hasChildren = true; } //If there was no child before one was added, then that child must become the only active child.
    }
    
    public final void setActiveChild(int index) { //Sets the active child based on the index of the child module.
        if(index >= 0 && index < children.length) { //If the index is useable, set active child
            activeIndex = index; //The activeIndex is set to the index; activeIndex stores index of activeChild
            activeChild = children[index]; //Set the activeChild so its methods/members can be accessed directly
        }
    }
    
    public final void clearParent() { parent = null; hasParent = false; } //Clears the parent
    public final void setParent(Module m) { parent = m; hasParent = true; } //Sets the parent
    public void drawParent() { //Draws the parent
        if(hasParent) { parent.draw(); }
    }
    public void drawParent(Module m) { //Tells the parent to draw only a certain child module ((this) is normally passed)
        if(hasParent) { parent.draw(m); }
    }
        
    public void draw(Module m) {
        /*
         * This is the method meant to be overridden by container classes.
         * When this method is called, the container class should re-draw the module passed and only that
         * module. The module passed is (this), and is passed by the child module when it needs to be
         * redrawn.
         */
        render(graphics); //For non-container classes, do the same thing as the normal draw method.
        drawParent();
    }
    
    
    public final void draw() { //Redraws the module and its parent
        render(graphics); //Render the module
        drawParent(); //Tell parent to re-draw
    }
    
    public final void drawSelf() { //Draws only self
        render(graphics); //Draw module
        drawParent(this); //Tell parent to draw only this module
    }
    
    public final void drawLimited() { //Draw without updating parents
        //This is called when a parent is re-rendering a child. It is called so that an infinite recursive
        //loop of parent drawing child and child drawing parent will not happen.
        render(graphics); //Draw this module
    }
    
    //Returns true if the mouse is inside bounds, false if mouse is outside bounds
    public final boolean mouseInside(MouseData m) {
        return (m.getX() > this.x && m.getY() > this.y && m.getX() < this.x + this.width && m.getY() < this.y + this.height);
    }
    
    //This is not mandatory to overwrite, but can be overwritten by those classes that need to.
    //Also overwritten by all container classes, in case subclasses use it.
    public void mouseScroll(MouseWheelEvent e) { }
}