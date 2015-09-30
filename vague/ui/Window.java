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
import vague.module.container.Container;
import vague.ui.editor.Editor;
import vague.module.Module;
import vague.module.ModulePane;
import vague.module.MouseTracker;
import vague.module.container.HorizontalModulePane;
import vague.module.container.VerticalModulePane;
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
    
    HorizontalModulePane modules;
    
    WindowInterfacer moduleInterface; //Allows the window to interface with the module system
    
    private void initModules() { 
        int testwidth = 3;
        
        //Editor top = new Editor(DEFAULT_WIDTH, (DEFAULT_HEIGHT / 2));
        //Editor bottom = new Editor(DEFAULT_WIDTH, (DEFAULT_HEIGHT / 2) - testwidth);
        //bottom.y += (DEFAULT_HEIGHT / 2) + testwidth;
        
        Editor left = new Editor(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT);
        Editor middle = new Editor(2000,2000);
        Editor right = new Editor(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT);
        right.x += (DEFAULT_WIDTH / 2);
        
        //ModulePane left = new ModulePane(new Module[] { top, bottom }, false);
        
        modules = new HorizontalModulePane(DEFAULT_WIDTH, DEFAULT_HEIGHT, new Module[] { left, middle, right } );
    }
    
    public Window() {
        setTitle("Vague Image Editor"); //Set the title of the window
        setIconImage(ImageLoader.loadProtected("/resource/img/logo.png")); //Load the image icon
        //TODO: Change close operation using window events
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Set the close operation so the program will end when closed
        
        initModules();
        moduleInterface = new WindowInterfacer(DEFAULT_WIDTH, DEFAULT_HEIGHT, modules) {
            @Override
            public int windowMouseX() {
                return getWindowMouseX();
            }

            @Override
            public int windowMouseY() {
                return getWindowMouseY();
            }

            @Override
            public void drawWindow() {
                panel.repaint();
            }
        };
        
        mouseTracker = new MouseTracker(0,0) {//Initialize mouseTracker at 0, 0 so that mouse move is accurate at start
            public void mouseMove() {
                moduleInterface.mouseMove(this.getData()); //"this" refers to mousetracker; move modules if mouse tracker moved
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
                Window.this.setSize(d);
                moduleInterface.resize(panel.getWidth(), panel.getHeight());
            }

        });
        
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                moduleInterface.keyDown(ke);
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                moduleInterface.keyUp(ke);
            }
        });
        
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                moduleInterface.mouseDown(me);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                moduleInterface.mouseUp(me);
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
                moduleInterface.mouseScroll(e);
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
        g.drawImage(moduleInterface.lastRender, 0, 0, null); //Draw editor render - editor is bottom layer in drawing and takes up full screen
    }
}