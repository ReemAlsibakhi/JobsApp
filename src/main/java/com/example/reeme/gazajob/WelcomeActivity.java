package com.example.reeme.gazajob;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.DataBase.SessionManager;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;

public class WelcomeActivity extends AppCompatActivity {
    Button advertise_btn;
    TextView txtGo_Home;
    SessionManager session;
    private AppSharedPreferences appSharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        txtGo_Home = findViewById(R.id.go_Home);

        advertise_btn = findViewById(R.id.btn_advertise);

        //Initialize
        // Session manager
        session = new SessionManager(getApplicationContext());
        appSharedPreferences = new AppSharedPreferences(WelcomeActivity.this);

        if(session.isLoggedInUser()) {
            advertise_btn.setVisibility(View.GONE);
        }

        advertise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (session.isLoggedInCompany()) {
                    Intent intent = new Intent(WelcomeActivity.this, PublishJob.class);
                    startActivity(intent);

                    }
                    //you not logging
                    else {
                        //custom dialog
                        final Dialog dialog = new Dialog(WelcomeActivity.this);
                        dialog.setContentView(R.layout.custom_dialog_company);

                        //set custom dialog components
                        TextView txt_chck = (TextView) dialog.findViewById(R.id.checkTxt);
                        txt_chck.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //write in sharedPreference it is came from welcomeActivity
                                appSharedPreferences.writeString(Constants.ADVERTISE, "welcome_advertise");
                                //move to signIn activity
                                Intent intent = new Intent(WelcomeActivity.this, SignIn.class);
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


        txtGo_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appSharedPreferences.writeString(Constants.FILTER, "210");
                appSharedPreferences.writeString(Constants.PROFILE,"noProfile");
               // appSharedPreferences.writeString(Constants.DELETE_BOOKMAERK,"not_delete");
                appSharedPreferences.writeString(Constants.SEARCH_HOME,"no_search");
              //  appSharedPreferences.writeString(Constants.SEARCH_HOME,"no_search");
             //   appSharedPreferences.writeString(Constants.LOGOUT,"noLogout");
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
