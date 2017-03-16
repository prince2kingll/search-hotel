package com.example.princewill.searchhotel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Intent intent;
    String HotelName;
    int ImageIndex;
    String reviews;
    String website;

    TextView hotelname,hoteladdress;
    CircleImageView hotelpic;
    RatingBar hotelrating;
    Button hotelreview,book,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        intent = getIntent();
        HotelName  = intent.getStringExtra("HotelName");

        hotelname = (TextView)findViewById(R.id.tv_hotelname);
        hoteladdress = (TextView)findViewById(R.id.tv_hoteladdress);
        hotelpic = (CircleImageView)findViewById(R.id.iv_hotelimage);
        hotelrating= (RatingBar)findViewById(R.id.rb_hotelrating);
        hotelreview = (Button)findViewById(R.id.btn_review);
        book = (Button)findViewById(R.id.btn_book);
        back = (Button)findViewById(R.id.btn_back);

        hotelrating.setEnabled(false);

        HotelDetails HD = new HotelDetails();
        HD.execute("hotelDetails");

        hotelpic.setClickable(true);
        hotelpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileActivity.this,PictureDisplay.class);
                intent.putExtra("HotelName",HotelName);
                intent.putExtra("ImageIndex",ImageIndex);
                startActivity(intent);

            }
        });

        ProfileImage HI = new ProfileImage();
        HI.execute("hotelDetails");

        hotelreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewsDialog ad=new ReviewsDialog(ProfileActivity.this,reviews);
                ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ad.show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = website;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }


    private class HotelDetails extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... voids) {
            String gps_url = "http://192.168.43.116:80/searchotel/getHotel.php";

            try {
                URL url = new URL(gps_url);

                HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();

                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);

                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data =  URLEncoder.encode("HotelName","UTF-8")+"="+URLEncoder.encode(HotelName,"UTF-8");
                bufferedWriter.write(post_data);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
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
            //Toast.makeText(ProfileActivity.this,avoid,Toast.LENGTH_LONG).show();

            //populate the view here////////////////////////////////////////////////
            String[] hotelDetails = avoid.split("%");
            hotelname.setText(hotelDetails[0]);
            hoteladdress.setText(hotelDetails[1]);
            reviews = hotelDetails[3];
            hotelrating.setRating(Float.valueOf(hotelDetails[2]));
            website = hotelDetails[4];
            ImageIndex = Integer.parseInt(hotelDetails[5]);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class ProfileImage extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... voids) {
            String name = HotelName.replace(" ","_");
            String img_url = "http://192.168.43.116:80/searchotel/images/"+name+"_profile.png";

            Bitmap mIcon11 = BitmapFactory.decodeResource(getResources(),R.drawable.searchotel_logo);
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
            hotelpic.setImageBitmap(avoid);
              }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    class ReviewsDialog extends Dialog{

        ListView review;
        ArrayList<String> reviewS = new ArrayList<>();
        ArrayAdapter arrayAdapter;

        public ReviewsDialog(Activity a,String list){
            super(a);
            String set[] = list.split("--");

            for(String s:set){
                reviewS.add(s);
            }
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_reviews);

            review = (ListView)findViewById(R.id.lv_reviewlist);
            arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this,R.layout.list_white_text,R.id.list_content);
            review.setAdapter(arrayAdapter);
            arrayAdapter.clear();
            for (int i = 0;i<reviewS.size();i++)
                arrayAdapter.add(reviewS.get(i));
            arrayAdapter.notifyDataSetChanged();
        }
    }


}
