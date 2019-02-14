package com.epicquotes.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.*;

import com.epicquotes.Analytics.MyApplication;
import com.epicquotes.R;
import com.epicquotes.activity.FullImage;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


/**
 * Created by mohahm01 on 30-09-2015.
 */
public class MyCustomAdapter extends ArrayAdapter<Story> {


    private ArrayList<Story> storyArrayList;
    LayoutInflater vi ;
    private Paint paint;
    private Paint paintBorder;
    private BitmapShader shader;
    DownloadManager downloadmanager;
    Animation expandIn,slideDown;
    Context mcontext;
    Fragment fragment;
    List<String> permissionNeeds= Arrays.asList("publish_actions");



    Story story;

    public MyCustomAdapter(Context context, int textViewResourceId,
                           ArrayList<Story> storyArrayList,Fragment fragment) {
        super(context, textViewResourceId, storyArrayList);
        this.storyArrayList=storyArrayList;
        this.vi = LayoutInflater.from(context);
        expandIn = AnimationUtils.loadAnimation(context, R.anim.expand_in);
        slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        File folder = new File(Environment.getExternalStorageDirectory() + "/Epic Quotes");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }
       this.fragment=fragment;
    }

    @Override
    public Resources.Theme getDropDownViewTheme() {
        return super.getDropDownViewTheme();
    }
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    private class ViewHolder {
        TextView total_count;
        ImageView imageView;
        ImageView imageView_like;
        ImageView download;
        ImageView share;
        TextView saveText,shareText;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;
        try {
            if (v == null) {

                v = vi.inflate(R.layout.custom_layout, null);


                holder = new ViewHolder();
                holder.total_count = (TextView) v.findViewById(R.id.textView2);
                holder.imageView = (ImageView) v.findViewById(R.id.imageView1);
                holder.imageView_like = (ImageView) v.findViewById(R.id.imageView2);
                holder.download=(ImageView) v.findViewById(R.id.imageView4);
                // holder.imageView_load = (ImageView) v.findViewById(R.id.imageViewLoad);
                holder.share=(ImageView)v.findViewById(R.id.imageView3);

                holder.shareText=(TextView) v.findViewById(R.id.textView3);
                holder.saveText=(TextView) v.findViewById(R.id.textView4);

                v.setTag(holder);

            } else {
                holder = (ViewHolder) v.getTag();
            }
            story = new Story();
            story = storyArrayList.get(position);
            //holder.full_picture.setText(story.getFull_picture());
            //  holder.total_count.setText("" + story.getTotal_count() + "");
            holder.total_count.setText(doubleToStringNoDecimal(story.getTotal_count()));
            if (story.getHas_liked()) {
                holder.imageView_like.setTag(R.drawable.heart_liked);
                holder.imageView_like.setImageDrawable(getContext().getResources().getDrawable(R.drawable.heart_liked));


            } else {
                holder.imageView_like.setTag(R.drawable.heart);
                holder.imageView_like.setImageDrawable(getContext().getResources().getDrawable(R.drawable.heart));

            }
        /*  holder.can_like.setText(story.getCan_like()+"");
        holder.has_liked.setText(story.getHas_liked()+"");*/
            holder.saveText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  holder.download.performClick();
                }
            });
            holder.shareText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.share.performClick();
                }
            });
            holder.imageView_like.setOnClickListener(mOnButtonClickListener);
            holder.share.setOnClickListener(shareListener);
            holder.download.setOnClickListener(downloadListener);
            holder.total_count.setOnClickListener(totalCount);

            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(story.getFull_picture()).placeholder(R.drawable.background_blank).into(holder.imageView);
            holder.imageView.setOnClickListener(imageClick);

        }
        catch(OutOfMemoryError e)
        { Toast.makeText(getContext(),"Out of memory exception",Toast.LENGTH_SHORT).show();}
        return v;
    }

    private View.OnClickListener totalCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Story temp;
            ListView mListView = (ListView) v.getParent().getParent();
            final int position = mListView.getPositionForView((View) v.getParent());
            temp=storyArrayList.get(position);
            // Toast.makeText(getContext(),"ID"+temp.getObject_id(),Toast.LENGTH_SHORT).show();
            TextView tv=(TextView)v.findViewById(R.id.textView2);
            String count=tv.getText().toString();
            count=count.replace(",","");
            long countLong=Long.parseLong(count);



            try {
                if (!temp.has_liked) {

                    temp.setHas_liked(true);

                    storyArrayList.set(position, temp);
                    likeAction(temp.getObject_id());
                   // View rowView = mListView.getChildAt(position);
                    View rowView = getViewByPosition(position,mListView);
                    if (rowView != null) {
                      //  View imageV = ((ViewGroup) rowView).getChildAt(0);
                        View likeV = ((ViewGroup) rowView).getChildAt(1);
                        ((ImageView)likeV).setImageDrawable(getContext().getResources().getDrawable(R.drawable.heart_liked));
                        // hide Image from here....
                      //  imageV.startAnimation(slideDown);
                        ((ImageView)likeV).startAnimation(expandIn);
                        countLong++;
                        String newCount=doubleToStringNoDecimal(countLong);
                        tv.setText(newCount);

                    }



                    // Google Analytics sending request
                    MyApplication.getInstance().trackEvent("Quote Liked", "Clicked", "");
                } else {

                    temp.setHas_liked(false);

                    storyArrayList.set(position, temp);
                    unlikeAction(temp.getObject_id());
                   View rowView = getViewByPosition(position,mListView);
                    if (rowView != null) {

                        View likeV = ((ViewGroup) rowView).getChildAt(1);
                        ((ImageView)likeV).setImageDrawable(getContext().getResources().getDrawable(R.drawable.heart));
                        // hide Image from here....  imageV.startAnimation(slideDown);

                    }
                    countLong--;
                    String newCount=doubleToStringNoDecimal(countLong);
                    tv.setText(newCount);

                    // Google Analytics sending request
                    MyApplication.getInstance().trackEvent("Quote Un-Liked", "Clicked ", "");
                }
            }
            catch(Exception e)
            {
                Toast.makeText(getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                Log.e("error",e.toString());
                MyApplication.getInstance().trackException(e);
            }
        }
    };

    private View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Story temp;
            ListView mListView = (ListView) v.getParent().getParent();
            final int position = mListView.getPositionForView((View) v.getParent());
            temp=storyArrayList.get(position);
            // Toast.makeText(getContext(),"ID"+temp.getObject_id(),Toast.LENGTH_SHORT).show();
            ImageView like=(ImageView)v.findViewById(R.id.imageView2);



            try {
                View rowView = getViewByPosition(position,mListView);

                View tvCount=((ViewGroup) rowView).getChildAt(2);
                TextView tv=(TextView)tvCount.findViewById(R.id.textView2);
                String count=tv.getText().toString();
                count=count.replace(",","");
                long countLong=Long.parseLong(count);
                if (getDrawableId(like) == R.drawable.heart) {
                    like.setTag(R.drawable.heart_liked);
                    temp.setHas_liked(true);
                    storyArrayList.set(position, temp);
                    like.setImageDrawable(getContext().getResources().getDrawable(R.drawable.heart_liked));
                    likeAction(temp.getObject_id());
                    v.startAnimation(expandIn);

                    if (rowView != null) {
                       // View imageV = ((ViewGroup) rowView).getChildAt(0);

                        countLong++;
                        String newCount=doubleToStringNoDecimal(countLong);
                        tv.setText(newCount);


                        // hide Image from here....
                       // imageV.startAnimation(slideDown);
                    }



                    // Google Analytics sending request
                    MyApplication.getInstance().trackEvent("Quote Liked", "Clicked", "");
                } else {
                    like.setTag(R.drawable.heart);
                    temp.setHas_liked(false);
                    storyArrayList.set(position, temp);
                    like.setImageDrawable(getContext().getResources().getDrawable(R.drawable.heart));
                    unlikeAction(temp.getObject_id());

                    countLong--;
                    String newCount=doubleToStringNoDecimal(countLong);
                    tv.setText(newCount);




                    // Google Analytics sending request
                    MyApplication.getInstance().trackEvent("Quote Un-Liked", "Clicked ", "");
                }
            }
            catch(Exception e)
            {
                Toast.makeText(getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                Log.e("error",e.toString());
                MyApplication.getInstance().trackException(e);
            }
        }
    };
    private View.OnClickListener imageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Story temp;
            ListView mListView = (ListView) v.getParent().getParent();
            final int position = mListView.getPositionForView((View) v.getParent());
            temp=storyArrayList.get(position);
            // Toast.makeText(getContext(),"ID"+temp.getObject_id(),Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getContext(),FullImage.class);
            intent.putExtra("url",temp.getFull_picture());
            intent.putExtra("message",temp.getMessage());
            getContext().startActivity(intent);

        }
    };
    private View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Story temp;
            ListView mListView = (ListView) v.getParent().getParent();
            final int position = mListView.getPositionForView((View) v.getParent());
            temp=storyArrayList.get(position);
            // Toast.makeText(getContext(),"ID"+temp.getObject_id(),Toast.LENGTH_SHORT).show();
            sharingFunction(temp.getFull_picture());
            v.startAnimation(expandIn);
           /* View rowView = getViewByPosition(position,mListView);
            if (rowView != null) {
             //   View imageV = ((ViewGroup) rowView).getChildAt(0);
                // hide Image from here....
               // imageV.startAnimation(slideDown);
            }*/
            // Google Analytics sending request
            MyApplication.getInstance().trackEvent("Quote Shared", "Clicked  ", "");


        }
    };
    private View.OnClickListener downloadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Story temp;
            ListView mListView = (ListView) v.getParent().getParent();
            v.startAnimation(expandIn);
            final int position = mListView.getPositionForView((View) v.getParent());
            temp=storyArrayList.get(position);
            Toast.makeText(getContext(),"Image saved to gallery",Toast.LENGTH_LONG).show();
           /* View rowView = getViewByPosition(position,mListView);
            if (rowView != null) {
                View imageV = ((ViewGroup) rowView).getChildAt(0);
                // hide Image from here....
                imageV.startAnimation(slideDown);
            }

*/
            try {
                String servicestring = Context.DOWNLOAD_SERVICE;
                Uri uri = Uri.parse(temp.getFull_picture());
                // Toast.makeText(getContext(), "coming here", Toast.LENGTH_SHORT).show();
                downloadmanager = (DownloadManager) getContext().getSystemService(servicestring);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle("Downloading");
                //Restrict the types of networks over which this download may proceed.
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                //Set whether this download may proceed over a roaming connection.
                request.setAllowedOverRoaming(false);
                //Set the local destination for the downloaded file to a path within the application's external files directory
                request.setDestinationInExternalPublicDir("/Epic Quotes", temp.getObject_id() + ".jpg");
                // request.setDestinationInExternalFilesDir(getContext(), , temp.getObject_id()+".jpg");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                Long reference = downloadmanager.enqueue(request);
            }
            catch(Exception ex)
            {
                MyApplication.getInstance().trackException(ex);
            }


            // Google Analytics sending request
            MyApplication.getInstance().trackEvent("Quote Downloaded", "Clicked", "");



        }
    };
    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {

        }
    };
    private int getDrawableId(ImageView iv) {
        return (Integer) iv.getTag();
    }
    public void sharingFunction(String url_1)
    {
        try {
            URL url = new URL(url_1);
            Intent intent=new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            intent.putExtra(Intent.EXTRA_STREAM, getImageUri(getContext(), image));
            getContext().startActivity(Intent.createChooser(intent, "Share"));
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void likeAction(String object_id)
    {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+object_id+"/likes",

                null,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        if(response.getError()!=null) {
                            Toast.makeText(getContext(), "Allow the POST permission to send likes on Facebook.We do not post anything else on your behalf.", Toast.LENGTH_LONG).show();

                            if (fragment.isAdded())
                                LoginManager.getInstance().logInWithPublishPermissions(fragment, permissionNeeds);
                        }

                      /*  Log.e("data",response.toString());
                        JSONObject raw_data=response.getJSONObject();
                        String data= null;
                        try {

                            data = raw_data.getString("success");
                           // Toast.makeText(getContext(),data,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/



                        // This is to refresh the dataset.. the control goes to onpostexecute with the above code running in background. therefore, this will refresh the list view.
                        // Leaving this as it is for now I don't think it will cause much problem later..spinner can be displayed from here...



                    }
                }
        ).executeAsync();
      /*  Bundle parameters = new Bundle();
        parameters.putString("fields", "full_picture,id,likes.limit(0).summary(true)");
        parameters.putString("limit", "100");
        request.setParameters(parameters);
        request.executeAsync();*/
    }
    public void unlikeAction(String object_id)
    {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+object_id+"/likes",

                null,
                HttpMethod.DELETE,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        if(response.getError()!=null) {
                            Toast.makeText(getContext(), "Allow the POST permission to send likes on Facebook.We do not post anything else on your behalf.", Toast.LENGTH_LONG).show();

                            if (fragment.isAdded())
                                LoginManager.getInstance().logInWithPublishPermissions(fragment, permissionNeeds);
                        }
                      /*  Log.e("data",response.toString());
                        JSONObject raw_data=response.getJSONObject();

                        String data= null;
                        try {
                            data = raw_data.getString("success");
                          //  Toast.makeText(getContext(),data,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                        }*/

                        // This is to refresh the dataset.. the control goes to onpostexecute with the above code running in background. therefore, this will refresh the list view.
                        // Leaving this as it is for now I don't think it will cause much problem later..spinner can be displayed from here...



                    }
                }
        ).executeAsync();


    }
    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);;
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }


}