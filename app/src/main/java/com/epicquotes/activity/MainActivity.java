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
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.epicquotes.Helper.AsyncResponse;
import com.epicquotes.Helper.ListLoadAsync;
import com.epicquotes.Helper.MyCustomAdapter;
import com.epicquotes.Helper.Story;
import com.epicquotes.R;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements AsyncResponse {



    private static ArrayList<Story> story = new ArrayList<Story>();
    private boolean success = false;
    public static MyCustomAdapter dataAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + "Epic Quotes" + "</font>"));
            /*   dataAdapter = new MyCustomAdapter(this,
                R.layout.custom_layout, countryList);
        ListView listView = (ListView) findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);*/
        // ListLoadAsync load=new ListLoadAsync();

      /*  ListLoadAsync load=new ListLoadAsync(this);
        load.execute();
        load.delegate = this;*/

       /* dataAdapter = new MyCustomAdapter(this,
                R.layout.custom_layout, story,this);*/
        listView = (ListView) findViewById(R.id.listView);
        listView.setFriction(ViewConfiguration.getScrollFriction() * 5);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


    }
    @Override
    public void onResume() {
        super.onResume();
        dataAdapter.notifyDataSetChanged();
        Toast.makeText(this,"Resumed Activity",Toast.LENGTH_LONG).show();
    }
    public void displayCountries(ArrayList<Story> story){
        //if the request was successful then notify the adapter to display the data

        Log.e("coming here", "yes yes");
        this.story=story;
        this.dataAdapter.notifyDataSetChanged();
      /*  dataAdapter = new MyCustomAdapter(this,
                R.layout.custom_layout, story);
        listView = (ListView) findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_profile) {
                    *//*getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.your_placeholder, new Profile())
                    .commit();*//*
            Intent intent=new Intent(this,Profile.class);
            startActivity(intent);



            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(ArrayList<Story> story) {

    }


   /* @Override
    public void processFinish(ArrayList<Story> story) {
        this.story=story;
        Log.e("Strange", "really");
        this.dataAdapter.notifyDataSetChanged();
        dataAdapter = new MyCustomAdapter(this,
                R.layout.custom_layout, this.story);
        listView = (ListView) findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


    }
*/


}
