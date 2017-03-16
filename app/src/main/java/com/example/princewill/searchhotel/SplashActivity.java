package com.example.princewill.searchhotel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
public class SplashActivity extends AppCompatActivity{


    ImageView logo;
    RelativeLayout layout;
    TextView loading;

    private LocationManager locationManager;
    private LocationListener locationListener;
    boolean ranAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView)findViewById(R.id.iv_logo);
        loading = (TextView)findViewById(R.id.tv_loading);
        layout = (RelativeLayout)findViewById(R.id.activity_splash);

        loading.setVisibility(View.GONE);
        startAnims();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);
                setupGPS();
            }
        }, 4500);
    }

    private void setupGPS(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(!ranAlready){
                    Intent intent = new Intent(SplashActivity.this,SignActivity.class);
                    intent.putExtra("Longitude",location.getLongitude()+"");
                    intent.putExtra("Latitude",location.getLatitude()+"");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    ranAlready=true;
                }

            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},10);
                return;
            }
        }else{
            runGPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    runGPS();
        }
    }

    private void runGPS() {
        locationManager.requestLocationUpdates("gps", 2000, 0, locationListener);
    }

    private void startAnims(){
        TransitionDrawable transition = (TransitionDrawable) layout.getBackground();
        transition.startTransition(3000);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow);
        logo.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        loading.startAnimation(animation2);

    }

}
