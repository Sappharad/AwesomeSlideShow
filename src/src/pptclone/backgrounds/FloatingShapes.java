/**
 * FloatingShapes.java
 * @author krattp
 * 
 * A bunch of shapes near the bottom floating away.
 */
package pptclone.backgrounds;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class FloatingShapes implements pptclone.SlideBackground{
    private float height = 0; //Current location of the shapes.
    
    public void drawFrame(GL2 gl) {
        GLU glu = new GLU();
        
        // Move the "drawing cursor" around
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        
        // Draw a gradient for the background
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.329f/2f, 0.427f/2f, 0.556f/2f);    //Light blue
        gl.glVertex3f(-5.0f, 2.5f, 0.0f);   // Top left
        gl.glColor3f(0.184f/2f, 0.211f/2f, 0.6f/2f);    // Dark blue
        gl.glVertex3f(-5.0f, -2.5f, 0.0f); // Bottom Left
        gl.glColor3f(0.184f/2f, 0.211f/2f, 0.6f/2f);    // Dark blue
        gl.glVertex3f(5.0f, -2.5f, 0.0f);  // Bottom Right
        gl.glColor3f(0.329f/2f, 0.427f/2f, 0.556f/2f);    // Light blue
        gl.glVertex3f(5.0f, 2.5f, 0.0f);   // Top right
        // Finished Drawing The square
        gl.glEnd();
        
        gl.glRotated(-75.0, 1.0, 0.0, 0.0);
        gl.glTranslated(0, -4.5, 0); //Come closer to the camera
        gl.glColor3f(0.329f/2f, 0.427f/2f, 0.556f/2f);
        GLUquadric qobj = glu.gluNewQuadric();
        
        //Draw circles
        for(int row=0; row<20; row++){
            for(double i=-4.0; i<4.0; i+=0.5){
                float color = row/20f+height/20f;
                
                gl.glPushMatrix();
                gl.glTranslated(i, height+(row*0.5), 0);
                gl.glColor3f((1-color*(1-0.329f))/2f, (1-color*(1-0.427f))/2f, (1-color*(1-0.556f))/2f);
                glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);
                glu.gluQuadricNormals(qobj, GLU.GLU_FLAT);
                glu.gluDisk(qobj, 0.1, 0.20, 20, 4);
                gl.glPopMatrix();
            }
        }
        
        gl.glPopMatrix();
        
        height+=0.025;
        if(height > 1.0)
            height=0;
    }

    /** Get the name of this background. **/
    public String getName() {
        return "floating";
    }
}
