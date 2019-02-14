package com.epicquotes.Drawer;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicquotes.Analytics.MyApplication;
import com.epicquotes.Facebook.GetToken;
import com.epicquotes.Helper.AsyncSendEmail;
import com.epicquotes.Helper.CircleTransform;
import com.epicquotes.R;
import com.epicquotes.Utils.Constants;
import com.epicquotes.fragments.AllFeedFragment;
import com.epicquotes.fragments.LikedFeedFragment;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class DrawerActivity extends AppCompatActivity  {

    // declare properties
    private String[] mNavigationDrawerItemTitles;

    private ListView mDrawerList;
    ImageView ivProfile; TextView tvHeading;
    List<String> permissionNeeds= Arrays.asList("publish_actions");
    List<String> permissionNeeds_1= Arrays.asList("public_profile","email");

    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;
    ViewPager pager;
    String url;
    // used to store app title
    private CharSequence mTitle;
    DrawerLayout mDrawerLayout;
    private WebView webView;
    Profile profile;
    private static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_drawer);
        Intent intent=getIntent();
        url=intent.getStringExtra("Id");
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList=(ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.hello_world,R.string.hello_world);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.item_image_view, null);
         ivProfile=(ImageView)v.findViewById(R.id.iv_profile);
         webView = (WebView) findViewById(R.id.webview);
        checkWritePermission();
    //      LoginManager.getInstance().logInWithReadPermissions(DrawerActivity.this,permissionNeeds_1);
    //    LoginManager.getInstance().logInWithPublishPermissions(DrawerActivity.this,permissionNeeds);

        graphRequest();

        tvHeading=(TextView)v.findViewById(R.id.tv_drawer_title);
       // Log.e("",url);
        Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(100,100)).placeholder(R.drawable.ic_profile).into(ivProfile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DrawerActivity.this,com.epicquotes.activity.Profile.class);
                startActivity(intent);

            }
        });

        getSupportActionBar().setCustomView(v);

        // ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),Constants.Keys.QUOTE_OF_THE_DAY));
        pager.setOffscreenPageLimit(5);

        ViewPager.OnPageChangeListener pagechangelistener =new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                Log.e("pager",arg0+"");
                if(arg0==0) {
                    pager.getAdapter().notifyDataSetChanged();
                    tvHeading.setText("Epic Quotes");


                }

                if(arg0==1){
                    tvHeading.setText("Liked Quotes");

                }


            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {


            }
        };
        pager.setOnPageChangeListener(pagechangelistener);


          // for proper titles
       // mTitle = mDrawerTitle = getTitle();

        // initialize properties
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);

        // list the drawer items
        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[13];
        drawerItem[0] = new ObjectDrawerItem(Constants.Keys.FEEDS,false,false);
        drawerItem[1] = new ObjectDrawerItem(Constants.Keys.QUOTE_OF_THE_DAY,false,false);
        drawerItem[2] = new ObjectDrawerItem(Constants.Keys.FRIENDSHIP,false,false);
        drawerItem[3] = new ObjectDrawerItem(Constants.Keys.FUNNY,false,false);
        drawerItem[4] = new ObjectDrawerItem(Constants.Keys.INSPIRATIONAL,false,false);
        drawerItem[5] = new ObjectDrawerItem(Constants.Keys.LIFE,false,false);
        drawerItem[6] = new ObjectDrawerItem(Constants.Keys.LOVE,false,false);
      //  drawerItem[6] = new ObjectDrawerItem("Songs",false,false);
        drawerItem[7] = new ObjectDrawerItem(Constants.Keys.TV_MOVIES,false,false);
        drawerItem[8] = new ObjectDrawerItem(Constants.Keys.FAMILY,false,false);
        drawerItem[9] = new ObjectDrawerItem(Constants.Keys.SHOP,false,false);
        drawerItem[10] = new ObjectDrawerItem(Constants.Keys.SUBMIT,false,false);
        drawerItem[11] = new ObjectDrawerItem(Constants.Keys.SHARE,false,false);
        drawerItem[12] = new ObjectDrawerItem(Constants.Keys.FEEDBACK,false,false);

        // Pass the folderData to our ListView adapter
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_listview_item_row, drawerItem);

        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);

        // set the item click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());





        // for app icon control for nav drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.hello_world,R.string.hello_world) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                tvHeading.setText(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                tvHeading.setText("Epic Quotes");
                MyApplication.getInstance().trackScreenView("Drawer opened");
            }
        };


        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

       /* getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
       // getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + "Epic Quotes" + "</font>"));
      */
     /*   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
*/


        if (savedInstanceState == null) {
            // on first time display view for first nav item
            selectItem(0);
        }
    }
    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        String choice;
        public MyPagerAdapter(android.support.v4.app.FragmentManager fm,String choice) {
            super(fm);
            this.choice=choice;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch(pos) {

                case 0: return AllFeedFragment.newInstance("AllFeedFragment, Instance 1",choice);
                case 1: return LikedFeedFragment.newInstance("LikedFeedFragment, Instance 2");
                default: return LikedFeedFragment.newInstance("LikedFeedFragment, Default");

            }

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
    public void onStart()
    {   super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }
    @Override
    public void onStop()
    {   super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
    // to change up caret
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            mDrawerToggle.syncState();
        }
    }
    // navigation drawer click listener
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
       String choice;
        pager.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);

        switch (position) {
            case 0:
                choice=Constants.HashTags.FEEDS;
                break;
            case 1:
                choice=Constants.HashTags.QUOTE_OF_THE_DAY;
                break;
            case 2:
                choice=Constants.HashTags.FRIENDSHIP;
                break;
            case 3:
                choice=Constants.HashTags.FUNNY;
                break;
            case 4:
                choice=Constants.HashTags.INSPIRATIONAL;
                break;
            case 5:
                choice=Constants.HashTags.LIFE;
                break;
            case 6:
                choice=Constants.HashTags.LOVE;
                break;
            case 7:
                choice=Constants.HashTags.TV_MOVIES;
                break;
            case 8:
                choice=Constants.HashTags.FAMILY;
                break;
            case 9:
                choice=Constants.HashTags.SHOP;
                break;
            case 10:
                choice=Constants.HashTags.SUBMIT;
                break;
            case 11:
                choice=Constants.HashTags.SHARE;
                break;
            case 12:
                choice=Constants.HashTags.FEEDBACK;
                break;
            default:
                choice=Constants.HashTags.FEEDS;
            break;

        }
//
        MyApplication.getInstance().trackEvent(choice,"Clicked", "From navigation drawer");
        if(choice==Constants.HashTags.SHARE)
        {
            Intent intent=new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Download Epic Quotes from : https://play.google.com/store/apps/details?id=com.epicquotes");
            startActivity(Intent.createChooser(intent, "Share"));
        }
        if(choice==Constants.HashTags.FEEDBACK)
        {
           Log.e("here", "coming here");
          openWebView("http://epicquotes.com/contact/");


        }
        if(choice==Constants.HashTags.SHOP)
        {
            Log.e("here","coming here");
            openWebView("http://mcsidrazz.com/epic-quotes/");


        }
        if(choice==Constants.HashTags.SUBMIT)
        {
            Log.e("here","coming here");
            openWebView("http://epicquotes.com/submit-a-quote/");


        }

            if(choice!="Share")
            {   //pager.setVisibility(View.VISIBLE);
                pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), choice));}
                mDrawerList.setItemChecked(position, true);
              mDrawerList.setSelection(position);
              setTitle(mNavigationDrawerItemTitles[position]);
              tvHeading.setText(mNavigationDrawerItemTitles[position]);
              mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void onResume() {
        super.onResume();
       // MyApplication.getInstance().trackScreenView("Side Drawer Page");


    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
       // if(title=="Timeline"){ mTitle="Home";}
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" +mTitle+ "</font>"));
        tvHeading.setText(Html.fromHtml("<font color=\"white\">" + mTitle + "</font>"));
       // getSupportActionBar().setTitle(mTitle);
    }
    public void openWebView(String url)
    {

        pager.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

     //   webSettings.setBuiltInZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // view.loadUrl("http://epicquotes.com/contact/");
                view.loadUrl(url);
                webView.getSettings().setBuiltInZoomControls(false);
                webView.getSettings().setDisplayZoomControls(false);

                return true;
            }
        });
       // webView.loadUrl("http://epicquotes.com/contact/");
        webView.loadUrl(url);



    }
    public boolean checkWritePermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_STORAGE);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            return false;
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted


            } else {
                Toast.makeText(this, "Until you grant the permission, you cannot download the image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {

        Log.e("Drawer","onActivityResult");
      //  graphRequest();
    }

    public void graphRequest() {

        GetToken gt = new GetToken(this);
        if (CircleTransform.isInternetConnected(DrawerActivity.this)) {

            GraphRequest request = GraphRequest.newMeRequest(gt.doNothing()
                    , new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                           // Application code
                            try {
                                String id = object.getString("id");
                                String name = object.getString("name");
                                String gender = object.getString("gender");
                                JSONObject temp = object.getJSONObject("age_range");
                                String age_range = temp.getString("min");
                                if (!object.isNull("email")) {
                                    String email = object.getString("email");
                                    Log.e("Profile values", id + name + age_range + gender + email);
                                    new AsyncSendEmail(DrawerActivity.this, id, name, gender, age_range, email).execute();
                                }
                                else
                                {
                                    LoginManager.getInstance().logInWithReadPermissions(DrawerActivity.this,permissionNeeds_1);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("GraphResponse", e.toString());
                            }
                            ; // 01/31/1980 format
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,age_range,gender,email");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    @Override
    public void onBackPressed()
    {
        this.moveTaskToBack(true);
    }

}


