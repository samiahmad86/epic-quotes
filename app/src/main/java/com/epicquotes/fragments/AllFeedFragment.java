package com.epicquotes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.epicquotes.Analytics.MyApplication;
import com.epicquotes.Helper.AsyncResponse;
import com.epicquotes.Helper.ListLoadAsync;
import com.epicquotes.Helper.MyCustomAdapter;
import com.epicquotes.Helper.Story;
import com.epicquotes.R;
import com.epicquotes.Utils.Constants;
import com.epicquotes.activity.CreateQuoteActivity;
import com.facebook.login.LoginManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by SAMI on 28-10-2015.
 */
public class AllFeedFragment extends Fragment implements AsyncResponse,SwipeRefreshLayout.OnRefreshListener {


    private  ArrayList<Story> story = new ArrayList<Story>();
    private boolean success = false;
    public  MyCustomAdapter dataAdapter;
    ListView listView;
    static String choice;
    int pos;
    static Parcelable state;
    ProgressBar progress;
    SwipeRefreshLayout swipeRefreshLayout;
    List<String> permissionNeeds= Arrays.asList("publish_actions");
    FloatingActionButton fab;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_main, container, false);

        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        fab=(FloatingActionButton) v.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        Bundle bundle=getArguments();
        choice=bundle.getString("pos");
        dataAdapter = new MyCustomAdapter(getActivity(),
                R.layout.custom_layout, story,this);
        if(choice== Constants.Keys.QUOTE_OF_THE_DAY)
            v.findViewById(R.id.tv_more).setVisibility(View.VISIBLE);
        else
            v.findViewById(R.id.tv_more).setVisibility(View.GONE);
      //  Log.e("Position",choice);

        //here is your list array
        if(savedInstanceState==null)
        { //  Toast.makeText(getActivity(), "oncreateview", Toast.LENGTH_LONG).show();
            ListLoadAsync load=new ListLoadAsync(this,0,choice);
            load.doInBackground();
            Log.e("no save instance","here");
        }


        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getActivity(), CreateQuoteActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) v.findViewById(R.id.listView);
        progress=(ProgressBar) v.findViewById(R.id.progress);
        listView.setFriction(ViewConfiguration.getScrollFriction() * 5);
        listView.setAdapter(dataAdapter);

        if(savedInstanceState!=null && state!=null)
        {
            Log.e("saved insance","here");
            ListLoadAsync load=new ListLoadAsync(this,0,choice);
            load.doInBackground();
            listView.setAdapter(dataAdapter);
            listView.onRestoreInstanceState(state);
            progress.setVisibility(View.VISIBLE);
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                final ListView lw = (ListView) view;
                if (scrollState == 0)
                    Log.i("a", "scrolling stopped...");
                if (view.getId() == lw.getId()) {
                    final int currentFirstVisibleItem = lw.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        /*fab.animate().cancel();
                        fab.animate().translationYBy(150);*/
                        fab.hide();
                        Log.i("a", "scrolling down...");
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                           fab.show();
                        /* fab.animate().cancel();
                       //  fab.animate().translationY(btn_initPosY);*/
                        Log.i("a", "scrolling up...");
                    }
                    mLastFirstVisibleItem = currentFirstVisibleItem;
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
      //  listView.setAdapter(dataAdapter);


        return v;
    }
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
         state = listView.onSaveInstanceState();
       // Toast.makeText(getActivity(), "save instance", Toast.LENGTH_LONG).show();
    }

    public static AllFeedFragment newInstance(String text,String choice) {

        AllFeedFragment f = new AllFeedFragment();
        Bundle b = new Bundle();
        b.putString("pos", choice);
        f.setArguments(b);
        return f;
    }
    @Override
    public void onResume() {  // Inflate the menu; this adds items to the action bar if it is present.
        super.onResume();
        MyApplication.getInstance().trackScreenView("All Feeds Page");
    }
    @Override
    public void onPause() {
        super.onPause();
    }



    public void processFinish(ArrayList<Story> story) {
        this.story.addAll(story);
        Log.e("Strange", "really");
        swipeRefreshLayout.setRefreshing(false);
       // Collections.shuffle(this.story);
        fab.setVisibility(View.VISIBLE);
        dataAdapter.notifyDataSetChanged();
        // Assign adapter to ListView
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        Collections.shuffle(this.story);
        dataAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);


    }
}
