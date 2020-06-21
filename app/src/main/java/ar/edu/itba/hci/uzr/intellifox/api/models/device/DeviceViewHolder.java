package ar.edu.itba.hci.uzr.intellifox.api.models.device;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;

import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;

public class DeviceViewHolder<A extends Device<AcDeviceState>> {
    public ConstraintLayout expandableView;
    public Button arrowBtn;
    public CardView cardView;
    public ImageView imageView;
    public TextView nameTextView;
    public Observer<Device> observer;
}
