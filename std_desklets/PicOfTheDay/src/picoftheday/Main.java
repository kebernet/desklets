/*
 * Main.java
 *
 * Created on February 14, 2007, 5:05 PM
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
import java.net.URL;
import javax.imageio.ImageIO;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;
import org.jdesktop.dom.SimpleDocument;
import org.jdesktop.http.Method;
import org.jdesktop.http.async.HtmlHttpRequest;
import org.jdesktop.swingx.JXImageView;
import org.jdesktop.xpath.XPathUtils;
import org.joshy.util.u;

/**
 *
 * @author joshua@marinacci.org
 */
public class Main extends AbstractDesklet {

    public static void main(String[] args) {
        DeskletTester.start(Main.class);
    }
    
    private JXImageView view;
    String baseURL = "http://antwrp.gsfc.nasa.gov/apod/";
    String pageURL = "astropix.html";
    String xpathQuery = "//body/center/p/a/img/@src";
    
    public Main() {
    }
    
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        view = new JXImageView();
        view.setPreferredSize(new Dimension(300,300));
        context.getContainer().setContent(view);
        context.getContainer().setResizable(true);
        context.getContainer().setVisible(true);
    }
    
    
    public void start() throws Exception {
        final HtmlHttpRequest req = new HtmlHttpRequest();
        req.addReadyStateChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                u.p("state = " + req.getReadyState());
                if(req.getReadyState() == HtmlHttpRequest.ReadyState.LOADED) {
                    try {
                        SimpleDocument html = req.getResponseHtml();
                        u.p("xpath class = " + XPathFactory.newInstance().getClass());
                        u.p("html = " + html);
                        u.p("text = " + html.toXML());
                        String url = XPathUtils.getString(xpathQuery,html);
                        URL furl = new URL(baseURL+url);
                        BufferedImage buf = ImageIO.read(furl);
                        view.setImage(buf);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        req.open(Method.GET,baseURL+pageURL,true);
        req.send();
    }
    
    public void stop() throws Exception {
        this.context.notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
}
