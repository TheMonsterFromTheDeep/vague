package vague.module;

/**
 * Stores various mouse data. Passed to modules through the mouseMove() method.
 * @author TheMonsterFromTheDeep
 */
public class MouseData {
    private int x;
    private int y;
    private int difx;
    private int dify;
    
    public MouseData(int x, int y, int difx, int dify) {
        this.x = x;
        this.y = y;
        this.difx = difx;
        this.dify = dify;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public int getDifX() { return difx; }
    public int getDifY() { return dify; }
    
    public MouseData getShift(int shiftX, int shiftY, int shiftDifX, int shiftDifY) {
        return new MouseData(this.getX() + shiftX, this.getY() + shiftY,this.getDifX() + shiftDifX, this.getDifY() + shiftDifY);
    }
}