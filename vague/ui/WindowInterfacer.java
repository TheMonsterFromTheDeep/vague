package vague.ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import vague.module.Module;
import vague.module.MouseData;

/**
 * Interfaces between the module system and the Window class.
 * @author TheMonsterFromTheDeep
 */
public abstract class WindowInterfacer extends Module {
    
    public WindowInterfacer(int width, int height, Module construct) { //Requires width and height because is supposed to be whole window
        super(width, height);
        
        this.addChild(construct);
        this.setActiveChild(0);
    }
    
    @Override
    protected void resizeComponent(int width, int height) {
        activeChild.resize(width, height);
    }
    
    @Override
    public int getMouseX() {
        return windowMouseX();
    }
    
    @Override
    public int getMouseY() {
        return windowMouseY();
    }
    
    public abstract int windowMouseX(); //Overloaded by Window class to interface mouse pos between window and module system
    public abstract int windowMouseY();

    @Override
    public void drawParent(Module m) {
        drawWindow();
    }
    
    public abstract void drawWindow(); //Overloaded by Window so calls from child modules will force window to redraw
    
    @Override
    public void mouseMove(MouseData mouseData) {
        activeChild.mouseMove(mouseData);
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
    public void mouseScroll(MouseWheelEvent e) {
        activeChild.mouseScroll(e);
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
        g.drawImage(activeChild.lastRender, 0, 0, null);
        drawWindow();
    }

}