/*
 * Desklet.java
 *
 * Created on August 3, 2006, 10:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.desklet;

import java.awt.Container;

/**
 *
 * @author cooper
 */
public interface Desklet {
    
    
    public void init(DeskletContext context) throws Exception;
    
    public void start() throws Exception;
    
    public void stop() throws Exception;
    
    public void destroy() throws Exception;
    
    

}
