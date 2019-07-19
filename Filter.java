package Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rdmcl on 3/21/2018.
 */

public class Filter {
    boolean[] FilterSettings;

    String[] typeData;

    public boolean[] getFilterSettings() {
        return FilterSettings;
    }

    public void setFilterSettings(boolean[] filterSettings) {
        for (int i = 0; i < filterSettings.length; i++) {
            FilterSettings[i] = filterSettings[i];
        }
    }

    public void updateFilter(int position, boolean b) {
        FilterSettings[position] = b;
    }

    public void printFilter() {
        for (int i = 0; i < FilterSettings.length; i++) {
            System.out.println("Position " + i + " is " + FilterSettings[i]);
        }
    }

    public void initTypeData(int size) {
        typeData = new String[size];
    }

    public void setTypeToPosition(String current, int position) {
        typeData[position] = current;
    }

    public void printTypeData(){
        for (int i= 0; i<typeData.length;i++){
            System.out.println("Position: " + i + " is type " + typeData[i]);
        }
    }

    public String[] getTypeData() {
        return typeData;
    }
}
