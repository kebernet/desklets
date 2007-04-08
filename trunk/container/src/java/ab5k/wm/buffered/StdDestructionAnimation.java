/*
 * StdDestructionAnimation.java
 *
 * Created on April 7, 2007, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.desklet.DeskletContainer;
import ab5k.util.AnimRepainter;
import ab5k.wm.WindowManager;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author joshy
 */
public class StdDestructionAnimation extends TransitionAnimation {
    private boolean oldAnim = false;
    
    /** Creates a new instance of StdDestructionAnimation */
    public StdDestructionAnimation() {
    }
    
    
    public Animator createAnimation(TransitionEvent evt) {
        final BufferedDeskletContainer dc = evt.getContainer();
        if(oldAnim) {
            Animator anim = new Animator(500);
            //anim.addTarget(new PropertySetter(dc,"location",new Point(0,0), new Point(200,200)));
            anim.addTarget(new PropertySetter(dc,"alpha",dc.getAlpha(),0f));
            //anim.addTarget(new PropertySetter(dc,"rotation",0f,(float)Math.PI*2f*5f));
            anim.addTarget(new PropertySetter(dc,"scale",dc.getScale(),0.3));
            return anim;
        } else {
            
            Animator anim = new Animator(1000);
            int num = 5;
            BSurface[][] surfaces = new BSurface[num][num];
            int w = (int)dc.getSize().getWidth()/num;
            int h = (int)dc.getSize().getHeight()/num;
            
            Point2D pt = dc.getLocation();
            for(int i=0; i<num; i++) {
                surfaces[i] = new BSurface[num];
                for(int j = 0; j<num; j++) {
                    BSurface s  = new BSurface();
                    s.setSize(new Dimension(w,h));//img.getWidth(), img.getHeight()));
                    
                    s.subRect = new Rectangle(0+i*w, 0+j*h, w, h);
                    
                    surfaces[i][j] = s;
                    dc.surfaces.add(s);
                    int distx = i-num/2;
                    int disty = j-num/2;
                    anim.addTarget(new PropertySetter(s,"location",
                            new Point2D.Double(
                            pt.getX()+i*w,
                            pt.getY()+j*h),
                            new Point2D.Double(
                            pt.getX()+i*w + 100*distx,
                            pt.getY()+j*h + 100*disty)));
                    anim.addTarget(new PropertySetter(s,"alpha",1f,0.7f,0f));
                    anim.addTarget(new PropertySetter(s,"rotation",0.0,
                            ((Math.random()*2)-1)*
                            Math.PI*2));
                }
            }
            
            anim.addTarget(new TimingTarget() {
                public void begin() {
                    dc.setVisible(false);
                    dc.showSurfaces = true;
                }
                public void end() {
                }
                public void repeat() {
                }
                public void timingEvent(float f) {
                }
            });
            Animator astart = new Animator(250);
            astart.addTarget(new AnimRepainter((JComponent)evt.getWindowManager().panel));
            astart.addTarget(new PropertySetter(dc,"scale",dc.getScale(), 1.0));
            astart.addTarget(new StartAnimAfter(anim));
            astart.addTarget(new TimingTarget() {
                public void begin() {
                }
                public void end() {
                }
                public void repeat() {
                }
                public void timingEvent(float f) {
                }
            });
            return anim;
        }
    }

}
