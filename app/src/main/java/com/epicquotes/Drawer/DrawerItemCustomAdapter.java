package com.epicquotes.Drawer;

/**
 * Created by SAMI on 02-11-2015.
 */
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.epicquotes.R;

public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {

    Context mContext;
    int layoutResourceId;
    ObjectDrawerItem data[] = null;

    /*
     * @mContext - app context
     *
     * @layoutResourceId - the listview_item_row.xml
     *
     * @data - the ListItem data
     */
    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ObjectDrawerItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    /*
     * @We'll overried the getView method which is called for every ListItem we
     * have.
     *
     * @There are lots of different caching techniques for Android ListView to
     * achieve better performace especially if you are going to have a very long
     * ListView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        // inflate the listview_item_row.xml parent
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        // get the elements in the layout
        ObjectDrawerItem folder = data[position];
        Log.e("error",folder.name);
        if(folder.header)
        {
            listItem.findViewById(R.id.textViewName).setVisibility(View.INVISIBLE);
            listItem.findViewById(R.id.footer).setVisibility(View.INVISIBLE);
            TextView tvHeader = (TextView) listItem.findViewById(R.id.header_title);
            tvHeader.setText(folder.name);
            Log.e("Header value", folder.name);
        }
       else if(folder.footer)
        {
            listItem.findViewById(R.id.textViewName).setVisibility(View.INVISIBLE);
            listItem.findViewById(R.id.header).setVisibility(View.INVISIBLE);
            TextView tvFooter = (TextView) listItem.findViewById(R.id.footer_title);
            tvFooter.setText(folder.name);
        }
        else
        {
            listItem.findViewById(R.id.footer).setVisibility(View.INVISIBLE);
            listItem.findViewById(R.id.header).setVisibility(View.INVISIBLE);
            TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);
            textViewName.setText(folder.name);
        }

/*
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

		*//*
		 * Set the data for the list item. You can also set tags here if you
		 * want.
		 *//*


        textViewName.setText(folder.name);*/

        return listItem;
    }

}