package vague.editor;

import java.awt.Color;
import java.awt.Graphics;
import module.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import module.util.Rectangle;
import vague.input.Controls;
import module.Module;
import module.paint.GraphicsHandle;
import module.util.Vector;

/**
 * Represents an image editor that can manipulate and draw to the Canvas. Multiple instances can be created, with different
 * pan, zoom, and active tools.
 * @author TheMonsterFromTheDeep
 */
public class Editor extends Module {
    static final Color BG_COLOR = new Color(0xcbcbdd);
    
    Context context;
    
    private int panx; //Stores the distance, from the center, that the Context is panned.
    private int pany;
    
    private int centerx; //Stores the coordinates of the center of the Editor
    private int centery;
    
    boolean mouse = false;
    
    private Editor(Window w) {
        super(w);
        context = Context.getContext();
        
        panx = 0;
        pany = 0;
        
        centerx = 0;
        centery = 0;
    }
    
    @Override
    public void onResize(Vector size) {
        centerx = size.x / 2;
        centery = size.y / 2;
    }
            
    public static Editor create(Window w) {
        return new Editor(w);
    }
    
    @Override
    public void mouseMove(Vector pos, Vector dif) {
        if(mouse) { 
            Vector newPosition = context.expand(new Vector(panx + centerx + context.getOffsetX(), pany + centery + context.getOffsetY()), pos);
            panx = newPosition.x - (centerx + context.getOffsetX());
            pany = newPosition.y - (centery + context.getOffsetY());
            repaint();
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        mouse = true;
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        mouse = false;
    }
    
    @Override
    public void paint(GraphicsHandle handle) {
        handle.fill(BG_COLOR);
        context.drawBorder(handle, panx + centerx + context.getOffsetX(), pany + centery + context.getOffsetY());
    }
}