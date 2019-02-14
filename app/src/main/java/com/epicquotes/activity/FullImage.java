package com.epicquotes.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.epicquotes.Analytics.MyApplication;
import com.epicquotes.Drawer.DrawerActivity;
import com.epicquotes.Facebook.GetToken;
import com.epicquotes.Helper.CircleTransform;
import com.epicquotes.Helper.ImageLoadTask;
import com.epicquotes.R;
import com.epicquotes.fragments.MainFragmentActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class FullImage extends ActionBarActivity {


    ImageView im; TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + "" + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        im=(ImageView)findViewById(R.id.imageView1);
        tv=(TextView) findViewById(R.id.textView1);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String url =b.getString("url");
            Picasso.with(this).load(url).placeholder(R.drawable.background_blank).into(im);
            String message=b.getString("message");
            tv.setText(message);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("Full Image Page");


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
          finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


}
