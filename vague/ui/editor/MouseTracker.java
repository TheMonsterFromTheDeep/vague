package vague.ui.editor;

/**
 * Tracks mouse position.
 * @author TheMonsterFromTheDeep
 */
public class MouseTracker {
    public int x; //The start x and y of the mouse.
    public int y;
    
    public MouseTracker(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Shift to new position after object has been moved to old.
     * @param x X of new position.
     * @param y Y of new position.
     */
    public void shift(int x, int y) {
        this.x = x;
        this.y = y;
    }
}