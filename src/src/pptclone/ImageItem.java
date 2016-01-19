/**
 * ImageItem.java
 * @author Paul Kratt
 */
package pptclone;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class ImageItem implements DrawableItem{
    private double xloc=0.5,yloc=0.5; //Locations are from 0 to 1. Allows for resolution independence.
    private double width=0.2,height=0.2; //Width and height of the image
    private String path="";
    private int layer=0; //What layer am I on?
    private float visibility=1.0f; //How visible?
    private TextureLoader.Texture texture = null;
    private int texid = -1; //Texture ID
    
    public ImageItem(){
        //Constructor
        layer = 0;
        visibility = 1.0f;
        
        width = ((xloc+width)*4.0)-2.0;
        height = (((yloc+height)*4.0)-2.0)*-1.0;        
        xloc = (xloc*4.0)-2.0;
        yloc = ((yloc*4.0)-2.0)*-1.0;
        
        try {
            texture = TextureLoader.readTexture(path);
        } catch (Exception ex) {
            System.err.println("Unable to read texture.");
        }
    }
    
    /** Create a new image item with all of the parameters.
     * @param xloc X position
     * @param yloc Y position
     * @param font Preferred font
     * @param fontsize Font size (percentage of screen height)
     * @param r Redness
     * @param g Greeness
     * @param b Blueness
     * @param a Alpha value
     */
    public ImageItem(double xloc, double yloc, double width, double height, String path, int layer){
        this.width = ((xloc+width)*4.0)-2.0;
        this.height = (((yloc+height)*4.0)-2.0)*-1.0;        
        this.xloc = (xloc*4.0)-2.0;
        this.yloc = ((yloc*4.0)-2.0)*-1.0;
        this.layer = layer;
        this.path = path;
        
        try {
            texture = TextureLoader.readTexture(path);
        } catch (Exception ex) {
            System.err.println("Unable to read texture.");
        }
    }
    
    /** Draw this text string
     * @param gl The GL context to draw into.
     */
    public void drawItem(GL2 gl) {
        //Draw textured quadrilateral
        GLU glu = new GLU();
              
        gl.glEnable(gl.GL_TEXTURE_2D); //Enable textures
        if(texid<0)
            texid = (texture != null)? texture.toGL(gl, glu, true): -1;
        else
            gl.glBindTexture(gl.GL_TEXTURE_2D, texid); //Bind to the texture we copied the framebuffer into
        
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3d(xloc, height, -5.0); // Top Left Of The Texture and Quad
            
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3d(width, height, -5.0);  // Bottom Left Of The Texture and Quad
            
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3d(width, yloc, -5.0);   // Bottom Right Of The Texture and Quad
            
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3d(xloc, yloc, -5.0);  // Top Right Of The Texture and Quad
        }
        gl.glEnd();
        gl.glDisable(gl.GL_TEXTURE_2D); //Enable textures
    }
    
    /** Recreate the text renderer when the window is resized to prevent crashing. */
    public void doResize(){
        //We're fine
    }
    
    public void setLayer(int depth){
        layer = depth;
    }
    
    public int getLayer(){
        return layer;
    }
    
    public void setVisibility(float percent){
        visibility = percent;
    }
    
    public float getVisibility(){
        return visibility;
    }
}