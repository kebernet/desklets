/*
 * Desklet.java
 *
 * Created on August 4, 2006, 8:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.rss;

import ab5k.desklet.AbstractDesklet;
import ab5k.desklet.DeskletContext;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import java.awt.BorderLayout;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author cooper
 */
public class Desklet extends AbstractDesklet{
    
    private Display display;
    private boolean running = false;;
    /** Creates a new instance of Desklet */
    public Desklet() {
        super();
    }
    
    public void destroy() throws Exception {
    }
    
    public void start() throws Exception {
        this.running = true;
        while( running ){
            
            // loop for 30 min (30*60 sec), then call update
            for(int i=0; i< 30*60; i++) {
                Thread.sleep(1000);
                if(!running) {
                    System.out.println("returning");
                    context.notifyStopped();
                    return; 
                }
            }
            update();
        }
    }
    
    public void stop() throws Exception {
        System.out.println("RSs Desklet stopping");
        this.running = false;
    }
    
    public void init(DeskletContext context) throws Exception {
        this.display = new Display(this);
        this.update();
        this.context = context;
        context.getContainer().setContent(display);
        context.getContainer().setBackgroundDraggable(true);
        context.getContainer().setShaped(false);
        context.getContainer().setVisible(true);
    }
    
    private void update() throws Exception {
        
        DefaultListModel model = new DefaultListModel();
        WireFeedInput input = new WireFeedInput();
        SyndFeed feed = new SyndFeedImpl( input.build( new InputStreamReader( new URL("http://weblogs.java.net/blog/editors/index.rdf").openStream())));
        for( SyndEntry entry : (List<SyndEntry>) feed.getEntries() ){
            model.addElement( entry);
        }
        this.display.list.setModel(model);
        
    }

    void showURI(String string) {
        try {
            context.showURL(new URI(string));
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
    
}
