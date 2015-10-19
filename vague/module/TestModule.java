package vague.module;

import java.awt.Color;
import vague.util.Vector;

/**
 * A Module object used to test various features of Containers and other Modules that rely on 
 * child / dummy Modules.
 * @author TheMonsterFromTheDeep
 */
public class TestModule extends Module {
    Color COLOR_1 = new Color(0xff0000); //TODO: Replace with array
    Color COLOR_2 = new Color(0x00ff00);
    Color COLOR_3 = new Color(0x0000ff);
    
    public TestModule(int width, int height, int type) {
        super(width, height);
        switch(type) {
            case 1:
                bgColor = COLOR_1;
                break;
            case 2:
                bgColor = COLOR_2;
                break;
            case 3:
                bgColor = COLOR_3;
                break;
        }
    }
    
    @Override
    public void onResize(Vector v) {
    }
    
    @Override
    public void redraw() {
        graphics.setColor(bgColor);
        graphics.fillRect(0,0,width(),height());
    }
}