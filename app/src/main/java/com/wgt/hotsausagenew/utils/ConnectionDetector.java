package com.wgt.hotsausagenew.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;

/**
 * Created by Admin on 27-12-2017.
 */

public class ConnectionDetector extends AsyncTask<Void, Void, Boolean> {
    private Context _context;
    private ProgressDialog dialog;
    private boolean showDialog;
    private ConnectionDetectorListener listener;

    public ConnectionDetector(Context context, boolean showDialog) {
        this._context = context;
        this.showDialog = showDialog;
        if (showDialog) {
            dialog = new ProgressDialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Checking networking status...");
        }
    }
    /*public boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager= (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }*/

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 " + Urls.NETWORK_STATE_CHECK);
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showDialog) {
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (showDialog) {
            dialog.dismiss();
        }
        if (listener != null) {
            listener.onConnectionDetected(aBoolean);
        }
    }

    public void setConnectionDetectorListener(ConnectionDetectorListener listener) {
        this.listener = listener;
    }

    public interface ConnectionDetectorListener {
        void onConnectionDetected(boolean status);
    }
}
