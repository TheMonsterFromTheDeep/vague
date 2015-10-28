package vague.workspace;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import vague.Resources;
import vague.geom.Rectangle;
import vague.module.Module;
import vague.module.container.Container;
import vague.util.Vector;

/**
 * The Workspace defines a container in which the user can create and move small containers which
 * contain Modules.
 * @author TheMonsterFromTheDeep
 */
public class Workspace extends Container {
    public static Color TOOL_BORDER_COLOR = new Color(0xbbbbdd); //Colors used when drawing the WorkTools
    public static Color TOOL_FILL_COLOR = new Color(0xc3c3dd);
    public static Color BAD_TOOL_BORDER_COLOR = new Color(0xff5858);
    public static Color BAD_TOOL_FILL_COLOR = new Color(0xef7d7d);
    
    public static int MIN_SIZE = 60; //Stores the minimum creatable size of WorkTool
    
    private static final int BG_WIDTH = 800; //Width/height of bgimage
    private static final int BG_HEIGHT = 600;
    
    private Vector toolStart; //Used when the Workspace is creating a tool - toolStart is anchored and toolEnd
    private Vector toolEnd;   //moves to match the mouse
    
    private boolean createTool; //Stores whether the Workspace is currently being used to create a tool
    //NOTE: May be changed in the future to a integer which stores the function that the Container is
    //currently performing.
    
    private BufferedImage workspace; //Stores a secondary buffer of the current workspace
    //such that it does not have to be re-drawn every time new tools are created
    
    public Workspace(int width, int height, Module[] children) {
        super(width,height,children);
        
        workspace = getValidBuffer(size());
        
        this.bgColor = new Color(0xcfcfcf);
    }
    
    @Override
    public void onResize(Vector newSize) {
        workspace = getValidBuffer(newSize);
    }
    
    @Override
    public void mouseDown(MouseEvent e) {
        if(activeIndex == -1) {
            createTool = true;
            toolStart = new Vector(mousePosition()); //Copy the mousePosition into both the toolStart and toolEnd
            toolEnd = new Vector(toolStart);
        }
        else {
            activeChild.mouseDown(e);
        }
    }
    
    @Override
    public void mouseUp(MouseEvent e) {
        if(activeIndex == -1) {
            if(createTool) {
                createTool = false;
                WorkTool newTool = new WorkTool(toolStart,toolEnd);
                if(newTool.width() > MIN_SIZE && newTool.height() > MIN_SIZE) {
                    boolean create = true;
                    for(Module m : children) {
                        if(newTool.intersects(m)) { create = false; }
                    }
                    if(create) { addChild(newTool); }
                }
                
                children[children.length - 1].draw();
                redraw();
            }
        }
        else {
            activeChild.mouseUp(e);
        }      
    }
    
    private void drawTool() {
        graphics.drawImage(workspace,0,0,null); //Draw the current buffer of the workspace
        
        Vector start = new Vector(), size = new Vector();
        if(toolStart.x < toolEnd.x) {
            start.x = toolStart.x;
            size.x = toolEnd.x - toolStart.x;
        }
        else {
            start.x = toolEnd.x;
            size.x = toolStart.x - toolEnd.x;
        }
        
        if(toolStart.y < toolEnd.y) {
            start.y = toolStart.y;
            size.y = toolEnd.y - toolStart.y;
        }
        else {
            start.y = toolEnd.y;
            size.y = toolStart.y - toolEnd.y;
        }
        Rectangle validator = new Rectangle(start,size);
        boolean valid = (size.x > MIN_SIZE && size.y > MIN_SIZE);
        for(Module m : children) {
            if(m.intersects(validator)) {
                valid = false;
            }
        }
        
        graphics.setColor(valid ? TOOL_FILL_COLOR : BAD_TOOL_FILL_COLOR);
        graphics.fillRect(start.x + 1,start.y + 1,size.x - 2,size.y - 2);
        graphics.setColor(valid ? TOOL_BORDER_COLOR : BAD_TOOL_BORDER_COLOR);
        graphics.drawRect(start.x, start.y, size.x - 1, size.y - 1);
        
        drawParent(); //Draw the parent because it SHOULD NOT be implicitly called by any other method when drawTool() is called
    }
    
    private void updateActiveChild(Vector mousePosition) {
        boolean updated = false; //Stores whether a new active Module was discovered
        int i = 0;
        while(!updated && i < children.length) { //Iterate through child Modules to see if any are active
            if(children[i].containsPoint(mousePosition)) { //If the Module contains the mouse, it should be active
                updated = true; //A Module has been made active
                setActiveChild(i); //Set the active child
                children[i].onFocus();
            }                
            i++;
        }
        if(!updated) { //If the active child hasn't been updated, then it should be cleared
            clearActiveChild();
        }
    }
    
    @Override
    public void mouseMove(Vector pos, Vector dif) {
        if(activeIndex > -1) {
            if(!activeChild.retainFocus) {
                if (!activeChild.containsPoint(pos) || !activeChild.visible()) {
                    activeChild.onUnfocus();
                    updateActiveChild(pos);
                }
            }
            else { retainFocus = true; } //If the child is retaining focus, this needs to retain focus too
            //Pass mouse coordinates onto child module but where the coordinates passed will have an origin
            //at the top left corner of the child module
            activeChild.mouseMove(pos.getDif(activeChild.position()),dif.getDif(activeChild.position())); 
        }
        else if(createTool) {
            toolEnd = pos;

            drawTool();
        }
        else {
            updateActiveChild(pos);
        }
    }
    
    @Override
    public void draw() {
        this.fillBackground();
        for(int x = 0; x < width(); x+= BG_WIDTH) {
            for(int y = 0; y < height(); y+= BG_HEIGHT) {
                graphics.drawImage(Resources.bank.BACKGROUND, x, y, null);
            }
        }
        drawChildren();
        
        workspace.createGraphics().drawImage(buffer, 0, 0, null);
    }
}