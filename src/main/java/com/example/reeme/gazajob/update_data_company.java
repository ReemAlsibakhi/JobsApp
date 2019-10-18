package com.example.reeme.gazajob;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class update_data_company extends AppCompatActivity {
    EditText mCompanyName, mCompanyAddress, mCompanyMobile, mCompanyPhone, mCompanyWebsite, mUsername, mEmail, mPlace;
    private TextView back_arrow, mUsername_invalid, mEmailInValid, txt_warning_name, txt_warning_address, txt_warning_mobile, txt_warning_website, txt_warning_place;
    private Button btn_updateData;
    private AppSharedPreferences sharedPreferences;
    final String nameRegex = ".{3,25}";//small
    final String nameRegex2 = ".{0,25}";//large
    final String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    final String jawwalregex = "05[69][245789][0-9]{6}";
    final String urlregex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data_company);


        mUsername = (EditText) findViewById(R.id.username);
        mEmail = (EditText) findViewById(R.id.email);
        mCompanyName = (EditText) findViewById(R.id.companyEdit);
        mPlace = findViewById(R.id.placeEditTxt);
        mCompanyAddress = (EditText) findViewById(R.id.AddressEdit);
        mCompanyMobile = (EditText) findViewById(R.id.mobileEdit);
        mCompanyPhone = (EditText) findViewById(R.id.phoneEdit);
        mCompanyWebsite = (EditText) findViewById(R.id.webEdit);
        btn_updateData = findViewById(R.id.btn_update);
        ///   warning
        mUsername_invalid = (TextView) findViewById(R.id.username_invalid);
        mEmailInValid = (TextView) findViewById(R.id.email_invalid);
        txt_warning_name = (TextView) findViewById(R.id.nameCompany);
        txt_warning_address = (TextView) findViewById(R.id.companyAddress);
        txt_warning_mobile = (TextView) findViewById(R.id.companyJawwal);
        txt_warning_website = (TextView) findViewById(R.id.companyWebsite);
        txt_warning_place = findViewById(R.id.companyPlace);
        //initializes class
        progressDialog = new ProgressDialog(update_data_company.this);
        progressDialog.setCancelable(false);

        //read company data
        sharedPreferences = new AppSharedPreferences(update_data_company.this);
        final String username = sharedPreferences.readString(Constants.COMPANY_USER);
        final String companyname = sharedPreferences.readString(Constants.COMPANY_NAME);
        final String email = sharedPreferences.readString(Constants.COMPANY_EMAIL);
        final String mobile = sharedPreferences.readString(Constants.COMPANY_MOBILE);
        final String phone = sharedPreferences.readString(Constants.PHONE_COMPANY);
        final String place = sharedPreferences.readString(Constants.SITE_COMPANY);
        final String address = sharedPreferences.readString(Constants.ADDRESS_COMPANY);
        final String website = sharedPreferences.readString(Constants.WEBSITE_COMPANY);

        mUsername.setText(companyname);
        mEmail.setText(email);
        mCompanyName.setText(username);
        mCompanyAddress.setText(address);
        mPlace.setText(place);
        mCompanyMobile.setText(mobile);
        mCompanyPhone.setText(phone);
        mCompanyWebsite.setText(website);


        mUsername.addTextChangedListener(validationuserNameWatcher);
        mEmail.addTextChangedListener(validationEmailWatcher);
        mCompanyName.addTextChangedListener(validationNameWatcher);
        mCompanyAddress.addTextChangedListener(validationAddressWatcher);
        mCompanyMobile.addTextChangedListener(validationMobileWatcher);
        mCompanyWebsite.addTextChangedListener(validationWebsitesWatcher);
        mPlace.addTextChangedListener(validationPlaceWatcher);
        mCompanyPhone.addTextChangedListener(validationPhoneWatcher);

        back_arrow = findViewById(R.id.back);
        //event
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = mUsername.getText().toString().trim();
                String email_comp = mEmail.getText().toString().trim();
                String companyName = mCompanyName.getText().toString().trim();
                String place = mPlace.getText().toString().trim();
                String companyAddress = mCompanyAddress.getText().toString().trim();
                String companyMobile = mCompanyMobile.getText().toString().trim();
                String companyPhone = mCompanyPhone.getText().toString().trim();
                String companyWebsite = mCompanyWebsite.getText().toString().trim();
                if (user_name.matches(nameRegex2) && user_name.matches(nameRegex) && email_comp.matches(emailPattern)
                        && companyName.matches(nameRegex2) && companyName.matches(nameRegex)
                        && place.matches(nameRegex2) && place.matches(nameRegex)
                        && companyAddress.matches(nameRegex2) && companyAddress.matches(nameRegex)
                        && companyMobile.matches(jawwalregex) ) {
                    update_data_company(user_name, email_comp, companyName, place, companyAddress, companyMobile, companyPhone, companyWebsite);
                } else {
                    // enter the details
                    Toast.makeText(getApplicationContext(), R.string.Please_enter_details, Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    public void update_data_company(final String user_name, final String email_comp, final String companyName,final String place,
                                    final String companyAddress,final String companyMobile,final String companyPhone,final String companyWebsite){
        String tag_string_req = "req_update";
        progressDialog.setMessage(getString(R.string.updating));
        showDialog();
        StringRequest stringReq=new StringRequest(Request.Method.POST, AppConfig.URL_UPDATE_COMPANY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                // JSONObject jsonObject = new JSONObject(response);
                // boolean error= jsonObject.getBoolean("error");
                // Check for error node in json

                //sharedPreferences.writeString(Constants.UPDATE_USER,"update_user");
                Toast.makeText(update_data_company.this, R.string.updated, Toast.LENGTH_SHORT).show();
                sharedPreferences.writeString(Constants.COMPANY_USER,user_name);
                sharedPreferences.writeString(Constants.COMPANY_NAME,companyName);
                sharedPreferences.writeString(Constants.COMPANY_EMAIL,email_comp);
                sharedPreferences.writeString(Constants.COMPANY_MOBILE,companyMobile);
                sharedPreferences.writeString(Constants.PHONE_COMPANY,companyPhone);
                sharedPreferences.writeString(Constants.WEBSITE_COMPANY,companyWebsite);
              //  sharedPreferences.writeString(Constants.ADDRESS_COMPANY,companyAddress);
                sharedPreferences.writeString(Constants.SITE_COMPANY,place);
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
                params.put("company_email",email_comp);
                params.put("name",companyName);
                params.put("manager_name",user_name);
                params.put("site",place);
                params.put("address",companyAddress);
                params.put("jawwal",companyMobile);
                params.put("telephone",companyPhone);
                params.put("website",companyWebsite);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, tag_string_req);
    }

    private TextWatcher validationuserNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           btn_updateData.setEnabled(true);
            String userValid = mUsername.getText().toString().trim();
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


    private TextWatcher validationEmailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btn_updateData.setEnabled(true);

            String emailValid = mEmail.getText().toString().trim();

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



    ///name
    private TextWatcher validationNameWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btn_updateData.setEnabled(true);

            //name to company
            if(!mCompanyName.getText().toString().matches(nameRegex2)){
                txt_warning_name.setText(R.string.largeInput);
            }else if(!mCompanyName.getText().toString().matches(nameRegex)){
                txt_warning_name.setText(R.string.smallInput);
            }else {
                txt_warning_name.setText(""); }
        }@Override
        public void afterTextChanged(Editable s) {

        }
    };

    ///address
    private TextWatcher validationAddressWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btn_updateData.setEnabled(true);

            //address to company
            if(!mCompanyAddress.getText().toString().matches(nameRegex2)){
                txt_warning_address.setText(R.string.largeInput);
            }else if(!mCompanyAddress.getText().toString().matches(nameRegex)){
                txt_warning_address.setText(R.string.smallInput);
            }else {
                txt_warning_address.setText(""); }
        }@Override
        public void afterTextChanged(Editable s) {

        }
    };

    //place

    ///address
    private TextWatcher validationPlaceWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btn_updateData.setEnabled(true);

            //address to company
            if(!mPlace.getText().toString().matches(nameRegex2)){
                txt_warning_place.setText(R.string.largeInput);
            }else if(!mPlace.getText().toString().matches(nameRegex)){
                txt_warning_place.setText(R.string.smallInput);
            }else {
                txt_warning_place.setText(""); }
        }@Override
        public void afterTextChanged(Editable s) {

        }
    };
    ////mobile
    private TextWatcher validationMobileWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btn_updateData.setEnabled(true);
            //mobile to company
            if(!mCompanyMobile.getText().toString().matches(jawwalregex)){
                txt_warning_mobile.setText(R.string.invalid_mobile_Input);
            }else {
                txt_warning_mobile.setText(""); }
        }@Override
        public void afterTextChanged(Editable s) {

        }
    };
    /// url
    private TextWatcher validationWebsitesWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //url company
            btn_updateData.setEnabled(true);

            if(!mCompanyWebsite.getText().toString().matches(urlregex)){
                txt_warning_website.setText(R.string.error_url);
            }else {
                txt_warning_website.setText("");
            }
        }@Override
        public void afterTextChanged(Editable s) {

        }
    };

    /// url
    private TextWatcher validationPhoneWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //url company
            btn_updateData.setEnabled(true);

        }@Override
        public void afterTextChanged(Editable s) {

        }
    };
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

