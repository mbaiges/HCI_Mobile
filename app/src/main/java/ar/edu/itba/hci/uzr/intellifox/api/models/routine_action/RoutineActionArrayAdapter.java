package ar.edu.itba.hci.uzr.intellifox.api.models.routine_action;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.RoutineMeta;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.RoutineViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineActionArrayAdapter extends ArrayAdapter<RoutineAction> {

    static HashMap<String, Integer> actionNameMap;
    static Map<String, Integer> typeInfo;

    public RoutineActionArrayAdapter(Activity context, RoutineAction[] objects) {
        super(context, R.layout.routine_action_card_item, objects);

        if (actionNameMap == null) {
            actionNameMap = new HashMap<String, Integer>() {{
               put("setTemperature", R.string.routine_action_ac_set_temperature);

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
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        RoutineActionViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.routine_action_card_item, parent, false);
            holder = new RoutineActionViewHolder();
            holder.iconView = convertView.findViewById(R.id.icon);
            holder.deviceNameTextView = convertView.findViewById(R.id.deviceName);
            holder.actionNameTextView = convertView.findViewById(R.id.actionName);
            holder.paramsTextView = convertView.findViewById(R.id.params);
            convertView.setTag(holder);
        } else {
            holder = (RoutineActionViewHolder) convertView.getTag();
        }

        RoutineAction routineAction = getItem(position);
        if (routineAction != null) {
            RoutineActionDevice device = routineAction.getDevice();
            if (device != null) {
                String deviceId = device.getId();
                updateDeviceInfo(deviceId, convertView);
            }
            if (holder.actionNameTextView != null) {
                String actionName = holder.actionNameTextView.getText().toString();
                Integer actionNameTextResource = actionNameMap.get(actionName);
                if (actionNameTextResource != null) {
                    holder.actionNameTextView.setText(actionNameTextResource);
                }
            }
            if (holder.paramsTextView != null) {
                String[] params = routineAction.getParams();
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
                        stringBuilder.append("]");
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
                            RoutineActionViewHolder holder = (RoutineActionViewHolder) convertView.getTag();
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

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }
}
