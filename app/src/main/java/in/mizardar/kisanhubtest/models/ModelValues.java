package in.mizardar.kisanhubtest.models;

/**
 * Created by mizardar on 13/03/18.
 */

public class ModelValues {

    private int year;
    private double[] values = new double[17];

    public ModelValues(int year, double[] values) {
        this.year = year;
        this.values = values;
    }

    public int getYear() {
        return year;
    }

    public double[] getValues() {
        return values;
    }


}
