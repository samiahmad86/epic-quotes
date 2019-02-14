package com.epicquotes.Facebook;

import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

/**
 * Created by mohahm01 on 23-09-2015.
 */


public class GetToken {

    private CallbackManager callbackManager;
    Context context;

    public GetToken(Context context)
    {
        this.context=context;
        callbackManager = CallbackManager.Factory.create();

    }
    public AccessToken doNothing()
    {
        FacebookSdk.sdkInitialize(context);
        //Log.e("from the java class : ", AccessToken.getCurrentAccessToken().getToken().toString());
        return AccessToken.getCurrentAccessToken();
    }
}
