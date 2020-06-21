package ar.edu.itba.hci.uzr.intellifox.ui.devices.blind;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlindDeviceObserver extends DeviceObserver {

    private Integer level = 100;

    public BlindDeviceObserver(View contextView) {
        super(contextView);
    }

    @Override
    protected void createHolder() {
        this.holder = new BlindDeviceViewHolder();
    }

    @Override
    protected void findElements() {
        super.findElements();
        BlindDeviceViewHolder h = (BlindDeviceViewHolder) holder;

        h.btnUp = contextView.findViewById(R.id.up);
        h.btnDown = contextView.findViewById(R.id.down);
        h.level = contextView.findViewById(R.id.number);
    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            BlindDeviceState s = (BlindDeviceState) state;
            BlindDeviceViewHolder h = (BlindDeviceViewHolder) holder;

            String status = s.getStatus();

            if (status != null) {
                String closed = contextView.getResources().getString(R.string.dev_blind_state_closed);
                String opened = contextView.getResources().getString(R.string.dev_blind_state_opened);
                String aux = status.equals("closed") ? closed : opened ;
                if (h.description != null){
                    h.description.setText(aux);
                }
            }
        }
    }

    @Override
    protected void setUI(DeviceState state) {
        super.setUI(state);
        if (state != null) {
            TapDeviceState s = (TapDeviceState) state;
            BlindDeviceViewHolder h = (BlindDeviceViewHolder) holder;

//            if(h.btnL != null){
//                clearSelections();
//                h.btnL.setChecked(true);
//            }
        }
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        BlindDeviceViewHolder h = (BlindDeviceViewHolder) holder;

        if (h.btnUp != null) {
            h.btnUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BlindDevice d = (BlindDevice) h.device;
                    if (d != null) {
                        BlindDeviceState s = (BlindDeviceState) d.getState();
                        if (s != null) {
                            String aux = h.level.getText().toString();
                            if(level+5 <= 100){
                                level = Integer.parseInt(aux) + 5;
                                h.level.setText(level.toString());
                                String[] args = new String[1];
                                args[0] = level.toString();
                                Log.v("NEW_LEVEL", level.toString());
                                ApiClient.getInstance().executeDeviceAction(d.getId(), "setLevel", args, new Callback<Result<Object>>() {
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
                }
            });
        }

        if (h.btnDown != null) {
            h.btnDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BlindDevice d = (BlindDevice) h.device;
                    if (d != null) {
                        BlindDeviceState s = (BlindDeviceState) d.getState();
                        if (s != null) {
                            String aux = h.level.getText().toString();
                            if(level-5 >= 0){
                                level = Integer.parseInt(aux) - 5;
                                h.level.setText(level.toString());
                                String[] args = new String[1];
                                args[0] = level.toString();
                                Log.v("NEW_LEVEL", level.toString());
                                ApiClient.getInstance().executeDeviceAction(d.getId(), "setLevel", args, new Callback<Result<Object>>() {
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
                }
            });
        }


    }

    @Override
    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"open":"close";
    }
}
