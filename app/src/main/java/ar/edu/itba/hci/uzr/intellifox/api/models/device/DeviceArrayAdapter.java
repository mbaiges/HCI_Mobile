package ar.edu.itba.hci.uzr.intellifox.api.models.device;


import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserverViewFactoryContainer;

public class DeviceArrayAdapter extends ArrayAdapter<Device> {

    static final String DEVICE_ID_ARG = "DEVICE_ID";
    static final String DEVICE_TYPE_NAME_ARG = "DEVICE_TYPE_NAME";

    private Map<String, Integer> typeInfo;

    public DeviceArrayAdapter(Activity context, Device[] objects) {
        super(context, R.layout.device_card_item, objects);

        typeInfo = new HashMap<String, Integer>() {
            {
                put("faucet", R.drawable.ic_device_water_pump);
                put("ac", R.drawable.ic_device_air_conditioner);
                put("alarm", R.drawable.ic_device_alarm_light_outline);
                put("blinds", R.drawable.ic_device_blinds);
                put("door", R.drawable.ic_device_door);
                put("refrigerator", R.drawable.ic_device_fridge_outline);
                put("lamp", R.drawable.ic_device_lightbulb_outline);
                put("vacuum", R.drawable.ic_device_robot_vacuum);
                put("speaker", R.drawable.ic_device_speaker);
                put("oven", R.drawable.ic_device_toaster_oven);
            }
        };
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
            DeviceType dt = device.getType();
            if (holder.arrowBtn != null) {
                Bundle args = new Bundle();
                args.putString(DEVICE_ID_ARG, device.getId());
                if (dt != null) {
                    args.putString(DEVICE_TYPE_NAME_ARG, device.getType().getName());
                    if (holder.arrowBtn != null) {
                        holder.arrowBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_device, args));
                    }
                }
            }

            holder.nameTextView.setText(device.getName());
            if (dt != null) {
                Integer iconRef = typeInfo.get(dt.getName());
                if (iconRef != null) {
                        holder.imageView.setImageResource(iconRef);
                }
                try {
                    holder.observer = DeviceObserverViewFactoryContainer.getInstance().getObserver(dt.getName(), convertView);
                    if (holder.observer != null) {
                        holder.observer.onChanged(device);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return convertView;
    }
}
