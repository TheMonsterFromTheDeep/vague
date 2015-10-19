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
    int activeIndex; //Stores the index of the active child: equal to -1 if there is no active child
    
    public Container(Module[] children) {
        if(children.length < 1) {
            children = new Module[0]; //If there are no children, then there are no children
            clearActiveChild(); //Clear the active child because there can't be an active child
        }
        else {
            this.children = children;
            setActiveChild(0); //Set an active child so that nothing weird happens
        }
    }
    
    /**
     * Makes sure that every child object has a useful percentage. This is not guaranteed to update every
     * percentage object to the version it needs to be.
     */
    protected final void synchronizePercents() {
        for(int i = 0; i < children.length; i++) { //Iterate through all children and update them if they do not have a useful percentage
            if(!children[i].sizedata.initialized) {
                children[i].sizedata.update(children[i].width(), width(), children[i].height(), height());
            }
            if(!children[i].posdata.initialized) {
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
        for(int i = 0; i < children.length; i++) {
            children[i].sizedata.update(children[i].width(), width(), children[i].height(), height());
            children[i].sizedata.update(children[i].x(), width(), children[i].y(), height());
        }
    }
    
    /**
     * Recalculates the percentage for a single specified child, without losing data
     * for any of the other child Modules.
     * @param index The index of the child to update.
     */
    protected final void updatePercent(int index) {
        children[index].sizedata.update(children[index].width(), width(), children[index].height(), height());
        children[index].posdata.update(children[index].x(), width(), children[index].y(), height());
    }
    
    protected final void clearActiveChild() {
        activeIndex = -1;
        activeChild = new Module();
    }
    
    protected final void setActiveChild(int index) {
        activeIndex = index;
        activeChild = children[index];
    }
    
    @Override
    public void mouseMove(Vector mousePos, Vector mouseDif) {
        if(!activeChild.retainFocus) {
            if (!activeChild.containsPoint(mousePos) || !activeChild.visible()) {
                boolean updated = false; //Stores whether a new active Module was discovered
                for(int i = 0; i < children.length; i++) { //Iterate through child Modules to see if any are active
                    if(children[i].containsPoint(mousePos)) { //If the Module contains the mouse, it should be active
                        updated = true; //A Module has been made active
                        setActiveChild(i); //Set the active child
                    }
                }
                if(!updated) { //If the active child hasn't been updated, then it should be cleared
                    clearActiveChild();
                }
            }
        }
        //Pass mouse coordinates onto child module but where the coordinates passed will have an origin
        //at the top left corner of the child module
        activeChild.mouseMove(mousePos.getDif(position()),mouseDif.getDif(position())); 
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
    public void keyDown(KeyEvent e) { activeChild.keyDown(e); }
    @Override
    public void keyUp(KeyEvent e) { activeChild.keyUp(e); }
    @Override
    public void keyType(KeyEvent e) { activeChild.keyType(e); }
}
