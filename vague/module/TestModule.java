package vague.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import vague.Resources;
import vague.input.Controls;
import vague.util.Cursor;
import vague.util.Vector;

/**
 * A Module object used to test various features of Containers and other Modules that rely on 
 * child / dummy Modules.
 * @author TheMonsterFromTheDeep
 */
public class TestModule extends Module {
    class myCallback implements GraphicsCallback {

        @Override
        public void callback(Graphics g) {
            g.setColor(new Color(0xff0000));
            g.fillRect(0, 0, 20, 20);
        }
        
    }
    
    int rectx;
    
    myCallback callback;
    
    private TestModule(int width, int height) {
        callback = new myCallback();
        beginDraw(0, 0, 0, 0, callback);
    }
    
    public static TestModule create(int width, int height) {
        return new TestModule(width,height);
    }
    
    @Override
    public void mouseMove(Vector mPos, Vector mDif) {
    }

    @Override
    public void onResize(Vector v) {
    }
    
    @Override
    public void onFocus() {
        
    }
    
    @Override
    public void onUnfocus() {
    }
}