package com.example.princewill.searchhotel;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Princewill on 1/8/2017.
 */

public class SignupDialog  extends Dialog implements
        android.view.View.OnClickListener  {

    public Activity c;
    public Dialog d;

    public EditText Username;
    public EditText Email;
    public EditText Password;
    public EditText FirstName;
    public EditText LastName;
    public EditText Country;
    public EditText State;

    public Button sign;

    public SignupDialog(Activity a){
        super(a);

        this.c = a;
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
        Country = (EditText)findViewById(R.id.editText_country);
        State = (EditText)findViewById(R.id.editText_state);

        sign = (Button)findViewById(R.id.button_signup);
        sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.button_signup:

                //SEND INFO TO THE SERVER DATABASE TO STORE

                break;
            default:
                break;
        }

    }
}
