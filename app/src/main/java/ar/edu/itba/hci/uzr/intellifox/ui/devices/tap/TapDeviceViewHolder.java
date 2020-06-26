package ar.edu.itba.hci.uzr.intellifox.ui.devices.tap;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceViewHolder;

public class TapDeviceViewHolder extends DeviceViewHolder {
    public ToggleButton btnML;
    public ToggleButton btnCL;
    public ToggleButton btnDL;
    public ToggleButton btnL;
    public ToggleButton btnDAL;
    public ToggleButton btnHL;
    public ToggleButton btnKL;
    public EditText amount;
    public Button btnDispence;
    public TextView txtDispensing;
    public ProgressBar progBarLoading;
    public ProgressBar progBarDispensing;
}
