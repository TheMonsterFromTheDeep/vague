package vague.module;

/**
 * The Container class is a basic Module which contains other Modules.
 * 
 * In the style of a highly controllable UI, most Modules will be in Containers
 * which are controllable and splittable by the user.
 * @author TheMonsterFromTheDeep
 */
public class Container {
    Module[] children; //Stores the child Modules of the container
    Module activeChild; //Stores a reference to the active child
    int activeIndex; //Stores the index of the active child
    
    public Container(Module[] children) {
        if(children.length < 1) {
            this.children = new Module[1];
            this.children[0] = new Module(); //Create a dummy module to be the child
        }
        else {
            this.children = children;
        }
        setActiveChild(0); //Set an active child so that nothing weird happens
    }
    
    protected final void setActiveChild(int index) {
        activeIndex = index;
        activeChild = children[index];
    }
}
