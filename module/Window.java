package module;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import module.meta.ModuleParent;
import module.paint.GraphicsHandle;
import module.util.Vector;
import vague.util.Cursor;

/**
 * The Window interfaces the Module system with various Java windowing systems to form a coherent unit.
 * @author TheMonsterFromTheDeep
 */
public class Window extends JFrame implements ModuleParent/*, MouseListener, MouseMotionListener, KeyListener*/ {

    private static final int DEFAULT_SIZE = 300;
    
    private JPanel panel;
    
    Module paintTarget;
    
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
                //TODO: Make module targeting work correctly
                paintTarget.paint(new GraphicsHandle(paintTarget.getAbsoluteX(), paintTarget.getAbsoluteY(), g));
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        
        add(panel);
        pack();
        
        setVisible(true);
    }
    
    @Override
    public void drawChild(Module child) {
        panel.repaint(child.x(), child.y(), child.width(), child.height());
    }
    
    public void requestHandle(Module child) {
    }
    
    @Override
    public int getAbsoluteX() {
        return 0;
    }

    @Override
    public int getAbsoluteY() {
        return 0;
    }
    
    /*@Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
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
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }*/

    
    
    @Override
    public Vector mousePosition() {
        //TODO: implement mouse position
        return null;
    }

    
    //TODO: CARE ABOUT OR KILL THESE METHODS
    @Override
    public void setCursor(Cursor c) {
    }

    @Override
    public void clearCursor() {
    }

    

    
}