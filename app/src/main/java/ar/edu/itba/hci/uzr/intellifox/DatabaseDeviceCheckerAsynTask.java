package ar.edu.itba.hci.uzr.intellifox;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import androidx.core.app.NotificationCompat;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDevice;
import ar.edu.itba.hci.uzr.intellifox.database.AppDatabase;

public class DatabaseDeviceCheckerAsynTask extends AsyncTask<Void, Void, Device> {

    private static final String CHANNEL_ID = "NOTIFICATIONS";
    private static final int MY_NOTIFICATION_ID = 1;

    static AppDatabase db;
    static private Map<String, Integer> typeInfo;

    private WeakReference<Context> weakContext;
    private String typeName, deviceID;
    private Device actualDevice;

    public DatabaseDeviceCheckerAsynTask(Context context, String typeName, String deviceID, Device actualDevice) {

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

        if (db == null) {
            db = DatabaseGetter.getInstance();
        }

        this.weakContext = new WeakReference<>(context);
        this.typeName = typeName;
        this.deviceID = deviceID;

        if (typeName.equals("faucet")) {
            this.actualDevice = createComparableTap(actualDevice);
        }
    }

    private Device createComparableTap(Device actualDevice) {
        TapDevice device = new TapDevice();
        device.setMeta(null);
        device.setRoom(null);
        DeviceState deviceState = actualDevice.getState();
        if (deviceState != null) {
            TapDeviceState state = new TapDeviceState();
            state.setStatus(deviceState.getStatus());
            device.setState(state);
        }
        return device;
    }

    @Override
    protected Device doInBackground(Void... params) {
        return db.getDevice(typeName, deviceID);
    }

    @Override
    protected void onPostExecute(Device device) {
        boolean notify = false;

        Log.d("BD_GET_DEVICE", device.toString());
        if (typeName.equals("faucet")) {
            checkTapChanges((TapDevice) device);
        }
        else if (typeName.equals("ac")) {
            checkAcChanges((AcDevice) device);
        }
        else if (typeName.equals("lamp")) {
            checkLightChanges((LightDevice) device);
        }
        else if (typeName.equals("oven")) {
            checkOvenChanges((OvenDevice) device);
        }
        else if (typeName.equals("speaker")) {
            checkSpeakerChanges((SpeakerDevice) device);
        }
        else if (typeName.equals("blinds")) {
            checkBlindChanges((BlindDevice) device);
        }
        else if (typeName.equals("vacuum")) {
            checkVacuumChanges((VacuumDevice) device);
        }
        else if (typeName.equals("door")) {
            checkDoorChanges((DoorDevice) device);
        }
    }

    private void checkTapChanges(TapDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";
                // Tell the user
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void checkAcChanges(AcDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";
                // Tell the user
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void checkLightChanges(LightDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";
                // Tell the user
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void checkOvenChanges(OvenDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";
                // Tell the user
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void checkSpeakerChanges(SpeakerDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";
                // Tell the user
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void checkBlindChanges(BlindDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";
                // Tell the user
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void checkVacuumChanges(VacuumDevice device) {
        if (!actualDevice.equals(device)) {
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";
                // Tell the user
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void checkDoorChanges(DoorDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";
                // Tell the user
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void showNotification(Context context, String deviceType, Device device, String message) {
        // Create the intent to start Activity when notification in action bar is
        // clicked.

        String eventName = "action";
        String title = device.getName();
        String text = "an " + eventName + " was performed on device " + device.getName();
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), getIconRef(typeName));;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra(MainActivity.MESSAGE_ID, deviceType + "," + device.getId());

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


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.ic_menu_remote)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, builder.build());
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