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
    private Vector position; //Stores where the Canvas is drawn on-screen (has no effect on image data)
    //position represents the center of the Canvas
    private Vector size; //Stores the size of the canvas
    
    private Layer[] layers; //Stores the layers of the image
    
    private BufferedImage lastRender; //Stores the last render of the image
    private Graphics graphics;
    
    private void createDrawSystem() {
        int width = size.x > 0 ? size.x : 1;
        int height = size.y > 0 ? size.y : 1;
        
        //The width and height are +2 to make room for the border
        lastRender = new BufferedImage(width + 2, height + 2, BufferedImage.TYPE_INT_ARGB);
        graphics = lastRender.createGraphics();
    }
    
    public Canvas(Vector size) {
        this.size = new Vector(size);
        layers = new Layer[1];
        layers[0] = new Layer(size);
        
        createDrawSystem();
        
        position = new Vector(0, 0);
    }
    
    public Canvas(int width, int height) {
        this.size = new Vector(width, height);
        layers = new Layer[1];
        layers[0] = new Layer(width, height);
        
        createDrawSystem();
        
        position = new Vector(0, 0);
    }
    
    public void draw() {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, size.x - 1, size.y - 1);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(1, 1, size.x - 2, size.y - 2);        
        for(Layer l : layers) {
            graphics.drawImage(l.render(), l.x(), l.y(), null);
        }
    }
    
    public int x() { return position.x; }
    public int y() { return position.y; }
    
    public int width() { return size.x; }
    public int height() { return size.y; }
    
    public void move(Vector posDif) {
        position.add(posDif);
    }
    
    public void locate(Vector location) {
        position = new Vector(location);
    }
    
    public BufferedImage render() {
        return lastRender;
    }
}