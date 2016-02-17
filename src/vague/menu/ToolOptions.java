package vague.menu;

import java.awt.Color;
import java.awt.event.MouseEvent;
import module.Window;
import module.Module;
import module.paint.GraphicsHandle;
import module.util.Vector;
import vague.VagueWindow;
import vague.editor.Context;
import vague.editor.settings.ToolSetting;

/**
 * The ToolOptions menu displays option panels for the various ToolSetting objects that are currently active.
 * @author TheMonsterFromTheDeep
 */
public class ToolOptions extends Module {
    public static class OptionPanel extends Module {
        protected OptionPanel() {
            super(VagueWindow.getWindow());
        }
        
        protected OptionPanel(int height) {
            super(VagueWindow.getWindow(), 50, height);
        }
        
        public static OptionPanel create() {
            return new OptionPanel();
        }
    }
    
    static final Color BG_COLOR = new Color(0xc2c2dd);
    
    OptionPanel[] options;
    OptionPanel activeOption;
    
    private void update() {
        ToolSetting[] settings = Context.getContext().toolSettings.settings;
        
        options = new OptionPanel[settings.length];
        for(int i = 0; i < settings.length; i++) {
            options[i] = settings[i].panel;
            options[i].setParent(this); //Parent the panel
            options[i].resize(width(), options[i].height()); //Make sure the panel is filling this panel horizontally
        }
        
        //DEBUG ONLY:
        activeOption = options[0];
    }
    
    private ToolOptions(Window w) {
        super(w);
        
        options = new OptionPanel[0];
    }
    
    public static ToolOptions create(Window w) {
        return new ToolOptions(w);
    }
    
    @Override
    public void onResize(Vector newSize) {
        for(OptionPanel p : options) {
            p.resize(newSize.x, p.height()); //Resize all option panels along the x axis
        }
    }
    
    @Override
    public void onFocus() {
        update();
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
    }
    
    @Override
    public void mouseMove(Vector mousePos, Vector mouseDif) {
        //TODO: Implement focus changing, etc...
        //for now, just implement basic control for testing individual controls
        activeOption.mouseMove(mousePos, mouseDif);
    }
    
    @Override
    public void paint(GraphicsHandle handle) {
        handle.fill(BG_COLOR);
        
        for(OptionPanel p : options) {
            p.repaint();
        }
    }
}