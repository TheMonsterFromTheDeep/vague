package vague.editor.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import vague.image.Canvas;
import module.util.Vector;

/**
 * Represents a single layer of image data. Multiple layers can be created inside a single image project, and
 * they are all drawn on top of each other.
 * @author TheMonsterFromTheDeep
 */
public class Layer {
    private Vector position; //Stores the position of the layer
    private Vector size; //Stores the size of the layer - should never be out of sync with the data image
    
    //private BufferedImage data; //Stores the image data of the Layer
    private Canvas data; //Stores the image data of the Layer
    
    public Layer(Vector size) {
        this.size = new Vector(size);
        position = new Vector(0,0);
        
        //data = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
        data = new Canvas(size.x, size.y);
    }
    
    public Layer(int width, int height) {
        this.size = new Vector(width, height);
        position = new Vector(0,0);
        
        //data = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        data = new Canvas(width, height);
    }
    
    public BufferedImage render() {
        return data.render();
    }
    
    public int x() { return position.x; }
    public int y() { return position.y; }
    
    public void setPixel(int x, int y, Color c) {
        data.set(x, y, c);
    }
    
    public void blendPixel(int x, int y, Color c) {
        data.blend(x, y, c);
    }
    
    public Color getPixel(int x, int y) {
        return data.get(x, y);
    }
    
    public void setRGB(int x, int y, int rgb) {
        data.setRGB(x, y, rgb);
    }
    
    public int getRGB(int x, int y) {
        return data.getRGB(x, y);
    }
}