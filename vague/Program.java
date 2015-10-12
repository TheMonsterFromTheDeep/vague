package vague;

/**
 * The Program class is simply the entry point for the application.
 * It initializes a new instance of Window and calls its run() method.
 * @author TheMonsterFromTheDeep
 */
public class Program {
    /**
     * The entry point for the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        //Create a new ImageLoader and load images before the application is run.
        ImageLoader.IMG = new ImageLoader();
        
        /*
        In the future, a temporary window will be used that is displayed while all resources are loading,
        if that amount of resources ever exists.
        */
        
        //Creates a new Window object and makes it useable for interaction with the user.
        new Window().run();
    }
}