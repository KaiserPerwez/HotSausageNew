package com.wgt.hotsausagenew.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by debasish on 10-01-2018.
 */

public class UpdateManager {
    public static final String DOWNLOADED_FILE_NAME = "hotsausageUpdate.apk";
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private Context context;
    private long downloadID;

    public UpdateManager(Context context) {
        this.context = context;
        downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(Uri.parse(Urls.UPDATE_URL));
        request.setTitle("Downloading Update...");
        request.setDescription("Hot Sausage update is downloading.");
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, DOWNLOADED_FILE_NAME);
    }

    public long startDownload() {
        downloadID = downloadManager.enqueue(request);
        return downloadID;
    }
}
