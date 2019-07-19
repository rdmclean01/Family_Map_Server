package Handler;

/**
 * This class exists as a wrapper to grab the data from the locations.json file
 */

public class FillLocations {
    FillLocationData[] data;

    public FillLocations() {
    }

    public FillLocationData[] getData() {
        return data;
    }

    public void setData(FillLocationData[] data) {
        this.data = data;
    }
}
