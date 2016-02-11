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
            
            int lcos = 50;
            int lsin = 50;
            int ncos, nsin;
            
            for(float i = 0; i < 360; i += 0.125) {
                float hue = i / 360;
                double rad = Math.toRadians(i);
                for(int s = 0; s < 50; s++) {
                    g.setColor(new Color(Color.HSBtoRGB(hue, s / 50f, 1)));
                    ncos = 50 + (int)Math.round(s * Math.cos(rad));
                    nsin = 50 + (int)Math.round(s * Math.sin(rad));
                    //System.err.println(":: " + ncos + " " + nsin);
                    g.drawLine(lcos, lsin, ncos, nsin);
                    lcos = ncos;
                    lsin = nsin;
                }
            }
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