package vague.editor.shape;

import java.awt.Color;
import module.paint.GraphicsHandle;
import module.util.FloatVector;
import module.util.Vector;

public class Pencil implements Shape {

    public static class Builder implements Shape.Builder {
        Color color;
        
        FloatVector[] points;
        
        int nextWrite;
        
        public Builder(Color c) {
            points = new FloatVector[10];
            nextWrite = 0;
            
            color = c;
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
            return new Pencil(points, color);
        }

        @Override
        public void draw(int offx, int offy, GraphicsHandle handle, float scale) {
            handle.setColor(color);
            if(nextWrite > 1) {
                //TODO: Use these variables for faster
                int px = (int)((points[0].x + offx) * scale), py = (int)((points[0].y + offy) * scale), nx, ny;
                //Stupid derp optimization (i + 1)
                //[i'm sorry]
                for(int i = 0; i + 1 < nextWrite; i++) {
                    nx = (int)((points[i + 1].x + offx) * scale);
                    ny = (int)((points[i + 1].y + offy) * scale);
                    handle.drawLine(px, py, nx, ny);
                    px = nx;
                    py = ny;
                }
            }
        }
    }
    
    Color color;
    
    FloatVector[] points;
    
    public Pencil(FloatVector[] points, Color c) {
        this.points = points;
        
        color = c;
    }
    
    @Override
    public void draw(int offx, int offy, GraphicsHandle handle, float scale) {
        
        
        handle.setColor(color);
        if(points.length > 1) {
            //TODO: Use these variables for faster
            int px = (int)((points[0].x + offx) * scale), py = (int)((points[0].y + offy) * scale), nx, ny;
                //Stupid derp optimization (i + 1)
                //[i'm sorry]
                for(int i = 0; i + 1 < points.length; i++) {
                    nx = (int)((points[i + 1].x + offx) * scale);
                    ny = (int)((points[i + 1].y + offy) * scale);
                    handle.drawLine(px, py, nx, ny);
                    px = nx;
                    py = ny;
                }
        }
    }
}