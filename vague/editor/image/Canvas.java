package vague.editor.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import vague.util.Vector;

/**
 * Represents an image editing object of a specific size. Holds a set of Layers that are drawn.
 * @author TheMonsterFromTheDeep
 */
public class Canvas {
    private Vector size; //Stores the size of the canvas
    
    private Layer[] layers; //Stores the layers of the image
    
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
        
        createDrawSystem();
    }
    
    private Canvas(int width, int height) {
        this.size = new Vector(width, height);
        layers = new Layer[1];
        layers[0] = new Layer(width, height);
         
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
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, size.x, size.y);        
        for(Layer l : layers) {
            graphics.drawImage(l.render(), l.x(), l.y(), null);
        }
    }
    
    public int width() { return size.x; }
    public int height() { return size.y; }

    public BufferedImage render() {
        return lastRender;
    }
}