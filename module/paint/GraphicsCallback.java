package module.paint;

/**
 * Provides a mechanism through which Modules can request partial graphical callbacks. This way, a Module can obtain access
 * to part of the screen, and draw only there. This is more efficient for graphical code.
 * @author TheMonsterFromTheDeep
 */
public interface GraphicsCallback {
    void paint(GraphicsHandle handle);
}