package vague.ui.module;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Each section of the ui is contained in a module. The modules are then drawn on to the screen.
 * @author TheMonsterFromTheDeep
 */
public abstract class Module {
    public BufferedImage lastRender;
    protected Graphics graphics;
    
    public Module(int width, int height) {
        lastRender = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = lastRender.createGraphics();
    }
    
    public abstract void tick(int mousex, int mousey);
    
    public abstract void mouseDown(MouseEvent e);
    public abstract void mouseUp(MouseEvent e);
    
    public void mouseScroll(MouseWheelEvent e) { }
    
    public abstract void keyDown(KeyEvent e);
    public abstract void keyUp(KeyEvent e);
    
    public abstract void render();
}