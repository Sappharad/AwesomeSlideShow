/**
 * RotateTransition.java
 * @author Paul Kratt
 * 
 * Rotate the camera towards the next slide
 */
package pptclone.transitions;

import com.sun.opengl.util.BufferUtil;
import java.nio.ByteBuffer;
import javax.media.opengl.GL;
import pptclone.Slide;
import pptclone.Transition;
import pptclone.glPanel;

public class RotateTransition extends Transition {
    private boolean firstcall; //Is this the first time this function is called?
    private ByteBuffer texin,texout; //Texture data storage
    private int[] textures = new int[2]; //Store the texture numbers in an array
    private int frames; //Number of frames this has played for
    
    /** Constructor **/
    public RotateTransition(Slide start, Slide end){
        super(start,end);
        firstcall = true;
        frames = 0;
    }
    
    /** Draw the next frame of this transition **/
    public void drawFrame(GL gl) {
        int lastw=glPanel.screenwidth,lasth=glPanel.screenheight;
        
        if(firstcall){
            firstcall=false;
            
            //Allocate texture.
            gl.glEnable(gl.GL_TEXTURE_2D);
            texin = BufferUtil.newByteBuffer(512 * 512 * 3);  //I'm going to do a 512x512 RGB texture
            texin.limit(texin.capacity()); //Resize the buffer, to save memory.
            texout = BufferUtil.newByteBuffer(512 * 512 * 3);  //I'm going to do a 512x512 RGB texture
            texout.limit(texout.capacity()); //Resize the buffer, to save memory.
            gl.glGenTextures(2, textures, 0); //Generate two textures
            gl.glBindTexture(gl.GL_TEXTURE_2D, textures[0]); //Tell OpenGL I'm working with the texture that was just created
            gl.glTexImage2D(textures[0], 0, gl.GL_RGB, 512, 512, 0, gl.GL_RGB, gl.GL_UNSIGNED_BYTE, texin);
            //Tell OpenGL to use the buffer I allocated above to store the texture
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            //Without these filters, the texture shows up as a grey box for some reason.

            gl.glBindTexture(gl.GL_TEXTURE_2D, textures[1]); //Tell OpenGL I'm working with the texture that was just created
            gl.glTexImage2D(textures[1], 0, gl.GL_RGB, 512, 512, 0, gl.GL_RGB, gl.GL_UNSIGNED_BYTE, texout);
            //Tell OpenGL to use the buffer I allocated above to store the texture
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            //Without these filters, the texture shows up as a grey box for some reason.
        }
        
        gl.glViewport(0, 0, 512, 512); //Change the viewport to be 512x512 (our texture size)
        glPanel.screenwidth=512;
        glPanel.screenheight=512;
        
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT); // Clear all pixels and the depth buffer
        start.getSlideBG().drawFrame(gl);
        start.drawSlide(gl);
        gl.glBindTexture(gl.GL_TEXTURE_2D, textures[0]); //Bind OpenGL to the texture I want to copy onto
        gl.glCopyTexImage2D(gl.GL_TEXTURE_2D, 0 /* LOD */, gl.GL_RGB, 0 /* X */, 0 /*Y*/, 512 /*width*/, 512/*height*/, 0 /*border width*/);
        //Copy the viewport onto a texture.
        
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT); // Clear all pixels and the depth buffer
        end.getSlideBG().drawFrame(gl);
        end.drawSlide(gl);
        gl.glBindTexture(gl.GL_TEXTURE_2D, textures[1]); //Bind OpenGL to the texture I want to copy onto
        gl.glCopyTexImage2D(gl.GL_TEXTURE_2D, 0 /* LOD */, gl.GL_RGB, 0 /* X */, 0 /*Y*/, 512 /*width*/, 512/*height*/, 0 /*border width*/);
        //Copy the viewport onto a texture.
        
        glPanel.screenwidth=lastw;
        glPanel.screenheight=lasth;
        gl.glViewport(0,0, glPanel.screenwidth, glPanel.screenheight); //Set viewport back to normal
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT); // Clear all pixels and the depth buffer

        gl.glEnable(gl.GL_TEXTURE_2D); //Enable textures
        
        //Draw the slides
        gl.glPushMatrix();
        gl.glScaled((double)glPanel.screenwidth/glPanel.screenheight, 1.0, 1.0);
        
        //Draw the ending slide first
        gl.glPushMatrix();
            gl.glRotatef((float)frames-50f, 0f, 1.0f, 0f);
            gl.glBindTexture(gl.GL_TEXTURE_2D, textures[1]); //Bind to the texture we copied the framebuffer into
            gl.glBegin(gl.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-2f, -2f, -5f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(2f, -2f, -5f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(2f, 2f, -5f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-2f, 2f, -5f);
            gl.glEnd();
        gl.glPopMatrix();
        
        //Next draw the starting slide
        gl.glRotatef((float)frames, 0.0f, 1.0f, 0.0f);
        gl.glBindTexture(gl.GL_TEXTURE_2D, textures[0]); //Bind to the texture we copied the framebuffer into
        gl.glBegin(gl.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-2f, -2f, -5f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(2f, -2f, -5f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(2f, 2f, -5f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-2f, 2f, -5f);
        gl.glEnd();
        gl.glPopMatrix();
        
        gl.glBindTexture(gl.GL_TEXTURE_2D, 0); //Unbind the texture
        gl.glDisable(gl.GL_TEXTURE_2D); //Disable textures
        frames++;
    }

    /** Return this slide's name **/
    public String getName() {
        return "rotate";
    }

    /** Is the animtion done? If so, return true then reset the animation so it can play again **/
    public boolean isDone() {
        if(frames>50){
            frames=0;
            return true;
        }
        return false;
    }
    
    

}
