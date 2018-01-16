package com.wgt.hotsausagenew.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wgt.hotsausagenew.R;

/**
 * Created by debasish on 12-01-2018.
 */

public class ToastUtil {
    private static Toast toast = null;

    public static Toast showToastGeneric(Context mcontext, String message, int dureation) {
        toast = Toast.makeText(mcontext, message, dureation);
        toast.setGravity(Gravity.BOTTOM, 0, 30);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setGravity(Gravity.CENTER);
        toastTV.setTextSize(30);
        toastTV.setTextColor(Color.WHITE);
        toast.getView().setBackgroundResource(R.drawable.customtoast);
        return toast;
    }
}
