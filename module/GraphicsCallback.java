package module;

import java.awt.Graphics;

/**
 * A callback for drawing. When a Module requests a Graphics object, the Window will return a graphics object
 * as soon as possible, and will call the requested callback with the Graphics object.
 * @author TheMonsterFromTheDeep
 */
public interface GraphicsCallback {
    void callback(Graphics g);
}