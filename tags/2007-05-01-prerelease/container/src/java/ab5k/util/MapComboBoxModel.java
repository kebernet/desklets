package ab5k.util;

import ab5k.*;
import java.util.*;
import java.awt.event.*;
import org.joshy.util.u;

public class MapComboBoxModel extends ListComboBoxModel {

    protected Map map_data;
    protected List index;
    protected List invertedIndex;
    protected boolean inverted;
    
    public MapComboBoxModel() {
        this.map_data = new HashMap();
        index = new ArrayList();
    }
    
    public MapComboBoxModel(Map map) {
        this.map_data = map;
        this.inverted = inverted;
        buildIndex();
        if(index.size() > 0) {
            selected = index.get(0);
        }
    }
    
    protected void buildIndex() {
        invertedIndex = new ArrayList(map_data.values());
        index = new ArrayList(map_data.keySet());
    }


    public Object getElementAt(int i) {
        return index.get(i);
    }
    public int getSize() {
        return map_data.size();
    }
    
    public Map getMap() {
        return map_data;
    }
    
    
    public void actionPerformed(ActionEvent evt) {
        if(evt.getActionCommand().equals("update")) {
            buildIndex();
            fireUpdate();
        }
    }

    
    public Object getValue(Object selectedItem) {
        return map_data.get(selectedItem);
    }
    
    public Object getValue(int selectedItem) {
        return getValue(index.get(selectedItem));
    }
    
}
