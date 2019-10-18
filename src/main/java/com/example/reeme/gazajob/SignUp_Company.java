package com.example.reeme.gazajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class SignUp_Company extends Fragment {

    private static final String TAG = SignUp_Company.class.getSimpleName();
    EditText mCompanyName, mCompanyAddress, mCompanyMobile, mCompanyPhone, mCompanyWebsite;
    private  RadioGroup radioGroup_place;
    private  Button signup_company;
    private RadioButton radioButton;
    private AppSharedPreferences appSharedPreferences;
    private ProgressDialog progressDialog;
    private TextView txt_warning_name,txt_warning_address,txt_warning_mobile,txt_warning_website;
    //regex
    final String urlregex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    final String jawwalregex = "05[69][245789][0-9]{6}";
    final String nameRegex = ".{3,25}";//small
    final  String nameRegex2=".{0,25}";//large


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    final  View v = inflater.inflate(R.layout.activity_signup_company, null);

           //get data from profileActivity.
        final String username = getActivity().getIntent().getStringExtra("NAME");
        final String emailCompany = getActivity().getIntent().getStringExtra("EMAIL");
        final String passwordCompany = getActivity().getIntent().getStringExtra("PASSWORD");


        mCompanyName = (EditText) v.findViewById(R.id.companyEdit);
        radioGroup_place=(RadioGroup)v.findViewById(R.id.radioGroup);
        mCompanyAddress = (EditText) v.findViewById(R.id.AddressEdit);
        mCompanyMobile = (EditText) v.findViewById(R.id.mobileEdit);
        mCompanyPhone = (EditText) v.findViewById(R.id.phoneEdit);
        mCompanyWebsite = (EditText) v.findViewById(R.id.webEdit);
        signup_company = (Button) v.findViewById(R.id.btn_signUp_Company);
        txt_warning_name=(TextView)v.findViewById(R.id.nameCompany);
        txt_warning_address=(TextView)v.findViewById(R.id.companyAddress);
        txt_warning_mobile=(TextView)v.findViewById(R.id.companyJawwal);
        txt_warning_website=(TextView)v.findViewById(R.id.companyWebsite);



        //initializes classes
        appSharedPreferences = new AppSharedPreferences(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

mCompanyName.addTextChangedListener(validationNameWatcher);
mCompanyAddress.addTextChangedListener(validationAddressWatcher);
mCompanyMobile.addTextChangedListener(validationMobileWatcher);
mCompanyWebsite.addTextChangedListener(validationWebsitesWatcher);


  //event on button

        signup_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup_place.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton= (RadioButton)v.findViewById(selectedId);

                String companyName = mCompanyName.getText().toString().trim();
                String place= radioButton.getText().toString().trim();
                String companyAddress = mCompanyAddress.getText().toString().trim();
                String companyMobile = mCompanyMobile.getText().toString().trim();
                String companyPhone = mCompanyPhone.getText().toString().trim();
                String companyWebsite = mCompanyWebsite.getText().toString().trim();
                     //
                if (companyName.matches(nameRegex2)  && companyName.matches(nameRegex)&& !place.isEmpty() && companyAddress.matches(nameRegex2)
                      &&companyAddress.matches(nameRegex) && companyMobile.matches(jawwalregex)){
                        signup_company.setEnabled(false);
                        RegsiterCompany(username, emailCompany, passwordCompany, companyName, place, companyAddress, companyMobile, companyPhone, companyWebsite);
                    }else {
                    // enter the details
                    Toast.makeText(getContext(), R.string.Please_enter_details, Toast.LENGTH_LONG).show();
                }
                }

        });
        return v;

    }


    private void RegsiterCompany(final String username, final String emailCompany, final String passwordCompany, final String companyName, final String place, final String companyAddress, final String companyMobile, final String companyPhone, final String companyWebsite) {
        // Tag used to cancel the request
        String  tag_string_req="req_register";
        progressDialog.setMessage(getString(R.string.registering));
         showDialog();
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER_COMPANY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                  hideDialog();
                         try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                //    if(!error){
                         Toast.makeText(getActivity(), R.string.confirm_application, Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(getActivity(),SignIn.class);
                         startActivity(intent);
                         getActivity().finish();
                   /**  }else {
                    //   Toast.makeText(getActivity(), jObj.getString("error_msg"), Toast.LENGTH_SHORT).show(); }

                        Toast.makeText(getActivity(),R.string.email_found, Toast.LENGTH_SHORT).show();
                }*/
                         }
                catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getContext(), "signUp error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registeratin error :" + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
               hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",username);
                params.put("email", emailCompany);
                params.put("password",passwordCompany);
                params.put("manager_name",companyName);
                params.put("site",place);
                params.put("address",companyAddress);
                params.put("jawwal",companyMobile);
                params.put("telephone",companyPhone);
                params.put("website",companyWebsite);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
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
     ///name
    private TextWatcher validationNameWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
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
    ////mobile
    private TextWatcher validationMobileWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
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

            if(!mCompanyWebsite.getText().toString().matches(urlregex)){
                txt_warning_website.setText(R.string.error_url);
            }else {
                txt_warning_website.setText("");
            }
            }@Override
        public void afterTextChanged(Editable s) {

        }
    };
    }
