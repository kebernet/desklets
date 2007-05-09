package org.glossitope.container.wm.buffered.animations;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;


public class StartAnimAfter implements TimingTarget {
    
    private Animator anim;
    
    public StartAnimAfter(Animator anim) {
        super();
        this.anim = anim;
    }
    
    public void begin() {
    }
    
    public void end() {
        anim.start();
    }
    
    public void repeat() {
    }
    
    public void timingEvent(float f) {
    }
}