package com.epicquotes.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Profile extends ActionBarActivity {

    ImageView cover_pic, profile;TextView name;ProgressBar progress;
    private LoginButton fbLoginButton;
    private Button btnRate;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_1);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + "Profile" + "</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GetToken gt=new GetToken(getApplicationContext());
        AccessToken accessToken= gt.doNothing();
       cover_pic=(ImageView)findViewById(R.id.cover);
        profile=(ImageView)findViewById(R.id.profile);
        name=(TextView)findViewById(R.id.name);
        btnRate=(Button) findViewById(R.id.btn_market);
        progress=(ProgressBar)findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
      //  Log.e("token from profile", accessToken.getToken().toString());

        final GraphRequest request=new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        String json_response=response.toString();

                        try {

                            JSONObject jsonObj = response.getJSONObject();


                            try {
                                JSONObject image = jsonObj.getJSONObject("cover");
                                String link = image.getString("source");
                                Log.e("Link", link);
                                ImageLoadTask obj = new ImageLoadTask(link, cover_pic, Profile.this, progress);
                                obj.execute();
                               // Picasso.with(Profile.this).load(link).placeholder(R.drawable.background_blank).into(cover_pic);

                            }
                            catch (JSONException e) {
                                Log.e("Link error",e.toString());
                                progress.setVisibility(View.INVISIBLE);
                                e.printStackTrace();
                            }
                            name.setText(jsonObj.getString("name"));
                            try {
                                JSONObject picture = jsonObj.getJSONObject("picture");
                                if (picture != null) {
                                    JSONObject data = picture.getJSONObject("data");
                                    String url = data.getString("url");
                           /* ImageLoadTask obj_1=new ImageLoadTask(url,profile,Profile.this,progress);
                            obj_1.execute();*/
                                    Picasso.with(Profile.this).load(url).into(profile);
                                }
                            }
                            catch (JSONException e) {
                                Log.e("Link error",e.toString());
                                progress.setVisibility(View.INVISIBLE);
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            Log.e("Link error",e.toString());
                            progress.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }
                        catch(Exception e)
                        {

                        }
            /* handle the result */
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,cover,picture.width(200).height(200)");
        request.setParameters(parameters);
        request.executeAsync();
        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
        fbLoginButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 LoginManager.getInstance().logOut();
                                                // v.setVisibility(View.GONE);
                                                 Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                 startActivity(intent);
                                                 finish();

                                             }
                                         }
        );
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

      /*  fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

            *//*    System.out.println("Facebook Login Successful!");
                System.out.println("Logged in user Details : ");
                System.out.println("--------------------------");
                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());

*//*

            }

            @Override
            public void onCancel() {
               // Toast.makeText(LoginActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
               // Toast.makeText(LoginActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
            }


        });*/

    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("Profile Page");


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
            Intent intent = new Intent(this, DrawerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
