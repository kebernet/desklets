/*
 * RSSPhotoFeed.java
 *
 * Created on March 23, 2007, 2:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 * @author jwill
 */

package picoftheday;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import com.sun.syndication.feed.module.mediarss.MediaEntryModule;
import com.sun.syndication.feed.module.mediarss.MediaModule;
import com.sun.syndication.feed.module.mediarss.types.MediaContent;
import com.sun.syndication.feed.module.mediarss.types.UrlReference;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.io.SyndFeedInput;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import org.joshy.util.u;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import java.io.*;
import javax.swing.*;
/**
 *
 * @author joshy, jwill
 */
public class PhotoFeed {
    enum SourceType { WEBPAGE, RSSMEDIA,RSSENC}
    private String name;
    private String url;
    private String webpage="";
    private String queryString;
    private SourceType sourceType;
    private static PhotoFeed selectedFeed;
    List<URI> urls;
    static ArrayList<PhotoFeed> feeds;
    boolean isDonePreparingURLS =false;
    static Image img = null;
    
    
    /** Creates a new instance of PhotoFeed */
    public PhotoFeed() {
        if (feeds == null) {
            feeds = new ArrayList<PhotoFeed>();
        }
    }
    
    public void prepareURLS() {
        urls = new ArrayList<URI>();
        isDonePreparingURLS = false;
        new Thread(new Runnable() {
            public void run() {
                try {
                    String query = url+webpage+queryString;
                    Reader reader = new InputStreamReader(new URL(query).openStream());
                    SyndFeed feed = new SyndFeedInput().build(reader);
                    if (sourceType == SourceType.RSSMEDIA) {
                        for(SyndEntry en : (List<SyndEntry>)feed.getEntries()) {
                            MediaEntryModule media = (MediaEntryModule) en.getModule(MediaModule.URI);
                            u.p("got media: " + media.getMetadata().getThumbnail()[0].getUrl());
                            MediaContent cont = media.getMediaContents()[0];
                            u.p("contents = " + cont);
                            URL url = ((UrlReference)cont.getReference()).getUrl();
                            urls.add(url.toURI());
                        }
                        u.p("Done");
                        isDonePreparingURLS = true;
                    } else {
                        if(sourceType == SourceType.RSSENC) {
                            for(SyndEntry en : (List<SyndEntry>)feed.getEntries()) {
                                SyndEnclosure enclosure = (SyndEnclosure)en.getEnclosures().get(0);
                                u.p("got enclosure: "+ enclosure.getUrl() );
                                urls.add(new URI(enclosure.getUrl()));
                            }
                            u.p("Done");
                            isDonePreparingURLS = true;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        
        
        
    }
    
    public Image pickRandom() throws Exception{
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main.busyPanel.start();
            }
        });
        this.prepareURLS();
        while (isDonePreparingURLS != true) {
            u.p("Wait");
            Thread.sleep(10);
        }
        u.p(urls.size());
        int random = new Random(new java.util.Date().getTime()).nextInt(urls.size());
        final URI uri = urls.get(random);
        u.p(urls.get(random));
        
        
        
        u.p("getting photo");
        img = ImageIO.read(uri.toURL());
        img = img.getScaledInstance(300,300,Image.SCALE_SMOOTH);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main.busyPanel.stop();
                Main.busyPanel.setVisible(false);
            }
        });
        
        
        
        u.p("returning image");
        return img;
        
    }
    
    public static ArrayList<PhotoFeed> convertToList(Element element) {
        ArrayList<PhotoFeed> feeds = new ArrayList<PhotoFeed>();
        try {
            for(Object obj : element.getChildren("source")) {
                Element e = (Element)obj;
                PhotoFeed feed = new PhotoFeed();
                feed.setName(e.getChild("name").getText());
                feed.setBaseUrl(e.getChild("url").getText());
                if (e.getChild("page") != null)
                    feed.setPageUrl(e.getChild("page").getText());
                feed.setQueryString(e.getChild("queryString").getText());
                feed.setSourceType(SourceType.valueOf(e.getChild("type")
                .getAttribute("id").getValue()));
                
                if (e.getChild("selected") != null &&
                        e.getChild("selected").getAttribute("id").getValue().equals("Y"))
                    PhotoFeed.setSelectedFeed(feed);
                feeds.add(feed);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return feeds;
    }
    
    public static void loadFromXML() {
        /* Eventually this will be replaced by context.getWorkingDirectory().
         * For now, this desklet will place files most likely in the home
         * directory
         */
        try {
            // workingDir = dir;
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
                PhotoFeed.createPhotoSources();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void saveToXML() {
        try {
            Document doc = new Document();
            Element root = new Element("sources");
            doc.setRootElement(root);
            for(PhotoFeed f : feeds) {
                Element source = new Element("source");
                source.addContent(new Element("name").setText(f.getName()));
                source.addContent(new Element("url").setText(f.getBaseUrl()));
                source.addContent(new Element("page").setText(f.getPageUrl()));
                source.addContent(new Element("queryString").setText(f.getQueryString()));
                source.addContent(new Element("type").setAttribute(new Attribute("id",f.getSourceType().toString())));
                if (PhotoFeed.getSelectedFeed() == f)
                    source.addContent(new Element("selected").setAttribute(new Attribute("id","Y")));
                root.addContent(source);
            }
            File file = new File(new File(".").getCanonicalPath()+File.separator+"pod.xml");
            u.p(file.toString());
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(doc, new FileWriter(file.getAbsolutePath()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void createPhotoSources() {
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
            u.p(file.toString());
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(doc, new FileWriter(file.getAbsolutePath()));
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getBaseUrl() {
        return url;
    }
    
    public void setBaseUrl(String url) {
        this.url = url;
    }
    
    public String getPageUrl() {
        return webpage;
    }
    
    public void setPageUrl(String webpage) {
        this.webpage = webpage;
    }
    
    public String getQueryString() {
        return queryString;
    }
    
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
    public SourceType getSourceType() {
        return sourceType;
    }
    
    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }
    
    public static PhotoFeed getSelectedFeed() {
        if (selectedFeed == null && feeds.size() > 0) {
            int random = new Random(new java.util.Date().getTime()).nextInt(feeds.size());
            selectedFeed = feeds.get(random);
        }
        return selectedFeed;
    }
    
    public static void setSelectedFeed(PhotoFeed aSelectedFeed) {
        selectedFeed = aSelectedFeed;
    }
}
