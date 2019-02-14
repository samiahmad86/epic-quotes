package com.epicquotes.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mohahm01 on 29-09-2015.
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;
    private Context context;
    private ProgressBar progress;

    public ImageLoadTask(String url, ImageView imageView,Context context,ProgressBar progress) {
        this.url = url;
        this.imageView = imageView;
        this.context=context;
        this.progress=progress;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();


    }
    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
       // imageView.setImageBitmap(result);
        Bitmap temp=CircleTransform.fastblur(result,4,context);
        imageView.setImageBitmap(temp);
        progress.setVisibility(View.GONE);
    }

}