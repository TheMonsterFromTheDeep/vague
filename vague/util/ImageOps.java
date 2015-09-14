package vague.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Performs multiple operations on images.
 * @author TheMonsterFromTheDeep
 */
public class ImageOps {
    /**
     * Scales images for use in end-user images.
     * @param b BufferedImage to scale
     * @param multiplier Multiplier to scale by
     * @return The scaled BufferedImage
     */
    public static BufferedImage renderScale(BufferedImage b, int multiplier) {
        double scalex = (double)multiplier;
        double scaley = (double)multiplier;
        
        AffineTransform at = AffineTransform.getScaleInstance(scalex, scaley);
        AffineTransformOp op = new AffineTransformOp(at, null);
        
        return op.filter(b, null);
    }
}