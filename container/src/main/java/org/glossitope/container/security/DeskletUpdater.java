/*
 * DeskletUpdater.java
 *
 * Created on March 10, 2007, 5:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.glossitope.container.security;

import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.totsp.util.StreamUtility;


/**
 *
 * @author cooper
 */
public class DeskletUpdater implements Runnable {
    private static final Logger LOG = Logger.getLogger("org.glossitope");
    private DeskletManager manager;
    private HttpClient client = new HttpClient();
    private Registry registry = Registry.getInstance();
    private SAXBuilder builder = new SAXBuilder();
    private Properties prefs;

    private boolean doUpdates = false;
    /** Creates a new instance of DeskletUpdater */
    public DeskletUpdater(DeskletManager manager, Properties prefs) {
        this.manager = manager;
        this.prefs = prefs;
    }
    
    public void run() {
        List<DeskletConfig> configs = registry.getDeskletConfigs();
        
        for(DeskletConfig config : configs) {
            if(!doUpdates) {
                continue;
            }
            if( prefs.getProperty( config.getUUID()+"-noupgrade", "false").equals("true") ){
                continue;
            }
            if(config.getSourceDef() != null) {
                GetMethod method = new GetMethod(config.getSourceDef()
                .toExternalForm());
                
                try {
                    client.executeMethod(method);
                    
                    Document doc = builder.build(method.getResponseBodyAsStream());
                    String version = doc.getRootElement()
                    .getChildTextTrim("version");
                    
                    if((version != config.getVersion()) &&
                            !config.getVersion().equals(version)) {
                        update(config,
                                doc.getRootElement().getChildText("source"));
                    }
                } catch(JDOMException je) {
                    LOG.log(Level.WARNING,
                            "Exception parsing source-def for " + config.getName(),
                            je);
                } catch(IOException ioe) {
                    LOG.log(Level.WARNING,
                            "Exception reading source-def for " + config.getName(),
                            ioe);
                } finally {
                    method.releaseConnection();
                }
            } else if(config.getSource() != null) {
                HeadMethod method = new HeadMethod(config.getSource()
                .toExternalForm());
                
                try {
                    client.executeMethod(method);
                    
                    Header lastModified = method.getResponseHeader(
                            "Last-Modified");
                    
                    if( lastModified == null ){
                        continue;
                    }
                    DateFormat dtf = new SimpleDateFormat( "EEE, dd MMM yyyy hh:mm:ss z");
                    Date d = dtf.parse(lastModified.getValue() );
                    if( d.getTime() > config.getHomeDir().lastModified() ){
                        update( config, config.getSource().toExternalForm() );
                    }
                } catch(Exception ioe) {
                    LOG.log(Level.WARNING,
                            "Exception getting headers for " + config.getName(), ioe);
                }
            }
        }
    }
    
    private void update(DeskletConfig config, String updateUrl) {
        StringBuffer sb = new StringBuffer();
        sb.append("The Desklet \"");
        sb.append(config.getName());
        sb.append("\" is out of date. Would you like to update now?");
        
        String[] options = { "Yes", "No", "Never" };
        Window win = DeskletManager.main.getFrame();
        int value = JOptionPane.showOptionDialog(win, sb.toString(),
                "Update", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        switch(value) {
            case 0:
                try {
                    manager.shutdownDesklet(config.getUUID());
                } catch(Exception e) {
                    LOG.log(Level.WARNING,
                            "Exception shutting down desklet " + config.getName() +
                            " " + config.getUUID(), e);
                }
                
                GetMethod method = new GetMethod(updateUrl);
                
                try {
                    client.executeMethod(method);
                } catch(IOException ioe) {
                    manager.main.handleError("Error Downloading",
                            "There was an error attempting to download the new " +
                            config.getName() +
                            " desklet version. Update has been aborted.",
                            ioe);
                    return;
                }
                
                File temp = new File(System.getProperty("java.io.tmpdir") +
                        File.separator + System.currentTimeMillis() + ".desklet");
                
                try {
                    FileOutputStream fos = new FileOutputStream(temp);
                    StreamUtility.copyStream(method.getResponseBodyAsStream(), fos);
                } catch(IOException ioe) {
                    manager.main.handleError("Error Downloading",
                            "There was an error downloading the new " + config.getName() + " desklet version. Update has been aborted.",
                            ioe);
                    return;
                }
                
                DeskletConfig newConfig = null;
                
                try {
                    newConfig = registry.installDesklet(temp.toURI().toURL());
                } catch(MalformedURLException mue) {
                    manager.main.handleError("ERROR!",
                            "This should not have happened",
                            mue);
                    return;
                } catch(Exception e) {
                    manager.main.handleError("ERROR!",
                            "There was an error installing the desklet" +
                            config.getName() + ". Update has been aported",
                            e);
                    return;
                }
                try{
                    manager.startDesklet( newConfig.getUUID() );
                } catch(LifeCycleException lce ){
                    manager.main.handleError("ERROR!",
                            "There was an error starting the upgraded desklet" +
                            config.getName() + ". Update has been aported",
                            lce);
                    try{
                        manager.startDesklet( config.getUUID() );
                    } catch(LifeCycleException e){
                        LOG.log(Level.WARNING,
                                "Exception restarting desklet " + config.getName() +
                                " " + config.getUUID(), e);
                    }
                    return;
                    
                }
                try{
                    registry.uninstallDesklet( config.getUUID() );
                } catch( Exception e){
                    LOG.log(Level.WARNING,
                            "Exception uninstalling desklet " + config.getName() +
                            " " + config.getUUID(), e);
                }
                return;
            case 1:
                return;
            case 2:
                prefs.setProperty( config.getUUID()+"-noupgrade", "true");
                return;
        }
    }
}
