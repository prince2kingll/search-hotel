package com.example.princewill.searchhotel;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignActivity extends AppCompatActivity {

    Button signin,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        signin = (Button)findViewById(R.id.signin_btn);
        signup = (Button)findViewById(R.id.signup_btn);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpDialog();
            }
        });
    }

    public void loginDialog(){

        final Dialog loginDial = new Dialog(this);
        loginDial.setContentView(R.layout.activity_sign_in);
        loginDial.setTitle("LOG IN");


        final EditText Username = (EditText)findViewById(R.id.editText_username);
        final EditText Email = (EditText)findViewById(R.id.editText_email);
        final EditText Password = (EditText)findViewById(R.id.editText_password);

        Button log = (Button)findViewById(R.id.button_login);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String info[]= {Username.getText().toString(),Email.getText().toString(),Password.getText().toString()};
                Toast.makeText(SignActivity.this,info[0]+info[1]+info[2],Toast.LENGTH_SHORT).show();

                BackgroundLogin(info);

                loginDial.dismiss();
            }
        });


    }

    public void SignUpDialog(){

        final Dialog signUpDial = new Dialog(this);
        signUpDial.setContentView(R.layout.activity_sign_up);
        signUpDial.setTitle("LOG IN");


        final EditText Username = (EditText)findViewById(R.id.editText_username);
        final EditText Email = (EditText)findViewById(R.id.editText_email);
        final EditText Password = (EditText)findViewById(R.id.editText_password);
        final EditText FirstName = (EditText)findViewById(R.id.editText_firstname);
        final EditText LastName = (EditText)findViewById(R.id.editText_lastname);
        final EditText Country = (EditText)findViewById(R.id.editText_country);
        final EditText State = (EditText)findViewById(R.id.editText_state);

        Button log = (Button)findViewById(R.id.button_signup);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String info[]= {Username.getText().toString(),Email.getText().toString(),Password.getText().toString(),FirstName.getText().toString(),LastName.getText().toString(),Country.getText().toString(),State.getText().toString()};
                Toast.makeText(SignActivity.this,info[0]+info[1]+info[2]+info[3]+info[4]+info[5]+info[6],Toast.LENGTH_SHORT).show();

                BackgroundSignUp(info);

                signUpDial.dismiss();
            }
        });

    }

    public void BackgroundLogin(String info[]){

        //Background class for log in goes here
        LoginBackground loginBackground = new LoginBackground(this);
        loginBackground.execute("login",info[0],info[2],info[1]);

    }

    public void BackgroundSignUp(String info[]){

        //Background class for sign up goes here

    }

}
