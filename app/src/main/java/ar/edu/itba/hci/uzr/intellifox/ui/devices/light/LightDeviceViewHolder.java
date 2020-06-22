package ar.edu.itba.hci.uzr.intellifox.ui.devices.light;

import android.widget.ImageView;

import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceViewHolder;

public class LightDeviceViewHolder extends DeviceViewHolder {
    ColorPickerView colorPickerView;
    BrightnessSlideBar brightnessSlideBar;
    ImageView lightIcon;
}
