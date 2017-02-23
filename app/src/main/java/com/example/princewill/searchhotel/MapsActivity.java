package com.example.princewill.searchhotel;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Vector<String[]> gpsLocations = new Vector<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getGPS signUpBackground = new getGPS();
        signUpBackground.execute("signup");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        for(String[] hotels:gpsLocations){
            LatLng newHotel = new LatLng(Double.valueOf(hotels[1]),Double.valueOf(hotels[2]));
            mMap.addMarker(new MarkerOptions().position(newHotel).title("Marker in "+hotels[0]));
        }
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private class getGPS extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... voids) {
            String gps_url = "http://192.168.43.116:80/searchotel/getGPS.php";

                try {
                    URL url = new URL(gps_url);

                    HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();

                    httpsURLConnection.setRequestMethod("POST");
                    httpsURLConnection.setDoInput(true);
                    httpsURLConnection.setDoOutput(true);

                    InputStream inputStream = httpsURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] gps = line.split("%");
                        gpsLocations.add(gps);
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpsURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    return "mal" + e.toString();
                } catch (IOException e) {
                    return "io" + e.toString();
                }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String avoid) {
            Toast.makeText(MapsActivity.this,avoid,Toast.LENGTH_LONG).show();

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(MapsActivity.this);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    }
