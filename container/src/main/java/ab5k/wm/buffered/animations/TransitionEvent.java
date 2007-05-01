/*
 * TransitionEvent.java
 *
 * Created on April 7, 2007, 2:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered.animations;

import ab5k.desklet.Desklet;
import ab5k.wm.buffered.BufferedDeskletContainer;
import ab5k.wm.buffered.BufferedWM;

/**
 *
 * @author joshy
 */
public class TransitionEvent {
    
    /** Creates a new instance of TransitionEvent */
    public TransitionEvent(BufferedWM wm, Desklet desklet, BufferedDeskletContainer dc) {
        this.wm = wm;
        this.desklet = desklet;
        this.dc = dc;
    }
    private BufferedWM wm;
    private Desklet desklet;
    private BufferedDeskletContainer dc;
    
    public BufferedWM getWindowManager() {
        return wm;
    }
    
    public Desklet getDesklet() {
        return desklet;
    }
    
    public BufferedDeskletContainer getContainer() {
        return dc;
    }
    
}
