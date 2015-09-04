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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import vague.ui.editor.Editor;
import vague.ui.module.Module;
import vague.util.ImageLoader;

/**
 * Main editor window.
 * @author TheMonsterFromTheDeep
 */
public class Window extends JFrame {
    public final static int DEFAULT_WIDTH = 800; //Default width and height of program
    public final static int DEFAULT_HEIGHT = 600;
    
    private JPanel panel; //Panel for main drawing of graphics
    
    private final Timer timer;
    
    private Module activeModule; //Stores the active module ui element. Event handlers call the module's event handling methods.
    
    Editor editor; //Editor - does all image editing; also stores various data like background color
    
    public Window() {
        setTitle("Vague Image Editor"); //Set the title of the window
        setIconImage(ImageLoader.loadProtected("/resource/img/logo.png")); //Load the image icon
        //TODO: Change close operation using window events
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Set the close operation so the program will end when closed
                
        editor = new Editor(DEFAULT_WIDTH,DEFAULT_HEIGHT) {
            @Override
            public void redraw() {
                panel.repaint();
            }
        };
        editor.render();
        
        activeModule = editor;
        
        //Only move at 33 fps to conserve resources
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int mousex = MouseInfo.getPointerInfo().getLocation().x; //Calculate mouse x and y
                int mousey = MouseInfo.getPointerInfo().getLocation().y;
                activeModule.tick(mousex, mousey);
            }  
        });
        
        panel = new JPanel() { //Initialize panel
            @Override
            public void paintComponent(Graphics g) { //Override paintComponent method so custom graphics can be drawn
                draw(g); //Call draw method
            }
        };
        panel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)); //Set preferredsize so that window isn't tiny
        panel.setBackground(editor.background); //Set background color
        
        add(panel); //Add panel to frame so that drawing stuff happens
        
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                editor.resize(panel.getWidth(), panel.getHeight());
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
                activeModule.keyDown(ke);
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                activeModule.keyUp(ke);
            }
        });
        
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                activeModule.mouseDown(me);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                activeModule.mouseUp(me);
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
                activeModule.mouseScroll(e);
            }
        });
        
        pack();

    }
    /**
     * Runs the window's program by showing it.
     */
    public void run() {
        setVisible(true); //Show the window
        timer.start();
    }
    
    /**
     * Contains all drawing code for the editor. Called in the paintComponent method of panel.
     * @param g The graphics object passed by paintComponent of which to use for all drawing.
     */
    private void draw(Graphics g) {
        g.drawImage(editor.lastRender, 0, 0, null); //Draw editor render - editor is bottom layer in drawing and takes up full screen
    }
}