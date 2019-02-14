package com.epicquotes.Helper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.epicquotes.activity.MainActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohahm01 on 02-10-2015.
 */
public class PostLikeAsync extends AsyncTask<Void, Void, ArrayList<Story>> {

    ArrayList<Story> story=new ArrayList<Story>();

    public PostLikeAsync(MainActivity main_activity)
    {

    }

    @Override
    protected ArrayList<Story> doInBackground(Void... voids) {
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
                            JSONObject likes;JSONObject summary;

                            // This is to refresh the dataset.. the control goes to onpostexecute with the above code running in background. therefore, this will refresh the list view.
                            // Leaving this as it is for now I don't think it will cause much problem later..spinner can be displayed from here...

                                } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "full_picture,id,likes.limit(0).summary(true)");
        parameters.putString("limit","50");
        request.setParameters(parameters);
        request.executeAsync();

        return story;
    }
    @Override
    protected void onPostExecute(ArrayList<Story> result) {

        super.onPostExecute(result);
        Log.e("onpostexecute","yes");
       // main_activity.displayCountries();
        // displayCountries();

    }

}