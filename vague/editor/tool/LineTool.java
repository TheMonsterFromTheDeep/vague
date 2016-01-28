package vague.editor.tool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import module.util.Vector;
import vague.editor.Context;
import vague.editor.shape.Line;

/**
 * Draws lines.
 * @author TheMonsterFromTheDeep
 */
public class LineTool implements Tool {

    boolean drawing = false;
    
    Line.Builder builder;
    
    @Override
    public boolean mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            drawing = true;
            builder = new Line.Builder();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseUp(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            drawing = false;
            Context.getContext().addShape(builder.getLine());
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScroll(MouseWheelEvent e) {
        return false;
    }

    @Override
    public boolean mouseMove(Vector pos, Vector dif) {
        return true;
    }

    @Override
    public boolean keyUp() {
        return false;
    }

    @Override
    public boolean keyDown() {
        return false;
    }
    
}