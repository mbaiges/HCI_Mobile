package ar.edu.itba.hci.uzr.intellifox.api.models.device_type;


import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;

public class DeviceTypeArrayAdapter extends ArrayAdapter<DeviceType> {
    private final String DEVICE_TYPE_NAME = "device_type_name";
    private Map<String, Pair<Integer, Integer>> typeInfo;

    public DeviceTypeArrayAdapter(Activity context, DeviceType[] objects) {
        super(context, R.layout.device_type_card_item, objects);

        typeInfo = new HashMap<String, Pair<Integer, Integer>>() {
            {
                put("faucet", new Pair<>(R.string.dev_faucet, R.drawable.ic_device_water_pump));
                put("ac", new Pair<>(R.string.dev_ac, R.drawable.ic_device_air_conditioner));
                put("alarm", new Pair<>(R.string.dev_alarm, R.drawable.ic_device_alarm_light_outline));
                put("blinds", new Pair<>(R.string.dev_blinds, R.drawable.ic_device_blinds));
                put("door", new Pair<>(R.string.dev_door, R.drawable.ic_device_door));
                put("refrigerator", new Pair<>(R.string.dev_refrigerator, R.drawable.ic_device_fridge_outline));
                put("lamp", new Pair<>(R.string.dev_lamp, R.drawable.ic_device_lightbulb_outline));
                put("vacuum", new Pair<>(R.string.dev_vacuum, R.drawable.ic_device_robot_vacuum));
                put("speaker", new Pair<>(R.string.dev_speaker, R.drawable.ic_device_speaker));
                put("oven", new Pair<>(R.string.dev_oven, R.drawable.ic_device_toaster_oven));
            }
        };
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        DeviceTypeViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_type_card_item, parent, false);
            holder = new DeviceTypeViewHolder();
            holder.card = convertView.findViewById(R.id.card);
            holder.imageView = convertView.findViewById(R.id.icon);
            holder.nameTextView = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (DeviceTypeViewHolder) convertView.getTag();
        }

        DeviceType deviceType = getItem(position);
        if (deviceType != null) {
            Bundle args = new Bundle();
            args.putString(DEVICE_TYPE_NAME, deviceType.getName());
            Pair<Integer, Integer> p = typeInfo.get(deviceType.getName());
            String devTitle = "";
            if (p != null) {
                if (p.first != null) {
                    devTitle = convertView.getResources().getString(p.first);
                }
            }
            args.putString(DEVICE_TYPE_TITLE, devTitle);
            if (holder.card != null) {
                holder.card.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_device_types_devices, args));
            }
            Pair<Integer, Integer> info = typeInfo.get(deviceType.getName());
            if (info != null) {
                if (info.second != null) {
                    holder.imageView.setImageResource(info.second);
                    holder.imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.background1));
                }
                holder.nameTextView.setText(info.first);
            }
        }
        return convertView;
    }
}
