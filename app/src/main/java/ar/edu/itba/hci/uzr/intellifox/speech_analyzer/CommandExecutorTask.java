package ar.edu.itba.hci.uzr.intellifox.speech_analyzer;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac.SetFanSpeedAcCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac.SetHorizontalSwingAcCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac.SetModeAcCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac.SetTemperatureAcCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac.SetVerticalSwingAcCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac.TurnOffAcCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac.TurnOnAcCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.blind.CloseBlindCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.blind.OpenBlindCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.door.CloseDoorCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.door.LockDoorCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.door.OpenDoorCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.door.UnlockDoorCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.light.SetBrightnessLightCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.light.SetColorLightCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.light.TurnOffLightCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.light.TurnOnLightCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven.SetConvectionOvenCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven.SetGrillOvenCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven.SetHeatOvenCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven.SetTemperatureOvenCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven.TurnOffOvenCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven.TurnOnOvenCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker.GetPlaylistSpeakerCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker.NextSongSpeakerCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker.PreviousSongSpeakerCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker.SetGenreSpeakerCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker.SetVolumeSpeakerCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker.StopSpeakerCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker.PlaySpeakerCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.tap.CloseTapCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.tap.DispenseTapCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.tap.OpenTapCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum.DockVacuumCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum.PauseVacuumCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum.SetLocationVacuumCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum.SetModeVacuumCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum.StartVacuumCommand;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import ar.edu.itba.hci.uzr.intellifox.path_highlighter.PathHighlighter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommandExecutorTask extends AsyncTask<Void, Void, Void> {

    static final String COMMAND_TASK_TAG = "Command_Task";
    static private WeakReference<AppCompatActivity> currentActivity;

    static private HashMap<String, HashMap<Integer, Class>> commandsMap;

    private String command;

    public CommandExecutorTask(String command) {
        String s = Normalizer.normalize(command, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        this.command = s.toLowerCase();
    }

    public static void associateActivity(AppCompatActivity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (currentActivity != null) {
            AppCompatActivity activity = currentActivity.get();
            if (activity != null) {
                if (command.equals(activity.getResources().getString(R.string.speech_analyzer_turn_on_path_highlighter).toLowerCase())) {
                    highlightMyWay(true);
                }
                else if (command.equals(activity.getResources().getString(R.string.speech_analyzer_turn_off_path_highlighter).toLowerCase())) {
                    highlightMyWay(false);
                }
                else {
                    parseExpression(activity.getResources(), command);
                }
            }
        }

        return null;
    }


    private void highlightMyWay(boolean intentSwitch) {
        Log.d("HIGHLIGHTER", "Entered");
        PathHighlighter.getInstance().highlightPath(intentSwitch);
    }


    // Execute Routine "{deviceName}"

    // Execute "{actionName}" to "{param}" over "deviceName"

    // Execute "{actionName} over "deviceName"

    private void parseExpression(Resources res, String command) {
        boolean firstWordAnalyzed = false, reachedTo = false, reachedOver = false, commandEnded = false;
        String[] words = command.split(" ");

        if (words.length > 0 && words[0].equals(res.getString(R.string.speech_analyzer_execute).toLowerCase())) {
            String[] nextPath = new String[words.length - 2];
            for (int i = 0; i < nextPath.length; i++) {
                nextPath[i] = words[i+2];
            }

            if (nextPath.length > 0) {
                if (words[1].equals(res.getString(R.string.speech_analyzer_routine).toLowerCase())) {
                    StringBuilder nameBuilder = new StringBuilder("");
                    for(int j = 0; j < nextPath.length; j++) {
                        nameBuilder.append(nextPath[j]);
                        if (j < nextPath.length - 1) {
                            nameBuilder.append(" ");
                        }
                    }
                    findAndExecuteRoutineNamed(nameBuilder.toString());
                }
                else {
                    StringBuilder[] strings = { new StringBuilder(""),new StringBuilder(""),new StringBuilder("")};
                    int stringToAppend = 0;
                    for(int j = 1; j < words.length ; j++){
                        if(words[j].equals(res.getString(R.string.speech_analyzer_to).toLowerCase())){
                            stringToAppend = 1;
                        }else if(words[j].equals(res.getString(R.string.speech_analyzer_over).toLowerCase())){
                            stringToAppend = 2;
                        }else{
                            strings[stringToAppend].append(words[j]);
                            strings[stringToAppend].append(" ");
                        }
                    }
                    if(strings[0].length() > 0)
                        strings[0].deleteCharAt(strings[0].length()-1);
                    if(strings[1].length() > 0)
                        strings[1].deleteCharAt(strings[1].length()-1);
                    if(strings[2].length() > 0)
                        strings[2].deleteCharAt(strings[2].length()-1);
                    Log.d(COMMAND_TASK_TAG, "." + strings[0].toString() + ".");
                    Log.d(COMMAND_TASK_TAG, "." + strings[1].toString() + ".");
                    Log.d(COMMAND_TASK_TAG, "." + strings[2].toString() + ".");
                    String actionName = strings[0].toString();
                    String[] args = null;
                    if (strings[1].length() > 0) {
                        args = strings[1].toString().split(" ");
                    }
                    String deviceName = strings[2].toString();

                    findAndExecuteDeviceNamed(deviceName, actionName, args);
                }
            }
        }
    }

    private void findAndExecuteDeviceNamed(String deviceName, String actionName, String[] args) {
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Device>> result = response.body();
                    if (result != null) {
                        List<Device> devicesList = result.getResult();
                        if (devicesList != null) {
                            for (Device d: devicesList) {
                                String name = d.getName();
                                if (name != null && name.toLowerCase().equals(deviceName)) {
                                    findAndExecuteOnDeviceActionNamed(d, actionName, args);
                                }
                            }
                        }
                    }
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

            }
        });
    }

    private void findAndExecuteOnDeviceActionNamed(Device d, String actionName, String[] params) {
        DeviceType dt = d.getType();
        if (dt != null) {
            String typeName = dt.getName();
            if (typeName != null) {
                DeviceCommand c = getCommand(typeName, d.getId(), actionName, params);
                if (c != null) {
                    Log.d(COMMAND_TASK_TAG, "I want to make a move onto device id: " + d.getId());
                    c.execute(new Callback<Result<Object>>() {
                        @Override
                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                            if (response.isSuccessful()) {
                                if (currentActivity != null) {
                                    AppCompatActivity activity = currentActivity.get();
                                    if (activity != null) {
                                        View parentLayout = activity.findViewById(android.R.id.content);
                                        Snackbar snackbar = Snackbar.make(parentLayout, R.string.snackbar_device_action_executed_succesfully, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.primary2));
                                        snackbar.show();
                                    }
                                }
                            }
                            else {
                                handleError(response);
                            }
                        }

                        @Override
                        public void onFailure(Call<Result<Object>> call, Throwable t) {

                        }
                    });
                }
            }
        }
    }

    private DeviceCommand getCommand(String typeName, String deviceId, String actionName, String[] params) {
        if (commandsMap == null) {
            commandsMap = new HashMap<String, HashMap<Integer, Class>>() {{
                put("door", new HashMap<Integer, Class>() {{
                    put(R.string.speech_analyzer_door_action_open, OpenDoorCommand.class);
                    put(R.string.speech_analyzer_door_action_close, CloseDoorCommand.class);
                    put(R.string.speech_analyzer_door_action_lock, LockDoorCommand.class);
                    put(R.string.speech_analyzer_door_action_unlock, UnlockDoorCommand.class);
                }});
                put("blinds", new HashMap<Integer, Class>() {{
                    put(R.string.speech_analyzer_blind_action_open, OpenBlindCommand.class);
                    put(R.string.speech_analyzer_blind_action_close, CloseBlindCommand.class);
                }});
                put("ac", new HashMap<Integer, Class>() {{
                    put(R.string.speech_analyzer_ac_action_turn_on, TurnOnAcCommand.class);
                    put(R.string.speech_analyzer_ac_action_turn_off, TurnOffAcCommand.class);
                    put(R.string.speech_analyzer_ac_action_set_mode, SetModeAcCommand.class);
                    put(R.string.speech_analyzer_ac_action_set_temperature, SetTemperatureAcCommand.class);
                    put(R.string.speech_analyzer_ac_action_set_vertical_swing, SetVerticalSwingAcCommand.class);
                    put(R.string.speech_analyzer_ac_action_set_horizontal_swing, SetHorizontalSwingAcCommand.class);
                    put(R.string.speech_analyzer_ac_action_set_fan_speed, SetFanSpeedAcCommand.class);
                }});
                put("faucet", new HashMap<Integer, Class>() {{
                    put(R.string.speech_analyzer_tap_action_open, OpenTapCommand.class);
                    put(R.string.speech_analyzer_tap_action_close, CloseTapCommand.class);
                    put(R.string.speech_analyzer_tap_action_dispense, DispenseTapCommand.class);
                }});
                put("oven", new HashMap<Integer, Class>() {{
                    put(R.string.speech_analyzer_oven_action_turn_on, TurnOnOvenCommand.class);
                    put(R.string.speech_analyzer_oven_action_turn_off, TurnOffOvenCommand.class);
                    put(R.string.speech_analyzer_oven_action_set_heat, SetHeatOvenCommand.class);
                    put(R.string.speech_analyzer_oven_action_set_grill, SetGrillOvenCommand.class);
                    put(R.string.speech_analyzer_oven_action_set_convection, SetConvectionOvenCommand.class);
                    put(R.string.speech_analyzer_oven_action_set_temperature, SetTemperatureOvenCommand.class);
                }});
                put("speaker", new HashMap<Integer, Class>() {{
                    put(R.string.speech_analyzer_speaker_action_get_playlist, GetPlaylistSpeakerCommand.class);
                    put(R.string.speech_analyzer_speaker_action_next_song, NextSongSpeakerCommand.class);
                    put(R.string.speech_analyzer_speaker_action_previous_song, PreviousSongSpeakerCommand.class);
                    put(R.string.speech_analyzer_speaker_action_set_genre, SetGenreSpeakerCommand.class);
                    put(R.string.speech_analyzer_speaker_action_set_volume, SetVolumeSpeakerCommand.class);
                    put(R.string.speech_analyzer_speaker_action_stop, StopSpeakerCommand.class);
                    put(R.string.speech_analyzer_speaker_action_play, PlaySpeakerCommand.class);

                }});
                put("vacuum", new HashMap<Integer, Class>() {{
                    put(R.string.speech_analyzer_vacuum_action_dock, DockVacuumCommand.class);
                    put(R.string.speech_analyzer_vacuum_action_pause, PauseVacuumCommand.class);
                    put(R.string.speech_analyzer_vacuum_action_set_location, SetLocationVacuumCommand.class);
                    put(R.string.speech_analyzer_vacuum_action_set_mode, SetModeVacuumCommand.class);
                    put(R.string.speech_analyzer_vacuum_action_start, StartVacuumCommand.class);
                }});
                put("lamp", new HashMap<Integer, Class>() {{
                    put(R.string.speech_analyzer_light_action_set_brightness, SetBrightnessLightCommand.class);
                    put(R.string.speech_analyzer_light_action_set_color, SetColorLightCommand.class);
                    put(R.string.speech_analyzer_light_action_turn_off, TurnOffLightCommand.class);
                    put(R.string.speech_analyzer_light_action_turn_on, TurnOnLightCommand.class);
                }});
            }};
        }

        HashMap<Integer, Class> actionMap = commandsMap.get(typeName);
        if (actionMap != null) {
            if (currentActivity != null) {
                AppCompatActivity activity = currentActivity.get();
                if (activity != null) {
                    final Resources resources = activity.getResources();
                    for(Map.Entry<Integer, Class> e :actionMap.entrySet() ) {
                        Integer res = e.getKey();
                        if (res != null) {
                            String action = resources.getString(res);
                            if (actionName.equals(action.toLowerCase())) {
                                Class commandClass = e.getValue();
                                if (commandClass != null) {
                                    try {
                                       return (DeviceCommand) commandClass.getDeclaredConstructor(String.class, String[].class).newInstance(deviceId, params);
                                    } catch (Exception ex) {
                                        Log.e(COMMAND_TASK_TAG, "An exception creating commandClass ocurred");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    void findAndExecuteRoutineNamed(String routineName) {
        ApiClient.getInstance().getRoutines(new Callback<Result<List<Routine>>>() {
            @Override
            public void onResponse(Call<Result<List<Routine>>> call, Response<Result<List<Routine>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Routine>> result = response.body();
                    if (result != null) {
                        List<Routine> routinesList = result.getResult();
                        if (routinesList != null) {
                            for (Routine r: routinesList) {
                                String name = r.getName();
                                if (name != null && name.toLowerCase().equals(routineName)) {
                                    executeRoutine(r.getId());
                                }
                            }
                        }
                    }
                }
                else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<List<Routine>>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void executeRoutine(String id){
        if (id != null) {
            Log.d(COMMAND_TASK_TAG, "Rutina ID: " + id);
            ApiClient.getInstance().executeRoutine(id, new Callback<Result<Object>>() {
                @Override
                public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                    if (response.isSuccessful()) {
                        if (currentActivity != null) {
                            AppCompatActivity activity = currentActivity.get();
                            if (activity != null) {
                                View parentLayout = activity.findViewById(android.R.id.content);
                                Snackbar snackbar = Snackbar.make(parentLayout, R.string.snackbar_routine_executed_success, Snackbar.LENGTH_SHORT);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.primary2));
                                snackbar.show();
                            }
                        }
                    }
                    else {
                        handleError(response);
                    }
                }

                @Override
                public void onFailure(Call<Result<Object>> call, Throwable t) {
                    handleUnexpectedError(t);
                }
            });
        }else{
            Log.v("EXECUTE", "NULL ID");
        }
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String desc = error.getDescription();
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
}
