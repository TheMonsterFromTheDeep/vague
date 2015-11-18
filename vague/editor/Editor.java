package vague.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import vague.editor.image.Canvas;
import vague.module.Module;
import vague.util.Vector;

/**
 * Represents an image editor that can manipulate and draw to the Canvas. Multiple instances can be created, with different
 * pan, zoom, and active tools.
 * @author TheMonsterFromTheDeep
 */
public class Editor extends Module {
    static final Color BG_COLOR = new Color(0xcbcbdd);
    
    private Vector canvasPosition; //Stores the position of the canvas inside this particular Editor module
    
    private boolean panning = false;
    private Vector panOffset;
    
    private Editor() {
        this.bgColor = BG_COLOR;
        
        canvasPosition = new Vector(0, 0);
    }
    
    public static Editor create() {
        if(Canvas.canvas == null) {
            Canvas.create(80, 80); //TEST SIZE ONLY
        }
        return new Editor();
    }
    
    @Override
    public void mouseMove(Vector mousePos, Vector mouseDif) {
        if(panning) {
            canvasPosition.add(mouseDif);
            redraw();
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
            panOffset = mousePosition();
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
    public void onResize(Vector newSize) {
        Vector oldOffset = new Vector((width() / 2) - canvasPosition.x, (height() / 2) - canvasPosition.y);
        canvasPosition = new Vector((newSize.x / 2) - oldOffset.x, (newSize.y / 2) - oldOffset.y);
    }
    
    @Override
    public void draw() {
        this.fillBackground();
        graphics.drawImage(Canvas.canvas.render(), canvasPosition.x - (Canvas.canvas.width() / 2), canvasPosition.y - (Canvas.canvas.height() / 2), null);
    }
}