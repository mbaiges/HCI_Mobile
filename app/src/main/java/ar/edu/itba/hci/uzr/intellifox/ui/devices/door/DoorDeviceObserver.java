package ar.edu.itba.hci.uzr.intellifox.ui.devices.door;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoorDeviceObserver extends DeviceObserver {

    private static final String LOCK_ACTION = "lock";
    private static final String UNLOCK_ACTION = "unlock";

    public DoorDeviceObserver(View contextView) {
        super(contextView);
    }

    @Override
    protected void createHolder() {
        this.holder = new DoorDeviceViewHolder();
    }

    @Override
    protected void findElements() {
        super.findElements();
        DoorDeviceViewHolder h = (DoorDeviceViewHolder) holder;

        h.lockBtn = contextView.findViewById(R.id.lockBtn);
    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            DoorDeviceState s = (DoorDeviceState) state;
            DoorDeviceViewHolder h = (DoorDeviceViewHolder) holder;

            String status = s.getStatus();
            String lock = s.getLock();

            if (status != null && lock != null) {
                String aux = status + "-" + lock;
                if (h.description != null) {
                    h.description.setText(aux);
                }
            }
        }
    }

    @Override
    protected void setUI(DeviceState state) {
        super.setUI(state);
        if (state != null) {
            DoorDeviceState s = (DoorDeviceState) state;
            DoorDeviceViewHolder h = (DoorDeviceViewHolder) holder;
            String status = state.getStatus();
            if (status != null) {
                if (holder.onSwitch != null) {
                    holder.onSwitch.setChecked(status.equals("opened"));
                }
            }
            if (h.lockBtn != null && s.getLock() != null) {
                h.lockBtn.setText((s.getLock().equals("locked"))?R.string.dev_door_button_unlock:R.string.dev_door_button_lock);
            }
        }
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        DoorDeviceViewHolder h = (DoorDeviceViewHolder) holder;

        if (h.lockBtn != null) {
            h.lockBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DoorDevice d = (DoorDevice) h.device;
                    if (d != null) {
                        DoorDeviceState s = (DoorDeviceState) d.getState();
                        if (s != null) {
                            String lockStatus = s.getLock();
                            String actionName = LOCK_ACTION;
                            if (lockStatus.equals("locked")) {
                                actionName = UNLOCK_ACTION;
                            }
                            ApiClient.getInstance().executeDeviceAction(d.getId(), actionName, new String[0], new Callback<Result<Object>>() {
                                @Override
                                public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                    if (response.isSuccessful()) {
                                        Result<Object> result = response.body();

                                        if (result != null) {
                                            Boolean success = (Boolean) result.getResult();
                                            if (success != null) {
                                                Log.v("ACTION_SUCCESS", success.toString());
                                            }
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
                }
            });
        }
    }

    @Override
    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"open":"close";
    }
}
