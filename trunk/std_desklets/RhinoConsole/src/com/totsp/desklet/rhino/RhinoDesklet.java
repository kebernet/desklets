/*
 * RhinoDesklet.java
 *
 * Created on March 12, 2007, 11:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.totsp.desklet.rhino;

import ab5k.desklet.Desklet;
import ab5k.desklet.DeskletContainer;
import ab5k.desklet.DeskletContext;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import org.mozilla.javascript.tools.shell.ConsoleTextArea;
import org.mozilla.javascript.tools.shell.JSConsole;
import org.mozilla.javascript.tools.shell.Main;

/**
 *
 * @author cooper
 */
public class RhinoDesklet implements Desklet {
    private ConsoleTextArea consoleTextArea;
    Thread t = new Thread(){
        public void run() {
            Main.main(new String[0] );
        }
        
    };
   
    
    private DeskletContext context;
    /** Creates a new instance of RhinoDesklet */
    public RhinoDesklet() {
    }
    
    public void start() throws Exception {
        
    }
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        DeskletContainer container = context.getContainer();
        container.setShaped( false );
        container.setResizable( true );
        consoleTextArea = new ConsoleTextArea(new String[0]);
        JScrollPane scroller = new JScrollPane(consoleTextArea);
        container.setContent(scroller);
        consoleTextArea.setRows(24);
        consoleTextArea.setColumns(80);
        container.setSize( new Dimension(250, 250));
        // System.setIn(consoleTextArea.getIn());
        // System.setOut(consoleTextArea.getOut());
        // System.setErr(consoleTextArea.getErr());
        Main.setIn(consoleTextArea.getIn());
        Main.setOut(consoleTextArea.getOut());
        Main.setErr(consoleTextArea.getErr());
        t.start();
        container.setBackgroundDraggable( false );
        container.setVisible(true);
    }
    
    public void stop() throws Exception {
        t.interrupt();
        context.notifyStopped();
    }
    
    public void destroy() throws Exception {
    }
    
}
