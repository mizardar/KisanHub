package in.mizardar.kisanhubtest.models;

import java.util.ArrayList;

/**
 * Created by mizardar on 13/03/18.
 */

public class ModelCat {

    private String category;
    private int categoryID;
    private ArrayList<ModelValues> modelValues;

    public ModelCat(String category, int categoryID,ArrayList<ModelValues> modelValues) {
        this.category = category;
        this.categoryID = categoryID;
        this.modelValues = modelValues;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public ArrayList<ModelValues> getModelValues() {
        return modelValues;
    }
}
