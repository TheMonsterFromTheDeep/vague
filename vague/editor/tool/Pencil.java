package vague.editor.tool;

import java.awt.Color;
import vague.util.Vector;

/**
 *
 * @author TheMonsterFromTheDeep
 */
public class Pencil extends Tool {

    public Pencil(EditFilter filter) {
        super(filter);
    }

    @Override
    public void modify(Vector position) {
        filter.setPixel(position.x, position.y, Color.BLACK);
    }
    
}
