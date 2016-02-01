package vague.editor.tool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import module.util.Vector;
import vague.editor.Context;
import vague.editor.shape.Pencil;

/**
 * The pencil tool draws curves using hundreds of nodes.
 * @author TheMonsterFromTheDeep
 */
public class PencilTool implements Tool {

    boolean drawing = false;
    
    Pencil.Builder builder;
    
    public PencilTool() {
        builder = new Pencil.Builder();
    }
    
    @Override
    public boolean mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            drawing = true;
            builder = new Pencil.Builder();
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
        if(drawing) {
            builder.addPoint(new Vector(pos));
        }
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