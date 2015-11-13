package vague.workspace.menu;

import java.awt.Color;
import java.awt.event.MouseEvent;
import vague.module.Module;
import vague.module.TestModule;
import vague.util.Vector;
import vague.workspace.WorkTool;

/**
 * Represents a menu that fills a WorkTool class on its creation. Intelligent and gives suggestions
 * for what type of Module should fill the WorkTool based on its size / proximity to other WorkTools.
 * @author TheMonsterFromTheDeep
 */
public class SmartMenu extends Module {
    static final Color BG_COLOR = new Color(0xafafdd);
    
    private WorkTool tool;
    private ControlSelector[] controlSelectors;
    
    private SmartMenu(WorkTool tool) {
        this.tool = tool;
        this.bgColor = BG_COLOR;
        
        controlSelectors = new ControlSelector[] {
            //Create a dummy test module Module to test SmartMenu module module
            new ControlSelector("Test", TestModule.create(20,20), 3, 20),
            new ControlSelector("Test 2", TestModule.create(20,20), 3, 43)
        };
    }
    
    public static SmartMenu create(WorkTool tool) {
        return new SmartMenu(tool);
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        for(ControlSelector cs : controlSelectors) {
            if(cs.pressed()) {
                tool.fill(cs.getFill());
            }
        }
    }
    
    @Override
    public void mouseMove(Vector mousePos, Vector mouseDif) {
        for(ControlSelector cs : controlSelectors) {
            cs.update(mousePos.x, mousePos.y);
        }
        redraw();
    }
    
    @Override
    public void draw() {
        this.fillBackground();
        
        graphics.setColor(new Color(0x000000));
        graphics.drawRect(0, 0, width() - 1, height() - 1);
        
        this.drawText("Choose control:", 2, 3, 3);
        for(int i = 0; i < controlSelectors.length; i++) {
            graphics.drawImage(controlSelectors[i].draw(), controlSelectors[i].x, controlSelectors[i].y, null);
        }
    }
}