package ar.edu.itba.hci.uzr.intellifox.ui.devices.ac;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;
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
        h.temperatureValue = contextView.findViewById(R.id.temperatureValue);

        h.ModeBtn[0] = new Pair<>(contextView.findViewById(R.id.coolButton),MODE_COOL_ACTION);
        h.ModeBtn[1] = new Pair<>(contextView.findViewById(R.id.heatButton),MODE_HEAT_ACTION);
        h.ModeBtn[2] = new Pair<>(contextView.findViewById(R.id.fanButton),MODE_FAN_ACTION);

        h.VBladesBtn[0] = new Pair<>(contextView.findViewById(R.id.vRotationButton1), VERTICAL_AUTO_ACTION);
        h.VBladesBtn[1] = new Pair<>(contextView.findViewById(R.id.vRotationButton2), VERTICAL_22_ACTION);
        h.VBladesBtn[2] = new Pair<>(contextView.findViewById(R.id.vRotationButton3), VERTICAL_45_ACTION);
        h.VBladesBtn[3] = new Pair<>(contextView.findViewById(R.id.vRotationButton4), VERTICAL_67_ACTION);
        h.VBladesBtn[4] = new Pair<>(contextView.findViewById(R.id.vRotationButton5), VERTICAL_90_ACTION);

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

            String status = state.getStatus();
            if (status != null) {
                if (h.onSwitch != null) {
                    h.onSwitch.setChecked(status.equals("turnOn"));
                }
            }


            if(h.temperatureValue != null){
                Integer temp = s.getTemperature();
                String aux = temp.toString();
                h.temperatureValue.setText(aux);
            }
            if(h.ModeBtn != null){
                String mode = s.getMode();
                clearModeSelections();
                if(mode.equals("cool")){
                    h.ModeBtn[0].first.setChecked(true);
                } else if(mode.equals("heat")){
                    h.ModeBtn[1].first.setChecked(true);
                } else if(mode.equals("fan")){
                    h.ModeBtn[2].first.setChecked(true);
                }
            }

            if(h.VBladesBtn != null){
                String vertical = s.getVerticalSwing();
                clearVBladesSelections();
                if(vertical.equals("auto")){
                    h.VBladesBtn[0].first.setChecked(true);
                }else if(vertical.equals("22")){
                    h.VBladesBtn[1].first.setChecked(true);
                }else if(vertical.equals("45")){
                    h.VBladesBtn[2].first.setChecked(true);
                }else if(vertical.equals("67")){
                    h.VBladesBtn[3].first.setChecked(true);
                }else if(vertical.equals("90")){
                    h.VBladesBtn[4].first.setChecked(true);
                }
            }

            if(h.HBladesBtn != null){
                String horizontal = s.getHorizontalSwing();
                clearHBladesSelections();
                if(horizontal.equals("auto")){
                    h.HBladesBtn[0].first.setChecked(true);
                }else if(horizontal.equals("-90")){
                    h.HBladesBtn[1].first.setChecked(true);
                }else if(horizontal.equals("-45")){
                    h.HBladesBtn[2].first.setChecked(true);
                }else if(horizontal.equals("0")){
                    h.HBladesBtn[3].first.setChecked(true);
                }else if(horizontal.equals("45")){
                    h.HBladesBtn[4].first.setChecked(true);
                }else if(horizontal.equals("90")){
                    h.HBladesBtn[5].first.setChecked(true);
                }
            }

            if(h.FanSpeedBtn != null){
                String fan = s.getFanSpeed();
                clearFanSpeedSelections();
                if(fan.equals("auto")){
                    h.FanSpeedBtn[0].first.setChecked(true);
                }else if(fan.equals("25")){
                     h.FanSpeedBtn[1].first.setChecked(true);
                }else  if(fan.equals("50")){
                    h.FanSpeedBtn[2].first.setChecked(true);
                }else  if(fan.equals("75")){
                    h.FanSpeedBtn[3].first.setChecked(true);
                }else  if(fan.equals("100")){
                    h.FanSpeedBtn[4].first.setChecked(true);
                }
            }
        }
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;

        toggleButtonsWork(h.ModeBtn, h, "setMode");
        toggleButtonsWork(h.VBladesBtn, h, "setVerticalSwing");
        toggleButtonsWork(h.HBladesBtn, h, "setHorizontalSwing");
        toggleButtonsWork(h.FanSpeedBtn, h, "setFanSpeed");

        upButtonWork(h.tempIncBtn, h);
        downButtonWork(h.tempDecBtn, h);

    }

    public void toggleButtonsWork(Pair<ToggleButton,String>[] array, ACDeviceViewHolder h, String functionType){
        if(h.VBladesBtn != null && h.HBladesBtn != null && h.ModeBtn != null && h.FanSpeedBtn != null){
            for(int i = 0 ; i < array.length ; i++){
                if(array[i] != null){
                    final Integer aux = i;
                    array[i].first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AcDevice d = (AcDevice) h.device;
                            if(d != null){
                                AcDeviceState s = d.getState();
                                if(s != null){
                                    String[] args = {array[aux].second};
                                    ApiClient.getInstance().executeDeviceAction(d.getId(), functionType, args, new Callback<Result<Object>>() {
                                        @Override
                                        public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                                            if(response.isSuccessful()){
                                                Result<Object> result = response.body();
                                                if(result != null){
                                                    Object success =  result.getResult();
                                                    if(success != null){
                                                        Log.v("ACTION_SUCCESS", success.toString());
                                                        if(functionType.equals("setMode"))
                                                            clearModeSelections();
                                                        else if(functionType.equals("setVerticalSwing"))
                                                            clearVBladesSelections();
                                                        else if(functionType.equals("setHorizontalSwing"))
                                                            clearHBladesSelections();
                                                        else if(functionType.equals("setFanSpeed"))
                                                            clearFanSpeedSelections();

                                                        array[aux].first.setChecked(true);
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


    public void upButtonWork(Button button, ACDeviceViewHolder h){
        if(button != null){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AcDevice d = (AcDevice) h.device;
                    if(d != null){
                        AcDeviceState s = d.getState();
                        if(s != null){
                            String actionName = "setTemperature";
                            Integer newTemperature = s.getTemperature();
                            if(newTemperature >= 38){
                                return;
                            }else{
                                newTemperature++;
                                String[] args = {newTemperature.toString()};
                                ApiClient.getInstance().executeDeviceAction(d.getId(), actionName, args , new Callback<Result<Object>>() {
                                    @Override
                                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                                        if(response.isSuccessful()){
                                            Result<Object> result = response.body();
                                            if(result != null){
                                                Object success =  result.getResult();
                                                if(success != null){
                                                    Log.v("ACTION_SUCCESS", success.toString());
                                                    h.temperatureValue.setText(args[0]);
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
                }
            });
        }
    }

    public void downButtonWork(Button button, ACDeviceViewHolder h){
        if(button != null){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AcDevice d = (AcDevice) h.device;
                    if(d != null){
                        AcDeviceState s = d.getState();
                        if(s != null){
                            String actionName = "setTemperature";
                            Integer newTemperature = s.getTemperature();
                            if(newTemperature <= 18){
                                return;
                            }else{
                                newTemperature--;
                                String[] args = {newTemperature.toString()};
                                ApiClient.getInstance().executeDeviceAction(d.getId(), actionName, args , new Callback<Result<Object>>() {
                                    @Override
                                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                                        if(response.isSuccessful()){
                                            Result<Object> result = response.body();
                                            if(result != null){
                                                Object success =  result.getResult();
                                                if(success != null){
                                                    Log.v("ACTION_SUCCESS", success.toString());
                                                    h.temperatureValue.setText(args[0]);
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
                }
            });
        }
    }


    @Override
    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"turnOn":"turnOff";
    }

    private void clearModeSelections(){
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        for (int i = 0 ; i<h.ModeBtn.length ; i++){
            if(h.ModeBtn[i] != null)
                h.ModeBtn[i].first.setChecked(false);
        }
    }

    private void clearVBladesSelections(){
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        for (int i = 0 ; i<h.VBladesBtn.length ; i++){
            if(h.VBladesBtn[i] != null)
                h.VBladesBtn[i].first.setChecked(false);
        }
    }

    private void clearHBladesSelections(){
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        for (int i = 0 ; i<h.HBladesBtn.length ; i++){
            if(h.HBladesBtn[i] != null)
                h.HBladesBtn[i].first.setChecked(false);
        }
    }

    private void clearFanSpeedSelections(){
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        for (int i = 0 ; i<h.FanSpeedBtn.length ; i++){
            if(h.FanSpeedBtn[i] != null)
                h.FanSpeedBtn[i].first.setChecked(false);
        }
    }
}
