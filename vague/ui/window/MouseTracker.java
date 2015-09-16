package vague.ui.window;

/**
 * Tracks mouse position.
 * @author TheMonsterFromTheDeep
 */
public abstract class MouseTracker {
    private int x; //The start x and y of the mouse.
    private int y;
    
    private int difx = 0;
    private int dify = 0;
    
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
        difx = x - this.x;
        dify = y - this.y;
        
        this.x = x;
        this.y = y;
        
        if(difx != 0 || dify != 0) { mouseMove(); } //Call mouse move if there is a difference in mouse pos
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDifX() { return difx; }
    public int getDifY() { return dify; }
    
    public abstract void mouseMove(); //Overloaded and called when mouse moved.
}