package vague.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import vague.ImageLoader;
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
        drawPoint(mousePosition().x,mousePosition().y);
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        drawing = false;
    }
    
    @Override
    public void onResize(Vector v) {
        if(v.x > width() || v.y > height()) {
            int cwidth = v.x < width() ? width() : v.x;
            int cheight = v.y < height() ? height() : v.y;
            BufferedImage old = new BufferedImage(width(),height(),BufferedImage.TYPE_INT_ARGB);
            canvas = new BufferedImage(cwidth,cheight,BufferedImage.TYPE_INT_ARGB);
            canvas.createGraphics().drawImage(old, 0, 0, null);
        }
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
            Cursor s = ImageLoader.IMG.TEST_CURSOR;
            graphics.drawImage(s.image,s.getDrawX(mousePosition().x),s.getDrawY(mousePosition().y),null);
        }
    }
}