package vague.module;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import vague.util.Vector;

/**
 * The Module class defines an object which does graphical rendering and processes user input.
 * 
 * Module classes can form an inherited parent-child structure where parent, container modules
 * render their child modules and pass user events down to the child modules.
 * 
 * Module could technically be abstract, but it isn't so that any class can override exactly the
 * methods it needs to override. Also, making Module abstract would prevent initializing simple Module objects
 * that can be used as dummies or something else.
 * 
 * Non-abstract modules can also be initialized without subclassing.
 * @author TheMonsterFromTheDeep
 */
public class Module {
    /**
     * Position and size are private so that they are not modified outside the class.
     * 
     * This way resizes and positions can be handled with all the relevant changes to rendering
     * logic.
     */
    private Vector position; //Stores the position of the Module.
    private Vector size; //Stores the size of the module.
    
    //Stores the rendered version of the module. Private because nothing should modify it.
    private BufferedImage buffer;
    /*
    The graphics object draws directly to "buffer". It is expected of the child
    modules that the graphics object is not modified to draw on another image.    
    -->Question: maybe subclass graphics so that it can't be modified?
    
    The 'graphics' object  is protected so that subclasses can access it and
    do their drawing code.
    */
    protected Graphics graphics;
    
    //The parent of this Module.
    private Module parent;
    //Stores whether this module *has* a parent.
    private boolean hasParent;
    
    public Module() {
        /**
         * Default position and size of the Module.
         * The default position is 0, 0 and the default size is also 0, 0.
         * 
         * With the default size, it is impossible to render anything.
         */
        position = new Vector(0,0);
        size = new Vector(0,0);
        doRenderCalc();
    }
    
    public Module(int width, int height) {
        position = new Vector(0, 0);
        size = new Vector(width, height);
        doRenderCalc();
    }
    
    /**
     * Should be called every time the size of the module is changed.
     * 
     * Does calculations so that the module can be rendered with the correct
     * width and height.
     */
    private void doRenderCalc() {
        if(size.similar(Vector.ZERO)) {
            //If the size is zero, the buffer needs to have some sort of size
            //so it is created as an image with a width of 1 and height of 1.
            buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        }
        else {
            //Simply create the buffer with the width and height of the module.
            buffer = new BufferedImage(size.x,size.y,BufferedImage.TYPE_INT_ARGB);
        }
        //Allow graphics to be used.
        graphics = buffer.createGraphics();
    }
    
    /**
     * Causes the Module to re-draw itself, saving all changes to the
     * 'buffer' BufferedImage. The buffer is accessed by other classes through
     * the render() method.
     */
    public void redraw() { }
    
    /**
     * Sets the parent of the module.
     * 
     * Mostly called by container classes so that their child modules
     * will be able to call methods of their parent.
     * @param m The module to become the parent.
     */
    public final void setParent(Module m) {
        hasParent = true;
        parent = m;
    }
    
    /**
     * Clears the parent of the module.
     * 
     * The module will no longer have a parent (it will be nullified) and it will no longer
     * consider itself as having a parent.
     */
    public final void clearParent() {
        hasParent = false;
        parent = null;
    }
    
    /**
     * Returns a vector object containing the mouse position.
     * 
     * Requires polling the parent Module, because otherwise there is no way
     * to determine the mouse position.
     * 
     * As such, this method will NOT WORK and will throw a NullPointerException if the 
     * parent Module is null.
     * 
     * This method is overloaded by the very top module so that the Window class can pass
     * the mouse position directly.
     * @return A Vector object containing the mouse position.
     */
    public Vector mousePosition() {
        return parent.mousePosition().getDif(position);
    }
    
    /*
    Mouse event methods to be overloaded in subclasses.
     */
    public void mouseDown(MouseEvent e) { }
    public void mouseUp(MouseEvent e) { }
    public void mouseClick(MouseEvent e) { }
    public void mouseScroll(MouseWheelEvent e) { }
    
    /*
    Key Event methods to be overloaded in subclasses.
    */
    public void keyDown(KeyEvent e) { }
    public void keyUp(KeyEvent e) { }
    public void keyType(KeyEvent e) { }
    
    /**
     * Causes the module to resize to the specified width and height.
     * 
     * This method is NOT overloadable because very specific things need
     * to happen when resized for the module to work correctly.
     * @param width The new width of the module.
     * @param height The new height of the module.
     */
    public final void resize(int width, int height) {
        onResize(new Vector(width, height));
        size = new Vector(width, height);
        doRenderCalc();
    }
    
    /**
     * Causes the module to resize as the specified Vector.
     * 
     * This method is NOT overloadable because very specific things need
     * to happen when resized for the module to work correctly. 
     * @param v The Vector to resize to.
     */
    public final void resize(Vector v) {
        onResize(v);
        size = new Vector(v); //The Vector is copied so that nothing has a reference to size through a refererence
        doRenderCalc();
    }
    
    /**
     * Causes the module to move to the specified position.
     * 
     * This method is NOT overloadable because very specific things need
     * to happen when moved for the module to work correctly.
     * @param x The new x position of the module.
     * @param y The new y position of the module.
     */
    public final void locate(int x, int y) {
        onLocate(new Vector(x, y));
        position = new Vector(x, y);
    }
    
    /**
     * Causes the module to move to the specified Vector location.
     * 
     * This method is NOT overloadable because very specific things need
     * to happen when moved for the module to work correctly. 
     * @param v The Vector to move to.
     */
    public final void locate(Vector v) {
        onLocate(v);
        position = new Vector(v); //The Vector is copied so that nothing has a reference to position through a refererence
    }
    
    /*
    Overloadable methods for subclasses to use so that they can 
    modify certain things when they are resized or relocated.
    
    A Vector object is passed rather than two values for ease of use - being able
    to reference values directly rather than have to convert to a Vector if necessary.
    */
    public void onResize(Vector v) { }
    public void onLocate(Vector v) { }
    
    /*
    width() and height() return the width and height of the module, respectively.
    
    The values of the module, however, cannot be modified.
    */
    public final int width() { return size.x; }
    public final int height() { return size.y; }
    
    /*
    x() and y() return the x and y position of the module, respectively.
    
    The values of the module, however, cannot be modified.
    */
    public final int x() { return position.x; }
    public final int y() { return position.y; }
    
    /**
     * This method returns the rendered version of the Module.
     * 
     * However, it's misleading name is false - it does not actually
     * re-draw ('render') the module, it simply returns an image that has
     * already been drawn to.
     * @return A rendered graphic of the Module.
     */
    public final BufferedImage render() {
        /*
        Question of redundancy:
        
        Doesn't an accessor not really make sense when the object in question is a reference
        and not in fact an object?
        
        The rendered version will still be able to be modified. It would be really inefficient
        to make a copy of the render when returning it.
        
        Maybe just keep this method, and if in the future there are more things that need to be done,
        change it here so it doesn't have to be changed elsewhere?
         */
        return buffer;
    }
}