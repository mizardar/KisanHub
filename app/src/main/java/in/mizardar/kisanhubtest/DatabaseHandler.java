package in.mizardar.kisanhubtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import in.mizardar.kisanhubtest.Utils.StaticDataList;
import in.mizardar.kisanhubtest.models.ModelCat;
import in.mizardar.kisanhubtest.models.ModelRegion;
import in.mizardar.kisanhubtest.models.ModelValues;

/**
 * Created by mizardar on 13/03/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "kisanhub";
    private static final int DB_VERSION = 1;

    private static final String TABLE_REGION = "RegionMaster";
    private static final String TABLE_CATEGORY = "CategoryMaster";
    private static final String TABLE_VALUES = "ValueMaster";

    private static final String KEY_REGION_NAME = "RegionName";
    private static final String KEY_REGION_ID = "RegionID";

    private static final String KEY_CATEGORY_NAME = "CategoryName";
    private static final String KEY_CATEGORY_ID = "CategoryID";

    private static final String KEY_VALUES_ID = "ValueID";
    private static final String KEY_YEAR = "ValueYear";
    private static final String KEY_JAN = "JANValue";
    private static final String KEY_FEB = "FEBValue";
    private static final String KEY_MAR = "MARValue";
    private static final String KEY_APR = "APRValue";
    private static final String KEY_MAY = "MAYValue";
    private static final String KEY_JUN = "JUNValue";
    private static final String KEY_JUL = "JULValue";
    private static final String KEY_AUG = "AUGValue";
    private static final String KEY_SEP = "SEPValue";
    private static final String KEY_OCT = "OCTValue";
    private static final String KEY_NOV = "NOVValue";
    private static final String KEY_DEC = "DECValue";
    private static final String KEY_WIN = "WINValue";
    private static final String KEY_SPR = "SPRValue";
    private static final String KEY_SUM = "SUMValue";
    private static final String KEY_AUT = "AUTValue";
    private static final String KEY_ANN = "ANNValue";


    private static final String CREATE_TABLE_REGION = "CREATE TABLE " + TABLE_REGION + "("
            + KEY_REGION_ID + " INTEGER PRIMARY KEY NOT NULL , "
            + KEY_REGION_NAME + " TEXT )";

    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "("
            + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY NOT NULL , "
            + KEY_CATEGORY_NAME + " TEXT )";

    private static final String CREATE_TABLE_VALUES = "CREATE TABLE " + TABLE_VALUES + "("
            + KEY_VALUES_ID + " INTEGER PRIMARY KEY NOT NULL , "
            + KEY_REGION_ID + " INTEGER ,"
            + KEY_CATEGORY_ID + " INTEGER ,"
            + KEY_YEAR + " INTEGER ,"
            + KEY_JAN + " TEXT ,"
            + KEY_FEB + " TEXT ,"
            + KEY_MAR + " TEXT ,"
            + KEY_APR + " TEXT ,"
            + KEY_MAY + " TEXT ,"
            + KEY_JUN + " TEXT ,"
            + KEY_JUL + " TEXT ,"
            + KEY_AUG + " TEXT ,"
            + KEY_SEP + " TEXT ,"
            + KEY_OCT + " TEXT ,"
            + KEY_NOV + " TEXT ,"
            + KEY_DEC + " TEXT ,"
            + KEY_WIN + " TEXT ,"
            + KEY_SPR + " TEXT ,"
            + KEY_SUM + " TEXT ,"
            + KEY_AUT + " TEXT ,"
            + KEY_ANN + " TEXT ,"
            + "FOREIGN KEY(" + KEY_REGION_ID + ") REFERENCES "
            + TABLE_REGION + "(" + KEY_REGION_ID + ") , "
            + "FOREIGN KEY(" + KEY_CATEGORY_ID + ") REFERENCES "
            + TABLE_CATEGORY + "(" + KEY_CATEGORY_ID + ")" + ")";


    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE_REGION);
        sqLiteDatabase.execSQL(CREATE_TABLE_CATEGORY);
        sqLiteDatabase.execSQL(CREATE_TABLE_VALUES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void insertRegion() {

        SQLiteDatabase dataBase = this.getWritableDatabase();
        for (String region: StaticDataList.COUNTRY_LIST) {
            if (getRegionCount(region)==0) {
                ContentValues cValues = new ContentValues();
                cValues.put(KEY_REGION_NAME, region);
                //insert data into database
                dataBase.insert(TABLE_REGION, null, cValues);
            }
        }

        dataBase.close();

    }

    public void insertCategory() {

        SQLiteDatabase dataBase = this.getWritableDatabase();
        for (String categoryName:StaticDataList.CATEGORY_LIST) {
            if (getCategoryCount(categoryName)==0) {
                ContentValues cValues = new ContentValues();
                cValues.put(KEY_CATEGORY_NAME, categoryName);
                //insert data into database
                dataBase.insert(TABLE_CATEGORY, null, cValues);
            }
        }

        dataBase.close();

    }

    public int getCategoryCount() {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        //String sql = "select * from " + CATEGORY_TABLE;// + " ORDER BY " + Student_Percentage + " DESC";

        Cursor cursor = dataBase.query(TABLE_CATEGORY,
                new String[]{KEY_CATEGORY_ID, KEY_CATEGORY_NAME},
                null,
                null, null, null, null, null
        );
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getCategoryCount(String catName) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        //String sql = "select * from " + CATEGORY_TABLE;// + " ORDER BY " + Student_Percentage + " DESC";

        Cursor cursor = dataBase.query(TABLE_CATEGORY,
                new String[]{KEY_CATEGORY_ID, KEY_CATEGORY_NAME},
                KEY_CATEGORY_NAME+ "='" +catName+"'",
                null, null, null, null, null
        );
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getRegionCount() {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        //String sql = "select * from " + CATEGORY_TABLE;// + " ORDER BY " + Student_Percentage + " DESC";

        Cursor cursor = dataBase.query(TABLE_REGION,
                new String[]{KEY_REGION_ID, KEY_REGION_NAME},
                null,
                null, null, null, null, null
        );
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getRegionCount(String regionName) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        //String sql = "select * from " + CATEGORY_TABLE;// + " ORDER BY " + Student_Percentage + " DESC";

        Cursor cursor = dataBase.query(TABLE_REGION,
                new String[]{KEY_REGION_ID, KEY_REGION_NAME},
                KEY_REGION_NAME + "='" + regionName+"'",
                null, null, null, null, null
        );

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

 public int getValueCount() {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        //String sql = "select * from " + CATEGORY_TABLE;// + " ORDER BY " + Student_Percentage + " DESC";

        Cursor cursor = dataBase.query(TABLE_VALUES,
                new String[]{KEY_VALUES_ID},
                null,
                null, null, null, null, null
        );

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getRegionID(String regionName) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        //String sql = "select * from " + CATEGORY_TABLE;// + " ORDER BY " + Student_Percentage + " DESC";

        Cursor cursor = dataBase.query(TABLE_REGION,
                new String[]{KEY_REGION_ID, KEY_REGION_NAME},
                KEY_REGION_NAME + "='" + regionName+"'",
                null, null, null, null, null
        );

        cursor.moveToFirst();
        int ID = cursor.getInt(cursor.getColumnIndex(KEY_REGION_ID));
        cursor.close();
        return ID;
    }

    public int getCategoryID(String categoryName) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        //String sql = "select * from " + CATEGORY_TABLE;// + " ORDER BY " + Student_Percentage + " DESC";

        Cursor cursor = dataBase.query(TABLE_CATEGORY,
                new String[]{KEY_CATEGORY_ID, KEY_CATEGORY_NAME},
                KEY_CATEGORY_NAME+ "= '" + categoryName+"'",
                null, null, null, null, null
        );

        cursor.moveToFirst();
        int ID = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID));
        cursor.close();
        return ID;
    }


    public void insertValues(ArrayList<ModelRegion> modelRegionArrayList) {

        SQLiteDatabase dataBase = this.getWritableDatabase();


        for (ModelRegion region:modelRegionArrayList) {
            int regionID = region.getRegionID();
            for (ModelCat modelCat:region.getModelCatArrayList()){
                int catID = modelCat.getCategoryID();

                for (ModelValues modelValues:modelCat.getModelValues()){
                    ContentValues cValues = new ContentValues();
                     cValues.put(KEY_REGION_ID, regionID);
                     cValues.put(KEY_CATEGORY_ID,catID);
                     cValues.put(KEY_YEAR,modelValues.getYear());
                     cValues.put(KEY_JAN,modelValues.getValues()[0]);
                     cValues.put(KEY_FEB,modelValues.getValues()[1]);
                     cValues.put(KEY_MAR,modelValues.getValues()[2]);
                     cValues.put(KEY_APR,modelValues.getValues()[3]);
                     cValues.put(KEY_MAY,modelValues.getValues()[4]);
                     cValues.put(KEY_JUN,modelValues.getValues()[5]);
                     cValues.put(KEY_JUL,modelValues.getValues()[6]);
                     cValues.put(KEY_AUG,modelValues.getValues()[7]);
                     cValues.put(KEY_SEP,modelValues.getValues()[8]);
                     cValues.put(KEY_OCT,modelValues.getValues()[9]);
                     cValues.put(KEY_NOV,modelValues.getValues()[10]);
                     cValues.put(KEY_DEC,modelValues.getValues()[11]);
                     cValues.put(KEY_WIN,modelValues.getValues()[12]);
                     cValues.put(KEY_SPR,modelValues.getValues()[13]);
                     cValues.put(KEY_SUM,modelValues.getValues()[14]);
                     cValues.put(KEY_AUT,modelValues.getValues()[15]);
                     cValues.put(KEY_ANN,modelValues.getValues()[16]);
                    //insert data into database
                    dataBase.insert(TABLE_VALUES, null, cValues);
                }

            }
        }
        dataBase.close();
    }

    public HashMap<Integer, ModelValues> getValueList(int regionID,int catID) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        //String sql = "select * from " + CATEGORY_TABLE;// + " ORDER BY " + Student_Percentage + " DESC";
        HashMap<Integer, ModelValues> modelValuesHashMap = new HashMap<>();


        Cursor cursor = dataBase.query(TABLE_VALUES,
                new String[]{KEY_REGION_ID,KEY_CATEGORY_ID,KEY_YEAR,KEY_JAN,
                        KEY_FEB,KEY_MAR,KEY_APR,KEY_MAY,KEY_JUN,KEY_JUL,
                        KEY_AUG,KEY_SEP,KEY_OCT,KEY_NOV,KEY_DEC,KEY_WIN,
                        KEY_SPR,KEY_SUM,KEY_AUT,KEY_ANN},
                KEY_REGION_ID+ "=" + regionID +" and "+KEY_CATEGORY_ID+"="+catID,
                null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                ModelValues modelValues = new ModelValues();
                String[] val = new String[17];
                int year = cursor.getInt(cursor.getColumnIndex(KEY_YEAR));
                modelValues.setRegionID(cursor.getInt(cursor.getColumnIndex(KEY_REGION_ID)));
                modelValues.setCatID(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)));
                modelValues.setYear(year);
                for (int valueLoop = 0;valueLoop<17;valueLoop++){
                    val[valueLoop] = cursor.getString(valueLoop+3);
                }
                modelValues.setValues(val);
                modelValuesHashMap.put(year,modelValues);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return modelValuesHashMap;
    }

    public int[] getFirstLastYear(int regionID,int catID) {
        SQLiteDatabase dataBase = this.getWritableDatabase();
        //String sql = "select * from " + CATEGORY_TABLE;// + " ORDER BY " + Student_Percentage + " DESC";
        int[] FirstLastYear = new int[2];


        Cursor cursor = dataBase.query(TABLE_VALUES,
                new String[]{KEY_REGION_ID,KEY_CATEGORY_ID,KEY_YEAR,KEY_JAN,
                        KEY_FEB,KEY_MAR,KEY_APR,KEY_MAY,KEY_JUN,KEY_JUL,
                        KEY_AUG,KEY_SEP,KEY_OCT,KEY_NOV,KEY_DEC,KEY_WIN,
                        KEY_SPR,KEY_SUM,KEY_AUT,KEY_ANN},
                KEY_REGION_ID+ "=" + regionID +" and "+KEY_CATEGORY_ID+"="+catID,
                null, null, null, null, null
        );

        cursor.moveToFirst();
        int year1 = cursor.getInt(cursor.getColumnIndex(KEY_YEAR));
        cursor.moveToLast();
        int year2 = cursor.getInt(cursor.getColumnIndex(KEY_YEAR));
        FirstLastYear[0] = year1;
        FirstLastYear[1] = year2;

        cursor.close();
        return FirstLastYear;
    }

}
