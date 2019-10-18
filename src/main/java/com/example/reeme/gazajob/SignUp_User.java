
package com.example.reeme.gazajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SignUp_User extends Fragment{

   private static final String TAG = SignUp_User.class.getSimpleName();
    private  ArrayAdapter<String>adapter;
    private  String[] specialzeList;
    private CheckBox mobileNum_checkBox,email_checkBox;
    private EditText EditSearch,EditPhone;
    private ListView listView_specialize;
    private Button btn_signUp;
    private AppSharedPreferences appSharedPreferences;
    private SessionManager session;
    private ProgressDialog progressDialog;
    private TextView txt_userJawwwal;
    final String jawwalregex = "05[69][245789][0-9]{6}";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       final View v = inflater.inflate(R.layout.activity_signup_user, null);



        //Generate sample data.
        EditPhone=(EditText)v.findViewById(R.id.phoneEdit);
        mobileNum_checkBox = (CheckBox)v.findViewById(R.id.mobile_checkBox);
        email_checkBox = (CheckBox)v.findViewById(R.id.email_checkBox);
        EditSearch= (EditText)v.findViewById(R.id.searchEdit);
        listView_specialize=(ListView)v.findViewById(R.id.listview);
        btn_signUp = (Button)v.findViewById(R.id.btn_signUp_Apply);
        txt_userJawwwal=v.findViewById(R.id.userJawwwal);

        //initialze the classes
        appSharedPreferences = new AppSharedPreferences(getContext());
        session=new SessionManager(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
         //
        EditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!EditPhone.getText().toString().matches(jawwalregex)){
                txt_userJawwwal.setText(R.string.invalid_mobile_Input);
            }else {
                txt_userJawwwal.setText("");
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        specialzeList=getResources().getStringArray(R.array.fields);
        adapter=new ArrayAdapter<String>(getActivity(),R.layout.simple_list_item_multiple_choice,specialzeList);
        listView_specialize.setAdapter(adapter);
        listView_specialize.setChoiceMode(listView_specialize.CHOICE_MODE_MULTIPLE);



        listView_specialize.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        //get data from SignUp Activity.
        final String Username =getActivity().getIntent().getStringExtra("NAME");
        final String Email = getActivity().getIntent().getStringExtra("EMAIL");
        final String Password = getActivity().getIntent().getStringExtra("PASSWORD");

         //filter
       initEvent();


        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray checked=listView_specialize.getCheckedItemPositions();
                ArrayList<String> selectItems=new ArrayList<String>();
                for (int i=0;i<checked.size();i++){
                    //item position in adapter.
                    int position=checked.keyAt(i);
                    //Add specialize if it is checked )==TRUE.
                    if(checked.valueAt(i))
                        selectItems.add(adapter.getItem(position));
                }
                String[] outputStrArr=new String[selectItems.size()];
                for(int i=0; i<selectItems.size() ;i++){
                    outputStrArr[i]=selectItems.get(i);

                   // Toast.makeText(getContext(), Arrays.toString(outputStrArr),Toast.LENGTH_LONG).show();
                }

                boolean email;
                boolean mobile;
                String mobileAlert,emailAlert="0";

                String userMobile = EditPhone.getText().toString().trim();
             //  final String interest=EditInterest.getText().toString().trim();


               mobile = mobileNum_checkBox.isChecked();
               email= email_checkBox.isChecked();
             if (mobile==true){
                 mobileAlert="1";
             }else {
                 mobileAlert="0";
             }
             if(email==true){
                 emailAlert="1";

             }else {
                 emailAlert="0";
             }
               if(userMobile.matches(jawwalregex)){
                   register(Username, Email, Password, userMobile, mobileAlert, emailAlert, outputStrArr);

               }else {
                   Toast.makeText(getContext(), R.string.Please_enter_mobile, Toast.LENGTH_LONG).show();
               }

            }
        });


        return v;
    }


    // filter
    private void initEvent() {

        EditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //filter
                SignUp_User.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void register(final String username, final String email, final String password,final String mobile,  final String jawwalalert, final String emailalert,final String[] outputStrArr) {
        // Tag used to cancel the request
      String  tag_string_req="req_register";
        progressDialog.setMessage(getString(R.string.registering));
              showDialog();
        StringRequest str_req = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER_User, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
             hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                     if(!error) {
                         Toast.makeText(getActivity(), R.string.confirm_application, Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(getActivity(), SignIn.class);
                         startActivity(intent);
                         getActivity().finish();
                     }else {
                        // Toast.makeText(getActivity(), jObj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                         Toast.makeText(getActivity(), R.string.email_found, Toast.LENGTH_SHORT).show();
                     }
                       //

                } catch (JSONException ex) {
                    ex.printStackTrace();
                    Toast.makeText(getContext(), "signup error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Registeratin error :" + error.getMessage());
            Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                 hideDialog();
                      }
        }) {

          @Override
          protected Map<String, String> getParams(){
              // Posting parameters to register url
             Map<String,String> params=new HashMap<String, String>();
              params.put("name",username);
              params.put("email",email);
              params.put("password",password);
              params.put("jawwal",mobile);
              params.put("jawwalalert",jawwalalert);
              params.put("emailalert",emailalert);
              params.put("job",TextUtils.join(",",outputStrArr));
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



