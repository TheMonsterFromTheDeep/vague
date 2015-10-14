package vague.module;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import vague.util.Vector;

/**
 * This is simply a test class for the Module system which allows the user to doodle with the mouse
 * (checks mouse events / position) and that allows the user to clear the screen with space (checks key
 * events).
 * @author TheMonsterFromTheDeep
 */
public class Doodle extends Module {
    //Stores whether the mouse is down and drawing
    boolean drawing = false;
    
    Color drawColor = new Color(0xff0f0f);
    Color fillColor = new Color(0xaaffaa);
    
    Vector last;
    
    public Doodle(int width, int height) {       
        super(width, height);
        graphics.setColor(fillColor);
        graphics.fillRect(0, 0, width(), height());
        this.bgColor = fillColor;
    }
    
    private void doodle(Vector pos) {
        graphics.setColor(drawColor);
        graphics.fillOval(pos.x - 5, pos.y - 5, 10, 10);
        graphics.drawLine(pos.x, pos.y, last.x, last.y);
        drawParent();
        last = pos;
    }
    
    @Override
    public void mouseMove(Vector pos) {
        if(drawing) {
            doodle(pos);
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            drawing = true;
            last = mousePosition();
            doodle(mousePosition());
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            drawing = false;
        }
    }
    
    @Override
    public void keyDown(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            graphics.setColor(fillColor);
            graphics.fillRect(0, 0, width(), height());          
            drawParent();
        }
    }
}
