package in.mizardar.kisanhubtest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import in.mizardar.kisanhubtest.CategoryActivity;
import in.mizardar.kisanhubtest.DatabaseHandler;
import in.mizardar.kisanhubtest.R;
import in.mizardar.kisanhubtest.Utils.StaticDataList;

public class RegionActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        AppCompatButton UKBtn, EnglandBtn, WalesBtn, ScotlandBtn;
        UKBtn = (AppCompatButton) findViewById(R.id.UKBtn);
        EnglandBtn = (AppCompatButton) findViewById(R.id.EnglandBtn);
        WalesBtn = (AppCompatButton) findViewById(R.id.WalesBtn);
        ScotlandBtn = (AppCompatButton) findViewById(R.id.ScotlandBtn);

        databaseHandler = new DatabaseHandler(this);

        UKBtn.setOnClickListener(this);
        EnglandBtn.setOnClickListener(this);
        WalesBtn.setOnClickListener(this);
        ScotlandBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int regionID = 0;
        //ArrayList<ModelValues> modelValuesArrayList;

        switch (view.getId()) {


            case R.id.UKBtn:
                regionID = databaseHandler.getRegionID(StaticDataList.COUNTRY_LIST[0]);
                goToMainActivity(regionID);
                break;
            case R.id.EnglandBtn:
                regionID = databaseHandler.getRegionID(StaticDataList.COUNTRY_LIST[1]);

                goToMainActivity(regionID);
                break;
            case R.id.WalesBtn:
                regionID = databaseHandler.getRegionID(StaticDataList.COUNTRY_LIST[2]);

                goToMainActivity(regionID);
                break;
            case R.id.ScotlandBtn:
                regionID = databaseHandler.getRegionID(StaticDataList.COUNTRY_LIST[3]);

                goToMainActivity(regionID);
                break;
        }
    }

    private void goToMainActivity(int regionID){
        Intent intent = new Intent(RegionActivity.this, CategoryActivity.class);
        intent.putExtra("RegionID",regionID);
        startActivity(intent);
    }
}
