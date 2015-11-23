package vague.editor.tool;

import vague.editor.image.Canvas;
import vague.util.Vector;

/**
 * Represents a tool that performs some operation on the image, such as modifying its look or changing what other
 * tools might do.
 * @author TheMonsterFromTheDeep
 */
public abstract class Tool {
    protected EditFilter filter;
    
    public Tool(EditFilter filter) {
        this.filter = filter;
    }
    
    //Called when the mouse is down on the Editor - the Tool should update the EditFilter
    public abstract void modify(Vector position);
}
