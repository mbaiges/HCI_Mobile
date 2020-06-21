package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.widget.Switch;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public abstract class DeviceViewHolder<T extends Device<? extends DeviceState>> {
    public T device;
    public Switch onSwitch;
}
