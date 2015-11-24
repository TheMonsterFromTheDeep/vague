package vague.util;

import java.awt.Color;

/**
 * Defines utility methods for color manipulation.
 * @author TheMonsterFromTheDeep
 */
public class Colors {
    public static int getBlendedColor(int color1, int color2, double alpha1, double alpha2, double newalpha) {
        return (int)((color1 * alpha1 + (color2 * alpha2 * (1 - alpha1))) / newalpha);
    }
    
    public static double getFloatingColor(int color) {
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
}
