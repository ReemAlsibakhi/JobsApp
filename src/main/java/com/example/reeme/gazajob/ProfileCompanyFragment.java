package com.example.reeme.gazajob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.DataBase.SessionManager;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;


public class ProfileCompanyFragment extends Fragment {

    private TextView name,companyName,emailCompany,mobileCompany,txt_settings,mTxt_update;
    Activity context;
    private AppSharedPreferences sharedPreferences;
    private SessionManager session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }
            @Override
            public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
                context = getActivity();
                // Inflate the layout for this fragment
                View v = inflater.inflate(R.layout.activity_profile_company_fragment, container, false);

                sharedPreferences=new AppSharedPreferences(getContext());

                name=(TextView)v.findViewById(R.id.name);
                companyName=(TextView)v.findViewById(R.id.companyName);
                emailCompany=(TextView)v.findViewById(R.id.email_txt);
                mobileCompany=(TextView)v.findViewById(R.id.mobile_txt);
                txt_settings = (TextView) v.findViewById(R.id.settings);
                mTxt_update=v.findViewById(R.id.update);

                sharedPreferences = new AppSharedPreferences(getContext());
                final String username =sharedPreferences.readString(Constants.COMPANY_USER);
                final String companyname =sharedPreferences.readString(Constants.COMPANY_NAME);
                final String email =sharedPreferences.readString(Constants.COMPANY_EMAIL);
                final String mobile =sharedPreferences.readString(Constants.COMPANY_MOBILE);

                name.setText(username);
                companyName.setText(companyname);
                emailCompany.setText(email);
                mobileCompany.setText(mobile);

                mTxt_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(getActivity(),update_data_company.class);
                        startActivity(intent);
                    }
                });


                session=new SessionManager(getActivity());
                txt_settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(getActivity(), txt_settings);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                 item.setVisible(true);
                                switch (item.getItemId()){
                                 case  R.id.logout:
                                     Toast.makeText(getActivity(),  item.getTitle(), Toast.LENGTH_SHORT).show();
                                     session.setLoginCompany(false);
                                     sharedPreferences.writeString(Constants.FILTER, "logout");
                                     sharedPreferences.writeString(Constants.PROFILE, "profile");
                                     //sharedPreferences.writeString(Constants.Register_Company,"noRegister");
                                     Intent intent = new Intent(getActivity(),MainActivity.class);
                                     startActivity(intent);
                                     getActivity().finish();
                                    case R.id.change_image:
                                        Toast.makeText(getActivity(),  item.getTitle(), Toast.LENGTH_SHORT).show();


                                }

                                return true;

                            }
                        });

                        popup.show();//showing popup menu
                    }
                });//closing the setOnClickListener method
                return v;


            }

        }

