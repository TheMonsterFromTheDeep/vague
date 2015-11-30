package vague.editor.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import vague.util.Colors;
import vague.util.Vector;

/**
 * Represents a single layer of image data. Multiple layers can be created inside a single image project, and
 * they are all drawn on top of each other.
 * @author TheMonsterFromTheDeep
 */
public class Layer {
    private Vector position; //Stores the position of the layer
    private Vector size; //Stores the size of the layer - should never be out of sync with the data image
    
    private BufferedImage data; //Stores the image data of the Layer
    
    public Layer(Vector size) {
        this.size = new Vector(size);
        position = new Vector(0,0);
        
        data = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
    }
    
    public Layer(int width, int height) {
        this.size = new Vector(width, height);
        position = new Vector(0,0);
        
        data = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    public BufferedImage render() {
        return data;
    }
    
    public int x() { return position.x; }
    public int y() { return position.y; }
    
    public void setPixel(int x, int y, Color c) {
        data.setRGB(x, y, c.getRGB());
    }
    
    public void blendPixel(int x, int y, Color c) {
        data.setRGB(x, y, Colors.mix(c, new Color(data.getRGB(x, y), true)).getRGB());
    }
    
    public Color getPixel(int x, int y) {
        return new Color(data.getRGB(x, y), true);
    }
    
    public void setRGB(int x, int y, int rgb) {
        data.setRGB(x, y, rgb);
    }
    
    public int getRGB(int x, int y) {
        return data.getRGB(x, y);
    }
}