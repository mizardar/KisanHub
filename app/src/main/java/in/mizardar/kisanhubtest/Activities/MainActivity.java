package in.mizardar.kisanhubtest.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import in.mizardar.kisanhubtest.R;
import in.mizardar.kisanhubtest.Utils.AdapterValues;
import in.mizardar.kisanhubtest.Utils.DatabaseHandler;
import in.mizardar.kisanhubtest.Utils.StaticDataList;
import in.mizardar.kisanhubtest.models.ModelValues;

public class MainActivity extends AppCompatActivity {

    public static final String[] CATEGORY_LIST_SPINNER = new String[]{"Select a Category", "Max Temp", "Min Temp", "Mean Temp", "Sunshine", "Rainfall"};
    public static final String[] REGION_LIST_SPINNER = new String[]{"Select a Region", "UK", "England", "Wales", "Scotland"};
    HashMap<Integer, ModelValues> modelValuesHashMap;
    HashMap<String, Integer> regionList;
    HashMap<String, Integer> categoryList;
    DatabaseHandler databaseHandler;
    int[] FirstLastYear;
    TextView textView;
    EditText yearEdit;
    ListView listData;
    AppCompatSpinner spinnerRegion, spinnerCategory;
    int regionID = 0;
    int catID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHandler = new DatabaseHandler(this);

        textView = (TextView) findViewById(R.id.textField);
        yearEdit = (EditText) findViewById(R.id.yearText);
        listData = (ListView) findViewById(R.id.listData);
        spinnerRegion = (AppCompatSpinner) findViewById(R.id.spinnerRegion);
        spinnerCategory = (AppCompatSpinner) findViewById(R.id.spinnerCategory);

        textView.setText("Please select region and category");

        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, REGION_LIST_SPINNER);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CATEGORY_LIST_SPINNER);

        spinnerRegion.setAdapter(adapterRegion);
        spinnerCategory.setAdapter(adapterCategory);

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != 0) {
                    String selectedRegion = REGION_LIST_SPINNER[position];
                    regionID = databaseHandler.getRegionID(selectedRegion);
                    initView();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != 0) {
                    String selectedCategory = StaticDataList.CATEGORY_LIST[position - 1];
                    catID = databaseHandler.getCategoryID(selectedCategory);
                    initView();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        yearEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (editable.toString().length() > 0) {
                    int yearVal = Integer.parseInt(editable.toString().trim());

                    if (String.valueOf(yearVal).length() == 4) {
                        if (yearVal < FirstLastYear[0]) {
                            yearEdit.setText(String.valueOf(FirstLastYear[0]));
                            Toast.makeText(MainActivity.this, "Reached minimum value", Toast.LENGTH_SHORT).show();


                        } else if (yearVal > FirstLastYear[1]) {
                            yearEdit.setText(String.valueOf(FirstLastYear[1]));
                            Toast.makeText(MainActivity.this, "Reached maximum value", Toast.LENGTH_SHORT).show();
                        }
                        setListView();
                    }
                }
            }
        });


    }

    private void initView() {
        if (regionID != 0 && catID != 0) {
            modelValuesHashMap = databaseHandler.getValueList(regionID, catID);
            FirstLastYear = databaseHandler.getFirstLastYear(regionID, catID);
            yearEdit.setText(String.valueOf(FirstLastYear[0]));
            yearEdit.setSelection(yearEdit.getText().toString().length());
            textView.setText("Please enter year from " + FirstLastYear[0] + " to " + FirstLastYear[1]);
            setListView();
        } else {
            textView.setText("Please select region and category");
        }


    }

    private void setListView() {

        int yearVal = Integer.parseInt(yearEdit.getText().toString().trim());

        ModelValues modelValues = modelValuesHashMap.get(yearVal);
        AdapterValues adapterValues = new AdapterValues(modelValues.getValues(), MainActivity.this);
        listData.setAdapter(adapterValues);
    }


}
