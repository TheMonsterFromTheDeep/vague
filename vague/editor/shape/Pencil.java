package vague.editor.shape;

import java.awt.Color;
import module.paint.GraphicsHandle;
import module.util.Vector;

public class Pencil implements Shape {

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
        
        public Pencil getLine() {
            if(nextWrite < points.length) {
                Vector[] tmp = points;
                points = new Vector[nextWrite];
                System.arraycopy(tmp, 0, points, 0, points.length);
            }
            return new Pencil(points);
        }

        @Override
        public void draw(GraphicsHandle handle, float scale) {
            //Stupid derp optimization (i + 1)
            //[i'm sorry]
            for(int i = 0; i + 1 < points.length; i++) {
                handle.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
            }
        }
    }
    
    Vector[] points;
    
    public Pencil(Vector[] points) {
        this.points = points;
    }
    
    @Override
    public void draw(GraphicsHandle handle, float scale) {
        
        
        handle.setColor(Color.BLACK);
        if(points.length > 1) {
            //TODO: Use these variables for faster
            int px = (int)(points[0].x * scale), py = (int)(points[0].y * scale), nx = (int)(points[1].x * scale), ny = (int)(points[1].y * scale);
            //Stupid derp optimization (i + 1)
            //[i'm sorry]
            for(int i = 0; i + 1 < points.length; i++) {
                //nx = (int)(points[i + 1].x * scale);
                //ny = (int)(points[i + 1].y * scale);
                handle.drawLine((int)(scale * points[i].x), (int)(scale * points[i].y), (int)(scale * points[i + 1].x), (int)(scale * points[i + 1].y));
                //px = nx;
                //py = ny;
            }
        }
    }
}