/*
 * PluginLoader.java
 *
 * Created on October 31, 2006, 7:13 PM
 *
 * 
 * @author Paul Kratt
 * 
 * @revision 1.1 - Paul Kratt - Feb 5th 2008 - Changed classpath to pptclone.backgrounds
 * Plugin loader originally written by me for an audio player that played obscure formats.
 */

package pptclone;
import java.io.*;

public class PluginLoader extends ClassLoader{
    String plugname; //Name of the plugin class, needed by the classloader
    
    
    /** Creates a new instance of PluginLoader */
    public PluginLoader() {
        plugname = "testplugin"; //Default plugin name
    }
    
    /** Locate and return the class to load */
    @Override
    public Class findClass(String name) {
        Class retval = null;
        byte[] plugdata = loadClassData(name);
        
        try{
            retval = defineClass("pptclone.backgrounds."+plugname,plugdata,0,plugdata.length);
        }catch(Exception e){
            System.out.println("Error loading plugin!");
        }
        
        return retval;
    }
    
    /** Load the data for the class whose name is specified **/
    private byte[] loadClassData(String name) {
        File plugfile = new File(name);
        FileInputStream plugreader;
        byte[] plugdata = new byte[(int)plugfile.length()];
        
        //Populate plugname with the plugin name. We must assume the filename is the same as the plugin class name
        plugname = plugfile.getName();
        plugname = plugname.substring(0,plugname.length()-6);
        
        try{
            plugreader = new FileInputStream(plugfile);
            plugreader.read(plugdata);
            plugreader.close();
        } catch(Exception e){
            System.out.println("There was some sort of problem loading the plugin.");
        }
        
        return plugdata;
    }
    
}