package vague.editor.settings;

import java.awt.Color;
import module.paint.GraphicsHandle;
import vague.menu.ToolOptions;

public class ColorSetting extends ToolSetting {
    static class ColorPanel extends ToolOptions.OptionPanel {
        private ColorPanel() {
            super(100);
        }
        
        public static ColorPanel create() { return new ColorPanel(); }
        
        @Override
        public void paint(GraphicsHandle g) {
            g.fill(Color.RED);
        }
    }
    
    private static ColorSetting setting;
    
    public Color color;
    
    private ColorSetting() {
        super("Color", ColorPanel.create());
        
        color = new Color(0);
    }
    
    public static ColorSetting getColorSetting() {
        if(setting == null) {
            setting = new ColorSetting();
        }
        return setting;
    }
}