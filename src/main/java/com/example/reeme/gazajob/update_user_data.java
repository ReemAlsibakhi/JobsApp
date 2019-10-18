package com.example.reeme.gazajob;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class update_user_data extends AppCompatActivity {
private  EditText username,email,jawwal;
    private Button button_update;
    final String nameRegex = ".{3,25}";//small
    final  String nameRegex2=".{0,25}";//large
    final    String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    final String jawwalregex = "05[69][245789][0-9]{6}";
    private AppSharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private TextView mEmailInValid,mUsername_invalid,txt_userJawwwal,back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_data);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        jawwal=(EditText)findViewById(R.id.phoneEdit);

        mUsername_invalid=(TextView)findViewById(R.id.username_invalid);
        mEmailInValid=(TextView)findViewById(R.id.email_invalid);
        txt_userJawwwal=findViewById(R.id.userJawwwal);

        button_update=findViewById(R.id.btn_update);
        back_arrow=findViewById(R.id.back);



        username.addTextChangedListener(validationNameWatcher);
        email.addTextChangedListener(validationEmailWatcher);
        jawwal.addTextChangedListener(validationMobileWatcher);
        //event
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       // get data from login response
        sharedPreferences=new AppSharedPreferences(update_user_data.this);
        progressDialog = new ProgressDialog(update_user_data.this);
        progressDialog.setCancelable(false);

        final String name_user =sharedPreferences.readString(Constants.USER_NAME);
        final String email_user =sharedPreferences.readString(Constants.EMAIL);
        final String mobile_user =sharedPreferences.readString(Constants.MOBILE);

        username.setText(name_user);
        email.setText(email_user);
        jawwal.setText(mobile_user);

        username.addTextChangedListener(updateData);
        email.addTextChangedListener(updateData);
        jawwal.addTextChangedListener(updateData);





        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String name=username.getText().toString().trim();
              String E_mail=email.getText().toString().trim();
              String mobile=jawwal.getText().toString().trim();

                if(name.matches(nameRegex) && name.matches(nameRegex2) && E_mail.matches(emailPattern) && mobile.matches(jawwalregex)){
                   updateUser(name,E_mail,mobile);
                }else {
                    Toast.makeText(update_user_data.this, R.string.Please_enter_details, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private TextWatcher updateData=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          button_update.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    } ;


    public void updateUser(final String username, final String email, final String mobile){
        String tag_string_req = "req_update";
        progressDialog.setMessage(getString(R.string.updating));
         showDialog();
        StringRequest stringReq=new StringRequest(Request.Method.POST, AppConfig.URL_UPDATE_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                     // JSONObject jsonObject = new JSONObject(response);
                     // boolean error= jsonObject.getBoolean("error");
                      // Check for error node in json

               // sharedPreferences.writeString(Constants.UPDATE_USER,"update_user");
                   sharedPreferences.writeString(Constants.USER_NAME,username);
                   sharedPreferences.writeString(Constants.EMAIL,email);
                   sharedPreferences.writeString(Constants.MOBILE,mobile);

                          Toast.makeText(update_user_data.this, R.string.updated, Toast.LENGTH_SHORT).show();
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",username);
                params.put("user_email",email);
                params.put("jawwal",mobile);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, tag_string_req);
    }

    private void hideDialog() {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private void showDialog() {

        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
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
            }@Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailValid = email.getText().toString().trim();
            /////email
            if (!emailValid.matches(emailPattern)) {
                mEmailInValid.setText(R.string.invalid_email);
            } else {
                mEmailInValid.setText("");
            } }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    //
    private TextWatcher validationMobileWatcher=new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!jawwal.getText().toString().matches(jawwalregex)){
                txt_userJawwwal.setText(R.string.invalid_mobile_Input);
            }else {
                txt_userJawwwal.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}

