package vague.editor;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;
import module.paint.GraphicsHandle;

/**
 * A ScalarGraphics is a wrapper over a GraphicsHandle that includes an extra offset and a scale.
 * 
 * It is passed to Shapes for their rendering so that they do not have to properly implement scaling
 * and offset.
 * @author TheMonsterFromTheDeep
 */
public class ScalarGraphics {
    GraphicsHandle handle;
    
    public final int offx;
    public final int offy;
    public final float scale;
    
    public ScalarGraphics(GraphicsHandle handle, int offx, int offy, float scale) {
        this.handle = handle;
        this.offx = offx;
        this.offy = offy;
        this.scale = scale;
    }
    
    public void setColor(Color c) {
        handle.setColor(c);
    }
    
    public Color getColor() {
        return handle.getColor();
    }
    
    public void drawRect(int x, int y, int width, int height) {
        handle.drawRect((int)((offx + x) * scale), (int)((offy + y) * scale), (int)(width * scale), (int)(height * scale));
    }
    
    public void fillRect(int x, int y, int width, int height) {
        handle.fillRect((int)((offx + x) * scale), (int)((offy + y) * scale), (int)(width * scale), (int)(height * scale));
    }
    
    public void drawImage(Image image, int x, int y) {
        handle.drawImage(image, (int)((offx + x) * scale), (int)((offy + y) * scale), null);
    }
    
    public void drawImage(Image image, int x, int y, ImageObserver io) {
        handle.drawImage(image, (int)((offx + x) * scale), (int)((offy + y) * scale), io);
    }
    
    public void drawLine(float sx, float sy, float ex, float ey) {
        handle.drawLine((int)((offx + sx) * scale), (int)((offy + sy) * scale), (int)((offx + ex) * scale), (int)((offy + ey) * scale));
    }
    
    public void fill(Color c) {
        handle.fill(c);
    }
}