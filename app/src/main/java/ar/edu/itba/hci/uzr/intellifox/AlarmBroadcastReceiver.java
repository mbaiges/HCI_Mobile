package ar.edu.itba.hci.uzr.intellifox;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "NOTIFICATIONS";
    private static final int MY_NOTIFICATION_ID = 1;
    private static SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MainActivity.TAG, "Alarm at: " + DateFormat.getDateTimeInstance().format(new Date()));

        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        }

        showNotification(context);

        //To save
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(myObject);
//        editor.putString("MyObject", json);
//        editor.commit();
//        showNotification(context);

        //To retrieve
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("MyObject", "");
//        MyObject obj = gson.fromJson(json, MyObject.class);
    }

    private void showNotification(Context context, Device device) {
        // Create the intent to start Activity when notification in action bar is
        // clicked.
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra(MainActivity.MESSAGE_ID, "speaker,018113fde104afe9");

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        // Create the pending intent granting the Operating System to launch activity
        // when notification in action bar is clicked.
        final PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String eventName = "action";
        String title = device.getName();
        String text = "an " + eventName + "was performed on device " + device.getName();
        Bitmap icon = getIcon(device, context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.ic_menu_remote)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, builder.build());
    }

    private Bitmap getIcon(Device device, Context context){
        //get the icon 
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_remote);
    }
}
