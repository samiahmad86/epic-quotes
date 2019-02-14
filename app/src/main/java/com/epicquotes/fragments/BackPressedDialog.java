package com.epicquotes.fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.epicquotes.R;

public class BackPressedDialog extends DialogFragment  implements View.OnClickListener{

    public static String TAG="For User Status";
    public boolean checkForStatus=false;
    public static BackPressedDialog newInstance() {
        BackPressedDialog mBackPressedDialog = new BackPressedDialog();
        return mBackPressedDialog;
    }
    public static BackPressedDialog newInstance(boolean forUserStatus) {
        BackPressedDialog mBackPressedDialog = new BackPressedDialog();
        Bundle args= new Bundle();
        args.putBoolean(TAG,forUserStatus);
        mBackPressedDialog.setArguments(args);
        return mBackPressedDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setCancelable(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dialog, container, false);
        Bundle args=getArguments();
        if(args!=null)
             checkForStatus=args.getBoolean(TAG);


        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOkay = (Button) view.findViewById(R.id.btn_send);
        btnOkay.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

      //  btnCancel.setTextColor(getResources().getColor(R.color.light_blue));
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_cancel: {


                 getDialog().cancel();
               // getActivity().onBackPressed();
                break;
            }

            case R.id.btn_send: {
              //  getDialog().cancel();
                getDialog().cancel();

                break;
            }

        }
    }
}
