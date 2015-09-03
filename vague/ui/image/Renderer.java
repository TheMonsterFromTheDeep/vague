package vague.ui.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import vague.util.ImageLoader;

/**
 * Renders the image presented to the user.
 * @author TheMonsterFromTheDeep
 */
public class Renderer {
    private BufferedImage background; //Stores the tiled background used to show transparency
    
    public BufferedImage lastRender; //Stores the last render
    
    int width; //The width and height of the largest layer of the renderer
    int height;
    
    public int posx = 0; //Stores renderer image position - used to make drawing easier
    public int posy = 0;
    
    public int scale = 1; //Stores scale of rendered image
    
    public Renderer() {
        width = 100;
        height = 60;
        
        background = ImageLoader.loadProtected("/resource/img/ui/background.png");
        
        lastRender = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
    }
    
    public void scale(int dif) {
        int temp = scale + dif;
        if(temp >= 1 && temp <= 500) {
            scale = temp;
        }
    }
    
    /**
     * Pan the rendered image position.
     * @param x X distance to pan.
     * @param y Y distance to pan.
     */
    public void pan(int x, int y) {
        posx += x;
        posy += y;
    }
    
    /**
     * Renders all the layers of the image as well as the border.
     * @return A BufferedImage containing all the data of the image.
     */
    public void render() {
        BufferedImage bgRender = new BufferedImage((width * scale) + 2, (height * scale) + 2, BufferedImage.TYPE_INT_ARGB); //The BufferedImage to return - width and height have added 2 to make room for border
        Graphics bg = bgRender.createGraphics(); //Create a graphics object used to render things
        
        //Draw tiled background of image
        for(int x = 0; x < (bgRender.getWidth() / 16) + 1; x += 1) {
            for(int y = 0; y < (bgRender.getHeight() / 16) + 1; y += 1) {
                bg.drawImage(background, 1 + (x * 16), 1 + (y * 16), null); //Offset by 1 so not interfere with border
            }
        }
        
        //Draw border
        bg.setColor(Color.BLACK); //Set color of border
        bg.drawRect(0, 0, bgRender.getWidth() - 1, bgRender.getHeight() - 1); //Subtract 1 when drawing border because image pos indexing starts at 0     
        
        BufferedImage layersRender = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = layersRender.createGraphics();
        
        bg.drawImage(layersRender, 1, 1, null); //Draw the layers render on top of the background
        
        lastRender = bgRender; //Set the last render to the rendered image
    }
}