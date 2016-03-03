package vague.menu;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import module.Module;
import module.paint.GraphicsHandle;
import module.util.Rectangle;
import module.util.Vector;
import vague.input.Controls;
import vague.util.TextDrawer;
import vague.workspace.WorkTool;

/**
 * The SaveFile dialog is used to save files. Right now it only saves image files.
 * 
 * As of now, when a SaveFile dialog is opened, it simply replaces the active module in a window. This will hopefully be
 * changed in the future.
 * @author TheMonsterFromTheDeep
 */
public class SaveFile extends Module {
    static final Color BG_COLOR = new Color(0xc2c2dd);
    
    static final Color BG_NORMAL = new Color(0xcacadd);
    static final Color BG_PRESSED = new Color(0xb0b0dd);
    
    static final int OFFSET_LEFT = 16;
    static final int OFFSET_CANCEL = 80;
    static final int OFFSET_TITLE = 16;
    static final int OFFSET_SAVE = 48;
    static final int OFFSET_BUTTON = 80;
    
    String filePath;
    String prompt;
    
    BufferedImage toSave;
    
    boolean okPress;
    boolean cancelPress;
    
    Rectangle okButton;
    Rectangle cancelButton;
    
    WorkTool parent;
    
    final int filePathOffset;
    
    /**
     * Modifies a string to represent what it should look like by taking out special characters.
     * @param s The string to type.
     * @return The typed string.
     */
    static String type(String s) {
        String ret = "";
        for(int i = 0; i < s.length() - 1; i++) { //TODO: Handle other special characters; optimize
            if(s.charAt(i + 1) == '\b') {
                i += 2;
            }
            else {
                ret += s.charAt(i);
            }
        }
        if(s.charAt(s.length() - 1) != '\b') {
            ret += s.charAt(s.length() - 1);
        }
        return ret;
    }
    
    static final boolean save(BufferedImage image, String path) {
        boolean valid = false;
        if(path.contains(".")) {
            String extension;
            extension = path.substring(path.lastIndexOf(".") + 1);
            String[] allowed = ImageIO.getWriterFileSuffixes();
            for(int i = 0; i < allowed.length; i++) {
                if(allowed[i].equals(extension)) {
                    valid = true;
                    break;
                }
            }
            if(valid) {
                try {
                    ImageIO.write(image, extension, new File(path));
                } 
                catch (IOException ex) {
                    valid = false;
                }
            }
        }
        //TODO: Show error message
        return valid;
    }
    
    public SaveFile(String prompt, BufferedImage toSave, WorkTool parent) {
        filePath = "";
        this.prompt = prompt;
        this.toSave = toSave;
        this.parent = parent;
        
        filePathOffset = (int)TextDrawer.stringWidth("Path: ", 1);
        
        okButton = new Rectangle(OFFSET_LEFT - 2, OFFSET_BUTTON - 2, (int)TextDrawer.stringWidth("OK", 1) + 4, TextDrawer.TEXT_HEIGHT + 4);
        cancelButton = new Rectangle(OFFSET_CANCEL - 2, OFFSET_BUTTON - 2, (int)TextDrawer.stringWidth("Cancel", 1) + 4, TextDrawer.TEXT_HEIGHT + 4);
    }
    
    @Override
    public void keyType(KeyEvent e) {
        filePath += e.getKeyChar();
        filePath = type(filePath);
        repaint();
    }
    
    @Override
    public void mouseMove(Vector pos, Vector dif) {
        okPress = okButton.contains(pos);
        cancelPress = cancelButton.contains(pos);
        repaint();
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(Controls.bank.LMBDown) {
            if(okPress) {
                if(save(toSave, filePath)) {
                    parent.flip();
                }
                else {
                    parent.warn = true;
                    parent.repaint();
                }
            }
            if(cancelPress) {
                parent.flip();
            }
        }
    }
    
    @Override
    public void onUnfocus() {
        okPress = false;
        cancelPress = false;
    }
    
    @Override
    public void paint(GraphicsHandle handle) {
        handle.fill(BG_COLOR);
        
        this.drawText(prompt, 1, OFFSET_LEFT, OFFSET_TITLE, handle);
        
        this.drawText("Path:", 1, OFFSET_LEFT, OFFSET_SAVE, handle);
        int textWidth = (int)TextDrawer.stringWidth(filePath, 1);
        handle.setColor(Color.BLACK);
        handle.drawRect(OFFSET_LEFT + filePathOffset - 2, OFFSET_SAVE - 2, textWidth + 4, TextDrawer.TEXT_HEIGHT + 4);
        this.drawText(filePath, 1, OFFSET_LEFT + filePathOffset, OFFSET_SAVE, handle);
        //TODO: Better text drawing?
        
        //TODO: Centralized color resource base
        handle.setColor(okPress ? BG_PRESSED : BG_NORMAL);
        handle.fillRect(okButton.position.x, okButton.position.y, okButton.size.x, okButton.size.y);
        handle.setColor(Color.BLACK);
        handle.drawRect(okButton.position.x, okButton.position.y, okButton.size.x, okButton.size.y);
        
        this.drawText("OK", 1, OFFSET_LEFT, OFFSET_BUTTON, handle);
        
        handle.setColor(cancelPress ? BG_PRESSED : BG_NORMAL);
        handle.fillRect(cancelButton.position.x, cancelButton.position.y, cancelButton.size.x, cancelButton.size.y);
        handle.setColor(Color.BLACK);
        handle.drawRect(cancelButton.position.x, cancelButton.position.y, cancelButton.size.x, cancelButton.size.y);
        
        this.drawText("Cancel", 1, OFFSET_CANCEL, OFFSET_BUTTON, handle);
    }
}