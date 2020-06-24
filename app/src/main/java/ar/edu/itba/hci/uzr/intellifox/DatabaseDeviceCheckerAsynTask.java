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

import androidx.core.app.NotificationCompat;

import java.lang.ref.WeakReference;

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

    private WeakReference<Context> weakContext;
    private String typeName, deviceID;
    private Device actualDevice;

    public DatabaseDeviceCheckerAsynTask(Context context, String typeName, String deviceID, Device actualDevice) {
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
        String message = "";

        if (typeName.equals("faucet")) {
            checkTapChanges((TapDevice) device, message);
        }
        else if (typeName.equals("ac")) {
            checkAcChanges((AcDevice) device, message);
        }
        else if (typeName.equals("lamp")) {
            checkLightChanges((LightDevice) device, message);
        }
        else if (typeName.equals("oven")) {
            checkOvenChanges((OvenDevice) device, message);
        }
        else if (typeName.equals("speaker")) {
            checkSpeakerChanges((SpeakerDevice) device, message);
        }
        else if (typeName.equals("blinds")) {
            checkBlindChanges((BlindDevice) device, message);
        }
        else if (typeName.equals("vacuum")) {
            checkVacuumChanges((VacuumDevice) device, message);
        }
        else if (typeName.equals("door")) {
            checkDoorChanges((DoorDevice) device, message);
        }
    }

    private void checkTapChanges(TapDevice device, String message) {
        Log.d("BD_GET_DEVICE", device.toString());
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                showNotification(context, typeName, device);
            }
        };
    }

    private void checkAcChanges(AcDevice device, String message) {
        Log.d("BD_GET_DEVICE", device.toString());
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                showNotification(context, typeName, device);
            }
        };
    }

    private void checkLightChanges(LightDevice device, String message) {
        Log.d("BD_GET_DEVICE", device.toString());
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                showNotification(context, typeName, device);
            }
        };
    }

    private void checkOvenChanges(OvenDevice device, String message) {
        Log.d("BD_GET_DEVICE", device.toString());
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                showNotification(context, typeName, device);
            }
        };
    }

    private void checkSpeakerChanges(SpeakerDevice device, String message) {
        Log.d("BD_GET_DEVICE", device.toString());
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                showNotification(context, typeName, device);
            }
        };
    }

    private void checkBlindChanges(BlindDevice device, String message) {
        Log.d("BD_GET_DEVICE", device.toString());
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                showNotification(context, typeName, device);
            }
        };
    }

    private void checkVacuumChanges(VacuumDevice device, String message) {
        Log.d("BD_GET_DEVICE", device.toString());
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                showNotification(context, typeName, device);
            }
        };
    }

    private void checkDoorChanges(DoorDevice device, String message) {
        Log.d("BD_GET_DEVICE", device.toString());
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                showNotification(context, typeName, device);
            }
        };
    }

    private void showNotification(Context context, String deviceType, Device device) {
        // Create the intent to start Activity when notification in action bar is
        // clicked.

        String eventName = "action";
        String title = device.getName();
        String text = "an " + eventName + " was performed on device " + device.getName();
        Bitmap icon = getIcon(device, context);

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

    private Bitmap getIcon(Device device, Context context){
        //get the icon
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_remote);
    }
}