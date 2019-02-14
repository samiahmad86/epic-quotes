package com.epicquotes.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.AppIndexApi;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import com.epicquotes.Analytics.MyApplication;
import com.epicquotes.Drawer.DrawerActivity;
import com.epicquotes.Facebook.GetToken;
import com.epicquotes.Helper.CircleTransform;
import com.epicquotes.Notification.NotifyService;
import com.epicquotes.R;
import com.epicquotes.fragments.MainFragmentActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class LoginActivity extends Activity {
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    public  AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    public static String uid;
    public static AccessToken token;
    private ProgressBar progressBar;
    List<String> permissionNeeds= Arrays.asList("public_profile","email");
    List<String> permissionNeeds1= Arrays.asList("publish_actions");
    SharedPreferences prefs = null;
    private GoogleApiClient mClient;
    private Uri mUrl;
    private String mTitle;
    private String mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        /*PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.epicquotes", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
         PendingIntent pendingIntent = PendingIntent.getBroadcast(
                LoginActivity.this, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
            Log.e("exception", e.toString());


        }*/


        boolean alarmUp = (PendingIntent.getBroadcast(LoginActivity.this, 0,
                new Intent(LoginActivity.this, NotifyService.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp)
        {
            //Toast.makeText(LoginActivity.this, "Alarm already created", Toast.LENGTH_LONG).show();
        }
        else {
            createAlarm();
        }
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

                Profile.fetchProfileForCurrentAccessToken();

                try {
                    if (AccessToken.getCurrentAccessToken().getToken().toString() != null)
                        Log.e("access token", AccessToken.getCurrentAccessToken().getToken().toString());
                     if(oldToken.getToken()!=null)
                Log.e("access token", oldToken.getToken().toString());
                    if (newToken.getToken() != null)
                        Log.e("access token", newToken.getToken().toString());
                }
                catch (NullPointerException ex)
                {
                    Log.e("error",ex.toString());
                }
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {


                displayMessage(newProfile);

            }
        };

        Profile.fetchProfileForCurrentAccessToken();
        setContentView(R.layout.activity_login);


        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mUrl = Uri.parse("http://epicquotes.com");
        mTitle = "Epic Quotes";
        mDescription = "Epic Quotes";

        mClient.connect();

        Uri appUri=Uri.parse("android-app://com.epicquotes/http/www.epicquotes.com");

        Uri webUrl=Uri.parse("http://www.epicquotes.com");

        AppIndexApi.AppIndexingLink item1=new AppIndexApi.AppIndexingLink(appUri,webUrl,findViewById(R.id.login_button));
        List<AppIndexApi.AppIndexingLink> outlinks=new ArrayList<AppIndexApi.AppIndexingLink>();
        outlinks.add(item1);

        String title="Epic Quotes";
        AppIndex.AppIndexApi.view(mClient,this,appUri,title,webUrl,outlinks);






        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
      // List<String> permissionNeeds = Arrays.asList("public_profile");
        // fbLoginButton.setReadPermissions("public_profile");
        //fbLoginButton.setPublishPermissions("publish_actions");


        fbLoginButton.setReadPermissions(permissionNeeds);



        prefs = getSharedPreferences("com.epicquotes", MODE_PRIVATE);
       /* if (prefs.getBoolean("firstrun1", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            Log.e("First time","Here");
                LoginManager.getInstance().logOut();
                deleteFacebookApplication();


         //   fbLoginButton.setReadPermissions("public_profile","email");
          //  fbLoginButton.setReadPermissions("public_profile","email");
               prefs.edit().putBoolean("firstrun1", false).commit();
        }*/

        progressBar=(ProgressBar)findViewById(R.id.progress);
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            //Toast.makeText(LoginActivity.this, AccessToken.getCurrentAccessToken().getToken().toString()+" :tocken tracker", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.VISIBLE);


        }
        if(!CircleTransform.isInternetConnected(getApplicationContext()))
        {
            Toast.makeText(LoginActivity.this, "No internet Connection", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }





        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

            /*    System.out.println("Facebook Login Successful!");
                System.out.println("Logged in user Details : ");
                System.out.println("--------------------------");
                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());

*/
                String accessToken= loginResult.getAccessToken().getToken();
                token=loginResult.getAccessToken();
                fbLoginButton.setVisibility(View.INVISIBLE);

              //  LoginManager.getInstance().logInWithPublishPermissions(LoginActivity.this,permissionNeeds1);









              //  fbLoginButton.clearPermissions();
            //     LoginManager.getInstance().logInWithPublishPermissions(LoginActivity.this,permissionNeeds);
             //   fbLoginButton.setPublishPermissions("publish_actions","email");
             //   Toast.makeText(LoginActivity.this, "at the time of login : "+accessToken, Toast.LENGTH_LONG).show();
                accessTokenTracker.startTracking();

                Profile profile = Profile.getCurrentProfile();
              //  displayMessage(profile);


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


        });


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
        accessTokenTracker.stopTracking();
    }


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("Login Page");
        Profile profile = Profile.getCurrentProfile();

        displayMessage(profile);

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

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        callbackManager.onActivityResult(reqCode, resCode, i);
    }
    private void displayMessage(Profile profile){

            if(profile!=null) {
              //  Toast.makeText(this, profile.getFirstName().toString(), Toast.LENGTH_SHORT).show();
              //  Intent intent=new Intent(this,MainActivity.class);
              //  Intent intent = new Intent(getApplicationContext(), MainFragmentActivity.class);
                Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra("Id",profile.getProfilePictureUri(200,200).toString());
                //Log.e("some",profile.getProfilePictureUri(200, 200).toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               // LoginManager.getInstance().logInWithPublishPermissions(LoginActivity.this,permissionNeeds);
                startActivity(intent);
                finish();

               //startActivity(intent);

            }
            else {

            }

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    public void createAlarm()
    {
        Log.e("setting an alarm","Okay");
        Calendar calendar = Calendar.getInstance();

// we can set time by open date and time picker dialog

        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);

        Intent intent1 = new Intent(LoginActivity.this, NotifyService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                LoginActivity.this, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) LoginActivity.this
                .getSystemService(LoginActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    public Action getAction() {
        Thing object = new Thing.Builder()
                .setName(mTitle)
                .setDescription(mDescription)
                .setUrl(mUrl)
                .build();

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getAction());
    }

    @Override
    public void onStop() {
        AppIndex.AppIndexApi.end(mClient, getAction());
        mClient.disconnect();
        super.onStop();
    }
}
