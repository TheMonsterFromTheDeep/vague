package module.meta;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import module.Module;
import vague.util.Cursor;
import vague.util.Vector;

/**
 * The ModuleBase class defines a simple parent class that is then subclassed to Module.
 * 
 * It is necessary to avoid unnecessary waste of space in the Exchange class. In order
 * to parent a Module to an Exchange, the Exchange must be a subclass of ModuleBase
 * @author TheMonsterFromTheDeep
 */
public class ModuleBase {
  
    //Stores the rendered version of the module. Private because nothing should modify it.
    protected BufferedImage buffer;
    /*
    The graphics object draws directly to "buffer". It is expected of the child
    modules that the graphics object is not modified to draw on another image.    
    -->Question: maybe subclass graphics so that it can't be modified?
    
    The 'graphics' object  is protected so that subclasses can access it and
    do their drawing code.
    */
    protected Graphics graphics;
    
    //The parent of this Module.
    protected ModuleBase parent;
    //Stores whether this module *has* a parent.
    /*
    Question: is this even relevant? Most of the methods that rely on hasParent being true
    shouldn't even be called if a module doesn't have a parent and wouldn't make any sense to
    call if the module doesn't have a parent.
    */
    protected boolean hasParent;
    
    public ModuleBase() {
    }

    /**
     * Causes the Module to draw one of its child nodes.
     * 
     * Also causes the Module to ask its parent to draw itself, so that the graphical
     * changes caused by drawChild() will be reflected higher up in the module system
     * hierarchy.
     * 
     * Overloadable so that the top-level window/module system interfacer
     * can properly draw without drawing its out parent.
     * @param m 
     */
    public void drawChild(Module m, int x, int y, int width, int height) {
    }
    
    public void setCursor(Cursor c) { }
    public void clearCursor() { }
    
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
        return parent.mousePosition();
    }
    
    /**
     * Sets the parent of the module.
     * 
     * Mostly called by container classes so that their child modules
     * will be able to call methods of their parent.
     * @param m The ModuleBase to become the parent.
     */
    public final void setParent(ModuleBase m) {
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
}