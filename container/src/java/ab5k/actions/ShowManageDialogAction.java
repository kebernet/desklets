package ab5k.actions;

import ab5k.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.PinstripePainter;

public class ShowManageDialogAction extends AbstractAction {
    Main main;
    public ShowManageDialogAction(Main main) {
        this.main = main;
    }
    
    public void actionPerformed(ActionEvent e) {
        main.getCollapseWindowAction().doExpand();
        main.getFrame().setAlwaysOnTop(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JLayeredPane layeredPane = main.getFrame().getLayeredPane();
                
                // get the desktop pane metrics
                Dimension desktopSize = main.getMainPanel().desktop.getSize();
                Point desktopLocation = main.getMainPanel().desktop.getLocation();
                
                // set up an overlay to disable the widgets
                final JXPanel gpanel = new JXPanel();
                gpanel.setOpaque(false);
                MattePainter disablePainter = new MattePainter(new Color(255,255,255,0));
                //PinstripePainter pinStripe = new PinstripePainter(new Color(255,255,255,0),0,10,5);
                gpanel.setBackgroundPainter(disablePainter);
                gpanel.setSize(desktopSize);
                gpanel.setLocation(desktopLocation);
                layeredPane.add(gpanel,JLayeredPane.PALETTE_LAYER);
                gpanel.setVisible(true);
                
                final Animator anim = new Animator(600);
                anim.addTarget(new PropertySetter(disablePainter,"fillPaint",new Color(255,255,255,0),new Color(255,255,255,100)));
                anim.addTarget(new TimingTargetAdapter() {
                    public void timingEvent(float f) {
                        gpanel.repaint();
                    }
                });
                
                // create a container for the dialog
                final JPanel ldialog = new JPanel();
                
                // configure the management panels
                ShowManageDialogActionPanel panel = new ShowManageDialogActionPanel(main,new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        anim.setDirection(Animator.Direction.BACKWARD);
                        anim.setInitialFraction(1f);
                        anim.addTarget(new TimingTarget() {
                            public void begin() {
                            }
                            public void end() {
                                ldialog.setVisible(false);
                                gpanel.setVisible(false);
                                layeredPane.remove(gpanel);
                                layeredPane.remove(ldialog);
                                main.getFrame().setAlwaysOnTop(true);
                            }
                            public void repeat() {
                            }
                            public void timingEvent(float f) {
                            }
                        });
                        anim.start();
                    }
                });
                panel.tabs.add("Manage Desklets", new ManagePanel(main));
                panel.tabs.add("Preferences", new PreferencesPanel(main));
                panel.tabs.add("About", new AboutPanel(main));
                
                ldialog.setLayout(new BorderLayout());
                ldialog.add(panel,"Center");
                ldialog.setSize(700,500);
                
                
                // add and center it
                layeredPane.add(ldialog,JLayeredPane.MODAL_LAYER);
                Point upperLeft = new Point((desktopSize.width-ldialog.getWidth())/2,
                        (desktopSize.height-ldialog.getHeight())/2);
                Point offScreen = new Point(upperLeft.x, -ldialog.getHeight());
                ldialog.setLocation(offScreen);
                
                //anim.addTarget(new PropertySetter(ldialog,"size",new Dimension(0,500),new Dimension(700,500)));
                anim.addTarget(new PropertySetter(ldialog,"location",offScreen,upperLeft));
                anim.start();
            }
        });
    }
}