package vague.module.container;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import vague.module.Module;
import vague.util.Vector;

/**
 * The Container class is a basic Module which contains other Modules.
 * 
 * In the style of a highly controllable UI, most Modules will be in Containers
 * which are controllable and splittable by the user.
 * @author TheMonsterFromTheDeep
 */
public class Container extends Module {
    Module[] children; //Stores the child Modules of the container
    Module activeChild; //Stores a reference to the active child
    int activeIndex; //Stores the index of the active child
    
    public Container(Module[] children) {
        if(children.length < 1) {
            this.children = new Module[1];
            this.children[0] = new Module(); //Create a dummy module to be the child
        }
        else {
            this.children = children;
        }
        setActiveChild(0); //Set an active child so that nothing weird happens
    }
    
    protected final void setActiveChild(int index) {
        activeIndex = index;
        activeChild = children[index];
    }
    
    @Override
    public void mouseMove(Vector mousePos) { activeChild.mouseMove(mousePos.getDif(new Vector(x(),y()))); }

    @Override
    public void mouseDown(MouseEvent e) { activeChild.mouseDown(e); }
    @Override
    public void mouseUp(MouseEvent e) { activeChild.mouseUp(e); }
    @Override
    public void mouseClick(MouseEvent e) { activeChild.mouseClick(e); }
    @Override
    public void mouseScroll(MouseWheelEvent e) { activeChild.mouseScroll(e); }

    @Override
    public void keyDown(KeyEvent e) { activeChild.keyDown(e); }
    @Override
    public void keyUp(KeyEvent e) { activeChild.keyUp(e); }
    @Override
    public void keyType(KeyEvent e) { activeChild.keyType(e); }
}
