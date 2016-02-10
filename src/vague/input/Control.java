package vague.input;

/**
 * Stores data on whether a specific keyboard control is activated or not.
 * @author TheMonsterFromTheDeep
 */
public class Control {
    public boolean status; //The status of the control - true for pressed and false for unpressed
    public int code; //The KeyEvent.VK_<X> keycode of the Control
    
    public Control(int code) {
        this.code = code;
        this.status = false;
    }
    
    public Control(Control c) {
        this.code = c.code;
        this.status = false;
    }
    
    public void update(int keyCode, boolean status) {
        if(keyCode == code) {
            this.status = status;
        }
    }
}