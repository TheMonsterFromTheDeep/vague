package vague.input;

/**
 * Stores data on whether a specific keyboard control is activated or not.
 * @author TheMonsterFromTheDeep
 */
public class Control {
    public boolean status; //The status of the control - true for pressed and false for unpressed
    public int code; //The KeyEvent.VK_<X> keycode of the Control
    public int id; //The identifier by which this control is identified - usually a static final integer value
    
    public Control(int id, int code) {
        this.id = id;
        this.code = code;
        this.status = false;
    }
}