package in.mizardar.kisanhubtest.Activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.mizardar.kisanhubtest.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DrawGeojsonLineActivity";
    private MapView mapView;
    private MapboxMap mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.accessToken));
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        // Load and Draw the GeoJSON
        new DrawGeoJson().execute();
    }


    private class FieldsData {
        ArrayList<LatLng> points;
        String field_name;
        String farm_name;

        public ArrayList<LatLng> getPoints() {
            return points;
        }

        public void setPoints(ArrayList<LatLng> points) {
            this.points = points;
        }

        public String getField_name() {
            return field_name;
        }

        public void setField_name(String field_name) {
            this.field_name = field_name;
        }

        public String getFarm_name() {
            return farm_name;
        }

        public void setFarm_name(String farm_name) {
            this.farm_name = farm_name;
        }
    }


    private class DrawGeoJson extends AsyncTask<Void, Void, List<FieldsData>> {

        List<BaseMarkerOptions> markersArrayList = new ArrayList<>();


        @Override
        protected List<FieldsData> doInBackground(Void... voids) {

            ArrayList<FieldsData> fieldsDataArrayList = new ArrayList<>();


            try {
                // Load GeoJSON file


                String farmsFields = null;
                try {
                    InputStream is = getAssets().open("farms.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    farmsFields = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Parse JSON
                JSONArray json = new JSONArray(farmsFields);
                JSONObject jsonobject = json.getJSONObject(0);
                JSONArray farms = jsonobject.getJSONArray("farms");
                JSONArray fields = jsonobject.getJSONArray("fields");
                for (int farmsLoop = 0; farmsLoop < farms.length(); farmsLoop++) {
                    JSONObject feature = farms.getJSONObject(farmsLoop);

                    JSONObject geometry = feature.getJSONObject("geometry");
                    if (geometry != null) {
                        String type = geometry.getString("type");

                        if (type.equalsIgnoreCase("point")) {
                            JSONArray coordinates = geometry.getJSONArray("coordinates");
                            double lng = coordinates.getDouble(0);
                            double lat = coordinates.getDouble(1);
                            LatLng latLng = new LatLng(lat, lng);
                            String farm_name = feature.getJSONObject("properties").getString("farm_name");
                            String farm_location = feature.getJSONObject("properties").getString("farm_location");
                            String text = farm_name + "," + farm_location;

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.setPosition(latLng);
                            markerOptions.setTitle(text);

//                            Marker marker = new Marker(latLng, null,text,"");
//                            Markers markers = new Markers(, latLng);
                            markersArrayList.add(markerOptions);

                        }
                        // Our GeoJSON only has one feature: a line string

                    }
                }
                for (int fieldsLoop = 0; fieldsLoop < fields.length(); fieldsLoop++) {
                    JSONObject fieldsObject = fields.getJSONObject(fieldsLoop);
                    ArrayList<LatLng> points = new ArrayList<>();

                    JSONObject properties = fieldsObject.getJSONObject("properties");
                    String farm_name = properties.getString("farm_name");
                    String field_name = properties.getString("field_name");

                    FieldsData fieldsData = new FieldsData();
                    fieldsData.setFarm_name(farm_name);
                    fieldsData.setField_name(field_name);

                    JSONObject geometry = fieldsObject.getJSONObject("geometry");
                    if (geometry != null) {
                        String type = geometry.getString("type");

                        if (type.equalsIgnoreCase("polygon")) {
                            JSONArray coordinates = geometry.getJSONArray("coordinates");
                            JSONArray coordinates1 = coordinates.getJSONArray(0);

                            for (int lc = 0; lc < coordinates1.length(); lc++) {
                                JSONArray coord = coordinates1.getJSONArray(lc);
                                LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                                points.add(latLng);
                            }

                            fieldsData.setPoints(points);

                            fieldsDataArrayList.add(fieldsData);
                        }
                        // Our GeoJSON only has one feature: a line string

                    }
                }


            } catch (Exception exception) {
                Log.e(TAG, "Exception Loading GeoJSON: " + exception.toString());
            }

            return fieldsDataArrayList;
        }

        @Override
        protected void onPostExecute(List<FieldsData> fieldsDataArrayList) {
            super.onPostExecute(fieldsDataArrayList);

            if (fieldsDataArrayList.size() > 0) {

                for (FieldsData fieldsData : fieldsDataArrayList) {
                    // Draw polyline on map
                    mapboxMap.addPolyline(new PolylineOptions()
                            .addAll(fieldsData.getPoints())
                            .color(Color.parseColor("#08b4df"))
                            .width(2));
                }
            }

            mapboxMap.addMarkers(markersArrayList);


        }
    }

}
