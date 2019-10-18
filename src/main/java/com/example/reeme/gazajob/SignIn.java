package com.example.reeme.gazajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.Data.UserContract;
import com.example.reeme.gazajob.DataBase.SessionManager;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.reeme.gazajob.R.*;

public class SignIn extends AppCompatActivity{
    private AutoCompleteTextView email;
    private EditText pass;
    private Button login_btn;
    private SessionManager session;
    private CheckBox remember_chckBox;
    private TextView txt_signup,validationEmail,validationPassword;
    private AppSharedPreferences appSharedPreferences;
    private ProgressDialog progressDialog;
    private String password;
    final   String passwordregex =".{6,12}";
    final    String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";


    private static final String TAG =SignIn.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_sign_in);


        email = (AutoCompleteTextView) findViewById(R.id.email);
        validationEmail=findViewById(id.email_invalid);
        pass = (EditText) findViewById(R.id.password);
        validationPassword=findViewById(id.password_invalid);
        login_btn = (Button) findViewById(id.btn_login);
        txt_signup=(TextView)findViewById(id.signup);


        //initializes classes
        appSharedPreferences=new AppSharedPreferences(SignIn.this);
        //Session Manager
        session = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setCancelable(false);


        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignIn.this,SignUp.class);
                startActivity(intent);
                finish();
            }
        });

       pass.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               }
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
             if(!pass.getText().toString().matches(passwordregex)){
                 validationPassword.setText(R.string.password_length);
             }else {
                 validationPassword.setText("");
             }
           }
           @Override
           public void afterTextChanged(Editable s) {

           }
       });



        // Login button Click Event
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_mail = email.getText().toString().trim();
                   password = pass.getText().toString().trim();
                  // Check for empty data in the form
                if ( password.matches(passwordregex) && e_mail.matches(emailPattern)) {
                              // login user
                            CheckLogin(e_mail, password);
                        } else {
                      // validationEmail.setText(R.string.invalid_email);
                    Toast.makeText(getApplicationContext(), R.string.Please_enter_details, Toast.LENGTH_LONG).show();

                }

            }
        });


        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appSharedPreferences.writeString(Constants.FILTER, "no");
        appSharedPreferences.writeString(Constants.PROFILE,"noProfile");
        startActivity(new Intent(SignIn.this,MainActivity.class));
        finish();
    }

    /**
     * function to verify login details in mysql db
     */

    private void CheckLogin(final String e_mail, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        progressDialog.setMessage(getString(string.logging));
         showDialog();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,AppConfig.URL_LOGIN,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response :" + response.toString());
                hideDialog();
                try {

               JSONObject jsonObject = new JSONObject(response);
               boolean error= jsonObject.getBoolean("error");
               // Check for error node in json
                    if(!error){
                        try {
                            JSONObject user =jsonObject.getJSONObject("user");
                            //user data ->user profile
                            // session for email and password ,jawwal
                            String userconfirmed = user.getString("isconfirmed");
                            //   if(userconfirmed.equals("1")){
                            String username=user.getString("name");
                            String email=user.getString("email");
                            String jawwal=user.getString("jawwal");
                            // String job=user.getString("job");
                            session.setLoginUser(true);
                            appSharedPreferences.writeString(Constants.USER_PASSWORD,password);
                            appSharedPreferences.writeString(Constants.USER_NAME,username);
                            appSharedPreferences.writeString(Constants.EMAIL,email);
                            appSharedPreferences.writeString(Constants.MOBILE,jawwal);
                            //read from sharedPreference//check if it came from buttomSheetFragment
                            appSharedPreferences.writeString(Constants.ADVERTISE,"advertise");
                            String buttomsheet=appSharedPreferences.readString(Constants.BUTTOMSHEET);
                            if(buttomsheet.equals("buttomsheet")){
                                Toast.makeText(SignIn.this, string.Login_successfully, Toast.LENGTH_SHORT).show();
                                finish();
                                appSharedPreferences.writeString(Constants.BUTTOMSHEET,"no_buttomsheet");
                            }else {
                                appSharedPreferences.writeString(Constants.PROFILE,"profile");
                                Intent intent = new Intent(SignIn.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }/**}else{
                             Toast.makeText(SignIn.this, "user not confirmed", Toast.LENGTH_SHORT).show();

                             }
                             */
                        }
                        catch (Exception ex){

                            try{
                                JSONObject company = jsonObject.getJSONObject("company");
                                ///read company data ->intent company profile
                                //session
                                String isconfirmedcompany = company.getString("isconfirmed");
                                //    if(isconfirmedcompany.equals("1")){
                                String username_company=company.getString("manager_name");
                                String name_company=company.getString("name");
                                String email_company=company.getString("email");
                                String jawwal_company=company.getString("jawwal");
                                String palce_company=company.getString("site");
                                String website_company=company.getString("website");
                                String phone_company=company.getString("telephone");
                               // String address_company=company.getString("address");
                                session.setLoginCompany(true);
                                appSharedPreferences.writeString(Constants.COMPANY_USER,username_company);
                                appSharedPreferences.writeString(Constants.COMPANY_NAME,name_company);
                                appSharedPreferences.writeString(Constants.COMPANY_EMAIL,email_company);
                                appSharedPreferences.writeString(Constants.COMPANY_MOBILE,jawwal_company);
                                appSharedPreferences.writeString(Constants.SITE_COMPANY,palce_company);
                                appSharedPreferences.writeString(Constants.WEBSITE_COMPANY,website_company);
                                appSharedPreferences.writeString(Constants.PHONE_COMPANY,phone_company);
                               // appSharedPreferences.writeString(Constants.ADDRESS_COMPANY,address_company);

                                //read from welcome activity.-> advertise
                                String advertise=appSharedPreferences.readString(Constants.ADVERTISE);
                                if (advertise.equals("welcome_advertise")){
                                    Toast.makeText(SignIn.this, string.Login_successfully, Toast.LENGTH_SHORT).show();
                                    finish();
                                    appSharedPreferences.writeString(Constants.ADVERTISE,"advertise");

                                }else {
                                    appSharedPreferences.writeString(Constants.PROFILE,"profile");
                                    Intent intent = new Intent(SignIn.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                /**}else{
                                 Toast.makeText(SignIn.this, "company not confirmed", Toast.LENGTH_SHORT).show();
                                 }
                                 */
                            } catch (Exception exp){
                                Toast.makeText(SignIn.this, exp.getMessage().toString()+"/"+ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignIn.this, "there sth wrong not company or user", Toast.LENGTH_SHORT).show();
                            }
                        }
                        }
                    else{
                        //error in login. Get the error msg
                        //   String errormsg=jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),R.string.email_password,Toast.LENGTH_LONG).show();; }
                        }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
        @Override
            public void onErrorResponse(VolleyError error){
            Log.d(TAG,"Login Error :" +error.getMessage());
            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            hideDialog();
        }
        }){
            @Override
        protected Map<String, String> getParams() {
            // Posting parameters to login url
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", e_mail);
            params.put("password", password);

            return params;
        }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
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





    }





