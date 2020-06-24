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
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDeviceState;
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
        }else if (typeName.equals("ac")) {
            this.actualDevice = createComparableAc(actualDevice);
        }else if (typeName.equals("blinds")) {
            this.actualDevice = createComparableBlind(actualDevice);
        }else if (typeName.equals("door")) {
            this.actualDevice = createComparableDoor(actualDevice);
        }else if (typeName.equals("lamp")) {
            this.actualDevice = createComparableLight(actualDevice);
        }else if (typeName.equals("vacuum")) {
            this.actualDevice = createComparableVacuum(actualDevice);
        }else if (typeName.equals("speaker")) {
            this.actualDevice = createComparableSpeaker(actualDevice);
        }else if (typeName.equals("oven")) {
            this.actualDevice = createComparableOven(actualDevice);
        }

    }

    private Device createComparableOven(Device actualDevice) {
        OvenDevice device = new OvenDevice();
        device.setId(actualDevice.getId());
        device.setName(actualDevice.getName());
        device.setMeta(null);
        device.setRoom(null);
        OvenDeviceState deviceState = (OvenDeviceState)actualDevice.getState();
        if (deviceState != null) {
            OvenDeviceState state = new OvenDeviceState();
            state.setStatus(deviceState.getStatus());
            state.setConvection(deviceState.getConvection());
            state.setGrill(deviceState.getGrill());
            state.setHeat(deviceState.getHeat());
            state.setTemperature(deviceState.getTemperature());
            device.setState(state);
        }
        return device;
    }

    private Device createComparableSpeaker(Device actualDevice) {
        SpeakerDevice device = new SpeakerDevice();
        device.setId(actualDevice.getId());
        device.setName(actualDevice.getName());
        device.setMeta(null);
        device.setRoom(null);
        SpeakerDeviceState deviceState = (SpeakerDeviceState) actualDevice.getState();
        if (deviceState != null) {
            SpeakerDeviceState state = new SpeakerDeviceState();
            state.setStatus(deviceState.getStatus());
            state.setGenre(deviceState.getGenre());
            device.setState(state);
        }
        return device;
    }

    private Device createComparableVacuum(Device actualDevice) {
        VacuumDevice device = new VacuumDevice();
        device.setId(actualDevice.getId());
        device.setName(actualDevice.getName());
        device.setMeta(null);
        device.setRoom(null);
        VacuumDeviceState deviceState = (VacuumDeviceState)actualDevice.getState();
        if (deviceState != null) {
            VacuumDeviceState state = new VacuumDeviceState();
            state.setStatus(deviceState.getStatus());
            state.setLocation(deviceState.getLocation());
            state.setBatteryLevel(deviceState.getBatteryLevel());
            state.setMode(deviceState.getMode());
            device.setState(state);
        }
        return device;
    }

    private Device createComparableLight(Device actualDevice) {
        LightDevice device = new LightDevice();
        device.setId(actualDevice.getId());
        device.setName(actualDevice.getName());
        device.setMeta(null);
        device.setRoom(null);
        LightDeviceState deviceState = (LightDeviceState)actualDevice.getState();
        if (deviceState != null) {
            LightDeviceState state = new LightDeviceState();
            state.setStatus(deviceState.getStatus());
            state.setBrightness(deviceState.getBrightness());
            state.setColor(deviceState.getColor());
            device.setState(state);
        }
        return device;
    }

    private Device createComparableDoor(Device actualDevice) {
        DoorDevice device = new DoorDevice();
        device.setId(actualDevice.getId());
        device.setName(actualDevice.getName());
        device.setMeta(null);
        device.setRoom(null);
        DoorDeviceState deviceState = (DoorDeviceState)actualDevice.getState();
        if (deviceState != null) {
            DoorDeviceState state = new DoorDeviceState();
            state.setStatus(deviceState.getStatus());
            state.setLock(deviceState.getLock());
            device.setState(state);

        }
        return device;
    }

    private Device createComparableBlind(Device actualDevice) {
        BlindDevice device = new BlindDevice();
        device.setId(actualDevice.getId());
        device.setName(actualDevice.getName());
        device.setMeta(null);
        device.setRoom(null);
        BlindDeviceState deviceState = (BlindDeviceState)actualDevice.getState();
        if (deviceState != null) {
            BlindDeviceState state = new BlindDeviceState();
            state.setStatus(deviceState.getStatus());
            device.setState(state);
            state.setLevel(deviceState.getLevel());
        }
        return device;
    }

    private Device createComparableAc(Device actualDevice) {
        AcDevice device = new AcDevice();
        device.setId(actualDevice.getId());
        device.setName(actualDevice.getName());
        device.setMeta(null);
        device.setRoom(null);
        AcDeviceState deviceState = (AcDeviceState)actualDevice.getState();
        if (deviceState != null) {
            AcDeviceState state = new AcDeviceState();
            state.setStatus(deviceState.getStatus());
            state.setTemperature(deviceState.getTemperature());
            state.setFanSpeed(deviceState.getFanSpeed());
            state.setHorizontalSwing(deviceState.getHorizontalSwing());
            state.setVerticalSwing(deviceState.getVerticalSwing());
            state.setMode(deviceState.getMode());
            device.setState(state);
        }
        return device;
    }

    private Device createComparableTap(Device actualDevice) {
        TapDevice device = new TapDevice();
        device.setId(actualDevice.getId());
        device.setName(actualDevice.getName());
        device.setMeta(null);
        device.setRoom(null);
        TapDeviceState deviceState = (TapDeviceState)actualDevice.getState();
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
                if(actualDevice.getState().getStatus().equals("opened")){
                    message = message + R.string.notif_tap_title + actualDevice.getName() + R.string.notif_tap_opened + "\n";
                }else{
                    message = message + R.string.notif_tap_title + actualDevice.getName() + R.string.notif_tap_closed + "\n";
                }
                // Tell the user
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void checkAcChanges(AcDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            AcDevice actualAcDevice = (AcDevice) actualDevice;
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();

            if (context != null) {
                String message = "";

                if(!actualAcDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualAcDevice.getState().getStatus().equals("on")){
                        message = message + R.string.notif_ac_title + actualDevice.getName() + R.string.notif_ac_turned_on + "\n";
                    }else{
                        message = message + R.string.notif_ac_title + actualDevice.getName() + R.string.notif_ac_turned_off + "\n";
                    }
                }

                if(!actualAcDevice.getState().getTemperature().equals(device.getState().getTemperature())){
                    message = message + R.string.notif_ac_changed_temperature + "\n";
                }
                if(!actualAcDevice.getState().getFanSpeed().equals(device.getState().getFanSpeed())){
                    message = message + R.string.notif_ac_changed_fan_speed + "\n";
                }
                if(!actualAcDevice.getState().getHorizontalSwing().equals(device.getState().getHorizontalSwing())){
                    message = message + R.string.notif_ac_changed_horizontal_swing + "\n";
                }
                if(!actualAcDevice.getState().getVerticalSwing().equals(device.getState().getVerticalSwing())){
                    message = message + R.string.notif_ac_changed_vertical_swing + "\n";
                }

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
                if(!actualDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualDevice.getState().getStatus().equals("on")){
                        message = message + R.string.notif_light_title + actualDevice.getName() + R.string.notif_light_turned_on + "\n";
                    }else{
                        message = message + R.string.notif_light_title + actualDevice.getName() + R.string.notif_light_turned_off + "\n";
                    }
                }

                if( !(((LightDevice)actualDevice).getState().getColor().equals(device.getState().getColor())) ||
                        (((LightDevice)actualDevice).getState().getBrightness().equals(device.getState().getBrightness()))){
                    message = message + R.string.notif_light_title + actualDevice.getName() + R.string.notif_light_color + "\n";
                }
                showNotification(context, typeName, device, message);
            }
        }
    }

    private void checkOvenChanges(OvenDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            OvenDevice actualOvenDevice = (OvenDevice) actualDevice;
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";

                if(!actualOvenDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualOvenDevice.getState().getStatus().equals("on")){
                        message = message + R.string.notif_oven_title + actualDevice.getName() + R.string.notif_oven_turned_on + "\n";
                    }else{
                        message = message + R.string.notif_oven_title + actualDevice.getName() + R.string.notif_oven_turned_off + "\n";
                    }
                }

                if(!actualOvenDevice.getState().getTemperature().equals(device.getState().getTemperature())){
                    message = message + R.string.notif_oven_changed_temperature + "\n";
                }
                if(!actualOvenDevice.getState().getConvection().equals(device.getState().getConvection())){
                    message = message + R.string.notif_oven_changed_convection + "\n";
                }
                if(!actualOvenDevice.getState().getGrill().equals(device.getState().getGrill())){
                    message = message + R.string.notif_oven_changed_grill + "\n";
                }
                if(!actualOvenDevice.getState().getHeat().equals(device.getState().getHeat())){
                    message = message + R.string.notif_oven_changed_heat + "\n";
                }

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
                if(!actualDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualDevice.getState().getStatus().equals("playing")){
                        message = message + R.string.notif_speaker_title + actualDevice.getName() + R.string.notif_speaker_turned_on + "\n";
                    }else{
                        message = message + R.string.notif_speaker_title + actualDevice.getName() + R.string.notif_speaker_turned_off + "\n";
                    }
                }

                if(!((SpeakerDevice)actualDevice).getState().getGenre().equals(device.getState().getGenre())){
                    message = message + R.string.notif_speaker_genre + ((SpeakerDevice)actualDevice).getState().getGenre() + "\n";
                }
                showNotification(context, typeName, device, message);
            }
        };
    }

    private void checkBlindChanges(BlindDevice device) {
        if (!actualDevice.equals(device)) {
            Log.d("DEVICE_CHANGED", actualDevice.toString());
            BlindDevice actualBlindDevice = (BlindDevice) actualDevice;
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                String message = "";

                if(!actualBlindDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualBlindDevice.getState().getStatus().equals("opened") && device.getState().getStatus().equals("closed")){
                        message = message + R.string.notif_blind_title + actualDevice.getName() + R.string.notif_blind_opened + "\n";
                    }else if(actualBlindDevice.getState().getStatus().equals("closed") && device.getState().getStatus().equals("opened")) {
                        message = message + R.string.notif_blind_title + actualDevice.getName() + R.string.notif_blind_closed + "\n";
                    }
                }

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
                if(!actualDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualDevice.getState().getStatus().equals("opened")){
                        message = message + R.string.notif_door_title + actualDevice.getName() + R.string.notif_door_turned_opened + "\n";
                    }else{
                        message = message + R.string.notif_door_title + actualDevice.getName() + R.string.notif_door_turned_closed + "\n";
                    }
                }
                showNotification(context, typeName, device, message);
                if(!((DoorDevice)actualDevice).getState().getLock().equals(device.getState().getLock())){
                    if(((DoorDevice)actualDevice).getState().getLock().equals("unlocked")){
                        message = message + R.string.notif_door_title + actualDevice.getName() + R.string.notif_door_unlocked + "\n";
                    }else{
                        message = message + R.string.notif_door_title + actualDevice.getName() + R.string.notif_door_locked + "\n";
                    }
                }
            }
        };
    }

    private void checkVacuumChanges(VacuumDevice device) {
        if (!actualDevice.equals(device)) {
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, device);
            task.execute();
            Context context = weakContext.get();
            VacuumDevice actualVacuumDevice = (VacuumDevice) actualDevice;
            if (context != null) {
                String message = "";

                if(!actualVacuumDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualVacuumDevice.getState().getStatus().equals("on")){
                        message = message + R.string.notif_vacuum_title + actualVacuumDevice.getName() + R.string.notif_vacuum_turned_on + "\n";
                    }else{
                        message = message + R.string.notif_vacuum_title + actualVacuumDevice.getName() + R.string.notif_vacuum_turned_off + "\n";
                    }
                }

                if(!actualVacuumDevice.getState().getMode().equals(device.getState().getMode())){
                    message = message + R.string.notif_vacuum_title + actualVacuumDevice.getName() + R.string.notif_vacuum_mode + "\n";
                }

                if(!actualVacuumDevice.getState().getLocation().equals(device.getState().getLocation())){
                    message = message + R.string.notif_vacuum_title + actualVacuumDevice.getName() + R.string.notif_vacuum_location + "\n";
                }

                if( (!actualVacuumDevice.getState().getBatteryLevel().equals(device.getState().getBatteryLevel())) &&
                        actualVacuumDevice.getState().getBatteryLevel() <= 6 && device.getState().getBatteryLevel() > 6){
                    message = message + R.string.notif_vacuum_title + actualVacuumDevice.getName() + R.string.notif_vacuum_battery + "\n";
                }


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
        //        // started Activity.
        //        // This ensures that navigating backward from the Activity leads out of
        //        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        // Create the pending intent granting the Operating System to launch activity
        // when notification in action bar is clicked.
        final PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
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