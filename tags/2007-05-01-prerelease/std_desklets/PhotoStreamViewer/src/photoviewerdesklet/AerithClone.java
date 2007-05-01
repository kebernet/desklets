package photoviewerdesklet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.jogl.EvaluatorPoint3f;
import org.jdesktop.jogl.FPSText;
import org.jdesktop.jogl.Painter3DSurface;
import org.jdesktop.jogl.Point3f;
import org.jdesktop.jogl.Scene3D;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.RectanglePainter;

public class AerithClone extends JPanel {
    public AerithClone() {
        // set up the scene
        final EvaluatorPoint3f eval = new EvaluatorPoint3f();
        final Scene3D scene = new Scene3D();
        scene.setPreferredSize(new Dimension(650,500));
        //scene.setPreferredSize(new Dimension(1024,768));
        
        
        // set up the shared painters
        RectanglePainter rect = new RectanglePainter(10,10,10,10, 30,30, true,
                Color.BLACK, 5, Color.RED);
        CompoundPainter comp = new CompoundPainter(rect);
        Painter3DSurface surf1 = new Painter3DSurface(300,300);
        surf1.setPainter(comp);
        surf1.setReflectionEnabled(true);
        scene.addSurface(surf1);
        
        Painter3DSurface surf2 = new Painter3DSurface(300,300);
        surf2.setPainter(comp);
        surf2.setReflectionEnabled(true);
        surf2.setScale(0.5f);
        scene.addSurface(surf2);
        
        Painter3DSurface surf3 = new Painter3DSurface(300,300);
        surf3.setPainter(comp);
        surf3.setReflectionEnabled(true);
        surf3.setScale(0.5f);
        //surf3.setYAngle(rightStartAngle);
        scene.addSurface(surf3);
        
        
        // some constants for positions
        Point3f leftStartPos = new Point3f(-10,0,-10);
        Point3f leftOffPos = new Point3f(-40,0,10);
        float leftStartAngle = 20;
        float leftOffAngle = 90;
        float leftScale = 1f;
        
        Point3f rightStartPos = new Point3f(15,-5,-10);
        float rightStartAngle = -30;
        float rightScale = 0.5f;
        
        Point3f rightOffPos = new Point3f(50,-8,-10);
        float rightOffAngle = -90;
        
        
        final Animator anim = new Animator(1000);
        anim.setAcceleration(0.7f);
        anim.setDeceleration(0.3f);
        anim.addTarget( new PropertySetter(surf1, "anchor", eval, leftStartPos, leftOffPos));
        anim.addTarget( new PropertySetter(surf1, "yAngle", leftStartAngle, leftOffAngle) );
        anim.addTarget( new PropertySetter(surf2, "anchor", eval, rightStartPos, leftStartPos));
        anim.addTarget( new PropertySetter(surf2, "yAngle", rightStartAngle, leftStartAngle) );
        anim.addTarget( new PropertySetter(surf2, "scale", 0.5f, 1f));
        anim.addTarget( new PropertySetter(surf3, "anchor", eval, rightOffPos, rightStartPos));
        anim.addTarget( new PropertySetter(surf3, "yAngle", rightOffAngle, rightStartAngle) );
        
        anim.setRepeatBehavior(Animator.RepeatBehavior.REVERSE);
        anim.setEndBehavior(Animator.EndBehavior.HOLD);
        //anim.setRepeatCount(Animator.INFINITE);
        
        scene.addSurface(new FPSText());
        // drop this all into a frame
        this.setLayout(new BorderLayout());
        this.add(scene,"Center");
        
        //scene.setLayout(new BorderLayout());
        
        final JButton leftButton = new JButton("<");
        final JButton rightButton = new JButton(">");
        leftButton.setOpaque(false);
        rightButton.setOpaque(false);
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(leftButton);
        panel.add(rightButton);
        this.add(panel,"South");
        
        anim.addTarget(new TimingTarget() {
            public void begin() {
                leftButton.setEnabled(false);
                rightButton.setEnabled(false);
            }
            public void end() {
                leftButton.setEnabled(true);
                rightButton.setEnabled(true);
            }
            public void repeat() {  }
            public void timingEvent(float fraction) {    }
        });
        
        leftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anim.setInitialFraction(1.0f);
                anim.setDirection(Animator.Direction.BACKWARD);
                anim.start();
            }
        });
        
        rightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                anim.setInitialFraction(0.0f);
                anim.setDirection(Animator.Direction.FORWARD);
                anim.start();
            }
        });
        
        /*
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        scene.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
         */
        scene.start();
    }
    
}
