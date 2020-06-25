package ar.edu.itba.hci.uzr.intellifox.api.models.device.log;


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

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceViewHolder;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserverViewFactoryContainer;

public class DeviceLogRecordArrayAdapter extends ArrayAdapter<DeviceLogRecord> {

    static final String DEVICE_ID_ARG = "DEVICE_ID";
    static final String DEVICE_TYPE_NAME_ARG = "DEVICE_TYPE_NAME";

    static Map<String, Integer> typeInfo;

    public DeviceLogRecordArrayAdapter(Activity context, DeviceLogRecord[] objects) {
        super(context, R.layout.device_log_record_item, objects);

        if (typeInfo == null) {
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
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        DeviceLogRecordViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_log_record_item, parent, false);
            holder = new DeviceLogRecordViewHolder();
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
            holder = (DeviceLogRecordViewHolder) convertView.getTag();
        }

        DeviceLogRecord logRecord = getItem(position);
        if (logRecord != null) {

        }
        return convertView;
    }
}
