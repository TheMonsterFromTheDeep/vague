package vague.ui;

import java.awt.Color;
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
import vague.module.Module;
import vague.module.ModulePane;
import vague.util.ImageLoader;

/**
 * Main editor window.
 * @author TheMonsterFromTheDeep
 */
public class Window extends JFrame {
    public final static int DEFAULT_WIDTH = 800; //Default width and height of program
    public final static int DEFAULT_HEIGHT = 600;
    
    public final static Color DEF_BG_COLOR = new Color(0xd0d3fb); //Default background color
    
    private JPanel panel; //Panel for main drawing of graphics
    
    private final Timer timer;
    
    Editor editor; //Editor - does all image editing; also stores various data like background color
    
    ModulePane modules;
    
    private void initModules() {
        editor = new Editor(DEFAULT_WIDTH,DEFAULT_HEIGHT / 2);
        Editor editor2 = new Editor(DEFAULT_WIDTH,DEFAULT_HEIGHT / 2);
        editor2.y += DEFAULT_HEIGHT / 2;
        
        //modules = new ModulePane(new Module[] { editor });
        modules = new ModulePane(new Module[] { editor, editor2 }, false) {
            @Override
            public void drawParent() {
                panel.repaint();
            }
        };
        modules.setParent(null);
        editor.drawSelf();
    }
    
    public Window() {
        setTitle("Vague Image Editor"); //Set the title of the window
        setIconImage(ImageLoader.loadProtected("/resource/img/logo.png")); //Load the image icon
        //TODO: Change close operation using window events
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Set the close operation so the program will end when closed
        
        
        
        //Only move at 33 fps to conserve resources
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int mousex = MouseInfo.getPointerInfo().getLocation().x; //Calculate mouse x and y
                int mousey = MouseInfo.getPointerInfo().getLocation().y;
                modules.tick(mousex, mousey);
            }  
        });
        
        panel = new JPanel() { //Initialize panel
            @Override
            public void paintComponent(Graphics g) { //Override paintComponent method so custom graphics can be drawn
                draw(g); //Call draw method
            }
        };
        panel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)); //Set preferredsize so that window isn't tiny
        panel.setBackground(DEF_BG_COLOR); //Set background color
        
        add(panel); //Add panel to frame so that drawing stuff happens
        
        initModules();
        
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                //Resize the editor component.
                modules.resize(panel.getWidth(), panel.getHeight());
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
                modules.keyDown(ke);
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                modules.keyUp(ke);
            }
        });
        
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                modules.mouseDown(me);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                modules.mouseUp(me);
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
                modules.mouseScroll(e);
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
        g.drawImage(modules.lastRender, 0, 0, null); //Draw editor render - editor is bottom layer in drawing and takes up full screen
    }
}