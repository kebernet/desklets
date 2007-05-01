/*
 * Main.java
 *
 * Created on February 13, 2007, 8:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package photoviewerdesklet;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import ab5k.desklet.test.DeskletTester;
import ab5k.utils.BusyLabel;
import com.sun.syndication.feed.module.mediarss.MediaEntryModule;
import com.sun.syndication.feed.module.mediarss.MediaModule;
import com.sun.syndication.feed.module.mediarss.types.MediaContent;
import com.sun.syndication.feed.module.mediarss.types.UrlReference;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.totsp.util.BeanArrayList;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.joshy.util.u;

/**
 *
 * @author joshy
 */
public class Main extends AbstractDesklet {
    
    BeanArrayList photos;
    Thread thread;
    PhotoViewerPanel panel;
    
    /** Creates a new instance of Main */
    public Main() {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DeskletTester.start(Main.class);
    }
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        
        photos = new BeanArrayList("photos",this);
        panel = new PhotoViewerPanel();
        panel.setList(photos);
        context.getContainer().setContent(panel);
    }
    
    public void start() throws Exception {
        thread = new Thread(new Runnable() {
            public void run() {
                ((BusyLabel)panel.spinner).startAnimation();
                List<URI> urls = new ArrayList<URI>();
                try {
                    String flickrquery = "http://api.flickr.com/services/feeds/photos_public.gne?tags=animal&format=rss_200";
                    Reader reader = new InputStreamReader(new URL(flickrquery).openStream());
                    SyndFeed feed = new SyndFeedInput().build(reader);
                    for(SyndEntry en : (List<SyndEntry>)feed.getEntries()) {
                        MediaEntryModule media = (MediaEntryModule) en.getModule(MediaModule.URI);
                        //u.p("got media: " + media.getMetadata().getThumbnail()[0].getUrl());
                        MediaContent cont = media.getMediaContents()[0];
                        //u.p("contents = " + cont);
                        URL url = ((UrlReference)cont.getReference()).getUrl();
                        u.p("adding a url: " + url);
                        urls.add(url.toURI());
                    }
                    u.p("added: " + urls.size() + " urls");
                    /*
                    urls.add(new URI("http://files.joshy.org/files/joshy/Photos/DeskletTest/DSC00717.jpg"));
                    urls.add(new URI("http://files.joshy.org/files/joshy/Photos/DeskletTest/DSC00718.jpg"));
                    urls.add(new URI("http://files.joshy.org/files/joshy/Photos/DeskletTest/DSC00719.jpg"));
                    urls.add(new URI("http://files.joshy.org/files/joshy/Photos/DeskletTest/DSC00720.jpg"));
                    urls.add(new URI("http://files.joshy.org/files/joshy/Photos/DeskletTest/DSC00723.jpg"));
                    urls.add(new URI("http://files.joshy.org/files/joshy/Photos/DeskletTest/DSC00725.jpg"));
                    urls.add(new URI("http://files.joshy.org/files/joshy/Photos/DeskletTest/DSC00726.jpg"));
                    urls.add(new URI("http://files.joshy.org/files/joshy/Photos/DeskletTest/DSC00727.jpg"));
                    urls.add(new URI("http://files.joshy.org/files/joshy/Photos/DeskletTest/DSC00728.jpg"));*/
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                while(urls.size() > 0) {
                    URI uri = urls.get(0);
                    try {
                        BufferedImage img = ImageIO.read(uri.toURL());
                        photos.add(img);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    urls.remove(uri);
                }
                ((BusyLabel)panel.spinner).stopAnimation();
            }
        });
        thread.start();
    }
    
    public void stop() throws Exception {
        if(thread != null) {
            thread.interrupt();
        }
        context.notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
}
