package vague;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import module.util.Rectangle;
import vague.input.Control;
import vague.input.Controls;
import module.Exchange;
import module.GraphicsCallback;
import module.Module;
import vague.module.TestModule;
import module.container.Container;
import vague.util.MouseTracker;
import module.util.Vector;
import vague.workspace.Workspace;

/**
 * The Window class is a graphical window presented to the user. It is the base for all user interaction.
 * Events are passed through the window to all child modules.
 * @author TheMonsterFromTheDeep
 */
public class VagueWindow extends JFrame {
    public final static int DEFAULT_WIDTH = 800; //Default width and height of the window
    public final static int DEFAULT_HEIGHT = 600;
    
    public final static int MIN_WIDTH = 400;
    public final static int MIN_HEIGHT = 300;
    
    //Stores the delay between checks for mouse movement
    public final static int MOUSE_FRAMES = 25;
    //Right now it is 40 FPS
    
    Cursor hiddenCursor;
    Cursor shownCursor;
    
    /*
    The 'system' member is simpy the interface between this Window class and the Module system.
    */
    private Exchange system;
    
    /*
    Checks if the mouse is moved and calls Module events if it is through the 'system' Exchange.
    */
    private Timer mouseTimer;
    
    /*
    The mouseTracker simply tracks the mouse.
    
    It is used to determine whether to fire a mouse-moved psuedo-event.
    */
    MouseTracker mouseTracker;
    
    GraphicsCallback nextCallback;
    
    /**
     * Constructs a new Window. This should be the only
     * constructor and should initialize the window for the application.
     * 
     * However, it should not yet run the program. The .run() method needs
     * to be called to begin the program.
     */
    public VagueWindow() {
        //The format for the title is "$Filename | Vague".
        super("Untitled | Vague"); //Set the title of the window. The default file is "Untitled".
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //When the window is closed, the program should exit.
        this.setIconImage(Resources.bank.ICON);  
        
        /*
        A JPanel object will be used for all graphical presentation.
        
        The paintComponent() method will be overloaded and used to call drawing
        methods of child classes.
        */
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                if(nextCallback != null) {
                    nextCallback.callback(g);
                    nextCallback = null;
                }
            }
        };
        panel.setPreferredSize(new java.awt.Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT));
        this.add(panel); //Add the JPanel so that its rendering will be reflected on the shown window
        this.pack(); //Pack the JFrame
        
        this.setLocationRelativeTo(null); //Center the window
        
        //TestModule[] testChildMods = new TestModule[] { new TestModule(1, 1) };
        
        shownCursor = Cursor.getDefaultCursor();
        hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "transparent cursor");
        
        //system = new Exchange(Workspace.create(DEFAULT_WIDTH,DEFAULT_HEIGHT,new Module[]{})) {
        system = new Exchange(TestModule.create(DEFAULT_WIDTH,DEFAULT_HEIGHT)) {
            @Override
            public Vector mousePosition() {
                return windowMousePosition();
            }

            @Override
            public void drawWindow(int x, int y, int width, int height) {
                panel.repaint(0, x, y, width, height);
            }
            
            @Override
            public void hideCursor() {
                panel.setCursor(hiddenCursor);
            }
            
            @Override
            public void showCursor() {
                panel.setCursor(shownCursor);
            }
        };
        
        /*
        In order to prevent the window from being resized beyond a certain margin, the minimum size
        needs to be set and a special component adapter is added which resizes the module when it is
        resized but will not let it resize beyond the minimum size.
        */
        this.setMinimumSize(new Dimension(MIN_WIDTH,MIN_HEIGHT));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                Dimension size = VagueWindow.this.getSize();
                if(size.width < MIN_WIDTH) { size.width = MIN_WIDTH; }
                if(size.height < MIN_HEIGHT) { size.height = MIN_HEIGHT; }
                VagueWindow.this.setSize(size); 
                system.resize(size.width,size.height);
            }
        });
        
        /*
        The KeyListener that is added to the Window simply passes key event data to the Exchange class
        when keys are pressed.
        */
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) { system.keyType(ke); }

            @Override
            public void keyPressed(KeyEvent ke) {
                Controls.bank.update(ke.getKeyCode(),true);
                system.keyDown(); 
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                Controls.bank.update(ke.getKeyCode(),false);
                system.keyUp(); 
            }
        });
        
        /*
        The MouseListener that is added to the Window simply passes mouse event data to the Exchange class
        when MouseEvents are fired.
        */
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) { system.mouseClick(me); }

            @Override
            public void mousePressed(MouseEvent me) { system.mouseDown(me); }

            @Override
            public void mouseReleased(MouseEvent me) { system.mouseUp(me); }

            @Override
            public void mouseEntered(MouseEvent me) {}

            @Override
            public void mouseExited(MouseEvent me) {}
        });
        
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                system.mouseScroll(mwe);
            }            
        });
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                system.resize(VagueWindow.this.getSize().width,VagueWindow.this.getSize().height);
                System.err.println("Window state changed!");
            }
        });
        
        mouseTracker = new MouseTracker();
        mouseTimer = new Timer(MOUSE_FRAMES, (ActionEvent e) -> {
            mouseTracker.track(windowMousePosition());
            if(mouseTracker.moved()) {
                system.mouseMove(mouseTracker.position(),mouseTracker.difference());
            }
        });
    }
    
    public void paintRect(int x, int y, int width, int height) {
        
    }
    
    /**
     * Runs the program, allowing the user to interact with the controls.
     */
    public void run() {
        system.redraw(); //Redraw so that there is content on the screen
        this.setVisible(true); //Set the window visible so that the user can interact.
        mouseTimer.start(); //Start the mouse timer so that most of the app will actually work
    }
    
    public void requestDraw(int x, int y, int width, int height, GraphicsCallback g) {
        System.err.println("recieved draw reqquest");
        if(nextCallback == null) {
            nextCallback = g;
            
            repaint(x, y, width, height);
        }
    }
    
    /**
     * Gets the mouse position using the AWT MouseInfo object. 
     * 
     * windowMousePosition() is passed to the 'system' Exchange object through its mousePosition()
     * abstract method so that the Module system can use the mouse position.
     * @return a Vector containing the mouse position.
     */
    private Vector windowMousePosition() {
        return new Vector(
                MouseInfo.getPointerInfo().getLocation().x - this.getX() - this.getInsets().left,
                MouseInfo.getPointerInfo().getLocation().y - this.getY() - this.getInsets().top
        );
    }
}