package org.glossitope.container.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.glossitope.container.Core;
import org.glossitope.container.security.DeskletManager;

// End of variables declaration                   

public class QuitAction extends AbstractAction {

    private Core main;
    public QuitAction(Core main) {
        this.main = main;
    }
    public void actionPerformed(ActionEvent e) {
        main.getFrame().setVisible(false);
        DeskletManager manager = DeskletManager.getInstance();
        manager.shutdown();
        System.exit(0);
    }
}