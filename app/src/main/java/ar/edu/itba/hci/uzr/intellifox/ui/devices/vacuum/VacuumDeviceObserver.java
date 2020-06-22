package ar.edu.itba.hci.uzr.intellifox.ui.devices.vacuum;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacuumDeviceObserver extends DeviceObserver {

    private static final String DOCK_ACTION = "docked";
    private static final String DOCKED_ACTION = "dock";

    public VacuumDeviceObserver(View contextView) {
        super(contextView);
    }

    @Override
    protected void createHolder() {
        this.holder = new VacuumDeviceViewHolder();
        VacuumDeviceViewHolder h = (VacuumDeviceViewHolder) this.holder;
        h.modeBtn = new Object[2];
    }

    @Override
    protected void findElements() {
        super.findElements();
        VacuumDeviceViewHolder h = (VacuumDeviceViewHolder) holder;

        h.dockBtn = contextView.findViewById(R.id.lockBtn);
        h.modeBtn[0] = new Pair<String, View>("mop", contextView.findViewById(R.id.mopBtn));
        h.modeBtn[1] = new Pair<String, View>("vacuum", contextView.findViewById(R.id.vacuumBtn));
    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            VacuumDeviceState s = (VacuumDeviceState) state;
            VacuumDeviceViewHolder h = (VacuumDeviceViewHolder) holder;

            String status = s.getStatus();
            Integer battLevel = s.getBatteryLever();
            String bat = "";
            if (battLevel != null) {
                bat = s.getBatteryLever().toString();
            }
            String mode = s.getMode();

            if (status != null && mode != null) {
                String aux = new String();
                if(status.equals("docked"))
                    aux = "docked + battery:" + bat;
                else if(status.equals("inactive"))
                    aux = "inactive";
                else
                    aux = status + "- battery: " + bat + "mode:" + mode;
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
            VacuumDeviceState s = (VacuumDeviceState) state;
            VacuumDeviceViewHolder h = (VacuumDeviceViewHolder) holder;
            String status = state.getStatus();
            if (status != null) {
                if (holder.onSwitch != null) {
                    holder.onSwitch.setChecked(status.equals("active"));
                }
            }
            Pair<String, ToggleButton> p0 = (Pair<String, ToggleButton>) h.modeBtn[0];
            if (p0.second != null && s.getMode().equals("mop")) {
                p0.second.setText(R.string.dev_vacuum_button_mop);
            }
            Pair<String, ToggleButton> p1 = (Pair<String, ToggleButton>) h.modeBtn[1];
            if(p1.second != null && s.getMode().equals("vacuum")) {
                p1.second.setText(R.string.dev_vacuum_button_vacuum);
            }

            if (h.dockBtn != null && s.getStatus() != null) {
                h.dockBtn.setText((s.getStatus().equals("docked"))?R.string.dev_vacuum_button_docked:R.string.dev_vacuum_button_dock);
            }
        }
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        VacuumDeviceViewHolder h = (VacuumDeviceViewHolder) holder;

        /*
        for(int i = 0; i < 2; i++){
           if(h.modeBtn[i] != null){
               int finalI = i;
               h.modeBtn[i].second.setOnClickListener(new View.OnClickListener(){
                   @Override
                   public void onClick(View v) {
                       VacuumDevice d = (VacuumDevice) h.device;
                       if (d != null) {
                           VacuumDeviceState s = (VacuumDeviceState) d.getState();
                           if (s != null) {
                               String actionName = h.modeBtn[finalI].first;
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

        */

        if (h.dockBtn != null) {
            h.dockBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VacuumDevice d = (VacuumDevice) h.device;
                    if (d != null) {
                        VacuumDeviceState s = (VacuumDeviceState) d.getState();
                        if (s != null) {
                            String dockStatus = s.getStatus();
                            String actionName = DOCK_ACTION;
                            if (dockStatus.equals("docked")) {
                                actionName = DOCKED_ACTION;
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
        return switchStatus?"active":"inactive";
    }
}
