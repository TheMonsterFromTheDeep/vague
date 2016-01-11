package module.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * A wrapper to a Graphics object with an offset provided by a Window object. The GraphicsHandle is passed to a Module, which then
 * uses it for drawing. A properly created GraphicsHandle is guaranteed to provide a correct graphical offset.
 */
public class GraphicsHandle {
    private int offsetx;
    private int offsety;
    
    private Graphics graphics;
    
    public GraphicsHandle(int x, int y, Graphics g) {
        offsetx = x;
        offsety = y;
        graphics = g;
    }
    
    public void setColor(Color c) {
        graphics.setColor(c);
    }
    
    public void drawRect(int x, int y, int width, int height) {
        graphics.drawRect(x, y, width, height);
    }
    
    public void fillRect(int x, int y, int width, int height) {
        graphics.fillRect(x, y, width, height);
    }
    
    public void drawImage(Image image, int x, int y) {
        graphics.drawImage(image, x, y, null);
    }
    
    public void drawImage(Image image, int x, int y, ImageObserver io) {
        graphics.drawImage(image, x, y, io);
    }
}