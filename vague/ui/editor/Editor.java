package vague.ui.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import vague.ui.editor.image.Renderer;
import vague.ui.tabmenu.TabMenu;
import vague.module.Module;
import vague.ui.Window;
import vague.ui.window.MouseTracker;

public class Editor extends Module {    
    private Renderer renderer; //Stores the end-user image renderer
    private MouseTracker mtracker; //Mouse tracker - used for panning image
    
    public Color background;
    
    boolean pan; //True if panning
    
    public Editor(int width, int height) {   
        super(width, height);
        this.background = Window.DEF_BG_COLOR;
        
        renderer = new Renderer();
        renderer.render();
    }
    
    
    @Override
    public void tick(int mousex, int mousey) {
        if(pan) {
            renderer.pan(mousex - mtracker.x, mousey - mtracker.y); //Pan the renderer
            mtracker.shift(mousex, mousey); //Shift the mousetracker
            drawSelf();
        }       
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
            mtracker = new MouseTracker(e.getX(),e.getY());
            pan = true;
            drawSelf();
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
            //renderer.pan(e.getX() - mtracker.x, e.getY() - mtracker.y);
            //drawSelf();
            pan = false;
        }
    }
    
    @Override
    public void mouseScroll(MouseWheelEvent e) {
        if(e.isControlDown()) { //If control key is down, zoom in/out
            renderer.scale(-1 * e.getWheelRotation()); //Scale renderer inverse of wheelrotation (up wheel rotation is negative, but up wheel rotation should be positive scale -> bigger -> zoom in)
            renderer.render(); //Re-render the scaled image
            drawSelf();
        }
    }

    @Override
    public void keyDown(KeyEvent e) {
        if(e.isControlDown()) {
            if(e.getKeyCode() == KeyEvent.VK_EQUALS) {
                renderer.scale(1);
                renderer.render(); //Re-render image to reflect changes
                drawSelf();
            }
            if(e.getKeyCode() == KeyEvent.VK_MINUS) {
                renderer.scale(-1);
                renderer.render();
                drawSelf();
            }
        }
    }

    @Override
    public void keyUp(KeyEvent e) {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, width, height);
               
        g.drawImage(renderer.lastRender,
                ((width - renderer.lastRender.getWidth()) / 2) + renderer.posx,
                ((height - renderer.lastRender.getHeight()) / 2) + renderer.posy,
                null);
    }
}