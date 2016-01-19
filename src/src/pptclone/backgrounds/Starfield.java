/**
 * Starfield.java
 * @author krattp
 * 
 * A background similar to the starfield screensaver.
 */
package pptclone.backgrounds;

import com.jogamp.opengl.GL2;


public class Starfield implements pptclone.SlideBackground{
    private final int NUM_STARS=50; //Max number of stars on the screen at once
    private float starx[],stary[],starz[]; //Location of the stars
    private float dirx[],diry[]; //Directional velocity. (Z movement is constant, because we're moving, not the stars)
    
    public Starfield(){
        starx = new float[NUM_STARS];
        stary = new float[NUM_STARS];
        starz = new float[NUM_STARS];
        dirx = new float[NUM_STARS];
        diry = new float[NUM_STARS];
        
        //Initialize arrays
        for(int i=0;i<NUM_STARS;i++){
            starx[i]=0;
            stary[i]=0;
            starz[i]=-5f;
            dirx[i]=(float)(Math.random()/5)-0.1f;
            diry[i]=(float)(Math.random()/5)-0.1f;
        }
    }
    
    /** Draw the starfield **/
    public void drawFrame(GL2 gl) {
        // Move the "drawing cursor" around
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -10.0f);
        gl.glColor3f(1f, 1f, 1f);
        
        //Draw gray squares
        gl.glBegin(GL2.GL_QUADS);
        
        for(int i=0;i<NUM_STARS;i++){
            gl.glVertex3f(starx[i], stary[i], starz[i]);   // Top left
            gl.glVertex3f(starx[i], stary[i]-0.05f, starz[i]); // Bottom Left
            gl.glVertex3f(starx[i]+0.05f, stary[i]-0.05f, starz[i]);  // Bottom Right
            gl.glVertex3f(starx[i]+0.05f, stary[i], starz[i]);   // Top right
            starx[i] += dirx[i];
            stary[i] += diry[i];
            starz[i] += 0.25;
            
            if(starx[i]>5.0 || stary[i]>5.0 || starx[i]<-5.0 || stary[i]<-5.0){
                starx[i]=0.0f;
                stary[i]=0.0f;
                starz[i]=-1f;
                dirx[i]=(float)(Math.random()/5)-0.1f;
                diry[i]=(float)(Math.random()/5)-0.1f;
            }
        }
        gl.glEnd();

        gl.glPopMatrix();
    }

    /** Get the name of this background. **/
    public String getName() {
        return "stars";
    }
}
