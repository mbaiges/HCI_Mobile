package ar.edu.itba.hci.uzr.intellifox.ui.devices.light;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LightDeviceObserver extends DeviceObserver {

    private static final String LOCK_ACTION = "lock";
    private static final String UNLOCK_ACTION = "unlock";

    public LightDeviceObserver(View contextView) {
        super(contextView);
    }

    @Override
    protected void createHolder() {
        this.holder = new LightDeviceViewHolder();
    }

    @Override
    protected void findElements() {
        super.findElements();
        LightDeviceViewHolder h = (LightDeviceViewHolder) holder;

        //FIND ELEEEMEEEEENETTSSSSSSS
    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            LightDeviceState s = (LightDeviceState) state;
            LightDeviceViewHolder h = (LightDeviceViewHolder) holder;

            String status = s.getStatus();


            if (status != null) {
                if(status.equals("on")){
                    status = contextView.getResources().getString(R.string.dev_light_status_on);
                }else{
                    status = contextView.getResources().getString(R.string.dev_light_status_off);
                }

                if (h.description != null) {
                    h.description.setText(status);
                }
            }
        }
    }

    @Override
    protected void setUI(DeviceState state) {
        super.setUI(state);
        if (state != null) {
            LightDeviceState s = (LightDeviceState) state;
            LightDeviceViewHolder h = (LightDeviceViewHolder) holder;
            String status = state.getStatus();
            if (status != null) {
                if (holder.onSwitch != null) {
                    holder.onSwitch.setChecked(status.equals("on"));
                }
            }
        }
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        LightDeviceViewHolder h = (LightDeviceViewHolder) holder;

//        if (h. .................  != null) {
//            h. ................. setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LightDevice d = (LightDevice) h.device;
//                    if (d != null) {
//                        LightDeviceState s = (LightDeviceState) d.getState();
//                        if (s != null) {
//
//                            String actionName = ...............;
//
//                            ApiClient.getInstance().executeDeviceAction(d.getId(), actionName, .........., new Callback<Result<Object>>() {
//                                @Override
//                                public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
//                                    if (response.isSuccessful()) {
//                                        Result<Object> result = response.body();
//
//                                        if (result != null) {
//                                            Boolean success = (Boolean) result.getResult();
//                                            if (success != null) {
//                                                Log.v("ACTION_SUCCESS", success.toString());
//                                            }
//                                        } else {
//                                            handleError(response);
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
//                                    handleUnexpectedError(t);
//                                }
//                            });
//                        }
//                    }
//                }
//            });
//        }
    }

    @Override
    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"turnOn":"turnOff";
    }
}
