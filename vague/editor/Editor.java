package vague.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import vague.editor.image.EditTarget;
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
    
    static final int LOW_RES_RATIO = 24;
    
    //The default canvas position and zoom
    static final Vector DEFAULT_CANVAS_POSITION = new Vector(0, 0);
    static final int DEFAULT_CANVAS_ZOOM = 0;
    
    static final Color BG_COLOR = new Color(0xcbcbdd);
    
    static final Color TILE_COLOR_LIGHT = new Color(0xcfcfdd);
    static final Color TILE_COLOR_DARK = new Color(0x909090);
    
    static final Color OUTLINE_COLOR_LIGHT = new Color(0xaa0000);
    static final Color OUTLINE_COLOR_DARK = new Color(0x440000);
    
    static final Color GRID_COLOR = new Color(0xff0000);
    
    //The number of pixels tall and wide each tile in a transparent image background is
    static final int TILE_SIZE = 4;
    
    private Vector canvasPosition; //Stores the position of the canvas inside this particular Editor module
    private Rectangle canvasBounds; //Stores the bounds of the canvas
    private int canvasZoom; //Stores the zoom level of the Editor
    
    //Stores the Editor's current buffer of the Canvas - each editor can have a different zoom, so each editor may
    //need its own buffer.
    //private BufferedImage canvasRender;
    
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
        //canvasRender = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        
        //////TEST PURPOSES ONLY!!!!!!
        currentTool = new Pencil(filter());
        /////
        
        canvasBounds = new Rectangle(0, 0, 0, 0);
        
        center(); //Set the canvas position to the center of the Editor
        prepare(); //Initialize the graphical state of this Editor's buffer
    }
    
    public static Editor create() {
        if(EditTarget.target == null) {
            EditTarget.create(100,100);//TEST SIZE ONLY
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
    
    //////?TODO!!!!!!!!!!!!!!!!!: Make this work with scales less than 0
    private void createBounds() {
        double scale = getScale();
        int width = (int)Math.round(EditTarget.target.width() * scale);
        int height = (int)Math.round(EditTarget.target.height() * scale);
        
        canvasBounds = new Rectangle(
                canvasPosition.x - (width / 2), 
                canvasPosition.y - (height / 2),
                width,
                height
        );
    }
    
    private void renderLowResCanvas() {
        System.err.println("rendering low res canvas!");
        
        int size = ((EditTarget.target.width() + EditTarget.target.height()) / 2) / LOW_RES_RATIO;
        size = size % 2 == 0 ? size + 1 : size;
        size = size < 1 ? 1 : size;
        
        double scaleMultiplier = Math.pow(2, canvasZoom);
        
        int pixelDist = (int)Math.round(scaleMultiplier);
        int pixelWidth = pixelDist * size;
        
        for(int x = 0; x < EditTarget.target.width(); x += size) {
            for(int y = 0; y < EditTarget.target.height(); y += size) {
                graphics.setColor((((x % 2) + y) % 2) == 0 ? TILE_COLOR_LIGHT : TILE_COLOR_DARK);
                graphics.fillRect(canvasBounds.left() + x * pixelDist, canvasBounds.top() + y * pixelDist, pixelWidth, pixelWidth);
                
                graphics.setColor(EditTarget.target.getColor(x,y));
                graphics.fillRect(canvasBounds.left() + x * pixelDist, canvasBounds.top() + y * pixelDist, pixelWidth, pixelWidth);
            }
        }
    }
    
    private void renderCanvas() {
        System.err.println("rendering canvas!");
        //if(canvasRender != null) {
            //canvasRender.flush(); //Dispose of current data so as not to leave a bunch of floating pixel data in memory
        //}
        double scaleMultiplier = Math.pow(2, canvasZoom);
        //AffineTransform scale = new AffineTransform(AffineTransform.getScaleInstance(scaleMultiplier, scaleMultiplier));
        //AffineTransformOp scaleop = new AffineTransformOp(scale, null);
        //canvasRender = scaleop.filter(Canvas.canvas.render(), null);
        
        //createBounds();
        
        int pixelWidth = (int)Math.round(scaleMultiplier);
        
        for(int x = 0; x < EditTarget.target.width(); x++) {
            for(int y = 0; y < EditTarget.target.height(); y++) {
                graphics.setColor((((x % 2) + y) % 2) == 0 ? TILE_COLOR_LIGHT : TILE_COLOR_DARK);
                graphics.fillRect(canvasBounds.left() + x * pixelWidth, canvasBounds.top() + y * pixelWidth, pixelWidth, pixelWidth);
                
                graphics.setColor(EditTarget.target.getColor(x,y));
                graphics.fillRect(canvasBounds.left() + x * pixelWidth, canvasBounds.top() + y * pixelWidth, pixelWidth, pixelWidth);
            }
        }
        for(int x = 0; x < EditTarget.target.width(); x++) {
            graphics.setColor(x % 2 == 0 ? OUTLINE_COLOR_LIGHT : OUTLINE_COLOR_DARK);
            graphics.drawLine(canvasBounds.left() + x * pixelWidth, canvasBounds.top() - 1, canvasBounds.left() + (x + 1) * pixelWidth,canvasBounds.top() - 1);
            graphics.drawLine(canvasBounds.left() + x * pixelWidth, canvasBounds.bottom(), canvasBounds.left() + (x + 1) * pixelWidth,canvasBounds.bottom());
        }
        for(int y = 0; y < EditTarget.target.height(); y++) {
            graphics.setColor(y % 2 == 0 ? OUTLINE_COLOR_LIGHT : OUTLINE_COLOR_DARK);
            graphics.drawLine(canvasBounds.left() - 1, canvasBounds.top() + y * pixelWidth, canvasBounds.left() - 1,canvasBounds.top() + (y + 1) * pixelWidth);
            graphics.drawLine(canvasBounds.right(), canvasBounds.top() + y * pixelWidth, canvasBounds.right(),canvasBounds.top() + (y + 1) * pixelWidth);
        }
    }
    
    private void drawLowRes() {
        this.fillBackground();
        renderLowResCanvas();
        
        drawParent();
    }
    
    private void drawNormal() {
        this.fillBackground();
        renderCanvas();
        
        drawParent();
    }
    
    private void prepare() {
        createBounds();
        renderCanvas();
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
            drawLowRes();
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
    public void keyUp() {
        drawNormal();
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            updateTool = true;
            if(currentTool != null) {
                Vector mPos = mousePosition();
                
                if(canvasBounds.encloses(mPos)) {
                    double scale = getScale();
                    currentTool.onDown(new Vector((int)((mPos.x - canvasBounds.left()) / scale), (int)((mPos.y - canvasBounds.top()) / scale)));
                }
            }
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
            drawNormal();
        }
    }
    
    @Override
    public void mouseScroll(MouseWheelEvent e) {
        if(Controls.bank.status(Controls.EDITOR_MODIFIER_ZOOM)) {
            canvasZoom -= e.getWheelRotation();
            if(canvasZoom > ZOOM_MAX) { canvasZoom = ZOOM_MAX; }
            if(canvasZoom < ZOOM_MIN) { canvasZoom = ZOOM_MIN; }
            prepare();
            drawLowRes();
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
        drawNormal();
    }
    
    public EditFilter filter() {
        return new EditFilter(this, EditTarget.target);
    }
}