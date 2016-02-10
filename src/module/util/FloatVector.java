package module.util;

/**
 * The FloatVector class is a simple container for two floats.
 * 
 * It can be used for position or dimension.
 * @author TheMonsterFromTheDeep
 */
public class FloatVector {
    public float x; //The x value of the vector - either x position or width.
    public float y; //The y value of the vector - either y position or height.
    
    //The ZERO Vector stores a vector with zeroed values.
    public static FloatVector ZERO = new FloatVector();
    
    /**
     * Default constructor for Vector. Sets x and y to zero.
     */
    public FloatVector() {
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Constructs a vector from the given x and y.
     * @param x The x value of the vector.
     * @param y The y value of the vector.
     */
    public FloatVector(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructs a vector by copying another one.
     * @param v The vector to copy.
     */
    public FloatVector(FloatVector v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    /**
     * Adds another vector to this one.
     * @param v The vector to add to this one.
     */
    public void add(FloatVector v) {
        this.x += v.x;
        this.y += v.y;
    }
    
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }
    
    /**
     * Subtracts another vector from this one.
     * @param v The vector to subtract from this one.
     */
    public void subtract(FloatVector v) {
        this.x -= v.x;
        this.y -= v.y;
    }
    
    public void subtract(float x, float y) {
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
    public FloatVector getSum(FloatVector v) {
        return new FloatVector(this.x + v.x, this.y + v.y);
    }
    
    /**
     * Returns a Vector difference of this Vector and the passed Vector object.
     * 
     * This method does not modify this object.
     * @param v The Vector to subtract to this one.
     * @return A Vector object containing the difference between this vector and the passed object.
     */
    public FloatVector getDif(FloatVector v) {
        return new FloatVector(this.x - v.x, this.y - v.y);
    }
    
    /**
     * Returns whether both the x and the y are the same as the specified Vector.
     * @param v The vector to compare to.
     * @return Whether the two vectors are equal.
     */
    public boolean exacts(FloatVector v) {
        return (this.x == v.x) && (this.y == v.y);
    }
    
    /**
     * Returns whether either the x or the y are the same as the specified Vector.
     * @param v The vector to compare to.
     * @return Whether the two vectors are similar.
     */
    public boolean similar(FloatVector v) {
        return (this.x == v.x) || (this.y == v.y);
    }
    
    /**
     * Sets a Vector object to be identical to another Vector object without requiring reallocation of memory.
     * @param v The Vector object to set this Vector identical to.
     */
    public void set(FloatVector v) {
        this.x = v.x;
        this.y = v.y;
    }
}
