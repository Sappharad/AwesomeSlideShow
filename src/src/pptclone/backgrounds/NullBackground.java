/**
 * NullBackground.java
 * @author krattp
 * 
 * A plain black background.
 */
package pptclone.backgrounds;

import com.jogamp.opengl.GL2;


public class NullBackground implements pptclone.SlideBackground{

    /** Draw this background **/
    public void drawFrame(GL2 gl) {
        //Draw nothing. It works!
    }

    /** Get the name of this background. **/
    public String getName() {
        return "null";
    }
    
}
