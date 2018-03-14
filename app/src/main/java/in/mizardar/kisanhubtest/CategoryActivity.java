package in.mizardar.kisanhubtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import in.mizardar.kisanhubtest.Utils.StaticDataList;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHandler databaseHandler;
    int regionID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        regionID = getIntent().getIntExtra("RegionID",0);

        AppCompatButton MaxTempBtn,MinTempBtn,MeanTempBtn,SunshineBtn,RainfallBtn;
        MaxTempBtn = (AppCompatButton) findViewById(R.id.MaxTempBtn);
        MinTempBtn = (AppCompatButton) findViewById(R.id.MinTempBtn);
        MeanTempBtn = (AppCompatButton) findViewById(R.id.MeanTempBtn);
        SunshineBtn = (AppCompatButton) findViewById(R.id.SunshineBtn);
        RainfallBtn = (AppCompatButton) findViewById(R.id.RainfallBtn);

        databaseHandler = new DatabaseHandler(this);

        MaxTempBtn.setOnClickListener(this);
        MinTempBtn.setOnClickListener(this);
        MeanTempBtn.setOnClickListener(this);
        SunshineBtn.setOnClickListener(this);
        RainfallBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int catID;

        switch (view.getId()){

            case R.id.MaxTempBtn:
                catID = databaseHandler.getCategoryID(StaticDataList.CATEGORY_LIST[0]);

                goToMainActivity(regionID,catID);
                break;
            case R.id.MinTempBtn:
                catID = databaseHandler.getCategoryID(StaticDataList.CATEGORY_LIST[1]);

                goToMainActivity(regionID,catID);
                break;
            case R.id.MeanTempBtn:
                catID = databaseHandler.getCategoryID(StaticDataList.CATEGORY_LIST[2]);

                goToMainActivity(regionID,catID);
                break;
            case R.id.SunshineBtn:
                catID = databaseHandler.getCategoryID(StaticDataList.CATEGORY_LIST[3]);

                goToMainActivity(regionID,catID);
                break;
            case R.id.RainfallBtn:
                catID = databaseHandler.getCategoryID(StaticDataList.CATEGORY_LIST[4]);

                goToMainActivity(regionID,catID);
                break;
        }
    }

    private void goToMainActivity(int regionID,int catID){
        Intent intent = new Intent(CategoryActivity.this,MainActivity.class);
        intent.putExtra("RegionID",regionID);
        intent.putExtra("CatID",catID);
        startActivity(intent);
    }
}
