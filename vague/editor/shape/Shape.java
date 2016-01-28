package vague.editor.shape;

import module.paint.GraphicsHandle;

/**
 * A Shape represents something that can be created and added to a Context. A Shape should be able to draw itself.
 * @author TheMonsterFromTheDeep
 */
public interface Shape {
    public static interface Builder {
        void draw(GraphicsHandle handle);
    }
    
    void draw(GraphicsHandle handle);
}