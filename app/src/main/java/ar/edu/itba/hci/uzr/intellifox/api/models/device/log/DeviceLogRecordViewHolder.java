package ar.edu.itba.hci.uzr.intellifox.api.models.device.log;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;

public class DeviceLogRecordViewHolder {
    public ImageView iconView;
    public TextView deviceNameTextView;
    public TextView actionNameTextView;
    public TextView timestampTextView;
    public TextView paramsTextView;

    public String actionName;
}
