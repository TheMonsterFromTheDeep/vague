package vague.module;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import vague.util.Vector;

/**
 * The Exchange class interfaces between the Window class, which handles user input using AWT,
 * and the different Module classes.
 * 
 * The class is abstract because it has methods that need to be overloaded by Window such that
 * draw methods and other things can be processed correctly.
 * @author TheMonsterFromTheDeep
 */
public abstract class Exchange extends ModuleBase {
    private Module child; //Stores the child module which this class interfaces to the window
    
    /**
     * Constructs the Exchange on top of the specified Module.
     * 
     * The Module passed to the constructor will be directly interfaced
     * with the Exchange.
     * @param child 
     */
    public Exchange(Module child) {
        this.child = child;
        this.child.setParent(this);
    }
    
    /**
     * The resize method simply resizes the child Module because the Exchange itself does not need
     * to have a size.
     * 
     * The Exchange is simply a container to interface between the Window and the eModule system.
     * @param width The new width.
     * @param height The new height.
     */
    public final void resize(int width, int height) {
        child.resize(width, height);
    }
    
    public final void resize(Vector size) {
        child.resize(size);
    }
    
    public final BufferedImage render() {
        return child.render();
    }
    
    public abstract void drawWindow();
    
    public final void redraw() {
        child.redraw();
    }
    
    @Override
    public void drawChild(Module m) {
        drawWindow();
    }
    
    /**
     * MousePosition is abstract and must be overloaded by the Window class.
     * 
     * All child modules rely on this MousePosition method to determine their mouse position.
     * @return A Vector object containing the mouse position.
     */
    @Override
    public abstract Vector mousePosition();
    
    /**
     * The Window module checks for when the mouse is moved and calls this method when it is.
     * 
     * The Exchange passes the mouse movement data and calls its child mouse movement events.
     * @param mousePos The position of the mouse (originating from the Window)
     * @param mouseDif The difference in the position of the mouse (originating from the Window)
     */
    public void mouseMove(Vector mousePos, Vector mouseDif) {
        child.mouseMove(mousePos, mouseDif);
    }
    
    /*
    Different events to be called by Window class. When an event is fired, call the Exchange's method
    for that event from the Window class, and the Exchange object will then pass the event data down the
    Module hierarchy to its child modules. Any children container modules will probably then pass the event
    to *their* child modules.
    */
    public final void mouseDown(MouseEvent e) { child.mouseDown(e); }
    public final void mouseUp(MouseEvent e) { child.mouseUp(e); }
    public final void mouseClick(MouseEvent e) { child.mouseClick(e); }
    public final void mouseScroll(MouseWheelEvent e) { child.mouseScroll(e); } 
    public final void keyDown() { child.keyDown(); }
    public final void keyUp() { child.keyUp(); }
    public final void keyType(KeyEvent e) { child.keyType(e); }
}
