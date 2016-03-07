package vague.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import module.Module;
import module.paint.GraphicsHandle;

public class RendererTest extends Module {
    public static RendererTest create() {
        return new RendererTest();
    }
    
    @Override
    public void paint(GraphicsHandle g) {
        //Renderer r = new Renderer(200, 200);
        BufferedImage buffer = Renderer.drawLine(80, 20, 20, 80, 1, Color.BLACK);
        g.drawImage(buffer, 0, 0);
    }
}