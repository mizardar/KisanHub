package in.mizardar.kisanhubtest.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;

import in.mizardar.kisanhubtest.DatabaseHandler;
import in.mizardar.kisanhubtest.R;

public class SelectionActivity extends AppCompatActivity {

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        db = new DatabaseHandler(this);

        AppCompatButton mapActivity = (AppCompatButton) findViewById(R.id.mapActivity);
        AppCompatButton metaofficeData = (AppCompatButton) findViewById(R.id.metaofficeData);

        mapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAccess(101)) {

                    startActivity(new Intent(SelectionActivity.this, MapActivity.class));
                    finish();
                } else {

                    ActivityCompat.requestPermissions(SelectionActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            101);
                }
            }
        });

        metaofficeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAccess(102)) {
                    if (db.getValueCount() > 1) {
                        startActivity(new Intent(SelectionActivity.this, RegionActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SelectionActivity.this, InitializeActivity.class));
                        finish();
                    }
                } else {
                    ActivityCompat.requestPermissions(SelectionActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            102);
                }
            }
        });

//        metaofficeData.performClick();
    }

    public boolean isAccess(int reqID) {
        if (reqID == 102) {
            if (ContextCompat.checkSelfPermission(SelectionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(SelectionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                Log.e("PERMISSION_GRANTED", "false");
                return false;
            } else {
                return true;
            }
        } else if (reqID == 101) {
            if (ContextCompat.checkSelfPermission(SelectionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                Log.e("PERMISSION_GRANTED", "false");
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101:

                Log.e("req", "onRequestPermissionsResult");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        ) {
                    startActivity(new Intent(SelectionActivity.this, MapActivity.class));
                    finish();
                }
                return;
            case 102:

                Log.e("req", "onRequestPermissionsResult");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        ) {
                    startActivity(new Intent(SelectionActivity.this, InitializeActivity.class));
                    finish();
                }
                return;

        }
    }


}
