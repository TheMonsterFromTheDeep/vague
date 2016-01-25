package vague.editor.tool;

import vague.editor.image.EditTarget;
import module.util.Vector;

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
    
    //Called when the tool first becomes "down"
    public void onDown(Vector position) { }
    //Called when the tool is released
    public void onUp(Vector position) { }
}
