package Model;

import android.graphics.Color;

import com.example.rdmcl.client.R;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by rdmcl on 3/21/2018.
 */

public class Settings {
    boolean displayLifeStory;
    boolean displayFamilyTree;
    boolean displaySpouseLine;

    int[] LifeStoryColor = {R.color.blue, R.color.white, R.color.orange, R.color.black};
    int[] FamilyTreeColor = {R.color.green, R.color.gray, R.color.purple, R.color.aqua};
    int[] SpouseColor = {R.color.red, R.color.navy, R.color.silver, R.color.lime};
    int[] MapType = {GoogleMap.MAP_TYPE_NORMAL, GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_SATELLITE, GoogleMap.MAP_TYPE_TERRAIN};

    int posLifeStory;
    int posFamilyTree;
    int posSpouse;
    int posMapType;

    public void setUpSettings(){
        displayLifeStory = true;
        displayFamilyTree = true;
        displaySpouseLine = true;
        posLifeStory = 0;
        posFamilyTree = 0;
        posSpouse = 0;
        posMapType = 0;
    }

    public boolean isDisplayLifeStory() {
        return displayLifeStory;
    }

    public void setDisplayLifeStory(boolean displayLifeStory) {
        this.displayLifeStory = displayLifeStory;
    }

    public boolean isDisplayFamilyTree() {
        return displayFamilyTree;
    }

    public void setDisplayFamilyTree(boolean displayFamilyTree) {
        this.displayFamilyTree = displayFamilyTree;
    }

    public boolean isDisplaySpouseLine() {
        return displaySpouseLine;
    }

    public void setDisplaySpouseLine(boolean displaySpouseLine) {
        this.displaySpouseLine = displaySpouseLine;
    }

    public int getLifeStoryColor(int position) {
        return LifeStoryColor[position];
    }

    public int getLifeStoryColor() {
        return LifeStoryColor[posLifeStory];
    }

    public int getFamilyTreeColor(int position) {
        return FamilyTreeColor[position];
    }

    public int getFamilyTreeColor() {
        return FamilyTreeColor[posFamilyTree];
    }

    public int getSpouseColor(int position) {
        return SpouseColor[position];
    }

    public int getSpouseColor() {
        return SpouseColor[posSpouse];
    }

    public int getMapType(int position) {
        return MapType[position];
    }

    public int getMapType() {
        return MapType[posMapType];
    }

    public int getPosLifeStory() {
        return posLifeStory;
    }

    public void setPosLifeStory(int posLifeStory) {
        this.posLifeStory = posLifeStory;
    }

    public int getPosFamilyTree() {
        return posFamilyTree;
    }

    public void setPosFamilyTree(int posFamilyTree) {
        this.posFamilyTree = posFamilyTree;
    }

    public int getPosSpouse() {
        return posSpouse;
    }

    public void setPosSpouse(int posSpouse) {
        this.posSpouse = posSpouse;
    }

    public int getPosMapType() {
        return posMapType;
    }

    public void setPosMapType(int posMapType) {
        this.posMapType = posMapType;
    }
}
