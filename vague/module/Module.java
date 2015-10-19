package vague.module;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import vague.util.Percents;
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
public class Module extends ModuleBase {
    /**
     * Position and size are private so that they are not modified outside the class.
     * 
     * This way resizes and positions can be handled with all the relevant changes to rendering
     * logic.
     */
    private Vector position; //Stores the position of the Module.
    private Vector size; //Stores the size of the module.
    
    protected Color bgColor; //Stores the color that is put in the background whenever applicable
    
    public Percents sizedata = new Percents(); //Stores percents of this Module's size compared to it's container Module's size.
                                               //Used by container classes.
    
    public Percents posdata = new Percents(); //Stores percents of this Module's position offset.
                                               //Used by container classes.
    
    /*
    This boolean stores whether the Module should retain focus. Vague is designed such that
    moving the mouse into a different module should change the focus to that module. However,
    sometimes a Module needs to make sure that focus won't change - for example, if the user
    is panning the editor, they should be able to continue panning even if the mouse leaves
    the Module where they are panning the mouse. 
    
    Container Modules should check this value before changing focus.
    */
    public boolean retainFocus;
    
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
        BufferedImage old; //Declare a BufferedImage object to hold the current data of the buffer
                           //so that it can be drawn back to the new buffer
        if(size.similar(Vector.ZERO)) {
            //if the size is zero, the old BufferedImage needs to be created with a size of 1,1
            //so that it can be created correctly
            old = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            old.createGraphics().drawImage(buffer, 0, 0, null);
            //If the size is zero, the buffer needs to have some sort of size
            //so it is created as an image with a width of 1 and height of 1.
            buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        }
        else {
            //If the size is not zero, the old buffer can simply have the new size 
            old = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
            old.createGraphics().drawImage(buffer, 0, 0, null);
            //Simply create the buffer with the width and height of the module.
            buffer = new BufferedImage(size.x,size.y,BufferedImage.TYPE_INT_ARGB);
        }
        //Allow graphics to be used.
        graphics = buffer.createGraphics();
        graphics.setColor(bgColor);
        graphics.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
        graphics.drawImage(old, 0, 0, null);
    }
    
    /**
     * Can be called if a module needs to be sure it is ready for rendering.
     * 
     * For example, it can be called in a constructor if a module must start with
     * a certain graphical state.
     */  
    protected final void readyForRendering() { doRenderCalc(); }
    
    /**
     * Causes the Module to re-draw itself, saving all changes to the
     * 'buffer' BufferedImage. The buffer is accessed by other classes through
     * the render() method.
     * 
     * IMPORTANT: redraw is only ever called if it is also called in the parent.
     * ergo, it is not necessary to call the drawParent() method within the redraw()
     * method.
     */
    public void redraw() { }
    
    /**
     * Causes the Module to draw one of its child nodes.
     * 
     * Also causes the Module to ask its parent to draw itself, so that the graphical
     * changes caused by drawChild() will be reflected higher up in the module system
     * hierarchy.
     * 
     * Overloadable so that the top-level window/module system interfacer
     * can properly draw without drawing its out parent.
     * 
     * Overrides so that it can call the drawParent() method.
     * @param m 
     */
    @Override
    public void drawChild(Module m) {
        graphics.drawImage(m.render(),m.x(),m.y(),null);
        drawParent();
    }
    
    /**
     * Causes the parent to update it's graphical representation of the child object.
     */
    public void drawParent() {
        parent.drawChild(this);
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
    @Override
    public Vector mousePosition() {
        return parent.mousePosition().getDif(position);
    }
    
    /**
     * Returns whether the Module contains the specified point.
     * 
     * The Container classes often need to know whether a point is within the bounds of a Module,
     * particularly whether the Module contains the mouse so that it is active.
     * @param point The point to check against.
     * @return Whether the point is within the Module's bounds.
     */
    public final boolean containsPoint(Vector point) {
        return (
                point.x >= position.x &&
                point.y >= position.y &&
                point.x < position.x + size.x &&
                point.y < position.y + size.y
        );
    }
    
    //Called when the mouse is moved - container classes pass mouse offset as well
    public void mouseMove(Vector mousePos, Vector mouseDif) { }
    
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
        redraw(); //Redraw in case it needs to be re-drawn
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
        redraw();
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
    
    //Returns a copy of the size Vector so it can be used without being changed.
    public final Vector size() { return new Vector(size); }
    
    /*
    x() and y() return the x and y position of the module, respectively.
    
    The values of the module, however, cannot be modified.
    */
    public final int x() { return position.x; }
    public final int y() { return position.y; }
    
    //Returns a copy of the position Vector so it can be used without being changed.
    public final Vector position() { return new Vector(position); }
    
    /*
    right() and bottom() return the x and y values of the right of the Module and thhe bottom of the Module,
    respectively.
    
    These cannot be modified.
    */
    public final int right() { return position.x + size.x; }
    public final int bottom() { return position.y + size.y; }
    
    //Returns the position of the bottom right corner of the module. Useful for various application. 
    public final Vector bottomRight() { return new Vector(position.getSum(size)); }
    
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
    
    /**
     * Returns whether parent classes should bother to draw this module.
     * If it has a size of zero, than it's graphical data buffer will not contain any useful graphical
     * information, and so the parent shouldn't bother to re-draw the module.
     * 
     * In the future, this may also indicate whether a module has been hidden by the user and should
     * not be drawn.
     * 
     * This method does not particularly indicate whether the container class should draw
     * any *other* modules differently.
     * @return A boolean value indicating whether parent classes should draw the module.
     */
    public final boolean visible() {
        return !size.similar(Vector.ZERO);
    }
}