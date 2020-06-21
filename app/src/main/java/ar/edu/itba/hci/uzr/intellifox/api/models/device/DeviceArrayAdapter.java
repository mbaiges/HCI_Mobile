package ar.edu.itba.hci.uzr.intellifox.api.models.device;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;

public class DeviceArrayAdapter extends ArrayAdapter<Device> {

    static final String DEVICE_ID_ARG = "DEVICE_ID";
    static final String DEVICE_TYPE_NAME_ARG = "DEVICE_TYPE_NAME";

    public DeviceArrayAdapter(Activity context, Device[] objects) {
        super(context, R.layout.device_card_item, objects);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        DeviceViewHolder<Device<AcDeviceState>> holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_card_item, parent, false);
            holder = new DeviceViewHolder<Device<AcDeviceState>>();
            holder.imageView = convertView.findViewById(R.id.icon);
            holder.nameTextView = convertView.findViewById(R.id.name);
            final ConstraintLayout expandableView = convertView.findViewById(R.id.expandableView);
            final Button arrowBtn = convertView.findViewById(R.id.arrowBtn);
            final CardView cardView = convertView.findViewById(R.id.cardView);
            holder.expandableView = expandableView;
            holder.arrowBtn = arrowBtn;
            holder.cardView = cardView;
            convertView.setTag(holder);
        } else {
            holder = (DeviceViewHolder<Device<AcDeviceState>>) convertView.getTag();
        }

        Device device = getItem(position);
        if (device != null) {
            if (holder.arrowBtn != null) {
                Bundle args = new Bundle();
                args.putString(DEVICE_ID_ARG, device.getId());
                DeviceType dt = device.getType();
                if (dt != null) {
                    args.putString(DEVICE_TYPE_NAME_ARG, device.getType().getName());
                    holder.arrowBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_device, args));
                }
            }
            //ColorFilter filter = new PorterDuffColorFilter(Color.parseColor(device.getColor()), PorterDuff.Mode.SRC_IN);
            //holder.imageView.setColorFilter(filter);
            holder.nameTextView.setText(device.getName());
        }

        return convertView;
    }
}
