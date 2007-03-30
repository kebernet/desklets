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
import ab5k.util.AnimRepainter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXInsets;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.TextPainter;

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
    
    private List<JPanel> manageButtons;

    private BufferedWM wm;
    private final int transitionLength = 500;
    private final int manageButtonGap = 60;
    private final int manageButtonSpacing = 10;
    private final int manageButtonWidth = 200;
    private final int firstColumnX = 100;
    private Icon closeIcon, closeOverIcon; 
    
    void showManagePanel() {
        manageButtons = new ArrayList<JPanel>();
        List<DeskletConfig> configs = Registry.getInstance().getDeskletConfigs();
        
        Animator firstAnim = new Animator(1);
        Animator prevAnim = firstAnim;
        Animator lastAnim = firstAnim;
        int y = 0;
        final int x = wm.panel.getWidth()-manageButtonWidth-50;
        int animlen = transitionLength / configs.size();
        for(DeskletConfig cfg : configs) {
            final DeskletConfig config = cfg;
            final ManageDeskletPanel panel = new ManageDeskletPanel();
            panel.setDeskletName(cfg.getName());
            panel.setDeskletDescription(cfg.getDescription());
            panel.addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    wm.start(config);
                }
            });
            panel.setOpaque(false);
            panel.setPadding(new JXInsets(3));
            RectanglePainter rect = new RectanglePainter(2,2,2,2, 10,10, true,
                    new GradientPaint(
                        new Point(0,0), Color.ORANGE,
                        new Point(0,1), ColorUtil.setSaturation(Color.ORANGE,0.5f)),
                        3, Color.BLACK);
            rect.setPaintStretched(true);
            panel.setBackgroundPainter(rect);
            manageButtons.add(panel);
            final int startY = y;
            final int endY = y+manageButtonGap;
            
            final Animator propAnim = PropertySetter.createAnimator(animlen, panel, "location",
                    new Point(x,startY), new Point(x,endY));
            TimingTarget creator = new TimingTarget() {
                public void begin() {
                    wm.panel.add(panel);
                    panel.setLocation(x,startY);
                    int manageButtonHeight = panel.getPreferredSize().height;
                    manageButtonHeight = 60;
                    Dimension dim = new Dimension(manageButtonWidth,
                            manageButtonHeight);
                    panel.setPreferredSize(dim);
                    panel.setSize(dim);
                    panel.validate();
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
            y+=60+20;//manageButtonGap;
        }
        firstAnim.start();
    }

    void hideManagePanel() {
        Animator startAnim = new Animator(1);
        Animator prevAnim = startAnim;
        int animlen = transitionLength / manageButtons.size();
        // create the animators in reverse order 
        for(int i=manageButtons.size()-1; i>=0; i--) {
            final JPanel panel = manageButtons.get(i);
            final Animator propAnim = PropertySetter.createAnimator(animlen,panel,"location", panel.getLocation(),
                    new Point(panel.getLocation().x,panel.getLocation().y-manageButtonGap));
            propAnim.addTarget(new TimingTarget() {
                public void begin() {
                }
                public void end() {
                    wm.panel.remove(panel);
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
    

    Map<BufferedDeskletContainer, Point2D> originalLocations = new HashMap<BufferedDeskletContainer, Point2D>();
    Map<BufferedDeskletContainer, JButton> stopButtons = new HashMap<BufferedDeskletContainer, JButton>();
    Map<BufferedDeskletContainer, JXPanel> rolloverPanels = new HashMap<BufferedDeskletContainer, JXPanel>();
    
    void moveDeskletsToColumns(final AbstractButton button) {
        Animator anim = new Animator(500);
        anim.addTarget(new AnimButtonDisabler(button));
        for(int i=0; i<wm.getDesklets().size(); i++) {
            DeskletContainer dc = wm.getDesklets().get(i);
            final int scaledWidth = 200;
            final int scaledHeight = 100;
            final int rowGap = 10;
            final int columnGap = 70;
            if(dc instanceof BufferedDeskletContainer) {
                final BufferedDeskletContainer bdc = (BufferedDeskletContainer) dc;
                
                // save the original location
                originalLocations.put(bdc,bdc.getLocation());
                
                // constrain to 200x100 if needed
                if(bdc.getSize().getWidth() > scaledWidth || bdc.getSize().getHeight() > scaledHeight) {
                    double targetScale = scaledWidth/bdc.getSize().getWidth();
                    // if still too big
                    if(targetScale*bdc.getSize().getHeight() > scaledHeight) {
                        targetScale = scaledHeight/bdc.getSize().getHeight();
                    }
                    anim.addTarget(new PropertySetter(bdc, "scale", 1.0, targetScale));
                }
                
                int x = firstColumnX;
                int y = i*(scaledHeight + rowGap) + 50;
                
                // wrap to new columns
                while(y > wm.panel.getHeight()-scaledHeight-rowGap) {
                    x+=scaledWidth+columnGap;
                    y-=(wm.panel.getHeight()-scaledHeight-rowGap);
                }
                
                anim.addTarget(new PropertySetter(bdc,"location",
                        originalLocations.get(bdc),
                        new Point(x,y)));
                anim.addTarget(new TimingTarget() {
                    public void begin() {
                    }
                    public void end() {
                        // the close button
                        final JButton close = new JButton();
                        close.setIcon(closeIcon);
                        close.setRolloverIcon(closeOverIcon);
                        close.setBorderPainted(false);
                        close.setOpaque(false);
                        close.setLocation((int)bdc.getLocation().getX()-44-10,
                                (int)bdc.getLocation().getY());
                        wm.panel.add(close);
                        close.setPreferredSize(new Dimension(40,40));
                        close.setSize(new Dimension(40,40));
                        stopButtons.put(bdc,close);
                        
                        // the overlay rollover panel
                        final JXPanel panel = new JXPanel();
                        Dimension dim = new Dimension(scaledWidth+10*2,scaledHeight+10*2);
                        panel.setPreferredSize(dim);
                        panel.setSize(dim);
                        panel.setLocation((int)bdc.getLocation().getX()-10, (int)bdc.getLocation().getY()-10);
                        panel.setOpaque(false);
                        wm.panel.add(panel);
                        rolloverPanels.put(bdc,panel);
                        String text = bdc.getContext().getConfig().getName();
                        Font font = new Font(Font.SANS_SERIF,Font.BOLD,20);
                        // set a rollover painter
                        panel.addMouseListener(new AnimPanelRollover(panel,
                                null,
                                new CompoundPainter(
                                    new RectanglePainter(5, 5, 5, 5, 20, 20, true, new Color(0, 0, 0, 160), 3.0f, Color.WHITE),
                                    //new RectanglePainter(35, 35, 35, 35, 20, 20, true, new Color(0, 0, 0, 200), 0.0f, Color.WHITE),
                                    new TextPainter(text,font,Color.WHITE)
                                )
                                ));
                        close.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                wm.stop(bdc);
                                wm.panel.remove(close);
                                wm.panel.remove(panel);
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

    void moveDeskletsToOriginalPositions(final AbstractButton button) {
        Animator anim = new Animator(500);
        anim.addTarget(new AnimButtonDisabler(button));
        for(DeskletContainer dc : wm.getDesklets()) {
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
                for(JXPanel p : rolloverPanels.values()) {
                    wm.panel.remove(p);
                }
                rolloverPanels.clear();
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

    private class AnimButtonDisabler implements TimingTarget {

        private AbstractButton button;

        public AnimButtonDisabler(AbstractButton button) {
            super();
            this.button = button;
        }

        public void begin() {
            button.setEnabled(false);
        }

        public void end() {
            button.setEnabled(true);
        }

        public void repeat() {
        }

        public void timingEvent(float f) {
        }
    }

    private class AnimPanelRollover implements MouseListener {

        private JXPanel panel;
        private Painter normal;
        private Painter rollover;

        public AnimPanelRollover(JXPanel panel, Painter normal, Painter rollover) {
            super();
            this.panel = panel;
            this.normal = normal;
            this.rollover = rollover;
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
            panel.setBackgroundPainter(rollover);
        }

        public void mouseExited(MouseEvent e) {
            panel.setBackgroundPainter(normal);
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }




    
}
