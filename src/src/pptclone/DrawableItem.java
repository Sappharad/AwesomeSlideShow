/*
 * DrawableItem.java
 * 
 * @author Paul Kratt
 */

package pptclone;

import javax.media.opengl.GL;

public interface DrawableItem {
    public void drawItem(GL gl); //Draw this item
    public void doResize(); //Called automatically when the show is resized.
    public void setLayer(int depth); //Set the depth layer of this item
    public int getLayer(); //Get the index of the layer this item is on
    public void setVisibility(float percent); //How visible is this item?
    public float getVisibility(); //Get this item's visibility.
}
