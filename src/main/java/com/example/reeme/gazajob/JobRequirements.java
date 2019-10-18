package com.example.reeme.gazajob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class JobRequirements extends AppCompatActivity {
 private TextView mDescription;
 private TextView mRequirements;
    private TextView back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_requirements);

        back_arrow=findViewById(R.id.back);
        mDescription=(TextView)findViewById(R.id.txtDescriptions);
        mRequirements=(TextView) findViewById(R.id.txtRequirements);

       String descriptionStr= getIntent().getStringExtra("Descriptions");
       String requirementsStr=getIntent().getStringExtra("Requirements");

    mDescription.setText(descriptionStr);
    mRequirements.setText(requirementsStr);
//arrow to back
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
