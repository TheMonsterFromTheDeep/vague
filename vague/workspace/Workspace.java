package vague.workspace;

import java.awt.Color;
import vague.Resources;
import vague.module.Module;
import vague.module.container.Container;

/**
 * The Workspace defines a container in which the user can create and move small containers which
 * contain Modules.
 * @author TheMonsterFromTheDeep
 */
public class Workspace extends Container {
    private static int BG_WIDTH = 800; //Width/height of bgimage
    private static int BG_HEIGHT = 600;
    
    public Workspace(int width, int height, Module[] children) {
        super(width,height,children);
        
        this.bgColor = new Color(0xcfcfcf);
    }
    
    @Override
    public void draw() {
        this.fillBackground();
        for(int x = 0; x < width(); x+= BG_WIDTH) {
            for(int y = 0; y < height(); y+= BG_HEIGHT) {
                graphics.drawImage(Resources.bank.BACKGROUND, x, y, null);
            }
        }
    }
}