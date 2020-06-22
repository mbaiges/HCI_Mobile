package ar.edu.itba.hci.uzr.intellifox.ui.room;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import java.util.Set;


import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceArrayAdapter;


public class RoomFragment extends Fragment {

    static final String ROOM_ID_ARG = "room_id";
    RoomViewModel roomViewModel;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_room, container, false);
        listView = root.findViewById(R.id.room_devices_list_view);

        roomViewModel =
                ViewModelProviders.of(this).get(RoomViewModel.class);

        Bundle bundle = this.getArguments();
        String typeName = null;
        if (bundle != null) {
            typeName = bundle.getString(ROOM_ID_ARG, null);
        }

        if (typeName != null) {
            roomViewModel.init(typeName);
        }

        roomViewModel.getDevices().observe(getViewLifecycleOwner(), new Observer<Set<Device>>() {
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

    @Override
    public void onResume() {
        super.onResume();
        roomViewModel.scheduleFetching();
    }

    @Override
    public void onPause() {
        super.onPause();
        roomViewModel.stopFetching();
    }
}