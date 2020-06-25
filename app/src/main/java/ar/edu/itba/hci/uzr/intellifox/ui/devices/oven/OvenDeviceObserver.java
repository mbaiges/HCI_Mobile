package ar.edu.itba.hci.uzr.intellifox.ui.devices.oven;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;



import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;


import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OvenDeviceObserver extends DeviceObserver {


    //se llaman con setHeat
    private static final String HEAT_CONVENTIONAL_ACTION = "conventional";
    private static final String HEAT_BOTTOM_ACTION = "bottom";
    private static final String HEAT_TOP_ACTION = "top";


    //se llaman con setGrill
    private static final String GRILL_LARGE_ACTION = "large";
    private static final String GRILL_ECO_ACTION = "eco";
    private static final String GRILL_OFF_ACTION = "off";

    //se llaman con setConvection
    private static final String CONVECTION_NORMAL_ACTION = "normal";
    private static final String CONVECTION_ECO_ACTION = "eco";
    private static final String CONVECTION_OFF_ACTION = "off";


    public OvenDeviceObserver(View contextView) {
        super(contextView);
    }

    @Override
    protected void createHolder() {
        this.holder = new OvenDeviceViewHolder();
        OvenDeviceViewHolder h = (OvenDeviceViewHolder) this.holder;
        h.HeatBtn = new Object[3];
        h.GrillBtn = new Object[3];
        h.ConvectionBtn = new Object[3];

    }

    @Override
    protected void findElements() {
        super.findElements();
        OvenDeviceViewHolder h = (OvenDeviceViewHolder) holder;

        h.tempDecBtn = contextView.findViewById(R.id.arrowBtnDown);
        h.tempIncBtn = contextView.findViewById(R.id.arrowBtnUp);
        h.temperatureValue = contextView.findViewById(R.id.temperatureValue);


        h.HeatBtn[0] = new Pair<>(contextView.findViewById(R.id.conventionalButton),HEAT_CONVENTIONAL_ACTION);
        h.HeatBtn[1] = new Pair<>(contextView.findViewById(R.id.bottomButton),HEAT_BOTTOM_ACTION);
        h.HeatBtn[2] = new Pair<>(contextView.findViewById(R.id.topButton),HEAT_TOP_ACTION);


        h.GrillBtn[0] = new Pair<>(contextView.findViewById(R.id.largeButton), GRILL_LARGE_ACTION);
        h.GrillBtn[1] = new Pair<>(contextView.findViewById(R.id.ecoButton), GRILL_ECO_ACTION);
        h.GrillBtn[2] = new Pair<>(contextView.findViewById(R.id.offButton), GRILL_OFF_ACTION);



        h.ConvectionBtn[0] = new Pair<>(contextView.findViewById(R.id.normalButton),CONVECTION_NORMAL_ACTION);
        h.ConvectionBtn[1] = new Pair<>(contextView.findViewById(R.id.ecoConvectionButton),CONVECTION_ECO_ACTION);
        h.ConvectionBtn[2] = new Pair<>(contextView.findViewById(R.id.offConvectionButton),CONVECTION_OFF_ACTION);





    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            OvenDeviceState s = (OvenDeviceState) state;
            OvenDeviceViewHolder h = (OvenDeviceViewHolder) holder;


            String status = s.getStatus();
            if(status.equals("on")){
                status = contextView.getResources().getString(R.string.dev_oven_status_on);
            }else{
                status = contextView.getResources().getString(R.string.dev_oven_status_off);
            }

            String tempTitle = contextView.getResources().getString(R.string.dev_oven_title_temperature);
            String heatTitle = contextView.getResources().getString(R.string.dev_oven_title_heat);
            String grillTitle = contextView.getResources().getString(R.string.dev_oven_title_grill);
            String convectionTitle = contextView.getResources().getString(R.string.dev_oven_title_convection);

            String temp = s.getTemperature().toString();

            String heat = s.getHeat();
            if(heat.equals("conventional")){
                heat = contextView.getResources().getString(R.string.dev_oven_button_heat_conventional);
            }else if (heat.equals("bottom")){
                heat = contextView.getResources().getString(R.string.dev_oven_button_heat_bottom);
            }else if (heat.equals("top")){
                heat = contextView.getResources().getString(R.string.dev_oven_button_heat_top);
            }

            String grill = s.getGrill();
            if(grill.equals("large")){
                grill = contextView.getResources().getString(R.string.dev_oven_button_grill_large);
            }else if (grill.equals("eco")){
                grill = contextView.getResources().getString(R.string.dev_oven_button_grill_eco);
            }else if(grill.equals("off")){
                grill = contextView.getResources().getString(R.string.dev_oven_button_grill_off);
            }
            String convection = s.getConvection();
            if(convection.equals("large")){
                convection = contextView.getResources().getString(R.string.dev_oven_button_convection_normal);
            }else if (convection.equals("eco")){
                convection = contextView.getResources().getString(R.string.dev_oven_button_convection_eco);
            }else if(convection.equals("off")){
                convection = contextView.getResources().getString(R.string.dev_oven_button_convection_off);
            }



            if (status != null && temp != null && heat != null && grill != null && convection != null && tempTitle != null && heatTitle != null && grillTitle != null && convectionTitle != null) {

                String aux;
                if(h.icon != null) {  //si esta el icono
                    aux = status;
                }
                else
                    aux= status + "-" + tempTitle + temp + "-" + heatTitle + heat + "-" + grillTitle + grill + "-" + convectionTitle + convection  ;
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
            OvenDeviceState s = (OvenDeviceState) state;
            OvenDeviceViewHolder h = (OvenDeviceViewHolder) holder;
            String status = state.getStatus();
            if (status != null) {
                if (h.onSwitch != null) {
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
            if(h.HeatBtn != null){
                String heat = s.getHeat();
                clearHeatSelections();
                if(heat.equals("conventional")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HeatBtn[0];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                } else if(heat.equals("bottom")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HeatBtn[1];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                } else if(heat.equals("top")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HeatBtn[2];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }
            }

            if(h.GrillBtn != null){
                String grill = s.getGrill();
                clearGrillSelections();
                if(grill.equals("large")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.GrillBtn[0];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(grill.equals("eco")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.GrillBtn[1];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(grill.equals("off")) {
                    Pair<ToggleButton, String> aux = (Pair<ToggleButton, String>) h.GrillBtn[2];
                    if (aux.first != null) {
                        aux.first.setChecked(true);
                    }
                }
            }

            if(h.ConvectionBtn != null){
                String convection = s.getConvection();
                clearConvectionSelections();
                if(convection.equals("normal")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.ConvectionBtn[0];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(convection.equals("eco")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.ConvectionBtn[1];
                    if(aux.first != null){
                        aux.first.setChecked(true);
                    }
                }else if(convection.equals("off")){
                    Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.ConvectionBtn[2];
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
        OvenDeviceViewHolder h = (OvenDeviceViewHolder) holder;

        toggleButtonsWork(h.HeatBtn, h, "setHeat");
        toggleButtonsWork(h.GrillBtn, h, "setGrill");
        toggleButtonsWork(h.ConvectionBtn, h, "setConvection");


        upButtonWork(h.tempIncBtn, h);
        downButtonWork(h.tempDecBtn, h);

    }

    public void toggleButtonsWork(Object[] array, OvenDeviceViewHolder h, String functionType){
        if(h.HeatBtn != null && h.GrillBtn != null && h.ConvectionBtn != null ){
            for(int i = 0 ; i < array.length ; i++){
                if(array[i] != null){
                    final Integer aux = i;
                    Pair<ToggleButton,String> pairAux = (Pair<ToggleButton, String>) array[i];
                    if(pairAux.first != null){
                        pairAux.first.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OvenDevice d = (OvenDevice) h.device;
                                if(d != null){
                                    OvenDeviceState s = d.getState();
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
                                                            if(functionType.equals("setHeat"))
                                                                clearHeatSelections();
                                                            else if(functionType.equals("setGrill"))
                                                                clearGrillSelections();
                                                            else if(functionType.equals("setConvection"))
                                                                clearConvectionSelections();


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


    public void upButtonWork(Button button, OvenDeviceViewHolder h){
        if(button != null){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OvenDevice d = (OvenDevice) h.device;
                    if(d != null){
                        OvenDeviceState s = d.getState();
                        if(s != null){
                            String actionName = "setTemperature";
                            Integer newTemperature = s.getTemperature();
                            if(newTemperature >= 230){
                                return;
                            }else{
                                newTemperature += 10;
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

    public void downButtonWork(Button button, OvenDeviceViewHolder h){
        if(button != null){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OvenDevice d = (OvenDevice) h.device;
                    if(d != null){
                        OvenDeviceState s = d.getState();
                        if(s != null){
                            String actionName = "setTemperature";
                            Integer newTemperature = s.getTemperature();
                            if(newTemperature <= 90){
                                return;
                            }else{
                                newTemperature -= 10;
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

    private void clearHeatSelections(){
        OvenDeviceViewHolder h = (OvenDeviceViewHolder) holder;
        for (int i = 0 ; i<h.HeatBtn.length ; i++){
            if(h.HeatBtn[i] != null){
                Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.HeatBtn[i];
                if(aux.first != null){
                    aux.first.setChecked(false);
                }

            }
        }
    }

    private void clearGrillSelections(){
        OvenDeviceViewHolder h = (OvenDeviceViewHolder) holder;
        for (int i = 0 ; i<h.GrillBtn.length ; i++){
            if(h.GrillBtn[i] != null) {
                Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.GrillBtn[i];
                if(aux.first != null){
                    aux.first.setChecked(false);
                }
            }
        }
    }

    private void clearConvectionSelections(){
        OvenDeviceViewHolder h = (OvenDeviceViewHolder) holder;
        for (int i = 0 ; i<h.ConvectionBtn.length ; i++){
            if(h.ConvectionBtn[i] != null) {
                Pair<ToggleButton,String> aux = (Pair<ToggleButton, String>) h.ConvectionBtn[i];
                if(aux.first != null){
                    aux.first.setChecked(false);
                }
            }
        }
    }

}
