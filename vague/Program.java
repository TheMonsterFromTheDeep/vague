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
        Resources.bank = new Resources(); //Initialize the Resources bank so that resources can be loaded       
        WaitPopup p = new WaitPopup(); //Create the popup to be displayed while loading
        
        Resources.bank.loadResources(p); //Load the resources and pass the WaitPopup so messages will be displayed
        
        p.close(); //Close the loading popup
        p.dispose();
        
        //Create a Window so that the user can use the actual application
        new Window().run();
    }
}