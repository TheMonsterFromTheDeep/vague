package vague.editor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import vague.editor.image.Canvas;
import vague.input.Controls;
import vague.module.Module;
import vague.util.Vector;

/**
 * Represents an image editor that can manipulate and draw to the Canvas. Multiple instances can be created, with different
 * pan, zoom, and active tools.
 * @author TheMonsterFromTheDeep
 */
public class Editor extends Module {
    public static final int ZOOM_MAX = 7; //Stores the maximum and minimum values for canvasZoom
    public static final int ZOOM_MIN = -3;
    
    //The default canvas position and zoom
    static final Vector DEFAULT_CANVAS_POSITION = new Vector(0, 0);
    static final int DEFAULT_CANVAS_ZOOM = 0;
    
    static final Color BG_COLOR = new Color(0xcbcbdd);
    
    private Vector canvasPosition; //Stores the position of the canvas inside this particular Editor module
    private int canvasZoom; //Stores the zoom level of the Editor
    
    //Stores the Editor's current buffer of the Canvas - each editor can have a different zoom, so each editor may
    //need its own buffer.
    private BufferedImage canvasRender;
    
    private boolean panning = false;
    
    private Editor() {
        this.bgColor = BG_COLOR;
        
        canvasPosition = new Vector(DEFAULT_CANVAS_POSITION);
        canvasZoom = DEFAULT_CANVAS_ZOOM; //The zoom is calculated based on a power; a power of zero is a scale of 1:1
        
        center(); //Set the canvas position to the center of the Editor
        zoom(); //Initialize the graphical state of this Editor's buffer
    }
    
    public static Editor create() {
        if(Canvas.canvas == null) {
            Canvas.create(24, 24); //TEST SIZE ONLY
        }
        return new Editor();
    }
    
    private void zoom() {
        double scaleMultiplier = Math.pow(2, canvasZoom);
        AffineTransform scale = new AffineTransform(AffineTransform.getScaleInstance(scaleMultiplier, scaleMultiplier));
        AffineTransformOp scaleop = new AffineTransformOp(scale, null);
        canvasRender = scaleop.filter(Canvas.canvas.render(), null);
    }
    
    private void center() {
        canvasPosition = new Vector(width() / 2, height() / 2);
    }
    
    @Override
    public void mouseMove(Vector mousePos, Vector mouseDif) {
        if(panning) {
            canvasPosition.add(mouseDif);
            redraw();
        }
    }
    
    @Override
    public void keyDown() {
        if(Controls.bank.status(Controls.EDITOR_RESET_VIEW)) {
            boolean draw = false; //If the zoom / pan was updated, this thing needs to be re-drawn
            if(Controls.bank.status(Controls.MODIFIER_CONTROL)) {                
                //Reset the position
                canvasPosition = new Vector(DEFAULT_CANVAS_POSITION);
                center(); //Center the canvas in the center of the editor
                
                draw = true;
            }
            if(Controls.bank.status(Controls.MODIFIER_SHIFT)) {
                canvasZoom = DEFAULT_CANVAS_ZOOM;
                //Zoom to reset the buffer
                zoom();
                
                draw = true;
            }
            if(draw) {
                redraw();
            }
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
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
    public void mouseScroll(MouseWheelEvent e) {
        if(Controls.bank.status(Controls.MODIFIER_CONTROL)) {
            canvasZoom -= e.getWheelRotation();
            if(canvasZoom > ZOOM_MAX) { canvasZoom = ZOOM_MAX; }
            if(canvasZoom < ZOOM_MIN) { canvasZoom = ZOOM_MIN; }
            zoom();
            redraw();
        }
    }
    
    @Override
    public void onResize(Vector newSize) {
        Vector oldOffset = new Vector((width() / 2) - canvasPosition.x, (height() / 2) - canvasPosition.y);
        canvasPosition = new Vector((newSize.x / 2) - oldOffset.x, (newSize.y / 2) - oldOffset.y);
    }
    
    @Override
    public void onFocus() {
        zoom(); //Update this buffer in case it was changed elsewhere
    }
    
    @Override
    public void draw() {
        this.fillBackground();
        graphics.setColor(Color.BLACK);
        int canvasX = canvasPosition.x - (canvasRender.getWidth() / 2);
        int canvasY = canvasPosition.y - (canvasRender.getHeight() / 2);
        graphics.drawRect(canvasX - 1, canvasY - 1, canvasRender.getWidth() + 1, canvasRender.getHeight() + 1);
        graphics.drawImage(canvasRender, canvasX, canvasY, null);
    }
}