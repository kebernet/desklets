package ab5k.util;

import ab5k.*;
import com.totsp.util.BeanArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractListModel;
import org.joshy.util.u;

// End of variables declaration                   

public class BeanArrayListModel extends AbstractListModel implements PropertyChangeListener {
    private BeanArrayList bean;
    
    public BeanArrayListModel(BeanArrayList source) {
        this.bean = source;
        bean.addPropertyChangeListener(this);
    }

    public Object getElementAt(int i) {
        return bean.get(i);
        //return this.managePanel.main.desklets.get(i);
    }

    public int getSize() {
        return bean.size();
        //return this.managePanel.main.desklets.size();
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        u.p("got a change event " + evt.getClass().getName());
        u.p("evt = " + evt.getPropertyName());
        u.p(evt.getOldValue() + " ");
        u.p(evt.getNewValue() + " ");
        fireContentsChanged(this,0,bean.size());//this.managePanel.main.desklets.size());
    }
}