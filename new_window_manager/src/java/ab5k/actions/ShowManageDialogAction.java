package ab5k.actions;

import ab5k.*;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;

public class ShowManageDialogAction extends BaseAction {
    Core main;
    
    private MattePainter disablePainter;
    public ShowManageDialogAction(Core main) {
        this.main = main;
    }
    
    public void actionPerformed(ActionEvent e) {
        main.getCollapseWindowAction().doExpand();
        main.getFrame().setAlwaysOnTop(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JLayeredPane layeredPane = main.getFrame().getLayeredPane();
                
                // get the desktop pane metrics
                Dimension desktopSize = new Dimension(getOpenBounds().width, getOpenBounds().height);
                
                // set up an overlay to disable the widgets
                final JXPanel gpanel = getDisablePanel(layeredPane, desktopSize);
                
                // create a container for the dialog
                final JPanel ldialog = getDialog(layeredPane, desktopSize);
                
                anim.setDirection(Animator.Direction.FORWARD);
                anim.setInitialFraction(0f);
                anim.addTarget(new TimingTargetAdapter() {
                    public void begin() {
                        ldialog.setVisible(true);
                        gpanel.setVisible(true);
                    }
                    public void end() {
                        anim.removeTarget(this);
                    }
                });
                
                anim.start();
            }
        });
    }
    
    
    private Animator anim;
    private JPanel ldialog;
    private JXPanel gpanel;
    
    private JXPanel getDisablePanel(JLayeredPane layeredPane, Dimension desktopSize) {
        Point desktopLocation = new Point(0,0);//main.getMainPanel().desktop.getLocation();
        gpanel = new JXPanel();
        gpanel.setOpaque(false);
        disablePainter = new MattePainter(new Color(255,255,255,0));
        //PinstripePainter pinStripe = new PinstripePainter(new Color(255,255,255,0),0,10,5);
        gpanel.setBackgroundPainter(disablePainter);
        gpanel.setSize(desktopSize);
        gpanel.setLocation(desktopLocation);
        layeredPane.add(gpanel,JLayeredPane.PALETTE_LAYER);
        gpanel.setVisible(true);
        return gpanel;
    }
    
    private JPanel getDialog(final JLayeredPane layeredPane, Dimension desktopSize) {
        if(ldialog == null) {
            ldialog = new JPanel();
            anim = new Animator(600);
            anim.addTarget(new PropertySetter(disablePainter,"fillPaint",new Color(255,255,255,0),new Color(255,255,255,100)));
            anim.addTarget(new TimingTargetAdapter() {
                public void timingEvent(float f) {
                    gpanel.repaint();
                }
            });
            
            
            // configure the management panels
            ShowManageDialogActionPanel panel = new ShowManageDialogActionPanel(main,new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    anim.setDirection(Animator.Direction.BACKWARD);
                    anim.setInitialFraction(1f);
                    anim.addTarget(new TimingTargetAdapter() {
                        public void end() {
                            ldialog.setVisible(false);
                            gpanel.setVisible(false);
                            //layeredPane.remove(gpanel);
                            //layeredPane.remove(ldialog);
                            main.getFrame().setAlwaysOnTop(true);
                            anim.removeTarget(this);
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
            Point upperLeft = new Point((desktopSize.width-ldialog.getWidth())/2,
                    (desktopSize.height-ldialog.getHeight())/2);
            Point offScreen = new Point(upperLeft.x, -ldialog.getHeight());
            ldialog.setLocation(offScreen);
            anim.addTarget(new PropertySetter(ldialog,"location",offScreen,upperLeft));
            
            // add and center it
            layeredPane.add(ldialog,JLayeredPane.MODAL_LAYER);
        }
        return ldialog;
    }
}
