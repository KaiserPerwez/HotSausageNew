package com.wgt.hotsausagenew.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Admin on 27-12-2017.
 */

public class ConnectionDetector {
    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }
    public boolean isConnectedToInternet() {

        ConnectivityManager connectivityManager= (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected())
            return true;

        return false;

    }
}
