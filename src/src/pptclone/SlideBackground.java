/*
 * SlideBackground.java
 * An interface for slide backgrounds
 * 
 * @author Paul Kratt
 */

package pptclone;

import javax.media.opengl.GL;

public interface SlideBackground {
    void drawFrame(GL gl); //Draw the next frame of this background
    String getName(); //Get the name of this background
}
