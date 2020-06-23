package ar.edu.itba.hci.uzr.intellifox.ui.devices.blind;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.DialogCompat;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlindDeviceObserver extends DeviceObserver {

    private Integer level = 100;

    public BlindDeviceObserver(View contextView) {
        super(contextView);
        Log.v("INFO:", "Initializing");
    }

    @Override
    protected void createHolder() {
        this.holder = new BlindDeviceViewHolder();
    }

    @Override
    protected void findElements() {
        super.findElements();
        BlindDeviceViewHolder h = (BlindDeviceViewHolder) holder;

        Log.v("INFO:", "Setting Elements");

        h.btnUp = contextView.findViewById(R.id.btnUp);
        h.btnDown = contextView.findViewById(R.id.btnDown);
        h.level = contextView.findViewById(R.id.number);


    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            BlindDeviceState s = (BlindDeviceState) state;
            BlindDeviceViewHolder h = (BlindDeviceViewHolder) holder;

            Log.v("INFO:", "Setting Description");

            String status = s.getStatus();
            String stateStatus;

            switch (status){
                case "closed":
                    stateStatus = contextView.getResources().getString(R.string.dev_blind_state_closed);
                    break;
                case "opened":
                    stateStatus = contextView.getResources().getString(R.string.dev_blind_state_opened);
                    break;
                case "opening":
                    stateStatus = contextView.getResources().getString(R.string.dev_blind_state_opening);
                    break;
                case "closing":
                    stateStatus = contextView.getResources().getString(R.string.dev_blind_state_closing);
                    break;
                default:
                    stateStatus = "";
            }

            Integer stateLevel = s.getLevel() ;
            Integer stateCurrentLevel = s.getCurrentLevel();

            String levelText = contextView.getResources().getString(R.string.dev_blind_level);

            String auxDec;
            if(h.icon != null) {  //si esta el icono
                auxDec = status;
            }
            else
                auxDec = stateStatus + " - " +  levelText + ": " + stateCurrentLevel + "/" + stateLevel;
            if (h.description != null){
                h.description.setText(auxDec);
            }
        }
    }

    @Override
    protected void setUI(DeviceState state) {
        super.setUI(state);
        if (state != null) {
            BlindDeviceState s = (BlindDeviceState) state;
            BlindDeviceViewHolder h = (BlindDeviceViewHolder) holder;

            if(h != null){
                String status = s.getStatus();
                if (status != null && holder.onSwitch != null) {
                    holder.onSwitch.setChecked(status.equals("opened") || status.equals("opening"));
                }
                level = s.getLevel();
                if(h.level != null){
                    h.level.setText(level.toString());
                }
            }

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
                    Log.v("PRESSED", "btnUp");
                    BlindDevice d = (BlindDevice) h.device;
                    if (d != null) {
                        BlindDeviceState s = (BlindDeviceState) d.getState();
                        if (s != null) {
                            if(level+5 <= 100){
                                level += 5;
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
                                                Object lastVal = (Object) result.getResult();
                                                if (lastVal != null) {
                                                    Log.v("ACTION_SUCCESS", lastVal.toString());
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
                    Log.v("PRESSED", "btnDown");
                    BlindDevice d = (BlindDevice) h.device;
                    if (d != null) {
                        BlindDeviceState s = (BlindDeviceState) d.getState();
                        if (s != null) {
                            if(level-5 >= 0){
                                level -= 5;
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
                                                Object lastVal = (Object) result.getResult();
                                                if (lastVal != null) {
                                                    Log.v("ACTION_SUCCESS", lastVal.toString());
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
