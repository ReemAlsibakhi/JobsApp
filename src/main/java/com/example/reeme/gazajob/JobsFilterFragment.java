package com.example.reeme.gazajob;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsFilterFragment extends Fragment {
    private TextView textView;
    CustomListViewAdapter adapter;
    ListView listView;
    private List<HomeJobs> listFilter = new ArrayList<HomeJobs>();
    private List<HomeJobs> listSearch = new ArrayList<HomeJobs>();
    private AppSharedPreferences appSharedPreferences;
    private int minPrice, maxPrice = 0;
    private String specialize, place, type;
    private ProgressBar pBar;
    private TextView txt_arrowBack,txt_notFound,txt_title;
    private SearchView txt_search;
    private RelativeLayout relativeLayout_search;
    private static final String TAG = JobsFilterFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs_filter, null);

         pBar=(ProgressBar)view.findViewById(R.id.prgbar);
         listView=(ListView)view.findViewById(R.id.listFilter);
         textView = (TextView) view.findViewById(R.id.filter);
         txt_search=view.findViewById(R.id.search);

         txt_notFound=view.findViewById(R.id.txt_notFound);
         txt_title=view.findViewById(R.id.txt_job);

        // Inisialize Class
        appSharedPreferences = new AppSharedPreferences(getActivity());



        //event to filter
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterSide.class);
                startActivity(intent);
            }
        });
          //filter
        String filter = appSharedPreferences.readString(Constants.FILTER);
        if (filter.equals("012")){
            Log.v(Constants.LOG+1, filter);
            minPrice = appSharedPreferences.readInteger(Constants.MIN_PRICE);
            maxPrice = appSharedPreferences.readInteger(Constants.MAX_PRICE);
            specialize = appSharedPreferences.readString(Constants.SPECIALIZE);
            place = appSharedPreferences.readString(Constants.PLACE);
            type = appSharedPreferences.readString(Constants.TYPE);
            Log.v(Constants.LOG, minPrice+" , "+maxPrice+" , "+specialize+" , "+place+" , "+type);
            listFilter.clear();
            load(AppConfig.URL_JOB_FILTER_API, "result", Request.Method.POST);
            appSharedPreferences.writeString(Constants.FILTER, "210");
        }else{
            Log.v(Constants.LOG+1, filter);
            load(AppConfig.URL_RECENT_JOBS, "all", Request.Method.GET);

        }
     /**
      //event to search
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               relativeLayout_search.setVisibility(View.VISIBLE);
               txt_arrowBack.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       relativeLayout_search.setVisibility(View.INVISIBLE);

                   }
               });
            }
        });

*/


    txt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(final  String query) {

        //   adapter= new CustomListViewAdapter(getActivity(), listSearch);
        //  listView.setAdapter(adapter);
     //   listFilter.clear();
        if (txt_notFound.isShown()){
            txt_notFound.setVisibility(View.INVISIBLE);
        }

        pBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   MyCustomAdapter jobadapter;
              pBar.setVisibility(View.INVISIBLE);
                try {

                    JSONObject jobj = new JSONObject(response);
                    boolean error = jobj.getBoolean("error");
                    if (!error) {
                        JSONArray search_jobs = jobj.getJSONArray("result");
                        // ArrayList<Job> jobs = new ArrayList<Job>();
                        //read jobs
                        for (int i = 0; i < search_jobs.length(); i++) {
                            JSONObject obj = search_jobs.getJSONObject(i);
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
                            listSearch.add(dataSet);
                        }
                        //   Toast.makeText(MainActivity.this, "here", Toast.LENGTH_SHORT).show();
                        adapter = new CustomListViewAdapter(getActivity(), listSearch);
                      //  pBar.setVisibility(View.INVISIBLE);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                HomeJobs jobs = listSearch.get(position);
                                Bundle args = new Bundle();

                                args.putString("jobTitle", jobs.getJobtitle());
                                args.putString("companyName", jobs.getCompanyname());
                                args.putString("salary", jobs.getSalary());
                                args.putString("finalDate", jobs.getFinaldate());
                                args.putString("jobType", jobs.getJobtype());
                                args.putString("requirements", jobs.getRequirements());
                                args.putString("description", jobs.getSpecification());
                                args.putString("address", jobs.getCompanyaddress());
                                args.putInt("career_ID", jobs.getCarrerId());
                                args.putString("emailCompany", jobs.getEmailCompany());

                                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                                bottomSheetFragment.setArguments(args);
                                bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());


                            }
                        });
                    }else {
                        //
                        Toast.makeText(getActivity(), R.string.not_found, Toast.LENGTH_SHORT).show();
                        //txt_notFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    pBar.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
              pBar.setVisibility(View.INVISIBLE);
            }  }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", query);
                //    Toast.makeText(MainActivity.this, "getparams", Toast.LENGTH_SHORT).show();
                return params;


            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //   array.getFilter().filter(newText);
        //clear adapter
        return false;
    }
});
        //search
        String search = appSharedPreferences.readString(Constants.SEARCH_HOME);
        if(search.equals("search")){
           /** relativeLayout_search.setVisibility(View.VISIBLE);
            txt_arrowBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relativeLayout_search.setVisibility(View.INVISIBLE);
                    appSharedPreferences.writeString(Constants.SEARCH_HOME,"no_search");

                }
            });
            */
        }
        return  view;
    }


    public void load(String url, final String jsonName, int method){

        pBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest=new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pBar.setVisibility(View.INVISIBLE);

                    try {
                        //getting the whole json object from the response
                        JSONObject object = new JSONObject(response);
                        boolean error = object.getBoolean("error");
                        if (!error) {

                            JSONArray jobsArray = object.getJSONArray(jsonName);
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
                                listFilter.add(dataSet);

                            }
                            adapter = new CustomListViewAdapter(getActivity(), listFilter);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    HomeJobs jobs = listFilter.get(position);
                                    Bundle args = new Bundle();

                                    args.putString("jobTitle", jobs.getJobtitle());
                                    args.putString("companyName", jobs.getCompanyname());
                                    args.putString("salary", jobs.getSalary());
                                    args.putString("finalDate", jobs.getFinaldate());
                                    args.putString("jobType", jobs.getJobtype());
                                    args.putString("requirements", jobs.getRequirements());
                                    args.putString("description", jobs.getSpecification());
                                    args.putString("address", jobs.getCompanyaddress());
                                    args.putInt("career_ID", jobs.getCarrerId());
                                    args.putString("emailCompany", jobs.getEmailCompany());

                                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                                    bottomSheetFragment.setArguments(args);
                                    bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());


                                }
                            });
                        }else {
                             txt_notFound.setVisibility(View.VISIBLE);

                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } ,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "filter error :" + error.getMessage());
             //   Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                pBar.setVisibility(View.INVISIBLE);

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(Constants.MIN_PRICE, String.valueOf(minPrice));
                params.put(Constants.MAX_PRICE, String.valueOf(maxPrice));
                params.put(Constants.PLACE, place);
                params.put(Constants.SPECIALIZE, specialize);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public void search(String query){


    }

}
