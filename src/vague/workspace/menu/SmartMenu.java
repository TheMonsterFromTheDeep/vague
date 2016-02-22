package vague.workspace.menu;

import java.awt.Color;
import java.awt.event.MouseEvent;
import vague.editor.Editor;
import module.Module;
import module.Window;
import module.paint.GraphicsHandle;
import vague.module.TestModule;
import module.util.Vector;
import vague.menu.ToolOptions;
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
            new ControlSelector("Editor", Editor.create(), 3, 20),
            new ControlSelector("Tool Options", ToolOptions.create(), 3, 66)
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
        repaint();
    }
    
    //When the SmartMenu is unfocused, it needs to update its buttons in case any appear as pressed
    @Override
    public void onUnfocus() {
        boolean redraw = false; //Stores whether the graphical state has changed and the Menu needs to be re-drawn
        for(ControlSelector cs : controlSelectors) {
            if(cs.pressed()) { //If any of the buttons are pressed, unpress them and make it so this will be re-drawn
                cs.update(false);
                redraw = true;
            }
        }
        if(redraw) { //If any of the buttons were pressed (and now unpressed), redraw to reflect changed graphical state
            repaint();
        }
    }
    
    @Override
    public void paint(GraphicsHandle graphics) {
        //this.fillBackground();
        
        graphics.setColor(new Color(0x000000));
        graphics.drawRect(0, 0, width() - 1, height() - 1);
        
        this.drawText("Choose control:", 2, 3, 3, graphics);
        for(int i = 0; i < controlSelectors.length; i++) {
            graphics.drawImage(controlSelectors[i].draw(), controlSelectors[i].x, controlSelectors[i].y, null);
        }
    }
}