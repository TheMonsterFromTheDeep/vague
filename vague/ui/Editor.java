package vague.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import vague.ui.editor.EditorData;
import vague.ui.editor.MouseTracker;
import vague.ui.image.Renderer;
import vague.util.ImageLoader;

/**
 * Main editor window.
 * @author TheMonsterFromTheDeep
 */
public class Editor extends JFrame {
    public final static int DEFAULT_WIDTH = 800; //Default width and height of program
    public final static int DEFAULT_HEIGHT = 600;
    
    private JPanel panel; //Panel for main drawing of graphics
    
    private EditorData data; //Stores various data for the editor
    private Renderer renderer; //Stores the end-user image renderer
    
    private MouseTracker mtracker; //Mouse tracker - used for panning image
    private Timer pantimer; //Timer for when panning
    
    public Editor() {
        data = new EditorData(DEFAULT_WIDTH, DEFAULT_HEIGHT); //Initialize editor data
        setTitle("Vague Image Editor"); //Set the title of the window
        setIconImage(ImageLoader.loadProtected("/resource/img/logo.png")); //Load the image icon
        //TODO: Change close operation using window events
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Set the close operation so the program will end when closed
        
        //Only move at 33 fps to conserve resources
        pantimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int mousex = MouseInfo.getPointerInfo().getLocation().x; //Calculate mouse x and y
                int mousey = MouseInfo.getPointerInfo().getLocation().y;
                renderer.pan(mousex - mtracker.x, mousey - mtracker.y); //Pan the renderer
                mtracker.shift(mousex, mousey); //Shift the mousetracker
                panel.repaint(); //Repaint the panel
            }  
        });
        
        renderer = new Renderer();
        
        panel = new JPanel() { //Initialize panel
            @Override
            public void paintComponent(Graphics g) { //Override paintComponent method so custom graphics can be drawn
                draw(g); //Call draw method
            }
        };
        panel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)); //Set preferredsize so that window isn't tiny
        panel.setBackground(data.background); //Set background color
        
        add(panel); //Add panel to frame so that drawing stuff happens
        
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                data.resize(panel.getWidth(), panel.getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
            }

            @Override
            public void componentShown(ComponentEvent ce) {
            }

            @Override
            public void componentHidden(ComponentEvent ce) {
            }
        });
        
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                keyPress(ke);
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                mouseDown(me);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                mouseUp(me);
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) { //Listen for mouse scroll events
                if(e.isControlDown()) { //If control key is down, zoom in/out
                    renderer.scale(-1 * e.getWheelRotation()); //Scale renderer inverse of wheelrotation (up wheel rotation is negative, but up wheel rotation should be positive scale -> bigger -> zoom in)
                    renderer.render(); //Re-render the scaled image
                    panel.repaint();
                }
            }
        });
        
        pack();
    }
    /**
     * Runs the window's program by showing it.
     */
    public void run() {
        setVisible(true); //Show the window
        renderer.render();
    }
    
    /**
     * Contains all drawing code for the editor. Called in the paintComponent method of panel.
     * @param g The graphics object passed by paintComponent of which to use for all drawing.
     */
    private void draw(Graphics g) {
        g.setColor(data.background); //Clear image
        g.fillRect(0, 0, data.width, data.height);
        
        g.drawImage(renderer.lastRender,
                ((data.width - renderer.lastRender.getWidth()) / 2) + renderer.posx,
                ((data.height - renderer.lastRender.getHeight()) / 2) + renderer.posy,
                null);
    }
    
    /**
     * Handles all key events for application.
     * @param e The KeyEvent object passed by the event.
     */
    private void keyPress(KeyEvent e) {
        if(e.isControlDown()) {
            if(e.getKeyCode() == KeyEvent.VK_EQUALS) {
                renderer.scale(1);
                renderer.render(); //Re-render image to reflect changes
                panel.repaint();
            }
            if(e.getKeyCode() == KeyEvent.VK_MINUS) {
                renderer.scale(-1);
                renderer.render();
                panel.repaint();
            }
        }
    }
    
    private void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
            mtracker = new MouseTracker(e.getX(),e.getY());
            pantimer.start();
        }
    }
    
    private void mouseUp(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
            renderer.pan(e.getX() - mtracker.x, e.getY() - mtracker.y);
            panel.repaint(); //Repaint without rendering for efficiency
            pantimer.stop();
        }
    }
}