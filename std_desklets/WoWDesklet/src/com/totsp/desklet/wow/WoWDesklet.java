/*
 * WoWDesklet.java
 *
 * Created on February 19, 2007, 6:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.totsp.desklet.wow;

import org.glossitope.desklet.Desklet;
import org.glossitope.desklet.DeskletContainer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import org.glossitope.desklet.test.DeskletTester;

/**
 *
 * @author cooper
 */
public class WoWDesklet extends Desklet {
    
    public static void main(String ... args) {
        DeskletTester.start(WoWDesklet.class);
    }
    
    private boolean up = false;
    private MainForm form = new MainForm();
    private Dock dock = new Dock();
    private String serverName;
    private Pattern p;
    private Timer t = new Timer(true);
    private String statusPage;
    private TimerTask task = new TimerTask(){
        public void run() {
            try{
                statusPage = StreamUtility.readStreamAsString( new URL( "http://www.worldofwarcraft.com/realmstatus/compat.html").openStream());
                update();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    };
    
    private boolean hardCoded = true;
    /** Creates a new instance of WoWDesklet */
    public WoWDesklet() {
    }
    
    private void update(){
        if(hardCoded) {
            form.serverStatus.setIcon( new ImageIcon( this.getClass().getResource("/com/totsp/desklet/wow/uparrow.gif")) );
            dock.server.setIcon(new ImageIcon( this.getClass().getResource("/com/totsp/desklet/wow/uparrow.gif")) );
            dock.server.setText( this.serverName );
            return;
        }
        Matcher m = p.matcher( statusPage );
        m.find();
        String status = m.group();
        
        if(status.startsWith("up")) {
            form.serverStatus.setIcon( new ImageIcon( this.getClass().getResource("/com/totsp/desklet/wow/uparrow.gif")) );
            dock.server.setIcon(new ImageIcon( this.getClass().getResource("/com/totsp/desklet/wow/uparrow.gif")) );
            dock.server.setText( this.serverName );
            
        } else {
            form.serverStatus.setIcon( new ImageIcon( this.getClass().getResource("/com/totsp/desklet/wow/downarrow.gif")) );
            dock.server.setIcon(new ImageIcon( this.getClass().getResource("/com/totsp/desklet/wow/downarrow.gif") ) );
            dock.server.setText( this.serverName );
        }
    }
    public void stop() throws Exception {
        t.cancel();
        getContext().notifyStopped();
        
    }
    
    public boolean isShaped() {
        return true;
    }
    
    public void start() throws Exception {
        if(!hardCoded) {
            t.schedule( task, 5 * 60 * 1000, 5 * 60 * 1000);
        }
    }
    
    public boolean isResizable() {
        return false;
    }
    
    public boolean isBackgroundMovable() {
        return true;
    }
    
    public void init() throws Exception {
        DeskletContainer container = getContext().getContainer();
        container.setShaped( true );
        container.setBackgroundDraggable( true );
        container.setContent( form );
        container.setVisible(true);
        DeskletContainer dock = getContext().getDockingContainer();
        dock.setShaped( true );
        dock.setContent( this.dock );
        dock.setVisible( true );
        
        serverName = getContext().getPreference( "serverName", "Blackhand");
        if(hardCoded) {
            form.serverName.removeAllItems();
            form.serverName.addItem("Blackhand");
            form.serverName.addItem("Blackrock");
            form.serverName.addItem("Blackwing Lair");
            form.serverName.setSelectedIndex(0);
        } else {
            p = Pattern.compile( "(up|down)arrow\\.gif\" [^>]+><\\/td>\\s+<td [^>]+><b [^>]+>[A-Za-z\\s]+", Pattern.MULTILINE);
            statusPage = StreamUtility.readStreamAsString( new URL( "http://www.worldofwarcraft.com/realmstatus/compat.html").openStream());
            Matcher m = p.matcher( statusPage );
            form.serverName.removeAllItems();
            for( int i=0; m.find(); i++ ){
                String serverLine = m.group();
                String name = serverLine.substring( serverLine.lastIndexOf(">") +1 , serverLine.length());
                form.serverName.addItem( name );
                if( name.equals(serverName)){
                    form.serverName.setSelectedIndex(i);
                }
            }
        }
        form.serverName.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                serverName = (String) form.serverName.getSelectedItem();
                getContext().setPreference( "serverName", serverName);
                task.run();
            }
            
        });
        update();
        
    }
    
    public void destroy() throws Exception {
    }
    
    public boolean isDockable() {
        return false;
    }
    
}
