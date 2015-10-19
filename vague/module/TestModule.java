package vague.module;

import java.awt.Color;
import vague.util.Vector;

/**
 * A Module object used to test various features of Containers and other Modules that rely on 
 * child / dummy Modules.
 * @author TheMonsterFromTheDeep
 */
public class TestModule extends Module {
    static Color[] colors = new Color[] { new Color(0xff0000), new Color(0x00ff00), new Color(0x0000ff) };
    
    private int id;
    
    public TestModule(int width, int height, int type) {
        super(width, height);
        bgColor = colors[type-1];
        id = type-1;
    }
    
    @Override
    public void onResize(Vector v) {
    }
    
    @Override
    public void onFocus() {
        this.bgColor = Color.ORANGE;
        redraw();
        drawParent();
    }
    
    @Override
    public void onUnfocus() {
        this.bgColor = colors[id];
        redraw();
        drawParent();
    }
    
    @Override
    public void redraw() {
        graphics.setColor(bgColor);
        graphics.fillRect(0,0,width(),height());
    }
}