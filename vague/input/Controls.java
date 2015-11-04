package vague.input;

import java.awt.event.KeyEvent;

/**
 * Stores the statuses of all the different controls used in the application.
 * @author TheMonsterFromTheDeep
 */
public class Controls {
    public static final int WORKSPACE_SQUARE_TOOL = 0;
    public static final int WORKSPACE_GRID_SNAP = 1;
    
    public static final Control[] DEFAULT = new Control[] {
        new Control(WORKSPACE_SQUARE_TOOL,KeyEvent.VK_SHIFT),
        new Control(WORKSPACE_GRID_SNAP,KeyEvent.VK_CONTROL)
    };
    
    public static Controls bank;
    
    private Control[] controls;
    
    public Controls(Control[] controls) {
        this.controls = controls;
    }
    
    public boolean status(int id) {
        for (Control c : controls) {
            if(c.id == id) {
                return c.status;
            }
        }
        return false;
    }
    
    public void update(int code, boolean status) {
        for (Control c : controls) {
            if(c.code == code) {
                c.status = status;
            }
        }
    }
}