package vague.editor.tool;

import java.awt.Color;
import vague.editor.Editor;
import vague.editor.image.Canvas;

/**
 * Represents a system through which tools are interfaced with the editor and the image. This allows tools
 * to modify things like what the user has selected and the data of the image being edited, and also
 * updates the Editor module to a useful graphical state.
 * @author TheMonsterFromTheDeep
 */
public class EditFilter {
    //Handles to the Editor module and the Canvas
    private Editor editor;
    private Canvas canvas;
    
    public EditFilter(Editor editor, Canvas canvas) {
        this.editor = editor;
        this.canvas = canvas;
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
    
    public void setRGB(int x, int y, int rgb) {
        canvas.layer().setRGB(x, y, rgb);
        canvas.drawPixel(x, y);
        if(canvas.visible(x, y)) {
            editor.drawPixel(x, y, new Color(rgb));
            editor.queueCanvasDraw();
        }
    }
}
