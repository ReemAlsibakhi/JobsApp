package com.example.reeme.gazajob;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.Data.UserContract;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PublishJob extends AppCompatActivity {
    private static final String TAG = SignUp_User.class.getSimpleName();
    private ProgressDialog pDialog;

    EditText mDesription,mRequirements,mJobTitle,mSalary;
    Spinner mJobType;
    Button btn_publish;
    private EditText EditSearch;
    private ListView listView_specialize;
    private  ArrayAdapter<String>adapter;
    private  String[] specialzeList;
    private String JobType= UserContract.Full_time;
    private TextView dateView,mCloseDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int year, month, day;
    AppSharedPreferences preferences;
    SimpleDateFormat simpleDateFormat;
    final  String nameRegex = ".{1,50}";//medium
    final  String nameRegexSmall = ".{3,25}";//small
    final  String nameRegexLarge=".{0,25}";//large

    private TextView txtFinal_date,txt_description,txt_requirement,txt_jobTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ajob);




        preferences =new AppSharedPreferences(PublishJob.this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final String email =preferences.readString(Constants.COMPANY_EMAIL);
        final String password =preferences.readString(Constants.Company_password);
        final String place =preferences.readString(Constants.SITE_COMPANY);


        mDesription = (EditText) findViewById(R.id.descripEditText);
        mRequirements=(EditText) findViewById(R.id.requirEditText);
        mJobTitle=(EditText) findViewById(R.id.jobTitleEditText);
        mSalary=(EditText) findViewById(R.id.salaryEditText);
        mJobType=(Spinner) findViewById(R.id.jobTypeSpinner);
        mCloseDate=(TextView) findViewById(R.id.closeDateEditText);
        EditSearch= (EditText)findViewById(R.id.searchEdit);
        listView_specialize=(ListView)findViewById(R.id.listview);
        btn_publish=(Button) findViewById(R.id.publish_btn);
        txtFinal_date=findViewById(R.id.finalDate);
        txt_description=findViewById(R.id.inValidDescription);
        txt_requirement=findViewById(R.id.inValidRequirements);
        txt_jobTitle=findViewById(R.id.inValidJobTitle);

   //     mDesription.addTextChangedListener(validationDescriptionWatcher);
    //    mRequirements.addTextChangedListener(validationRequirementsWatcher);
        mJobTitle.addTextChangedListener(validationJobTitleWatcher);


        specialzeList=getResources().getStringArray(R.array.fields);
        adapter=new ArrayAdapter<String>(this,R.layout.simple_list_item_multiple_choice,specialzeList);
        listView_specialize.setAdapter(adapter);
        listView_specialize.setChoiceMode(listView_specialize.CHOICE_MODE_MULTIPLE);

        dateView = (TextView) findViewById(R.id.dataTextView);

        listView_specialize.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        dateView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(PublishJob.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });
       mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                 simpleDateFormat=new SimpleDateFormat("d,M,YYY");

 //    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EE,MMM dd,YYY");
            Calendar newDate=Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            newDate.set(year,month,day);
            String selectedDate=simpleDateFormat.format(newDate.getTime());
              mCloseDate.setText(selectedDate);

                if (today.getTimeInMillis() > newDate.getTimeInMillis()){
                      txtFinal_date.setText(R.string.invalid_date);
                   // Toast.makeText(PublishJob.this, R.string.invalid_date, Toast.LENGTH_SHORT).show();

                }
                else {
                    txtFinal_date.setText("");
                }
            }
        };
        setUpSpinner();

        /**
            Date nowDate=new Date(System.currentTimeMillis());
            result=nowDate.compareTo();
           */
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SparseBooleanArray checked=listView_specialize.getCheckedItemPositions();
                ArrayList<String> selectItems=new ArrayList<String>();
                for (int i=0;i<checked.size();i++){
                    //item position in adapter.
                    int position=checked.keyAt(i);
                    //Add specialize if it is checked )==TRUE.
                    if(checked.valueAt(i))
                        selectItems.add(adapter.getItem(position)); }
                String[] outputStrArr=new String[selectItems.size()];
                for(int i=0; i<selectItems.size() ;i++){
                    outputStrArr[i]=selectItems.get(i);
                  //  Toast.makeText(getApplicationContext(), Arrays.toString(outputStrArr),Toast.LENGTH_LONG).show();
                }

                String description = mDesription.getText().toString().trim();
                String requirements = mRequirements.getText().toString().trim();
                String jobTitle = mJobTitle.getText().toString().trim();
                String salary = mSalary.getText().toString().trim();
                String closeDate = mCloseDate.getText().toString().trim();
             //   Calendar today = Calendar.getInstance();

                if (!description.isEmpty() && !requirements.isEmpty()
                        && jobTitle.matches(nameRegexSmall) &&jobTitle.matches(nameRegexLarge) && !salary.isEmpty()
                        && !closeDate.isEmpty()) {

                        publisher(email, password, place, description, requirements, jobTitle, salary, JobType, closeDate, outputStrArr);


                  // Toast.makeText(getApplicationContext(),R.string.Advertising_done, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),R.string.please_data, Toast.LENGTH_LONG).show();
                }
            }
        });

        //filter
        initEvent();
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
                PublishJob.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the JobtYPE of the user.
     */

    private void setUpSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jobType,R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        mJobType.setAdapter(adapter);

        // Set the integer mSelected to the constant values
        mJobType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = parent.getItemAtPosition(position).toString();

                if (!TextUtils.isEmpty(selection)) {
                        JobType=selection;
                   /** if (selection.equals(getString(R.string.Full_Time))) {
                        JobType = selection;
                    } else if (selection.equals(getString(R.string.Part_Time))) {
                        JobType = UserContract.Part_time;
                    } else if (selection.equals(getString(R.string.Full_Part))) {
                        JobType = UserContract.Part_Full_time;

                    }*/
                }
            }
            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                JobType = UserContract.Full_time;
            }
        });
    }


    private  void  publisher(final String email,final String password,final String place,final String description, final String requirements, final String jobTitle, final String salary, final String jobType, final String closeDate,final String[] outputStrArr){

        // Tag used to cancel the request
        String  tag_string_req="req_publish";
        pDialog.setMessage(getString(R.string.publish));
        showDialog();
        StringRequest str_req = new StringRequest(Request.Method.POST, AppConfig.URL_POST_JOB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "publish Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                   boolean error = jObj.getBoolean("error");
                     if(!error){
                         preferences.writeString(Constants.SEARCH_HOME,"no_search");
                         preferences.writeString(Constants.PROFILE,"noProfile");
                         Toast.makeText(getApplicationContext(), R.string.published, Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(PublishJob.this,MainActivity.class);
                         startActivity(intent);
                         finish();
                     }else {
                         String errormsg = jObj.getString("error_msg");
                         Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_LONG).show();
                         hideDialog();
                     }



                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "publishing error :" + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams(){
                // Posting parameters to register url
                Map<String,String> params=new HashMap<String, String>();
                params.put("company_email",email);
             //   params.put("password",password);
                params.put("place",place);
                params.put("describe",description);
                params.put("requirements",requirements);
                params.put("salary",salary);
                params.put("job_type", jobType);
                params.put("final_date",closeDate);
                params.put("name",jobTitle);
                params.put("specilty",TextUtils.join(",",outputStrArr));

                return params;
            }
        };




        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(str_req,tag_string_req);

    }

    private void hideDialog () {
        if (!pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    private void showDialog () {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }




    private TextWatcher validationDescriptionWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String descriptionValid = mDesription.getText().toString().trim();
            //////name
            //////name

            if (!descriptionValid.matches(nameRegexSmall)) {
                txt_description.setText(R.string.smallInput);
            } else {
                txt_description.setText("");
            }
        }@Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher validationRequirementsWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String requirementsValid = mRequirements.getText().toString().trim();
            //////name
            //////name

          if (!requirementsValid.matches(nameRegexSmall)) {
                txt_requirement.setText(R.string.smallInput);
            } else {
                txt_requirement.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher validationJobTitleWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String jobTitleValid = mJobTitle.getText().toString().trim();

            //////name
            if (!jobTitleValid.matches(nameRegexLarge)) {
                txt_jobTitle.setText(R.string.largeInput);
            } else if (!jobTitleValid.matches(nameRegexSmall)) {
                txt_jobTitle.setText(R.string.smallInput);
            } else {
                txt_jobTitle.setText("");
            }

        }@Override
        public void afterTextChanged(Editable s) {
        }
    };



}




