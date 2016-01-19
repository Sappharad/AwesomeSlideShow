/**
 * NullBackground.java
 * @author krattp
 * 
 * A plain black background.
 */
package pptclone.backgrounds;

import javax.media.opengl.GL;


public class NullBackground implements pptclone.SlideBackground{

    /** Draw this background **/
    public void drawFrame(GL gl) {
        //Draw nothing. It works!
    }

    /** Get the name of this background. **/
    public String getName() {
        return "null";
    }
    
}
