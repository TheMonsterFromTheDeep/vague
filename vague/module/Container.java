package vague.module;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * A container class that contains multiple child modules. Contains extra methods for interacting with child modules.
 * @author TheMonsterFromTheDeep
 */
public class Container extends Module {

    private void initParent() {
        for(int i = 0; i < children.length; i++) { children[i].setParent(this); }
    }
    
    public Container(Module[] children) {
        this.children = children;
        
        setActiveChild(0);
        
        int nwidth = 0;
        int nheight = 0;
        for(int i = 0; i < children.length; i++) {
            int wdif = children[i].width - (nwidth - children[i].x);
            int hdif = children[i].height - (nheight - children[i].y);
            if(nwidth + wdif > nwidth) { nwidth += wdif; }
            if(nheight + hdif > nheight) { nheight += hdif; }
        }
        this.width = nwidth;
        this.height = nheight;
        resize(this.width, this.height);
        
        initParent(); //Initialize the parent of all children as this
    }
    
    @Override
    public void draw(Module m) {
        System.err.println("drawing continaer");
        graphics.drawImage(m.lastRender, m.x, m.y, null);
        drawParent(this);
    }
    
    @Override
    protected void resizeComponent(int width, int height) { //Basic resize method
        for(int i = 0; i < children.length; i++) {
            children[i].resize(Math.round(width / (this.width / children[i].width)), Math.round(height / (this.height / children[i].height)));
            children[i].locate(Math.round(width / (this.width / ((children[i].x > 0) ? children[i].x : 1))), Math.round(height / (this.height / ((children[i].y > 0) ? children[i].y : 1))));
        }
    }
    
    @Override
    public void mouseMove(MouseData mouseData) {
        if(!activeChild.retainFocus) {
            if(!activeChild.mouseInside(mouseData)) {
                for(int i = 0; i < children.length; i++) {
                    if(children[i].mouseInside(mouseData)) {
                        setActiveChild(i);
                    }
                }
            }
        }
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
    public void mouseScroll(MouseWheelEvent e) {
        activeChild.mouseScroll(e);
    }

    @Override
    protected void render(Graphics g) {
        for(int i = 0; i < children.length; i++) {
            children[i].drawLimited();
            g.drawImage(children[i].lastRender, children[i].x, children[i].y, null);
        }
    }
}