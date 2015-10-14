package vague.module;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
    Color fillColor = new Color(0xefefef);
    
    public Doodle(int width, int height) {       
        super(width, height);
        graphics.setColor(fillColor);
        graphics.fillRect(0, 0, width(), height());
        
    }
    
    @Override
    public void mouseMove(Vector pos) {
        if(drawing) {
            graphics.setColor(drawColor);
            graphics.fillRect(pos.x - 5, pos.y - 5, 10, 10);
            drawParent();
        }
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            drawing = true;
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
