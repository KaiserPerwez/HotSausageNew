package com.wgt.hotsausagenew.networking;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.wgt.hotsausagenew.database.AppDatabase;
import com.wgt.hotsausagenew.model.BillModel;
import com.wgt.hotsausagenew.model.TransactionModel;
import com.wgt.hotsausagenew.utils.ToastUtil;
import com.wgt.hotsausagenew.utils.Urls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by debasish on 11-01-2018.
 */

public class SyncTransaction implements Response.ErrorListener, Response.Listener<String> {
    private List<TransactionModel> listOfTrans;
    private Context context;
    private AppDatabase database;
    private VolleySingleton singleton;

    private SyncTransactionListener listener;

    //constructor
    public SyncTransaction(Context context) {
        this.context = context;
        database = AppDatabase.getDatabase(context);
        singleton = VolleySingleton.getInstance(context);
    }

    //====================================USER DEFINED FUNCTION===============================
    //get all transactions from DB
    private void getAllTransactions() {
        listOfTrans = database.transactionDAO().getAllTransForSync();
        for (TransactionModel model : listOfTrans) {
            List<BillModel> bills = database.billDAO().getAllBillById(model.getTrans_id());
            model.setBillList(bills);
        }
    }

    //actual networking logic
    public void startSyncing() {
        getAllTransactions();
        Gson gson = new Gson();
        final String requestBody = gson.toJson(listOfTrans); //convert to json

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPLOAD_TRANSACTION, this, this) {
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }*/

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("data", requestBody);
                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);
    }

    //===================================USER DEFINED FUNCTION  END===============================


    //-----------------------VOLLEY CALLBACKS---------------------//
    @Override
    public void onErrorResponse(VolleyError error) {
        if (listener != null) {
            listener.onFailure(error);
        } else {
            ToastUtil.showToastGeneric(context, "SyncTrans ERROR : \n\n" + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(String response) {
        if (listener != null) {
            listener.onSuccess(response);
        } else {
            ToastUtil.showToastGeneric(context, "SyncTrans RESPONSE : \n\n" + response, Toast.LENGTH_SHORT).show();
        }
    }
    //-----------------------VOLLEY CALLBACKS END---------------------//

    public void setSyncTransactionListener(SyncTransactionListener listener) {
        this.listener = listener;
    }

    //-------------------------listener interface--------------------
    public interface SyncTransactionListener {
        void onSuccess(String response);

        void onFailure(VolleyError error);
    }
    //-------------------------listener interface END--------------------
}
