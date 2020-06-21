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
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class DeviceTypesDevicesFragment extends Fragment {

    static final String DEVICE_TYPE_NAME = "device_type_name";
    DeviceTypesDevicesViewModel deviceTypesDevicesViewModel;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_device_types_devices, container, false);
        listView = root.findViewById(R.id.device_types_devices_list_view);

        deviceTypesDevicesViewModel =
                ViewModelProviders.of(this).get(DeviceTypesDevicesViewModel.class);

        Bundle bundle = this.getArguments();
        String typeName = null;
        if (bundle != null) {
            typeName = bundle.getString(DEVICE_TYPE_NAME, null);
        }

        if (typeName != null) {
            deviceTypesDevicesViewModel.init(typeName);
        }

        deviceTypesDevicesViewModel.getDevices().observe(getViewLifecycleOwner(), new Observer<Set<Device>>() {
            @Override
            public void onChanged(@Nullable Set<Device> deviceTypesDevices) {
                if (deviceTypesDevices != null) {
                    Device[] deviceTypesDevicesArray = new Device[deviceTypesDevices.size()];
                    int i = 0;
                    for (Device r : deviceTypesDevices) {
                        deviceTypesDevicesArray[i++] = r;
                    }
                    DeviceArrayAdapter adapter = new DeviceArrayAdapter(getActivity(), deviceTypesDevicesArray);
                    listView.setAdapter(adapter);
                }
            }
        });

        return root;
    }
}