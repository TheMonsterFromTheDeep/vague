package vague.workspace.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import vague.Resources;
import module.Module;
import vague.util.TextDrawer;

public class ControlSelector {
    static final Color BG_NORMAL = new Color(0xcacadd);
    static final Color BG_PRESSED = new Color(0xb0b0dd);
    
    static final int INSET_WIDTH = 3;
    static final double TEXT_SCALE = 1.0;
    
    private String text;
    private Module type;
    private boolean press;
    
    public int x;
    public int y;
    
    public ControlSelector(String text, Module type, int x, int y) {
        this.text = text;
        this.type = type;
        
        this.x = x;
        this.y = y;
    }
    
    //Updates whether a button is pressed based on a specific boolean; mostly used for when a menu is unfocused
    public void update(boolean value) {
        press = value;
    }
    
    public void update(int mousex, int mousey) {
        press = (mousex > x) && (mousex < x + INSET_WIDTH + TextDrawer.stringWidth(text, TEXT_SCALE)) &&
                (mousey > y) && (mousey < y + INSET_WIDTH + TextDrawer.textHeight(TEXT_SCALE));
    }
    
    public BufferedImage draw() {
        BufferedImage textRender = Resources.bank.text.draw(text, TEXT_SCALE);
        BufferedImage render = new BufferedImage(textRender.getWidth() + 2  * INSET_WIDTH, textRender.getHeight() + 2 * INSET_WIDTH, BufferedImage.TYPE_INT_ARGB);
        
        Graphics renderer = render.createGraphics();
        renderer.setColor(press ? BG_PRESSED : BG_NORMAL);
        renderer.fillRect(0, 0, render.getWidth() - 1, render.getHeight() - 1);
        
        renderer.setColor(Color.BLACK);
        renderer.drawRect(0, 0, render.getWidth() - 1, render.getHeight() - 1);
        
        renderer.drawImage(textRender, INSET_WIDTH, INSET_WIDTH, null);
        
        return render;
    }
    
    public boolean pressed() {
        return press;
    }
    
    public Module getFill() {
        return type;
    }
}