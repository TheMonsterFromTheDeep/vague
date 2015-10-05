package vague.util;

import java.awt.image.BufferedImage;

/**
 * Stores all the images in the application. This allows all images to have a centralized
 * source and removes duplicate images.
 * @author TheMonsterFromTheDeep
 */
public class ImageData {
    public static ImageData data;
    
    public BufferedImage RESIZE_ARROW_HORIZONTAL;
    public BufferedImage RESIZE_ARROW_VERTICAL;
    public BufferedImage DYN_MODULE_SPLIT;
    
    public ImageData() {
        RESIZE_ARROW_HORIZONTAL = ImageLoader.loadProtected("/resource/img/ui/control/resize_arrow_horizontal.png");
        RESIZE_ARROW_VERTICAL = ImageLoader.loadProtected("/resource/img/ui/control/resize_arrow_vertical.png");
        DYN_MODULE_SPLIT = ImageLoader.loadProtected("/resource/img/ui/control/module_split.png");
    }
}