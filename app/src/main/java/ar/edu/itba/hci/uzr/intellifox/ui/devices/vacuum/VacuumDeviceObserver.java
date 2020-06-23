package ar.edu.itba.hci.uzr.intellifox.ui.devices.vacuum;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.RoomArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;

import ar.edu.itba.hci.uzr.intellifox.ui.devices.ac.ACDeviceViewHolder;
import ar.edu.itba.hci.uzr.intellifox.ui.rooms.RoomsViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacuumDeviceObserver extends DeviceObserver {


    private static final String DOCK = "dock";

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

        h.dockBtn = contextView.findViewById(R.id.dock);

        h.modeBtn[0] = new Pair<>(contextView.findViewById(R.id.mopBtn), "mop");
        h.modeBtn[1] = new Pair<>(contextView.findViewById(R.id.vacuumBtn), "vacuum");

    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            VacuumDeviceState s = (VacuumDeviceState) state;
            VacuumDeviceViewHolder h = (VacuumDeviceViewHolder) holder;

            String status = s.getStatus();
            if (status.equals("active")) {
                status = contextView.getResources().getString(R.string.dev_vacuum_state_active);
            } else if (status.equals("inactive")) {
                status = contextView.getResources().getString(R.string.dev_vacuum_state_inactive);
            } else if (status.equals("docked")) {
                status = contextView.getResources().getString(R.string.dev_vacuum_state_docked);
            }


            String bat = s.getBatteryLevel().toString();

            String mode = s.getMode();
            if (mode.equals("mop")) {
                mode = contextView.getResources().getString(R.string.dev_vacuum_button_mop);
            } else if (mode.equals("vacuum")) {
                mode = contextView.getResources().getString(R.string.dev_vacuum_button_vacuum);
            }


            if (status != null && bat != null && mode != null) {
                String aux;
                if (h.icon != null) {  //si esta el icono
                    aux = status;
                } else
                    aux = status + "-" + mode + "-" + contextView.getResources().getString(R.string.dev_vacuum_battery) + ": " + bat + "%";
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
            Pair<ToggleButton, String> p0 = (Pair<ToggleButton, String>) h.modeBtn[0];
            if (p0.first != null && s.getMode().equals("mop")) {
                p0.first.setChecked(true);
            }
            Pair<ToggleButton, String> p1 = (Pair<ToggleButton, String>) h.modeBtn[1];
            if (p1.first != null && s.getMode().equals("vacuum")) {
                p1.first.setChecked(true);
            }

            if (h.dockBtn != null && s.getStatus() != null) {
                h.dockBtn.setText((s.getStatus().equals("docked")) ? R.string.dev_vacuum_button_docked : R.string.dev_vacuum_button_dock);
            }
        }

        ApiClient.getInstance().getRooms(new Callback<Result<List<Room>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Room>>> call, @NonNull Response<Result<List<Room>>> response) {
                MutableLiveData<Object> mRooms = new MutableLiveData<>();
                if (response.isSuccessful()) {
                    Result<List<Room>> result = response.body();
                    if (result != null) {
                        Set<Room> actualRoomsSet = new HashSet<>(result.getResult());
                        Set<Room> roomsSet = (Set<Room>) mRooms.getValue();

                        // Spinner element
                        Spinner spinner = contextView.findViewById(R.id.rooms_spinner);
                        
                        // Creating adapter for spinner
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomsSet);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        spinner.setAdapter(dataAdapter);

//                        if (roomsSet == null || !(roomsSet.equals(actualRoomsSet))) {
//                            mRooms.postValue(actualRoomsSet);
//                            VacuumDeviceViewHolder h = (VacuumDeviceViewHolder) holder;
//                            for (Room r : actualRoomsSet) {
//                                contextView.findViewById(R.id.rooms_spinner).
//                            }
//                        }

                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Room>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }


    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        VacuumDeviceViewHolder h = (VacuumDeviceViewHolder) holder;

        if (h.modeBtn != null) {
            for (int i = 0; i < 2; i++) {
                if (h.modeBtn[i] != null) {
                    Pair<ToggleButton, String> aux = (Pair<ToggleButton, String>) h.modeBtn[i];
                    if (aux.first != null) {
                        aux.first.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                VacuumDevice d = (VacuumDevice) h.device;
                                if (d != null) {
                                    VacuumDeviceState s = d.getState();
                                    if (s != null) {
                                        String[] args = {aux.second};
                                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setMode", args, new Callback<Result<Object>>() {
                                            @Override
                                            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                                                if (response.isSuccessful()) {
                                                    Result<Object> result = response.body();
                                                    if (result != null) {
                                                        Object success = result.getResult();
                                                        if (success != null) {
                                                            Log.v("ACTION_SUCCESS", success.toString());
                                                            clearModeSelections();
                                                            aux.first.setChecked(true);
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Result<Object>> call, Throwable t) {
                                                handleUnexpectedError(t);
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }

                }
            }
        }

        if (h.dockBtn != null) {
            h.dockBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VacuumDevice d = (VacuumDevice) h.device;
                    if (d != null) {
                        VacuumDeviceState s = (VacuumDeviceState) d.getState();
                        if (s != null) {
                            String actionName = DOCK;
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
        return switchStatus ? "start" : "pause";
    }

    private void clearModeSelections() {
        VacuumDeviceViewHolder h = (VacuumDeviceViewHolder) holder;
        for (int i = 0; i < h.modeBtn.length; i++) {
            if (h.modeBtn[i] != null) {
                Pair<ToggleButton, String> aux = (Pair<ToggleButton, String>) h.modeBtn[i];
                if (aux.first != null) {
                    aux.first.setChecked(false);
                }

            }
        }
    }

}