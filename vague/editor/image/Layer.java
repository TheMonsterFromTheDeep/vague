package vague.editor.image;

import java.awt.image.BufferedImage;
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
    }
    
    public Layer(int width, int height) {
        this.size = new Vector(width, height);
        position = new Vector(0,0);
    }
    
    public BufferedImage render() {
        return data;
    }
    
    public int x() { return position.x; }
    public int y() { return position.y; }
}