package ar.edu.itba.hci.uzr.intellifox.api.models.device_type;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import ar.edu.itba.hci.uzr.intellifox.R;

public class DeviceTypeArrayAdapter extends ArrayAdapter<DeviceType> {
    public DeviceTypeArrayAdapter(Activity context, DeviceType[] objects) {
        super(context, R.layout.device_type_card_item, objects);
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

        DeviceType room = getItem(position);
        if (room != null) {
            holder.imageView.setImageResource(room.getIcon());
            holder.imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.background1));
            holder.nameTextView.setText(room.getName());
        }

        return convertView;
    }
}
