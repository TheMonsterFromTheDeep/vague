package vague.util;

/**
 * The Vector class is a simple container for two integers.
 * 
 * It can be used for position or dimension.
 * @author TheMonsterFromTheDeep
 */
public class Vector {
    public int x; //The x value of the vector - either x position or width.
    public int y; //The y value of the vector - either y position or height.
    
    //The ZERO Vector stores a vector with zeroed values.
    public static Vector ZERO = new Vector();
    
    /**
     * Default constructor for Vector. Sets x and y to zero.
     */
    public Vector() {
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Constructs a vector from the given x and y.
     * @param x The x value of the vector.
     * @param y The y value of the vector.
     */
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructs a vector by copying another one.
     * @param v The vector to copy.
     */
    public Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    /**
     * Adds another vector to this one.
     * @param v The vector to add to this one.
     */
    public void add(Vector v) {
        this.x += v.x;
        this.y += v.y;
    }
    
    public boolean equals(Vector v) {
        return (this.x == v.x) && (this.y == v.y);
    }
}
