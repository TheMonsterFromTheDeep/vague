package vague.module;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Each section of the UI is contained in a module. The modules are then drawn on to the screen.
 * @author TheMonsterFromTheDeep
 */
public abstract class Module {
    protected Module parent;
    protected Module[] children = { };
    
    protected Module activeChild = null; //Stores a reference to the active child module
    protected int activeIndex = -1; //Stores the index of the active child
    
    protected boolean hasParent = false; //Stores whether the module has parents / children
    protected boolean hasChildren = false;
    
    public BufferedImage lastRender;
    protected Graphics graphics;
    
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
    
    protected int getMouseX() {
        if(hasParent) { return parent.getMouseX() - x; }
        return 0;
    }
    
    protected int getMouseY() {
        if(hasParent) { return parent.getMouseY() - y; }
        return 0;
    }
    
    public void resize(int width, int height) {
        resizeComponent(width, height);
        
        this.width = width;
        this.height = height;       
        
        doRenderComps(); //Update the rendering data
        
        drawSelf();
    }
    
    public void locate(int x, int y) { this.x = x; this.y = y; }
    
    public abstract void mouseMove(MouseData mouseData);
    public abstract void mouseDown(MouseEvent e);
    public abstract void mouseUp(MouseEvent e);
    public abstract void keyDown(KeyEvent e);
    public abstract void keyUp(KeyEvent e);
    protected abstract void render(Graphics g);
    
    public final boolean hasChildren() { return hasChildren; }
    public final boolean hasParent() { return hasParent; }
    
    public final Module activeChild() { return activeChild; }
    
    public final void addChild(Module m) {
        Module[] tmp = children;
        children = new Module[children.length + 1];
        System.arraycopy(tmp,0,children,0,tmp.length);
        children[tmp.length] = m; //Set at tmp.length which is last index in new array
        
        if(children.length == 1) { setActiveChild(0); hasChildren = true; } //If there was no child before one was added, then that child must become the only active child.
    }
    
    public final void setActiveChild(int index) {
        if(index >= 0 && index < children.length) {
            activeIndex = index;
            activeChild = children[index];
        }
    }
    
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