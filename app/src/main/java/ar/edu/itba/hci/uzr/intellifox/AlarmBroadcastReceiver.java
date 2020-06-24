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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.database.AppDatabase;
import ar.edu.itba.hci.uzr.intellifox.wrappers.BelledDevices;
import ar.edu.itba.hci.uzr.intellifox.wrappers.TypeAndDeviceId;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private final static String BELLED_DEVICES = "belled_devices";

    private static final String CHANNEL_ID = "NOTIFICATIONS";
    private static final int MY_NOTIFICATION_ID = 1;
    private static SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MainActivity.TAG, "Alarm at: " + DateFormat.getDateTimeInstance().format(new Date()));

        if (sharedPreferences == null) {
            sharedPreferences = SharedPreferencesGetter.getInstance();
        }

        checkBelledSavedDevices(context);
    }

    private void checkBelledSavedDevices(Context context) {
        final Gson gson = new Gson();
        String json = sharedPreferences.getString(BELLED_DEVICES, "");
        if (!json.equals("")) {
            BelledDevices belledDevices = gson.fromJson(json, BelledDevices.class);
            if (belledDevices != null) {
                HashSet<TypeAndDeviceId> tadis =  belledDevices.getBelledDevices();
                if (tadis != null) {
                    for (TypeAndDeviceId tadi: tadis) {
                        String deviceId = tadi.getDeviceId();
                        String typeName = tadi.getTypeName();
                        if (deviceId != null && typeName != null) {
                            Log.d("DEVICE", "Processing");
                            ApiClient.getInstance().getDevice(deviceId, new Callback<Result<Device>>() {
                                @Override
                                public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("GET_DEVICE", "Processing");
                                        Result<Device> result = response.body();
                                        if (result != null) {
                                            Device device = result.getResult();
                                            if (device != null) {
                                                DatabaseDeviceCheckerAsynTask task = new DatabaseDeviceCheckerAsynTask(context, typeName, deviceId, device);
                                                task.execute();
                                            }
                                        } else {
                                            handleError(response);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                                    handleUnexpectedError(t);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    protected <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        List<String> descList = error.getDescription();
        String desc = "";
        if (descList != null) {
            desc = descList.get(0);
        }
        String code = "Code " + String.valueOf(error.getCode());
        Log.e("ERROR", code + " - " + desc);
        /*
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        */
    }

    protected void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }

    private void showNotification(Context context, Device device) {
        // Create the intent to start Activity when notification in action bar is
        // clicked.

        String eventName = "action";
        String title = device.getName();
        String text = "an " + eventName + " was performed on device " + device.getName();
        Bitmap icon = getIcon(device, context);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra(MainActivity.MESSAGE_ID, device.getType().getName() + "," + device.getId());

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
