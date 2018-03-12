package in.mizardar.kisanhubtest.Utils;

/**
 * Created by mizardar on 08/03/18.
 */

public class StaticDataList {

    public static final String DATA_URL = "https://www.metoffice.gov.uk/climate/uk/summaries/datasets#yearOrdered";
    public static final String[] COUNTRY_LIST = new String[]{"UK","England","Wales","Scotland"};
    public static final String[] VALUE_LIST = new String[]{"Date Tmax","Date Tmin","Date Tmean","Date Sunshine","Date Rainfall"};

    public static boolean hasCountryName(String title) {
        return title.contains("UK") || title.contains("England") || title.contains("Wales") || title.contains("Scotland");
    }

    public static boolean hasValName(String title) {
        return title.contains("Date Tmax") || title.contains("Date Tmin") || title.contains("Date Tmean")
                || title.contains("Date Sunshine") || title.contains("Date Rainfall");
    }
}
