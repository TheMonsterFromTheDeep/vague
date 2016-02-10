package module.meta;

import module.Module;
import module.util.Vector;
import vague.util.Cursor;

/**
 * Defines an object that can be treated as the parent to a Module. An object that Modules are parentable
 * must implement certain basic operations.
 */
public interface ModuleParent {
    void drawChild(Module child);
    
    Vector mousePosition();
    
    int getAbsoluteX();
    int getAbsoluteY();
    
    void setCursor(Cursor c);
    void clearCursor();
}