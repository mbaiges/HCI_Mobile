package ar.edu.itba.hci.uzr.intellifox.api.models.device_type;


import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;

public class DeviceTypeArrayAdapter extends ArrayAdapter<DeviceType> {
    private Map<String, Pair<String, Integer>> typeInfo;

    public DeviceTypeArrayAdapter(Activity context, DeviceType[] objects) {
        super(context, R.layout.device_type_card_item, objects);

        typeInfo = new HashMap<String, Pair<String, Integer>>() {
            {
                put("faucet", new Pair<>("Tap", R.drawable.ic_device_water_pump));
                put("ac", new Pair<>("Air Conditioner", R.drawable.ic_device_air_conditioner));
                put("alarm", new Pair<>("Alarm", R.drawable.ic_device_alarm_light_outline));
                put("blinds", new Pair<>("Blind", R.drawable.ic_device_blinds));
                put("door", new Pair<>("Door", R.drawable.ic_device_door));
                put("refrigerator", new Pair<>("Fridge", R.drawable.ic_device_fridge_outline));
                put("lamp", new Pair<>("Light", R.drawable.ic_device_lightbulb_outline));
                put("vacuum", new Pair<>("Vacuum", R.drawable.ic_device_robot_vacuum));
                put("speaker", new Pair<>("Speaker", R.drawable.ic_device_speaker));
                put("oven", new Pair<>("Oven", R.drawable.ic_device_toaster_oven));
            }

        };
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        DeviceTypeViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_type_card_item, parent, false);
            holder = new DeviceTypeViewHolder();
            holder.imageView = convertView.findViewById(R.id.icon);
            holder.nameTextView = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (DeviceTypeViewHolder) convertView.getTag();
        }

        DeviceType deviceType = getItem(position);
        if (deviceType != null) {
            Pair<String, Integer> info = typeInfo.get(deviceType.getName());
            if (info != null) {
                if (info.second != null) {
                    holder.imageView.setImageResource(info.second);
                    holder.imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.background1));
                }
                String aux = info.first + "s";
                holder.nameTextView.setText(aux);
            }
        }
        return convertView;
    }
}
