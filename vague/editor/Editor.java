package vague.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import vague.editor.image.Canvas;
import vague.module.Module;
import vague.util.Vector;

/**
 * @author TheMonsterFromTheDeep
 */
public class Editor extends Module {
    static final Color BG_COLOR = new Color(0xcbcbdd);
    
    private Canvas canvas; //The canvas that everything is drawn to - stores the data of the image being edited
    
    public static Editor editor; //A global reference to the single editor Module that can exist
    
    private boolean panning = false;
    private Vector panOffset;
    
    private Vector getEqualCanvasPosition(Vector position) {
        return new Vector(position.x - (width() / 2), position.y - (height() / 2));
    }
    
    private Editor() {
        this.bgColor = BG_COLOR;
        
        canvas = new Canvas(80, 80);
        canvas.draw();
    }
    
    public static Editor create() {
        if(editor == null) {
            editor = new Editor();
        }
        return editor;
    }
    
    @Override
    public void mouseMove(Vector mousePos, Vector mouseDif) {
        if(panning) {
            //TODO: make this work
            redraw();
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
            panOffset = getEqualCanvasPosition(mousePosition());
            panning = true;
            keepFocus();
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
            panning = false;
            releaseFocus();
        }
    }
    
    @Override
    public void draw() {
        this.fillBackground();
        graphics.drawImage(canvas.render(), 
                ((width() - canvas.width()) / 2) + canvas.x(), 
                ((height() - canvas.height()) / 2) + canvas.y(), 
        null);
    }
}