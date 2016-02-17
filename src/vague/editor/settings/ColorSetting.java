package vague.editor.settings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import module.paint.GraphicsHandle;
import module.util.Vector;
import vague.Resources;
import vague.menu.ToolOptions;

public class ColorSetting extends ToolSetting {
    static class ColorPanel extends ToolOptions.OptionPanel {
        
        static final int WHEEL_RADIUS = 64;
        static final int WHEEL_OFFSET = 8;
        static final int WHEEL_END = WHEEL_OFFSET + 2 * WHEEL_RADIUS;
        static final int WHEEL_CENTER = WHEEL_RADIUS + WHEEL_OFFSET;
        static final int BAR_HEIGHT = WHEEL_RADIUS * 2;
        static final int BAR_WIDTH = 16;
        static final int BAR_CALC_HEIGHT = BAR_HEIGHT - BAR_WIDTH;
        static final int BAR_OFFSET = WHEEL_END + 8;
        static final int BAR_END = BAR_OFFSET + BAR_WIDTH;
        static final int BAR_CURSOR_X = BAR_OFFSET + BAR_WIDTH / 2;
        static final int BAR_CURSOR_BASEY = WHEEL_OFFSET + BAR_WIDTH / 2;
        
        static final Color BG_COLOR = new Color(0xc1c1dd);
        
        /**
         * 
         * @param radius
         * @return Two BufferedImages: The first being the color wheel, the second being its mask.
         */
        private static BufferedImage[] createColorWheel(int radius, Color maskColor) {
            BufferedImage wheel = new BufferedImage(radius * 2, radius * 2, BufferedImage.TYPE_INT_ARGB);
            BufferedImage mask = new BufferedImage(radius * 2, radius * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics g = wheel.createGraphics();
            
            for(int x = 0; x < 2 * radius; x++) {
                for(int y = 0; y < 2 * radius; y++) {
                    float dist = (float)Math.sqrt(((x - radius) * (x - radius)) + ((y - radius) * (y - radius)));
                    if(dist <= radius) {
                        float hue = (float)(Math.atan2(y - radius, x - radius) / (Math.PI * 2));
                        g.setColor(new Color(Color.HSBtoRGB(hue, dist / radius, 1)));
                        g.fillRect(x, y, 1, 1);
                    }
                    else {
                        mask.setRGB(x, y, maskColor.getRGB());
                    }
                }
            }
            
            return new BufferedImage[] { wheel, mask };
        }
        
        private static BufferedImage createBar(int height) {
            BufferedImage bar = new BufferedImage(BAR_WIDTH, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bar.createGraphics();
            
            final int center = BAR_WIDTH / 2;
            final int lowcenter = height - (BAR_WIDTH / 2);
            final int changeheight = height - BAR_WIDTH;
            
            for(int y = center; y < lowcenter; y++) {
                g.setColor(new Color(Color.HSBtoRGB(0, 0, 1 - ((float)(y - center) / changeheight))));
                g.fillRect(0, y, BAR_WIDTH, 1);
            }
            
            g.setColor(new Color(0xffffff));
            g.fillRect(0, 0, BAR_WIDTH, center);
            
            g.setColor(new Color(0x0));
            g.fillRect(0, lowcenter, BAR_WIDTH, height);
            
            int transparent = 0x0;
            
            //ROUND CORNERS
            
            for(int x = 0; x < BAR_WIDTH; x++) {
                for(int y = 0; y < center; y++) {
                    if(Math.sqrt(((x - center) * (x - center)) + ((y - center) * (y - center))) > center) {
                        bar.setRGB(x, y, transparent);
                    }
                }
            }
            
            for(int x = 0; x < BAR_WIDTH; x++) {
                for(int y = lowcenter; y < height; y++) {
                    if(Math.sqrt(((x - center) * (x - center)) + ((y - lowcenter) * (y - lowcenter))) > center) {
                        bar.setRGB(x, y, transparent);
                    }
                }
            }
            
            //DONE WITH ROUND CORNERS
            
            return bar;
        }
        
        private BufferedImage colorWheel;
        private BufferedImage colorWheelMask;
        private BufferedImage bar;
        private BufferedImage hsvCursor; //Reference to the HSV cursor
        private BufferedImage valueCursor; //Reference to the value cursor
        //TODO: Consolidate all redundant mousedown booleans into one control thing
        boolean mouseDown;
        
        private ColorPanel() {
            super(256);
            
            BufferedImage[] tmp = createColorWheel(WHEEL_RADIUS, BG_COLOR);
            colorWheel = tmp[0];
            colorWheelMask = tmp[1];
            bar = createBar(WHEEL_RADIUS * 2);
            hsvCursor = Resources.bank.SETTING_COLOR_HSV_CURSOR;
            valueCursor = Resources.bank.SETTING_COLOR_VALUE_CURSOR;
        }
        
        public static ColorPanel create() { return new ColorPanel(); }
        
        @Override
        public void paint(GraphicsHandle g) {
            ColorSetting cs = getColorSetting();
            
            g.fill(BG_COLOR);
            
            g.drawImage(colorWheel, WHEEL_OFFSET, WHEEL_OFFSET);
            g.setColor(new Color(0xff000000 - ((int)(cs.value * 255) << 24), true));
            g.fillRect(WHEEL_OFFSET, WHEEL_OFFSET, BAR_HEIGHT, BAR_HEIGHT);
            g.drawImage(colorWheelMask, WHEEL_OFFSET, WHEEL_OFFSET);
            
            g.drawImage(bar, BAR_OFFSET, WHEEL_OFFSET);
            
            double angle = cs.hue * 2 * Math.PI;
            float dist = cs.saturation * WHEEL_RADIUS;
            
            int cx = (int)(Math.cos(angle) * dist);
            int cy = (int)(Math.sin(angle) * dist);
            
            g.drawImage(hsvCursor, WHEEL_CENTER + cx - hsvCursor.getWidth() / 2, WHEEL_CENTER + cy - hsvCursor.getHeight() / 2);
            
            g.setColor(cs.getColor());
            g.fillRect(WHEEL_OFFSET, WHEEL_END + 8, BAR_END, WHEEL_OFFSET + 32);
            g.drawImage(valueCursor, BAR_CURSOR_X - (valueCursor.getWidth() / 2), BAR_CURSOR_BASEY + (int)((1 - cs.value) * BAR_CALC_HEIGHT) - (valueCursor.getHeight() / 2));
        }
        
        void updateColor(Vector pos) {
            float dist = (float)Math.sqrt(((pos.x - WHEEL_CENTER) * (pos.x - WHEEL_CENTER)) + ((pos.y - WHEEL_CENTER) * (pos.y - WHEEL_CENTER)));
            if(dist < WHEEL_RADIUS) {
                float hue = (float)(Math.atan2(pos.y - WHEEL_CENTER, pos.x - WHEEL_CENTER) / (2 * Math.PI));
                ColorSetting cs = getColorSetting();
                cs.hue = hue;
                cs.saturation = dist / WHEEL_RADIUS;
                repaint();
            }
            else if(pos.x > BAR_OFFSET && pos.x < BAR_END && pos.y > WHEEL_OFFSET && pos.y < WHEEL_END) {
                float value;
                if(pos.y < BAR_CURSOR_BASEY) {
                    value = 1;
                }
                else if (pos.y > BAR_HEIGHT - (BAR_WIDTH / 2)) {
                    value = 0;
                    getColorSetting().saturation = 0;
                }
                else { value = 1 - ((float)(pos.y - BAR_CURSOR_BASEY) / BAR_CALC_HEIGHT); }
                getColorSetting().value = value;
                repaint();
            }
        }
        
        @Override
        public void mouseDown(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) { 
                mouseDown = true;
                updateColor(mousePosition());
            }
        }
        
        @Override
        public void mouseUp(MouseEvent e) {
            mouseDown = false;
        }
        
        @Override
        public void mouseMove(Vector pos, Vector dif) {
            if(mouseDown) {
                updateColor(pos);
            }
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
        saturation = 0;
        value = 0;
        
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