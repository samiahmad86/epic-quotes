package com.epicquotes.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.epicquotes.Analytics.MyApplication;
import com.epicquotes.Helper.AsyncResponse;
import com.epicquotes.Helper.ListLoadAsync;
import com.epicquotes.Helper.MyCustomAdapter;
import com.epicquotes.Helper.Story;
import com.epicquotes.Helper.Updateable;
import com.epicquotes.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by SAMI on 28-10-2015.
 */
public class LikedFeedFragment extends Fragment implements AsyncResponse,SwipeRefreshLayout.OnRefreshListener {
    private  ArrayList<Story> story = new ArrayList<Story>();
    private boolean success = false;
    public  MyCustomAdapter dataAdapter;
    ListView listView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    List<String> permissionNeeds= Arrays.asList("publish_actions");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_liked, container, false);


        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        ListLoadAsync load=new ListLoadAsync(this,1,"");
        load.doInBackground();
        progressBar=(ProgressBar)v.findViewById(R.id.progress);
        dataAdapter = new MyCustomAdapter(getContext(),R.layout.custom_layout, story,this);

        listView = (ListView) v.findViewById(R.id.listView);
        listView.setFriction(ViewConfiguration.getScrollFriction() * 5);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub


                if(scrollState == 0) {
                    Log.i("a", "scrolling stopped...");
                    //   swipeRefreshLayout.setEnabled(true);
                }
                if (scrollState != 0) {

                    //  swipeRefreshLayout.setEnabled(false);
                }

            }



            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);



            }

        });


        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        return v;
    }

    public static LikedFeedFragment newInstance(String text) {

        LikedFeedFragment f = new LikedFeedFragment();
        Bundle b = new Bundle();

        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
    public void processFinish(ArrayList<Story> story) {
        this.story.addAll(story);
        Log.e("Strange", "really");
        //this.dataAdapter.notifyDataSetChanged();
       /* dataAdapter = new MyCustomAdapter(getActivity(),
                R.layout.custom_layout, this.story,this);
*/
        // Assign adapter to ListView
        dataAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
       // listView.setAdapter(dataAdapter);
        progressBar.setVisibility(View.GONE);


    }
    @Override
    public void onResume() {
        super.onResume();

        // Tracking the screen viewlayout
        MyApplication.getInstance().trackScreenView("Liked Quotes Page");
    }
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        Collections.shuffle(this.story);
        dataAdapter.notifyDataSetChanged();
    }


}
