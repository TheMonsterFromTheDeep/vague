package vague.editor.tool;

import java.awt.Color;
import vague.input.Controls;
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
    public void onDown(Vector position) {
        if(Controls.bank.status(Controls.PENCIL_DRAW_LINE)) {
            filter.blendLine(filter.cursor.x, filter.cursor.y, position.x, position.y, (Controls.bank.status(Controls.PENCIL_INVERT_COLORS)) ? filter.background : filter.foreground);
        }
        filter.cursor = position;
    }

    @Override
    public void modify(Vector position) {    
        Color drawColor = (Controls.bank.status(Controls.PENCIL_INVERT_COLORS)) ? filter.background : filter.foreground;
        
        filter.blendLine(filter.cursor.x, filter.cursor.y, position.x, position.y, drawColor);
        //Update the cursor
        filter.cursor = position;
    }
    
}
