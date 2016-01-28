package vague.editor.shape;

import module.paint.GraphicsHandle;
import module.util.Vector;

public class Line implements Shape {

    public static class Builder implements Shape.Builder {
        Vector[] points;
        
        int nextWrite;
        
        public Builder() {
            points = new Vector[10];
            nextWrite = 0;
        }
        
        public void addPoint(Vector v) {
            if(nextWrite < points.length) {
                points[nextWrite] = v;
            } else {
                Vector[] tmp = points;
                points = new Vector[points.length + 10];
                System.arraycopy(tmp, 0, points, 0, tmp.length);
                
                points[nextWrite] = v;
            }
            nextWrite++;
        }
        
        public Line getLine() {
            if(nextWrite < points.length) {
                Vector[] tmp = points;
                points = new Vector[nextWrite];
                System.arraycopy(tmp, 0, points, 0, points.length);
            }
            return new Line(points);
        }

        @Override
        public void draw(GraphicsHandle handle) {
        }
    }
    
    Vector[] points;
    
    public Line(Vector[] points) {
        this.points = points;
    }
    
    @Override
    public void draw(GraphicsHandle handle) {
        
    }
}