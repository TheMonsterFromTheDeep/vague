package vague.editor.image;

import java.awt.Color;
import vague.image.Canvas;
import module.util.Vector;

/**
 * Represents an image editing object of a specific size. Holds a set of Layers that are drawn.
 * @author TheMonsterFromTheDeep
 */
public class EditTarget {
    private Vector size; //Stores the size of the canvas
    
    private Layer[] layers; //Stores the layers of the image
    
    private int layer;
    
    //private BufferedImage lastRender; //Stores the last render of the image
    //Stores the current graphical state of the EditTarget
    private Canvas data;
    
    //private Graphics graphics;
    
    public static EditTarget target;
    
    private void createDrawSystem() {
        int width = size.x > 0 ? size.x : 1;
        int height = size.y > 0 ? size.y : 1;
        
        //lastRender = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        data = new Canvas(width, height);
        //graphics = lastRender.createGraphics();
    }
    
    private EditTarget(Vector size) {
        this.size = new Vector(size);
        layers = new Layer[1];
        layers[0] = new Layer(size);
        
        layer = 0;
        
        createDrawSystem();
    }
    
    private EditTarget(int width, int height) {
        this.size = new Vector(width, height);
        //layers = new Layer[2];
        //layers[0] = new Layer(width, height);
        //Color fill = new Color(0x22ffffff, true);
        //for(int x = 0; x < 24; x++) {
         //   for(int y = 0; y < 24; y++) {
        //        layers[0].setPixel(x, y, fill);
        //    }
        //}
        //layers[1] = new Layer(width, height);
        //layer = 0;
        layers = new Layer[1];
        layers[0] = new Layer(size);
         
        layer = 0;
        
        createDrawSystem();
    }
    
    public static EditTarget create() {
        return new EditTarget(0, 0);
    }
    
    public static EditTarget create(Vector size) {
        if(target == null) {
            target = new EditTarget(size);
        }
        return target;
    }
    
    public static EditTarget create(int width, int height) {
        if(target == null) {
            target = new EditTarget(width, height);
        }
        return target;
    }
    
    public int width() { return size.x; }
    public int height() { return size.y; }
    
    public Layer layer() {
        return layers[layer];
    }
    
    public void drawPixel(int x, int y) {
        //TODO: Make this much more efficient / support transparency
        Color c = layers[0].getPixel(x, y);
        //for(int i = layers.length - 1; i >= 0; i--) {
        //    lastRender.setRGB(x, y, layers[i].getRGB(x, y));
        //}
        for(Layer l : layers) {
            c = Canvas.mix(c, l.getPixel(x, y));
        }
        
        data.set(x, y, c);
    }
    
    public Color getColor(int x, int y) {
        return data.get(x, y);
    }
    
    //Returns whether the specified pixel is visible in the current layer
    public boolean visible(int x, int y) {
        return true; //TODO: Actually check for visibility
    }
}