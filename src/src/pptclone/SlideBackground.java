/*
 * SlideBackground.java
 * An interface for slide backgrounds
 * 
 * @author Paul Kratt
 */

package pptclone;

import com.jogamp.opengl.GL2;

public interface SlideBackground {
    void drawFrame(GL2 gl); //Draw the next frame of this background
    String getName(); //Get the name of this background
}
