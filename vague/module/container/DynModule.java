package vague.module.container;

import java.awt.Color;
import java.awt.Graphics;
import vague.module.Module;
import vague.module.MouseData;
import vague.util.ImageData;

/**
 * The DynModule class contains a single module and also allows itself to be split into a ModulePane
 * containing multiple modules. Multiple DynModule classes can also be merged.
 * @author TheMonsterFromTheDeep
 */
public class DynModule extends Container {
    static final int BOTTOM_WIDTH = 30;
    
    public static Color SIDE_COLOR = new Color(0xbabde3);
    public static Color VIEWPORT_COLOR = new Color(0xa7aacd);
    
    final static int SPLIT_NONE = 0; //The different states for readiness for splitting
    final static int SPLIT_BR = 1;
    final static int SPLIT_BOTTOM = 2;
    final static int SPLIT_TL = 3;
    
    int splitStatus = SPLIT_NONE;
    
    public DynModule(Module child, int width, int height) { 
        this.minwidth = 30;
        this.minheight = BOTTOM_WIDTH + 30;
        
        resize(width, height);
        
        child.resize(width, height - BOTTOM_WIDTH);
        child.locate(0, 0);
        
        child.setParent(this);
        
        children = new Module[] { child };
        setActiveChild(0);
    }
    
    @Override
    protected void resizeComponent(int width, int height) { //Basic resize method
        activeChild.resize(width, height - BOTTOM_WIDTH);
    }
    
    @Override
    public void mouseMove(MouseData mouseData) {
        if((mouseData.getX() + 30 > width) && (mouseData.getY() + 30 > height)) {
            splitStatus = SPLIT_BR;
               
            drawLimited();
            drawBRArrow(mouseData.getX(), mouseData.getY());
            drawParent(this);
        }
        else {
            activeChild.mouseMove(mouseData); //Pass the mouse data down to the active child along with the necessary shifts 
            if(splitStatus != SPLIT_NONE) {
                splitStatus = SPLIT_NONE;
                draw();
            }
        }
    }
    
    @Override
    public void draw(Module m) {
        graphics.drawImage(m.lastRender, m.x, m.y, null);
        drawParent(this);
    }
    
    //Draws the bottom right ararow
    protected void drawBRArrow(int x, int y) {
        graphics.drawImage(ImageData.data.SPLIT_ARROW_BR, x - 16, y - 16, null);
    }

    @Override
    protected void render(Graphics g) {
        g.setColor(SIDE_COLOR);
        g.fillRect(0, 0, width, height);
        
        g.drawImage(ImageData.data.DYN_MODULE_SPLIT, width - 20, height - 20, null);
        
        activeChild.drawLimited();
        g.drawImage(activeChild.lastRender, activeChild.x, activeChild.y, null);
        
        if(splitStatus != SPLIT_NONE) {
            switch(splitStatus) {
                case SPLIT_BR:
                    drawBRArrow(getMouseX(),getMouseY());
                    break;
                case SPLIT_BOTTOM:
                    break;
                case SPLIT_TL:
                    break;
            }
        }
    }
}