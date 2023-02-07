package com.dasadweb.eamenu_service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    public void notification(Context context, Intent intent, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "eAmenuReciever")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("eAmenu Service")
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }

    public void runService(Context context, Intent intent) {
        // start service
        Intent myIntent = new Intent(context, DestinationActivity.class);
        context.startService(myIntent);

        // repeat alarm daily
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent _intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, 1); // sets interval

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent){
        // sends notification
        notification(context, intent, "My message!");
        // runs service
        runService(context, intent);
    }
}
