package vague.editor.tool;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import module.paint.GraphicsHandle;
import module.util.FloatVector;
import module.util.Vector;
import vague.editor.Context;
import vague.editor.Editor;
import vague.editor.settings.*;
import vague.editor.shape.Pencil;
import vague.input.Controls;

/**
 * The pencil tool draws curves using hundreds of nodes.
 * @author TheMonsterFromTheDeep
 */
public class PencilTool implements Tool {

    static final ToolSetting[] toolSettings = { ColorSetting.getColorSetting() };
    
    ColorSetting colorSetting;
    
    boolean drawing = false;
    Pencil.Builder builder;
    
    public PencilTool() {
        colorSetting = ColorSetting.getColorSetting();
        builder = new Pencil.Builder(colorSetting.color);
    }
    
    @Override
    public boolean mouseDown(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            drawing = true;
            builder = new Pencil.Builder(colorSetting.color);
            Context.getContext().activeEditor.beginRetainingFocus();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseUp(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            drawing = false;
            Context.getContext().addShape(builder.getLine());
            Context.getContext().activeEditor.stopRetainingFocus();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScroll(MouseWheelEvent e) {
        return false;
    }

    @Override
    public boolean mouseMove(FloatVector pos, FloatVector dif) {
        if(drawing) {
            builder.addPoint(new FloatVector(pos));
            Editor e = Context.getContext().activeEditor;
            e.drawShapeBuilder(builder);
        }
        return true;
    }

    @Override
    public boolean keyUp() {
        return false;
    }

    @Override
    public boolean keyDown() {
        if(Controls.bank.status(Controls.TEST_COLOR)) {
            colorSetting.color = new Color(0xff0000);
            return true;
        }
        return false;
    }
    
}