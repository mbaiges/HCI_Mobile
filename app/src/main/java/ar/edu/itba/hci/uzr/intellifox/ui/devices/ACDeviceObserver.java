package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.view.View;

import androidx.lifecycle.Observer;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDevice;

public class ACDeviceObserver implements Observer<Device> {
    private View contextView;
    private ACViewHolder acViewHolder;

    public ACDeviceObserver(View contextView) {
        this.contextView = contextView;
        this.acViewHolder = new ACViewHolder();
    };

    @Override
    public void onChanged(Device device) {
        AcDevice ac = (AcDevice) device;


    }
}
