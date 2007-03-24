package ab5k.wm.buffered;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


class MouseRedispatcher implements MouseListener, MouseMotionListener {
    public MouseRedispatcher(BufferedWM bufferedWM) {
        this.bufferedWM = bufferedWM;
    }

    private final BufferedWM bufferedWM;

    
    public void mouseClicked(MouseEvent e) {
        this.bufferedWM.redispatch(e);
    }
    
    public void mouseEntered(MouseEvent e) {
        this.bufferedWM.redispatch(e);
    }
    
    public void mouseExited(MouseEvent e) {
        this.bufferedWM.redispatch(e);
    }
    
    public void mousePressed(MouseEvent e) {
        this.bufferedWM.raiseWindow(e);
        this.bufferedWM.redispatch(e);
    }
    
    public void mouseReleased(MouseEvent e) {
        this.bufferedWM.redispatch(e);
    }
    public void mouseDragged(MouseEvent e) {
        this.bufferedWM.redispatch(e);
    }
    public void mouseMoved(MouseEvent e) {
        this.bufferedWM.redispatch(e);
    }
}