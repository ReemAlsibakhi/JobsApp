package com.example.reeme.gazajob;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.reeme.gazajob.model.HomeJobs;

import java.util.List;

public class CustomListViewAdapter extends BaseAdapter {
private  TextView txt_checked_delete;
    private Activity activity;
    private LayoutInflater inflater;
    private List<HomeJobs> DataList;
    int mSelectedItem;
  //  private SparseBooleanArray mSelectedItemIds;
  //  ImageLoader imageLoader = Controller.getPermission().getImageLoader();

    public CustomListViewAdapter(Activity activity,List<HomeJobs> dataitem) {
        //super(activity, dataitem, resourceId);
        this.activity = activity;
       this.DataList = dataitem;
    }

    @Override
    public int getCount() {
        return DataList.size();
    }

    @Override
    public Object getItem(int location) {
        return DataList.get(location);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void remove(HomeJobs object){
        DataList.remove(object);
        notifyDataSetChanged();
    }
public List<HomeJobs>getDataList(){
        return DataList;
}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);

       /* if(position==mSelectedItem){
        convertView.setBackgroundColor(Color.TRANSPARENT);
        }
     /**   if (imageLoader == null)
            imageLoader = Controller.getPermission().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

      */
      txt_checked_delete =(TextView) convertView.findViewById(R.id.checkTxt);
        TextView jobTitle = (TextView) convertView.findViewById(R.id.job_name);
        TextView companyName = (TextView) convertView.findViewById(R.id.company_name);
        Button postDate = (Button) convertView.findViewById(R.id.postDate);
        HomeJobs m = DataList.get(position);
    //    thumbNail.setImageUrl(m.getImage(), imageLoader);
        jobTitle.setText(m.getJobtitle());
        companyName.setText(m.getCompanyname());
        postDate.setText(m.getPostdate());


        return convertView;
    }

    public void show_checkedDelete(){

        txt_checked_delete.setVisibility(View.VISIBLE);
    }
public  void  hide_checkedDelete(){
        txt_checked_delete.setVisibility(View.GONE);
    }
}
 /**   private Context context;
   // ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public CustomListViewAdapter(@NonNull Context context, int resource, List<JobsPosts_Home> wordAdapter) {
       super(context, resource,wordAdapter);
          this.context=context;
    }

 @NonNull
 @Override
 public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
     //   return super.getView(position, convertView, parent);

     // Check if an existing view is being reused, otherwise inflate the view

     View listItemView =convertView;
     if (listItemView == null) {
         listItemView = LayoutInflater.from(getContext()).inflate(
                 R.layout.list_item, parent, false);
     }

     // Get the {@link Word} object located at this position in the list
    JobsPosts_Home jobsPosts = getItem(position);
/**
     if (imageLoader == null)
         imageLoader = AppController.getInstance().getImageLoader();

     NetworkImageView imageView = (NetworkImageView) listItemView.findViewById(R.id.image);
     imageView.setImageUrl(jobsPosts.getImageResourse(), imageLoader);



     // Find the TextView in the list_item.xml layout with the ID default_text_view.
     TextView txtJobTitle = (TextView) listItemView.findViewById(R.id.job_name);
     // Get the default translation from the currentWord object and set this text on
     // the default TextView.
     txtJobTitle.setText(jobsPosts.getJobTitle());



     // Find the TextView in the list_item.xml layout with the ID company_name.
     TextView txtCompanyName = (TextView) listItemView.findViewById(R.id.company_name);
     // Get the comapany name from the currentWord object and set this text on
     // the default TextView.
     txtCompanyName.setText(jobsPosts.getCompanyName());


     Button buttonPostDate = (Button) listItemView.findViewById(R.id.postDate);
     buttonPostDate.setText(jobsPosts.getPostDate());


     // Return the whole list item layout (containing 3 TextViews & 1 imageView) so that it can be shown in
     // the ListView.
     return listItemView;
 }
}
*/


