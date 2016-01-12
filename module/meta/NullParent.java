package module.meta;

import module.Module;
import module.util.Vector;
import vague.util.Cursor;

/**
 * The NullParent represents a null module parent. It might be good in the future for it to not silently ignore everything a module does.
 */
public final class NullParent implements ModuleParent {

    @Override
    public void drawChild(Module child) { }

    @Override
    public Vector mousePosition() { return Vector.ZERO; }

    @Override
    public int getAbsoluteX() { return 0; }

    @Override
    public int getAbsoluteY() { return 0; }

    @Override
    public void setCursor(Cursor c) { }

    @Override
    public void clearCursor() { }
    
}