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
import org.joshy.util.u;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 *
 * @author joshua@marinacci.org
 */
public class Main extends AbstractDesklet {
    static List<PhotoFeed> feeds = new ArrayList<PhotoFeed>();
    
    public static void main(String[] args) {
        DeskletTester.start(Main.class);
    }
    
    private JXImageView view;
    
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
        LoadData();
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
    public void stop() throws Exception {
        this.context.notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
    public void LoadData() throws Exception{
        /* Eventually this will be replaced by context.getWorkingDirectory().
         * For now, this desklet will place files most likely in the home
         * directory
         */
        try {
           // u.p("Working directory: "+ context.getWorkingDirectory());
            File file = new File(new File(".").getCanonicalPath()+File.separator+"pod.xml");
            if (file.exists() == true) {
                //Load file
                
                SAXBuilder builder = new SAXBuilder();
                Document doc = builder.build(file);
                Element root = doc.getRootElement();
                feeds = PhotoFeed.convertToList(root);
            } else {
                //Regenerate sources
                createPhotoSources();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void createPhotoSources() {
        try {
            u.p("Creating photo sources");
            Document doc = new Document();
            Element root = new Element("sources");
            doc.setRootElement(root);
            
            Element source = new Element("source");
            source.addContent(new Element("name").setText("NASA Photo of the Day"));
            source.addContent(new Element("url").setText("http://antwrp.gsfc.nasa.gov/apod/"));
            source.addContent(new Element("page").setText("astropix.html"));
            source.addContent(new Element("queryString").setText("//body/center/p/a/img/@src"));
            source.addContent(new Element("type").setAttribute(new Attribute("id","WEBPAGE")));
            source.addContent(new Element("selected").setAttribute(new Attribute("id","Y")));
            root.addContent(source);
            
            source = new Element("source");
            source.addContent(new Element("name").setText("Earth Science Photo of the Day"));
            source.addContent(new Element("url").setText("http://epod.usra.edu/"));
            source.addContent(new Element("page").setText("index.php3"));
            source.addContent(new Element("queryString").setText("//body/center/a/img/@src"));
            source.addContent(new Element("type").setAttribute(new Attribute("id","WEBPAGE")));
            source.addContent(new Element("selected").setAttribute(new Attribute("id","N")));
            root.addContent(source);
            
            source = new Element("source");
            source.addContent(new Element("name").setText("Flickr Photo Feed"));
            source.addContent(new Element("url").setText("http://api.flickr.com/services/feeds/photos_public.gne"));
            source.addContent(new Element("queryString").setText("?tags=animal&format=rss_200"));
            source.addContent(new Element("type").setAttribute(new Attribute("id","RSSMEDIA")));
            source.addContent(new Element("selected").setAttribute(new Attribute("id","N")));
            root.addContent(source);
            
            feeds = PhotoFeed.convertToList(root);
           /* Eventually this will be replaced by context.getWorkingDirectory().
            * For now, this desklet will place files most likely in the home
            * directory
            */
            u.p("Saving photo sources to file");
            File file = new File(new File(".").getCanonicalPath()+File.separator+"pod.xml");
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(doc, new FileWriter(file.getAbsolutePath()));
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
