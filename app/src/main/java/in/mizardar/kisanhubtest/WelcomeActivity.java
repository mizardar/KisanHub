package in.mizardar.kisanhubtest;

import android.annotation.SuppressLint;
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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import static in.mizardar.kisanhubtest.StaticDataList.COUNTRY_LIST;
import static in.mizardar.kisanhubtest.StaticDataList.DATA_URL;
import static in.mizardar.kisanhubtest.StaticDataList.VALUE_LIST;
import static in.mizardar.kisanhubtest.StaticDataList.hasCountryName;
import static in.mizardar.kisanhubtest.StaticDataList.hasValName;

public class WelcomeActivity extends AppCompatActivity {

    DownloadManager downloadManager;
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
        setContentView(R.layout.activity_welcome);

        ContextWrapper wrapper = new ContextWrapper(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        fileBasePath = wrapper.getDir("files", MODE_PRIVATE);

        Log.e("fileBasePath", fileBasePath.toString());

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
            finish();
        }


    }

    @SuppressLint("Assert")
    private void removeLine(final File file) throws IOException {
        for (int lineIndex = 0; lineIndex <= 7; lineIndex++) {
            final List<String> lines = new LinkedList<>();
            final Scanner reader = new Scanner(new FileInputStream(file), "UTF-8");
            while (reader.hasNextLine())
                lines.add(reader.nextLine());
            reader.close();
            assert lineIndex >= 0 && lineIndex <= lines.size() - 1;
            lines.remove(lineIndex);
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            for (final String line : lines)
                writer.write(line);
            writer.flush();
            writer.close();
        }
    }

    private void parseDocument() {

        for (String countryName : COUNTRY_LIST) {
            for (String valueName : VALUE_LIST) {

                ModelLink modelLink = modelLinkHashMap.get(countryName + " " + valueName);


                File myFile = new File(Environment.getExternalStorageDirectory() + "/myfiles", "/metoffice/" + modelLink.get_title() + ".txt");

                try {
                    removeLine(myFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (myFile.exists()) {

                    Log.e("myFile", myFile.toString());
                    try {
                        FileReader input = new FileReader(myFile);
                        BufferedReader bufRead = new BufferedReader(input);
                        String myLine = null;
                        int lineNo = 0;
                        while ((myLine = bufRead.readLine()) != null) {

                            lineNo++;
                            if (lineNo > 8) {
                                Log.e("parseDocument", "lineNo:" + lineNo + ": " + myLine);

                                String[] line = myLine.split("\\s\\s\\s");
                                Log.d("parseDocument", ": splitted");
                                //readIt(myLine);
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Log.e("parseDocument", "Completed");
    }

//    public static void main(String[] args) {
//        try {
//
//            final Path path = Paths.get("path", "to", "folder");
//            final Path txt = path.resolve("myFile.txt");
//            final Path csv = path.resolve("myFile.csv");
//            try (
//                    final Stream<String> lines = Files.lines(txt);
//                    final PrintWriter pw = new PrintWriter(Files.newBufferedWriter(csv, StandardOpenOption.CREATE_NEW))) {
//                lines.map((line) -> line.split("\\|")).
//                        map((line) -> Stream.of(line).collect(Collectors.joining(","))).
//                        forEach(pw::println);
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

    public static class convertExcel {

        public void convertToExcel() {


            try {
                Properties prop = new Properties();
                prop.load(new FileInputStream("overlay.properties"));
                String fileName = prop.getProperty("SendMail.File.Attachment");
                String inputFile = prop.getProperty("Excel.File.Input");
                Log.e("Converting FILE  :", inputFile);
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                String[] dataArray = null;
                String delimiter = "\\|";
                String text = null;

                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("overlayReport");
                Map<Integer, String[]> data = new HashMap<Integer, String[]>();

                data.put(1, new String[]{"MASTERMEMRECNO", "MINONMFIRST", "MAXONMFIRST", "MINONMLAST", "MAXONMLAST", "MINSSN", "MAXSSN", "MINDOB",
                        "MAXDOB", "JWFN", "JWLN", "JWMINREV", "JWMAXREV", "JWSSN", "JWDOB", "JWAVG", "DATE_CREATED"});
                int i = 2;
                while ((text = reader.readLine()) != null) {
                    dataArray = text.split(delimiter);

                    data.put(i, new String[]{dataArray[0].trim(), dataArray[1].trim(), dataArray[2].trim(), dataArray[3].trim(),
                            dataArray[4].trim(), dataArray[5].trim(), dataArray[6].trim(), dataArray[7].trim(),
                            dataArray[8].trim(), dataArray[9].trim(), dataArray[10].trim(),
                            dataArray[11].trim(), dataArray[12].trim(), dataArray[13].trim(),
                            dataArray[14].trim(), dataArray[15].trim(), dataArray[16].trim()});
                    i++;

                }


                int rownum = 0;
                for (int k = 1; k < i; k++) {
                    Row row = sheet.createRow(rownum++);
                    String[] strArr = data.get(k);
                    int cellnum = 0;
                    for (Object obj : strArr) {
                        Cell cell = row.createCell(cellnum++);
                        cell.setCellValue((String) obj);
                    }
                }


                FileOutputStream out =
                        new FileOutputStream(new File(fileName));
                workbook.write(out);
                out.close();
                Log.e("excel", "Data is written to Excel file successfully");


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class ReadHTML extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {

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
                    for (String valueName : VALUE_LIST) {

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
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }

}
