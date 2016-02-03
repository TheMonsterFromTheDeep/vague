package vague.editor.shape;

import java.awt.Color;
import module.paint.GraphicsHandle;
import module.util.FloatVector;
import module.util.Vector;

public class Pencil implements Shape {

    public static class Builder implements Shape.Builder {
        FloatVector[] points;
        
        int nextWrite;
        
        public Builder() {
            points = new FloatVector[10];
            nextWrite = 0;
        }
        
        public void addPoint(FloatVector v) {
            if(nextWrite < points.length) {
                points[nextWrite] = v;
            } else {
                FloatVector[] tmp = points;
                points = new FloatVector[points.length + 10];
                System.arraycopy(tmp, 0, points, 0, tmp.length);
                
                points[nextWrite] = v;
            }
            nextWrite++;
        }
        
        public Pencil getLine() {
            if(nextWrite < points.length) {
                FloatVector[] tmp = points;
                points = new FloatVector[nextWrite];
                System.arraycopy(tmp, 0, points, 0, points.length);
            }
            return new Pencil(points);
        }

        @Override
        public void draw(int offx, int offy, GraphicsHandle handle, float scale) {
            //Stupid derp optimization (i + 1)
            //[i'm sorry]
            for(int i = 0; i + 1 < points.length; i++) {
                handle.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
            }
        }
    }
    
    FloatVector[] points;
    
    public Pencil(FloatVector[] points) {
        this.points = points;
    }
    
    @Override
    public void draw(int offx, int offy, GraphicsHandle handle, float scale) {
        
        
        handle.setColor(Color.BLACK);
        if(points.length > 1) {
            //TODO: Use these variables for faster
            int px = (int)(points[0].x * scale), py = (int)(points[0].y * scale), nx = (int)(points[1].x * scale), ny = (int)(points[1].y * scale);
            //Stupid derp optimization (i + 1)
            //[i'm sorry]
            for(int i = 0; i + 1 < points.length; i++) {
                //nx = (int)(points[i + 1].x * scale);
                //ny = (int)(points[i + 1].y * scale);
                handle.drawLine((int)(scale * (points[i].x + offx)), (int)(scale * (points[i].y + offy)), (int)(scale * (points[i + 1].x + offx)), (int)(scale * (points[i + 1].y + offy)));
                //px = nx;
                //py = ny;
            }
        }
    }
}