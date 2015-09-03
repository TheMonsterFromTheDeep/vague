package vague.ui.editor;

import java.awt.Color;

/**
 * Stores various data about the editor. 
 * @author TheMonsterFromTheDeep
 */
public class EditorData {
    //Data is public so can be accessed and modified by editor
    
    public int width; //Width and height of editor window - stores width and height of "panel"
    public int height;
    
    //public Color background = new Color(0xC4C8F5); //Stores background color of editor window
    public Color background = new Color(0xd0d3fb);
    //Further data may be added in the future
    
    public EditorData(int width, int height) {
        this.width = width; //Initialize data values
        this.height = height;
    }
    
    /**
     * Stores new width and height in data structure. Called by Editor when the window is resized.
     * @param width New width of the window.
     * @param height New height of the window.
     */
    public void resize(int width, int height) {
        this.width = width; //Store new data
        this.height = height;
    }
}