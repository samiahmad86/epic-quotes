package com.epicquotes.Helper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.epicquotes.Utils.Constants;
import com.epicquotes.activity.MainActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mohahm01 on 02-10-2015.
 */
public class ListLoadAsync {

  //  public AsyncResponse delegate=null;
   AsyncResponse listener;
    Story single_story=new Story();
    int flag;
    ArrayList<Story> story=new ArrayList<Story>();
    String choice;

    public ListLoadAsync(AsyncResponse listener,int flag,String choice)
    {
        this.listener=listener;
        this.flag=flag;
        this.choice=choice;
    }

    public void doInBackground() {
        final GraphRequest request=new GraphRequest(
                AccessToken.getCurrentAccessToken(),
              "/462768487218398/posts",

                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        try {
                            JSONObject raw_data=response.getJSONObject();
                            JSONArray data=raw_data.getJSONArray("data");

                          //  Log.e("Next Page",nextPage);
                            JSONObject likes;JSONObject summary;
                            for(int i=0;i<data.length();i++)
                            {
                                single_story=new Story();
                                JSONObject single_data=data.getJSONObject(i);
                             /*   Log.e("FUll_PICTURE", single_data.getString("full_picture"));
                                Log.e("Object Id",single_data.getString("id"));
                                Log.e("ID",single_data.getString("id") );*/
                                likes=single_data.getJSONObject("likes");
                                summary=likes.getJSONObject("summary");
                            /*    Log.e("Total Count",summary.getInt("total_count")+"" );
                                Log.e("Can Like",summary.getBoolean("can_like")+"");
                                Log.e("Has Liked", summary.getBoolean("has_liked") + "");*/



                               if(flag==0){
                                  // if((single_data.getString("message").contains(choice))||(choice=="#Timeline")) {

                                   if((choice== Constants.Keys.QUOTE_OF_THE_DAY)){

                                       String timeCreated=single_data.getString("created_time");
                                     /*  Log.e("date",timeCreated);
                                       Log.e("date",single_data.getString("created_time"));*/
                                       if(checkDate(timeCreated)) {
                                           single_story.setFull_picture(single_data.getString("full_picture"));
                                           single_story.setObject_id(single_data.getString("id"));
                                           single_story.setCan_like(summary.getBoolean("can_like"));
                                           single_story.setHas_liked(summary.getBoolean("has_liked"));
                                           single_story.setTotal_count(summary.getInt("total_count"));
                                           single_story.setMessage(single_data.getString("message"));
                                           story.add(single_story);

                                       }


                                   }

                                   if((choice== Constants.Keys.FEEDS)){

                                       //   Log.e("Choice",choice);
                                       single_story.setFull_picture(single_data.getString("full_picture"));
                                       single_story.setObject_id(single_data.getString("id"));
                                       single_story.setCan_like(summary.getBoolean("can_like"));
                                       single_story.setHas_liked(summary.getBoolean("has_liked"));
                                       single_story.setTotal_count(summary.getInt("total_count"));
                                       single_story.setMessage(single_data.getString("message"));
                                       story.add(single_story);


                                   }




                                   if((single_data.getString("message").contains(choice))) {
                                      // Log.e("Choice",choice);
                                     //  Log.e("message",single_data.getString("message"));
                                       single_story.setFull_picture(single_data.getString("full_picture"));
                                       single_story.setObject_id(single_data.getString("id"));
                                       single_story.setCan_like(summary.getBoolean("can_like"));
                                       single_story.setHas_liked(summary.getBoolean("has_liked"));
                                       single_story.setTotal_count(summary.getInt("total_count"));
                                       single_story.setMessage(single_data.getString("message"));
                                       story.add(single_story);
                                   }
                               }
                               else {
                                    if(summary.getBoolean("has_liked"))
                                    {
                                       // Log.e("Liked fragment","liked");
                                        single_story.setFull_picture(single_data.getString("full_picture"));
                                        single_story.setMessage(single_data.getString("message"));
                                        single_story.setObject_id(single_data.getString("id"));
                                        single_story.setCan_like(summary.getBoolean("can_like"));
                                        single_story.setHas_liked(summary.getBoolean("has_liked"));
                                        single_story.setTotal_count(summary.getInt("total_count"));
                                        story.add(single_story);

                                    }

                               }

                            }

                            // This is to refresh the dataset.. the control goes to onpostexecute with the above code running in background. therefore, this will refresh the list view.
                            // Leaving this as it is for now I don't think it will cause much problem later..spinner can be displayed from here...
                           // delegate.processFinish(story);
                            if(story!=null)
                            listener.processFinish(story);
                            JSONObject paging=raw_data.getJSONObject("paging");
                            String nextPage=paging.getString("next");
                            nextPageParse(nextPage);

                          } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e)
                        {

                        }


                    }
                }
        );
        Bundle parameters = new Bundle();
      //  parameters.putString("fields", "full_picture,message,id,likes.limit(0).summary(true)");
        parameters.putString("fields", "full_picture,created_time,message,id,likes.limit(0).summary(true)");
        parameters.putString("limit","100");
        request.setParameters(parameters);
        request.executeAsync();


    }
    public void nextPageParse(String nextQuery)
    {

       // Log.e("page parse","page parse");
        String pagingToken=getPagingToken(nextQuery);
        String until=getUntil(nextQuery);
        final GraphRequest request=new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/462768487218398/posts",

                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        try {
                            // Log.e("Choice page parse", response.toString());
                            JSONObject raw_data = response.getJSONObject();
                            JSONArray data = raw_data.getJSONArray("data");
                            JSONObject likes;
                            JSONObject summary;
                            story.clear();
                            if (data != null) {

                                for (int i = 0; i < data.length(); i++) {
                                    single_story = new Story();
                                    JSONObject single_data = data.getJSONObject(i);
                                    likes = single_data.getJSONObject("likes");
                                    summary = likes.getJSONObject("summary");

                                    if (flag == 0) {
                                        // if((single_data.getString("message").contains(choice))||(choice=="#Timeline")) {

                                        if ((choice == Constants.Keys.QUOTE_OF_THE_DAY)) {

                                            String timeCreated = single_data.getString("created_time");
                                     /*  Log.e("date",timeCreated);
                                       Log.e("date",single_data.getString("created_time"));*/
                                            if (checkDate(timeCreated)) {
                                                single_story.setFull_picture(single_data.getString("full_picture"));
                                                single_story.setObject_id(single_data.getString("id"));
                                                single_story.setCan_like(summary.getBoolean("can_like"));
                                                single_story.setHas_liked(summary.getBoolean("has_liked"));
                                                single_story.setTotal_count(summary.getInt("total_count"));
                                                single_story.setMessage(single_data.getString("message"));
                                                story.add(single_story);

                                            }


                                        }

                                        if ((choice == Constants.Keys.FEEDS)) {
                                          //  Log.e("Choice page parse", choice);
                                            single_story.setFull_picture(single_data.getString("full_picture"));
                                            single_story.setObject_id(single_data.getString("id"));
                                            single_story.setCan_like(summary.getBoolean("can_like"));
                                            single_story.setHas_liked(summary.getBoolean("has_liked"));
                                            single_story.setTotal_count(summary.getInt("total_count"));
                                            single_story.setMessage(single_data.getString("message"));
                                            story.add(single_story);
                                        }
                                        if ((single_data.getString("message").contains(choice))) {
                                         //   Log.e("Choice", choice);
                                          //  Log.e("message", single_data.getString("message"));
                                            single_story.setFull_picture(single_data.getString("full_picture"));
                                            single_story.setObject_id(single_data.getString("id"));
                                            single_story.setCan_like(summary.getBoolean("can_like"));
                                            single_story.setHas_liked(summary.getBoolean("has_liked"));
                                            single_story.setTotal_count(summary.getInt("total_count"));
                                            single_story.setMessage(single_data.getString("message"));
                                            story.add(single_story);
                                        }
                                    } else {
                                        if (summary.getBoolean("has_liked")) {
                                           // Log.e("Liked fragment", "liked");
                                            single_story.setFull_picture(single_data.getString("full_picture"));
                                            single_story.setMessage(single_data.getString("message"));
                                            single_story.setObject_id(single_data.getString("id"));
                                            single_story.setCan_like(summary.getBoolean("can_like"));
                                            single_story.setHas_liked(summary.getBoolean("has_liked"));
                                            single_story.setTotal_count(summary.getInt("total_count"));
                                            story.add(single_story);

                                        }

                                    }

                                }
                                // This is to refresh the dataset.. the control goes to onpostexecute with the above code running in background. therefore, this will refresh the list view.
                                // Leaving this as it is for now I don't think it will cause much problem later..spinner can be displayed from here...
                                // delegate.processFinish(story);
                               // Log.e("Next Page Parse", "here");
                                listener.processFinish(story);


                                JSONObject paging = raw_data.getJSONObject("paging");
                                String nextPage = paging.getString("next");
                                if (nextPage != null)
                                    nextPageParse(nextPage);

                            }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                            catch(NullPointerException e)
                            {

                            }


                    }
                }
        );
        Bundle parameters = new Bundle();
        //  parameters.putString("fields", "full_picture,message,id,likes.limit(0).summary(true)");
        parameters.putString("fields", "full_picture,created_time,message,id,likes.limit(0).summary(true)");
        parameters.putString("until",until);
        parameters.putString("_paging_token",pagingToken);
        parameters.putString("limit","100");
        request.setParameters(parameters);
        request.executeAsync();





    }

    private String getPagingToken(String nextQuery) {
        String tokenStart=nextQuery.substring(nextQuery.indexOf("&__paging_token=")+16);
        String token="";
        for(int i=0;i<tokenStart.length();i++) {
            char c=tokenStart.charAt(i);
           if((c=='&')||(c=='"'))
           {
               break;
           }
            else
               token=token+c;
        }
       // Log.e("paging token=",token);
        return token;
    }
    private String getUntil(String nextQuery) {
        String tokenStart=nextQuery.substring(nextQuery.indexOf("&until=")+7);
        String token="";
        for(int i=0;i<tokenStart.length();i++) {
            char c=tokenStart.charAt(i);
            if((c=='&')||(c=='"'))
            {
                break;
            }
            else
                token=token+c;
        }
       // Log.e("until=",token);
        return token;

    }


    boolean checkDate(String createdDate)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(createdDate);
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = df.format(c.getTime());

            Date end = sdf.parse(currentDate);
            long diff = end.getTime() - start.getTime();

            long days=diff / (1000 * 60 * 60 * 24);
            //Log.e("days",("Difference between  " + end + " and "+ start+" is "
           //         + (diff / (1000 * 60 * 60 * 24)) + " days."));
            if(days==2)
            return true;
            else
            return false;


        }
        catch (Exception ex)
        {
            Log.e("Exceptoin",ex.toString());

        }
        return true;
    }


}