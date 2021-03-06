/**
 * NullTransition.java
 * @author Paul Kratt
 * 
 * An empty transition. Go directly to the next slide
 */
package pptclone.transitions;

import com.jogamp.opengl.GL2;
import pptclone.Slide;
import pptclone.Transition;

public class NullTransition extends Transition {

    public NullTransition(Slide start, Slide end){
        super(start,end);
    }
    
    public void drawFrame(GL2 gl) {
        //It's null! What am I supposed to do?
    }

    /** Get the name of this transition **/
    public String getName() {
        return "null";
    }

    /** Return true **/
    public boolean isDone() {
        return true;
    }
    
    

}
