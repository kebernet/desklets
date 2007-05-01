/*
 * RubyDesklet.java
 *
 * Created on March 12, 2007, 9:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.totsp.desklet.ruby;

import ab5k.desklet.Desklet;
import ab5k.desklet.DeskletContainer;
import ab5k.desklet.DeskletContext;
import java.awt.Dimension;

/**
 *
 * @author cooper
 */
public class RubyDesklet implements Desklet{
    
    DeskletContext context;
    /** Creates a new instance of RubyDesklet */
    public RubyDesklet() {
    }
    
    public void init(DeskletContext context) throws Exception {
        this.context = context;
        DeskletContainer container = context.getContainer();
        final IRBConsole console = new IRBConsole();
        container.setShaped( false );
        container.setResizable( true );
        container.setBackgroundDraggable( false );
        container.setContent( console );
        container.setSize( new Dimension( 250, 250 ));
        console.start();
        container.setVisible(true);
    }
    
    public void stop() throws Exception {
        context.notifyStopped();
    }
    
    public void start() throws Exception {
    }
    
    public void destroy() throws Exception {
    }
    
}
