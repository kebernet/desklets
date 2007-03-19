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
    
    public static boolean isMacOSX() {
        if(System.getProperty("mrj.version") == null) {
            return false;
        } else {
            return true;
        }
    }
    
}
