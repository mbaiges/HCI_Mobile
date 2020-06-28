package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.settings.SharedPreferencesSetting;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceMeta;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.wrappers.BelledDevices;
import ar.edu.itba.hci.uzr.intellifox.wrappers.TypeAndDeviceId;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DeviceObserver implements Observer<Device<? extends DeviceState>> {

    private final static Integer DEFAULT_ON_DEVICE_COLOR = R.color.text;
    private final static Integer DEFAULT_OFF_DEVICE_COLOR = R.color.background2;

    private final static Integer FAVOURITE_ICON = R.drawable.ic_heart_filled;
    private final static Integer NON_FAVOURITE_ICON = R.drawable.ic_heart_outline;

    private final static Integer BELLED_ICON = R.drawable.ic_bell;
    private final static Integer NON_BELLED_ICON = R.drawable.ic_bell_off;

    private final static String BELLED_DEVICES = "belled_devices";

    private static HashMap<String, Pair<Integer, Integer>> turnMessages;

    static SharedPreferences sharedPreferences;

    protected View contextView;
    protected DeviceViewHolder holder;

    public DeviceObserver(View contextView) {
        this.contextView = contextView;

        if (sharedPreferences == null) {
            sharedPreferences = SharedPreferencesSetting.getInstance();
        }

        if (turnMessages == null) {
            turnMessages = new HashMap<String, Pair<Integer, Integer>>() {{
                put("ac", new Pair<>(R.string.notif_ac_turned_off, R.string.notif_ac_turned_on));
                put("lamp", new Pair<>(R.string.notif_light_turned_off, R.string.notif_light_turned_on));
                put("oven", new Pair<>(R.string.notif_oven_turned_off, R.string.notif_oven_turned_on));
                put("vacuum", new Pair<>(R.string.notif_vacuum_turned_off, R.string.notif_vacuum_turned_on));
                put("speaker", new Pair<>(R.string.notif_speaker_turned_off, R.string.notif_speaker_turned_on));
                put("blinds", new Pair<>(R.string.notif_blind_closed, R.string.notif_blind_opened));
                put("faucet", new Pair<>(R.string.notif_tap_closed, R.string.notif_tap_opened));
                put("door", new Pair<>(R.string.notif_door_turned_closed, R.string.notif_door_turned_opened));
            }};
        }

        createHolder();
        findElements();
        attachFunctions();
    }

    protected void createHolder() {
        this.holder = new DeviceViewHolder();
    }

    @Override
    public void onChanged(Device<? extends DeviceState> newDevice) {
        Device<? extends DeviceState> oldDevice = holder.device;

        if (oldDevice == null && newDevice != null)
            init(newDevice);

        if (newDevice != null) {
            deviceChanged(newDevice);
            holder.device = newDevice;
        }
    }

    protected void deviceChanged(Device<? extends DeviceState> newDevice) {
        setUI(newDevice.getState());
    }

    protected void findElements() {
        holder.bell = contextView.findViewById(R.id.bell);
        holder.icon = contextView.findViewById(R.id.icon);
        holder.description = contextView.findViewById(R.id.desc);
        holder.favourite = contextView.findViewById(R.id.favourite);
        holder.onSwitch = contextView.findViewById(R.id.switch1);
    }

    protected void init(Device<? extends DeviceState> device) {
        DeviceState state = device.getState();

        if (state != null) {
            setFavourite(device);
            setBell(device);
            setUI(state);
        }
    }

    private void setBell(Device<? extends DeviceState> device) {
        if (device != null && holder.bell != null) {
            final Gson gson = new Gson();
            String json = sharedPreferences.getString(BELLED_DEVICES, "");
            if (!json.equals("")) {
                BelledDevices belledDevices = gson.fromJson(json, BelledDevices.class);
                if (belledDevices != null) {
                    HashSet<TypeAndDeviceId> tadis = belledDevices.getBelledDevices();
                    if (tadis != null) {
                        DeviceType dt = device.getType();
                        if (dt != null) {
                            String typeName = dt.getName();
                            if (typeName != null) {
                                String deviceId = device.getId();
                                if (deviceId != null) {
                                    TypeAndDeviceId tadi = new TypeAndDeviceId(typeName, device.getId());
                                    boolean present = tadis.contains(tadi);
                                    holder.bell.setImageResource(present?BELLED_ICON:NON_BELLED_ICON);
                                    holder.bell.setColorFilter(ContextCompat.getColor(contextView.getContext(), R.color.icon));
                                }
                            }
                        }
                    }
                }
            }
            else {
                holder.bell.setImageResource(NON_BELLED_ICON);
                holder.bell.setColorFilter(ContextCompat.getColor(contextView.getContext(), R.color.icon));
            }
        }
    }

    private void setFavourite(Device<? extends DeviceState> device) {
        if (device != null) {
            DeviceMeta meta = device.getMeta();
            if (meta != null) {
                Boolean fav = meta.getFavourites();
                if (fav != null) {
                    if (holder.favourite != null) {
                        holder.favourite.setImageResource(fav?FAVOURITE_ICON:NON_FAVOURITE_ICON);
                        holder.favourite.setColorFilter(ContextCompat.getColor(contextView.getContext(), R.color.icon));
                    }
                }
            }
        }
    }

    protected void setDescription(DeviceState state) {
        // To implement on each Device
    }

    protected void setUI(DeviceState state) {
        // To implement on each Detailed Info Device
        setIconColor(state);
        setDescription(state);
    }

    private void setIconColor(DeviceState state) {
        if (holder.icon != null && holder.onSwitch != null) {
            holder.icon.setColorFilter(getIconColor(holder.onSwitch.isChecked(), state));
        }
    }

    protected void attachFunctions() {
        if (holder.onSwitch != null) {
            holder.onSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                    Device<? extends DeviceState> device = holder.device;
                    if (device != null) {
                        if (holder.icon != null) {
                            holder.icon.setColorFilter(getIconColor(isChecked, device.getState()));
                        }
                        ApiClient.getInstance().executeDeviceAction(device.getId(), getOnSwitchActionName(isChecked), new String[0], new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        DeviceType dt = device.getType();
                                        if (dt != null) {
                                            String typeName = dt.getName();
                                            if (typeName != null) {
                                                Pair<Integer, Integer> pair = turnMessages.get(typeName);
                                                if (pair != null) {
                                                    Snackbar snackbar = Snackbar.make(contextView, isChecked?pair.second:pair.first, Snackbar.LENGTH_SHORT);
                                                    View sbView = snackbar.getView();
                                                    sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                                    snackbar.show();
                                                }
                                            }
                                        }
                                        Log.v("RESULT", result.toString());
                                    } else {
                                        handleError(response);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                                handleUnexpectedError(t);
                            }
                        });
                    }
                }
            });
        }
        if (holder.favourite != null) {
            holder.favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Device device = holder.device;
                    if (device != null) {
                        DeviceMeta meta = device.getMeta();
                        if (meta != null) {
                            Boolean fav = meta.getFavourites();
                            if (fav != null) {
                                fav = !fav;
                                meta.setFavourites(fav);
                                device.setMeta(meta);
                                updateDevice(device);
                                if (holder.favourite != null) {
                                    holder.favourite.setImageResource(fav?FAVOURITE_ICON:NON_FAVOURITE_ICON);
                                    holder.favourite.setColorFilter(ContextCompat.getColor(contextView.getContext(), R.color.icon));
                                    Snackbar snackbar = Snackbar.make(contextView, fav?R.string.snackbar_added_to_favourites:R.string.snackbar_removed_from_favourites, Snackbar.LENGTH_SHORT);
                                    View sbView = snackbar.getView();
                                    sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                    snackbar.show();
                                }
                            }
                        }
                    }
                }
            });
        }
        if (holder.bell != null) {
            holder.bell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Device device = holder.device;
                    if (device != null) {
                        final Gson gson = new Gson();
                        String json = sharedPreferences.getString(BELLED_DEVICES, "");
                        BelledDevices belledDevices;
                        if (json.equals("")) {
                            belledDevices = new BelledDevices(new HashSet<>());
                        }
                        else {
                            belledDevices = gson.fromJson(json, BelledDevices.class);
                        }
                        if (belledDevices != null) {
                            HashSet<TypeAndDeviceId> tadis = belledDevices.getBelledDevices();
                            if (tadis != null) {
                                DeviceType dt = device.getType();
                                if (dt != null) {
                                    String typeName = dt.getName();
                                    if (typeName != null) {
                                        String deviceId = device.getId();
                                        if (deviceId != null) {
                                            TypeAndDeviceId tadi = new TypeAndDeviceId(typeName, device.getId());
                                            boolean present = tadis.contains(tadi);
                                            if (present) {
                                                tadis.remove(tadi);
                                            }
                                            else {
                                                tadis.add(tadi);
                                            }
                                            present = !present;

                                            holder.bell.setImageResource(present?BELLED_ICON:NON_BELLED_ICON);
                                            holder.bell.setColorFilter(ContextCompat.getColor(contextView.getContext(), R.color.icon));

                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            belledDevices.setBelledDevices(tadis);

                                            String jsonToSave = gson.toJson(belledDevices);
                                            editor.putString(BELLED_DEVICES, jsonToSave);
                                            editor.apply();

                                            Snackbar snackbar = Snackbar.make(contextView, present?R.string.snackbar_will_receive_notifications:R.string.snackbar_wont_receive_notifications, Snackbar.LENGTH_SHORT);
                                            View sbView = snackbar.getView();
                                            sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                            snackbar.show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void updateDevice(Device device) {
        ApiClient.getInstance().modifyDevice(device.getId(), device, new Callback<Result<Device>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Device>> call, @NonNull Response<Result<Device>> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                //Log.d("FAVOURITE", "onFailure");
                // Although it updated device correctly, it appears to return onFailure instead of onResponse
                //handleUnexpectedError(t);
            }
        });
    }

    protected int getIconColor(Boolean turnedOn, DeviceState state) {
        return ContextCompat.getColor(contextView.getContext(), getIconColorResource(turnedOn));
    }

    private Integer getIconColorResource(Boolean turnedOn) {
        return turnedOn?DEFAULT_ON_DEVICE_COLOR:DEFAULT_OFF_DEVICE_COLOR;
    }

    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"turnOn":"turnOff";
    }

    protected <T> void handleError(Response<T> response) {

        Snackbar snackbar = Snackbar.make(contextView, contextView.getResources().getString(R.string.handle_unexpected_error), Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.handle_error));
        snackbar.show();

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

        Snackbar snackbar = Snackbar.make(contextView, contextView.getResources().getString(R.string.handle_unexpected_error), Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.handle_error));
        snackbar.show();

        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }
}
