package vague.util;

/**
 * Tracks the mouse. Stores a vector of the old position and checks to see if the mouse moved.
 * @author TheMonsterFromTheDeep
 */
public class MouseTracker {
    private Vector oldPos;
    private Vector dif;
    private boolean moved = false;
   
    public MouseTracker() {
        oldPos = new Vector();
    }
    
    /**
     * Tracks the mouse to the current position.
     * @param mousePos The mouse position to check against.
     */
    public void track(Vector mousePos) {
        if(oldPos.exacts(mousePos)) {
            moved = false;
        }
        else {
            moved = true;
            dif = mousePos.getDif(oldPos);
            oldPos = mousePos; //Change oldPos after dif so that dif doesn't get the new version of oldPos which is equal to mousePos           
        }
    }
    
    /**
     * The mouse position as of the last track().
     * @return The mouse position.
     */
    public Vector position() {
        return oldPos;
    }
    
    /**
     * The amount the mouse moved as of the last track().
     * @return The amount the mouse moved.
     */
    public Vector difference() {
        return dif;
    }
    
    /**
     * Returns, as of the last track, whether the mouse had moved.
     * @return Whether the mouse moved.
     */
    public boolean moved() {
        return moved;
    }
}
