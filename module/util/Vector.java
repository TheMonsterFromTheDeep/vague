package module.util;

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
    
    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }
    
    /**
     * Subtracts another vector from this one.
     * @param v The vector to subtract from this one.
     */
    public void subtract(Vector v) {
        this.x -= v.x;
        this.y -= v.y;
    }
    
    public void subtract(int x, int y) {
        this.x += x;
        this.y += y;
    }
    
    /**
     * Returns a Vector sum of this Vector and the passed Vector object.
     * 
     * This method does not modify this object.
     * @param v The Vector to sum to this one.
     * @return A Vector object containing the sum of this vector and the passed object.
     */
    public Vector getSum(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y);
    }
    
    /**
     * Returns a Vector difference of this Vector and the passed Vector object.
     * 
     * This method does not modify this object.
     * @param v The Vector to subtract to this one.
     * @return A Vector object containing the difference between this vector and the passed object.
     */
    public Vector getDif(Vector v) {
        return new Vector(this.x - v.x, this.y - v.y);
    }
    
    /**
     * Returns whether both the x and the y are the same as the specified Vector.
     * @param v The vector to compare to.
     * @return Whether the two vectors are equal.
     */
    public boolean exacts(Vector v) {
        return (this.x == v.x) && (this.y == v.y);
    }
    
    /**
     * Returns whether either the x or the y are the same as the specified Vector.
     * @param v The vector to compare to.
     * @return Whether the two vectors are similar.
     */
    public boolean similar(Vector v) {
        return (this.x == v.x) || (this.y == v.y);
    }
    
    /**
     * Snaps the Vector to a grid of a specific size.
     * @param size The increments in size to snap to.
     */
    public void snap(int size) {
        this.x = (this.x / size) * size; //Integer divides the size and re-multiplies it, causing the sizes to be snapped
        this.y = (this.y / size) * size;
    }
}
