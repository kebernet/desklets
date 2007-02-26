package ab5k.actions;

import ab5k.*;
import ab5k.security.DeskletManager;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.joshy.util.u;

// End of variables declaration                   

public class QuitAction extends AbstractAction {

    private Main main;
    public QuitAction(Main main) {
        this.main = main;
    }
    public void actionPerformed(ActionEvent e) {
        main.getFrame().setVisible(false);
        DeskletManager manager = DeskletManager.getInstance();
        manager.shutdown();
        System.exit(0);
    }
}