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
    BufferedImage buffer;
    
    public Renderer(int width, int height) {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    public void drawLine(float sx, float sy, float ex, float ey, float width, Color c) {
        float dx = ex - sx;
        float dy = ey - sy;
        float xStep, yStep;
        
        float cw = width / 2;
        
        int color = c.getRGB();
        
        float length = (float)Math.sqrt(dx * dx + dy * dy);
        
        if(Math.abs(dx) < 0.001f) {
            xStep = 0;
            yStep = 1.0f;
        }
        else if(Math.abs(dy) < 0.001f) {
            xStep = 1.0f;
            yStep = 0;
        }
        else {
            xStep = dx / dy;
            yStep = dy / dx;
        }
        
        if(Math.abs(xStep) > 1.0) {
            yStep /= Math.abs(xStep);
            xStep /= Math.abs(xStep);
        }
        else if(Math.abs(yStep) > 1.0) {
            xStep /= Math.abs(yStep);
            yStep /= Math.abs(yStep);
        }
        
        //int imgWidth = (int)(Math.abs(ex - sx) + width * yStep) + 1;
        //int imgHeight = (int)(Math.abs(ey - sy) + width * xStep) + 1;
        
        float x, y;
        x = sx;
        y = sy;
        
        for(float f = 0; f < length; f++) {
            for(float w = -cw; w < cw; w++) {
                buffer.setRGB((int)(x - w * yStep), (int)(y + w * xStep), color);
            }
            x += xStep;
            y += yStep;
            System.err.println("Non-width coords: " + x + " " + y);
        }
    } 
}