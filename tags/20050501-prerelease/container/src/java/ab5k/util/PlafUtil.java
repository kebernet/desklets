/*
 * PlafUtil.java
 *
 * Created on July 12, 2006, 2:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.util;

/**
 *
 * @author jm158417
 */
public class PlafUtil {
    
    /** Creates a new instance of PlafUtil */
    private PlafUtil() {
    }
    
    /**
     * Indicates if the VM is running on some version of Linux
     * @return 
     */
    public static boolean isLinux() {
        if(System.getProperty("os.name") != null &&
                System.getProperty("os.name").toLowerCase().startsWith("linux")) {
            return true;
        }
        return false;
    }
    
    
    /**
     * Indicates if the VM is running on some version of Mac OS X
     * @return 
     */
    public static boolean isMacOSX() {
        if(System.getProperty("mrj.version") == null) {
            return false;
        } else {
            return true;
        }
    }
    
}
