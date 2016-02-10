package vague.editor.settings;

import java.awt.Color;

public class ColorSetting extends ToolSetting {
    private static ColorSetting setting;
    
    public Color color;
    
    private ColorSetting() {
        super("Color");
        
        color = new Color(0);
    }
    
    public static ColorSetting getColorSetting() {
        if(setting == null) {
            setting = new ColorSetting();
        }
        return setting;
    }
}