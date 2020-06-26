package ar.edu.itba.hci.uzr.intellifox.api.models.device.log;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceLogRecordArrayAdapter extends ArrayAdapter<DeviceLogRecord> {

    static private HashMap<String, Integer> actionNameMap;
    static private Map<String, Integer> typeInfo;

    static private SimpleDateFormat formatter, customFormatter;

    @SuppressLint("SimpleDateFormat")
    public DeviceLogRecordArrayAdapter(Activity context, DeviceLogRecord[] objects) {
        super(context, R.layout.device_log_record_item, objects);

        if (actionNameMap == null) {
            actionNameMap = new HashMap<String, Integer>() {{
                put("speaker_setVolume", R.string.routine_action_speaker_set_volume);
                put("speaker_play", R.string.routine_action_speaker_play);
                put("speaker_stop", R.string.routine_action_speaker_stop);
                put("speaker_pause", R.string.routine_action_speaker_pause);
                put("speaker_resume", R.string.routine_action_speaker_resume);
                put("speaker_nextSong", R.string.routine_action_speaker_next_song);
                put("speaker_previousSong", R.string.routine_action_speaker_previous_song);
                put("speaker_setGenre", R.string.routine_action_speaker_set_genre);
                put("speaker_getPlaylist", R.string.routine_action_speaker_get_playlist);

                put("faucet_open", R.string.routine_action_faucet_open);
                put("faucet_close", R.string.routine_action_faucet_close);
                put("faucet_dispense", R.string.routine_action_faucet_dispense);

                put("blinds_open", R.string.routine_action_blinds_open);
                put("blinds_close", R.string.routine_action_blinds_close);
                put("blinds_setLevel", R.string.routine_action_blinds_set_level);

                put("lamp_turnOn", R.string.routine_action_lamp_turn_on);
                put("lamp_turnOff", R.string.routine_action_lamp_turn_off);
                put("lamp_setColor", R.string.routine_action_lamp_set_color);
                put("lamp_setBrightness", R.string.routine_action_lamp_set_brightness);

                put("oven_turnOn", R.string.routine_action_oven_turn_on);
                put("oven_turnOff", R.string.routine_action_oven_turn_off);
                put("oven_setTemperature", R.string.routine_action_oven_set_temperature);
                put("oven_setHeat", R.string.routine_action_oven_set_heat);
                put("oven_setGrill", R.string.routine_action_oven_set_grill);
                put("oven_setConvection", R.string.routine_action_oven_set_convection);

                put("ac_turnOn", R.string.routine_action_ac_turn_on);
                put("ac_turnOff", R.string.routine_action_ac_turn_off);
                put("ac_setTemperature", R.string.routine_action_ac_set_temperature);
                put("ac_setMode", R.string.routine_action_ac_set_mode);
                put("ac_setVerticalSwing", R.string.routine_action_ac_set_vertical_swing);
                put("ac_setHorizontalSwing", R.string.routine_action_ac_set_horizontal_swing);
                put("ac_setFanSpeed", R.string.routine_action_ac_set_fan_speed);

                put("door_open", R.string.routine_action_door_open);
                put("door_close", R.string.routine_action_door_close);
                put("door_lock", R.string.routine_action_door_lock);
                put("door_unlock", R.string.routine_action_door_unlock);

                put("vacuum_start", R.string.routine_action_vacuum_start);
                put("vacuum_pause", R.string.routine_action_vacuum_pause);
                put("vacuum_dock", R.string.routine_action_vacuum_dock);
                put("vacuum_setMode", R.string.routine_action_vacuum_set_mode);
                put("vacuum_setLocation", R.string.routine_action_vacuum_set_location);
            }};
        }

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

        if (formatter == null) {
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        }

        if (customFormatter == null) {
            customFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        }
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        DeviceLogRecordViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_log_record_item, parent, false);
            holder = new DeviceLogRecordViewHolder();
            holder.iconView = convertView.findViewById(R.id.icon);
            holder.timestampTextView = convertView.findViewById(R.id.timestamp);
            holder.deviceNameTextView = convertView.findViewById(R.id.txtTitle);
            holder.actionNameTextView = convertView.findViewById(R.id.txtAlbum);
            holder.paramsTextView = convertView.findViewById(R.id.txtAlbumContent);
            convertView.setTag(holder);
        } else {
            holder = (DeviceLogRecordViewHolder) convertView.getTag();
        }

        DeviceLogRecord deviceLogRecord = getItem(position);
        if (deviceLogRecord != null) {
            String deviceId = deviceLogRecord.getDeviceId();
            if (deviceId != null) {
                updateDeviceInfo(deviceId, convertView);
            }
            holder.actionName = deviceLogRecord.getAction();
            if (holder.timestampTextView != null) {
                Date timestamp = null;
                try {
                    timestamp = formatter.parse(deviceLogRecord.getTimestamp());
                } catch (ParseException e) {
                    Log.d("ERROR", "Converting date format");
                }
                if (timestamp != null) {
                    holder.timestampTextView.setText(customFormatter.format(timestamp));
                }
            }
            if (holder.paramsTextView != null) {
                String[] params = deviceLogRecord.getParams();
                if (params != null) {
                    StringBuilder stringBuilder = new StringBuilder("");
                    if (params.length != 0) {
                        stringBuilder.append("[ ");
                        for(int i = 0; i < params.length; i++) {
                            stringBuilder.append(params[i]);
                            if (i < params.length - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        stringBuilder.append(" ]");
                    }
                    final String paramsText = stringBuilder.toString();
                    holder.paramsTextView.setText(paramsText);
                }
            }
        }

        return convertView;
    }

    private void updateDeviceInfo(String deviceId, View convertView) {
        if (deviceId != null) {
            ApiClient.getInstance().getDevice(deviceId, new Callback<Result<Device>>() {
                @Override
                public void onResponse(@NonNull Call<Result<Device>> call, @NonNull Response<Result<Device>> response) {
                    if (response.isSuccessful()) {
                        Result<Device> result = response.body();
                        if (result != null) {
                            Device device = result.getResult();
                            DeviceLogRecordViewHolder holder = (DeviceLogRecordViewHolder) convertView.getTag();
                            if (device != null && holder != null) {
                                String deviceName = device.getName();
                                if (deviceName != null && holder.deviceNameTextView != null) {
                                    holder.deviceNameTextView.setText(deviceName);
                                }
                                DeviceType dt = device.getType();
                                if (dt != null) {
                                    String typeName = dt.getName();
                                    if (typeName != null) {
                                        Integer iconRef = typeInfo.get(typeName);
                                        if (iconRef != null && holder.iconView != null) {
                                            holder.iconView.setImageResource(iconRef);
                                            holder.iconView.setColorFilter(ContextCompat.getColor(convertView.getContext(), R.color.icon));
                                        }

                                        if (holder.actionNameTextView != null) {
                                            if (holder.actionName != null) {
                                                String actionName = holder.actionName;
                                                String resName = typeName + "_" + actionName;
                                                Log.d("RESOURCE_NAME", resName);
                                                Integer actionNameTextResource = actionNameMap.get(resName);
                                                if (actionNameTextResource != null) {
                                                    holder.actionNameTextView.setText(actionNameTextResource);
                                                }
                                            }
                                        }
                                    }
                                }
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

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }
}
