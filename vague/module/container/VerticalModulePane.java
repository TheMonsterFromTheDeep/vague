package vague.module.container;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import vague.module.Module;
import vague.module.MouseData;

public class VerticalModulePane extends Container {   
    int firstResizeIndex = -1; //Stores the index of the first module during a module resize - if no resizing is possibe, equals -1
    boolean resizeModule = false; //Stores whether a module is currently being resized
    
    final static Color LINE_COLOR = new Color(0xb0b3dc); //Stores the color of the lines separating modules
    final static Color VIEWPORT_COLOR = new Color(0xbfc2e7); //Stores the color of the viewport for each module
    static final int THRESHOLD = 15; //The pixel distance from which it is possible to resize modules
    static final int MIN_SIZE = 30; //Minimum size, in pixels, of child modules
    
    BufferedImage resizeControl;
    
    final static int LINE_WIDTH = 3; //Stores the width of the line separating panes; used in calculating widths when resized
    
    private void initParent() {
        for(int i = 0; i < children.length; i++) { children[i].setParent(this); }
    }
    
    public VerticalModulePane(int width, int height, Module[] childmods) {
        //Calculates size to evenly divide pane into modules - subtracts width of lines
        children = childmods;
        
        int size = (height - ((children.length - 1) * LINE_WIDTH)) / (children.length);
        int total = 0;
        for(int i = 0; i < children.length - 1; i++) {
            children[i].resize(width,size);
            children[i].locate(0, ((size + LINE_WIDTH) * i));
            total += size + LINE_WIDTH;
        }
        children[children.length - 1].resize(width, height - total);
        children[children.length - 1].locate(0, total);
        this.width = width;
        this.height = height;
        
        resize(width, height);
        
        initParent();
    }
    
    @Override
    protected void resizeComponent(int width, int height) { //Basic resize method
        int ypos = 0;
        for(int i = 0; i < children.length; i++) {
            children[i].resize(width, (int)Math.round(height * ((double)children[i].height / (double)this.height)));
            children[i].locate(0, ypos);
            ypos += children[i].height + LINE_WIDTH;
        }
    }
    
    @Override
    public void mouseMove(MouseData mouseData) {
        if(!activeChild.retainFocus) { //If the active child doesn't retain focus, check to see if another object is is focus
            if(!activeChild.mouseInside(mouseData)) { //If the active child no longer contains mouse, it shouldn't necessarily be in focus
                for(int i = 0; i < children.length; i++) { //Iterate through child modules
                    if(children[i].mouseInside(mouseData)) { //if the mouse is inside a child module, set that one as active
                        setActiveChild(i);
                    }
                }
            } //If none of the modules contain the mouse, the originally active one remains active.
        }
        activeChild.mouseMove(mouseData.getShift(-x,-y,-x,-y)); //Pass the mouse data down to the active child along with the necessary shifts
    }
    
    private void drawViewport(int x, int y, int width, int height) {
        graphics.drawRect(x, y, width, height);
        graphics.drawRect(x + 1, y + 1, width - 2, height - 2);
    }
    
    @Override
    public void draw(Module m) { //Overloaded so viewport can be drawn
        graphics.drawImage(m.lastRender, m.x, m.y, null);
        graphics.setColor(VIEWPORT_COLOR);
        drawViewport(m.x,m.y,m.width,m.height);
        drawParent(this);
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
    }
}