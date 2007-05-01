/*
 * ManagePanel.java
 *
 * Created on July 3, 2006, 1:32 PM
 */

package ab5k;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.TextPainter;
import org.jdesktop.swingx.painter.effects.ShadowPathEffect;

import ab5k.prefs.ConfigurationImportExport;
import ab5k.security.DeskletConfig;
import ab5k.security.DeskletManager;
import ab5k.security.DeskletRunner;
import ab5k.security.Registry;
import ab5k.util.BeanArrayListModel;
import ab5k.util.Bugs;
import ab5k.utils.BusyPainter;

/**
 *
 * @author  joshy
 */
public class ManagePanel extends javax.swing.JPanel {
    Core main;
    DeskletManager manager = DeskletManager.getInstance();
    Registry registry = Registry.getInstance();
    
    /** Creates new form ManagePanel */
    public ManagePanel() {
        initComponents();
    }
    
    public ManagePanel(Core nmain) {
        this();
        this.main = nmain;
        
        BeanArrayListModel model = new BeanArrayListModel( manager.getRunners() );
        desklets.setModel(model);
        
        desklets.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList jList, Object object, int i, boolean b, boolean b0) {
                JLabel label = (JLabel)super.getListCellRendererComponent(jList, object, i, b, b0);
                if(object instanceof DeskletRunner) {
                    DeskletRunner dkl = (DeskletRunner)object;
                    label.setText( dkl.getConfig().getName() + ": " +
                            dkl.getConfig().getDescription());
                } else {
                    label.setText("Non-desklet: " + object.getClass().getName());
                }
                
                return label;
            }
        });
        
        BeanArrayListModel installedModel = new BeanArrayListModel( registry.getDeskletConfigs() );
        installed.setModel( installedModel );
        installed.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList jList, Object object, int i, boolean b, boolean b0) {
                JLabel label = (JLabel)super.getListCellRendererComponent(jList, object, i, b, b0);
                if(object instanceof DeskletConfig) {
                    DeskletConfig dk = (DeskletConfig)object;
                    label.setText( dk.getName() + ": " +
                            dk.getDescription());
                } else {
                    label.setText("Non-desklet: " + object.toString()+ " " + object.getClass().getName());
                }
                
                return label;
            }
        });
    }
    
    private static FileDialog getFileDialog(Component comp) {
        Object frame = SwingUtilities.windowForComponent(comp);
        if(frame instanceof Frame) {
            return new FileDialog((Frame)frame);
        }
        if(frame instanceof Dialog) {
            return new FileDialog((Dialog)frame);
        }
        return null;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jButton5 = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        installed = new javax.swing.JList();
        startButton = new javax.swing.JButton();
        uninstallButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        desklets = new javax.swing.JList();
        stopButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        exportButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();

        jButton5.setText("Download More!");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        addButton.setText("Add...");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setContinuousLayout(true);
        jLabel1.setText("Installed Desklets");

        installed.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(installed);

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        uninstallButton.setText("Uninstall");
        uninstallButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uninstallButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(startButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(uninstallButton)
                .addContainerGap(104, Short.MAX_VALUE))
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
        );

        jPanel2Layout.linkSize(new java.awt.Component[] {startButton, uninstallButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(startButton)
                    .add(uninstallButton)))
        );
        jSplitPane1.setRightComponent(jPanel2);

        desklets.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(desklets);

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Running Desklets");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(stopButton)
                .addContainerGap(216, Short.MAX_VALUE))
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
            .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(stopButton))
        );
        jSplitPane1.setLeftComponent(jPanel1);

        exportButton.setText("Export");
        exportButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exportButtonMouseClicked(evt);
            }
        });

        importButton.setText("Import");
        importButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                importButtonMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jButton5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(addButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 211, Short.MAX_VALUE)
                        .add(importButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(exportButton))
                    .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jButton5)
                        .add(addButton))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(exportButton)
                        .add(importButton)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void importButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_importButtonMouseClicked
        File importFile = openFile(".ab5k");
        ConfigurationImportExport cie = new ConfigurationImportExport();
        try{
            cie.importFromURL(importFile.toURI().toURL());
        } catch(Exception e){
            main.handleError("Problem Importing Configuration",
                    "There was a problem importing your AB5k configuration",
                    e);
        }
    }//GEN-LAST:event_importButtonMouseClicked
    
    private void exportButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportButtonMouseClicked
        File export = saveFile(".ab5k");
        ConfigurationImportExport cie = new ConfigurationImportExport();
        try{
            cie.exportToFile( export );
        } catch(Exception e){
            main.handleError("Problem Exporting Configuration",
                    "There was a problem exporting your AB5k configuration",
                    e);
        }
        
    }//GEN-LAST:event_exportButtonMouseClicked
    
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // add a new desklet from a file on disk
        final File file = openFile();
        if(file == null) { return; }
        
        final BusyGlassPane busyPanel = new BusyGlassPane();
        busyPanel.setText("Loading");
        Window win = SwingUtilities.getWindowAncestor(this);
        //((JDialog)win).setGlassPane(busyPanel);
        // load up the desklet
        new Thread(new Runnable() {
            public void run() {
                try{
                    busyPanel.start();
                    main.getLoadDeskletAction().load(file.toURI().toURL());
                    busyPanel.stop();
                } catch(Exception e){
                    System.out.println("ending the maddness");
                    e.printStackTrace();
                    busyPanel.stop();
                    main.handleError("Problem loading desklet",
                            "There was a problem loading this desklet",
                            e);
                }
            }
        }).start();
        
    }//GEN-LAST:event_addButtonActionPerformed
    
    
    private File openFile() {
        return openFile(null);
    }
    
    private File openFile(final String filter) {
        if(Bugs.isFileDialogUgly()) {
            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(false);
            if(filter != null) {
                jfc.setFileFilter(new FileFilter() {
                    public boolean accept(File f) {
                        if(f.getName().endsWith(filter)){
                            return true;
                        } else {
                            return false;
                        }
                    }

                    public String getDescription() {
                        return filter + " files";
                    }
                });
            }
            jfc.showOpenDialog(this);
            if(jfc.getSelectedFile() == null) {
                return null;
            }
            return jfc.getSelectedFile();
        }else {
            FileDialog fd = getFileDialog(this);
            if(filter != null) {
                fd.setFilenameFilter( new FilenameFilter(){
                    public boolean accept(File file, String string) {
                        return string.endsWith(filter);
                    }
                });
            }
            fd.setVisible(true);
            if(fd.getFile() == null) {
                return null;
            }
            final String file = fd.getDirectory() + File.separator + fd.getFile();
            return new File(file);
        }
    }
    
    private File saveFile(final String filter) {
        FileDialog d = getFileDialog( this );
        d.setMode( d.SAVE );
        if(filter != null) {
            d.setFilenameFilter( new FilenameFilter(){
                public boolean accept(File file, String string) {
                    return string.endsWith(filter);
                }
                
            });
        }
        d.setVisible( true );
        if( d.getFile() == null ){
            return null;
        }
        File export = new File( d.getDirectory() + File.separator + d.getFile());
        return export;
    }
    
    class BusyGlassPane extends JXPanel {
        Animator anim;
        private String text;
        private TextPainter textPainter;
        public void start() {
            anim.start();
        }
        public void stop() {
            anim.stop();
        }
        public BusyGlassPane() {
            setOpaque(false);
            BusyPainter busyPainter = new BusyPainter();
            busyPainter.setBaseColor(new Color(0,0,0,0));
            busyPainter.setBarWidth(26f);
            busyPainter.setBarLength(60f);
            busyPainter.setCenterDistance(90f);
            busyPainter.setPoints(20);
            busyPainter.setTrailLength(18);
            textPainter = new TextPainter("Loading...", new Font(Font.SANS_SERIF,Font.BOLD,60),Color.BLUE);
            textPainter.setAreaEffects(new ShadowPathEffect());
            setBackgroundPainter(new CompoundPainter(
                    new MattePainter(new Color(255,255,255,150)),
                    busyPainter, textPainter));
            anim = PropertySetter.createAnimator(1500,busyPainter,"frame",0,busyPainter.getPoints());
            anim.setRepeatBehavior(Animator.RepeatBehavior.LOOP);
            anim.setRepeatCount(Animator.INFINITE);
            anim.addTarget(new TimingTarget() {
                public void begin() {
                    setVisible(true);
                }
                public void end() {
                    setVisible(false);
                }
                public void repeat() {
                }
                public void timingEvent(float f) {
                    repaint();
                }
            });
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
            textPainter.setText(text);
        }
    }
    
    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        final BusyGlassPane busyPanel = new BusyGlassPane();
        Window win = SwingUtilities.getWindowAncestor(this);
        //((JDialog)win).setGlassPane(busyPanel);
        busyPanel.setText("Stopping");
        
        new Thread(new Runnable() {
            public void run() {
                busyPanel.start();
                Object[] runners = desklets.getSelectedValues();
                for( Object o : runners ){
                    DeskletRunner r = (DeskletRunner) o;
                    manager.shutdownDesklet( r.getConfig().getUUID() );
                }
                busyPanel.stop();
            }
        }).start();
        
    }//GEN-LAST:event_stopButtonActionPerformed
    
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        final BusyGlassPane busyPanel = new BusyGlassPane();
        busyPanel.setText("Loading");
        Window win = SwingUtilities.getWindowAncestor(this);
        //((JDialog)win).setGlassPane(busyPanel);
        new Thread(new Runnable() {
            public void run() {
                busyPanel.start();
                Object[] configs = installed.getSelectedValues();
                for( Object o: configs ){
                    DeskletConfig config = (DeskletConfig) o;
                    try{
                        manager.startDesklet( config.getUUID() );
                    } catch(Exception e){
                        e.printStackTrace();
                        main.handleError("Problem starting desklet",
                                "There was a problem starting this desklet: " + config.getName(),
                                e);
                    }
                }
                busyPanel.stop();
            }
        }).start();
        
    }//GEN-LAST:event_startButtonActionPerformed
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        
        try {
            Desktop dk = Desktop.getDesktop();
            dk.browse(new URI("http://www.ab5k.org/page2/page2.html"));
        } catch (Exception ex) {
            main.handleException(ex);
        }
        
    }//GEN-LAST:event_jButton5ActionPerformed
    
    private void uninstallButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uninstallButtonActionPerformed
        final BusyGlassPane busyPanel = new BusyGlassPane();
        busyPanel.setText("Loading");
        Window win = SwingUtilities.getWindowAncestor(this);
        //((JDialog)win).setGlassPane(busyPanel);
        new Thread(new Runnable() {
            public void run() {
                busyPanel.start();
                Object[] configs = installed.getSelectedValues();
                for( Object o: configs ){
                    DeskletConfig config = (DeskletConfig) o;
                    try{
                        registry.uninstallDesklet( config );
                    } catch(Exception e){
                        main.handleError(
                                "Problem uninstalling desklet",
                                "There was a problem uninstalling this desklet: " + config.getName(),
                                e);
                    }
                }
                busyPanel.stop();
            }
        }).start();
    }//GEN-LAST:event_uninstallButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JList desklets;
    private javax.swing.JButton exportButton;
    private javax.swing.JButton importButton;
    private javax.swing.JList installed;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton uninstallButton;
    // End of variables declaration//GEN-END:variables
    
    
    
}
