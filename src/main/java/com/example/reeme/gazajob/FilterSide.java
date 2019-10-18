package com.example.reeme.gazajob;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.reeme.gazajob.Data.Constants;
import com.example.reeme.gazajob.Utils.AppSharedPreferences;

import java.security.PrivateKey;

public class FilterSide extends AppCompatActivity {

    private ListView list_specialize, list_type, list_place;
    private String[] specialzeList, typeList, placeList;
    private ArrayAdapter<String> adapter1, adapter2, adapter3;
    private TextView txt_close, startPrice, endPrice;
    private SeekBar sbMinPrice, sbMaxPrice;
    private Button btn_signUp_Apply;
    private int minPrice, maxPrice = 0;
    private String specialize, place, type;
    private AppSharedPreferences appSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_side);

        list_specialize = findViewById(R.id.listview_specaity);
        list_type = findViewById(R.id.listview_type);
        list_place = findViewById(R.id.listview_place);
        txt_close = findViewById(R.id.closeTxt);
        sbMinPrice = findViewById(R.id.sb_min_price);
        sbMaxPrice = findViewById(R.id.sb_max_price);
        startPrice = findViewById(R.id.start_price);
        endPrice = findViewById(R.id.end_price);
        btn_signUp_Apply = findViewById(R.id.btn_signUp_Apply);

        // Inisialize Class
        appSharedPreferences = new AppSharedPreferences(FilterSide.this);

        specialzeList = getResources().getStringArray(R.array.fields);
        adapter1 = new ArrayAdapter<String>(getApplication(), R.layout.simple_list_item_choice_single, specialzeList);
        list_specialize.setAdapter(adapter1);
        list_specialize.setChoiceMode(list_specialize.CHOICE_MODE_MULTIPLE);

        list_specialize.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        list_specialize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                specialize = specialzeList[position];
                Log.v("log21", specialzeList[position] + " = CHECKED");
            }
        });


        typeList = getResources().getStringArray(R.array.jobType);
        adapter2 = new ArrayAdapter<String>(getApplication(), R.layout.simple_list_item_choice_single, typeList);
        list_type.setAdapter(adapter2);
        list_type.setChoiceMode(list_type.CHOICE_MODE_MULTIPLE);

        list_type.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        list_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = typeList[position];
                Log.v("log21", typeList[position] + " = CHECKED");
            }
        });


        placeList = getResources().getStringArray(R.array.place);
        adapter3 = new ArrayAdapter<String>(getApplication(), R.layout.simple_list_item_choice_single, placeList);
        list_place.setAdapter(adapter3);
        list_place.setChoiceMode(list_place.CHOICE_MODE_MULTIPLE);

        list_place.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        list_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                place = placeList[position];
                Log.v("log21", placeList[position] + " = CHECKED");
            }
        });

        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //////////////////////////////////////////////////////

        sbMinPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minPrice = progress * 50;
                startPrice.setText("$" + minPrice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbMaxPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxPrice = progress * 50;
                endPrice.setText("$" + maxPrice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_signUp_Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appSharedPreferences.writeString(Constants.FILTER, "012");
                appSharedPreferences.writeString(Constants.SPECIALIZE, specialize);
                appSharedPreferences.writeString(Constants.PLACE, place);
                appSharedPreferences.writeString(Constants.TYPE, type);
                appSharedPreferences.writeInteger(Constants.MIN_PRICE, minPrice);
                appSharedPreferences.writeInteger(Constants.MAX_PRICE, maxPrice);

                startActivity(new Intent(FilterSide.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                 }
        });
    }
}

