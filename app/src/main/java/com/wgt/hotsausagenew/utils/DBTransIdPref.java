package com.wgt.hotsausagenew.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by debasish on 08-01-2018.
 */

public class DBTransIdPref {

    private static final String DB_ID_PREF = "db_id_pref";
    private static final String DB_ID_KEY = "id_key";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public DBTransIdPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(DB_ID_PREF, Context.MODE_PRIVATE);
    }

    public int getID() {
        return sharedPreferences.getInt(DB_ID_KEY, 1);
    }

    public void incrementID() {
        editor = sharedPreferences.edit();
        editor.putInt(DB_ID_KEY, getID() + 1);
        editor.apply();
    }
}
