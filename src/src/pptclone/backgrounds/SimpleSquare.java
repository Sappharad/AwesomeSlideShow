/**
 * SimpleSquare.java
 * @author krattp
 * 
 * A simple square. Basically, a test background.
 */
package pptclone.backgrounds;

import javax.media.opengl.GL;


public class SimpleSquare implements pptclone.SlideBackground{
    private int rotval = 0; //Rotation value
    
    /** Draw the current animation frame **/
    public void drawFrame(GL gl) {
        // Move the "drawing cursor" around
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glScalef(5.0f, 5.0f, 5.0f); //Make it really big
        gl.glRotatef((float)(rotval), 0.0f, 0.0f, 1.0f);
        rotval++;
        if(rotval>=360)
            rotval-=360;

        // Drawing Using Triangles
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.75f, 0.0f, 0.0f);    // Set the current drawing color to red
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);   // Top left
        gl.glColor3f(0.0f, 0.75f, 0.0f);    // Set the current drawing color to green
        gl.glVertex3f(-1.0f, -1.0f, 0.0f); // Bottom Left
        gl.glColor3f(0.0f, 0.0f, 0.75f);    // Set the current drawing color to blue
        gl.glVertex3f(1.0f, -1.0f, 0.0f);  // Bottom Right
        gl.glColor3f(0.0f, 0.0f, 0.0f);    // Set the current drawing color to something I picked randomly.
        gl.glVertex3f(1.0f, 1.0f, 0.0f);   // Top right
        // Finished Drawing The square
        gl.glEnd();

        gl.glPopMatrix();
    }

    /** Get the name of this background. **/
    public String getName() {
        return "square";
    }
}
