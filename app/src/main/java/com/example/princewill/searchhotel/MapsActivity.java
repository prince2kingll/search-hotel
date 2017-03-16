package com.example.princewill.searchhotel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    Intent intent;
    String[] myGPS;

    Button adjust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        adjust = (Button)findViewById(R.id.btn_adjust);

        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add dialog for adjusting here
                AdjustDialog ad=new AdjustDialog(MapsActivity.this);
                ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ad.show();
            }
        });

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        adjust.startAnimation(animation2);

        intent = getIntent();
        String hotelGPS = intent.getStringExtra("hotelGPS");
        myGPS = intent.getStringExtra("myGPS").split(",");

        for(String line:hotelGPS.split("<br>")){
            String[] set = line.split("%");
            if(set.length>1)
                gpsLocations.add(set);


        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(!marker.getTitle().equals("Me")){
                    intent = new Intent(MapsActivity.this,ProfileActivity.class);
                    intent.putExtra("HotelName",marker.getTitle());
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                }

                return false;
            }
        });

        LatLng myLatLong = new LatLng(Double.valueOf(myGPS[1]),Double.valueOf(myGPS[0]));
        mMap.addMarker(new MarkerOptions()
                .position(myLatLong)
                .title("Me"));

        for(String[] hotels:gpsLocations){
            LatLng newHotel = new LatLng(Double.valueOf(hotels[3]),Double.valueOf(hotels[2]));

            if(hotels[1].equalsIgnoreCase("Hotel")){
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_icon))
                        .position(newHotel)
                        .title(hotels[0]));
            }else if(hotels[1].equalsIgnoreCase("Hostel")){
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel_icon))
                        .position(newHotel)
                        .title(hotels[0]));
            }else if(hotels[1].equalsIgnoreCase("Motel")){
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.motel_icon))
                        .position(newHotel)
                        .title(hotels[0]));
            }else if(hotels[1].equalsIgnoreCase("Apartment")){
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.apartment_icon))
                        .position(newHotel)
                        .title(hotels[0]));
            }else{
                mMap.addMarker(new MarkerOptions()
                        .position(newHotel)
                        .title(hotels[0]));
            }
        }
        float zoomlevel = 10;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLong,zoomlevel));
    }

    class AdjustDialog extends Dialog implements
            android.view.View.OnClickListener  {

        public SeekBar searchRange;
        public SeekBar rating;

        public CheckBox hotel;
        public CheckBox hostel;
        public CheckBox motel;
        public CheckBox apartment;

        Vector<String[]> checked = new Vector<String[]>();

        public Button done;

        public AdjustDialog(Activity a){
            super(a);
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_adjust_search);

            searchRange = (SeekBar)findViewById(R.id.sb_range);
            rating = (SeekBar)findViewById(R.id.sb_rating);

            hotel = (CheckBox)findViewById(R.id.cb_hotel);
            hostel = (CheckBox)findViewById(R.id.cb_hostel);
            motel = (CheckBox)findViewById(R.id.cb_motel);
            apartment = (CheckBox)findViewById(R.id.cb_apartment);

            hotel.setChecked(true);
            hostel.setChecked(true);
            motel.setChecked(true);
            apartment.setChecked(true);

            done = (Button)findViewById(R.id.btn_done);
            done.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_done:

                    if(hotel.isChecked())
                        checked.add(new String[]{"hotel","true"});
                    else
                        checked.add(new String[]{"hotel","false"});

                    if(hostel.isChecked())
                        checked.add(new String[]{"hostel","true"});
                    else
                        checked.add(new String[]{"hostel","false"});

                    if(motel.isChecked())
                        checked.add(new String[]{"motel","true"});
                    else
                        checked.add(new String[]{"motel","false"});

                    if(apartment.isChecked())
                        checked.add(new String[]{"apartment","true"});
                    else
                        checked.add(new String[]{"apartment","false"});

                    MapPopulate mp = new MapPopulate();
                    mp.execute("login",searchRange.getProgress()+"",rating.getProgress()+"");

                    break;
                default:
                    break;
            }
            dismiss();
        }
        class MapPopulate extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... voids) {
                String Type = voids[0];
                String login_url = "http://192.168.43.116:80/searchotel/MapPop.php";

                if(Type.equals("login")){

                    try {
                        String searchRange = voids[1];
                        String rating = voids[2];

                        URL url = new URL(login_url);

                        HttpURLConnection httpsURLConnection = (HttpURLConnection)url.openConnection();

                        httpsURLConnection.setRequestMethod("POST");
                        httpsURLConnection.setDoInput(true);
                        httpsURLConnection.setDoOutput(true);

                        OutputStream outputStream = httpsURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                       String post_data = URLEncoder.encode("Longitude","UTF-8")+"="+URLEncoder.encode(myGPS[0],"UTF-8")+"&"+URLEncoder.encode("Latitude","UTF-8")+"="+URLEncoder.encode(myGPS[1],"UTF-8");

                       post_data += "&"+URLEncoder.encode("range","UTF-8")+"="+URLEncoder.encode(searchRange,"UTF-8")+"&"+URLEncoder.encode("rating","UTF-8")+"="+URLEncoder.encode(rating,"UTF-8");

                        for(String[] check:checked)
                            post_data += "&"+URLEncoder.encode(check[0],"UTF-8")+"="+URLEncoder.encode(check[1],"UTF-8");

                        bufferedWriter.write(post_data);

                        bufferedWriter.flush();
                        bufferedWriter.close();
                        outputStream.close();

                        InputStream inputStream = httpsURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                        String result="";
                        String line;
                        while((line = bufferedReader.readLine())!= null){
                            result += line;
                        }

                        bufferedReader.close();
                        inputStream.close();
                        httpsURLConnection.disconnect();

                        return result;

                    } catch (MalformedURLException e) {
                        return "code:444 mal"+e.toString();
                    } catch (IOException e) {
                        return "code:444 io"+e.toString();
                    }
                }
                return "code:444 failed";
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String avoid) {
                if(!avoid.contains("code:444")){

                    Toast.makeText(MapsActivity.this,avoid,Toast.LENGTH_SHORT).show();
                    //repopulate map with new markers
                    Intent intent = new Intent(MapsActivity.this,MapsActivity.class);
                    intent.putExtra("hotelGPS",avoid);
                    intent.putExtra("myGPS",myGPS[0]+","+myGPS[1]);
                    startActivity(intent);
                    finish();

                }
            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        }

    }

}
