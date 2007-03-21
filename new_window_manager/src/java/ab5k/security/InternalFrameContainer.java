/*
 * InternalFrameContainer.java
 *
 * Created on February 22, 2007, 5:24 PM
 */
package ab5k.security;

import ab5k.desklet.DeskletContainer;

import ab5k.util.MoveMouseListener;
import ab5k.wm.WindowManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Dimension2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.joshy.util.u;


/**
 *
 * @author cooper
 */
public class InternalFrameContainer implements DeskletContainer {
    JComponent content;
    JDesktopPane desktop;
    public JInternalFrame iframe = new JInternalFrame() {
        
        protected void paintComponent(Graphics g) {
            //g.setColor(Color.RED);
            //g.fillRect(0,0,getWidth(),getHeight());
            if(!shaped) {
                super.paintComponent(g);
            }
        }
        public void setUI(InternalFrameUI ui) {
            u.p("ui = " + ui);
            super.setUI(ui);
        }
    };
    
    public JPanel panel = new JPanel() {
        protected void paintComponent(Graphics g) {
            //g.setColor(Color.GREEN);
            //g.fillRect(0,0,getWidth(),getHeight());
            if(!shaped) {
                super.paintComponent(g);
            }
        }
    };
    
    MoveMouseListener mml;
    boolean draggable = false;
    boolean shaped = false;
    
    /**
     * Creates a new instance of InternalFrameContainer
     */
    public InternalFrameContainer(String title, WindowManager wm) {
        iframe.setTitle(title);
        iframe.setContentPane(panel);
        mml = new MoveMouseListener(this,wm);
    }
    
    public Dimension2D getSize() {
        return iframe.getSize();
    }
    
    public void setBackgroundDraggable(boolean backgroundDraggable) {
        if(backgroundDraggable == this.draggable) {
            return;
        }
        
        if(backgroundDraggable) {
            panel.addMouseListener(mml);
            panel.addMouseMotionListener(mml);
        } else {
            panel.removeMouseListener(mml);
            panel.removeMouseMotionListener(mml);
        }
    }
    
    public void setContent(JComponent component) {
        if(this.content != null) {
            iframe.remove(this.content);
        }
        
        this.content = component;
        iframe.setLayout(new BorderLayout());
        iframe.add(this.content, "Center");
        iframe.pack();
    }
    
    public void setResizable(boolean resizable) {
        iframe.setResizable(resizable);
    }
    
    public void setShaped(boolean shaped) {
        if(this.shaped == shaped) {
            return;
        }
        
        if(shaped) {
            iframe.setOpaque(false);
            iframe.setClosable(false);
            iframe.setIconifiable(false);
            iframe.setMaximizable(false);
            //panel.setLayout(new BorderLayout());
            //u.p("installing hacked ui");
            iframe.setUI(new HackedInternalFrameUI(iframe));
            panel.setOpaque(false);
            //iframe.setContentPane(panel);
            iframe.setBorder(BorderFactory.createEmptyBorder());
            //iframe.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            
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
    
    public void setSize(Dimension2D size) {
        iframe.setSize(new Dimension((int) size.getWidth(),
                (int) size.getHeight()));
    }
    
    public void setVisible(boolean visible) {
        iframe.setVisible(visible);
    }
    
    class HackedInternalFrameUI extends BasicInternalFrameUI {
        HackedInternalFrameUI(JInternalFrame iframe) {
            super(iframe);
        }
        
        protected void installTitlePane() {
            titlePane = new HackedInternalFrameTitlePane(frame);
            titlePane = null;
        }
        
        private JComponent createDummy() {
            JComponent dummy = new JComponent() {
            };
            
            Dimension size = new Dimension(0,0);
            dummy.setSize(size);
            dummy.setMinimumSize(size);
            dummy.setMaximumSize(size);
            dummy.setPreferredSize(size);
            return dummy;
        }
        protected JComponent createSouthPane(JInternalFrame w) {
            return createDummy();
        }
        protected JComponent createEastPane(JInternalFrame w) {
            return createDummy();
        }
        protected JComponent createWestPane(JInternalFrame w) {
            return createDummy();//new JButton("west");
        }
        
        protected JComponent createNorthPane(JInternalFrame w) {
            return createDummy();
        }
        
        //protected void installComponents() {
        
        //}
        
        public void installUI(JComponent c) {
            super.installUI(c);
            //u.p("installing: " + c);
            c.setOpaque(false);
        }
    }
    
    class HackedInternalFrameTitlePane extends BasicInternalFrameTitlePane {
        HackedInternalFrameTitlePane(JInternalFrame iframe) {
            super(iframe);
        }
        protected void installTitlePane() {
            //titlePane = new HackedInternalFrameTitlePane(frame);
        }
        
        
        protected void addSubComponents() {
            
        }
        
        public void paint(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(0,0,100,100);
        }
        public void paintComponent(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(0,0,10,10);
        }
        
        public void paintTitleBackground(Graphics g) {
            g.setColor(Color.GREEN);
            g.fillRect(0,0,10,10);
        }
        
    }
}
