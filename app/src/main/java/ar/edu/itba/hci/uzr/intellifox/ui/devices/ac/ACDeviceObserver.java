package ar.edu.itba.hci.uzr.intellifox.ui.devices.ac;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.lifecycle.Observer;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDevice;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceViewHolder;

public class ACDeviceObserver implements Observer<Device> {
    private View contextView;
    private ACDeviceViewHolder holder;

    public ACDeviceObserver(View contextView) {
        this.contextView = contextView;
        this.holder = new ACDeviceViewHolder();

        findElements();
        attachFunctions();
    };

    @Override
    public void onChanged(Device device) {
        AcDevice oldAc = holder.device;
        AcDevice newAc = (AcDevice) device;


        holder.device = newAc;
    }

    private void findElements() {
        holder.onSwitch = contextView.findViewById(R.id.switch1);
    }

    private void attachFunctions() {
        if (holder.onSwitch != null) {
            holder.onSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                    if(isChecked){
                        Log.v("AC", "Turned On");
                    }else{
                        Log.v("AC", "Turned Off");
                    }
                }
            });
        }
    }
}
