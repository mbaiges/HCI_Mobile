package ar.edu.itba.hci.uzr.intellifox.ui.device_types;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.itba.hci.uzr.intellifox.MainActivity;
import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceTypeArrayAdapter;

public class DeviceTypesDevicesFragment extends Fragment {

    private final String DEVICE_TYPE_NAME = "device_type";
    DeviceTypesDevicesViewModel deviceTypesDevicesViewModel;
    ListView listView;
    String type_name;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_device_types_devices, container, false);
        listView = root.findViewById(R.id.device_types_devices_list_view);

        Bundle bundle = this.getArguments();
        String type_name_arg = null;
        if (bundle != null) {
            type_name_arg = bundle.getString(DEVICE_TYPE_NAME, null);
        }

        type_name = type_name_arg;

        deviceTypesDevicesViewModel =
                ViewModelProviders.of(this).get(DeviceTypesDevicesViewModel.class);

        deviceTypesDevicesViewModel.getDevices().observe(getViewLifecycleOwner(), new Observer<Set<Device>>() {
            @Override
            public void onChanged(@Nullable Set<Device> deviceTypesDevices) {
                List<Device> deviceTypesDevicesList = deviceTypesDevices.stream().filter(d -> d.getType().getName().equals(type_name)).collect(Collectors.toList());
                Device[] deviceTypesDevicesArray = new Device[deviceTypesDevicesList.size()];
                int i = 0;
                for (Device r : deviceTypesDevicesList) {
                    Log.v("DEVICE",r.getName());
                    deviceTypesDevicesArray[i++] = r;
                }
                DeviceArrayAdapter adapter = new DeviceArrayAdapter(getActivity(), deviceTypesDevicesArray);
                listView.setAdapter(adapter);
            }
        });

        return root;
    }
}