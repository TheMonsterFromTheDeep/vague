package vague.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import vague.Resources;
import vague.util.Cursor;
import vague.util.Vector;

/**
 * A Module object used to test various features of Containers and other Modules that rely on 
 * child / dummy Modules.
 * @author TheMonsterFromTheDeep
 */
public class TestModule extends Module {
    static Color background = new Color(0xbbbbdd);
    private BufferedImage canvas;
    
    boolean drawing;
    
    int oldx;
    int oldy;
    
    public TestModule(int width, int height) {
        super(width, height);
        bgColor = background;
        canvas = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        
        drawing = false;
    }
    
    private void drawPoint(int x, int y) {
        Graphics g = canvas.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(x - 1, y - 1, 2, 2);
        drawPoints(oldx,oldy,x,y);
        oldx = x;
        oldy = y;
    }
    
    private void drawPoints(int sx, int sy, int ex, int ey) {
        Graphics g = canvas.createGraphics();
        g.setColor(Color.BLACK);
        
        double angle = Math.atan2(ey-sy,ex-sx);
        double changex = Math.cos(angle);
        double changey = Math.sin(angle);
        
        int iterations = (int)Math.ceil(Math.sqrt(Math.abs(ex - sx) * Math.abs(ex - sx) + Math.abs(ey - sy) * Math.abs(ey - sy)));
        
        double x = sx;
        double y = sy;
        for(int i = 0; i < iterations; i++) {
            g.fillRect((int)x - 1, (int)y - 1, 2, 2);
            y += changey;
            x += changex;
        }
    }
    
    @Override
    public void mouseMove(Vector pos, Vector dif) {
        if(drawing) {
            drawPoint(pos.x,pos.y);
            redraw();
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        drawing = true;
        oldx = mousePosition().x;
        oldy = mousePosition().y;
        drawPoint(mousePosition().x,mousePosition().y);
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        drawing = false;
        redraw();
    }
    
    @Override
    public void onResize(Vector v) {
        int cwidth = v.x < canvas.getWidth() ? canvas.getWidth() : v.x;
        int cheight = v.y < canvas.getHeight() ? canvas.getHeight() : v.y;
        BufferedImage old = new BufferedImage(canvas.getWidth(),canvas.getHeight(),BufferedImage.TYPE_INT_ARGB);
        old.createGraphics().drawImage(canvas,0,0,null);
        canvas = new BufferedImage(cwidth,cheight,BufferedImage.TYPE_INT_ARGB);
        canvas.createGraphics().drawImage(old, 0, 0, null);
    }
    
    @Override
    public void onFocus() {
    }
    
    @Override
    public void onUnfocus() {
    }
    
    @Override
    public void draw() {
        graphics.setColor(bgColor);
        graphics.fillRect(0, 0, width(), height());
        graphics.drawImage(canvas, 0, 0, null);
        if(drawing) {
            Cursor s = Resources.bank.TEST_CURSOR;
            graphics.drawImage(s.image,s.getDrawX(mousePosition().x),s.getDrawY(mousePosition().y),null);
        }
    }
}