/**
 * glPanel.java
 * author: Paul Kratt
 *
 * This panel is where all of the rendering for my PowerPoint Clone is displayed.
 */
package pptclone;

import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.*;
import java.util.ArrayList;
import com.jogamp.opengl.glu.GLU;
import pptclone.transitions.*;

public class glPanel extends GLCanvas implements GLEventListener {
    private ArrayList<Slide> slides = new ArrayList();
    private int mySlide=0; //What side am I on right now?
    public static int screenwidth=800,screenheight=600; //Set some defaults, just in case
    private Slide endOfShow = new Slide();
    private long nexttime=0;
    private boolean inTransition=false; //Currently in a transition
    
    /** Add a slide to the presentation
     * @param theslide The slide to add
     */
    public void addSlide(Slide theslide){
        slides.add(theslide);
    }
    
    /** Initialize this GL drawing area **/
    public void init(GLAutoDrawable drawable) {
        GL2 gl = (GL2)drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        
        screenheight = drawable.getSurfaceHeight();
        screenwidth = drawable.getSurfaceWidth(); //Defaults

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL2.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        
        //Create our end of show slide
        endOfShow.addItem(new TextItem("End of slide show.",0.1,0.1,"Arial",8.0f,1.0f,1.0f,1.0f,1.0f,0));
        endOfShow.setSlideBG(new pptclone.backgrounds.NullBackground());
        
        nexttime = System.currentTimeMillis()+40; //Set the time for the next frame to complete at 25fps.
    }

    /** Window has been resized. Update the viewing area. Called automatically. **/
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = (GL2)drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) // avoid a divide by zero error!
        {
            height = 1;
        }
        glPanel.screenwidth = width;
        glPanel.screenheight = height;
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 2000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        for(Slide sld : slides)
            sld.notifyResize();
        endOfShow.notifyResize();
    }

    /** Render the next frame of the presentation video display
     * @param drawable This drawing area
     **/
    public void display(GLAutoDrawable drawable) {
        GL2 gl = (GL2)drawable.getGL();

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();
        
        if(inTransition && slides.get(mySlide).getTransition().isDone()){
            inTransition = false;
            mySlide++;
            if(mySlide > slides.size())
                mySlide = slides.size(); //Despite how this looks, we won't go out of bounds.
        }
        if(inTransition)
            slides.get(mySlide).getTransition().drawFrame(gl);
        else{
            //Draw the background
            if(mySlide < slides.size())
                slides.get(mySlide).getSlideBG().drawFrame(gl);
            else
                endOfShow.getSlideBG().drawFrame(gl);

            //Draw the foreground
            if(mySlide < slides.size())
                slides.get(mySlide).drawSlide(gl);
            else
                endOfShow.drawSlide(gl);
        }
        
        // Flush all drawing operations to the graphics card
        gl.glFlush();
        
        //The code below waits a bit, so the framerate is close to 25fps.
        nexttime -= System.currentTimeMillis();
        try{
            if(nexttime>0)
                Thread.sleep(nexttime);
            }
        catch(Exception e){}
        nexttime = System.currentTimeMillis()+40; //Set the next target time for 25fps.
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    /** Get the current slide number
     * @return The index of this slide in the slideshow.
     */
    public int getCurrentSlide() {
        return mySlide;
    }

    /** Set the current slide
     * @param mySlide The index of the slide to display
     */
    public void setCurrentSlide(int mySlide) {
        this.mySlide = mySlide;
    }
    
    /** Advance the slideshow. This could trigger a transition, or go to the next slide. */
    public void advanceSlide(){
        
        if(mySlide < slides.size()){
            Slide sldadv = slides.get(mySlide);
            if(sldadv.getCurrentLayer() < sldadv.getMaxLayers())
                sldadv.setCurrentLayer(sldadv.getCurrentLayer()+1); //Advance the layer
            else
                inTransition = true; //Advance the slide
        }
    }
    
    /** Feed an ArrayList of transitions into this class. Not sure why I decided to do it here instead of in the loader...
     * @param list The list of slide transitions. Should match number of slides.
     */
    public void feedTransitions(ArrayList<String> list){
        if(list.size() != slides.size()){
            System.out.println("Transition count mismatch. No transitions will be used");
        }
        else{
            for(int i=0; i<slides.size();i++){
                String tran = list.get(i);
                Slide cur = slides.get(i);
                Slide next;
                if(i<slides.size()-1)
                    next = slides.get(i+1);
                else
                    next = endOfShow;
                
                if(tran.equalsIgnoreCase("none")){
                    NullTransition ntrans = new NullTransition(cur,next);
                    cur.setTransition(ntrans);
                }
                else if(tran.equalsIgnoreCase("genie")){
                    GenieTransition ntrans = new GenieTransition(cur,next);
                    cur.setTransition(ntrans);
                }
                else if(tran.equalsIgnoreCase("rotate")){
                    RotateTransition ntrans = new RotateTransition(cur,next);
                    cur.setTransition(ntrans);
                }
                else{
                    NullTransition ntrans = new NullTransition(cur,next);
                    cur.setTransition(ntrans);
                }
            }
        }
    }

    public void dispose(GLAutoDrawable glad) {
        glad.disposeGLEventListener(this, false);
    }
}

