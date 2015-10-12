package com.example.tx.hacktx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by justi on 9/27/2015.
 */
public class Boot extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, MainActivity.class);
            context.startService(serviceIntent);
        }
    }
}
