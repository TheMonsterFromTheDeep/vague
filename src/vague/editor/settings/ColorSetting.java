package vague.editor.settings;

import java.awt.Color;
import module.paint.GraphicsHandle;
import vague.menu.ToolOptions;

public class ColorSetting extends ToolSetting {
    static class ColorPanel extends ToolOptions.OptionPanel {
        
        private ColorPanel() {
            super(300);
        }
        
        public static ColorPanel create() { return new ColorPanel(); }
        
        @Override
        public void paint(GraphicsHandle g) {
            g.fill(Color.BLACK);
            
            /*int lcos = 50;
            int lsin = 50;
            int ncos, nsin;
            
            for(float i = 0; i < 360; i += 0.0625) {
                float hue = i / 360;
                double rad = Math.toRadians(i);
                for(float s = 0; s < 50; s += 0.0625) {
                    g.setColor(new Color(Color.HSBtoRGB(hue, s / 50, 1)));
                    ncos = 50 + (int)Math.round(s * Math.cos(rad));
                    nsin = 50 + (int)Math.round(s * Math.sin(rad));
                    g.drawLine(lcos, lsin, ncos, nsin);
                    lcos = ncos;
                    lsin = nsin;
                }
            }*/
            
            int centerx = width() / 2;
            int centery = 150;
            int radius = 150;
            
            for(int x = centerx - radius; x < centerx + radius; x++) {
                for(int y = centery - radius; y < centery + radius; y++) {
                    float dist = (float)Math.sqrt(((x - centerx) * (x - centerx)) + ((y - centery) * (y - centery)));
                    if(dist <= radius) {
                        float hue = (float)(Math.atan2(y - centery, x - centerx) / (Math.PI * 2));
                        g.setColor(new Color(Color.HSBtoRGB(hue, dist / radius, 1)));
                        g.fillRect(x, y, 1, 1);
                    }
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