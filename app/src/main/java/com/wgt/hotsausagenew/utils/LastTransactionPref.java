package com.wgt.hotsausagenew.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by debasish on 08-01-2018.
 */

public class LastTransactionPref {
    public static final String LAST_TRANSICTION_TIME_PREF = "last_transaction";
    public static final String LAST_TRANSICTION_TIME_KEY = "time";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public LastTransactionPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(LAST_TRANSICTION_TIME_PREF, Context.MODE_PRIVATE);
    }

    public void saveTime(String value) {
        editor = sharedPreferences.edit();
        editor.putString(LAST_TRANSICTION_TIME_KEY, value);
        editor.apply();
    }

    public String getTime() {
        String t = sharedPreferences.getString(LAST_TRANSICTION_TIME_KEY, null);
        if (t == null) {
            return "";
        }
        String arr[] = t.split(":");
        String h = "";
        String m = "";
        if (arr[0].length() == 1) {
            h = "0" + arr[0];
        } else {
            h = arr[0];
        }
        if (arr[1].length() == 1) {
            m = "0" + arr[1];
        } else {
            m = arr[1];
        }
        return h + ":" + m;
    }
}
