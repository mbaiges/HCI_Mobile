package ar.edu.itba.hci.uzr.intellifox.broadcast_receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.MainActivity;
import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;


public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_DEVICE_CHANGED = "ar.edu.itba.hci.uzr.intellifox.ACTION_DEVICE_CHANGED";

    public static final String DEVICE_TYPE_NAME_KEY = "device_type_name";
    public static final String DEVICE_ID_KEY = "device_id";
    public static final String DEVICE_NAME_KEY = "device_name";
    public static final String MESSAGE_KEY = "message";

    static private Map<String, Integer> typeInfo;

    private static final String CHANNEL_ID = "NOTIFICATIONS";
    private static int notification_id = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (typeInfo == null) {
            typeInfo = new HashMap<String, Integer>() {
                {
                    put("faucet", R.drawable.ic_device_water_pump);
                    put("ac", R.drawable.ic_device_air_conditioner);
                    put("alarm", R.drawable.ic_device_alarm_light_outline);
                    put("blinds", R.drawable.ic_device_blinds);
                    put("door", R.drawable.ic_device_door);
                    put("refrigerator", R.drawable.ic_device_fridge_outline);
                    put("lamp", R.drawable.ic_device_lightbulb_outline);
                    put("vacuum", R.drawable.ic_device_robot_vacuum);
                    put("speaker", R.drawable.ic_device_speaker);
                    put("oven", R.drawable.ic_device_toaster_oven);
                }
            };
        }

        String deviceTypeName = intent.getStringExtra(DEVICE_TYPE_NAME_KEY);
        String deviceId = intent.getStringExtra(DEVICE_ID_KEY);
        String deviceName = intent.getStringExtra(DEVICE_NAME_KEY);
        String message = intent.getStringExtra(MESSAGE_KEY);

        Log.d("MESSAGE_TO_NOTIFY", message);

        // Create the intent to start Activity when notification in action bar is
        // clicked.

        String title = deviceName;
        Integer iconRef = getIconRef(deviceTypeName);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_circle);
        ;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra(MainActivity.MESSAGE_ID, deviceTypeName + "," + deviceId);

        // The stack builder object will contain an artificial back stack for the
        //        // started Activity.
        //        // This ensures that navigating backward from the Activity leads out of
        //        // your application to the Home screen.
        android.app.TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        // Create the pending intent granting the Operating System to launch activity
        // when notification in action bar is clicked.
        final PendingIntent contentIntent = stackBuilder.getPendingIntent(notification_id, PendingIntent.FLAG_UPDATE_CURRENT);

        Bundle args = new Bundle();
        args.putString(MainActivity.MESSAGE_ID, deviceTypeName + "," + deviceId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(icon)
                .setSmallIcon(getIconRef(deviceTypeName))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .addExtras(args);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notification_id++, builder.build());
    }

    private Integer getIconRef(String typeName){
        if (typeInfo != null) {
            Integer iconRef = typeInfo.get(typeName);
            if (iconRef != null) {
                return iconRef;
            }
        }
        return R.drawable.ic_menu_remote;
    }

}

