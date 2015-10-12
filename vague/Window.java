package vague;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The Window class is a graphical window presented to the user. It is the base for all user interaction.
 * Events are passed through the window to all child modules.
 * @author TheMonsterFromTheDeep
 */
public class Window extends JFrame {
    public final static int DEFAULT_WIDTH = 800; //Default width and height of the window
    public final static int DEFAULT_HEIGHT = 600;
    
    /*
    A buffer for drawing graphics. The buffer is used so that only certain
    child classes can be drawn while the JPanel used for drawing can actually
    be rendered.
    */
    private BufferedImage buffer;
    private Graphics graphics; //Stores the graphics object which draws to the buffer.
    /*
    Note: maybe the interface between the window and the module children will end up storing
    the buffer so that two images don't have to be used?
    */
    
    
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
        
        /*
        Initialize the components of the buffered graphics system.
        
        The image needs to be initialized the width and height of the module, while
        the graphics object simply needs to be initialized to draw to the buffer.
        */
        buffer = new BufferedImage(DEFAULT_WIDTH,DEFAULT_HEIGHT,BufferedImage.TYPE_INT_ARGB);
        graphics = buffer.createGraphics();
        
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
    }
    
    /**
     * Runs the program, allowing the user to interact with the controls.
     */
    public void run() {
        this.setVisible(true); //Set the window visible so that the user can interact.
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
        g.drawImage(buffer, 0, 0, null);
    }
}