package com.example.princewill.searchhotel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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

public class SignActivity extends AppCompatActivity {

    public static UserClass userclass;
    Button signin,signup;
    TextView skip;
    final Context context = this;
    private String locale;
    Intent intent;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        signin = (Button)findViewById(R.id.signin_btn);
        signup = (Button)findViewById(R.id.signup_btn);
        skip = (TextView)findViewById(R.id.tv_skip);

        intent = getIntent();
        locale = intent.getStringExtra("Longitude")+","+intent.getStringExtra("Latitude");

        handler = new Handler();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDialog ld=new LoginDialog(SignActivity.this);
                ld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ld.show();

                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                signin.startAnimation(animation2);
                signup.startAnimation(animation2);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        signin.setVisibility(View.GONE);
                        signup.setVisibility(View.GONE);
                    }
                }, 350);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupDialog sd=new SignupDialog(SignActivity.this);
                sd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                sd.show();

                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                signin.startAnimation(animation2);
                signup.startAnimation(animation2);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        signin.setVisibility(View.GONE);
                        signup.setVisibility(View.GONE);
                    }
                }, 350);
            }
        });

        skip.setClickable(true);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGPS gg = new getGPS();
                gg.execute();
            }
        });
    }


    class LoginDialog extends Dialog implements
            android.view.View.OnClickListener  {

        public EditText Username;
        public EditText Password;

        public Button log;

        public LoginDialog(Activity a){
            super(a);
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_sign_in);

            Username = (EditText)findViewById(R.id.editText_username);
            Password = (EditText)findViewById(R.id.editText_password);

            log = (Button)findViewById(R.id.button_login);
            log.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_login:
                    if(Username.getText().toString().equals("") || Password.getText().toString().equals("")){
                        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                        signin.startAnimation(animation1);
                        signup.startAnimation(animation1);

                        signin.setVisibility(View.VISIBLE);
                        signup.setVisibility(View.VISIBLE);
                    }else{
                        LoginBackground loginBackground = new LoginBackground();
                        loginBackground.execute("login",Username.getText().toString(),Password.getText().toString());
                        dismiss();

                        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                        signin.startAnimation(animation1);
                        signup.startAnimation(animation1);

                        signin.setVisibility(View.VISIBLE);
                        signup.setVisibility(View.VISIBLE);

                        signin.setEnabled(false);
                        signup.setEnabled(false);
                    }
                    break;
                default:
                    break;
            }
            dismiss();
        }
        class LoginBackground extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... voids) {
                String Type = voids[0];
                String login_url = "http://192.168.43.116:80/searchotel/Login.php";

                if(Type.equals("login")){

                    try {
                        String user_name = voids[1];
                        String password = voids[2];

                        URL url = new URL(login_url);

                        HttpURLConnection httpsURLConnection = (HttpURLConnection)url.openConnection();

                        httpsURLConnection.setRequestMethod("POST");
                        httpsURLConnection.setDoInput(true);
                        httpsURLConnection.setDoOutput(true);

                        OutputStream outputStream = httpsURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                        String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
                    }catch (Exception e){
                        return "code:444 unknown "+e.toString();
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
            if(!avoid.contains("code:444") || !avoid.contains("notsuccess")){
                String[] userInfo = avoid.split(" ");
                userclass = new UserClass(userInfo);
                getGPS gg = new getGPS();
                gg.execute();

            }else{
                Toast.makeText(context,"Error Logging in",Toast.LENGTH_LONG).show();

                signin.setEnabled(true);
                signup.setEnabled(true);
            }
            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        }

    }

    class SignupDialog  extends Dialog implements
            android.view.View.OnClickListener  {

        public EditText Username;
        public EditText Email;
        public EditText Password;
        public EditText FirstName;
        public EditText LastName;

        public Button sign;

        public SignupDialog(Activity a){
            super(a);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_sign_up);

            Username = (EditText)findViewById(R.id.editText_username);
            Email = (EditText)findViewById(R.id.editText_email);
            Password = (EditText)findViewById(R.id.editText_password);
            FirstName = (EditText)findViewById(R.id.editText_firstname);
            LastName = (EditText)findViewById(R.id.editText_lastname);

            sign = (Button)findViewById(R.id.button_signup);
            sign.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case R.id.button_signup:
                    SignUpBackground signUpBackground = new SignUpBackground();
                    signUpBackground.execute("signup",Username.getText().toString(),Email.getText().toString(),Password.getText().toString(),FirstName.getText().toString(),LastName.getText().toString());
                    dismiss();

                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                    signin.startAnimation(animation1);
                    signup.startAnimation(animation1);

                    signin.setVisibility(View.VISIBLE);
                    signup.setVisibility(View.VISIBLE);

                    signin.setEnabled(false);
                    signup.setEnabled(false);

                    break;
                default:
                    break;
            }

        }

        class SignUpBackground extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... voids) {
                String Type = voids[0];
                String login_url = "http://192.168.43.116:80/searchotel/Signup.php";

                if(Type.equals("signup")){

                    try {
                        String username = voids[1];
                        String email = voids[2];
                        String password = voids[3];
                        String firstname = voids[4];
                        String lastname = voids[5];

                        URL url = new URL(login_url);

                        HttpURLConnection httpsURLConnection = (HttpURLConnection)url.openConnection();

                        httpsURLConnection.setRequestMethod("POST");
                        httpsURLConnection.setDoInput(true);
                        httpsURLConnection.setDoOutput(true);

                        OutputStream outputStream = httpsURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                        String post_data =  URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+URLEncoder.encode("firstname","UTF-8")+"="+URLEncoder.encode(firstname,"UTF-8")+"&"+URLEncoder.encode("lastname","UTF-8")+"="+URLEncoder.encode(lastname,"UTF-8");
                        bufferedWriter.write(post_data);

                        bufferedWriter.flush();
                        bufferedWriter.close();
                        outputStream.close();

                        InputStream inputStream = httpsURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                        String result="";
                        String line="";
                        while((line = bufferedReader.readLine())!=null){
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
                if(!avoid.contains("code:444") || !avoid.contains("notsuccess")){
                String[] userInfo = avoid.split(" ");
                userclass = new UserClass(userInfo);
                        getGPS gg = new getGPS();
                        gg.execute();

                }else
                Toast.makeText(context,"Error Signing Up",Toast.LENGTH_LONG).show();
                signin.setEnabled(true);
                signup.setEnabled(true);
            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        }
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
            //Toast.makeText(SignActivity.this,avoid,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(SignActivity.this,MapsActivity.class);
            intent.putExtra("hotelGPS",avoid);
            intent.putExtra("myGPS",locale);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
