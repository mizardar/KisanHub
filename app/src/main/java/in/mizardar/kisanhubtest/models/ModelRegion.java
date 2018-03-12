package in.mizardar.kisanhubtest.models;

import java.util.ArrayList;

/**
 * Created by mizardar on 13/03/18.
 */

public class ModelRegion {

    private String region;
    private int regionID;
    private ArrayList<ModelCat> modelCatArrayList;

    public ModelRegion(String region, int regionID,ArrayList<ModelCat> modelCatArrayList) {
        this.region = region;
        this.regionID = regionID;
        this.modelCatArrayList = modelCatArrayList;
    }

    public String getRegion() {
        return region;
    }

    public int getRegionID() {
        return regionID;
    }

    public ArrayList<ModelCat> getModelCatArrayList() {
        return modelCatArrayList;
    }
}
