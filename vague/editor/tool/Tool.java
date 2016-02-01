package vague.editor.tool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import module.util.FloatVector;

/**
 * A tool intercepts events from the Editor and chooses whether to make a change to the current edited image
 * based on those events.
 * 
 * This is why a Tool must override every event. It then returns a status variable representing whether it
 * consumed the event action.
 * @author TheMonsterFromTheDeep
 */
public interface Tool {
    boolean mouseDown(MouseEvent e);
    boolean mouseUp(MouseEvent e);
    boolean mouseScroll(MouseWheelEvent e);
    
    boolean mouseMove(FloatVector pos, FloatVector dif);
    
    boolean keyUp();
    boolean keyDown();
}