package ab5k.wm.buffered;

import javax.swing.JComponent;
import org.jdesktop.animation.timing.TimingTarget;


class AnimRepainter implements TimingTarget {

    private JComponent comp;
    public AnimRepainter(JComponent comp) {
        this.comp = comp;
    }

    public void begin() {
    }

    public void end() {
    }

    public void repeat() {
    }

    public void timingEvent(float f) {
        comp.repaint();
    }
}