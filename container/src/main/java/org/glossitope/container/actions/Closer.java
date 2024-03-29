/*
 * CloseState.java
 *
 * Created on February 26, 2007, 12:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.glossitope.container.actions;

import org.glossitope.container.Core;
import org.glossitope.container.MainPanel;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.util.Date;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class Closer {
    private boolean windowClosed = false;
    private Core main;
    private boolean microdocking = false;
    private boolean microCollapsed = false;
    private boolean onTheMove = false;
    
    
    /** Creates a new instance of CloseState */
    public Closer(Core main) {
        this.main = main;
        new Thread(new MicroCloser()).start();
    }
    
    
    boolean isMicroCollapsed() {
        return microCollapsed;
    }
    
    public boolean isMicrodocking() {
        return microdocking;
    }
    public void setMicrodocking(boolean microdocking) {
        this.microdocking = microdocking;
    }

    public void setOnTheMove(boolean onTheMove) {
        this.onTheMove = onTheMove;
    }
    
    public boolean isOnTheMove() {
        return onTheMove;
    }
    
    private void microOpen() {
        if(isWindowClosed() && isMicrodocking()) {
            Rectangle cb = main.getCollapseWindowAction().getClosedBounds();
            Animator anim = PropertySetter.createAnimator(300, main.getFrame(), "location",
                    main.getFrame().getLocation(), 
                    new Point(cb.x, cb.y));
            anim.addTarget(new TimingTargetAdapter() {
                @Override
                public void begin() {
                    if(main.getMainPanel().getDockingSide() == MainPanel.DockingSide.Left) {
                        // if on the left move location back to minus before resize
                        main.getFrame().setLocation(-main.getMainPanel().getDockWidth()+2, main.getFrame().getY());
                    }
                    // set dock size
                    main.getFrame().setSize(main.getMainPanel().getDockWidth(),main.getFrame().getHeight());
                }
            });
            anim.start();
        }
        microCollapsed = false;
    }
    
    private void microClose() {
        if(isWindowClosed() && isMicrodocking()) {
            Rectangle cb = main.getCollapseWindowAction().getClosedBounds();
            Point ml;
            if(main.getMainPanel().getDockingSide() == MainPanel.DockingSide.Right) {
                ml = new Point(cb.x + main.getMainPanel().getDockWidth()-2, cb.y);
            } else {
                ml = new Point(cb.x - main.getMainPanel().getDockWidth()+2, cb.y);
            }
            Animator anim = PropertySetter.createAnimator(300, main.getFrame(), "location",
                    main.getFrame().getLocation(), ml);
            anim.addTarget(new TimingTargetAdapter() {
                   @Override
                public void end() {
                       // minimize dock after sliding away from the screen
                    main.getFrame().setSize(2, main.getFrame().getHeight());
                    if(main.getMainPanel().getDockingSide() == MainPanel.DockingSide.Left) {
                        // if on the left move location back to zero after resize
                        main.getFrame().setLocation(0, main.getFrame().getY());
                    }
                } 
            });
            anim.start();
            
        }
        microCollapsed = true;
    }
    
    public boolean isWindowClosed() {
        return windowClosed;
    }
     
    public void setWindowClosed(boolean windowClosed) {
        this.windowClosed = windowClosed;
    }
    
    private class MicroCloser implements Runnable {
         
        public void run() {
            long intime = -1;
            long outtime = -1;
            while (true) {
                u.sleep(100);
                PointerInfo info = MouseInfo.getPointerInfo();
                if(main.getFrame() == null) continue;
                if (intime < 0) {
                    if (main.getFrame().getBounds().contains(info.getLocation())) {
                        //u.p("inside the dock");
                        outtime = -1;
                        intime = new Date().getTime();
                    }
                }
                if (intime > 0 && outtime < 0) {
                    if (!main.getFrame().getBounds().contains(info.getLocation())) {
                        //u.p("moved out");
                        outtime = new Date().getTime();
                    }
                }
                if (intime > 0 && outtime > 0 && !isOnTheMove()) {
                    if (new Date().getTime() - outtime > 300) {
                        //u.p("in then out for more than one sec. closing");
                        microClose();
                        intime = -1;
                        outtime = -1;
                    }
                }
                
                if(isMicroCollapsed()) {
                    //outside in
                    if (intime < 0) {
                        if (main.getFrame().getBounds().contains(info.getLocation())) {
                            intime = new Date().getTime();
                            //u.p("moved in");
                        }
                    }
                    if (intime > 0 && ((new Date().getTime() - intime) > 300)) {
                        //u.p("inside for more than one sec. opening");
                        microOpen();
                        intime = -1;
                        outtime = -1;
                    }
                }
            }
        }
    }
    
}
