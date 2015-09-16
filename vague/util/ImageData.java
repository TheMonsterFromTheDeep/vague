package vague.util;

import java.awt.image.BufferedImage;

/**
 * Stores all the images in the application. This allows all images to have a centralized
 * source and removes duplicate images.
 * @author TheMonsterFromTheDeep
 */
public class ImageData {
    public static ImageData data = new ImageData();
    
    public BufferedImage resizeArrowHorizontal;
    public BufferedImage resizeArrowVertical;
    
    public ImageData() {
        resizeArrowHorizontal = ImageLoader.loadProtected("/resource/img/ui/control/resize_arrow_horizontal.png");
        resizeArrowVertical = ImageLoader.loadProtected("/resource/img/ui/control/resize_arrow_vertical.png");
    }
}