package vague.editor.tool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import module.util.FloatVector;
import module.util.Vector;

/**
 * The NullTool is the default tool (no tool), and as such consumes no events.
 * @author TheMonsterFromTheDeep
 */
public class NullTool implements Tool {

    @Override
    public boolean mouseDown(MouseEvent e) { return false; }

    @Override
    public boolean mouseUp(MouseEvent e) { return false; }

    @Override
    public boolean mouseScroll(MouseWheelEvent e) { return false; }

    @Override 
    public boolean mouseMove(FloatVector pos, FloatVector dif) { return false; }
    
    @Override
    public boolean keyUp() { return false; }

    @Override
    public boolean keyDown() { return false; }
    
}