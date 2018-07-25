package com.example.maboy.alwaysondisplay;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by maboy on 19/09/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    public static final String TAG = "ON";

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.e(TAG, "onNotificationPosted: ");

        String pack = sbn.getPackageName();

        Context remotePackageContext = null;
        Bitmap bmp = null;
        try {
            remotePackageContext = getApplicationContext().createPackageContext(pack, 0);
            Drawable icon = remotePackageContext.getResources().getDrawable(sbn.getNotification().icon);
            if (icon != null) {
                bmp = ((BitmapDrawable) icon).getBitmap();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent msgrcv = new Intent("post");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        msgrcv.putExtra("icon", byteArray);
        msgrcv.putExtra("pack", pack);
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.e(TAG, "onNotificationRemoved: ");

        Intent remove = new Intent("remove");
        String packRemove = sbn.getPackageName();
        remove.putExtra("pack_remove", packRemove);
        LocalBroadcastManager.getInstance(context).sendBroadcast(remove);
    }
}
