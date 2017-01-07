package com.example.princewill.searchhotel;

import android.content.Context;
import android.os.AsyncTask;
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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Princewill on 1/2/2017.
 */

public class LoginBackground extends AsyncTask<String,Void,String> {

    Context context;
    public LoginBackground(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... voids) {

        String Type = voids[0];
        String login_url = "https://192.168.43.116/searchhotel/login.php";

        if(Type.equals("login")){

            try {
                String user_name = voids[0];
                String password = voids[1];
                String e_mail = voids[2];

                URL url = new URL(login_url);

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();



                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setHostnameVerifier( new AlwaysTrust());

                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(e_mail,"UTF-8");
                bufferedWriter.write(post_data);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="nothing ";
                String line="";
                while((line = bufferedReader.readLine())!=null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                return "mal"+e.toString();
            } catch (IOException e) {
                return "io"+e.toString();
            }
        }
            return "failed";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String avoid) {

        Toast.makeText(context,avoid,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    class AlwaysTrust implements X509TrustManager,HostnameVerifier {

        public void checkClientTrusted(X509Certificate[] x509 , String authType ) throws CertificateException { /* nothing */ }
        public void checkServerTrusted( X509Certificate[] x509 , String authType ) throws CertificateException { /* nothing */ }
        public X509Certificate[] getAcceptedIssuers() { return null; }

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }
}
