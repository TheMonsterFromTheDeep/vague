package vague;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import vague.module.Doodle;
import vague.module.Exchange;
import vague.module.Module;
import vague.util.MouseTracker;
import vague.util.Vector;

/**
 * The Window class is a graphical window presented to the user. It is the base for all user interaction.
 * Events are passed through the window to all child modules.
 * @author TheMonsterFromTheDeep
 */
public class Window extends JFrame {
    public final static int DEFAULT_WIDTH = 800; //Default width and height of the window
    public final static int DEFAULT_HEIGHT = 600;
    
    public final static int MIN_WIDTH = 400;
    public final static int MIN_HEIGHT = 300;
    
    //Stores the delay between checks for mouse movement
    public final static int MOUSE_FRAMES = 25;
    //Right now it is 40 FPS
    
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
    
    /**
     * Constructs a new Window. This should be the only
     * constructor and should initialize the window for the application.
     * 
     * However, it should not yet run the program. The .run() method needs
     * to be called to begin the program.
     */
    public Window() {
        //The format for the title is "$Filename | Vague".
        super("Untitled | Vague"); //Set the title of the window. The default file is "Untitled".
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //When the window is closed, the program should exit.

        
        /*
        In order to prevent the window from being resized beyond a certain margin, the minimum size
        needs to be set and a special component adapter is added which resizes the module when it is
        resized but will not let it resize beyond the minimum size.
        */
        this.setMinimumSize(new Dimension(MIN_WIDTH,MIN_HEIGHT));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                Dimension size = Window.this.getSize();
                if(size.width < MIN_WIDTH) { size.width = MIN_WIDTH; }
                if(size.height < MIN_HEIGHT) { size.height = MIN_HEIGHT; }
                Window.this.setSize(size);                
            }
        });
        
        /*
        The KeyListener that is added to the Window simply passes key event data to the Exchange class
        when keys are pressed.
        */
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) { system.keyDown(ke); }

            @Override
            public void keyPressed(KeyEvent ke) { system.keyType(ke); }

            @Override
            public void keyReleased(KeyEvent ke) { system.keyUp(ke); }
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
        
        mouseTracker = new MouseTracker();
        mouseTimer = new Timer(MOUSE_FRAMES, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                mouseTracker.track(windowMousePosition());
                if(mouseTracker.moved()) {
                    system.mouseMove(mouseTracker.position());
                }
            }
        });
        
        /*
        A JPanel object will be used for all graphical presentation.
        
        The paintComponent() method will be overloaded and used to call drawing
        methods of child classes.
        */
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                draw(g); //Pass the graphics object to the class's draw() method so it can render the things it needs to
            }
        };
        panel.setPreferredSize(new java.awt.Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT));
        this.add(panel); //Add the JPanel so that its rendering will be reflected on the shown window
        this.pack(); //Pack the JFrame
        
                
        system = new Exchange(new Doodle(DEFAULT_WIDTH,DEFAULT_HEIGHT)) {
            @Override
            public Vector mousePosition() {
                return windowMousePosition();
            }

            @Override
            public void drawWindow() {
                panel.repaint();
            }
        };
    }
    
    /**
     * Runs the program, allowing the user to interact with the controls.
     */
    public void run() {
        this.setVisible(true); //Set the window visible so that the user can interact.
        mouseTimer.start(); //Start the mouse timer so that most of the app will actually work
    }
    
    /**
     * Gets the mouse position using the AWT MouseInfo object. 
     * 
     * windowMousePosition() is passed to the 'system' Exchange object through its mousePosition()
     * abstract method so that the Module system can use the mouse position.
     * @return a Vector containing the mouse position.
     */
    private Vector windowMousePosition() {
        return new Vector(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
    }
    
    /**
     * Does all the drawing for the program.
     * 
     * The draw method takes a Graphics object as a parameter and uses it to draw all
     * the graphics of the program.
     * 
     * The graphics are rendered from child classes and then drawn onto the top parent
     * window (this class) in order to present them to the end user.
     * 
     * Only specific graphics are necessarily drawn because rendering time needs to be
     * kept to a minimum.
     * @param g 
     */
    private void draw(Graphics g) {
        g.drawImage(system.render(), 0, 0, null);
    }
}