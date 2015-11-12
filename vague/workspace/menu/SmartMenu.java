package vague.workspace.menu;

import java.awt.Color;
import vague.module.Module;
import vague.workspace.WorkTool;

/**
 * Represents a menu that fills a WorkTool class on its creation. Intelligent and gives suggestions
 * for what type of Module should fill the WorkTool based on its size / proximity to other WorkTools.
 * @author TheMonsterFromTheDeep
 */
public class SmartMenu extends Module {
    static final Color BG_COLOR = new Color(0xbfbfdd);
    
    private WorkTool tool;
    
    private SmartMenu(WorkTool tool) {
        this.tool = tool;
        this.bgColor = BG_COLOR;
    }
    
    public static SmartMenu create(WorkTool tool) {
        return new SmartMenu(tool);
    }
    
    @Override
    public void draw() {
        this.fillBackground();
        
        graphics.setColor(new Color(0x000000));
        graphics.drawRect(0, 0, width() - 2, height() - 2);
        
        this.drawText("Choose control:", 2, 3, 3);
    }
}