package vague.editor.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import vague.util.Colors;
import vague.util.Vector;

/**
 * Represents an image editing object of a specific size. Holds a set of Layers that are drawn.
 * @author TheMonsterFromTheDeep
 */
public class Canvas {
    private Vector size; //Stores the size of the canvas
    
    private Layer[] layers; //Stores the layers of the image
    
    private int layer;
    
    private BufferedImage lastRender; //Stores the last render of the image
    private Graphics graphics;
    
    public static Canvas canvas;
    
    private void createDrawSystem() {
        int width = size.x > 0 ? size.x : 1;
        int height = size.y > 0 ? size.y : 1;
        
        lastRender = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = lastRender.createGraphics();
    }
    
    private Canvas(Vector size) {
        this.size = new Vector(size);
        layers = new Layer[1];
        layers[0] = new Layer(size);
        
        layer = 0;
        
        createDrawSystem();
    }
    
    private Canvas(int width, int height) {
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
    
    public static Canvas create() {
        return new Canvas(0, 0);
    }
    
    public static Canvas create(Vector size) {
        if(canvas == null) {
            canvas = new Canvas(size);
            canvas.draw();
        }
        return canvas;
    }
    
    public static Canvas create(int width, int height) {
        if(canvas == null) {
            canvas = new Canvas(width, height);
            canvas.draw();
        }
        return canvas;
    }
    
    public void draw() {      
        for(Layer l : layers) {
            graphics.drawImage(l.render(), l.x(), l.y(), null);
        }
    }
    
    public int width() { return size.x; }
    public int height() { return size.y; }

    public BufferedImage render() {
        return lastRender;
    }
    
    public Layer layer() {
        return layers[layer];
    }
    
    public void drawPixel(int x, int y) {
        //TODO: Make this much more efficient / support transparency
        Color c = new Color(layers[0].getRGB(x, y), true);
        //for(int i = layers.length - 1; i >= 0; i--) {
        //    lastRender.setRGB(x, y, layers[i].getRGB(x, y));
        //}
        for(Layer l : layers) {
            c = Colors.mix(c, new Color(l.getRGB(x, y), true));
        }
        
        lastRender.setRGB(x, y, c.getRGB());
    }
    
    public Color getColor(int x, int y) {
        ///TODO: DO THIS WITHOUT USING THE RENDERERERER
        return new Color(lastRender.getRGB(x, y),true);
    }
    
    //Returns whether the specified pixel is visible in the current layer
    public boolean visible(int x, int y) {
        return true; //TODO: Actually check for visibility
    }
}