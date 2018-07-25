package com.example.maboy.alwaysondisplay;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by maboy on 03/09/2017.
 */

public class Permission {
    public final static int REQUIRED_PERMISSION_REQUEST_CODE = 17;

    private Context mContext;

    public Permission(Context context) {
        mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isRequiredPermissionGranted() {
        if (isMarshmallowOrHigher()) {
            return Settings.canDrawOverlays(mContext);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public Intent createRequiredPermissionModifySystemSettings() {
        if (isMarshmallowOrHigher()) {
            Intent intent =
                    new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
//                            ,Uri.parse("package:" + mContext.getPackageName()));
            return intent;
        }
        return null;
    }

    private boolean isMarshmallowOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
