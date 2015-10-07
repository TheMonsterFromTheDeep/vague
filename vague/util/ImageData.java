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
    public BufferedImage SPLIT_ARROW_BR;
    
    public ImageData() {
        RESIZE_ARROW_HORIZONTAL = ImageLoader.loadProtected("/resource/img/ui/control/resize_arrow_horizontal.png");
        RESIZE_ARROW_VERTICAL = ImageLoader.loadProtected("/resource/img/ui/control/resize_arrow_vertical.png");
        DYN_MODULE_SPLIT = ImageLoader.loadProtected("/resource/img/ui/control/module_split.png");
        SPLIT_ARROW_BR = ImageLoader.loadProtected("/resource/img/ui/control/split_arrow_bottom.png");
    }
}