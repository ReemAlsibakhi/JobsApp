package com.example.reeme.gazajob;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.reeme.gazajob.Data.UserContract;
import com.example.reeme.gazajob.model.HomeJobs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.MyViewHolder> {

    private Context context;
    private List<HomeJobs> jobList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mJobTitle,mCompanyName,mSalary,mCompanyAdress,mJobType,mFinalDate,mJobRequirements,mJobDescription;
        public Button mPostDate;

        public MyViewHolder(View view) {
            super(view);
             mJobTitle= view.findViewById(R.id.job_name);
             mCompanyName= view.findViewById(R.id.company_name);
             mPostDate= view.findViewById(R.id.postDate);
        }
    }


    public JobsAdapter(Context context, List<HomeJobs> jobsList) {
        this.context = context;
        this.jobList = jobsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
     HomeJobs jobs= jobList.get(position);

        holder.mJobTitle.setText(jobs.getJobtitle());
        // Displaying dot from HTML character code
        holder.mCompanyName.setText(jobs.getCompanyname());
        holder.mPostDate.setText(formatDate(jobs.getPostdate()));
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }


    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}