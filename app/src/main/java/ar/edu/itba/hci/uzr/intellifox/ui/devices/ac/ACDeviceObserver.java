package ar.edu.itba.hci.uzr.intellifox.ui.devices.door;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;

import ar.edu.itba.hci.uzr.intellifox.ui.devices.ac.ACDeviceViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ACDeviceObserver extends DeviceObserver {

    private static final String MODE_COOL_ACTION = "cool";
    private static final String MODE_HEAT_ACTION = "heat";
    private static final String MODE_FAN_ACTION = "fan";

    private static final String VERTICAL_AUTO_ACTION = "auto";
    private static final String VERTICAL_22_ACTION = "22";
    private static final String VERTICAL_45_ACTION = "45";
    private static final String VERTICAL_67_ACTION = "67";
    private static final String VERTICAL_90_ACTION = "90";

    private static final String HORIZONTAL_AUTO_ACTION = "auto";
    private static final String HORIZONTAL_MINUS90_ACTION = "-90";
    private static final String HORIZONTAL_MINUS45_ACTION = "-45";
    private static final String HORIZONTAL_0_ACTION = "0";
    private static final String HORIZONTAL_45_ACTION = "45";
    private static final String HORIZONTAL_90_ACTION = "90";

    private static final String FAN_AUTO_ACTION = "auto";
    private static final String FAN_25_ACTION = "25";
    private static final String FAN_50_ACTION = "50";
    private static final String FAN_75_ACTION = "75";
    private static final String FAN_100_ACTION = "100";

    public ACDeviceObserver(View contextView) {
        super(contextView);
    }

    @Override
    protected void createHolder() {
        this.holder = new ACDeviceViewHolder();
    }

    @Override
    protected void findElements() {
        super.findElements();
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;

        h.tempDecBtn = contextView.findViewById(R.id.arrowBtnDown);
        h.tempIncBtn = contextView.findViewById(R.id.arrowBtnUp);


        h.ModeBtn[0] = new Pair<>(contextView.findViewById(R.id.coolButton),MODE_COOL_ACTION);
        h.ModeBtn[1] = new Pair<>(contextView.findViewById(R.id.heatButton),MODE_HEAT_ACTION);
        h.ModeBtn[2] = new Pair<>(contextView.findViewById(R.id.fanButton),MODE_FAN_ACTION);

        h.VBladesBtn[0] = new Pair<>(contextView.findViewById(R.id.vRotationButton1), VERTICAL_AUTO_ACTION);
        h.VBladesBtn[1] = new Pair<>(contextView.findViewById(R.id.vRotationButton2), VERTICAL_22_ACTION);
        h.VBladesBtn[2] = new Pair<>(contextView.findViewById(R.id.vRotationButton3), VERTICAL_45_ACTION);
        h.VBladesBtn[3] = new Pair<>(contextView.findViewById(R.id.vRotationButton4), VERTICAL_67_ACTION);
        h.VBladesBtn[4] = new Pair<>(contextView.findViewById(R.id.vRotationButton5), VERTICAL_90_ACTION;

        h.HBladesBtn[0] = new Pair<>(contextView.findViewById(R.id.hRotationButton1),HORIZONTAL_AUTO_ACTION);
        h.HBladesBtn[1] = new Pair<>(contextView.findViewById(R.id.hRotationButton2),HORIZONTAL_MINUS90_ACTION);
        h.HBladesBtn[2] = new Pair<>(contextView.findViewById(R.id.hRotationButton3),HORIZONTAL_MINUS45_ACTION);
        h.HBladesBtn[3] = new Pair<>(contextView.findViewById(R.id.hRotationButton4),HORIZONTAL_0_ACTION);
        h.HBladesBtn[4] = new Pair<>(contextView.findViewById(R.id.hRotationButton5),HORIZONTAL_45_ACTION);
        h.HBladesBtn[5] = new Pair<>(contextView.findViewById(R.id.hRotationButton6),HORIZONTAL_90_ACTION);

        h.FanSpeedBtn[0] = new Pair<>(contextView.findViewById(R.id.fanButton1),FAN_AUTO_ACTION);
        h.FanSpeedBtn[1] = new Pair<>(contextView.findViewById(R.id.fanButton2),FAN_25_ACTION);
        h.FanSpeedBtn[2] = new Pair<>(contextView.findViewById(R.id.fanButton3),FAN_50_ACTION);
        h.FanSpeedBtn[3] = new Pair<>(contextView.findViewById(R.id.fanButton4),FAN_75_ACTION);
        h.FanSpeedBtn[4] = new Pair<>(contextView.findViewById(R.id.fanButton5),FAN_100_ACTION);

    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            AcDeviceState s = (AcDeviceState) state;
            ACDeviceViewHolder h = (ACDeviceViewHolder) holder;

            String status = s.getStatus();
            String temp = s.getTemperature().toString();
            String mode = s.getMode();
            String vBlades = s.getVerticalSwing();
            String hBlades = s.getHorizontalSwing();
            String fanSpeed = s.getFanSpeed();



            if (status != null && temp != null && mode != null && vBlades != null && hBlades != null && fanSpeed != null) {
                String aux = status + "-" + temp + "-" + mode + "-" + vBlades + "-" + hBlades + "-" + fanSpeed ;
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
            AcDeviceState s = (AcDeviceState) state;
            ACDeviceViewHolder h = (ACDeviceViewHolder) holder;

            if (h.lockBtn != null && s.getLock() != null) {
                h.lockBtn.setText((s.getLock().equals("locked"))?R.string.dev_door_button_unlock:R.string.dev_door_button_lock);
            }
        }
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;

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
        return switchStatus?"turnOn":"turnOff";
    }
}
