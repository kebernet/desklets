/*
 * Main.java
 *
 * Created on February 14, 2007, 5:05 PM
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package picoftheday;

import ab5k.desklet.Desklet;
import ab5k.desklet.DeskletContext;
import ab5k.desklet.test.DeskletTester;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.imageio.ImageIO;
import javax.xml.xpath.XPathFactory;
import org.jdesktop.dom.SimpleDocument;
import org.jdesktop.http.Method;
import org.jdesktop.http.async.HtmlHttpRequest;
import org.jdesktop.swingx.JXImageView;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.*;
import org.jdesktop.animation.timing.*;
import org.jdesktop.animation.timing.interpolation.*;
import org.jdesktop.swingx.painter.effects.ShadowPathEffect;
import ab5k.utils.BusyLabel;
import org.jdesktop.xpath.XPathUtils;
import org.jdesktop.swingx.StackLayout;
import org.joshy.util.u;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import javax.swing.*;
import java.awt.event.*;
/**
 *
 * @author joshua@marinacci.org
 */
public class Main extends Desklet {
    private static JXImageView view = new JXImageView();
    public static BusyGlassPane busyPanel = new BusyGlassPane();
    private static ConfigPanel configPanel = new ConfigPanel();
    
    public static void main(String[] args) {
        DeskletTester.start(Main.class);
    }
    
    public Main() {
    }
    
    
    public void init() throws Exception {
        final JPopupMenu menu = new JPopupMenu();
        
        // Create and add a menu item
        JMenuItem item = new JMenuItem("Manage Sources");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configPanel.setVisible(true);
                busyPanel.setVisible(false);
            }
        });
        menu.add(item);
        final JButton btnSetup = new JButton("Setup");
        view.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
        view.add(menu);
        view.setLayout(new StackLayout());
        configPanel.setVisible(false);
        view.add(configPanel,StackLayout.TOP);
        view.add(busyPanel, StackLayout.TOP);
        
        view.setPreferredSize(new Dimension(300,300));
        getContext().getContainer().setContent(view);
        getContext().getContainer().setResizable(true);
        getContext().getContainer().setVisible(true);
    }
    
    
    public void start() throws Exception {
        PhotoFeed.loadFromXML();
        /*SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                busyPanel.start();
            }
        });*/
        if (PhotoFeed.feeds.size() > 0) {
            LoadImage();
        }
        /*SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                busyPanel.stop();
                busyPanel.setVisible(false);
            }
        });*/
    }
    public void stop() throws Exception {
        getContext().notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
    public static void LoadImage() throws Exception{
        new Thread(new Runnable() {
            public void run() {
                busyPanel.start();
            }
        }).start();
        final PhotoFeed pf = PhotoFeed.getSelectedFeed();
        if (pf.getSourceType() == PhotoFeed.SourceType.WEBPAGE) {
            final HtmlHttpRequest req = new HtmlHttpRequest();
            req.addReadyStateChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    u.p("state = " + req.getReadyState());
                    if(req.getReadyState() == HtmlHttpRequest.ReadyState.LOADED) {
                        try {
                            SimpleDocument html = req.getResponseHtml();
                            u.p("xpath class = " + XPathFactory.newInstance().getClass());
                            u.p("html = " + html);
                            if (html == null) {
                                // failed to connect to the feed
                                return;
                            }
                            u.p("text = " + html.toXML());
                            String url = XPathUtils.getString(pf.getQueryString(),html);
                            URL furl = new URL(pf.getBaseUrl()+url);
                            BufferedImage buf = ImageIO.read(furl);
                            view.setImage(buf);
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    busyPanel.stop();
                                    busyPanel.setVisible(false);
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            req.open(Method.GET,pf.getBaseUrl()+pf.getPageUrl(),true);
            req.send();
        } else {
            
            PhotoFeed d = PhotoFeed.getSelectedFeed();
            view.setImage(d.pickRandom());
            
        }
        
    }
    
}

class BusyGlassPane extends JXPanel {
    Animator anim;
    private String text;
    private TextPainter textPainter;
    public void start() {
        if (anim.isRunning() == false)
            anim.start();
    }
    public void stop() {
        anim.stop();
    }
    public BusyGlassPane() {
        setOpaque(false);
        BusyPainter busyPainter = new BusyPainter();
        busyPainter.setBaseColor(new Color(0,0,0,0));
        busyPainter.setBarWidth(15f);
        busyPainter.setBarLength(60f);
        busyPainter.setCenterDistance(75f);
        busyPainter.setPoints(20);
        busyPainter.setTrailLength(18);
        textPainter = new TextPainter("Loading...", new Font(Font.SANS_SERIF,Font.BOLD,60),Color.BLUE);
        textPainter.setAreaEffects(new ShadowPathEffect());
        setBackgroundPainter(new CompoundPainter(
                new MattePainter(new Color(255,255,255,150)),
                busyPainter, textPainter));
        anim = PropertySetter.createAnimator(1500,busyPainter,"frame",0,busyPainter.getPoints());
        anim.setRepeatBehavior(Animator.RepeatBehavior.LOOP);
        anim.setRepeatCount(Animator.INFINITE);
        anim.addTarget(new TimingTarget() {
            public void begin() {
                setVisible(true);
            }
            public void end() {
                setVisible(false);
            }
            public void repeat() {
            }
            public void timingEvent(float f) {
                repaint();
            }
        });
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        textPainter.setText(text);
    }
}