package com.example.reeme.gazajob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.DataBase.SessionManager;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;

public class FragmentNotFound extends Fragment {
    private TextView signup_txt;
    AppSharedPreferences sp;
    SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_not_found,null);

        signup_txt=(TextView)v.findViewById(R.id.signup_txt);
    sp=new AppSharedPreferences(getContext());
        signup_txt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(),SignUp.class);
               startActivity(intent);
               getActivity().finish();
           }
       });

        // Session manager
        session = new SessionManager(getActivity());
        if (session.isLoggedInUser()){
            switchToFragment1();
        }
        if(session.isLoggedInCompany()){
            switchToFragment2();
        }

        return v;
    }

    public void switchToFragment1() {
        sp.writeString(Constants.PROFILE,"noProfile");
        FragmentManager manager = getFragmentManager();
       FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, new ProfileFragment());
        transaction.commit();

    }

    public void switchToFragment2() {
        sp.writeString(Constants.PROFILE,"noProfile");
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, new ProfileCompanyFragment());
        transaction.commit();
    }
}
