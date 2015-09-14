package vague.module;

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
    Module parent;
    
    boolean hasParent = false;
    
    public BufferedImage lastRender;
    protected Graphics graphics;
    
    public boolean verticallyAbsolute = false; //Store whether the component should be dynamically resized or not
    public boolean horizontallyAbsolute = false; 
    
    public int x = 0;
    public int y = 0;
    
    public int width = 200;
    public int height = 200;
    
    public boolean retainFocus; //If the module should retain focus even when the mouse moves out of it, this should be true.
    
    private void doRenderComps() {
        lastRender = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = lastRender.createGraphics();
    }
    
    public Module() { doRenderComps(); }
    public Module(int width, int height) {
        this.width = width;
        this.height = height;
        
        doRenderComps();
    }
    
    protected void resizeComponent(int width, int height) { }
    
    public int getCompMouseX() { return 0; }
    public int getCompMouseY() { return 0; }
    
    protected final int getMouseX() {
        if(hasParent) { return parent.getMouseX() - x; }
        return getCompMouseX();
    }
    
    protected final int getMouseY() {
        if(hasParent) { return parent.getMouseY() - y; }
        return getCompMouseY();
    }
    
    protected final int getMouseX(MouseEvent e) { 
        if(hasParent) { return e.getX() - x - parent.x; }
        else { return e.getX() - x; }
    }
    
    protected final int getMouseY(MouseEvent e) { 
        if(hasParent) { return e.getY() - y - parent.y; }
        else { return e.getY() - y; }
    }
    
    public void resize(int width, int height) {
        resizeComponent(width, height);
        
        this.width = width;
        this.height = height;       
        
        doRenderComps(); //Update the rendering data
        
        draw();
    }
    
    public void locate(int x, int y) { this.x = x; this.y = y; }
    
    public abstract void tick(int mousex, int mousey);
    public abstract void mouseDown(MouseEvent e);
    public abstract void mouseUp(MouseEvent e);
    public abstract void keyDown(KeyEvent e);
    public abstract void keyUp(KeyEvent e);
    protected abstract void render(Graphics g);
    
    public final void clearParent() { parent = null; hasParent = false; }
    public final void setParent(Module m) { parent = m; hasParent = true; }
    public void drawParent() {
        if(hasParent) { parent.draw(); }
    }
    public void drawParent(Module m) {
        if(hasParent) { parent.draw(m); }
    }
        
    public void draw(Module m) { //Overriden by ModulePane
        render(graphics);
        drawParent();
    }
    
    public final void draw() {
        render(graphics);
        drawParent();
    }
    
    public final void drawSelf() {
        render(graphics);
        drawParent(this);
    }
    
    public final void drawLimited() {
        render(graphics);       
    }
    
    public void mouseScroll(MouseWheelEvent e) { }
}