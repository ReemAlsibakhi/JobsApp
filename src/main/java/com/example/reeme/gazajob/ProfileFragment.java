package com.example.reeme.gazajob;


import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.DataBase.SessionManager;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    private TextView mName,mUseremaiL,mUsermobile,mTxt_settings,mTxt_appliedJobs,mTxt_update;
    private Button mBtn_uploadCv;
    Activity context;
   private AppSharedPreferences sharedPreferences;
   private SessionManager session;
    private static final String TAG = ProfileFragment.class.getSimpleName();
    final String urlregex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        mTxt_appliedJobs=(TextView)v.findViewById(R.id.appliedJobs);
        mTxt_settings = (TextView)v.findViewById(R.id.settings);
        mBtn_uploadCv=(Button)v.findViewById(R.id.uploadResume);
        mTxt_update=v.findViewById(R.id.update);
        mName=(TextView)v.findViewById(R.id.name);
        mUseremaiL  = (TextView)v.findViewById(R.id.email_txt);
        mUsermobile=(TextView)v.findViewById(R.id.mobile_txt);


        sharedPreferences=new AppSharedPreferences(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);


        final String email =sharedPreferences.readString(Constants.EMAIL);
      //  final String password=sharedPreferences.readString(Constants.USER_PASSWORD);
        final String username =sharedPreferences.readString(Constants.USER_NAME);
        final String mobile =sharedPreferences.readString(Constants.MOBILE);
        final  String update=sharedPreferences.readString(Constants.UPDATE_USER);
     /**   if (update.equals("update_user")){
            readData_user(email,password);
        }
      */


        mName.setText(username);
        mUseremaiL.setText(email);
        mUsermobile.setText(mobile);

        session=new SessionManager(getActivity());


        mTxt_update.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent =new Intent(getActivity(),update_user_data.class);
        startActivity(intent);
    }
});

        mTxt_settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                final PopupMenu popup = new PopupMenu(getActivity(), mTxt_settings);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        session.setLoginUser(false);
                        sharedPreferences.writeString(Constants.FILTER, "logout");
                        sharedPreferences.writeString(Constants.PROFILE, "profile");
                      //  sharedPreferences.writeString(Constants.Register_User,"no_Register");
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method
         mTxt_appliedJobs.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(),JobsSubmitted_User.class);
        startActivity(intent);
    }
    }
    );

        //upload file to api
        mBtn_uploadCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //custom dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_dialog_upload);

                //set custom dialog components
                final EditText editText_url=(EditText)dialog.findViewById(R.id.upload_resume);
                Button btn_upload=(Button)dialog.findViewById(R.id.btn_apply);

                 //event on button
                btn_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         String url=editText_url.getText().toString().trim();
                        if(url.matches(urlregex)){
                            sharedPreferences.writeString(Constants.URL_WORKS,url);
                            dialog.dismiss();
                            Toast.makeText(getActivity(), R.string.upload_successfully, Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getActivity(), R.string.please_url, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });



    return v;
    }

    /**
public void readData_user(final String email,final  String password){
    // Tag used to cancel the request
    String tag_string_req = "req_login";
    StringRequest stringRequest=new StringRequest(Request.Method.POST,AppConfig.URL_LOGIN,new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {

                JSONObject jsonObject = new JSONObject(response);
                boolean error= jsonObject.getBoolean("error");
                // Check for error node in json
                if(!error){

                        JSONObject user =jsonObject.getJSONObject("user");


                        String username=user.getString("name");
                        String email=user.getString("email");
                        String jawwal=user.getString("jawwal");

                        sharedPreferences.writeString(Constants.USER_NAME,username);
                        sharedPreferences.writeString(Constants.EMAIL,email);
                        sharedPreferences.writeString(Constants.MOBILE,jawwal);

                    }else {


                    }
                      /**  try{
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
                            String address_company=company.getString("address");
                            session.setLoginCompany(true);
                            appSharedPreferences.writeString(Constants.COMPANY_USER,username_company);
                            appSharedPreferences.writeString(Constants.COMPANY_NAME,name_company);
                            appSharedPreferences.writeString(Constants.COMPANY_EMAIL,email_company);
                            appSharedPreferences.writeString(Constants.COMPANY_MOBILE,jawwal_company);
                            appSharedPreferences.writeString(Constants.SITE_COMPANY,palce_company);
                            appSharedPreferences.writeString(Constants.WEBSITE_COMPANY,website_company);
                            appSharedPreferences.writeString(Constants.PHONE_COMPANY,phone_company);
                            appSharedPreferences.writeString(Constants.ADDRESS_COMPANY,address_company);

                            //read from welcome activity.-> advertise
                            String advertise=appSharedPreferences.readString(Constants.ADVERTISE);
                            if (advertise.equals("welcome_advertise")){
                                Toast.makeText(SignIn.this, R.string.Login_successfully, Toast.LENGTH_SHORT).show();
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


                    }catch (JSONException e) {
                e.printStackTrace();
               // Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    },new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
        }
    }){
        @Override
        protected Map<String, String> getParams() {
            // Posting parameters to login url
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);
            params.put("password", password);

            return params;
        }
    };
    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
}
*/

}
