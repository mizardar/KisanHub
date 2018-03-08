package in.mizardar.kisanhubtest;

import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import static in.mizardar.kisanhubtest.StaticDataList.COUNTRY_LIST;
import static in.mizardar.kisanhubtest.StaticDataList.DATA_URL;
import static in.mizardar.kisanhubtest.StaticDataList.VALUE_LIST;
import static in.mizardar.kisanhubtest.StaticDataList.hasCountryName;
import static in.mizardar.kisanhubtest.StaticDataList.hasValName;

public class WelcomeActivity extends AppCompatActivity {

    private HashMap<String, ModelLink> modelLinkHashMap = new HashMap<>();
    private File fileBasePath;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ContextWrapper wrapper = new ContextWrapper(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        fileBasePath = wrapper.getDir("files", MODE_PRIVATE);

        Log.e("fileBasePath", fileBasePath.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (ConnectionDetector.isConnectingToInternet(this)) {
            new ReadHTML().execute();
        } else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class ReadHTML extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {

            try {

                Document doc = Jsoup.connect(DATA_URL).get();
                Elements links = doc.select("a[href]");

                for (Element link : links) {
                    String linkHref = link.attr("href");
                    String linkTitle = link.attr("title");
                    if (hasCountryName(linkTitle) && hasValName(linkTitle))
                        modelLinkHashMap.put(linkTitle, new ModelLink(linkHref, linkTitle));
                }

                Log.e("size", String.valueOf(modelLinkHashMap.size()));

                new DownloadFileFromURL().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }


    private class DownloadFileFromURL extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            int count = 0;
            try {

                for (String countryName : COUNTRY_LIST) {
                    for (String valueName : VALUE_LIST){

                        ModelLink modelLink = modelLinkHashMap.get(countryName+" "+valueName);

                        String filePath = fileBasePath+"/"+modelLink.get_title()+".txt";
                        File textFile = new File(filePath);
                        if (!textFile.exists()) {
                            textFile.createNewFile();
                        }

//                        URL url = new URL(modelLink.get_href());
//                        URLConnection conection = url.openConnection();
//                        conection.connect();
//                        // getting file length
//                        int lenghtOfFile = conection.getContentLength();
//
//                        // input stream to read file - with 8k buffer
//                        InputStream input = new BufferedInputStream(url.openStream(), 8192);
//
//                        // Output stream to write file
//
//                        OutputStream output = new FileOutputStream(textFile);
//
//                        byte data[] = new byte[1024];
//
//                        long total = 0;
//
//                        while ((count = input.read(data)) != -1) {
//
//                            total += count;
//
//                            // writing data to file
//                            output.write(data, 0, count);
//                        }
//
//                        // flushing output
//                        output.flush();
//
//                        // closing streams
//                        output.close();
//                        input.close();

                        URL url = new URL(modelLink.get_href());

                        URLConnection ucon = url.openConnection();
                        ucon.setReadTimeout(5000);
                        ucon.setConnectTimeout(10000);

                        InputStream is = ucon.getInputStream();
                        BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);



                        FileOutputStream outStream = new FileOutputStream(textFile);
                        byte[] buff = new byte[5 * 1024];

                        int len;
                        while ((len = inStream.read(buff)) != -1)
                        {
                            outStream.write(buff, 0, len);
                        }

                        outStream.flush();
                        outStream.close();
                        inStream.close();
                    }
                }



            } catch (Exception e) {
                Log.d("Error", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Process:","completed");
        }
    }
}
