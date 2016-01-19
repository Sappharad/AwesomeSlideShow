/*
 * Transition.java
 * An interface for transitions between slides
 * 
 * @author Paul Kratt
 */

package pptclone;

import com.jogamp.opengl.GL2;

public abstract class Transition {
    protected Slide start,end; //Starting and ending slide
    
    public Transition(Slide ts, Slide te){
        start = ts;
        end = te;
    }
    
    public abstract void drawFrame(GL2 gl); //Draw the next frame of this background
    public abstract String getName(); //Get the name of this background
    public abstract boolean isDone(); //Is the transition done?
}
