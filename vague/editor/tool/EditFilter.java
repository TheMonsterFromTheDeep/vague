package vague.editor.tool;

import java.awt.Color;
import vague.editor.Editor;
import vague.editor.image.EditTarget;
import vague.util.Vector;

/**
 * Represents a system through which tools are interfaced with the editor and the image. This allows tools
 * to modify things like what the user has selected and the data of the image being edited, and also
 * updates the Editor module to a useful graphical state.
 * @author TheMonsterFromTheDeep
 */
public class EditFilter {
    //Handles to the Editor module and the Canvas
    private Editor editor;
    private EditTarget canvas;
    
    //Can be used to do things like draw lines and stuff
    //changeable by tools
    public Vector cursor;
    
    //This is the foreground color as determined by external sources
    public Color foreground;
    //This is the background color
    public Color background;
    
    public EditFilter(Editor editor, EditTarget canvas) {
        this.editor = editor;
        this.canvas = canvas;
        
        cursor = new Vector(0, 0);
        
        foreground = new Color(0);
        background = new Color(0x44ffffff,true);
    }
    
    //The most basic edit filter - sets a pixel on the canvas.
    public void setPixel(int x, int y, Color c) {
        canvas.layer().setPixel(x, y, c);
        canvas.drawPixel(x, y);
        if(canvas.visible(x, y)) {
            editor.drawPixel(x, y, c);
            editor.queueCanvasDraw();
        }
    }
    
    //Blends the colors at the specified pixel with the specified color.
    public void blendPixel(int x, int y, Color c) {
        canvas.layer().blendPixel(x, y, c);
        canvas.drawPixel(x, y);
        if(canvas.visible(x, y)) {
            editor.drawPixel(x, y, canvas.layer().getPixel(x, y));
            editor.queueCanvasDraw();
        }
    }
    
    public void drawLine(int sx, int sy, int ex, int ey, Color c) {        
        int iterations = (int)Math.ceil(Math.sqrt((ex - sx) * (ex - sx) + (ey - sy) * (ey - sy)));
        
        double changex = (double)(ex - sx)  / iterations;
        double changey = (double)(ey - sy) / iterations;
        
        double x = sx;
        double y = sy;
        for(int i = 0; i < iterations; i++) {
            setPixel((int)x, (int)y, c);
            x += changex;
            y += changey;
        }           
    }
    
    public void blendLine(int sx, int sy, int ex, int ey, Color c) {
        int iterations = (int)Math.ceil(Math.sqrt((ex - sx) * (ex - sx) + (ey - sy) * (ey - sy)));
        
        double changex = (double)(ex - sx)  / iterations;
        double changey = (double)(ey - sy) / iterations;
        
        double x = sx;
        double y = sy;
        for(int i = 0; i < iterations; i++) {
            blendPixel((int)x, (int)y, c);
            x += changex;
            y += changey;
        }            
    }
    
    public void setRGB(int x, int y, int rgb) {
        canvas.layer().setRGB(x, y, rgb);
        canvas.drawPixel(x, y);
        if(canvas.visible(x, y)) {
            editor.drawPixel(x, y, new Color(rgb));
            editor.queueCanvasDraw();
        }
    }
}
