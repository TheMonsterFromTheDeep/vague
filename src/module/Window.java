package module;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
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
public class Window implements ModuleParent, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private static final int DEFAULT_SIZE = 300;
    
    private JFrame frame;
    private JPanel panel;
    
    private BufferedImage buffer;
    private Graphics graphics;
    
    int minWidth = 50;
    int minHeight = 50;
    
    //Used in the mouseMove() call to child modules
    private Vector lastMousePos;
    
    //private Module paintTarget;
    //private GraphicsCallback paintCallback;
    
    Module child;
    
    private static BufferedImage createValidBuffer(int width, int height) {
        width = (width < 1) ? 1 : width;
        height = (height < 1) ? 1 : height;
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    
    private void loadBuffer() {
        if(graphics != null) { graphics.dispose(); }
        if(buffer != null) { buffer.flush(); }
        buffer = createValidBuffer(panel.getWidth(), panel.getHeight());
        graphics = buffer.createGraphics();
    }
    
    public Window() {
        this("");
    }
    
    public Window(String title) {
        this(title, DEFAULT_SIZE, DEFAULT_SIZE);
    }
    
    public Window(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                //TODO: Make module targeting work correctly - fixed?
                /*if(paintTarget != null) {
                    if(paintCallback == null) {
                        paintTarget.paint(new GraphicsHandle(paintTarget.getAbsoluteX(), paintTarget.getAbsoluteY(), g));
                    }
                    else {
                        paintCallback.paint(new GraphicsHandle(paintTarget.getAbsoluteX(), paintTarget.getAbsoluteY(), g));
                    }
                }*/
                
                //TODO: Implement partial redraw calls by Modules
                g.drawImage(buffer, 0, 0, null);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        
        panel.addMouseListener(this);
        panel.addMouseWheelListener(this);
        frame.addKeyListener(this);
        
        lastMousePos = new Vector(Vector.ZERO);
        panel.addMouseMotionListener(this);
        
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = frame.getWidth();
                int height = frame.getHeight();
                width = (width < minWidth) ? minWidth : width;
                height = (height < minHeight) ? minHeight : height;
                frame.setSize(width, height);
                
                loadBuffer();
                
                if(child != null) {
                    child.resize(panel.getWidth(), panel.getHeight());
                }
            }
        });
        
        frame.add(panel);
        frame.pack();
        
        loadBuffer();
        
        child = Module.create(); //Create a dummy Module so there are no problems with null objects
        
        frame.setVisible(true);
    }
    
    public final void setChild(Module child) {
        this.child = child;
        child.setParent(this);
        child.repaint();
    }
    
    @Override
    public final void drawChild(Module child) {
        panel.repaint(child.getAbsoluteX(), child.getAbsoluteY(), child.width(), child.height());
    }
    
    @Override
    public final GraphicsHandle beginDraw(Module child) {
        return new GraphicsHandle(child.getAbsoluteX(), child.getAbsoluteY(), child.width(), child.height(), graphics);
    }
    
    public final GraphicsHandle beginDraw(Module child, int x, int y, int width, int height) {
        return new GraphicsHandle(x, y, width, height, graphics);
    }
    
    @Override
    public final void endDraw(GraphicsHandle g) {
        panel.repaint(g.offsetx, g.offsety, g.width, g.height);
    }
    
    public final BufferedImage getBuffer() {
        BufferedImage b = new BufferedImage(buffer.getWidth(), buffer.getHeight(), buffer.getType());
        Graphics g = b.createGraphics();
        g.drawImage(buffer, 0, 0, null);
        g.dispose();
        return b;
    }
    
    @Override
    public final int getAbsoluteX() {
        return 0;
    }

    @Override
    public final int getAbsoluteY() {
        return 0;
    }
    
    @Override
    public final void mouseClicked(MouseEvent me) {
        child.mouseClick(me);
    }

    @Override
    public final void mousePressed(MouseEvent me) {
        child.mouseDown(me);
    }

    @Override
    public final void mouseReleased(MouseEvent me) {
        child.mouseUp(me);
    }

    @Override
    public final void mouseEntered(MouseEvent me) {
    }

    @Override
    public final void mouseExited(MouseEvent me) {
    }
    
    //I'm assuming that mouseDragged and mouseMoved will never be called at the same time...
    @Override
    public final void mouseDragged(MouseEvent me) {
        Vector mousePos = new Vector(me.getX(), me.getY());
        Vector mouseDif = mousePos.getDif(lastMousePos);
        lastMousePos.set(mousePos);
        child.mouseMove(mousePos, mouseDif);
    }

    @Override
    public final void mouseMoved(MouseEvent me) {
        //TODO: Implement mouse moving
        Vector mousePos = new Vector(me.getX(), me.getY());
        Vector mouseDif = mousePos.getDif(lastMousePos);
        lastMousePos.set(mousePos);
        child.mouseMove(mousePos, mouseDif);
    }
    
    @Override
    public final void mouseWheelMoved(MouseWheelEvent mwe) {
        child.mouseScroll(mwe);
    }

    @Override
    public final void keyTyped(KeyEvent ke) {
        child.keyType(ke);
    }

    @Override
    public final void keyPressed(KeyEvent ke) {
        keyDown(ke);
        child.keyDown();
    }
    
    public void keyDown(KeyEvent e) { }

    @Override
    public final void keyReleased(KeyEvent ke) {
        keyUp(ke);
        child.keyUp();
    }
    
    public void keyUp(KeyEvent e) { }
    
    @Override
    public final Vector mousePosition() {
        return new Vector(
                MouseInfo.getPointerInfo().getLocation().x - frame.getX() - frame.getInsets().left,
                MouseInfo.getPointerInfo().getLocation().y - frame.getY() - frame.getInsets().top
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