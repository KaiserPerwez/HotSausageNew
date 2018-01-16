package com.wgt.hotsausagenew.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wgt.hotsausagenew.services.TransactionSyncService;

import java.util.Calendar;

public class BootCompletedReceiver extends BroadcastReceiver {

    private long timeInterval = System.currentTimeMillis() + (10 * 60 * 1000); // 10 mins

    @Override
    public void onReceive(Context context, Intent intent) {

        //init AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //TransactionSyncService intent
        Intent i = new Intent(context, TransactionSyncService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        //to fire AlarmCancelReceiver, which will cancel the alarm that calls TransactionSyncService
        Intent cancel_i = new Intent(context, AlarmCancelReceiver.class);
        cancel_i.putExtra("pendingIntent", pi);
        PendingIntent cancel_pi = PendingIntent.getBroadcast(context, 0, cancel_i, 0);

        //calender object, to set fire time at 6:00 P.M.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 00);


        //calender object, to set fire time at 6:00 A.M.
        Calendar calendar_cancel = Calendar.getInstance();
        calendar_cancel.setTimeInMillis(System.currentTimeMillis());
        calendar_cancel.set(Calendar.HOUR_OF_DAY, 6);
        calendar_cancel.set(Calendar.MINUTE, 00);

        //to fire TransactionSyncService
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), timeInterval, pi);

        //to fire AlarmCancelReceiver to cancel TransactionSyncService
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar_cancel.getTimeInMillis(), cancel_pi);


    }
}
