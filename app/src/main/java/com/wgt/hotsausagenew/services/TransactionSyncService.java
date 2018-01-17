package com.wgt.hotsausagenew.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.wgt.hotsausagenew.database.AppDatabase;
import com.wgt.hotsausagenew.networking.SyncTransaction;
import com.wgt.hotsausagenew.utils.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

public class TransactionSyncService extends Service {

    private BackgroundTask backgroungTask;
    private AppDatabase database;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        backgroungTask = new BackgroundTask();
        database = AppDatabase.getDatabase(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!backgroungTask.isRunning) {
            backgroungTask.start();
            backgroungTask.setRunning(true);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // Background Thread
    private class BackgroundTask extends Thread {
        private boolean isRunning = false;

        @Override
        public void run() {
            isRunning = true;
            ConnectionDetector detector = new ConnectionDetector(getApplicationContext(), false);
            detector.setConnectionDetectorListener(new ConnectionDetector.ConnectionDetectorListener() {
                @Override
                public void onConnectionDetected(boolean status) {
                    if (!status) {
                        return;
                    }
                    networkCall();
                }
            });
            detector.execute();
        }

        // volley calls
        private void networkCall() {
            SyncTransaction syncTransaction = new SyncTransaction(getApplicationContext());
            syncTransaction.setSyncTransactionListener(new SyncTransaction.SyncTransactionListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("success");
                        String message = jsonObject.getJSONArray("message").toString();

                        if (code.equals("1") && (message.length() > 2)) {
                            String data = message.substring(1, message.length() - 1);
                            String idS[] = data.split(",");

                            // converting idS to int format
                            int arr_id[] = new int[idS.length];
                            for (int i = 0; i < idS.length; i++) {
                                arr_id[i] = Integer.parseInt(idS[i]);
                            }

                            //database call
                            for (int id : arr_id) {
                                database.transactionDAO().setSyncStatus(id);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        // nothing to do now, stop the service
                        stopSelf();
                    }
                }

                @Override
                public void onFailure(VolleyError error) {
                    // nothing to do now, stop the service
                    stopSelf();
                }
            });
            syncTransaction.startSyncing();
        }

        //user defined
        public boolean isRunning() {
            return isRunning;
        }

        public void setRunning(boolean b) {
            isRunning = b;
        }
    } //end of  Thread
}
