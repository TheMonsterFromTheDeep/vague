package vague.geom;

import vague.util.Vector;

/**
 * Defines a rectangle with a position in coordinate space along with a width and a height.
 * @author TheMonsterFromTheDeep
 */
public class Rectangle {
    public Vector position;
    public Vector size;
    
    /**
     * Creates a Rectangle with the specified position and size.
     * @param position The position of the Rectangle.
     * @param size The size of the Rectangle.
     */
    public Rectangle(Vector position, Vector size) {
        this.position = position;
        this.size = size;
    }
    
    /**
     * Creates a Rectangle with the specified position and size.
     * @param x The x position of the Rectangle.
     * @param y The y position of the Rectangle.
     * @param width The width of the Rectangle.
     * @param height The height of the Rectangle. 
     */
    public Rectangle(int x, int y, int width, int height) {
        this.position = new Vector(x,y);
        this.size = new Vector(width,height);
    }
    
    /**
     * Creates a Rectangle copy of the specified Rectangle.
     * @param r The Rectangle to copy.
     */
    public Rectangle(Rectangle r) {
        position = new Vector(r.position);
        size = new Vector(r.size);
    }
    
    /*
    These methods return a Vector object containing the position of various corners of the Rectangle.
    */
    public Vector topLeft() { return position; }
    public Vector topRight() { return new Vector(position.x + size.x, position.y); }
    public Vector bottomLeft() { return new Vector(position.x, position.y + size.y); }
    public Vector bottomRight() { return new Vector(position.x + size.x, position.y + size.y); }
    
    /*
    These methods return integer positions of various sides of the Rectangle.
    */
    public int top() { return position.y; }
    public int left() { return position.x; }
    public int bottom() { return position.y + size.y; }
    public int right() { return position.x + size.x; }   
    
    /**
     * This returns whether the Rectangle contains the specified point.
     * 
     * WILL NOT RETURN TRUE IF THE POINT IS ON THE BORDER
     * @param x The x position of the point to check.
     * @param y The y position of the point to check.
     * @return Whether the Rectangle contains the specified point.
     */
    public boolean contains(int x, int y) {
        return (x > position.x && x < position.x + size.x) && 
               (y > position.y && y < position.y + size.y);
    }
    
    /**
     * This returns whether the Rectangle contains the specified point.
     * @param point The point to check.
     * @return Whether the Rectangle contains the specified point.
     */
    public boolean contains(Vector point) {
        return (point.x > position.x && point.x < position.x + size.x) && 
               (point.y > position.y && point.y < position.y + size.y);
    }
    
    /**
     * Returns whether the Rectangle intersects another Rectangle.
     * @param r The Rectangle to check for intersection.
     * @return Whether the two Rectangles intersect.
     */
    public boolean intersects(Rectangle r) {
        return r.contains(this.topLeft()) || r.contains(this.bottomRight()) || this.contains(r.topRight()) || this.contains(r.bottomLeft());
    }
}