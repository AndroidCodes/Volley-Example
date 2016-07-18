package com.example.androidcodes.volleyexample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {

    // public static boolean isNetworkAvailable(Context context) {
    // ConnectivityManager connectivityManager = (ConnectivityManager) context
    // .getSystemService(Context.CONNECTIVITY_SERVICE);
    // NetworkInfo activeNetworkInfo = connectivityManager
    // .getActiveNetworkInfo();
    //
    // return activeNetworkInfo != null
    // && activeNetworkInfo.isConnectedOrConnecting();
    // }

    // public static boolean isConnectingToInternet(Context context) {
    // ConnectivityManager connectivity = (ConnectivityManager) context
    // .getSystemService(Context.CONNECTIVITY_SERVICE);
    // if (connectivity != null) {
    // NetworkInfo[] info = connectivity.getAllNetworkInfo();
    // if (info != null)
    // for (int i = 0; i < info.length; i++)
    // if (info[i].getState() == NetworkInfo.State.CONNECTED) {
    // return true;
    // }
    //
    // }
    // return false;
    // }

    public static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                if (activeNetwork.isConnectedOrConnecting())
                    return true;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                if (activeNetwork.isConnectedOrConnecting())
                    return true;
        }
        return false;
    }
}
