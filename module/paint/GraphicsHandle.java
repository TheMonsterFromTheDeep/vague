package module.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 * A wrapper to a Graphics object with an offset provided by a Window object. The GraphicsHandle is passed to a Module, which then
 * uses it for drawing. A properly created GraphicsHandle is guaranteed to provide a correct graphical offset.
 */
public class GraphicsHandle {
    public final int offsetx;
    public final int offsety;
    public final int width;
    public final int height;
    
    private final Graphics graphics;
    
    public GraphicsHandle(int x, int y, int width, int height, Graphics g) {
        offsetx = x;
        offsety = y;
        this.width = width;
        this.height = height;
        graphics = g.create();
        graphics.setClip(x, y, width, height);
    }
    
    public void setColor(Color c) {
        graphics.setColor(c);
    }
    
    public void drawRect(int x, int y, int width, int height) {
        graphics.drawRect(offsetx + x, offsety + y, width, height);
    }
    
    public void fillRect(int x, int y, int width, int height) {
        graphics.fillRect(offsetx + x, offsety + y, width, height);
    }
    
    public void drawImage(Image image, int x, int y) {
        graphics.drawImage(image, offsetx + x, offsety + y, null);
    }
    
    public void drawImage(Image image, int x, int y, ImageObserver io) {
        graphics.drawImage(image, offsetx + x, offsety + y, io);
    }
    
    public void fill(Color c) {
        Color old = graphics.getColor();
        graphics.setColor(c);
        graphics.fillRect(offsetx, offsety, width, height);
        graphics.setColor(old);
    }
}