/*
 * InternalFrameContainer.java
 *
 * Created on February 22, 2007, 5:24 PM
 */

package ab5k.security;

import ab5k.desklet.DeskletContainer;
import ab5k.util.MoveMouseListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Dimension2D;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 *
 * @author cooper
 */
public class InternalFrameContainer implements DeskletContainer {
    
    JDesktopPane desktop;
    JInternalFrame iframe = new JInternalFrame(){
        protected void paintComponent(Graphics g) {
            //g.setColor(Color.RED);
            //g.fillRect(0,0,getWidth(),getHeight());
            if( !shaped ){
                super.paintComponent( g );
            }
        }
    };
    
    JPanel panel = new JPanel(){
        protected void paintComponent(Graphics g) {
            //g.setColor(Color.GREEN);
            //g.fillRect(0,0,getWidth(),getHeight());
            if( !shaped ){
                super.paintComponent( g );
            }
        }
    };
    
    JComponent content;
    MoveMouseListener mml = new MoveMouseListener(panel);
    boolean shaped = false;
    boolean draggable = false;
    /**
     * Creates a new instance of InternalFrameContainer
     */
    public InternalFrameContainer(String title) {
        iframe.setTitle( title );
        iframe.setContentPane( panel );
    }
    
    public void setContent(JComponent component) {
        if( this.content != null ){
            iframe.remove( this.content );
        }
        this.content = component;
        iframe.setLayout(new BorderLayout());
        iframe.add(this.content,"Center");
        iframe.pack();
    }
    
    public void setShaped(boolean shaped) {
        if( this.shaped == shaped )
            return;
        if (shaped ) {
            iframe.setOpaque(false);
            iframe.setClosable(true);
            iframe.setIconifiable(false);
            iframe.setMaximizable(false);
            //panel.setLayout(new BorderLayout());
            panel.setOpaque(false);
            //iframe.setContentPane(panel);
            iframe.setBorder(BorderFactory.createEmptyBorder());
            //panel.setBackground(Color.YELLOW);
        } else {
            iframe.setOpaque(true);
            panel.setOpaque(true);
            iframe.setClosable(false);
            iframe.setIconifiable(false);
            iframe.setMaximizable(false);
        }
        this.shaped = true;
        //iframe.pack();
    }
    
    public void setResizable(boolean resizable) {
        iframe.setResizable( resizable );
    }
    
    public void setBackgroundDraggable(boolean backgroundDraggable) {
        if(  backgroundDraggable == this.draggable )
            return;
        
        if( backgroundDraggable){
            panel.addMouseListener(mml);
            panel.addMouseMotionListener(mml);
        } else {
            panel.removeMouseListener( mml );
            panel.removeMouseMotionListener( mml );
        }
    }
    
    public void setVisible(boolean visible ){
        iframe.setVisible( visible );
    }
    
    public void setSize(Dimension2D size) {
        iframe.setSize( new Dimension( (int)size.getWidth(), (int) size.getHeight() ) );
    }
    
    public Dimension2D getSize() {
        return iframe.getSize();
    }
    
}
