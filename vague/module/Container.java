package vague.module;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * A container class that contains multiple child modules. Contains extra methods for interacting with child modules.
 * @author TheMonsterFromTheDeep
 */
public class Container extends Module {

    public Container() { }
    
    @Override
    public void mouseMove(MouseData mouseData) {
        activeChild.mouseMove(mouseData.getShift(-x,-y,-x,-y)); //Pass the mouse data down to the active child along with the necessary shifts
    }

    @Override
    public void mouseDown(MouseEvent e) {
        activeChild.mouseDown(e);
    }

    @Override
    public void mouseUp(MouseEvent e) {
        activeChild.mouseUp(e);
    }

    @Override
    public void keyDown(KeyEvent e) {
        activeChild.keyDown(e);
    }

    @Override
    public void keyUp(KeyEvent e) {
        activeChild.keyUp(e);
    }

    @Override
    protected void render(Graphics g) {
        
    }
}