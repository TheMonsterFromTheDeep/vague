package vague;

import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import module.Module;
import module.Window;
import vague.input.Controls;
import vague.workspace.Workspace;

/**
 * The Window class is a graphical window presented to the user. It is the base for all user interaction.
 * Events are passed through the window to all child modules.
 * @author TheMonsterFromTheDeep
 */
public class VagueWindow extends Window {
    public final static int DEFAULT_WIDTH = 800; //Default width and height of the window
    public final static int DEFAULT_HEIGHT = 600;
    
    public final static int MIN_WIDTH = 400;
    public final static int MIN_HEIGHT = 300;
    
    //Stores the delay between checks for mouse movement
    public final static int MOUSE_FRAMES = 25;
    //Right now it is 40 FPS
    
    private final Workspace workspace;
    
    /**
     * Constructs a new Window. This should be the only
     * constructor and should initialize the window for the application.
     * 
     * However, it should not yet run the program. The .run() method needs
     * to be called to begin the program.
     */
    public VagueWindow() {
        //The format for the title is "$Filename | Vague".
        super("Untitled | Vague", DEFAULT_WIDTH, DEFAULT_HEIGHT); //Set the title of the window. The default file is "Untitled".
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //When the window is closed, the program should exit.
        //this.setIconImage(Resources.bank.ICON);
        
        //this.setSize(800, 600);
        
        workspace = Workspace.create(this, DEFAULT_WIDTH, DEFAULT_HEIGHT, new Module[0]);
        
        setChild(workspace);
    }
    
    @Override
    public void keyDown(KeyEvent e) {
        Controls.bank.update(e.getKeyCode(), true);
    }
    
    @Override
    public void keyUp(KeyEvent e) {
        Controls.bank.update(e.getKeyCode(), false);
    }
}