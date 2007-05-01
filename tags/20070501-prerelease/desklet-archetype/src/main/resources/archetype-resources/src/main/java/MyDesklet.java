/*
 * MyDesklet.java
 *
 * Created on April 29, 2007, 1:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author cooper
 */
public class MyDesklet extends ab5k.desklet.Desklet{
    
    /** Creates a new instance of MyDesklet */
    public MyDesklet() {
    }

    public void destroy() throws Exception {
    }

    public void stop() throws Exception {
        this.getContext().notifyStopped();
    }

    public void start() throws Exception {
    }

    public void init() throws Exception {
    }
    
}
