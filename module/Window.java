package module;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import module.meta.ModuleParent;
import module.paint.GraphicsCallback;
import module.paint.GraphicsHandle;
import module.util.Vector;
import vague.input.Controls;
import vague.util.Cursor;

/**
 * The Window interfaces the Module system with various Java windowing systems to form a coherent unit.
 * @author TheMonsterFromTheDeep
 */
public class Window extends JFrame implements ModuleParent, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private static final int DEFAULT_SIZE = 300;
    
    private JPanel panel;
    
    private Module paintTarget;
    private GraphicsCallback paintCallback;
    
    Module child;
    
    public Window() {
        this("");
    }
    
    public Window(String title) {
        this(title, DEFAULT_SIZE, DEFAULT_SIZE);
    }
    
    public Window(String title, int width, int height) {
        super(title);
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                //TODO: Make module targeting work correctly - fixed?
                if(paintTarget != null) {
                    if(paintCallback == null) {
                        paintTarget.paint(new GraphicsHandle(paintTarget.getAbsoluteX(), paintTarget.getAbsoluteY(), g));
                    }
                    else {
                        paintCallback.paint(new GraphicsHandle(paintTarget.getAbsoluteX(), paintTarget.getAbsoluteY(), g));
                    }
                }
                
                //TODO: Implement partial redraw calls by Modules
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        
        add(panel);
        pack();
        
        setVisible(true);
    }
    
    public final void setChild(Module child) {
        this.child = child;
    }
    
    @Override
    public void drawChild(Module child) {
        paintTarget = child;
        paintCallback = null;
        panel.repaint(child.getAbsoluteX(), child.getAbsoluteY(), child.width(), child.height());
    }
    
    public void requestHandle(Module child) {
        paintTarget = child;
        paintCallback = null;
        panel.repaint(child.getAbsoluteX(), child.getAbsoluteY(), child.width(), child.height());
    }
    
    public void requestHandle(Module child, GraphicsCallback callback) {
        paintTarget = child;
        paintCallback = callback;
        panel.repaint(child.getAbsoluteX(), child.getAbsoluteY(), child.width(), child.height());
    }
    
    public void requestHandle(Module child, GraphicsCallback callback, int x, int y, int width, int height) {
        paintTarget = child;
        paintCallback = callback;
        panel.repaint(child.getAbsoluteX() + x, child.getAbsoluteY() + y, width, height);
    }
    
    @Override
    public int getAbsoluteX() {
        return 0;
    }

    @Override
    public int getAbsoluteY() {
        return 0;
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        child.mouseClick(me);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        child.mouseDown(me);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        child.mouseUp(me);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    @Override
    public void mouseDragged(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        //TODO: Implement mouse moving
        child.mouseMove(Vector.ZERO, Vector.ZERO);
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        child.mouseScroll(mwe);
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        child.keyType(ke);
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        Controls.bank.update(ke.getKeyCode(),true);
        child.keyDown();
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        Controls.bank.update(ke.getKeyCode(),false);
        child.keyUp();
    }
    
    @Override
    public Vector mousePosition() {
        return new Vector(
                MouseInfo.getPointerInfo().getLocation().x - this.getX() - this.getInsets().left,
                MouseInfo.getPointerInfo().getLocation().y - this.getY() - this.getInsets().top
        );
    }

    
    //TODO: CARE ABOUT OR KILL THESE METHODS
    @Override
    public void setCursor(Cursor c) {
    }

    @Override
    public void clearCursor() {
    }

    
}