package vague.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * The Renderer is the most important part of the vague drawing engine.
 * 
 * It draws basically everything that is needed in vague, and every sort of Shape
 * relies on it.
 * 
 * It essentially acts as a custom-built Graphics class, and as such will probably
 * be rather slow...
 * @author TheMonsterFromTheDeep
 */
public class Renderer {    
    public static BufferedImage drawLine(float sx, float sy, float ex, float ey, float width, Color c) {
        int color = c.getRGB();
        
        float xChange, yChange;
        
        float dw = width / 2;
        
        xChange = ex - sx;
        yChange = ey - sy;
        float length = (float)Math.sqrt((xChange * xChange) + (yChange * yChange));
        if(xChange > 1.0f) {
            yChange /= xChange;
            xChange = 1.0f;
        }
        if(xChange < -1.0f) {
            yChange /= xChange;
            xChange = -1.0f;
        }
        if(yChange > 1.0f) {
            xChange /= yChange;
            yChange = 1.0f;
        }
        if(yChange < -1.0f) {
            xChange /= yChange;
            yChange = -1.0f;
        }
        
        BufferedImage buffer = new BufferedImage((int)(width * yChange + Math.abs(ex - sx)) + 1, (int)(width * xChange + Math.abs(ey - sy)) + 1, BufferedImage.TYPE_INT_ARGB);
        
        System.err.println("Buffer size: " + buffer.getWidth() + " " + buffer.getHeight());
        
        float dx = yChange * dw, dy = xChange * dw;
        if(dx < 0) { dx = buffer.getWidth() - dx; }
        if(dy < 0) { dy = buffer.getHeight() - dy; }
        System.err.println("Starting position: " + dx + " " + dy);
        while(dx < buffer.getWidth()) {
            for(float w = -dw; w < dw; w++) {
                int x = (int)(dx - (w * yChange)) - 1;
                int y = (int)(dy + (w * xChange));
                //System.err.println("Coordinate: " + x + " " + y);
                buffer.setRGB(x, y, color);
                //buffer.setRGB(x + 1, y, color);
                //buffer.setRGB(x, y + 1, color);
                //buffer.setRGB(x + 1, y + 1, color);
            }
            dx += xChange;
            dy += yChange;
        }
        return buffer;
    } 
}