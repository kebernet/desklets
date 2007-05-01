/*
 * Desklet.java
 *
 * Created on March 2, 2007, 9:22 PM
 *
 */

package com.totsp.desklet.rome;

import ab5k.desklet.Desklet;
import ab5k.desklet.DeskletContainer;
import ab5k.desklet.DeskletContext;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;

import org.w3c.tidy.Tidy;

/**
 *
 * @author cooper
 */
public class RomeDesklet extends Desklet{
    
    private MainForm form = new MainForm();
    SyndFeedInput input = new SyndFeedInput();
    SyndFeed feed = null;
    String feedUrl = "http://feeds.feedburner.com/screaming-penguin";
    int itemIndex =0;
    boolean paused = false;
    boolean step = false;
    Timer timer = new Timer(true);
    TimerTask fetch = new TimerTask(){
        public void run() {
            try{
                feed = input.build( new InputStreamReader( new URL( feedUrl).openStream()));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        
    };
    
    TimerTask scroll = new TimerTask(){
        public void run() {
            if( !step && (feed == null || paused ) ) return;
            
            int previous = (itemIndex - 1 < 0) ? feed.getEntries().size() + (itemIndex - 1) : itemIndex - 1;
            int prevprev =  (itemIndex - 2 < 0) ? feed.getEntries().size() + (itemIndex - 2) : itemIndex - 2;
            form.now2.setText( trim( ((SyndEntry) feed.getEntries().get(prevprev)).getTitle()) );
            form.now1.setText( trim( ((SyndEntry) feed.getEntries().get(previous)).getTitle()) );
            form.now.setText( trim(((SyndEntry) feed.getEntries().get(itemIndex)).getTitle()) );
            try{
                Document dom = jTidyParse(  new ByteArrayInputStream(((SyndEntry) feed.getEntries().get(itemIndex)).getDescription().getValue().getBytes()), new ByteArrayOutputStream() );
                form.display.setDocument( dom );
            }catch(Exception e){
                e.printStackTrace();
            }
            itemIndex++;
            if( itemIndex >= feed.getEntries().size() ){
                itemIndex = 0;
            }
        }
        
    };
    
    
    private String trim(String value ){
        if( value.length() > 33)
            return value.substring( 0, 28) +"...";
        else
            return value;
    }
    /** Creates a new instance of Desklet */
    public RomeDesklet() {
        super();
    }
    
    public void destroy() throws Exception {
    }
    
    public void stop() throws Exception {
        form.display.shutdown();
        timer.cancel();
        getContext().notifyStopped();
    }
    
    public void start() throws Exception {
        timer.schedule( fetch, 0, 10 * 60 * 1000 );
        timer.schedule( scroll, 2000, 7000 );
        form.browse.addMouseListener( new MouseListener(){
            public void mouseReleased(MouseEvent mouseEvent) {
            }
            
            public void mousePressed(MouseEvent mouseEvent) {
            }
            
            public void mouseExited(MouseEvent mouseEvent) {
            }
            
            public void mouseEntered(MouseEvent mouseEvent) {
            }
            
            public void mouseClicked(MouseEvent mouseEvent) {
                try{
                    int index = itemIndex -1;
                    if( index < 0 ) index = feed.getEntries().size() - 2;
                    getContext().showURL( new URI( ((SyndEntry)feed.getEntries().get( index)).getLink() ));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            
        });
        form.feed.addMouseListener(new MouseListener(){
            public void mouseReleased( MouseEvent mouseEvent) {
            }
            
            public void mousePressed(MouseEvent mouseEvent) {
            }
            
            public void mouseExited(MouseEvent mouseEvent) {
            }
            
            public void mouseEntered(MouseEvent mouseEvent) {
            }
            
            public void mouseClicked(MouseEvent mouseEvent) {
                String newFeedUrl = JOptionPane.showInputDialog( form, "Feed URL:", feedUrl );
                if( newFeedUrl != null && newFeedUrl.length() > 0 ){
                    feedUrl = newFeedUrl;
                    getContext().setPreference( "feed.url", feedUrl );
                    fetch.run();
                }
            }
            
        });
        
        form.pause.addMouseListener(new MouseListener(){
            public void mouseReleased( MouseEvent mouseEvent) {
            }
            
            public void mousePressed(MouseEvent mouseEvent) {
            }
            
            public void mouseExited(MouseEvent mouseEvent) {
            }
            
            public void mouseEntered(MouseEvent mouseEvent) {
            }
            
            public void mouseClicked(MouseEvent mouseEvent) {
                paused = !paused;
            }
        });
        form.back.addMouseListener(new MouseListener(){
            public void mouseReleased( MouseEvent mouseEvent) {
            }
            
            public void mousePressed(MouseEvent mouseEvent) {
            }
            
            public void mouseExited(MouseEvent mouseEvent) {
            }
            
            public void mouseEntered(MouseEvent mouseEvent) {
            }
            
            public void mouseClicked(MouseEvent mouseEvent) {
                itemIndex = itemIndex -2 ;
                if( itemIndex < 0 ){
                    itemIndex = feed.getEntries().size() -1 - itemIndex;
                }
                step = true;
                scroll.run();
                step = false;
            }
        });
        form.forward.addMouseListener(new MouseListener(){
            public void mouseReleased( MouseEvent mouseEvent) {
            }
            
            public void mousePressed(MouseEvent mouseEvent) {
            }
            
            public void mouseExited(MouseEvent mouseEvent) {
            }
            
            public void mouseEntered(MouseEvent mouseEvent) {
            }
            
            public void mouseClicked(MouseEvent mouseEvent) {
                step = true;
                scroll.run();
                step = false;
            }
        });
        
    }
    
    public void init() throws Exception {
        DeskletContainer container = getContext().getContainer();
        container.setShaped( true );
        container.setResizable( false);
        container.setContent( form );
        container.setVisible(true);
        container.setBackgroundDraggable(true);
        feedUrl = getContext().getPreference( "feed.url", "http://feeds.feedburner.com/screaming-penguin");
    }
    
    /**
     * Parses a HTML document with jTidy and writes and XHTML document
     * @return Document DOM of the HTML file.
     * @param destination OutputStream to write pretty printed HTML to
     * @param source InputStream to read from
     * @throws java.io.IOException
     */
    public Document jTidyParse(InputStream source,OutputStream destination) throws java.io.IOException {
        Properties config = new Properties();
        config.load(getClass().getResourceAsStream("/com/totsp/conf/jtidy.properties"));
        
        Tidy tidy = new Tidy();
        tidy.setConfigurationFromProps(config);
        tidy.setQuoteAmpersand(true);
        tidy.setXmlOut(true);
        
        tidy.setXHTML(true);
        
        return tidy.parseDOM(source,destination);
    }
    
    
    
}
