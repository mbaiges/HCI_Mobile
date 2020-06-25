package ar.edu.itba.hci.uzr.intellifox.ui.devices.ac;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        ACDeviceViewHolder h = (ACDeviceViewHolder) this.holder;
        h.ModeBtn = new Object[3];
        h.VBladesBtn = new Object[5];
        h.HBladesBtn = new Object[6];
        h.FanSpeedBtn = new Object[5];
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
            if(status.equals("on")){
                status = contextView.getResources().getString(R.string.dev_ac_status_on);
            }else{
                status = contextView.getResources().getString(R.string.dev_ac_status_off);
            }

            String tempTitle = contextView.getResources().getString(R.string.dev_ac_title_temperature);
            String modeTitle = contextView.getResources().getString(R.string.dev_ac_title_mode);
            String vBladesTitle = contextView.getResources().getString(R.string.dev_ac_title_vSwing);
            String hBladesTitle = contextView.getResources().getString(R.string.dev_ac_title_hSwing);
            String fanTitle = contextView.getResources().getString(R.string.dev_ac_title_fan);

            String temp = s.getTemperature().toString();

            String mode = s.getMode();
            if(mode.equals("cool")){
                mode = contextView.getResources().getString(R.string.dev_ac_button_mode_cool);
            }else if (mode.equals("heat")){
                mode = contextView.getResources().getString(R.string.dev_ac_button_mode_heat);
            }else if (mode.equals("fan")){
                mode = contextView.getResources().getString(R.string.dev_ac_button_mode_fan);
            }

            String vBlades = s.getVerticalSwing();
            if(vBlades.equals("auto")){
                vBlades = contextView.getResources().getString(R.string.dev_ac_button_vSwing_auto);
            }

            String hBlades = s.getHorizontalSwing();
            if(hBlades.equals("auto")){
                hBlades = contextView.getResources().getString(R.string.dev_ac_button_hSwing_auto);
            }

            String fanSpeed = s.getFanSpeed();
            if(fanSpeed.equals("auto")){
                fanSpeed = contextView.getResources().getString(R.string.dev_ac_button_fan_auto);
            }


            if (status != null && temp != null && mode != null && vBlades != null && hBlades != null && fanSpeed != null && tempTitle != null && modeTitle != null && vBladesTitle != null && hBladesTitle != null && fanTitle != null) {
                String aux;
                if(h.icon != null) {  //si esta el icono
                    aux = status;
                }
                else {
                    aux = status + "\n" + tempTitle + temp + " - " + modeTitle + mode + " - " + vBladesTitle + vBlades + "\n" + hBladesTitle + hBlades + " - " + fanTitle + fanSpeed;
                }
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
            //Log.v("ENTROOO", "SET UI");
            String status = state.getStatus();
            if (status != null) {
                if (h.onSwitch != null) {
                    //Log.v("asdawdwd",status);
                    h.onSwitch.setChecked(status.equals("on"));
                }
            }


            if(h.temperatureValue != null){
                Integer temp = s.getTemperature();
                if(temp != null){

                    String aux = temp.toString();
                    h.temperatureValue.setText(aux);
                }
            }
            if(h.ModeBtn != null){
                String mode = s.getMode();
                clearModeSelections();
                if(mode.equals("cool")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.ModeBtn[0];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                } else if(mode.equals("heat")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.ModeBtn[1];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                } else if(mode.equals("fan")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.ModeBtn[2];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }
            }

            if(h.VBladesBtn != null){
                String vertical = s.getVerticalSwing();
                clearVBladesSelections();
                if(vertical.equals("auto")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.VBladesBtn[0];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(vertical.equals("22")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.VBladesBtn[1];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(vertical.equals("45")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.VBladesBtn[2];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(vertical.equals("67")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.VBladesBtn[3];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(vertical.equals("90")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.VBladesBtn[4];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }
            }

            if(h.HBladesBtn != null){
                String horizontal = s.getHorizontalSwing();
                clearHBladesSelections();
                if(horizontal.equals("auto")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HBladesBtn[0];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(horizontal.equals("-90")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HBladesBtn[1];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(horizontal.equals("-45")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HBladesBtn[2];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(horizontal.equals("0")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HBladesBtn[3];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(horizontal.equals("45")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HBladesBtn[4];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(horizontal.equals("90")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HBladesBtn[5];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }
            }

            if(h.FanSpeedBtn != null){
                String fan = s.getFanSpeed();
                clearFanSpeedSelections();
                if(fan.equals("auto")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.FanSpeedBtn[0];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(fan.equals("25")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.FanSpeedBtn[1];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else  if(fan.equals("50")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.FanSpeedBtn[2];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else  if(fan.equals("75")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.FanSpeedBtn[3];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else  if(fan.equals("100")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.FanSpeedBtn[4];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
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

    public void toggleButtonsWork(Object[] array, ACDeviceViewHolder h, String functionType){
        if(h.VBladesBtn != null && h.HBladesBtn != null && h.ModeBtn != null && h.FanSpeedBtn != null){
            for(int i = 0 ; i < array.length ; i++){
                if(array[i] != null){
                    final Integer aux = i;
                    Pair<ToggleButton,String> pairAux = (Pair<ToggleButton, String>) array[i];
                    if(pairAux.first != null){
                        pairAux.first.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AcDevice d = (AcDevice) h.device;
                                if(d != null){
                                    AcDeviceState s = d.getState();
                                    if(s != null){
                                        String[] args = {pairAux.second};
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

                                                            pairAux.first.setChecked(true);
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

    }


    public void upButtonWork(ImageButton button, ACDeviceViewHolder h){
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

    public void downButtonWork(ImageButton button, ACDeviceViewHolder h){
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
            if(h.ModeBtn[i] != null){
                Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.ModeBtn[i];
                if(aux.first != null){
                    aux.first.setChecked(false);
                }

            }
        }
    }

    private void clearVBladesSelections(){
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        for (int i = 0 ; i<h.VBladesBtn.length ; i++){
            if(h.VBladesBtn[i] != null) {
                Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.VBladesBtn[i];
                if(aux.first != null){
                    aux.first.setChecked(false);
                }
            }
        }
    }

    private void clearHBladesSelections(){
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        for (int i = 0 ; i<h.HBladesBtn.length ; i++){
            if(h.HBladesBtn[i] != null) {
                Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HBladesBtn[i];
                if(aux.first != null){
                    aux.first.setChecked(false);
                }
            }
        }
    }

    private void clearFanSpeedSelections(){
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        for (int i = 0 ; i<h.FanSpeedBtn.length ; i++){
            if(h.FanSpeedBtn[i] != null) {
                Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.FanSpeedBtn[i];
                if(aux.first != null){
                    aux.first.setChecked(false);
                }
            }
        }
    }
}
