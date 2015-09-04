package vague.ui.editor.tabs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import vague.util.ImageLoader;

/**
 * Displays a menu of tabs. Used to display open files.
 * @author TheMonsterFromTheDeep
 */
public class TabMenu {
    public Tab[] tabs; //Stores all of the tab data structures
    int width;
    
    public BufferedImage lastRender; //Stores the last render of the tabs
    
    private BufferedImage tabBody;
    private BufferedImage tabEnd;
    
    public TabMenu(int width) {
        tabs = new Tab[] { new Tab("test tab") };
        this.width = width;
        
        tabBody = ImageLoader.loadProtected("/resource/img/ui/module/tabs/tab_body.png");
        tabEnd = ImageLoader.loadProtected("/resource/img/ui/module/tabs/tab_end.png");
    }
    
    public void resize(int width) {
        this.width = width;
        
        render();
    }
    
    public void render() {
        lastRender = new BufferedImage(width, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics g = lastRender.createGraphics();
        
        g.setColor(new Color(0x8f93e7));
        g.fillRect(0, 0, width, 32);
        g.setColor(new Color(0x9fa2fb));
        g.fillRect(0, 30, width, 2);
        
        int renderx = 0; 
        for (Tab tab : tabs) {
            for(int i = 0; i < tab.title.length() - 1; i++) {
                g.drawImage(tabBody, renderx, 0, null);
                renderx += 32;
            }
            g.drawImage(tabEnd, renderx, 0, null);
            renderx += 32;
        }
    }
}