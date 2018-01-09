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
        editor.commit();
    }

    public String getTime() {
        return sharedPreferences.getString(LAST_TRANSICTION_TIME_KEY, "");
    }

}
