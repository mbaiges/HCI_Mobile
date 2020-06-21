package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.widget.Switch;
import android.widget.TextView;

public interface DeviceViewHolderInterface {

    public Switch getOnSwitch();

    public void setOnSwitch(Switch onSwitch);

    public TextView getDescription();

    public void setDescription(TextView description);
}
