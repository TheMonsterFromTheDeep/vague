package vague.util;

/**
 * The Percents class contains a pair of x and y percentages used to store percent values so Module children
 * of a Container can be resized properly with the most possible information.
 * @author TheMonsterFromTheDeep
 */
public class Percents {
    public double x; //Stores the x (width) percentage
    public double y; //Stores the y (height) percentage
    
    public boolean initialized = false; //Stores whether the percent object has been officially initialized
    
    //The default constructor initializes to a value of 0.0 for both
    public Percents() {
        x = 0.0;
        y = 0.0;
    }
    
    /**
     * Creates a Percents object from two existing double values.
     * @param x The x percentage value.
     * @param y The y percentage value.
     */
    public Percents(double x, double y) {
        this.x = x;
        this.y = y;
        initialized = true; //The Percents object has been initialized usefully and as such this can be set to true
    }
    
    /**
     * Creates a Percents object based on the values for a percentage.
     * 
     * This constructor uses integer values.
     * @param partX The x of the part.
     * @param totalX The x of the whole.
     * @param partY The y of the part.
     * @param totalY The y of the whole.
     */
    public Percents(int partX, int totalX, int partY, int totalY) {
        //Cast to double so percent != 0 
        x = (double)partX / totalX;
        y = (double)partY / totalY;
        initialized = true;
    }
    
    /**
     * Creates a Percents object based on the values for a percentage.
     * 
     * This constructor uses floating point values.
     * @param partX The x of the part.
     * @param totalX The x of the whole.
     * @param partY The y of the part.
     * @param totalY The y of the whole.
     */
    public Percents(double partX, double totalX, double partY, double totalY) {
        x = partX / totalX;
        y = partY / totalY;
        initialized = true;
    }
    
    /*
    The various update() methods simply update the Percents object with new values rather than having
    to construct a new one.
    */
    public void update(double x, double y) {
        this.x = x;
        this.y = y;
        initialized = true;
    }
    
    public void update(int partX, int totalX, int partY, int totalY) {
        //Cast to double so percent != 0
        x = (double)partX / totalX;
        y = (double)partY / totalY;
        initialized = true;
    }
    
    public void update(double partX, double totalX, double partY, double totalY) {
        x = partX / totalX;
        y = partY / totalY;
        initialized = true;
    }
    
    /**
     * Returns a total factor scaled to the percentage.
     * @param v The factor to multiply
     * @return The factor of the percentage.
     */
    public Vector getFactor(Vector v) {
        return new Vector((int)Math.round(x * v.x), (int)Math.round(y * v.y));
    }
    
    /**
     * Returns a total factor scaled to the percentage.
     * @param x The x value of the total.
     * @param y The y value of the total.
     * @return The factor of the percentage.
     */
    public Vector getFactor(int x, int y) {
        return new Vector((int)Math.round(this.x * x), (int)Math.round(this.y * y));
    }
}