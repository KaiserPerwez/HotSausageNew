package com.wgt.hotsausagenew.networking;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wgt.hotsausagenew.model.UserModel;
import com.wgt.hotsausagenew.utils.ToastUtil;
import com.wgt.hotsausagenew.utils.Urls;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by debasish on 11-01-2018.
 */

public class SyncUser implements Response.ErrorListener, Response.Listener<String> {
    public static final int KEY_UPLOAD_SINGLE_USER = 1;
    public static final int KEY_DOWNLOAD_ALL_USERS = 2;
    public static final int KEY_DELETE_SINGLE_USER = 3;

    private SyncUserListener listener;
    private Context context;
    private VolleySingleton singleton;

    private int key;

    //constructor
    public SyncUser(Context context) {
        this.context = context;
        singleton = VolleySingleton.getInstance(context);
    }

    //==============================NETWORK CALLS==========================

    public void uploadSingleUser(final UserModel userModel) {
//        List<UserModel> listOfUsers = new ArrayList<>();
//        listOfUsers.add(userModel);
//        Gson gson = new Gson();
//        final String requestBody = gson.toJson(listOfUsers);

        StringRequest request = new StringRequest(Request.Method.POST, Urls.UPLOAD_SINGLE_USER, this, this) {
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("Content-Type", "application/json");
                return params;
            }*/

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("data", requestBody);
                params.put("username", userModel.username);
                params.put("password", userModel.password);
                params.put("site", userModel.site);

                return params;
            }
        };
        singleton.addToRequestQueue(request);
    }

    public void downloadAllUsers() {
        StringRequest request = new StringRequest(Request.Method.GET, Urls.GET_ALL_USERS, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        singleton.addToRequestQueue(request);
    }

    public void deleteSingleUserFromRemote(final int user_id) {
        StringRequest request = new StringRequest(Request.Method.POST, Urls.DELETE_SINGLE_USER, this, this) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", Integer.toString(user_id));
                return params;
            }
        };
        singleton.addToRequestQueue(request);

    }

    //==========================VOLLEY CALLBACKS=====================
    @Override
    public void onErrorResponse(VolleyError error) {
        if (listener != null) {
            listener.onFailure(key, error);
        } else {
            ToastUtil.showToastGeneric(context, "SyncUsers ERROR : \n\n" + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(String response) {
        if (listener != null) {
            listener.onSuccess(key, response);
        } else {
            ToastUtil.showToastGeneric(context, "SyncUsers RESPONSE : \n\n" + response, Toast.LENGTH_SHORT).show();
        }
    }
    //==========================VOLLEY CALLBACKS END=====================

    public void setSyncUserListener(int key, SyncUserListener listener) {
        this.key = key;
        this.listener = listener;
    }

    //-------------------------listener interface--------------------
    public interface SyncUserListener {
        void onSuccess(int key, String response);

        void onFailure(int key, VolleyError error);
    }
    //-------------------------listener interface END--------------------
}
