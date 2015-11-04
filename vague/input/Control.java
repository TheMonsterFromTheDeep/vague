package vague.input;

/**
 * Stores data on whether a specific keyboard control is activated or not.
 * @author TheMonsterFromTheDeep
 */
public class Control {
    public boolean status;
    public int code;
    public int id;
    
    public Control(int id, int code) {
        this.id = id;
        this.code = code;
        this.status = false;
    }
}