/*
 * StdDestructionAnimation.java
 *
 * Created on April 7, 2007, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container.wm.buffered.animations;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import org.glossitope.container.util.AnimRepainter;
import org.glossitope.container.wm.buffered.Buffered2DPeer;
import org.glossitope.container.wm.buffered.Buffered2DSubSurface;
import org.glossitope.container.wm.buffered.BufferedDeskletContainer;

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
            Animator anim = new Animator(1000);
            //anim.addTarget(new PropertySetter(dc,"location",new Point(0,0), new Point(200,200)));
            Buffered2DPeer peer = (Buffered2DPeer) dc.getPeer();
            anim.addTarget(new PropertySetter(peer,"alpha",peer.getAlpha(),0f));
            //anim.addTarget(new PropertySetter(dc,"rotation",0f,(float)Math.PI*2f*5f));
            anim.addTarget(new PropertySetter(peer,"scale",peer.getScale(),0.3));
            return anim;
        } else {
            
            final Animator anim = new Animator(1000);
            int num = 5;
            Buffered2DSubSurface[][] surfaces = new Buffered2DSubSurface[num][num];
            int w = (int)dc.getSize().getWidth()/num;
            int h = (int)dc.getSize().getHeight()/num;
            
            Point2D pt = dc.getLocation();
            for(int i=0; i<num; i++) {
                surfaces[i] = new Buffered2DSubSurface[num];
                for(int j = 0; j<num; j++) {
                    Buffered2DSubSurface s  = new Buffered2DSubSurface();
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
            
            anim.addTarget(new TimingTargetAdapter() {
                public void begin() {
                    dc.setVisible(false);
                    dc.showSurfaces = true;
                }
                public void end() {
                    dc.showSurfaces = false;
                }
            });
            final Animator astart = new Animator(250);
            astart.addTarget(new AnimRepainter((JComponent)evt.getWindowManager().getRenderPanel()));
            if(dc.getPeer() instanceof Buffered2DPeer) {
                astart.addTarget(new PropertySetter(dc.getPeer(), "scale",((Buffered2DPeer)dc.getPeer()).getScale(), 1.0));
            }
            //astart.addTarget(new StartAnimAfter(anim));
            Animator a2 = new Animator(1250);
            a2.addTarget(new TimingTarget() {
                public void begin() {
                    astart.start();
                }
                public void end() {
                }
                public void repeat() {
                }
                public void timingEvent(float f) {
                    if(f > 250f/1250f) {
                        if(!anim.isRunning()) {
                            anim.start();
                        }
                    }
                }
            });
            return a2;
        }
    }
    
}
