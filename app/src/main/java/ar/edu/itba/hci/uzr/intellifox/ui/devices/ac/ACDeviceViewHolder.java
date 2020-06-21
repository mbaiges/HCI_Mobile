package ar.edu.itba.hci.uzr.intellifox.ui.devices.ac;

import android.util.Pair;
import android.widget.Button;

import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceViewHolder;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDevice;

public class ACDeviceViewHolder extends DeviceViewHolder {
    public Button tempDecBtn;
    public Button tempIncBtn;


    public Pair<Button,String>[] ModeBtn;

    public Button coolModeBtn;
    public Button heatModeBtn;
    public Button fanModeBtn;


    public Pair<Button,String>[] VBladesBtn;

    public Button VBladesBtn0;
    public Button VBladesBtn1;
    public Button VBladesBtn2;
    public Button VBladesBtn3;
    public Button VBladesBtn4;


    public Pair<Button,String>[] HBladesBtn;

    public Button HBladesBtn0;
    public Button HBladesBtn1;
    public Button HBladesBtn2;
    public Button HBladesBtn3;
    public Button HBladesBtn4;
    public Button HBladesBtn5;


    public Pair<Button,String>[] FanSpeedBtn;

    public Button FanSpeedBtn0;
    public Button FanSpeedBtn1;
    public Button FanSpeedBtn2;
    public Button FanSpeedBtn3;
    public Button FanSpeedBtn4;


}
