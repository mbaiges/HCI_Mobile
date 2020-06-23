package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class DeviceViewHolder {
    public Device<? extends DeviceState> device;
    public ImageView bell;
    public ImageView icon;
    public TextView description;
    public ImageView favourite;
    public Switch onSwitch;
}
