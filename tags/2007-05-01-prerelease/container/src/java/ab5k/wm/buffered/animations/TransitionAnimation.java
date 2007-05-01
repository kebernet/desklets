/*
 * TransitionAnimation.java
 *
 * Created on April 7, 2007, 2:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered.animations;

import ab5k.desklet.DeskletContainer;
import ab5k.wm.WindowManager;
import org.jdesktop.animation.timing.Animator;

/**
 *
 * @author joshua@marinacci.org
 */
public abstract class TransitionAnimation {
    
    /** Creates a new instance of TransitionAnimation */
    protected TransitionAnimation() { }
    
    /**
     * Returns an animation for the specified desklet container sub-type
     * @param deskletContainer 
     * @param windowManager 
     * @return 
     */
    public abstract Animator createAnimation(TransitionEvent evt);
    
}
