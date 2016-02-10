package vague.input;

import java.awt.event.KeyEvent;

/**
 * Represents a set of Controls that can be updated individually. Only when all Controls are in the active
 * status is the Chord also active.
 * @author TheMonsterFromTheDeep
 */
public class Chord {
    private Control[] controls;
    
    private static final Control CONTROL_SHIFT = new Control(KeyEvent.VK_SHIFT);
    private static final Control CONTROL_CONTROL = new Control(KeyEvent.VK_CONTROL);
    
    public static final int MODIFIER_SHIFT = 0;
    public static final int MODIFIER_CONTROL = 1;
    public static final int MODIFIER_SHIFT_AND_CONTROL = 2;
    
    public int id;
    public boolean status;
    
    public Chord(int id, Control control) {
        controls = new Control[1];
        controls[0] = control;
        
        this.id = id;
        this.status = false;
    }
    
    public Chord(int id, Control[] controls) {
        this.controls = controls;
        
        this.id = id;
        this.status = false;
    }
    
    public Chord(int id, Control control, int modifier) {
        switch(modifier) {
            case MODIFIER_SHIFT:
                this.controls = new Control[2];
                controls[0] = new Control(CONTROL_SHIFT);
                controls[1] = control;
                break;
            case MODIFIER_CONTROL:
                this.controls = new Control[2];
                controls[0] = new Control(CONTROL_CONTROL);
                controls[1] = control;
                break;
            case MODIFIER_SHIFT_AND_CONTROL:
                this.controls = new Control[3];
                controls[0] = new Control(CONTROL_SHIFT);
                controls[1] = new Control(CONTROL_CONTROL);
                controls[2] = control;
                break;
            default:
                this.controls = new Control[1];
                controls[0] = control;
        }
        
        this.id = id;
        this.status = false;
    }
    
    public void update(int keyCode, boolean status) {
        this.status = true;
        for(Control c : controls) {
            c.update(keyCode, status);
            if(!c.status) { this.status = false; }
        }
    }
}