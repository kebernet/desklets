package ab5k.desklet.test;

import ab5k.desklet.Desklet;
import ab5k.desklet.DeskletContext;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class DeskletTester {
    public static void start(final Class clss) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                final JFrame dockingFrame = new JFrame();
                try {
                    DeskletContext ctx = new TestDeskletContext(frame,dockingFrame);
                    final Desklet desklet = (Desklet) clss.newInstance();
                    frame.addWindowListener(new WindowListener() {
                        public void windowActivated(WindowEvent e) {
                        }
                        public void windowClosed(WindowEvent e) {
                        }
                        public void windowClosing(WindowEvent e) {
                            System.out.println("closing");
                            try {
                                desklet.stop();
                                System.exit(0);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        public void windowDeactivated(WindowEvent e) {
                        }
                        public void windowDeiconified(WindowEvent e) {
                        }
                        public void windowIconified(WindowEvent e) {
                        }
                        public void windowOpened(WindowEvent e) {
                        }
                    });
                    desklet.setContext(ctx);
                    desklet.init();
                    frame.pack();
                    frame.setVisible(true);
                    desklet.getContext().getContainer().getContent().requestFocusInWindow();
                    desklet.start();
                    dockingFrame.pack();
                    dockingFrame.setVisible(true);
                }  catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}