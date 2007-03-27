/*
 * ManagePanelAnimations.java
 *
 * Created on March 27, 2007, 1:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.wm.buffered;

import ab5k.desklet.DeskletContainer;
import ab5k.security.DeskletConfig;
import ab5k.security.Registry;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 *
 * @author joshy
 */
public class ManagePanelAnimations {
    
    /** Creates a new instance of ManagePanelAnimations */
    public ManagePanelAnimations(BufferedWM wm) {
        this.wm = wm;
        try {
            closeIcon = new ImageIcon(ImageIO.read(getClass().getResource("close2.png")));
            closeOverIcon = new ImageIcon(ImageIO.read(getClass().getResource("close2_over.png")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private List<JButton> manageButtons;

    private BufferedWM wm;
    private final int transitionLength = 500;
    private final int manageButtonGap = 60;
    private final int manageButtonSpacing = 10;
    private final int firstColumnX = 100;
    private Icon closeIcon, closeOverIcon; 
    
    void showManagePanel() {
        manageButtons = new ArrayList<JButton>();
        List<DeskletConfig> configs = Registry.getInstance().getDeskletConfigs();
        
        Animator firstAnim = new Animator(1);
        Animator prevAnim = firstAnim;
        Animator lastAnim = firstAnim;
        int y = 0;
        final int x = wm.panel.getWidth()-200;
        int animlen = transitionLength / configs.size();
        for(DeskletConfig cfg : configs) {
            final DeskletConfig config = cfg;
            final JButton btn = new JButton(cfg.getName());
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    wm.start(config);
                }
            });
            btn.setOpaque(false);
            manageButtons.add(btn);
            final int startY = y;
            final int endY = y+manageButtonGap;
            
            final Animator propAnim = PropertySetter.createAnimator(animlen, btn, "location",
                    new Point(x,startY), new Point(x,endY));
            TimingTarget creator = new TimingTarget() {
                public void begin() {
                    wm.panel.add(btn);
                    btn.setLocation(x,startY);
                    Dimension dim = new Dimension(150,manageButtonGap-manageButtonSpacing);
                    btn.setPreferredSize(dim);
                    btn.setSize(dim);
                }
                public void end() {
                }
                public void repeat() {
                }
                public void timingEvent(float f) {
                }
            };
            propAnim.addTarget(creator);
            prevAnim.addTarget(new StartAnimAfter(propAnim));
            prevAnim = propAnim;
            lastAnim = propAnim;
            y+=manageButtonGap;
        }
        firstAnim.start();
    }

    void hideManagePanel() {
        Animator startAnim = new Animator(1);
        Animator prevAnim = startAnim;
        int animlen = transitionLength / manageButtons.size();
        // create the animators in reverse order 
        for(int i=manageButtons.size()-1; i>=0; i--) {
            final JButton btn = manageButtons.get(i);
            final Animator propAnim = PropertySetter.createAnimator(animlen,btn,"location", btn.getLocation(),
                    new Point(btn.getLocation().x,btn.getLocation().y-manageButtonGap));
            propAnim.addTarget(new TimingTarget() {
                public void begin() {
                }
                public void end() {
                    wm.panel.remove(btn);
                }
                public void repeat() {
                }
                public void timingEvent(float f) {
                }
            });
            prevAnim.addTarget(new StartAnimAfter(propAnim));
            prevAnim = propAnim;
        }
        startAnim.start();
        manageButtons = null;
    }
    

    Map<BufferedDeskletContainer, Point> originalLocations = new HashMap<BufferedDeskletContainer, Point>();
    Map<BufferedDeskletContainer, JButton> stopButtons = new HashMap<BufferedDeskletContainer, JButton>();
    
    void moveDeskletsToColumns() {
        Animator anim = new Animator(500);
        for(int i=0; i<wm.desklets.size(); i++) {
            DeskletContainer dc = wm.desklets.get(i);
            if(dc instanceof BufferedDeskletContainer) {
                final BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
                
                // save the original location
                originalLocations.put(bdc,bdc.getLocation());
                
                // constrain to 200x100 if needed
                if(bdc.getSize().getWidth() > 200 || bdc.getSize().getHeight() > 100) {
                    double targetScale = 200/bdc.getSize().getWidth();
                    // if still too big
                    if(targetScale*bdc.getSize().getHeight() > 100) {
                        targetScale = 100/bdc.getSize().getHeight();
                    }
                    anim.addTarget(new PropertySetter(bdc, "scale", 1.0, targetScale));
                }
                
                int x = firstColumnX;
                int y = i*105 + 50;
                
                // wrap to new columns
                while(y > wm.panel.getHeight()-105) {
                    x+=205+50;
                    y-=(wm.panel.getHeight()-105);
                }
                
                anim.addTarget(new PropertySetter(bdc,"location",
                        originalLocations.get(bdc),
                        new Point(x,y)));
                anim.addTarget(new TimingTarget() {
                    public void begin() {
                    }
                    public void end() {
                        final JButton close = new JButton();
                        close.setIcon(closeIcon);
                        close.setRolloverIcon(closeOverIcon);
                        close.setBorderPainted(false);
                        close.setOpaque(false);
                        close.setLocation(bdc.getLocation().x-44,bdc.getLocation().y);
                        wm.panel.add(close);
                        close.setPreferredSize(new Dimension(40,40));
                        close.setSize(new Dimension(40,40));
                        stopButtons.put(bdc,close);
                        close.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wm.stop(bdc);
                                wm.panel.remove(close);
                            }
                        });
                    }
                    public void repeat() {
                    }
                    public void timingEvent(float f) {
                    }
                });
            }
        }
        anim.addTarget(new AnimRepainter(wm.panel));
        anim.addTarget(new ToggleAnimatingProperty());
        anim.start();
    }

    void moveDeskletsToOriginalPositions() {
        Animator anim = new Animator(500);
        for(DeskletContainer dc : wm.desklets) {
            if(dc instanceof BufferedDeskletContainer) {
                BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
                anim.addTarget(new PropertySetter(bdc,"location",
                        bdc.getLocation(),
                        originalLocations.get(bdc)));
                // restore the scale
                if(bdc.getScale() != 1.0) {
                    anim.addTarget(new PropertySetter(bdc,"scale",bdc.getScale(),1.0));
                }
            }
        }
        anim.addTarget(new AnimRepainter(wm.panel));
        anim.addTarget(new TimingTarget() {
            public void begin() {
                for(JButton b : stopButtons.values()) {
                    wm.panel.remove(b);
                }
                stopButtons.clear();
            }
            public void end() {
            }
            public void repeat() {
            }
            public void timingEvent(float f) {
            }
        });
        anim.addTarget(new ToggleAnimatingProperty());
        anim.start();
        originalLocations.clear();
    }

    private class ToggleAnimatingProperty implements TimingTarget {

        public void begin() {
            wm.panel.setAnimating(true);
        }

        public void end() {
            wm.panel.setAnimating(false);
        }

        public void repeat() {
        }

        public void timingEvent(float f) {
        }
    }


    
}
