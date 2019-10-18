package com.example.reeme.gazajob;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.example.reeme.gazajob.model.HomeJobs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsSubmitted_User extends AppCompatActivity {
    private static final String TAG = JobsSubmitted_User.class.getSimpleName();
    CustomListViewAdapter adapter;
    ListView listView;
    private List<HomeJobs> list = new ArrayList<HomeJobs>();
    private ProgressBar pBar;
    private TextView back_arrow,notSubmitted;
    private AppSharedPreferences sharedPreferences;
    String user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_submitted__user);

        back_arrow=findViewById(R.id.back);
        notSubmitted=findViewById(R.id.not_Submitted);
        pBar=(ProgressBar)findViewById(R.id.prgbar);
        listView=(ListView)findViewById(R.id.jobsSubmitted);
          //initialize the class
        sharedPreferences=new AppSharedPreferences(JobsSubmitted_User.this);
        //event
     back_arrow.setOnClickListener(new View.OnClickListener() {
        @Override
    public void onClick(View v) {
            finish();
    }
    });

    user_email=sharedPreferences.readString(Constants.EMAIL);

     //email="tahani23996@gmail.com";

           viewJobsSubmitted();
    }

    public void viewJobsSubmitted(){

        pBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,AppConfig.URL_VIEW_USER_JOBSAPPLIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pBar.setVisibility(View.INVISIBLE);

                try {
                    //getting the whole json object from the response
                    JSONObject object=new JSONObject(response);
                    boolean error = object.getBoolean("error");
                    if (!error) {
                        //we have array named jobsArray inside the object
                        //so here we are getting that json array
                        JSONArray jobsArray=object.getJSONArray("applies");
                        //now looping through all the elements of the jsonlastjobs array
                        for (int i = 0; i < jobsArray.length(); i++) {

                            JSONObject obj = jobsArray.getJSONObject(i);
                            HomeJobs dataSet = new HomeJobs();
                            dataSet.setJobtitle(obj.getString("name"));
                            dataSet.setCompanyname(obj.getString("company_name"));
                            dataSet.setPostdate(obj.getString("post_date"));
                            dataSet.setSalary(obj.getString("salary"));
                            dataSet.setFinaldate(obj.getString("finaldate"));
                            dataSet.setJobtype(obj.getString("jobtype"));
                            dataSet.setRequirements(obj.getString("requirements"));
                            dataSet.setSpecification(obj.getString("describe_job"));
                            dataSet.setCompanyaddress(obj.getString("company_address"));
                            dataSet.setCarrerId(obj.getInt("career_id"));
                            dataSet.setEmailCompany(obj.getString("company_email"));
                            list.add(dataSet);


                        }
                        adapter = new CustomListViewAdapter(JobsSubmitted_User.this, list);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                HomeJobs jobs=list.get(position);
                                Bundle args = new Bundle();
                            sharedPreferences.writeString(Constants.BEFOR_APPLIED_JOB,"befor_applied");
                                args.putString("jobTitle", jobs.getJobtitle());
                                args.putString("companyName", jobs.getCompanyname());
                                args.putString("salary", jobs.getSalary());
                                args.putString("finalDate", jobs.getFinaldate());
                                args.putString("jobType", jobs.getJobtype());
                                args.putString("requirements", jobs.getRequirements());
                                args.putString("description", jobs.getSpecification());
                                args.putString("address", jobs.getCompanyaddress());
                                args.putInt("career_ID",jobs.getCarrerId());
                                args.putString("emailCompany",jobs.getEmailCompany());

                                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                                bottomSheetFragment.setArguments(args);
                                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());


                            }
                        });
                    }else {
                       // String errormsg = object.getString("errormsg");
                       Toast.makeText(getApplicationContext(), R.string.arent_applied, Toast.LENGTH_LONG).show();
                       notSubmitted.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_LONG).show();

                }
            }
        } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pBar.setVisibility(View.INVISIBLE);
                error.printStackTrace();
                Log.e(TAG, "bookmark error :" + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams(){
                // Posting parameters to register url
                Map<String,String> params=new HashMap<String, String>();
                params.put("user_email",user_email);
                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}
