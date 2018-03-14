package in.mizardar.kisanhubtest.Activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import in.mizardar.kisanhubtest.R;
import in.mizardar.kisanhubtest.Utils.ConnectionDetector;
import in.mizardar.kisanhubtest.Utils.DatabaseHandler;
import in.mizardar.kisanhubtest.models.ModelCat;
import in.mizardar.kisanhubtest.models.ModelLink;
import in.mizardar.kisanhubtest.models.ModelRegion;
import in.mizardar.kisanhubtest.models.ModelValues;

import static in.mizardar.kisanhubtest.Utils.StaticDataList.CATEGORY_LIST;
import static in.mizardar.kisanhubtest.Utils.StaticDataList.COUNTRY_LIST;
import static in.mizardar.kisanhubtest.Utils.StaticDataList.DATA_URL;
import static in.mizardar.kisanhubtest.Utils.StaticDataList.hasCountryName;
import static in.mizardar.kisanhubtest.Utils.StaticDataList.hasValName;

public class InitializeActivity extends AppCompatActivity {

    DownloadManager downloadManager;
    DatabaseHandler db;
    private HashMap<String, ModelLink> modelLinkHashMap = new HashMap<>();
    private ArrayList<Long> refIdList = new ArrayList<>();
    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {


            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


            Log.e("IN", "" + referenceId);

            refIdList.remove(referenceId);


            if (refIdList.isEmpty()) {


                Log.e("INSIDE", "" + referenceId);
                //parse all downloaded files
                parseDocument();


            }

        }
    };
    private File fileBasePath;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        ContextWrapper wrapper = new ContextWrapper(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        fileBasePath = wrapper.getDir("files", MODE_PRIVATE);

        Log.e("fileBasePath", fileBasePath.toString());

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        db = new DatabaseHandler(this);
        try {

            db.insertRegion();
            db.insertCategory();

            int len1 = db.getRegionCount();
//            int len2 = db.getCategoryCount();
            Log.e("DataBase", "created");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("DataBase", "created");


    }

    @Override
    protected void onDestroy() {


        super.onDestroy();

        unregisterReceiver(onComplete);


    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.e("PERMISSION_GRANTED", "true");
        if (ConnectionDetector.isConnectingToInternet(this)) {
            new ReadHTML().execute();
        } else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(InitializeActivity.this,SelectionActivity.class));
            finish();
        }


    }

    private void parseDocument() {

        ArrayList<ModelRegion> modelRegionArrayList = new ArrayList<>();


        for (String countryName : COUNTRY_LIST) {
            ArrayList<ModelCat> modelCatArrayList = new ArrayList<>();
            for (String valueName : CATEGORY_LIST) {

                ModelLink modelLink = modelLinkHashMap.get(countryName + " " + valueName);
                ArrayList<ModelValues> modelValuesArrayList = new ArrayList<>();


                File myFile = new File(Environment.getExternalStorageDirectory() + "/myfiles", "/metoffice/" + modelLink.get_title() + ".txt");

//                try {
//                    removeLine(myFile);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                if (myFile.exists()) {

                    Log.e("myFile", myFile.toString());
                    try {
                        FileReader input = new FileReader(myFile);
                        BufferedReader bufRead = new BufferedReader(input);
                        String myLine = null;
                        int lineNo = 0;

                        while ((myLine = bufRead.readLine()) != null) {

                            lineNo++;
//                            if (lineNo == 8) {
//
//                            }
                            if (lineNo > 8) {
                                Log.e("parseDocument", "lineNo:" + lineNo + ": " + myLine);

                                ArrayList<String> data = parse(myLine);
                                String[] values = new String[17];
                                int year = Integer.parseInt(data.get(0).trim());

                                for (int valueLoop = 1; valueLoop <= 17; valueLoop++) {
                                    if (data.size() <= valueLoop) {
                                        values[valueLoop - 1] = "NA";
                                    } else {
                                        if (data.get(valueLoop).contains("--")) {
                                            values[valueLoop - 1] = "NA";
                                        } else {
                                            values[valueLoop - 1] = data.get(valueLoop).trim();
                                        }

                                    }
                                }
                                modelValuesArrayList.add(new ModelValues(year, values));
                            }


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                int catID = db.getCategoryID(valueName);
                modelCatArrayList.add(new ModelCat(valueName, catID, modelValuesArrayList));

            }
            int regionID = db.getRegionID(countryName);
            modelRegionArrayList.add(new ModelRegion(countryName, regionID, modelCatArrayList));
        }

        db.insertValues(modelRegionArrayList);

        Log.e("parseDocument", "Completed");

        startActivity(new Intent(InitializeActivity.this, MainActivity.class));
        finish();


    }

    private ArrayList<String> parse(String line) {

        ArrayList<String> data = new ArrayList<>();

        int location = 0;

        int spaceCount = 0;

        StringBuilder str = new StringBuilder("My Array:: ");
        try {

            int len = line.length();

            while (location < line.length()) {
                char character = line.charAt(location);

                if (character == ' ') {
                    spaceCount++;
                    if (spaceCount < 5) {
                        location++;
                    } else {
                        line = line.substring(0, location) + "---" + line.substring(location + 3, len);

                        location = location + 3;
                        spaceCount = 0;
                    }

                } else {
                    spaceCount = 0;
                    location++;
                }
            }
            location = 0;
//            spaceCount = 0;
            while (location < line.length()) {
                char character = line.charAt(location);

                if (character == ' ') {
                    location++;

                } else {
//                    spaceCount = 0;
                    StringBuilder val = new StringBuilder();
                    do {
                        if (location < line.length()) {
                            character = line.charAt(location);
                            val.append(character);
                            location++;
                        }
                    } while (character != ' ' && location < line.length());
                    if (val.toString().contains("--"))
                        data.add("NA");
                    else
                        data.add(val.toString());
                    if (location == 1) {
                        Log.e("last", "string");
                    }

                    str.append(val.toString()).append("**");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("final array", str.toString());

//        if (data.get(0).contains("2018")){
//            Log.e("time","pass");
//        }


        return data;
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(InitializeActivity.this, SelectionActivity.class));
        finish();

    }

    private class ReadHTML extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... voids) {

            int resultCode = 200;
            try {

                Document doc = Jsoup.connect(DATA_URL).get();
                Elements links = doc.select("a[href]");

                Log.e("req", "ReadHTML");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    String linkTitle = link.attr("title");
                    if (hasCountryName(linkTitle) && hasValName(linkTitle))
                        modelLinkHashMap.put(linkTitle, new ModelLink(linkHref, linkTitle));
                }

                Log.e("size", String.valueOf(modelLinkHashMap.size()));

//                new DownloadFileFromURL().execute();

                for (String countryName : COUNTRY_LIST) {
                    for (String valueName : CATEGORY_LIST) {

                        ModelLink modelLink = modelLinkHashMap.get(countryName + " " + valueName);

                        File myFile = new File(Environment.getExternalStorageDirectory() + "/myfiles", "/metoffice/" + modelLink.get_title() + ".txt");

                        if (myFile.exists()) {
                            myFile.delete();
                        }
                        Log.e("Download_Uri", modelLink.get_href());
                        Uri Download_Uri = Uri.parse(modelLink.get_href());
                        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        request.setAllowedOverRoaming(false);
                        request.setTitle(modelLink.get_title());
                        request.setVisibleInDownloadsUi(false);
                        request.setDestinationInExternalPublicDir("myfiles", "/metoffice/" + modelLink.get_title() + ".txt");

                        long refid = downloadManager.enqueue(request);

                        modelLink.setRefID(refid);
                        modelLinkHashMap.put(countryName + " " + valueName, modelLink);
                        refIdList.add(refid);

                    }
                }
            } catch (SocketTimeoutException e) {
                resultCode = 500;
                e.printStackTrace();

            } catch (Exception e) {
                resultCode = 405;
                e.printStackTrace();
            }


            return resultCode;
        }


        @Override
        protected void onPostExecute(Integer result) {

            if (result == 500) {
                Toast.makeText(InitializeActivity.this, "Please connect to an active internet connection", Toast.LENGTH_SHORT).show();
                finish();
            } else if (result == 405) {
                Toast.makeText(InitializeActivity.this, "Error Occured please relaunch the app ", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

