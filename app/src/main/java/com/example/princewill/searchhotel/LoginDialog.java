package com.example.princewill.searchhotel;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Princewill on 1/8/2017.
 */

public class LoginDialog extends Dialog implements
        android.view.View.OnClickListener  {

    public Activity c;
    public Dialog d;

    public EditText Username;
    public EditText Email;
    public EditText Password;

    public Button log;

    public LoginDialog(Activity a){
        super(a);

        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_in);

        Username = (EditText)findViewById(R.id.editText_username);
        Email = (EditText)findViewById(R.id.editText_email);
        Password = (EditText)findViewById(R.id.editText_password);

        log = (Button)findViewById(R.id.button_login);
        log.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_login:

                LoginBackground loginBackground = new LoginBackground(c);
                loginBackground.execute("login",Username.getText().toString(),Password.getText().toString(),Email.getText().toString());

                dismiss();
                break;
            default:
                break;
        }
        dismiss();


    }
}
