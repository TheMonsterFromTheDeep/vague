package vague.input;

import java.awt.event.KeyEvent;

/**
 * Stores the statuses of all the different controls used in the application.
 * @author TheMonsterFromTheDeep
 */
public class Controls {
    public static final int WORKSPACE_SQUARE_TOOL = 0; //The control for making tools square on the Workspace
    public static final int WORKSPACE_GRID_SNAP = 1; //The control for snapping tools to grid on the Workspace
    public static final int TEST_COLOR = 900; //A test control for changing drawing color on the TestModule
    
    //Initializes the default state of the controls of the application
    public static final Control[] DEFAULT = new Control[] {
        new Control(WORKSPACE_SQUARE_TOOL,KeyEvent.VK_SHIFT),
        new Control(WORKSPACE_GRID_SNAP,KeyEvent.VK_CONTROL),
        new Control(TEST_COLOR,KeyEvent.VK_SPACE)
    };
    
    public static Controls bank; //The static access controls object from which other objects access the state of controls
    
    //Stores the controls of the controls object
    private Control[] controls;
    
    
    //Creates a Controls object based on the array of 'Control's to use
    public Controls(Control[] controls) {
        this.controls = controls;
    }
    
    //Returns the status of the Control with the specified ID
    public boolean status(int id) {
        for (Control c : controls) { //Loop through controls; if the id matches the one to find, return its status
            if(c.id == id) {
                return c.status;
            }
        }
        return false; //If the id wasn't found, return false by default (this should never happen)
    }
    
    public void update(int code, boolean status) {
        for (Control c : controls) { //Loop through controls and update all the ones with the specified key code
            //Does not update by id because this method is called by a Key Event which does not know the ids
            //it simply updates any Controls that may exist with the specified key code
            if(c.code == code) {
                c.status = status;
            }
        }
    }
}