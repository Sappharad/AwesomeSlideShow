/**
 * ColorZoom.java
 * @author krattp
 * 
 * A background that zooms in various colors.
 */
package pptclone.backgrounds;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;


public class ColorZoom implements pptclone.SlideBackground{
    private double size = 0,speed=0.02; //Current size of the circle and rate of growth.
    private float colbackr,colbackg,colbackb; //Color of the background
    private float colnewr,colnewg,colnewb; //Color of the foreground
    
    /** Construct this background, set up default random colors. **/
    public ColorZoom(){
        colbackr = (float)(Math.random()/2);
        colbackg = (float)(Math.random()/2);
        colbackb = (float)(Math.random()/2);
        colnewr = (float)(Math.random()/2);
        colnewg = (float)(Math.random()/2);
        colnewb = (float)(Math.random()/2);
    }
    
    /** Draw this slide **/
    public void drawFrame(GL2 gl) {
        GLU glu = new GLU(); //I need some glue
        
        // Move the "drawing cursor" back
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        
        // Draw the background
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(colbackr/2f, colbackg/2f, colbackb/2f);    // Set the current drawing color
        gl.glVertex3f(-5.0f, 5.0f, 0.0f);   // Top left
        gl.glColor3f(colbackr, colbackg, colbackb);    // Set the current drawing color
        gl.glVertex3f(-5.0f, -5.0f, 0.0f); // Bottom Left
        gl.glColor3f(colbackr, colbackg, colbackb);    // Set the current drawing color
        gl.glVertex3f(5.0f, -5.0f, 0.0f);  // Bottom Right
        gl.glColor3f(colbackr/2f, colbackg/2f, colbackb/2f);    // Set the current drawing color
        gl.glVertex3f(5.0f, 5.0f, 0.0f);   // Top right
        // Finished Drawing The square
        gl.glEnd();
        
        GLUquadric qobj = glu.gluNewQuadric();
        
        gl.glColor3f(colnewr, colnewg, colnewb);    // Set the current drawing color
        glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);
        glu.gluQuadricNormals(qobj, GLU.GLU_FLAT);
        if(size>0)
            glu.gluDisk(qobj, 0.0, size, 35, 4);
        
        gl.glPopMatrix();
        
        size+=speed;
        if(size>5.0){
            speed=-0.08;
            colbackr = colnewr;
            colbackg = colnewg;
            colbackb = colnewb;
        }
        if(size<0){
            colnewr = (float)(Math.random()/2);
            colnewg = (float)(Math.random()/2);
            colnewb = (float)(Math.random()/2);
            size=0;
            speed=0.02;
        }
    }

    /** Get the name of this background. **/
    public String getName() {
        return "zoom";
    }
}
