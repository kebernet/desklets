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

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import ab5k.desklet.test.DeskletTester;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.imageio.ImageIO;
import javax.xml.xpath.XPathFactory;
import org.jdesktop.dom.SimpleDocument;
import org.jdesktop.http.Method;
import org.jdesktop.http.async.HtmlHttpRequest;
import org.jdesktop.swingx.JXImageView;
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
public class Main extends AbstractDesklet {
    public static void main(String[] args) {
        DeskletTester.start(Main.class);
    }
    
    private static JXImageView view = new JXImageView();
    private static ConfigPanel configPanel = new ConfigPanel();
    
    public Main() {
    }
    
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        
        final JPopupMenu menu = new JPopupMenu();
        
        // Create and add a menu item
        JMenuItem item = new JMenuItem("Manage Sources");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configPanel.setVisible(true);
                
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
        view.setPreferredSize(new Dimension(300,300));
        context.getContainer().setContent(view);
        context.getContainer().setResizable(true);
        context.getContainer().setVisible(true);
    }
    
    
    public void start() throws Exception {
        PhotoFeed.loadFromXML();
        if (PhotoFeed.feeds.size() > 0) {
            LoadImage();
        }
    }
    public void stop() throws Exception {
        this.context.notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
    public static void LoadImage() {
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
