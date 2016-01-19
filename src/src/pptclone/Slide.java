/*
 * Slide.java
 * 
 * @author Paul Kratt
 * 
 * Class representing a presentation slide
 */

package pptclone;

import java.util.ArrayList;
import javax.media.opengl.GL;

public class Slide {
    private ArrayList<DrawableItem> slideitems; //Items in the slide
    private SlideBackground slidebg; //The background
    private Transition posttrans; //Post slide transition
    private int maxLayers,currentLayer; //Number of layers this slide has, current highest layer visible
    private float animationcycle = 0;
    
    /** Construct a new slide **/
    public Slide(){
        //Of course we need a constructor!
        slideitems = new ArrayList();
        slidebg = new pptclone.backgrounds.NullBackground();
        maxLayers = 0;
        currentLayer = 0;
        animationcycle = 1.0f;
    }
    
    /** Add a drawable item to this slide
     * @param itm The item to add to the slide **/
    public void addItem(DrawableItem itm){
        slideitems.add(itm);
    }
    
    /** Draw the current slide
     * @param gl The GL context to draw onto.
     */
    public void drawSlide(GL gl){
        //Draw the background
        //--- Postmortem: Oops! Had the comment here the whole time,
        //--- but I actually do the drawing in the GL panel itself...
        
        if(animationcycle < 1.0)
            animationcycle += 0.1;
        
        //Draw the foreground
        for(DrawableItem itm : slideitems){
            if(itm.getLayer() < currentLayer){
                itm.setVisibility(1.0f);
                itm.drawItem(gl);
            }
            else if(itm.getLayer() == currentLayer){
                itm.setVisibility(animationcycle);
                itm.drawItem(gl);
            }
            //Otherwise, don't draw a thing!
        }
    }
    
    /** Notify all items in this slide that they have been resized */
    public void notifyResize(){
        for(DrawableItem itm : slideitems)
            itm.doResize();
    }

    /** Get the slide background.
     * @return The current slide background.
     */
    public SlideBackground getSlideBG() {
        return slidebg;
    }

    /** Set the slide background
     * @param slidebg The new slide background
     */
    public void setSlideBG(SlideBackground slidebg) {
        this.slidebg = slidebg;
    }

    /** Set the post-slide transition **/
    public Transition getTransition() {
        return posttrans;
    }

    /** Get the post-slide transition **/
    public void setTransition(Transition posttrans) {
        this.posttrans = posttrans;
    }
    
    /** How many layers does this slide have? **/
    public int getMaxLayers(){
        return maxLayers;
    }

    public void setMaxLayers(int maxLayers) {
        this.maxLayers = maxLayers;
    }

    public int getCurrentLayer() {
        return currentLayer;
    }

    public void setCurrentLayer(int currentLayer) {
        this.currentLayer = currentLayer;
        animationcycle = 0.0f;
    }
}
