package com.example.reeme.gazajob;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class BottomSheetFragment  extends BottomSheetDialogFragment {
    private static final String TAG = SignUp_User.class.getSimpleName();

    private Button btn_JobsReq,btn_applyNow,button_befor_applied;
    private TextView mJobTitle,mCompanyName,mSalary,mFinalDate,mJobType,mCompanyAddress,mSave,msave_white;
    private ProgressDialog progressDialog;
    private AppSharedPreferences sharedPreferences;
    private SessionManager sessionManager;
    String email_user;
    public BottomSheetFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.activity_bottom_sheet_fragment, container, false);


            //map
            mJobTitle = (TextView) view.findViewById(R.id.job_name);
            mCompanyName = (TextView) view.findViewById(R.id.company_name);
            mSalary = (TextView) view.findViewById(R.id.salary);
            mFinalDate = (TextView) view.findViewById(R.id.final_date);
            mJobType = (TextView) view.findViewById(R.id.jobType);
            mCompanyAddress = (TextView) view.findViewById(R.id.txtview_address);
            mSave = (TextView) view.findViewById(R.id.txt_save);
            msave_white=view.findViewById(R.id.txt_save_white);
            btn_applyNow = (Button) view.findViewById(R.id.btn_apply);
            button_befor_applied=(Button)view.findViewById(R.id.btn_befor_applied);
            //get data from activities
            Bundle mArgs = getArguments();

            final String email_company = mArgs.getString("emailCompany");
            final int career_id = mArgs.getInt("career_ID");
            String jobTitleString = mArgs.getString("jobTitle");
            String companyNameStr = mArgs.getString("companyName");
            String salaryString = mArgs.getString("salary");
            final String finalDateString = mArgs.getString("finalDate");
            String jobTypeString = mArgs.getString("jobType");
            String companyAddress = mArgs.getString("address");
            final String jobRequir = mArgs.getString("requirements");
            final String jobDescription = mArgs.getString("description");

            //initialize classes
            sharedPreferences = new AppSharedPreferences(getContext());
            sessionManager = new SessionManager(getContext());
            progressDialog=new ProgressDialog(getContext());
            //read email user of sharedPreferences
           email_user = sharedPreferences.readString(Constants.EMAIL);

            //check if logged as comapany.
            if (sessionManager.isLoggedInCompany()){
                mSave.setVisibility(View.GONE);
                btn_applyNow.setVisibility(View.GONE);
            }
              //check if came from submitted jobs
            String befor_submitted=sharedPreferences.readString(Constants.BEFOR_APPLIED_JOB);
            if(befor_submitted.equals("befor_applied")){
                button_befor_applied.setVisibility(View.VISIBLE);
                sharedPreferences.writeString(Constants.BEFOR_APPLIED_JOB,"not_applied");
            }
            //check if it came from bookmark
           String bookmark= sharedPreferences.readString(Constants.BOOKMARK);
            if(bookmark.equals("frombookmark")){
                   mSave.setVisibility(View.GONE);
                  msave_white.setVisibility(View.VISIBLE);
                sharedPreferences.writeString(Constants.BOOKMARK, "from_bookmarks");
            }

            //event to text save
                mSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sessionManager.isLoggedInUser()){
                            //method save

                            saveJobs(email_user, career_id);
                    }else {
                            //custom dialog
                            final Dialog dialog=new Dialog(getContext());
                            dialog.setContentView(R.layout.custom_dialog_user);

                            //set custom dialog components
                            TextView txt_chck=(TextView)dialog.findViewById(R.id.checkTxt);
                             txt_chck.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     //write in sharedPreference it is came from buttomSheetfragment
                                     sharedPreferences.writeString(Constants.BUTTOMSHEET,"buttomsheet");
                                     //move to signIn activity
                                     Intent intent = new Intent(getActivity(), SignIn.class);
                                     startActivity(intent);
                                     dialog.dismiss();

                                 }
                             });

                            TextView txt_close=(TextView)dialog.findViewById(R.id.closeTxt);
                            txt_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                       dialog.show();
                        }
                }}
                );

                //event to button apply now
                btn_applyNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (sessionManager.isLoggedInUser()){
                            String url_works=sharedPreferences.readString(Constants.URL_WORKS);

                            if (!url_works.isEmpty()){

                                applyToJob(url_works, email_user, email_company, career_id);

                            }else {
                                //custom dialog
                                final Dialog dialog=new Dialog(getContext());
                                dialog.setContentView(R.layout.warning_dialog_upload);
                                dialog.show();
                            }

                        }else {
                            //custom dialog
                            final Dialog dialog=new Dialog(getContext());
                            dialog.setContentView(R.layout.custom_dialog_user);

                            //set custom dialog components
                            TextView txt_chck=(TextView)dialog.findViewById(R.id.checkTxt);
                            txt_chck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //write in sharedPreference it is came from buttomSheetfragment
                                    sharedPreferences.writeString(Constants.BUTTOMSHEET,"buttomsheet");
                                    //move to signIn activity
                                    Intent intent = new Intent(getActivity(), SignIn.class);
                                    startActivity(intent);
                                    dialog.dismiss();

                                }
                            });

                            TextView txt_close=(TextView)dialog.findViewById(R.id.closeTxt);
                            txt_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }

                    }
                });



            mJobTitle.setText(jobTitleString);
            mCompanyName.setText(companyNameStr);
            mSalary.setText(salaryString);
            mFinalDate.setText(finalDateString);
            mJobType.setText(jobTypeString);
            mCompanyAddress.setText(companyAddress);

            btn_JobsReq = (Button) view.findViewById(R.id.jobRequirements);
            btn_JobsReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), JobRequirements.class);
                    intent.putExtra("Requirements", jobRequir);
                    intent.putExtra("Descriptions", jobDescription);
                    startActivity(intent);
                }
            });
           return view;
        }



    private void saveJobs(final String email_user, final int career_id) {
        // Tag used to cancel the request
        String  tag_string_req="req_saving";
        StringRequest str_req = new StringRequest(Request.Method.POST, AppConfig.URL_USER_FAVOURITES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "saving Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        mSave.setVisibility(View.GONE);
                        msave_white.setVisibility(View.VISIBLE);

                        // Launch mainActivity activity
                        Toast.makeText(getContext(),R.string.saved,Toast.LENGTH_LONG).show();;

                    } else {
                       String errormsg = jObj.getString("errormsg");
                        Toast.makeText(getContext(), R.string.error_save, Toast.LENGTH_LONG).show();
                     //   Toast.makeText(getContext(),errormsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
              //  pBar.setVisibility(View.GONE);
                Log.e(TAG, "saving error :" + error.getMessage());
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams(){
                // Posting parameters to register url
                Map<String,String> params=new HashMap<String, String>();
                params.put("user_email",email_user);
                params.put("fcareer_id", String.valueOf(career_id));
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(str_req,tag_string_req);

    }





private void applyToJob(final String url, final String email_user, final String email_company, final int career_id){
    // Tag used to cancel the request
    String  tag_string_req="applying_req";
   // progressDialog.setMessage();
    showDialog();
    StringRequest str_req = new StringRequest(Request.Method.POST, AppConfig.URL_USER_APPLY_JOB, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, "applying Response: " + response.toString());
           hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");

                if (!error) {

                    // Launch mainActivity activity
                    Toast.makeText(getContext(),R.string.submitted_successfully,Toast.LENGTH_LONG).show();;

                } else {
                     String errormsg = jObj.getString("error_msg");
                    // Toast.makeText(getContext(), R.string.couldnt_applied, Toast.LENGTH_LONG).show();
                   // Toast.makeText(getContext(),errormsg, Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),R.string.submitted_successfully,Toast.LENGTH_LONG).show();;

                }
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Log.e(TAG, "applying error :" + error.getMessage());
            Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            hideDialog();
        }
    }) {

        @Override
        protected Map<String, String> getParams(){
            // Posting parameters to register url
            Map<String,String> params=new HashMap<String, String>();

            params.put("fileurl",url);
            params.put("user_email",email_user);
            params.put("company_email",email_company);
            params.put("career_id", String.valueOf(career_id));

            return params;
        }
    };
    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(str_req,tag_string_req);

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
/**
 //checked true
 int tintColor = ContextCompat.getColor(getContext(), R.color.blue);
 Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmarklist);
 drawable = DrawableCompat.wrap(drawable);
 DrawableCompat.setTint(drawable.mutate(), tintColor);
 drawable.setBounds( 0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
 mSave.setCompoundDrawables(drawable, null, null, null);
 */