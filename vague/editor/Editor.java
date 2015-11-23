package vague.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import vague.editor.image.Canvas;
import vague.editor.tool.EditFilter;
import vague.editor.tool.Pencil;
import vague.editor.tool.Tool;
import vague.geom.Rectangle;
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
    private Rectangle canvasBounds; //Stores the bounds of the canvas
    private int canvasZoom; //Stores the zoom level of the Editor
    
    //Stores the Editor's current buffer of the Canvas - each editor can have a different zoom, so each editor may
    //need its own buffer.
    private BufferedImage canvasRender;
    
    //Stores the tiled background drawn behind a transparent Canvas.
    private BufferedImage tiledBackground;
    
    private boolean panning = false;
    
    private boolean drawGridLines = false;
    
    private boolean updateTool = false;
    
    //If the canvas needs to be re-drawn, this will be true
    //it will be set to false as soon as the canvas is re-drawn
    private boolean canvasDrawQueued = false;
    
    private Tool currentTool = null;
    
    private Editor() {
        this.bgColor = BG_COLOR;
        
        canvasPosition = new Vector(DEFAULT_CANVAS_POSITION);
        canvasZoom = DEFAULT_CANVAS_ZOOM; //The zoom is calculated based on a power; a power of zero is a scale of 1:1
        
        //Create a dummy immage
        canvasRender = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        
        //////TEST PURPOSES ONLY!!!!!!
        currentTool = new Pencil(filter());
        /////
        
        center(); //Set the canvas position to the center of the Editor
        prepare(); //Initialize the graphical state of this Editor's buffer
    }
    
    public static Editor create() {
        if(Canvas.canvas == null) {
            Canvas.create(24, 24); //TEST SIZE ONLY
        }
        return new Editor();
    }
           
    private double getScale() {
        return Math.pow(2, canvasZoom);
    }
       
    private void shift(int x, int y) {
        canvasPosition.add(x, y);
        canvasBounds.translate(x, y);
    }
    
    private void shift(Vector v) {
        canvasPosition.add(v);
        canvasBounds.translate(v);
    }
    
    
    private void createBounds() {
        canvasBounds = new Rectangle(
                canvasPosition.x - (canvasRender.getWidth() / 2), 
                canvasPosition.y - (canvasRender.getHeight() / 2),
                canvasRender.getWidth(),
                canvasRender.getHeight()
        );
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
        
        if(drawGridLines) {
            int scale = (int)getScale();
            //The grid only really looks good if the scale is greater than or equal to 4
            if(scale >= 4) {
                g.setColor(GRID_COLOR);
                int i = 0;
                while(i < width) {
                    g.drawLine(i, 0, i, height);
                    i += scale - 1;
                    g.drawLine(i, 0, i, height);
                    i++;
                }
                i = 0;
                while(i < height) {
                    g.drawLine(0, i, width, i);
                    i += scale - 1;
                    g.drawLine(0, i, width, i);
                    i++;
                }
            }
        }
    }
    
    private void renderCanvas() {
        if(canvasRender != null) {
            canvasRender.flush(); //Dispose of current data so as not to leave a bunch of floating pixel data in memory
        }
        double scaleMultiplier = Math.pow(2, canvasZoom);
        AffineTransform scale = new AffineTransform(AffineTransform.getScaleInstance(scaleMultiplier, scaleMultiplier));
        AffineTransformOp scaleop = new AffineTransformOp(scale, null);
        canvasRender = scaleop.filter(Canvas.canvas.render(), null);
        
        createBounds();
        
        //The draw has been done; it no longer needs to be queued
        canvasDrawQueued = false;
    }
    
    private void prepare() {
        renderCanvas();
        createBackground();
    }
    
    private void center() {
        canvasPosition = new Vector(width() / 2, height() / 2);
        createBounds();
    }
    
    @Override
    public void mouseMove(Vector mousePos, Vector mouseDif) {
        if(updateTool) {
            if(currentTool != null) {
                if(canvasBounds.encloses(mousePos)) {
                    double scale = getScale();
                    currentTool.modify(new Vector((int)((mousePos.x - canvasBounds.left()) / scale), (int)((mousePos.y - canvasBounds.top()) / scale)));
                }
            }
        }
        if(panning) {
            shift(mouseDif);
            redraw();
        }
    }
    
    @Override
    public void keyDown() {
        boolean draw = false;
        if(Controls.bank.status(Controls.EDITOR_RESET_ZOOM_AND_PAN)) {
            canvasPosition = new Vector(DEFAULT_CANVAS_POSITION);
            center();
            
            canvasZoom = DEFAULT_CANVAS_ZOOM;
            prepare();
            
            draw = true;
        }
        else {
            if(Controls.bank.status(Controls.EDITOR_RESET_PAN)) {                
                //Reset the position
                canvasPosition = new Vector(DEFAULT_CANVAS_POSITION);
                center(); //Center the canvas in the center of the editor

                draw = true;
            }
            if(Controls.bank.status(Controls.EDITOR_RESET_ZOOM)) {
                canvasZoom = DEFAULT_CANVAS_ZOOM;
                //Zoom to reset the buffer
                prepare();

                draw = true;
            }
        }
        if(Controls.bank.status(Controls.EDITOR_TOGGLE_GRID)) { 
            drawGridLines = !drawGridLines;
            prepare(); //Re-create the background buffer
            redraw(); //Reflect updated graphical state
        }
        if(draw) { redraw(); }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            updateTool = true;
            keepFocus();
        }
        if(e.getButton() == MouseEvent.BUTTON2) {
            panning = true;
            keepFocus();
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            updateTool = false;
            releaseFocus();
        }
        if(e.getButton() == MouseEvent.BUTTON2) {
            panning = false;
            releaseFocus();
        }
    }
    
    @Override
    public void mouseScroll(MouseWheelEvent e) {
        if(Controls.bank.status(Controls.EDITOR_MODIFIER_ZOOM)) {
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
        
        createBounds();
    }
    
    @Override
    public void onFocus() {
        renderCanvas(); //Update this buffer in case it was changed elsewhere
    }
    
    //Should bee set by the EditFilter when it wants the canvas to be re-drawn
    public void queueCanvasDraw() {
        canvasDrawQueued = true;
    }
    
    public void drawPixel(int x, int y, Color c) {
        graphics.setColor(c);
        int scale = (int)getScale();
        graphics.fillRect(canvasBounds.left() + x * scale, canvasBounds.top() + y * scale, scale, scale);
        
        drawParent();
    }
    
    @Override
    public void draw() {
        this.fillBackground();
        graphics.setColor(Color.BLACK);
        graphics.drawRect(canvasBounds.left() - 1, canvasBounds.top() - 1, canvasRender.getWidth() + 1, canvasRender.getHeight() + 1);
        graphics.drawImage(tiledBackground, canvasBounds.left(), canvasBounds.top(), null);
        
        if(canvasDrawQueued) {
            renderCanvas();
        }
        
        graphics.drawImage(canvasRender, canvasBounds.left(), canvasBounds.top(), null);
    }
    
    public EditFilter filter() {
        return new EditFilter(this, Canvas.canvas);
    }
}