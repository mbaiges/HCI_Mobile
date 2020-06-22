package ar.edu.itba.hci.uzr.intellifox.ui.devices.ac;

import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceViewHolder;

public class ACDeviceViewHolder extends DeviceViewHolder {
    public Button tempDecBtn;
    public Button tempIncBtn;

    public TextView temperatureValue;

    public Pair<ToggleButton,String>[] ModeBtn;

    public Pair<ToggleButton,String>[] VBladesBtn;

    public Pair<ToggleButton,String>[] HBladesBtn;

    public Pair<ToggleButton,String>[] FanSpeedBtn;


}
