package com.example.reeme.gazajob;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookmarkFragment extends Fragment {
    private static final String TAG = BookmarkFragment.class.getSimpleName();
    CustomListViewAdapter adapter;
    ListView listView;
    private List<HomeJobs> list = new ArrayList<HomeJobs>();
    private List<HomeJobs>listCompany=new ArrayList<HomeJobs>();
    private String email_user,email_company;
    private ProgressBar pBar;
    private SessionManager session;
    private TextView txt_userApplied, txtNo_bookmarks, txt_delete,txt_postsJobs,txt_bookmarkJobs;
    private AppSharedPreferences sharedPreferences;
    private SimpleDateFormat simpleDateFormat;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_bookmark, null);
        txtNo_bookmarks = (TextView) view.findViewById(R.id.noBookmarks);
        txt_delete = (TextView) view.findViewById(R.id.delete);
        txt_userApplied = (TextView) view.findViewById(R.id.jobs_applied);
        pBar = (ProgressBar) view.findViewById(R.id.prgbar);
        listView = (ListView) view.findViewById(R.id.jobsBookmark);
        txt_postsJobs=(TextView)view.findViewById(R.id.posts_jobs);
        txt_bookmarkJobs=(TextView)view.findViewById(R.id.Bookmark_jobs);
        //email="tahani23996@gmail.com";


        //initializes the classes
        simpleDateFormat = new SimpleDateFormat("EE,MMM dd,YYY");
        sharedPreferences = new AppSharedPreferences(getContext());
        session = new SessionManager(getActivity());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        //read user email from shared
        email_user = sharedPreferences.readString(Constants.EMAIL);
       email_company=sharedPreferences.readString(Constants.COMPANY_EMAIL);
        //check session
        if (session.isLoggedInUser()) {
            txt_userApplied.setVisibility(View.VISIBLE);
            viewFavourites(email_user);

            txt_userApplied.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), JobsSubmitted_User.class);
                    startActivity(intent);
                }

            });
        } else if (session.isLoggedInCompany()) {
            txt_bookmarkJobs.setVisibility(View.GONE);
            txt_postsJobs.setVisibility(View.VISIBLE);
            //company posts
            viewCompanyPosts();
        } else {
            txtNo_bookmarks.setVisibility(View.VISIBLE);
        }


    return view;
    }


  /**  @Override
    public void onStart() {
        super.onStart();

    }
  */
    //view every favourites for user

    public void viewFavourites(final String email) {
        pBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_VIEW_FAVOURITE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pBar.setVisibility(View.INVISIBLE);

                try {
                    //getting the whole json object from the response
                    JSONObject object = new JSONObject(response);
                    boolean error = object.getBoolean("error");
                    if (!error) {
                        //we have array named jobsArray inside the object
                        //so here we are getting that json array
                        JSONArray jobsArray = object.getJSONArray("favourites");
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
                        adapter = new CustomListViewAdapter(getActivity(), list);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                                HomeJobs jobs = list.get(id);
                                Bundle args = new Bundle();
                                sharedPreferences.writeString(Constants.BOOKMARK, "frombookmark");

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

                                //came from bookmark fragment
                            }
                        });

                        //long click on item
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                                view.setSelected(true);
                               // adapter.show_checkedDelete();
                                txt_delete.setEnabled(true);
                                final HomeJobs jobs = list.get(position);
                                final int career_id = jobs.getCarrerId();
                                txt_delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        //custom dialog
                                        final Dialog dialog = new Dialog(getContext());
                                        dialog.setContentView(R.layout.custom_dialog_confirm_delete);

                                        //  if(jobs.)
                                        //set custom dialog components
                                        TextView txt_chck = (TextView) dialog.findViewById(R.id.checkTxt);
                                        txt_chck.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                txt_delete.setEnabled(false);
                                                delete_favourite_Item(career_id, email_user);
                                                }});

                                        TextView txt_close = (TextView) dialog.findViewById(R.id.closeTxt);
                                        txt_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            //    adapter.hide_checkedDelete();
                                              //  listView.setOnLongClickListener(null);
                                                 txt_delete.setEnabled(false);
                                                 view.setSelected(false);
                                            }
                                        });
                                        dialog.show();
                                        }});
                                return true; }});
                        } else {
                        //String errormsg = object.getString("errormsg");
                        Toast.makeText(getContext(),R.string.no_bookmark, Toast.LENGTH_LONG).show();
                        txtNo_bookmarks.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"error bookmark", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pBar.setVisibility(View.INVISIBLE);
                error.printStackTrace();
                Log.e(TAG, "bookmark error :" + error.getMessage());
               // Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_email", email);
                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);

    }



public  void delete_favourite_Item(final int careerId, final String email_user){
    // Tag used to cancel the request
    String tag_string_req = "req_delete_user";
    progressDialog.setMessage(getString(R.string.startDeleting));
    showDialog();
    StringRequest stringRequest=new StringRequest(Request.Method.POST,AppConfig.URL_DELETE_USER_FAVOURITES,new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          hideDialog();
            Log.d(TAG, "delete Response :" + response.toString());

            try {

                JSONObject jsonObject = new JSONObject(response);

                boolean error= jsonObject.getBoolean("error");

                // Check for error node in json
                if(!error){
                   // adapter.notifyDataSetChanged();
                    //progressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                    list.clear();
                    viewFavourites(email_user);
                    Toast.makeText(getContext(), R.string.deleted, Toast.LENGTH_SHORT).show();

                } else {

                 //   String errormsg = jsonObject.getString("errormsg");
                    Toast.makeText(getContext(),R.string.failed_delete, Toast.LENGTH_LONG).show();
                }

            }catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(getContext(), "delete error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    },new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
            Log.d(TAG,"delete Error :" +error.getMessage());
          //  Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
             hideDialog();
        }
    }){@Override
        protected Map<String, String> getParams() {
            // Posting parameters to login url
            Map<String, String> params = new HashMap<String, String>();
            params.put("career_id", String.valueOf(careerId));
            params.put("user_email", email_user);
            return params; }

    };// Adding request to request queue
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

public void viewCompanyPosts(){
        pBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_VIEW_COMPANY_POSTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pBar.setVisibility(View.INVISIBLE);

                try {
                    //getting the whole json object from the response
                    JSONObject object = new JSONObject(response);
                    boolean error = object.getBoolean("error");
                    if (!error) {

                        // /we have array named jobsArray inside the object
                        //so here we are getting that json array
                        JSONArray jobsArray = object.getJSONArray("posts");
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
                            listCompany.add(dataSet);


                        }
                        adapter = new CustomListViewAdapter(getActivity(), listCompany);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                                HomeJobs jobs = listCompany.get(id);
                                Bundle args = new Bundle();
                                sharedPreferences.writeString(Constants.BOOKMARK, "frombookmark");

                                args.putString("jobTitle", jobs.getJobtitle());
                                args.putString("companyName", jobs.getCompanyname());
                                args.putString("salary", jobs.getSalary());
                                args.putString("finalDate", jobs.getFinaldate());
                                args.putString("jobType", jobs.getJobtype());
                                args.putString("requirements", jobs.getRequirements());
                                args.putString("description", jobs.getSpecification());
                                args.putString("address", jobs.getCompanyaddress());
                                args.putInt("career_ID", jobs.getCarrerId());
                                args.putString("emailCompa", jobs.getEmailCompany());

                                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                                bottomSheetFragment.setArguments(args);
                                bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

                                //came from bookmark fragment
                            }
                        });

                        //long click on item
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                                view.setSelected(true);
                                txt_delete.setEnabled(true);

                                final HomeJobs jobs = listCompany.get(position);
                                final int career_id = jobs.getCarrerId();
                                txt_delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        //custom dialog
                                        final Dialog dialog = new Dialog(getContext());
                                        dialog.setContentView(R.layout.custom_dialog_confirm_delete);

                                        //  if(jobs.)
                                        //set custom dialog components
                                        TextView txt_chck = (TextView) dialog.findViewById(R.id.checkTxt);
                                        txt_chck.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                txt_delete.setEnabled(false);
                                                delete_company_posts_Item(career_id);
                                                }});
                                        TextView txt_close = (TextView) dialog.findViewById(R.id.closeTxt);
                                        txt_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                //adapter.hide_checkedDelete();
                                              //  adapter.hide_checkedDelete();
                                                //  listView.setOnLongClickListener(null);
                                                txt_delete.setEnabled(false);
                                                view.setSelected(false);
                                                listView.setOnLongClickListener(null); }});
                                        dialog.show(); }});
                                return true; }});
                    } else {
                        //String errormsg = object.getString("errormsg");
                        //Toast.makeText(getContext(),R.string.no_bookmark, Toast.LENGTH_LONG).show();
                        txtNo_bookmarks.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pBar.setVisibility(View.INVISIBLE);
                error.printStackTrace();
                Log.e(TAG, "postsJobs error :" + error.getMessage());
               // Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("company_email",email_company );
                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);

    }



public void delete_company_posts_Item(final int career_id) {
    // Tag used to cancel the request
    String tag_string_req = "req_delete_company";
    progressDialog.setMessage(getString(R.string.deleting));
    showDialog();
    StringRequest stringRequest=new StringRequest(Request.Method.POST,AppConfig.URL_DELETE_COMPANY_JOBS,new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideDialog();
            Log.d(TAG, "delete Response :" + response.toString());
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean error= jsonObject.getBoolean("error");
                // Check for error node in json
                if(!error){
                    // adapter.notifyDataSetChanged();
                    //progressDialog.dismiss();
                     adapter.notifyDataSetChanged();
                     listCompany.clear();
                     viewCompanyPosts();
                    Toast.makeText(getContext(), R.string.deleted, Toast.LENGTH_SHORT).show(); }

                else {

                  //  String errormsg = jsonObject.getString("errormsg");
                    Toast.makeText(getContext(),R.string.failed_delete, Toast.LENGTH_LONG).show();
                }

            }catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(getContext(), "delete error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    },new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
            Log.d(TAG,"delete Error :" +error.getMessage());
         //   Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            hideDialog();
        }
    }){@Override
    protected Map<String, String> getParams() {
        // Posting parameters to login url
        Map<String, String> params = new HashMap<String, String>();
        params.put("career_id", String.valueOf(career_id));

        return params; }

    };// Adding request to request queue
    AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
}
}
/**
    private String formateDate(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE,MMM dd,YYY");
            Date date = simpleDateFormat.parse(dateStr);
            return simpleDateFormat.format(date);

        } catch (ParseException e) {

        }
        return "";
    }*/