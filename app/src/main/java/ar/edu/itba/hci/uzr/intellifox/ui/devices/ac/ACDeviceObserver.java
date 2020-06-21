package ar.edu.itba.hci.uzr.intellifox.ui.devices.ac;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;

import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceViewHolder;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.door.DoorDeviceViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ACDeviceObserver extends DeviceObserver {

    public ACDeviceObserver(View contextView) {
        super(contextView);
    }

    @Override
    protected void createHolder() {
        this.holder = new ACDeviceViewHolder();
    }

    @Override
    protected void deviceChanged(Device<? extends DeviceState> newDevice) {
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        // To implement on each device
    }

    @Override
    protected void findElements() {
        super.findElements();
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;


    }

    @Override
    protected void setDescription(DeviceState state) {
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        // To implement on each Device
    }

    @Override
    protected void setUI(DeviceState state) {
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        // To implement on each Detailed Info Device
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        ACDeviceViewHolder h = (ACDeviceViewHolder) holder;
        //
    }
}

