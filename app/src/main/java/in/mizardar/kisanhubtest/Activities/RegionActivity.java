package in.mizardar.kisanhubtest.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

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

        switch (view.getId()) {


            case R.id.UKBtn:
                regionID = databaseHandler.getRegionID(StaticDataList.COUNTRY_LIST[0]);

                break;
            case R.id.EnglandBtn:
                regionID = databaseHandler.getRegionID(StaticDataList.COUNTRY_LIST[0]);
                break;
            case R.id.WalesBtn:
                regionID = databaseHandler.getRegionID(StaticDataList.COUNTRY_LIST[0]);
                break;
            case R.id.ScotlandBtn:
                regionID = databaseHandler.getRegionID(StaticDataList.COUNTRY_LIST[0]);
                break;

        }
    }
}
