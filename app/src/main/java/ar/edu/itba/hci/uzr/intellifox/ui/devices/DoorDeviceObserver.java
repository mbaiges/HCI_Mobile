package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.view.View;

import androidx.lifecycle.Observer;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;

public class DoorDeviceObserver implements Observer<Device> {
    private View contextView;

    public DoorDeviceObserver(View contextView) {
        this.contextView = contextView;
    }

    @Override
    public void onChanged(Device device) {

    }
}
