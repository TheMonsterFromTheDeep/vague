package vague.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import vague.ui.editor.EditorData;
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
    
    public Editor() {
        data = new EditorData(DEFAULT_WIDTH, DEFAULT_HEIGHT); //Initialize editor data
        setTitle("Vague Image Editor"); //Set the title of the window
        setIconImage(ImageLoader.loadProtected("/resource/img/logo.png")); //Load the image icon
        //TODO: Change close operation using window events
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Set the close operation so the program will end when closed
        
        
        
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
        
        pack();
    }
    /**
     * Runs the window's program by showing it.
     */
    public void run() {
        setVisible(true); //Show the window
    }
    
    /**
     * Contains all drawing code for the editor. Called in the paintComponent method of panel.
     * @param g The graphics object passed by paintComponent of which to use for all drawing.
     */
    private void draw(Graphics g) {
        g.setColor(data.background); //Clear image
        g.fillRect(0, 0, data.width, data.height);
        
        BufferedImage endUser = renderer.render(); //Render end-user image
        g.drawImage(endUser,
                ((data.width - endUser.getWidth()) / 2) + renderer.posx,
                ((data.height - endUser.getHeight()) / 2) + renderer.posy,
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
                panel.repaint();
            }
            if(e.getKeyCode() == KeyEvent.VK_MINUS) {
                renderer.scale(-1);
                panel.repaint();
            }
        }
    }
}