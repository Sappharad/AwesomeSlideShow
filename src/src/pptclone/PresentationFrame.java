/*
 * PresentationFrame.java
 *
 * Created on January 23, 2008, 9:35 AM
 * 
 * @author Paul Kratt
 */
package pptclone;

import com.jogamp.opengl.util.Animator;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class PresentationFrame extends javax.swing.JFrame {
    private final Animator animator; //Animator used for auto-frame updates.
    private boolean isFullscreen = false; //Keep track so we can toggle fullscreen
    
    /** Creates new form PresentationFrame */
    public PresentationFrame() {
        initComponents();
        this.setSize(800, 600);
        
        pnlPresentation.addGLEventListener(pnlPresentation); //Listen to yourself!

        JFileChooser fbox = new JFileChooser();
        if (fbox.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadPresentation(fbox.getSelectedFile());
        }
        
        animator = new Animator(pnlPresentation);
        animator.start();        
    }
    
    /** Load a background plugin. This method is somewhat poor, (because it loads everything for each lookup)
     * but I was able to reuse this code from another project so it's sort've a win in that respect.
     * @param bgtype The background to load
     * @return The slide background
     **/
    private SlideBackground loadBackground(String bgtype){
        SlideBackground retval = null;
        
        File pluginFolder = new File("backgrounds");
        if(!pluginFolder.exists())
            pluginFolder.mkdir();
        File[] plugins = pluginFolder.listFiles();
        for(int i=0; i<plugins.length; i++){
            if(plugins[i].getName().indexOf('$')<0){
                //Good file
                PluginLoader plugloader = new PluginLoader();
                Class thepluginclass = plugloader.findClass(plugins[i].getAbsolutePath());
                try{
                    SlideBackground theplugin = (SlideBackground)thepluginclass.newInstance();
                    if(theplugin.getName().equalsIgnoreCase(bgtype)){
                        retval = theplugin;
                    }
                } catch(Exception e){}
            }
        }
        if(retval == null)
            retval = new pptclone.backgrounds.NullBackground();
        return retval;
    }
    

    /** Load a presentation
     * @param data The path to the file to load
     **/
    public void loadPresentation(File data) {
        ArrayList<String> transitions = new ArrayList(); //List of transitions to add to slides after the presentation is built
        
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document presdata = docBuilder.parse(data); //Parse presentation data

            presdata.getDocumentElement().normalize(); //This was in the example, seems like a good thing.
            NodeList slidelist = presdata.getElementsByTagName("slide");
            for (int i = 0; i < slidelist.getLength(); i++) {
                //Grab each slide's data.
                Slide nextslide = new Slide();
                int maxslidelayer = 0;
                transitions.add("null");
                NodeList content = slidelist.item(i).getChildNodes();
                org.w3c.dom.NamedNodeMap sldprops = slidelist.item(i).getAttributes();
                
                for(int z=0; z<sldprops.getLength(); z++){
                    String proptype = sldprops.item(z).getNodeName();
                    
                    if(proptype.equalsIgnoreCase("background")){
                        //Loop through them, find the right one.
                        nextslide.setSlideBG(loadBackground(sldprops.item(z).getTextContent()));
                    }
                    else if(proptype.equalsIgnoreCase("transition")){
                        transitions.set(transitions.size()-1, sldprops.item(z).getTextContent());
                    }
                }
                
                for (int j = 0; j < content.getLength(); j++) {
                    if (content.item(j).hasAttributes() && content.item(j).getNodeName().equalsIgnoreCase("text")) {
                        //We want this, it's probably a text node because it contains attributes
                        org.w3c.dom.NamedNodeMap attrs = content.item(j).getAttributes();
                        String text = "null", x = "0.0", y = "0.0", font = "Arial", size = "0.5", r = "1.0", b = "1.0", g = "1.0", a = "1.0"; //Attributes
                        String layer="0";
                        text = content.item(j).getTextContent();
                        for (int k = 0; k < attrs.getLength(); k++) {
                            String attrtype = attrs.item(k).getNodeName();
                            if (attrtype.equalsIgnoreCase("a")) {
                                a = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("b")) {
                                b = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("g")) {
                                g = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("r")) {
                                r = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("x")) {
                                x = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("y")) {
                                y = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("font")) {
                                font = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("size")) {
                                size = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("layer")) {
                                layer = attrs.item(k).getTextContent();
                            }
                            
                        }
                        //Create the text item now, since we have the data
                        try {
                            TextItem myline = new TextItem(text, Double.parseDouble(x), Double.parseDouble(y), font, Float.parseFloat(size), Float.parseFloat(r), Float.parseFloat(g), Float.parseFloat(b), Float.parseFloat(a) , Integer.parseInt(layer));
                            if(Integer.parseInt(layer)>maxslidelayer)
                                maxslidelayer = Integer.parseInt(layer);
                            nextslide.addItem(myline);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Likely a bad number for a parameter.");
                        }

                    }
                    else if (content.item(j).hasAttributes() && content.item(j).getNodeName().equalsIgnoreCase("image")) {
                        //We want this, it's probably an image node because it the image tag
                        org.w3c.dom.NamedNodeMap attrs = content.item(j).getAttributes();
                        String x = "0.0", y = "0.0", width = "0.5", height = "0.5", layer = "0"; //Attributes
                        String path = data.getParent() + File.separator + content.item(j).getTextContent();
                        
                        for (int k = 0; k < attrs.getLength(); k++) {
                            String attrtype = attrs.item(k).getNodeName();
                            if (attrtype.equalsIgnoreCase("x")) {
                                x = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("y")) {
                                y = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("width")) {
                                width = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("height")) {
                                height = attrs.item(k).getTextContent();
                            } else if (attrtype.equalsIgnoreCase("layer")) {
                                layer = attrs.item(k).getTextContent();
                            }
                            
                        }
                        //Create the image item now, since we have the data
                        try {
                            ImageItem myimg = new ImageItem(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(width), Double.parseDouble(height), path, Integer.parseInt(layer));
                            if(Integer.parseInt(layer)>maxslidelayer)
                                maxslidelayer = Integer.parseInt(layer);
                            nextslide.addItem(myimg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Likely a bad number for a parameter.");
                        }

                    }
                }
                pnlPresentation.addSlide(nextslide);
                nextslide.setMaxLayers(maxslidelayer);
                //Add slide here...
            }
        pnlPresentation.feedTransitions(transitions); //Feed the transitions to the class.
        //Done
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File load problem.");
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPresentation = new pptclone.glPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Presentation");

        pnlPresentation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlPresentationMouseClicked(evt);
            }
        });
        pnlPresentation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pnlPresentationKeyPressed(evt);
            }
        });
        getContentPane().add(pnlPresentation, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Advance the current slide or advance to the next. The user clicked **/
    private void pnlPresentationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlPresentationMouseClicked
        pnlPresentation.advanceSlide();
    }//GEN-LAST:event_pnlPresentationMouseClicked

    /** User pressed a key on the keyboard. **/
    private void pnlPresentationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnlPresentationKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_LEFT){
            int temp = pnlPresentation.getCurrentSlide();
            if(temp>0)
                temp--;
            pnlPresentation.setCurrentSlide(temp);
        }
        if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
            pnlPresentation.advanceSlide();
        }
        if(evt.getKeyCode()==KeyEvent.VK_F){
            if(isFullscreen){
                this.dispose();
                this.setUndecorated(false);
                this.setVisible(true);
                isFullscreen=false;
            }
            else{
                try{
                    GraphicsEnvironment gfx = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
                    GraphicsDevice[] devices = gfx.getScreenDevices();
                    this.dispose(); //Can't go undecorated without disposing first. The control needs to be marked undisplayable, which this does.
                    this.setUndecorated(true);
                    this.setVisible(true);
                    devices[devices.length-1].setFullScreenWindow(this); //Go fullscreen on the last screen.
                    isFullscreen = true;
                }
                catch(Exception e){
                    System.out.println("Unable to go fullscreen!");
                }
            }
            
        }
    }//GEN-LAST:event_pnlPresentationKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private pptclone.glPanel pnlPresentation;
    // End of variables declaration//GEN-END:variables
}
