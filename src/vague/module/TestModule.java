package vague.module;

import module.Module;
import java.awt.Color;
import module.Window;
import module.paint.GraphicsHandle;
import module.util.Vector;

/**
 * A Module object used to test various features of Containers and other Modules that rely on 
 * child / dummy Modules.
 * @author TheMonsterFromTheDeep
 */
public class TestModule extends Module {
    
    int rectx;
    
    private TestModule(int width, int height) {
        //beginDraw(0, 0, 0, 0, callback);
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