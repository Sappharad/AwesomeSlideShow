/**
 * TextItem.java
 * @author Paul Kratt
 */
package pptclone;

import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import javax.media.opengl.GL;


public class TextItem implements DrawableItem{
    private double xloc=0.5,yloc=0.5; //Locations are from 0 to 1. Allows for resolution independence.
    private String fontname="Times New Roman",text="Hello World TESTING"; //Preferred font, and the string
    private float fontsize=5; //Percentage of the height
    private TextRenderer txtrender; //We should keep the same one, because that's probably better.
    private float colr=1.0f,colg=1.0f,colb=1.0f,cola=1.0f; //Font color
    private int heightcache, widthcache; //Cache the width and height we were initialized at.
    private int layer=0; //What layer am I on?
    private float visibility=1.0f; //How visible?
    
    public TextItem(){
        //Constructor
        txtrender = new TextRenderer(new Font(fontname, Font.PLAIN, (int)(glPanel.screenheight/100.0*fontsize)));
        layer = 0;
        visibility = 1.0f;
    }
    
    /** Create a new text item with all of the parameters.
     * @param xloc X position
     * @param yloc Y position
     * @param font Preferred font
     * @param fontsize Font size (percentage of screen height)
     * @param r Redness
     * @param g Greeness
     * @param b Blueness
     * @param a Alpha value
     */
    public TextItem(String text, double xloc, double yloc, String font, float fontsize, float r, float g, float b, float a, int layer){
        this.xloc = xloc;
        this.yloc = yloc;
        this.fontname = font;
        this.fontsize = fontsize;
        this.colr = r;
        this.colg = g;
        this.colb = b;
        this.cola = a;
        this.text = text;
        this.layer = layer;
        heightcache = glPanel.screenheight; //The cache prevents appearence changes if we need to resize temporarily for transition rendering.
        widthcache = glPanel.screenwidth;
        txtrender = new TextRenderer(new Font(fontname, Font.PLAIN, (int)(heightcache/100.0*fontsize)));
    }
    
    /** Draw this text string
     * @param gl The GL context to draw into.
     */
    public void drawItem(GL gl) {
        txtrender.beginRendering(widthcache,heightcache);
        txtrender.setColor(colr, colg, colb, cola*visibility);
        txtrender.draw(text, (int)(widthcache*xloc), (int)(heightcache*(1-fontsize/100.0-yloc)));
        txtrender.endRendering();
    }
    
    /** Recreate the text renderer when the window is resized to prevent crashing. */
    public void doResize(){
        widthcache = glPanel.screenwidth;
        heightcache = glPanel.screenheight;
        txtrender = new TextRenderer(new Font(fontname, Font.PLAIN, (int)(heightcache/100.0*fontsize)));
        //Prevents crashing.
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