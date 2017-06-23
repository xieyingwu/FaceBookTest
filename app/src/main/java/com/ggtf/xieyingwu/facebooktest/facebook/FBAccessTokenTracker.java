package com.ggtf.xieyingwu.facebooktest.facebook;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

/**
 * Created by xieyingwu on 2017/6/22.
 */

public class FBAccessTokenTracker extends AccessTokenTracker {
    private static final String TAG = FBAccessTokenTracker.class.getName();

    @Override
    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
        Log.w(TAG, "thread = " + Thread.currentThread());
        Log.w(TAG, "oldAccessToken = " + oldAccessToken);
        Log.w(TAG, "currentAccessToken = " + currentAccessToken);
    }
}
