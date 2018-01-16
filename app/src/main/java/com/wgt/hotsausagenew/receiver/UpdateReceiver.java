package com.wgt.hotsausagenew.receiver;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.wgt.hotsausagenew.utils.ToastUtil;

import java.io.File;

/**
 * Created by debasish on 11-01-2018.
 */

public class UpdateReceiver extends BroadcastReceiver {
    private ProgressDialog progressDialog;
    private Context context;
    private long downloadID;
    private File downloadFile;

    public UpdateReceiver(ProgressDialog progressDialog, Context context, long downloadID, File downloadFile) {
        this.progressDialog = progressDialog;
        this.context = context;
        this.downloadID = downloadID;
        this.downloadFile = downloadFile;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (id == downloadID) { // apk downloaded
            // dismiss dialog if showing
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (!downloadFile.exists()) { // if file is not found, show Toast and return
                ToastUtil.showToastGeneric(this.context, "Update File not found.", Toast.LENGTH_SHORT).show();
                return;
            }
            //show app install dialog to the user
            Intent updateIntent = new Intent(Intent.ACTION_VIEW);
            updateIntent.setDataAndType(Uri.fromFile(downloadFile), "application/vnd.android.package-archive");
            this.context.startActivity(updateIntent);
        }
    }
}
