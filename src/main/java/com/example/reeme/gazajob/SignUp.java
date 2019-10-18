package com.example.reeme.gazajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.Data.UserContract;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;

public class SignUp extends AppCompatActivity {
    private TextView txt_signin;
    private Button btn_signup;
    private EditText username,email,pswrd;
    private TextView mEmailInValid,mUsername_invalid,mPassword_invalid;
    final String nameRegex = ".{3,25}";//small
    final  String nameRegex2=".{0,25}";//large
    final    String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    final String passwordregex =".{6,12}";
    private AppSharedPreferences appSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.username);
        mUsername_invalid=(TextView)findViewById(R.id.username_invalid);
        email = (EditText) findViewById(R.id.email);
        mEmailInValid=(TextView)findViewById(R.id.email_invalid);
        pswrd = (EditText) findViewById(R.id.password);
        mPassword_invalid=(TextView)findViewById(R.id.password_invalid);
        txt_signin = (TextView) findViewById(R.id.signin_txt);
        btn_signup = (Button) findViewById(R.id.btn_signUp);

        //initializes
        appSharedPreferences=new AppSharedPreferences(SignUp.this);

      username.addTextChangedListener(validationNameWatcher);
      email.addTextChangedListener(validationEmailWatcher);
      pswrd.addTextChangedListener(validationPasswordWatcher);

        //event on button
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String StringUsername = username.getText().toString().trim();
                String StringEmail= email.getText().toString().trim();
                String StringPassword = pswrd.getText().toString().trim();

                if (StringUsername.matches(nameRegex2) &&StringUsername.matches(nameRegex) && StringEmail.matches(emailPattern)
                        && StringPassword.matches(passwordregex)) {

                         //create an Intent object
                            Intent intent = new Intent(SignUp.this, CompleteSignUp.class);
                            //add data to the Intent object
                            intent.putExtra("NAME", username.getText().toString());
                            intent.putExtra("EMAIL", email.getText().toString());
                            intent.putExtra("PASSWORD", pswrd.getText().toString());
                            //start the second activity
                            startActivity(intent);
                            finish();
                            }
                            else {
                    Toast.makeText(getApplicationContext(), R.string.Please_enter_details, Toast.LENGTH_LONG).show();

                }
            }
        });


        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appSharedPreferences.writeString(Constants.FILTER, "no");
        appSharedPreferences.writeString(Constants.PROFILE,"noProfile");
        startActivity(new Intent(SignUp.this,MainActivity.class));
        finish();
    }


private TextWatcher validationNameWatcher=new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String userValid = username.getText().toString().trim();
            //////name
        if (!userValid.matches(nameRegex2)) {
            mUsername_invalid.setText(R.string.largeInput);
        } else if (!userValid.matches(nameRegex)) {
            mUsername_invalid.setText(R.string.smallInput);
        } else {
            mUsername_invalid.setText("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
};

    private TextWatcher validationEmailWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String emailValid = email.getText().toString().trim();

            /////email
            if (!emailValid.matches(emailPattern)) {
                mEmailInValid.setText(R.string.invalid_email);
            } else {
                mEmailInValid.setText("");
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher validationPasswordWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String passwordValid = pswrd.getText().toString().trim();
            //password
            if (!passwordValid.matches(passwordregex)) {
                mPassword_invalid.setText(R.string.password_length);
            } else {
                mPassword_invalid.setText("");
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}

