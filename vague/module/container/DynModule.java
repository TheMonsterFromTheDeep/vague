package vague.module.container;

import java.awt.Color;
import java.awt.Graphics;
import vague.module.Module;

/**
 * The DynModule class contains a single module and also allows itself to be split into a ModulePane
 * containing multiple modules. Multiple DynModule classes can also be merged.
 * @author TheMonsterFromTheDeep
 */
public class DynModule extends Container {
    
    static final int SIDE_WIDTH = 15; //Stores the widths of various parts of the DynModule
    static final int TOP_WIDTH = 15;
    static final int BOTTOM_WIDTH = 40;
    
    public static Color SIDE_COLOR = new Color(0xa7aacd);
    
    public DynModule(Module child, int width, int height) {             
        resize(width, height);
        
        child.resize(width - (2 * SIDE_WIDTH), height - (TOP_WIDTH + BOTTOM_WIDTH));
        child.locate(SIDE_WIDTH, TOP_WIDTH);
        
        children = new Module[] { child };
        setActiveChild(0);
    }
    
    @Override
    protected void resizeComponent(int width, int height) { //Basic resize method
        activeChild.resize(width - (2 * SIDE_WIDTH), height - (TOP_WIDTH + BOTTOM_WIDTH));
    }
    
    @Override
    protected void render(Graphics g) {
        g.setColor(SIDE_COLOR);
        g.fillRect(0, 0, width, height);
        
        activeChild.drawLimited();
        g.drawImage(activeChild.lastRender, activeChild.x + SIDE_WIDTH, activeChild.y + TOP_WIDTH, null);
    }
}