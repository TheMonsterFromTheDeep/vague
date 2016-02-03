package vague.editor.shape;

import module.paint.GraphicsHandle;

/**
 * A Shape represents something that can be created and added to a Context. A Shape should be able to draw itself.
 * @author TheMonsterFromTheDeep
 */
public interface Shape {
    public static interface Builder {
        void draw(int offx, int offy, GraphicsHandle handle, float scale);
    }
    
    void draw(int offx, int offy, GraphicsHandle handle, float scale);
}