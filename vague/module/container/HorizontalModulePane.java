package vague.module.container;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import vague.module.Module;
import vague.module.MouseData;
import vague.util.ImageData;

public class HorizontalModulePane extends Container {   
    int firstResizeIndex = -1; //Stores the index of the first module during a module resize - if no resizing is possibe, equals -1
    boolean resizeModule = false; //Stores whether a module is currently being resized
    
    boolean resizingChild = false; //Stores whether the pane is currently resizing child modules
    boolean drawArrow = false; //Stores whether the resize arrow should be drawn
    int resizeIndex = -1; //Stores the index of the child being resized  
    
    final static Color LINE_COLOR = new Color(0xb0b3dc); //Stores the color of the lines separating modules
    final static Color VIEWPORT_COLOR = new Color(0xbfc2e7); //Stores the color of the viewport for each module
    static final int THRESHOLD = 12; //The pixel distance from which it is possible to resize modules
    static final int MIN_SIZE = 60; //Minimum size, in pixels, of child modules
    
    int resizeOffset = 0; //Stores the shift offset of the resize control if moveed beyond the possible bounds for resize
    
    BufferedImage resizeControl;
    
    final static int LINE_WIDTH = 3; //Stores the width of the line separating panes; used in calculating widths when resized
    
    private void initParent() {
        for(int i = 0; i < children.length; i++) { children[i].setParent(this); }
    }
    
    public HorizontalModulePane(int width, int height, Module[] childmods) {
        //Calculates size to evenly divide pane into modules - subtracts width of lines
        children = childmods;
        
        int size = (width - ((children.length - 1) * LINE_WIDTH)) / (children.length);
        int total = 0;
        for(int i = 0; i < children.length - 1; i++) {
            children[i].resize(size,height);
            children[i].locate(((size + LINE_WIDTH) * i), 0);
            total += size + LINE_WIDTH;
        }
        children[children.length - 1].resize(width - total, height);
        children[children.length - 1].locate(total, 0);
        this.width = width;
        this.height = height;
        
        resize(width, height);
        
        initParent();
    }
    
    @Override
    protected void resizeComponent(int width, int height) { //Basic resize method
        int xpos = 0;
        for(int i = 0; i < children.length; i++) {
            int newWidth = (int)Math.round(width * ((double)children[i].width / (double)this.width));
            if(newWidth < children[i].minwidth) {
                newWidth = children[i].minwidth;
            }
            children[i].resize(newWidth, height);
            children[i].locate(xpos, 0);
            xpos += children[i].width + LINE_WIDTH;
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(!activeChild.retainFocus) {//If the active child is retaining focus, then nothing should resize
                if(activeIndex < children.length) { //If the active child is not the last, check for as first resize
                    if(Math.abs((activeChild.x) - getMouseX()) < THRESHOLD && activeIndex > 0) {
                        resizeIndex = activeIndex - 1;
                        resizingChild = true;
                        retainFocus = true;
                        resizeOffset = 0;
                        draw();
                    }
                    else if(Math.abs((activeChild.x + activeChild.width) - getMouseX()) < THRESHOLD && activeIndex < children.length - 1) {
                        resizeIndex = activeIndex;
                        resizingChild = true;
                        retainFocus = true;
                        resizeOffset = 0;
                        draw();
                    }
                }
            }
        }
        if(!resizingChild) {
            activeChild.mouseDown(e);
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(resizingChild) {
            resizingChild = false;
            retainFocus = false;
            resizeOffset = 0;
            draw();
        }
        activeChild.mouseUp(e);
    }
    
    @Override
    public void mouseMove(MouseData mouseData) {     
        if(!resizingChild) {
            if(!activeChild.retainFocus) { //If the active child doesn't retain focus, check to see if another object is is focus
                if(!activeChild.mouseInside(mouseData)) { //If the active child no longer contains mouse, it shouldn't necessarily be in focus
                    int last = activeIndex; //Store the index of the last active child
                    for(int i = 0; i < children.length; i++) { //Iterate through child modules
                        if(children[i].mouseInside(mouseData)) { //if the mouse is inside a child module, set that one as active
                            setActiveChild(i);
                        }
                    }
                    children[last].mouseMove(mouseData.getShift(-x,-y,-x,-y)); //Move the mouse for the active child in order to prevent some rendering errors
                    draw();
                } //If none of the modules contain the mouse, the originally active one remains active.
            }
            activeChild.mouseMove(mouseData.getShift(-activeChild.x,-activeChild.y,-activeChild.x,-activeChild.y)); //Pass the mouse data down to the active child along with the necessary shifts
            if(Math.abs((activeChild.x) - getMouseX()) < THRESHOLD && activeIndex > 0) {
                drawArrow = true;
                draw();
            }
            else if(Math.abs((activeChild.x + activeChild.width) - getMouseX()) < THRESHOLD && activeIndex < children.length - 1) {
                drawArrow = true;
                draw();
            }
            else if(drawArrow) { //If it is true but shouldn't be, invert it and re-draw
                drawArrow = false;
                draw(); 
            }
        }       
        else {
            int leftWidth = children[resizeIndex].width;
            int rightWidth =  children[resizeIndex + 1].width;
            if(Math.abs(resizeOffset) < THRESHOLD) {
                leftWidth = children[resizeIndex].width + mouseData.getDifX(); //Mouse moving left: module shrinks; mouse moving right: module grows
                rightWidth = children[resizeIndex + 1].width - mouseData.getDifX(); //Mouse moving left: module grows; mouse moving right: module shrinks 
            }
            
            if(leftWidth < children[resizeIndex].minwidth) {
                int dif = (children[resizeIndex].minwidth - leftWidth); //Stores the difference between the left width and the minimum size
                leftWidth = children[resizeIndex].minwidth;
                rightWidth -= dif; //The right size needs to be changed by the difference so that it will line up with the left module
            }
            if(rightWidth < children[resizeIndex + 1].minwidth) {
                int dif = (children[resizeIndex + 1].minwidth - rightWidth); //Stores the difference between the right width and the minimum size
                rightWidth = children[resizeIndex + 1].minwidth;
                leftWidth -= dif; //The left size needs to be changed by the difference so that it will line up with the right module
            }
            
            children[resizeIndex].resize(leftWidth, height); //Mouse moving up: module shrinks; mouse moving down: module grows
            children[resizeIndex + 1].locate(children[resizeIndex].x + leftWidth + LINE_WIDTH, 0);
            children[resizeIndex + 1].resize(rightWidth, height); //Mouse moving up: module grows; mouse moving down: module shrinks       
            
            resizeOffset = mouseData.getX() - (children[resizeIndex].x + children[resizeIndex].width);
            
            drawChild(children[resizeIndex]);
            drawChild(children[resizeIndex + 1]);
            
            drawLine(children[resizeIndex].x + children[resizeIndex].width);
            drawResizeArrow(getMouseX() - resizeOffset, getMouseY());
            drawParent(this);
        }
    }
    
    private void drawViewport(int x, int y, int width, int height) {
        graphics.drawRect(x, y, width, height);
        graphics.drawRect(x + 1, y + 1, width - 2, height - 2);
    }
    
    private void drawLine(int x) {
        graphics.setColor(LINE_COLOR);
        graphics.fillRect(x,0,LINE_WIDTH,height);
    }
    
    private void drawResizeArrow(int x, int y) {
        graphics.drawImage(ImageData.data.RESIZE_ARROW_HORIZONTAL, x - 16, y - 5, null);
    }
    
    @Override
    public void draw(Module m) { //Overloaded so viewport can be drawn
        graphics.drawImage(m.lastRender, m.x, m.y, null);
        graphics.setColor(VIEWPORT_COLOR);
        drawViewport(m.x,m.y,m.width,m.height);
        drawParent(this);
    }
    
    public void drawChild(Module m) { //Draw child without redrawing parent
        graphics.drawImage(m.lastRender, m.x, m.y, null);
        graphics.setColor(VIEWPORT_COLOR);
        drawViewport(m.x,m.y,m.width,m.height);
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(LINE_COLOR);
        g.fillRect(0, 0, this.width, this.height); //Fills a rectangle in bg; looks like lines when seen through cracks between modules
        g.setColor(VIEWPORT_COLOR);
        for(int i = 0; i < children.length; i++) {
            g.drawImage(children[i].lastRender, children[i].x, children[i].y, null);
            drawViewport(children[i].x, children[i].y, children[i].width, children[i].height);
        }
        if(drawArrow) {
            drawResizeArrow(getMouseX(), getMouseY());
        }
    }
}