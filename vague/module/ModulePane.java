package vague.module;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * A divided pane of modules. Stores either module panes or modules themselves.
 * @author TheMonsterFromTheDeep
 */
public class ModulePane extends Module {
    Module[] children; //Stores children of pane
    
    int activeChild = 0; //Stores the index of the active child node
    
    boolean horizontal = false; //Stores whether the layout is vertical(false) or horizontal(true)
    
    public ModulePane(Module[] childmods, boolean direction) {
        this.horizontal = direction;
        
        int width = 0;
        int height = 0;
        
        if(horizontal) {
            this.children = childmods;
            for(int i = 0; i < children.length; i++) {
                children[i].setParent(this);
                if(children[i].height > height) { height = children[i].height; }
                width += children[i].width;
            }
        }
        else {
            this.children = childmods;
            for(int i = 0; i < children.length; i++) {
                children[i].setParent(this);
                if(children[i].width > width) { width = children[i].width; }
                height += children[i].height;
            }
        }
        
        this.width = width;
        this.height = height;
    }
    
    @Override
    protected void resizeComponent(int w, int h) {
        if(horizontal) {
            double[] percents = new double[children.length]; //Stores percents for dynamically resized elements; -1 if element is not dynamically resized
            for(int i = 0; i < children.length; i++) {
                if(children[i].verticallyAbsolute) { percents[i] = -1; } //Set percent to negative 1 if it is absolute
                else {
                    percents[i] = (double)children[i].width / (double)this.width;
                }
            }
            for(int i = 0; i < children.length; i++) {
                if(percents[i] == -1) { children[i].resize(children[i].width, h); }
                else {
                    children[i].resize((int)Math.round(w * percents[i]), h);
                }
            }
            int newX = 0;
            for(int i = 0; i < children.length; i++) {
                children[i].locate(newX, children[i].y);
                newX += children[i].height;
            }
        }
        else {
            double[] percents = new double[children.length]; //Stores percents for dynamically resized elements; -1 if element is not dynamically resized
            for(int i = 0; i < children.length; i++) {
                if(children[i].verticallyAbsolute) { percents[i] = -1; } //Set percent to negative 1 if it is absolute
                else {
                    percents[i] = (double)children[i].height / (double)this.height;
                }
            }
            for(int i = 0; i < children.length; i++) {
                if(percents[i] == -1) { children[i].resize(w, children[i].height); }
                else {
                    children[i].resize(w, (int)Math.round(h * percents[i]));
                }
            }
            int newY = 0;
            for(int i = 0; i < children.length; i++) {
                children[i].locate(children[i].x, newY);
                System.err.println(newY + " " + children[i].height + " " + this.height);
                newY += children[i].height;
            }
        }
    }
    
    @Override
    public void tick(int mousex, int mousey) {
        if(!children[activeChild].retainFocus) {
            //We don't need to check if the mouse pos is out of this module's bounds because the higher-ups will do that by default
            if(horizontal) {
                if(mousex < children[activeChild].x) {
                    activeChild--;
                }
                else if (mousex > children[activeChild].x + children[activeChild].width) {
                    activeChild++;
                }
                if(activeChild < 0)  { activeChild = 0; } //If the child does happen to not exist than make sure bad things don't happen
            }
            else {
                if(mousey < children[activeChild].y) {
                    activeChild--;
                }
                else if (mousey > children[activeChild].y + children[activeChild].height) {
                    activeChild++;
                }
                if(activeChild >= children.length) { activeChild = children.length - 1; }
            }
        }
        children[activeChild].tick(mousex - x, mousey - y); //Subtract top x and y because mousex and y are passed relatively
    }

    @Override
    public void mouseDown(MouseEvent e) {
        children[activeChild].mouseDown(e);
    }

    @Override
    public void mouseUp(MouseEvent e) {
        children[activeChild].mouseUp(e);
    }

    @Override
    public void keyDown(KeyEvent e) {
        children[activeChild].keyDown(e);
    }

    @Override
    public void keyUp(KeyEvent e) {
        children[activeChild].keyUp(e);
    }
    
    @Override
    public void mouseScroll(MouseWheelEvent e) {
        children[activeChild].mouseScroll(e);
    }

    @Override
    protected void render(Graphics g) {
        for(int i = 0; i < children.length; i++) {
            children[i].drawLimited();
            g.drawImage(children[i].lastRender, children[i].x, children[i].y, null);
        }
    }
    
    @Override
    public void draw(Module m) {
        graphics.drawImage(m.lastRender, m.x, m.y, null);
        drawParent();
    }
}