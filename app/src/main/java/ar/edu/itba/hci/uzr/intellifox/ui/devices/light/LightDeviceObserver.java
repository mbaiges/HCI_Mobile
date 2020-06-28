package ar.edu.itba.hci.uzr.intellifox.ui.devices.light;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.skydoves.colorpickerview.ActionMode;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
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

        h.colorPickerView = contextView.findViewById(R.id.colorPickerView);
        if (h.colorPickerView != null) {
            h.colorPickerView.setPreferenceName("LightColorPicker");
            h.brightnessSlideBar = contextView.findViewById(R.id.brightnessSlide);
            if (h.brightnessSlideBar != null) {
                h.colorPickerView.attachBrightnessSlider(h.brightnessSlideBar);
            }
        }
        h.lightIcon = contextView.findViewById(R.id.lightIcon);




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
                if (h.onSwitch != null) {
                    h.onSwitch.setChecked(status.equals("on"));
                }
            }
            String color = s.getColor();
            if(color.substring(0,1).equals("#")){
                color = color.substring(1);
            }

            String auxColor = "#FF" + color;
            if (color != null && h.colorPickerView != null) {
                ColorPickerPreferenceManager manager = ColorPickerPreferenceManager.getInstance(h.colorPickerView.getContext());
                //Log.v("LIGHT_COLOR", color);
                //Log.v("LIGHT_COLOR_AUX", auxColor);

                manager.setColor("LightColorPicker", Color.parseColor(auxColor)); // manipulates the saved color data.
                if (h.lightIcon != null) {
                    h.lightIcon.setColorFilter(Color.parseColor(auxColor));
                }
            }
        }
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        LightDeviceViewHolder h = (LightDeviceViewHolder) holder;

        if (h.colorPickerView != null) {
            h.colorPickerView.setColorListener(new ColorEnvelopeListener() {
                @Override
                public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                    //Log.v("COLOR_PICKER", envelope.getHexCode());
                    String[] newColor = {envelope.getHexCode().substring(2)};
                    //Log.v("COLOR_PICKER_TO_API", newColor[0]);

                    LightDevice d = (LightDevice) h.device;
                    if(d != null){
                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setColor", newColor, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                                if(response.isSuccessful()){
                                    Result<Object> result = response.body();
                                    if(result != null){
                                        if (h.lightIcon != null) {
                                            String auxColor = "#FF" + newColor[0];
                                            h.lightIcon.setColorFilter(Color.parseColor(auxColor));
                                            String text = contextView.getResources().getString(R.string.notif_light_changed_color)  + ".";
                                            Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                            View sbView = snackbar.getView();
                                            sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                            snackbar.show();
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<Result<Object>> call, Throwable t) {
                                handleUnexpectedError(t);
                            }
                        });

                        String[] auxBrightness = {"255"};
                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setBrightness", auxBrightness , new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                                if(response.isSuccessful()){
                                    Result<Object> result = response.body();
                                    if(result != null){
                                        String text = contextView.getResources().getString(R.string.notif_light_changed_color)  + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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
            });
            h.colorPickerView.setActionMode(ActionMode.LAST);
        }
    }

    @Override
    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"turnOn":"turnOff";
    }

    @Override
    protected int getIconColor(Boolean turnedOn, DeviceState state) {
        //Log.v("LIGHT_COLOR", "ASKED FOR LIGHT COLOR");
        if (turnedOn && state != null) {
            LightDeviceState s = (LightDeviceState) state;
            //Log.v("LIGHT_COLOR", s.getColor());
            String color = s.getColor();
            if(color.substring(0,1).equals("#")){
                color = color.substring(1);
            }
            return Color.parseColor("#FF" + color);
        }
        return super.getIconColor(turnedOn,state);
    }
}
