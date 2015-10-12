package vague.module;

import vague.util.Vector;

/**
 * The Module class defines an object which does graphical rendering and processes user input.
 * 
 * Module classes can form an inherited parent-child structure where parent, container modules
 * render their child modules and pass user events down to the child modules.
 * @author TheMonsterFromTheDeep
 */
public class Module {
    /**
     * Position and size are private so that they are not modified outside the class.
     * 
     * This way resizes and positions can be handled with all the relevant changes to rendering
     * logic.
     */
    private final Vector position; //Stores the position of the Module.
    private final Vector size; //Stores the size of the module.
    
    public Module() {
        /**
         * Default position and size of the Module.
         * The default position is 0, 0 and the default size is also 0, 0.
         * 
         * With the default size, it is impossible to render anything.
         */
        position = new Vector(0,0);
        size = new Vector(0,0);
    }
}