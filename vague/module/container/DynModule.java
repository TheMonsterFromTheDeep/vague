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
    
    static final int SIDE_WIDTH = 15; //Stores the widths of various parts of the DynModule
    static final int TOP_WIDTH = 15;
    static final int BOTTOM_WIDTH = 40;
    
    public static Color SIDE_COLOR = new Color(0xbabde3);
    public static Color VIEWPORT_COLOR = new Color(0xa7aacd);
    
    public DynModule(Module child, int width, int height) {             
        resize(width, height);
        
        child.resize(width - (2 * SIDE_WIDTH), height - (TOP_WIDTH + BOTTOM_WIDTH));
        child.locate(SIDE_WIDTH, TOP_WIDTH);
        
        child.setParent(this);
        
        children = new Module[] { child };
        setActiveChild(0);
    }
    
    @Override
    protected void resizeComponent(int width, int height) { //Basic resize method
        activeChild.resize(width - (2 * SIDE_WIDTH), height - (TOP_WIDTH + BOTTOM_WIDTH));
        System.err.println(activeIndex);
    }
    
    @Override
    public void mouseMove(MouseData mouseData) {
        activeChild.mouseMove(mouseData); //Pass the mouse data down to the active child along with the necessary shifts
    }
    
    @Override
    public void draw(Module m) {
        graphics.drawImage(m.lastRender, m.x, m.y, null);
        drawParent(this);
    }
    
    protected void drawViewport() {
        graphics.setColor(VIEWPORT_COLOR);
        graphics.drawRect(SIDE_WIDTH - 2, TOP_WIDTH - 2, activeChild.width + 3, activeChild.height + 3);
        graphics.drawRect(SIDE_WIDTH - 1, TOP_WIDTH - 1, activeChild.width + 2, activeChild.height + 2);
    }
    
    @Override
    protected void render(Graphics g) {
        g.setColor(SIDE_COLOR);
        g.fillRect(0, 0, width, height);
        
        g.drawImage(ImageData.data.DYN_MODULE_SPLIT, width - 20, height - 20, null);
        
        drawViewport();
        
        activeChild.drawLimited();
        g.drawImage(activeChild.lastRender, activeChild.x, activeChild.y, null);
    }
}