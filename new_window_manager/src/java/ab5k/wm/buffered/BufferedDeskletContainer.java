package ab5k.wm;

import ab5k.desklet.DeskletContainer;
import ab5k.util.MoveMouseListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.joshy.util.u;


public class BufferedDeskletContainer implements DeskletContainer {
    JComponent comp;
    private Point location = new Point(-1, -1);
    Dimension2D size = new Dimension(50, 50);
    
    private boolean draggable = false;
    MoveMouseListener mml;
    
    private JComponent content;
    private BufferedImage buffer;
    private float alpha = 1f;
    private double rotation = 0;
    private double scale = 1.0;
    
    BufferedDeskletContainer(BufferedWM wm) {
        comp = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(Color.GREEN);
                g.fillRect(0,0,getWidth(),getHeight());
                super.paintComponent(g);
            }
        };
        comp.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
            public void mousePressed(MouseEvent e) {
                u.p("comp mouse pressed");
            }
            public void mouseReleased(MouseEvent e) {
            }
        });
        comp.setBorder(BorderFactory.createLineBorder(Color.RED));
        comp.setLayout(new BorderLayout());
        mml = new MoveMouseListener(this, wm);
        setBackgroundDraggable(false);
    }
    
    public Dimension2D getSize() {
        return size;
    }
    
    
    // must rewrite this to use a global mouse since the desklet won't
    // get the mouse events anymore
    public void setBackgroundDraggable(boolean backgroundDraggable) {
        if(backgroundDraggable == this.draggable) {
            return;
        }
        if(backgroundDraggable) {
            comp.addMouseListener(mml);
            comp.addMouseMotionListener(mml);
        } else {
            comp.removeMouseListener(mml);
            comp.removeMouseMotionListener(mml);
        }
    }
    
    public void setContent(JComponent content) {
        if(this.content != null) {
            comp.remove(this.content);
        }
        this.content = content;
        comp.add(content,"Center");
        comp.setSize(comp.getLayout().preferredLayoutSize(comp));
        setSize(comp.getSize());
        //comp.getLayout().layoutContainer(comp);
        //comp.validate();
    }
    
    public void setResizable(boolean b) {
    }
    
    public void setShaped(boolean b) {
    }
    
    public void setSize(Dimension2D dimension2D) {
        comp.setSize((Dimension) dimension2D);
        this.size = dimension2D;
    }
    
    public void setVisible(boolean b) {
    }
    
    public Point getLocation() {
        return location;
    }
    
    public void setLocation(Point point) {
        this.location = point;
    }
    
    public BufferedImage getBuffer() {
        return buffer;
    }
    
    public void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}