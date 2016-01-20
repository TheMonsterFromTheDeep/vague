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
    
    private TestModule(Window w, int width, int height) {
        super(w);
        //beginDraw(0, 0, 0, 0, callback);
    }
    
    public static TestModule create(Window w, int width, int height) {
        return new TestModule(w,width,height);
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