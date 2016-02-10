package vague;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * This is a simple JFrame which is shown while the application is loading resources.
 * @author TheMonsterFromTheDeep
 */
public class WaitPopup extends JFrame {
    private JLabel label;
    
    public WaitPopup() {
        this.setTitle("Just a second..."); //Set the title and loading message
        label = new JLabel("Loading...");
        
        this.setPreferredSize(new Dimension(300,100)); //Do styling of the frame
        this.setResizable(false);
        this.setType(Type.UTILITY);
        
        this.add(label); //Add the label to the frame   
        
        this.pack(); //Pack the frame
        
        this.setLocationRelativeTo(null); //Center the window
        this.setVisible(true);
    }
    
    /**
     * Updates the loading message such that it displays "Loading [itemName]..."
     * @param itemName The name of the item to display.
     */
    public void updateLoadingMessage(String itemName) {
        label.setText("Loading " + itemName + "...");
    }
    
    public void close() {
        this.setVisible(false);
    }
}