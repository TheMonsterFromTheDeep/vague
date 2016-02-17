package vague.editor.settings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import module.paint.GraphicsHandle;
import module.util.Vector;
import vague.menu.ToolOptions;

public class ColorSetting extends ToolSetting {
    static class ColorPanel extends ToolOptions.OptionPanel {
        
        private static BufferedImage createColorWheel(int radius) {
            BufferedImage wheel = new BufferedImage(radius * 2, radius * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics g = wheel.createGraphics();
            
            for(int x = 0; x < 2 * radius; x++) {
                for(int y = 0; y < 2 * radius; y++) {
                    float dist = (float)Math.sqrt(((x - radius) * (x - radius)) + ((y - radius) * (y - radius)));
                    if(dist <= radius) {
                        float hue = (float)(Math.atan2(y - radius, x - radius) / (Math.PI * 2));
                        g.setColor(new Color(Color.HSBtoRGB(hue, dist / radius, 1)));
                        g.fillRect(x, y, 1, 1);
                    }
                }
            }
            
            return wheel;
        }
        
        private BufferedImage colorWheel;
        
        private ColorPanel() {
            super(100);
            
            colorWheel = createColorWheel(50);
        }
        
        public static ColorPanel create() { return new ColorPanel(); }
        
        @Override
        public void paint(GraphicsHandle g) {
            g.fill(Color.BLACK);
            
            g.drawImage(colorWheel, 0, 0);
            g.setColor(new Color(0xff000000 - ((int)(getColorSetting().value * 255) << 24), true));
            g.fillRect(0, 0, 100, 100);
        }
        
        @Override
        public void mouseMove(Vector pos, Vector dif) {
            getColorSetting().value = (float)pos.y / 100;
            repaint();
        }
    }
    
    private static ColorSetting setting;
    
    public float hue;
    public float saturation;
    public float value;
    
    //public Color color;
    
    private ColorSetting() {
        super("Color", ColorPanel.create());
        
        hue = 0;
        saturation = 1;
        value = 1;
        
        //color = new Color(0);
    }
    
    public Color getColor() {
        return new Color(Color.HSBtoRGB(hue, saturation, value));
    }
    
    public static ColorSetting getColorSetting() {
        if(setting == null) {
            setting = new ColorSetting();
        }
        return setting;
    }
}