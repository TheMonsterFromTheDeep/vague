package vague.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
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
import vague.ui.window.MouseTracker;
import vague.util.ImageLoader;

/**
 * Main editor window.
 * @author TheMonsterFromTheDeep
 */
public class Window extends JFrame {
    public final static int DEFAULT_WIDTH = 800; //Default width and height of program
    public final static int DEFAULT_HEIGHT = 600;
    
    public final static Color DEF_BG_COLOR = new Color(0xd0d3fb); //Default background color
    
    MouseTracker mouseTracker;
    
    private JPanel panel; //Panel for main drawing of graphics
    
    private final Timer timer;
    
    ModulePane modules;
    
    private void initModules() {        
        Editor top = new Editor(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2);
        Editor bottom = new Editor(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2);
        bottom.y += (DEFAULT_HEIGHT / 2) + 3;
        
        Editor right = new Editor(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT);
        right.x += (DEFAULT_WIDTH / 2) + 3;
        
        ModulePane left = new ModulePane(new Module[] { top, bottom }, false);
        
        modules = new ModulePane(new Module[] { left, right }, true) {
            @Override
            public void drawParent() {
                panel.repaint();
            }
            @Override
            public void drawParent(Module m) {
                panel.repaint();
            }
            @Override
            public int getCompMouseX() { return getWindowMouseX(); }
            @Override
            public int getCompMouseY() { return getWindowMouseY(); }
        };
    }
    
    public Window() {
        setTitle("Vague Image Editor"); //Set the title of the window
        setIconImage(ImageLoader.loadProtected("/resource/img/logo.png")); //Load the image icon
        //TODO: Change close operation using window events
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Set the close operation so the program will end when closed
        
        mouseTracker = new MouseTracker(0,0) {//Initialize mouseTracker at 0, 0 so that mouse move is accurate at start
            public void mouseMove() {
                modules.mouseMove(this.getDifX(),this.getDifY()); //"this" refers to mousetracker; move modules if mouse tracker moved
            }
        };
        
        //Only move at 33 fps to conserve resources
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                mouseTracker.shift(getWindowMouseX(), getWindowMouseY()); //Update mousetracker; mousetracker will cause mouseMove() method if the mouse moved
                
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
        
        this.setMinimumSize(new Dimension(220,110));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                Dimension d = Window.this.getSize();
                Dimension minD = Window.this.getMinimumSize();
                if(d.width < minD.width) {
                    d.width = minD.width;
                }
                if(d.height < minD.height) {
                    d.height = minD.height;
                }
                System.err.println(d.width);
                Window.this.setSize(d);
                modules.resize(panel.getWidth(), panel.getHeight());
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
    
    private int getWindowMouseX() {
        return MouseInfo.getPointerInfo().getLocation().x - this.getX() - this.getInsets().left;
    }
    
    private int getWindowMouseY() {
        return MouseInfo.getPointerInfo().getLocation().y - this.getY() - this.getInsets().top;
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