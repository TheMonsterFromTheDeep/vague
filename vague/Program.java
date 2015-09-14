package vague;

import vague.ui.Window;

/**
 * Main entry point for the Vague image editor.
 * Creates a new Editor window and shows it, starting the editor.
 * @author TheMonsterFromTheDeep
 */
public class Program {
    
    //Entry point for the Vague image editor. 
    public static void main(String[] args) {
        //TODO: Implement command line arguments
        new Window().run(); //Initialize a new Editor window and run it.
    }
    
}