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
 * An Editor is simply an interface for drawing to the Context.
 * @author TheMonsterFromTheDeep
 */
public class Editor extends Module {
    static final Color BG_COLOR = new Color(0xcbcbdd);
      
    private int centerx; //Stores the coordinates of the center of the Editor
    private int centery;
    
    boolean LMBDown = false;
    boolean MMBDown = false;
    
    Vector panPos;
    
    ContextView view;
    
    private Editor(Window w) {
        super(w);
        
        centerx = 0;
        centery = 0;
        
        view = new ContextView();
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
        if(LMBDown) {
            if(view.checkMouse(pos)) {
                repaint();
            }
        }
        else if(MMBDown) {
            view.panX(dif.x);
            view.panY(dif.y);
            repaint();
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        switch(e.getButton()) {
            case MouseEvent.BUTTON1:
                LMBDown = true;
                break;
            case MouseEvent.BUTTON2:
                MMBDown = true;
                panPos = mousePosition();
                break;
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        switch(e.getButton()) {
            case MouseEvent.BUTTON1:
                LMBDown = false;
                break;
            case MouseEvent.BUTTON2:
                MMBDown = false;
                break;
        }
    }
    
    @Override
    public void mouseScroll(MouseWheelEvent e) {
        if(Controls.bank.status(Controls.EDITOR_MODIFIER_PAN_HORZ)) {
            view.panXScalar(-3 * (int)e.getPreciseWheelRotation());
        } else if(Controls.bank.status(Controls.EDITOR_MODIFIER_PAN_VERT)) {
            view.panYScalar(3 * (int)e.getPreciseWheelRotation());
        } else {
            view.zoom(-(int)e.getPreciseWheelRotation());
        }
        repaint();
    }
    
    @Override
    public void paint(GraphicsHandle handle) {
        handle.fill(BG_COLOR);
        //context.drawBorder(handle, panx + centerx + context.getOffsetX(), pany + centery + context.getOffsetY());
        view.draw(handle);
    }
}