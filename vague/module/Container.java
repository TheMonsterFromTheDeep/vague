package vague.module;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
    protected void resizeComponent(int width, int height) { //Basic resize method
        for(int i = 0; i < children.length; i++) {
            System.err.println(this.height / children[i].height);
            children[i].resize(Math.round(width / (this.width / children[i].width)), Math.round(height / (this.height / children[i].height)));
        }
    }
    
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
        for(int i = 0; i < children.length; i++) {
            children[i].drawLimited();
            g.drawImage(children[i].lastRender, children[i].x, children[i].y, null);
        }
    }
}