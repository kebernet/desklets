package ab5k.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ab5k.Core;
import ab5k.security.DeskletManager;

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