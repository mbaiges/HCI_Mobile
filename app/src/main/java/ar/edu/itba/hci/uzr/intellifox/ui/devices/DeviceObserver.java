package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceMeta;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DeviceObserver implements Observer<Device<? extends DeviceState>> {

    private final static Integer DEFAULT_ON_DEVICE_COLOR = R.color.text;
    private final static Integer DEFAULT_OFF_DEVICE_COLOR = R.color.background2;

    private final static Integer FAVOURITE_ICON = R.drawable.ic_heart_filled;
    private final static Integer NON_FAVOURITE_ICON = R.drawable.ic_heart_outline;

    protected View contextView;
    protected DeviceViewHolder holder;

    public DeviceObserver(View contextView) {
        this.contextView = contextView;
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
        holder.icon = contextView.findViewById(R.id.icon);
        holder.description = contextView.findViewById(R.id.desc);
        holder.favourite = contextView.findViewById(R.id.favourite);
        holder.onSwitch = contextView.findViewById(R.id.switch1);
    }

    protected void init(Device<? extends DeviceState> device) {
        DeviceState state = device.getState();

        if (state != null) {
            String status = state.getStatus();

            if (status != null) {
                if (holder.onSwitch != null) {
                    holder.onSwitch.setChecked(status.equals("opened"));
                }
            }
            setFavourite(device);
            setUI(state);
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
            holder.icon.setColorFilter(ContextCompat.getColor(contextView.getContext(), getIconColor(holder.onSwitch.isChecked())));
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
                            holder.icon.setColorFilter(ContextCompat.getColor(contextView.getContext(), getIconColor(isChecked)));
                        }
                        ApiClient.getInstance().executeDeviceAction(device.getId(), getOnSwitchActionName(isChecked), new String[0], new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();

                                    if (result != null) {
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
                if (response.isSuccessful()) {
                    Result<Device> result = response.body();

                    if (result != null) {
                        Log.v("RESULT", result.toString());
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

    private Integer getIconColor(Boolean turnedOn) {
        return turnedOn?DEFAULT_ON_DEVICE_COLOR:DEFAULT_OFF_DEVICE_COLOR;
    }

    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"turnOn":"turnOff";
    }

    protected <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
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
