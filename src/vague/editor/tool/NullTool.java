package vague.editor.tool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import module.util.FloatVector;
import module.util.Vector;
import vague.editor.settings.ToolSettings;

/**
 * The NullTool is the default tool (no tool), and as such consumes no events.
 * @author TheMonsterFromTheDeep
 */
public class NullTool implements Tool {

    static final ToolSettings nullSettings = new ToolSettings();
    
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

    @Override
    public ToolSettings getSettings() { return nullSettings; }
}