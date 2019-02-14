package com.epicquotes.fragments;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.epicquotes.Helper.CircleTransform;
import com.epicquotes.R;
import com.epicquotes.activity.Profile;
import com.facebook.AccessToken;

/**
 * Created by SAMI on 28-10-2015.
 */

    public class MainFragmentActivity extends AppCompatActivity {

    ViewPager pager;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fragment_main);


            if(!CircleTransform.isInternetConnected(getApplicationContext()))
            {
                Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_LONG).show();
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + "Epic Quotes" + "</font>"));

                pager = (ViewPager) findViewById(R.id.viewPager);
                pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                ViewPager.OnPageChangeListener pagechangelistener =new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int arg0) {
                        if(arg0!=0)
                            pager.getAdapter().notifyDataSetChanged();

                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {


                    }
                };
                pager.setOnPageChangeListener(pagechangelistener);
            }

        }

        private class MyPagerAdapter extends FragmentStatePagerAdapter {

            public MyPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int pos) {
                switch(pos) {
/*
                    case 0: return AllFeedFragment.newInstance("AllFeedFragment, Instance 1");
                    case 1: return LikedFeedFragment.newInstance("LikedFeedFragment, Instance 2");
                    default: return LikedFeedFragment.newInstance("LikedFeedFragment, Default");*/

                }
                return null;
            }

            @Override
            public int getCount() {
                Log.e("get count","");
                return 2;

            }
            @Override
            public int getItemPosition(Object object) {

               return POSITION_NONE;
            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }



    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
                    /*getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.your_placeholder, new Profile())
                    .commit();*/
            Intent intent=new Intent(this,Profile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

