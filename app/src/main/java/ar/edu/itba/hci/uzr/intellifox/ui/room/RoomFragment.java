package ar.edu.itba.hci.uzr.intellifox.ui.room;

import android.content.res.Configuration;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import java.util.Set;


import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceArrayAdapter;


public class RoomFragment extends Fragment {

    static final String ROOM_ID_ARG = "room_id";
    static final String ROOM_NAME_ARG = "room_name";
    RoomViewModel roomViewModel;
    View listView;

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
        String roomName = null;
        if (bundle != null) {
            roomName = bundle.getString(ROOM_NAME_ARG, null);
        }

        if (typeName != null) {
            roomViewModel.init(typeName);
        }
        if (roomName != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(roomName);
        }

        roomViewModel.getDevices().observe(getViewLifecycleOwner(), new Observer<Set<Device>>() {
            @Override
            public void onChanged(@Nullable Set<Device> devices) {
                if (devices != null) {
                    Device[] devicesArray = new Device[devices.size()];
                    int i = 0;
                    for (Device r : devices) {
                        devicesArray[i++] = r;
                    }
                    DeviceArrayAdapter adapter = new DeviceArrayAdapter(getActivity(), devicesArray);
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        // In landscape
                        ((GridView) listView).setAdapter(adapter);
                    } else {
                        // In portrait
                        ((ListView) listView).setAdapter(adapter);
                    }
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