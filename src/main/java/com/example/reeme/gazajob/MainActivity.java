package com.example.reeme.gazajob;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;


//implement the interface OnNavigationItemSelectedListener in your activity class
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private AppSharedPreferences appSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting bottom navigation view and attaching the listener
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //Initialize Classes
        appSharedPreferences = new AppSharedPreferences(MainActivity.this);
        //get data from SignUp_User.
        final String username =getIntent().getStringExtra("username_user");
        final String email = getIntent().getStringExtra("email_user");
        final String mobile =getIntent().getStringExtra("mobile_user");
        final String interest =getIntent().getStringExtra("interest_user");

        //loading the default fragment
        String filter = appSharedPreferences.readString(Constants.FILTER);
        String profile=appSharedPreferences.readString(Constants.PROFILE);
        String search_jobs=appSharedPreferences.readString(Constants.SEARCH_HOME);
        //    String logout=appSharedPreferences.readString(Constants.LOGOUT);
        if (filter != null) {
            if (filter.equals("012")) {
                Log.v(Constants.LOG+1, filter+" Home");
                bottomNavigationView.getMenu().findItem(R.id.navigation_bolt).setChecked(true);
                loadFragment(new JobsFilterFragment());
             ;
            }else if (search_jobs.equals("search")){
                appSharedPreferences.writeString(Constants.SEARCH_HOME,"no_search");
                bottomNavigationView.getMenu().findItem(R.id.navigation_bolt).setChecked(true);
                loadFragment(new JobsFilterFragment());
              //  appSharedPreferences.writeString(Constants.SEARCH_HOME,"no_search");
            }
                else if(profile.equals("profile")) {
                bottomNavigationView.getMenu().findItem(R.id.navigation_user).setChecked(true);
                loadFragment(new FragmentNotFound());
             //   appSharedPreferences.writeString(Constants.PROFILE,"noProfile");

            }
          else {
                    Log.v(Constants.LOG+1, filter+" Home");
                    loadFragment(new HomeFragment());
                }
            }


        }

/**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_options_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
 */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_bolt:
                fragment = new JobsFilterFragment();
                break;

            case R.id.navigation_user:
                fragment = new FragmentNotFound();
                break;
            case R.id.navigation_bookmark:
                fragment = new BookmarkFragment();
                break;
        }


        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}


/**
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:

                viewFragment(new HomeFragment(), "fragment_home");
                return  true;
            case R.id.navigation_bolt:
                viewFragment(new JobsFilterFragment(), "fragment_other");
                return  true;
            case R.id.navigation_user:
                viewFragment(new FragmentNotFound(), "fragment_other");
                return  true;
            case R.id.navigation_bookmark:
                viewFragment(new BookmarkFragment(), "fragment_other");
                return  true;
        }

              return  false;
    }


    private void viewFragment(Fragment fragment, String name){
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        // 1. Know how many fragments there are in the stack
        final int count = fragmentManager.getBackStackEntryCount();
        // 2. If the fragment is **not** "home type", save it to the stack
        if( name.equals( "fragment_other") ) {
            fragmentTransaction.addToBackStack(name);
        }
        // Commit !
        fragmentTransaction.commit();
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if( fragmentManager.getBackStackEntryCount() <= count){
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack("fragment_other", POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    // set the home button selected
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        });
}
}
*/