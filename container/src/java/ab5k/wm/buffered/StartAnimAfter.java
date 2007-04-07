package ab5k.wm.buffered;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;


class StartAnimAfter implements TimingTarget {
    
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