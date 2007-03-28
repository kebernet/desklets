/*
 * RSSPhotoFeed.java
 *
 * Created on March 23, 2007, 2:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.util.Properties;
/**
 *
 * @author joshy, jwill
 */
public class PhotoFeed {
    enum SourceType { WEBPAGE, RSSMEDIA,RSSENC}
    public Properties props;
    private String name;
    private String url;
    private String webpage;
    private String queryString;
    private SourceType sourceType;
    private static PhotoFeed selectedFeed;
    List<URI> urls;
    
    /** Creates a new instance of PhotoFeed */
    public PhotoFeed() {
        
    }
    
    public void prepareURLS() {
        urls = new ArrayList<URI>();
        try {
            String query = url+webpage+"?"+queryString;
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
            }
            else {
                for(SyndEntry en : (List<SyndEntry>)feed.getEntries()) {
                    SyndEnclosure enclosure = (SyndEnclosure)en.getEnclosures().get(0);
                    u.p("got enclosure: "+ enclosure.getUrl() );
                    urls.add(new URI(enclosure.getUrl()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    public Image pickRandom() {
        this.prepareURLS();
        u.p(urls.size());
        int random = new Random(new java.util.Date().getTime()).nextInt(urls.size());
        URI uri = urls.get(random);
        u.p(urls.get(random));
        
        Image img = null;
        try {
            img = ImageIO.read(uri.toURL());
            img = img.getScaledInstance(300,300,Image.SCALE_SMOOTH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return img;
    }
    
    public static ArrayList<PhotoFeed> convertToList(ResultSet set) {
        ArrayList<PhotoFeed> feeds = new ArrayList<PhotoFeed>();
        try {
        while (set.next()) {
            PhotoFeed feed = new PhotoFeed();
            feed.setName(set.getString(1));
            feed.setBaseUrl(set.getString(2));
            feed.setPageUrl(set.getString(3));
            feed.setQueryString(set.getString(4));
            feed.setSourceType(SourceType.valueOf(set.getString(5)));
            String x = set.getString(6);
            if (set.getString(6).equals("Y"))
                PhotoFeed.setSelectedFeed(feed);
            feeds.add(feed);
        }
        }
        catch (Exception e) {
            
        }
        return feeds;
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
        if (selectedFeed == null) {
            int random = new Random(new java.util.Date().getTime()).nextInt(Main.feeds.size());
            selectedFeed = Main.feeds.get(random);
        }
        
        return selectedFeed;
    }

    public static void setSelectedFeed(PhotoFeed aSelectedFeed) {
        selectedFeed = aSelectedFeed;
    }
}
