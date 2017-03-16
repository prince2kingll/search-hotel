package com.example.princewill.searchhotel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class PictureDisplay extends AppCompatActivity {

    ImageView[] img = new ImageView[5];
    Intent intent;
    String HotelName;
    int imageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_display);

        intent = getIntent();
        HotelName = intent.getStringExtra("HotelName");
        int index = intent.getIntExtra("ImageIndex",0);

        img[0] = (ImageView)findViewById(R.id.iv_firstimage);
        img[1] = (ImageView)findViewById(R.id.iv_secondimage);
        img[2] = (ImageView)findViewById(R.id.iv_thirdimage);
        img[3] = (ImageView)findViewById(R.id.iv_fourthimage);
        img[4] = (ImageView)findViewById(R.id.iv_fifthimage);

        for(int iv=0;iv<index;iv++){
            //need to add a delay timer so image 6 doesnt cover image 1 before it shows
            GetImage gi = new GetImage();
            gi.execute("0"+iv);
        }

    }

    private class GetImage extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... voids) {
            String name = HotelName.replace(" ","_");
            String img_url = "http://192.168.43.116:80/searchotel/images/"+name+"_"+voids[0]+".png";

            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(img_url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                return mIcon11;
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return mIcon11;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap avoid) {
            switch(imageIndex){
                case 0:
                   img[0].setImageBitmap(avoid);
                    imageIndex++;
                    break;
                case 1:
                    img[1].setImageBitmap(avoid);
                    imageIndex++;
                    break;
                case 2:
                    img[2].setImageBitmap(avoid);
                    imageIndex++;
                    break;
                case 3:
                    img[3].setImageBitmap(avoid);
                    imageIndex++;
                    break;
                case 4:
                    img[4].setImageBitmap(avoid);
                    imageIndex=0;
                    break;
                default:
                    imageIndex=0;
                    break;
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
