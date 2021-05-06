package gr.liakos.spearo.custom.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class NotificationBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            new NotificationHelper(context).scheduleNotification();
//            Toast.makeText(context, "BOOT", Toast.LENGTH_LONG).show();
//        }

    }

}
