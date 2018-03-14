package in.mizardar.kisanhubtest.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import in.mizardar.kisanhubtest.R;
import in.mizardar.kisanhubtest.Utils.ConnectionDetector;

public class MyProfileActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        if (ConnectionDetector.isConnectingToInternet(this)){
            webView.loadUrl("http://www.mizardar.in/");
        }else{
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            finish();
        }



    }
}
