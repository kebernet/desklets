/*
 * Desklet.java
 *
 * Created on August 3, 2006, 10:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.desklet;

import java.awt.Container;

/**
 *
 * @author cooper
 * @author joshua@marinacci.org
 */
public abstract class Desklet {
    
    private DeskletContext context;
    
    /**
     * Set the DeskletContext. This method will be called before init().
     * @param context 
     */
    public void setContext(DeskletContext context) {
        this.context = context;
    }
    
    public DeskletContext getContext() {
        return this.context;
    }
    
    public abstract void init() throws Exception;
    
    public abstract void start() throws Exception;
    
    public abstract void stop() throws Exception;
    
    public abstract void destroy() throws Exception;
    
    

}
