package com.epicquotes.Helper;

/**
 * Created by SAMI on 23-02-2016.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class AsyncSendEmail  extends AsyncTask<String,Void,String>{
    private TextView statusField,roleField;
    private Context context;
    private int byGetOrPost = 0;
    String id,name,age,gender,email;

    //flag 0 means get and 1 means post.(By default it is get.)
    public AsyncSendEmail(Context context,String id,String name,String gender,String age,String email) {
        this.context = context;
        this.statusField = statusField;
        this.roleField = roleField;
        this.id=id;
        this.name=name;
        this.gender=gender;
        this.age=age;
        this.email=email;

    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

            try{
                String link="http://epicquotes.esy.es/insert.php";
                String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                data += "&" + URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");


                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }


    @Override
    protected void onPostExecute(String result){
        Log.e("Send Emai","onPostExecute"+ result);
    }
}

