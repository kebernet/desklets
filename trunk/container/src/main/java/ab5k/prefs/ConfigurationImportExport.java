/*
 * ConfigurationImportExport.java
 *
 * Created on March 13, 2007, 2:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ab5k.prefs;

import ab5k.security.DeskletConfig;
import ab5k.security.DeskletManager;
import ab5k.security.LifeCycleException;
import ab5k.security.Registry;
import com.totsp.util.StreamUtility;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author cooper
 */
public class ConfigurationImportExport {
    
    private static final HashMap<String, File> PROPS_MAP = new HashMap();
    
    /** Creates a new instance of ConfigurationImportExport */
    public ConfigurationImportExport() {
        super();
    }
    
    public void importFromURL( URL source ) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build( source );
        this.installImported( doc );
    }
    
    public void exportToFile( File destination ) throws IOException {
        Document doc = createExport();
        XMLOutputter output = new XMLOutputter();
        FileOutputStream fos = new FileOutputStream( destination );
        output.output( doc, fos );
        fos.flush();
        fos.close();
    }
    
    Document createExport(){
        DeskletManager.getInstance().flushPreferences();
        Document doc = new Document();
        Element root = new Element("configuration");
        HashSet<Entry<String, File>> maps = new HashSet( PROPS_MAP.entrySet());
        for(Entry<String, File> entry: maps ){
            Element tag = new Element( entry.getKey() );
            try{
                String value =
                        StreamUtility.readStreamAsString(
                        new FileInputStream(entry.getValue()) );
                tag.setText( value );
                root.addContent( tag );
            } catch(IOException ioe){
                continue;
            }
        }
        Element desklets = new Element("desklets");
        List<DeskletConfig> configs = Registry.getInstance().getDeskletConfigs();
        for( DeskletConfig config : configs ){
            if( config.getSource() != null || config.getSourceDef() != null ){
                Element desklet = new Element("desklet");
                Element name = new Element("name").setText(config.getName());
                desklet.addContent( name );
                desklet.setAttribute( "id", config.getUUID() );
                if( config.getSource() != null ){
                    Element source = new Element("source");
                    source.setText( config.getSource().toExternalForm());
                    desklet.addContent( source );
                }
                if( config.getSourceDef() != null ){
                    Element sourceDef = new Element("source-def");
                    sourceDef.setText( config.getSourceDef().toExternalForm());
                    desklet.addContent( sourceDef );
                }
                Element prefs = new Element("preferences");
                try{
                    File prefsFile = new File( config.getHomeDir(), "META-INF"+File.separator+"preferences.properties");
                    
                    String content = StreamUtility.readStreamAsString( new FileInputStream( prefsFile ) );
                    prefs.setText( content );
                    desklet.addContent( prefs );
                } catch(IOException ioe ){
                    ioe.printStackTrace();
                    //TODO something here?
                }
                desklets.addContent( desklet );
            }
        }
        root.addContent( desklets );
        doc.setRootElement( root );
        return doc;
    }
    
    void installImported( Document doc ){
        HttpClient client = new HttpClient();
        Element root = doc.getRootElement();
        HashSet<Entry<String, File>> maps = new HashSet( PROPS_MAP.entrySet());
        for(Entry<String, File> entry: maps ){
            Element value = root.getChild( entry.getKey() );
            if( value != null ){
                try{
                    FileOutputStream fos = new FileOutputStream( entry.getValue() );
                    StreamUtility.copyStream( new ByteArrayInputStream( value.getText().getBytes()), fos );
                    fos.flush();
                    fos.close();
                } catch(IOException ioe){
                    //TODO do something here
                    continue;
                }
            }
        }
        List<Element> desklets = root.getChild("desklets").getChildren("desklet");
        Registry reg = Registry.getInstance();
        for( Element desklet : desklets){
            if( reg.getDeskletConfig( desklet.getAttributeValue("id")) != null ){
                continue;
            }
            String installUrl = null;
            if( desklet.getChild("source-def") != null ){
                GetMethod method = new GetMethod(desklet.getChildTextTrim("source-def"));
                SAXBuilder builder = new SAXBuilder();
                try {
                    Document def = builder.build(method.getResponseBodyAsStream());
                    installUrl = def.getRootElement().getChildTextTrim("source");
                } catch(Exception e){
                    //TODO something here.
                    continue;
                }
            }
            if( installUrl == null ){
                installUrl = desklet.getChildTextTrim("source");
            }
            try{
                DeskletConfig config = reg.installDesklet( desklet.getAttributeValue("id"), new URL( installUrl ) );
                String prefs = desklet.getChildText("preferences");
                FileOutputStream fos = new FileOutputStream( new File( config.getHomeDir(), "META-INF"+File.separator+"preferences.properties"));
                StreamUtility.copyStream( new ByteArrayInputStream( prefs.getBytes()), fos );
                fos.flush();
                fos.close();
            } catch(Exception e){
                //TODO
                e.printStackTrace();
                continue;
            }
            
        }
        DeskletManager mgr = DeskletManager.getInstance();
        try{
            mgr.restart();
        } catch(LifeCycleException e){
            //THIS IS REALLY BAD!
            e.printStackTrace();
        }
    }
    
    public static void registerExport(String tagName, File path){
        PROPS_MAP.put( tagName, path );
    }
    
}
