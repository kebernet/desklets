/*
 * Bugs.java
 *
 * Created on April 9, 2007, 10:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.util;

/**
 *
 * @author joshy
 */
public final class Bugs {
    
    /** Creates a new instance of Bugs */
    private Bugs() {
    }
    
    /**
     * Indicates that the java.awt.FileDialog is very ugly on this
     * platform and maybe we should use the JFileChooser instead.
     * @return 
     */
    public static boolean isFileDialogUgly() {
        return PlafUtil.isLinux();
        //return true;
    }
    
}
