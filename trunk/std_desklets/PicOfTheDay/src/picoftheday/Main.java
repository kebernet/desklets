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
import java.util.Set;
import javax.imageio.ImageIO;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;
import org.jdesktop.dom.SimpleDocument;
import org.jdesktop.http.Method;
import org.jdesktop.http.async.HtmlHttpRequest;
import org.jdesktop.swingx.JXImageView;
import org.jdesktop.xpath.XPathUtils;
import org.joshy.util.u;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

import java.util.Properties;
/**
 *
 * @author joshua@marinacci.org
 */
public class Main extends AbstractDesklet {
    /* the default framework is embedded*/
    String framework = "embedded";
    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String protocol = "jdbc:derby:";
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
        java.io.File workingDirectory = new java.io.File(".").getCanonicalFile();
        u.p("Working directory: "+ workingDirectory);
        try {
            Class.forName(driver).newInstance();
            Connection conn = null;
            conn = DriverManager.getConnection(protocol + "derbyDB;create=true");
            Statement s = conn.createStatement();
            ResultSet set = s.executeQuery("select * from sources");
            feeds = PhotoFeed.convertToList(set);
        }
        catch(Exception e) {
            createDatabase();
        }
    }
    
    public void createDatabase() {
        try {
            Connection conn = null;
            conn = DriverManager.getConnection(protocol + "derbyDB;create=true");
            Statement s = conn.createStatement();
            
            //Table doesn't exist, create it
            s.execute("create table sources(name varchar(40), " +
                        "url varchar(50), page varchar(25), queryString varchar(50), sourceType varchar(50), selected varchar(1))");
                s.execute("insert into sources values('NASA Photo of the Day'," +
                        "'http://antwrp.gsfc.nasa.gov/apod/','astropix.html','" +
                        "//body/center/p/a/img/@src','WEBPAGE','Y')");
                s.execute("insert into sources values('Earth Science Photo of the Day'," +
                        "'http://epod.usra.edu/','index.php3'," +
                        "'//body/center/a/img/@src','WEBPAGE','N')");
                s.execute("insert into sources values('Flickr Photo Feed'," +
                        "'http://api.flickr.com/services/feeds/','photos_public.gne'," +
                        "'?tags=animal&format=rss_200','RSSMEDIA','N')" );
                u.p("created db.");
                ResultSet set = s.executeQuery("select * from sources");
            feeds = PhotoFeed.convertToList(set);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
