/*
 * Main.java
 * 
 * @author Paul Kratt
 * 
 * Main - Launches the presentation
 */

package pptclone;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.UIManager;

public class Main {
    
    public static void main(String[] args){
        try{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){}
        
        try{
            GraphicsEnvironment gfx = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] devices = gfx.getScreenDevices();
            
            PresentationFrame test = new PresentationFrame();
            test.dispose(); //Can't go undecorated without disposing first. The control needs to be marked undisplayable, which this does.
            test.setUndecorated(true);
            test.setVisible(true);
            devices[devices.length-1].setFullScreenWindow(test); //Go fullscreen on the last screen.
        }
        catch(Exception e){
            System.out.println("Unable to go fullscreen!");
        }
        
        
    }
}
