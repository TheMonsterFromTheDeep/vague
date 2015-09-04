package vague.ui.editor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import vague.ui.editor.image.Renderer;
import vague.ui.editor.tabs.TabMenu;
import vague.ui.module.Module;
import vague.ui.window.MouseTracker;

public abstract class Editor extends Module {
    private TabMenu tabmenu;
    
    private Renderer renderer; //Stores the end-user image renderer
    private MouseTracker mtracker; //Mouse tracker - used for panning image
    
    public Color background = new Color(0xd0d3fb);
    
    int width;
    int height;
    
    int windowStartX = 0; //Stores coordinates of window space not covered by other dialogs
    int windowStartY = 0;
    int windowEndX;
    int windowEndY;
    int windowWidth;
    int windowHeight;
    
    boolean pan; //True if panning
    
    public Editor(int width, int height) {
        super(width, height);
        
        this.width = width;
        this.height = height;
        
        windowEndX = width - 1;
        windowEndY = height - 1;
        
        windowWidth = windowEndX - windowStartX;
        windowHeight = windowEndY - windowStartY;
        
        renderer = new Renderer();
        renderer.render();
        
        tabmenu = new TabMenu(width);
        tabmenu.render();
    }
    
    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        
        this.resizeRenderingPane(width, height);
        
        windowEndX = width - 1;
        windowEndY = height - 1;
        
        windowWidth = windowEndX - windowStartX;
        windowHeight = windowEndY - windowStartY;
        
        tabmenu.resize(width);
        
        repaint();
    }
    
    @Override
    public void tick(int mousex, int mousey) {
        if(pan) {
            renderer.pan(mousex - mtracker.x, mousey - mtracker.y); //Pan the renderer
            mtracker.shift(mousex, mousey); //Shift the mousetracker
            repaint();
        }       
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
            mtracker = new MouseTracker(e.getX(),e.getY());
            pan = true;
        }
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON2) {
            renderer.pan(e.getX() - mtracker.x, e.getY() - mtracker.y);
            repaint();
            pan = false;
        }
    }
    
    @Override
    public void mouseScroll(MouseWheelEvent e) {
        if(e.isControlDown()) { //If control key is down, zoom in/out
            renderer.scale(-1 * e.getWheelRotation()); //Scale renderer inverse of wheelrotation (up wheel rotation is negative, but up wheel rotation should be positive scale -> bigger -> zoom in)
            renderer.render(); //Re-render the scaled image
            repaint();
        }
    }

    @Override
    public void keyDown(KeyEvent e) {
        if(e.isControlDown()) {
            if(e.getKeyCode() == KeyEvent.VK_EQUALS) {
                renderer.scale(1);
                renderer.render(); //Re-render image to reflect changes
                repaint();
            }
            if(e.getKeyCode() == KeyEvent.VK_MINUS) {
                renderer.scale(-1);
                renderer.render();
                repaint();
            }
        }
    }

    @Override
    public void keyUp(KeyEvent e) {
    }

    @Override
    public void render() {
        graphics.setColor(background);
        graphics.fillRect(0, 0, width, height);
               
        graphics.drawImage(renderer.lastRender,
                ((width - renderer.lastRender.getWidth()) / 2) + renderer.posx,
                ((height - renderer.lastRender.getHeight()) / 2) + renderer.posy,
                null);
        
        graphics.setColor(new Color(0xb3b6fb));
        graphics.drawRect(windowStartX, windowStartY + 32, windowWidth, windowHeight - 32);
        graphics.drawRect(windowStartX + 1, windowStartY + 33, windowWidth - 2, windowHeight - 34);
        //graphics.setColor(new Color(0x8f93e7));
        //graphics.fillRect(windowStartX, windowStartY, windowWidth + 1, windowStartY + 32);
        graphics.drawImage(tabmenu.lastRender, 0, 0, null);
    }
    
    private void repaint() {
        render();
        redraw();
    }
    
    public abstract void redraw(); //Used to tell parent window to redraw
}