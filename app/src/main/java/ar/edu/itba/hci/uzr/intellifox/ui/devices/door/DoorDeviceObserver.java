package ar.edu.itba.hci.uzr.intellifox.ui.devices.door;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.lifecycle.Observer;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;

public class DoorDeviceObserver implements Observer<Device> {
    private View contextView;
    private DoorDeviceViewHolder holder;

    public DoorDeviceObserver(View contextView) {
        this.contextView = contextView;
        this.holder = new DoorDeviceViewHolder();

        findElements();
        attachFunctions();
    }

    @Override
    public void onChanged(Device device) {
        DoorDevice oldDoor = (DoorDevice) holder.device;
        DoorDevice newDoor = (DoorDevice) device;


        holder.device = newDoor;
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
                        Log.v("DOOR", "Turned On");
                    }else{
                        Log.v("DOOR", "Turned Off");
                    }
                }
            });
        }
    }
}
