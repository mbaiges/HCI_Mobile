package ar.edu.itba.hci.uzr.intellifox.database.tasks;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.core.app.NotificationCompat;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.settings.DatabaseSetting;
import ar.edu.itba.hci.uzr.intellifox.MainActivity;
import ar.edu.itba.hci.uzr.intellifox.R;
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

public class DatabaseDeviceCheckerAsyncTask extends AsyncTask<Void, Void, Device> {

    private static final String CHANNEL_ID = "NOTIFICATIONS";
    private static int notification_id = 1;

    static AppDatabase db;
    static private Map<String, Integer> typeInfo;

    private WeakReference<Context> weakContext;
    private String typeName, deviceID;
    private Device actualDevice;

    public DatabaseDeviceCheckerAsyncTask(Context context, String typeName, String deviceID, Device actualDevice) {

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
            db = DatabaseSetting.getInstance();
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
        /*
        Log.d("BD_GET_DEVICE", device.toString());
        Log.d("API_GET_DEVICE", actualDevice.toString());
        */
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
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, actualDevice);
            task.execute();
            Context context = weakContext.get();
            if (context != null) {
                StringBuilder messageBuilder = new StringBuilder("");
                if(actualDevice.getState().getStatus().equals("opened")){
                    messageBuilder.append(context.getResources().getString(R.string.notif_tap_opened));
                }else{
                    messageBuilder.append(context.getResources().getString(R.string.notif_tap_closed));
                }
                messageBuilder.append(" ");
                // Tell the user
                showNotification(context, typeName, device, messageBuilder.toString());
            }
        };
    }

    private void checkAcChanges(AcDevice device) {
        if (!actualDevice.equals(device)) {
            AcDevice actualAcDevice = (AcDevice) actualDevice;
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, actualAcDevice);
            task.execute();
            Context context = weakContext.get();
            boolean first = true, stateChanged = false;

            if (context != null) {
                StringBuilder messageBuilder = new StringBuilder("");

                if(!actualAcDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualAcDevice.getState().getStatus().equals("on")){
                        messageBuilder.append(context.getResources().getString(R.string.notif_ac_turned_on));
                    }else{
                        messageBuilder.append(context.getResources().getString(R.string.notif_ac_turned_off));
                    }
                    messageBuilder.append(" ");
                    stateChanged = true;
                }
                if(!actualAcDevice.getState().getMode().equals(device.getState().getMode())) {
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                        messageBuilder.append(" ");
                        first = false;
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_ac_changed_mode));
                }
                if(!actualAcDevice.getState().getTemperature().equals(device.getState().getTemperature())){
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                        messageBuilder.append(" ");
                        first = false;
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_ac_changed_temperature));
                }
                if(!actualAcDevice.getState().getFanSpeed().equals(device.getState().getFanSpeed())){
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                        messageBuilder.append(" ");
                        first = false;
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_ac_changed_fan_speed));
                }
                if(!actualAcDevice.getState().getHorizontalSwing().equals(device.getState().getHorizontalSwing())){
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                        messageBuilder.append(" ");
                        first = false;
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_ac_changed_horizontal_swing));
                }
                if(!actualAcDevice.getState().getVerticalSwing().equals(device.getState().getVerticalSwing())){
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_ac_changed_vertical_swing));
                }
                if (!first) {
                    messageBuilder.append(".");
                }
                showNotification(context, typeName, device, messageBuilder.toString());
            }

        };
    }

    private void checkLightChanges(LightDevice device) {
        if (!actualDevice.equals(device)) {
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, actualDevice);
            task.execute();
            Context context = weakContext.get();
            boolean first = true, stateChanged = false;

            if (context != null) {
                StringBuilder messageBuilder = new StringBuilder("");
                if(!actualDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualDevice.getState().getStatus().equals("on")){
                        messageBuilder.append(context.getResources().getString(R.string.notif_light_turned_on));
                    }else{
                        messageBuilder.append(context.getResources().getString(R.string.notif_light_turned_off));
                    }
                    messageBuilder.append(" ");
                    stateChanged = true;
                }

                if( !(((LightDevice)actualDevice).getState().getColor().equals(device.getState().getColor())) ||
                        (((LightDevice)actualDevice).getState().getBrightness().equals(device.getState().getBrightness()))){
                    messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                    messageBuilder.append(" ");
                    messageBuilder.append(context.getResources().getString(R.string.notif_light_changed_color));
                    first = false;
                }
                if (!first) {
                    messageBuilder.append(".");
                }
                showNotification(context, typeName, device, messageBuilder.toString());
            }
        }
    }

    private void checkOvenChanges(OvenDevice device) {
        if (!actualDevice.equals(device)) {
            OvenDevice actualOvenDevice = (OvenDevice) actualDevice;
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, actualDevice);
            task.execute();
            Context context = weakContext.get();
            boolean first = true, stateChanged = false;

            if (context != null) {
                StringBuilder messageBuilder = new StringBuilder("");

                if(!actualOvenDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualOvenDevice.getState().getStatus().equals("on")){
                        messageBuilder.append(context.getResources().getString(R.string.notif_oven_turned_on));
                    }else{
                        messageBuilder.append(context.getResources().getString(R.string.notif_oven_turned_off));
                    }
                    messageBuilder.append(" ");
                    stateChanged = true;
                }

                if(!actualOvenDevice.getState().getTemperature().equals(device.getState().getTemperature())){
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                        messageBuilder.append(" ");
                        first = false;
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_oven_changed_temperature));
                }
                if(!actualOvenDevice.getState().getConvection().equals(device.getState().getConvection())){
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                        messageBuilder.append(" ");
                        first = false;
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_oven_changed_convection));
                }
                if(!actualOvenDevice.getState().getGrill().equals(device.getState().getGrill())){
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                        messageBuilder.append(" ");
                        first = false;
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_oven_changed_grill));
                }
                if(!actualOvenDevice.getState().getHeat().equals(device.getState().getHeat())){
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                        messageBuilder.append(" ");
                        first = false;
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_oven_changed_heat));
                }
                if (!first) {
                    messageBuilder.append(".");
                }
                showNotification(context, typeName, device, messageBuilder.toString());
            }

        };
    }

    private void checkSpeakerChanges(SpeakerDevice device) {
        if (!actualDevice.equals(device)) {
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, actualDevice);
            task.execute();
            Context context = weakContext.get();
            boolean first = true, stateChanged = false;

            if (context != null) {
                StringBuilder messageBuilder = new StringBuilder("");
                if(!actualDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualDevice.getState().getStatus().equals("playing")){
                        messageBuilder.append(context.getResources().getString(R.string.notif_speaker_turned_on));
                    }else{
                        messageBuilder.append(context.getResources().getString(R.string.notif_speaker_turned_off));
                    }
                    messageBuilder.append(" ");
                    stateChanged = true;
                }

                if(!((SpeakerDevice)actualDevice).getState().getGenre().equals(device.getState().getGenre())){
                    messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                    messageBuilder.append(" ");
                    messageBuilder.append(context.getResources().getString(R.string.notif_speaker_changed_genre));
                    messageBuilder.append(" ");
                    messageBuilder.append(((SpeakerDevice)actualDevice).getState().getGenre());
                    first = false;
                }
                if (!first) {
                    messageBuilder.append(".");
                }
                showNotification(context, typeName, device, messageBuilder.toString());
            }
        };
    }

    private void checkBlindChanges(BlindDevice device) {
        if (!actualDevice.equals(device)) {
            BlindDevice actualBlindDevice = (BlindDevice) actualDevice;
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, actualDevice);
            task.execute();
            Context context = weakContext.get();

            if (context != null) {
                StringBuilder messageBuilder = new StringBuilder("");

                if(!actualBlindDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualBlindDevice.getState().getStatus().equals("opened") && device.getState().getStatus().equals("closed")){
                        messageBuilder.append(context.getResources().getString(R.string.notif_blind_opened));
                    }else if(actualBlindDevice.getState().getStatus().equals("closed")&& device.getState().getStatus().equals("opened")) {
                        messageBuilder.append(context.getResources().getString(R.string.notif_blind_closed));
                    }
                    messageBuilder.append(" ");
                }
                showNotification(context, typeName, device, messageBuilder.toString());
            }
        };
    }

    private void checkDoorChanges(DoorDevice device) {
        if (!actualDevice.equals(device)) {
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, actualDevice);
            task.execute();
            Context context = weakContext.get();
            boolean stateChanged = false;
            boolean first = true;

            if (context != null) {
                StringBuilder messageBuilder = new StringBuilder("");
                if(!actualDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualDevice.getState().getStatus().equals("opened")){
                        messageBuilder.append(context.getResources().getString(R.string.notif_door_turned_opened));
                    }else{
                        messageBuilder.append(context.getResources().getString(R.string.notif_door_turned_closed));
                    }
                    messageBuilder.append(" ");
                    stateChanged = true;
                }

                if(!((DoorDevice)actualDevice).getState().getLock().equals(device.getState().getLock())){
                    messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                    messageBuilder.append(" ");
                    if(((DoorDevice)actualDevice).getState().getLock().equals("unlocked")){
                        messageBuilder.append(context.getResources().getString(R.string.notif_door_unlocked));
                    }else{
                        messageBuilder.append(context.getResources().getString(R.string.notif_door_locked));
                    }
                    messageBuilder.append(" ");
                    first = false;
                }
                if (!first) {
                    messageBuilder.append(".");
                }
                showNotification(context, typeName, device, messageBuilder.toString());
            }
        };
    }

    private void checkVacuumChanges(VacuumDevice device) {
        if (!actualDevice.equals(device)) {
            DatabaseUpdateDeviceAsyncTask task = new DatabaseUpdateDeviceAsyncTask(typeName, actualDevice);
            task.execute();
            Context context = weakContext.get();
            VacuumDevice actualVacuumDevice = (VacuumDevice) actualDevice;
            boolean first = true, stateChanged = false;

            if (context != null) {
                StringBuilder messageBuilder = new StringBuilder("");

                if(!actualVacuumDevice.getState().getStatus().equals(device.getState().getStatus())){
                    if(actualVacuumDevice.getState().getStatus().equals("on")){
                        messageBuilder.append(context.getResources().getString(R.string.notif_vacuum_turned_on));
                    }else{
                        messageBuilder.append(context.getResources().getString(R.string.notif_vacuum_turned_off));
                    }
                    messageBuilder.append(" ");
                    stateChanged = true;
                }

                if(!actualVacuumDevice.getState().getMode().equals(device.getState().getMode())){
                    if(first){
                        messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                        messageBuilder.append(" ");
                        first = false;
                    }else{
                        messageBuilder.append(" - ");
                    }
                    messageBuilder.append(context.getResources().getString(R.string.notif_vacuum_changed_mode));
                }


                if( !actualVacuumDevice.getState().getBatteryLevel().equals(device.getState().getBatteryLevel()) ) {
                    if (actualVacuumDevice.getState().getBatteryLevel() <= 6 && device.getState().getBatteryLevel() > 6) {
                        if (first) {
                            messageBuilder.append(stateChanged?context.getResources().getString(R.string.notif_additional_changes):context.getResources().getString(R.string.notif_changes));
                            messageBuilder.append(" ");
                            first = false;
                        }
                        else{
                            messageBuilder.append(" - ");
                        }
                        messageBuilder.append(context.getResources().getString(R.string.notif_vacuum_battery));
                    }
                    else if (first && !stateChanged) {
                        return;
                    }
                }
                if (!first) {
                    messageBuilder.append(".");
                }
                showNotification(context, typeName, device, messageBuilder.toString());
            }
        };
    }



    private void showNotification(Context context, String deviceType, Device device, String message) {
        // Create the intent to start Activity when notification in action bar is
        // clicked.

        String title = device.getName();
        Integer iconRef = getIconRef(typeName);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_circle);;

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

        Bundle args = new Bundle();
        args.putString(MainActivity.MESSAGE_ID, deviceType + "," + device.getId());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(icon)
                .setSmallIcon(getIconRef(typeName))
                .setContentIntent(contentIntent)
                .addExtras(args);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
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