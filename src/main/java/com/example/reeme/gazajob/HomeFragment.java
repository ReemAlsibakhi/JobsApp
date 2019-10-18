package com.example.reeme.gazajob;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;
import com.example.reeme.gazajob.model.HomeJobs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {
    CustomListViewAdapter adapter;
    ListView listView;
    private TextView txt_search;
    private ProgressBar pBar;
    private List<HomeJobs> jobslist = new ArrayList<HomeJobs>();
    private static final String TAG = HomeFragment.class.getSimpleName();
    private AppSharedPreferences sharedPreferences;
    private TextView txt_notFound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_home, null);
        txt_search=(TextView)view.findViewById(R.id.search);
        listView = (ListView) view.findViewById(R.id.listHome);
        pBar=(ProgressBar)view.findViewById(R.id.prgbar);
        txt_notFound=view.findViewById(R.id.txt_notFound);
           //initialize the class.
       sharedPreferences=new AppSharedPreferences(getContext());


       txt_search.setOnClickListener(new View.OnClickListener() {
         @Override
          public void onClick(View v) {
       sharedPreferences.writeString(Constants.SEARCH_HOME,"search");
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        getActivity().finish();

    }
});
        loadLastJobs();


        return view;
    }
      public void loadLastJobs(){
            pBar.setVisibility(View.VISIBLE);
          StringRequest stringRequest=new StringRequest(AppConfig.URL_RECENT_JOBS, new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                     pBar.setVisibility(View.INVISIBLE);
                  try {
                      //getting the whole json object from the response
                      final JSONObject object=new JSONObject(response);
                      boolean error = object.getBoolean("error");
                      if (!error) {
                      //we have the array named jobsArray inside the object
                      //so here we are getting that json array
                      JSONArray jobsArray=object.getJSONArray("all");
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
                          jobslist.add(dataSet);
                      }
                              adapter = new CustomListViewAdapter(getActivity(), jobslist);
                              listView.setAdapter(adapter);
                              adapter.notifyDataSetChanged();

                      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                          @Override
                          public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                               HomeJobs jobs=jobslist.get(position);
                                 Bundle args = new Bundle();

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
                                  bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

                                  }
                                 });
                                     }
                                     else {
                              //String errormsg = object.getString("errormsg");
                              txt_notFound.setVisibility(View.VISIBLE);

                          }

                      } catch (JSONException e) {
                              e.printStackTrace();
                      Toast.makeText(getActivity(), "there error", Toast.LENGTH_SHORT).show();
                          }
                      }
                  }
                  ,new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  error.printStackTrace();
                  Log.e(TAG, "loading last jobs error :" + error.getMessage());
             //    Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                  pBar.setVisibility(View.INVISIBLE);

              }
          });

          // Adding request to request queue
          AppController.getInstance().addToRequestQueue(stringRequest);

      }


}
