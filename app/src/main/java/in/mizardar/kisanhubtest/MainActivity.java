package in.mizardar.kisanhubtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import in.mizardar.kisanhubtest.models.ModelValues;

public class MainActivity extends AppCompatActivity {

    HashMap<Integer, ModelValues> modelValuesHashMap;
    DatabaseHandler databaseHandler;
    int[] FirstLastYear;

    TextView textView;
    EditText yearEdit;
    ListView listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHandler = new DatabaseHandler(this);

        textView = (TextView) findViewById(R.id.textField);
        yearEdit = (EditText) findViewById(R.id.yearText);
        listData = (ListView) findViewById(R.id.listData);

        int regionID = getIntent().getIntExtra("RegionID", 0);
        int catID = getIntent().getIntExtra("CatID", 0);

        modelValuesHashMap = databaseHandler.getValueList(regionID, catID);
        FirstLastYear = databaseHandler.getFirstLastYear(regionID, catID);
        textView.setText("Please enter year from " + FirstLastYear[0] + " to " + FirstLastYear[1]);



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
                        if (yearVal < FirstLastYear[0]){
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

        yearEdit.setText(String.valueOf(FirstLastYear[0]));
    }

    private void setListView() {
        int yearVal = Integer.parseInt(yearEdit.getText().toString().trim());

        ModelValues modelValues = modelValuesHashMap.get(yearVal);
        AdapterValues adapterValues = new AdapterValues(modelValues.getValues(), MainActivity.this);
        listData.setAdapter(adapterValues);
    }
}
