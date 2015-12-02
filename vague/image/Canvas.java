package vague.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * The Canvas object represents a rectangle set of image data. It is completely transparent
 * and provides methods for blending colors.
 * @author TheMonsterFromTheDeep
 */
public class Canvas {
    //Represents the image data of the Canvas.
    private Color[][] data;
    
    //Represents the dimensions of the Canvas.
    private int width;
    private int height;
       
    private static int getBlendedColor(int color1, int color2, double alpha1, double alpha2, double newalpha) {
        return (int)((color1 * alpha1 + (color2 * alpha2 * (1 - alpha1))) / newalpha);
    }
    
    private static double getFloatingColor(int color) {
        return (double)color / 256;
    }
    
    public static Color mix(Color a, Color b) {
        if(a.getAlpha() == 0) {
            return b;
        }
        if(b.getAlpha() == 0) {
            return a;
        }
        
        double alphaA = getFloatingColor(a.getAlpha());
        double alphaB = getFloatingColor(b.getAlpha());
        double alpha = alphaA + (alphaB * (1 - alphaA));
        
        return new Color(
                getBlendedColor(a.getRed(), b.getRed(), alphaA, alphaB, alpha),
                getBlendedColor(a.getGreen(), b.getGreen(), alphaA, alphaB, alpha),
                getBlendedColor(a.getBlue(), b.getBlue(), alphaA, alphaB, alpha),
                (int)(alpha * 256)
        );
    }
    
    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        
        data = new Color[width][height];
        
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                data[x][y] = new Color(0,true);
            }
        }
    }
    
    public void setRGB(int x, int y, int rgb) {
        data[x][y] = new Color(rgb, true);
    }
    
    public int getRGB(int x, int y) {
        return data[x][y].getRGB();
    }
    
    public void set(int x, int y, Color c) {
        data[x][y] = c;
    }
    
    public void blend(int x, int y, Color c) {
        data[x][y] = mix(c,data[x][y]);
    }
    
    public Color get(int x, int y) {
        return data[x][y];
    }
    
    public BufferedImage render() {
        BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                b.setRGB(x, y, data[x][y].getRGB());
            }
        }
        
        return b;
    }
}
