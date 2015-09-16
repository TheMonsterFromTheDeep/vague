package vague.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import vague.util.ImageData;
import vague.util.ImageLoader;

/**
 * A divided pane of modules. Stores either module panes or modules themselves.
 * @author TheMonsterFromTheDeep
 */
public class ModulePane extends Module {
    Module[] children; //Stores children of pane
    
    int activeChild = 0; //Stores the index of the active child node
    
    boolean horizontal = false; //Stores whether the layout is vertical(false) or horizontal(true)
    
    int firstResizeIndex = -1; //Stores the index of the first module during a module resize - if no resizing is possibe, equals -1
    boolean resizeModule = false; //Stores whether a module is currently being resized
    
    //MouseTracker mtracker;
    
    static Color lineColor = new Color(0xb0b3dc); //Stores the color of the lines separating modules
    static final int resizeThreshold = 15; //The pixel distance from which it is possible to resize modules
    static final int minSize = 30; //Minimum size, in pixels, of child modules
    
    BufferedImage resizeControl;
    
    final int lineWidth = 3; //Stores the width of the line separating panes; used in calculating widths when resized
    
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
        
        if(horizontal) {
            resizeControl = ImageData.data.resizeArrowHorizontal;
            
        }
        else {
            resizeControl = ImageData.data.resizeArrowVertical;
        }
    }
    
    public void drawOnlySelf() { //Draws the component without redrawaing all its child components
        for(int i = 0; i < children.length; i++) {
            //g.drawImage(children[i].lastRender, children[i].x, children[i].y + ((i > 0) ? 1 : 0), null);
            graphics.drawImage(children[i].lastRender, children[i].x, children[i].y, null);
            graphics.setColor(lineColor);
            if(horizontal) {
                graphics.fillRect(children[i].x + children[i].width, children[i].y, lineWidth, children[i].height);
            }
            else {
                graphics.fillRect(children[i].x, children[i].y + children[i].height, children[i].width, lineWidth);
            }
            if(firstResizeIndex > -1) {
                if(horizontal) {
                    graphics.drawImage(resizeControl, getMouseX() - 16, getMouseY() - 5, null);
                }
                else {
                    graphics.drawImage(resizeControl, getMouseX() - 5, getMouseY() - 16, null);
                }
            }
        }
        drawParent(this);
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
                newX += children[i].width + lineWidth; //Add line widht so there is sppaace to draw line between modules
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
                newY += children[i].height + lineWidth; //Add lineWidth so that there is space to draw line between moduels
            }
        }
        drawOnlySelf();
    }
    
    @Override
    public void mouseMove(MouseData mouseData) {
        int mousex = getMouseX();
        int mousey = getMouseY();
        if(resizeModule) {
            //mtracker.shift(mousex, mousey);
            if(horizontal) {
                //children[firstResizeIndex].width = mtracker.x - children[firstResizeIndex].x;
                //if(firstResizeIndex < children.length) {                   
                //    int bottomX = children[firstResizeIndex + 1].x + children[firstResizeIndex + 1].width;
                //    children[firstResizeIndex + 1].x = children[firstResizeIndex].x + children[firstResizeIndex].width + lineWidth;
                //    children[firstResizeIndex + 1].width = bottomX - children[firstResizeIndex + 1].x;
                //}
                int topWidth = mouseData.getX() - children[firstResizeIndex].x;
                if(topWidth < minSize) { topWidth = minSize; }
                if(firstResizeIndex < children.length) {
                    int bottomX = children[firstResizeIndex + 1].x + children[firstResizeIndex + 1].width;
                    int middleX = children[firstResizeIndex].x + topWidth + lineWidth;
                    int lowerWidth = bottomX - middleX;
                    
                    if(lowerWidth < minSize) {
                        topWidth -= (minSize - lowerWidth);
                        middleX -= (minSize - lowerWidth);
                        lowerWidth = minSize;
                    }
                    
                    children[firstResizeIndex + 1].resize(lowerWidth, children[firstResizeIndex + 1].height);
                    children[firstResizeIndex + 1].locate(middleX, children[firstResizeIndex + 1].y);
                }
                children[firstResizeIndex].resize(topWidth, children[firstResizeIndex + 1].height);
            }
            else {
                //children[firstResizeIndex].height = mtracker.y - children[firstResizeIndex].y;
                //if(firstResizeIndex < children.length) {                   
                //    int bottomY = children[firstResizeIndex + 1].y + children[firstResizeIndex + 1].height;
                //    children[firstResizeIndex + 1].y = children[firstResizeIndex].y + children[firstResizeIndex].height + lineWidth;
                //    children[firstResizeIndex + 1].height = bottomY - children[firstResizeIndex + 1].y;
                //}
                int topHeight = mouseData.getY() - children[firstResizeIndex].y;
                if(topHeight < minSize) { topHeight = minSize; }
                if(firstResizeIndex < children.length) {
                    int bottomY = children[firstResizeIndex + 1].y + children[firstResizeIndex + 1].height;
                    int middleY = children[firstResizeIndex].y + topHeight + lineWidth;
                    int lowerHeight = bottomY - middleY;
                    
                    if(lowerHeight < minSize) {
                        topHeight -= (minSize - lowerHeight);
                        middleY -= (minSize - lowerHeight);
                        lowerHeight = minSize;
                    }
                    
                    children[firstResizeIndex + 1].resize(children[firstResizeIndex + 1].width, lowerHeight);
                    children[firstResizeIndex + 1].locate(children[firstResizeIndex + 1].x, middleY);
                }
                children[firstResizeIndex].resize(children[firstResizeIndex + 1].width, topHeight);
            }
            drawOnlySelf();
        }
        else {
            
            
            if(!children[activeChild].retainFocus) {
                if(firstResizeIndex > -1) {
                    drawOnlySelf();
                }
                //We don't need to check if the mouse pos is out of this module's bounds because the higher-ups will do that by default
                if(horizontal) {
                    if(mousex < children[activeChild].x) {
                        activeChild--;
                    }
                    else if (mousex > children[activeChild].x + children[activeChild].width) {
                        activeChild++;
                    }                 
                    
                    //This check is in both branches of if statement because otherwise exceptions can be thrown if focus is changed and then resizing is checked
                    if(activeChild < 0)  { activeChild = 0; } //If the child does happen to not exist than make sure bad things don't happen
                    if(activeChild >= children.length) { activeChild = children.length - 1; }
                    
                    if(Math.abs(mousex - (children[activeChild].x + children[activeChild].width)) <= resizeThreshold) {
                        firstResizeIndex = activeChild;
                        if(firstResizeIndex == children.length - 1) { firstResizeIndex = -1; }
                    }
                    else if(Math.abs(mousex - children[activeChild].x) <= resizeThreshold) {
                        firstResizeIndex = activeChild - 1;
                    }
                    else { 
                        if(firstResizeIndex > -1) {
                            firstResizeIndex = -1;
                            drawOnlySelf();
                        }
                        firstResizeIndex = -1;
                    }
                }
                else {
                    if(mousey < children[activeChild].y) {
                        activeChild--;
                    }
                    else if (mousey > children[activeChild].y + children[activeChild].height) {
                        activeChild++;
                    }
                    
                    if(activeChild < 0)  { activeChild = 0; } //If the child does happen to not exist than make sure bad things don't happen
                    if(activeChild >= children.length) { activeChild = children.length - 1; }
                    
                    if(Math.abs(mousey - (children[activeChild].y + children[activeChild].height)) <= resizeThreshold) {
                        firstResizeIndex = activeChild;
                        if(firstResizeIndex == children.length - 1) { firstResizeIndex = -1; }
                    }
                    else if(Math.abs(mousey - children[activeChild].y) <= resizeThreshold) {
                        firstResizeIndex = activeChild - 1;
                    }
                    else { 
                        if(firstResizeIndex > -1) {
                            firstResizeIndex = -1;
                            drawOnlySelf();
                        }
                        firstResizeIndex = -1;
                    }
                }
                
            }
            children[activeChild].mouseMove(mouseData.getShift(0, 0, -x, -y)); //Subtract top x and y because mousex and y are passed relatively
            
        }
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if(firstResizeIndex > -1 && e.getButton() == MouseEvent.BUTTON1) {
            resizeModule = true;
            retainFocus = true;
        }
        else { //Prevents multiple modules from resizing at once
            children[activeChild].mouseDown(e);
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if(resizeModule) {
            resizeModule = false;
            firstResizeIndex = -1;
            retainFocus = false;
            drawOnlySelf();
        }
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
            //g.drawImage(children[i].lastRender, children[i].x, children[i].y + ((i > 0) ? 1 : 0), null);
            g.drawImage(children[i].lastRender, children[i].x, children[i].y, null);
            g.setColor(lineColor);
            if(horizontal) {
                g.fillRect(children[i].x + children[i].width, children[i].y, lineWidth, children[i].height);
            }
            else {
                g.fillRect(children[i].x, children[i].y + children[i].height, children[i].width, lineWidth);
            }
        }
    }
    
    @Override
    public void draw(Module m) {
        graphics.drawImage(m.lastRender, m.x, m.y, null);
        drawParent();
    }
}