package vague.editor;

import java.awt.Color;
import java.awt.Graphics;
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
    
    static final Color TILE_COLOR_LIGHT = new Color(0xcfcfdd);
    static final Color TILE_COLOR_DARK = new Color(0x909090);
    
    static final Color GRID_COLOR = new Color(0xff0000);
    
    //The number of pixels tall and wide each tile in a transparent image background is
    static final int TILE_SIZE = 4;
    
    private Vector canvasPosition; //Stores the position of the canvas inside this particular Editor module
    private int canvasZoom; //Stores the zoom level of the Editor
    
    //Stores the Editor's current buffer of the Canvas - each editor can have a different zoom, so each editor may
    //need its own buffer.
    private BufferedImage canvasRender;
    
    //Stores the tiled background drawn behind a transparent Canvas.
    private BufferedImage tiledBackground;
    
    private boolean panning = false;
    
    private boolean gridLines = false;
    
    private Editor() {
        this.bgColor = BG_COLOR;
        
        canvasPosition = new Vector(DEFAULT_CANVAS_POSITION);
        canvasZoom = DEFAULT_CANVAS_ZOOM; //The zoom is calculated based on a power; a power of zero is a scale of 1:1
        
        center(); //Set the canvas position to the center of the Editor
        prepare(); //Initialize the graphical state of this Editor's buffer
    }
    
    public static Editor create() {
        if(Canvas.canvas == null) {
            Canvas.create(24, 24); //TEST SIZE ONLY
        }
        return new Editor();
    }
    
    private void createBackground() {
        int width = canvasRender.getWidth();
        int height = canvasRender.getHeight();
        
        tiledBackground = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = tiledBackground.createGraphics();
        
        for(int x = 0; x < (width / TILE_SIZE) + 1; x++) {
            for(int y = 0; y < (height / TILE_SIZE) + 1; y++) {
                g.setColor((((x % 2) + y) % 2) == 0 ? TILE_COLOR_LIGHT : TILE_COLOR_DARK);
                g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
        
        if(gridLines) {
            int scale = (int)getScale();
            //The grid only really looks good if the scale is greater than or equal to 4
            if(scale >= 4) {
                g.setColor(GRID_COLOR);
                for(int x = scale; x < width; x += scale) {
                    g.drawLine(x, 0, x, height);
                }
                for(int y = scale; y < height; y += scale) {
                    g.drawLine(0, y, width, y);
                }
            }
        }
    }
    
    private double getScale() {
        return Math.pow(2, canvasZoom);
    }
    
    private void zoom() {
        if(canvasRender != null) {
            canvasRender.flush(); //Dispose of current data so as not to leave a bunch of floating pixel data in memory
        }
        double scaleMultiplier = Math.pow(2, canvasZoom);
        AffineTransform scale = new AffineTransform(AffineTransform.getScaleInstance(scaleMultiplier, scaleMultiplier));
        AffineTransformOp scaleop = new AffineTransformOp(scale, null);
        canvasRender = scaleop.filter(Canvas.canvas.render(), null);
    }
    
    private void prepare() {
        zoom();
        createBackground();
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
                prepare();
                
                draw = true;
            }
            if(draw) {
                redraw();
            }
        }
        if(Controls.bank.status(Controls.MODIFIER_SHIFT)) {
            if(Controls.bank.status(Controls.EDITOR_TOGGLE_GRID)) { 
                gridLines = !gridLines;
                prepare(); //Re-create the background buffer
                redraw(); //Reflect updated graphical state
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
            prepare();
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
        graphics.drawImage(tiledBackground, canvasX, canvasY, null);
        graphics.drawImage(canvasRender, canvasX, canvasY, null);
    }
}