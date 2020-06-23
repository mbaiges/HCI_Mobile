package ar.edu.itba.hci.uzr.intellifox.ui.room;

import android.content.res.Configuration;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import java.util.HashSet;
import java.util.Set;


import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceViewHolder;


public class RoomFragment extends Fragment {

    static final String ROOM_ID_ARG = "room_id";
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

        if (typeName != null) {
            roomViewModel.init(typeName);
        }

        roomViewModel.getDevices().observe(getViewLifecycleOwner(), new Observer<Set<Device>>() {
            @Override
            public void onChanged(@Nullable Set<Device> devices) {
                if (devices != null) {
                    boolean removedOrAdded = false;
                    boolean changed = true;
                    if (listView.getClass() == GridView.class &&  ((GridView) listView).getAdapter() != null && ((GridView) listView).getAdapter().getCount() != 0  ) {
                        Log.v("GRIDVIEW", "Update");
                        DeviceArrayAdapter a = (DeviceArrayAdapter) ((GridView) listView).getAdapter();
                        for(int i = 0; i < a.getCount(); i++) {
                            Log.v("DENTRO_DEL_FOR", "Entre");
                            if (!devices.contains(a.getItem(i))){
                                removedOrAdded = true;
                                break;
                            }
                        }
                        if (!removedOrAdded) {
                            for(Device d : devices) {
                                int pos = findDeviceAtAdapter(d.getId(), a);
                                if (pos < 0) {
                                    removedOrAdded = true;
                                    break;
                                }
                                else {
                                    boolean somethingChanged = false;
                                    Device lastDevice = (Device) a.getItem(pos);
                                    if ((lastDevice.getName() != null && !lastDevice.getName().equals(d.getName())) || (lastDevice.getName() == null && d.getName() != null)) {
                                        lastDevice.setName(d.getName());
                                        somethingChanged = true;
                                    }
                                    if ((lastDevice.getMeta() != null && !lastDevice.getMeta().equals(d.getMeta())) || (lastDevice.getMeta() == null && d.getMeta() != null)) {
                                        lastDevice.setMeta(d.getMeta());
                                        somethingChanged = true;
                                    }
                                    if ((lastDevice.getRoom() != null && !lastDevice.getRoom().equals(d.getRoom())) || (lastDevice.getRoom() == null && d.getRoom() != null)) {
                                        lastDevice.setRoom(d.getRoom());
                                        somethingChanged = true;
                                    }
                                    if ((lastDevice.getState() != null && !lastDevice.getState().equals(d.getState())) || (lastDevice.getState() == null && d.getState() != null)) {
                                        lastDevice.setState(d.getState());
                                        somethingChanged = true;
                                    }
                                    if (somethingChanged) {
                                        Log.d("SOMETHING_CHANGED", "Indeed");
                                        DeviceViewHolder dvh = (DeviceViewHolder) a.holders[pos];
                                        if (dvh != null) {
                                            if (dvh.observer != null) {
                                                dvh.observer.onChanged(lastDevice);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if (listView.getClass() == ListView.class && ((ListView) listView).getAdapter() != null && ((ListView) listView).getAdapter().getCount() != 0 ) {
                        Log.v("LISTVIEW", "Update");
                        DeviceArrayAdapter a = (DeviceArrayAdapter) ((ListView) listView).getAdapter();
                        int size = a.getCount();
                        Log.v("LISTVIEW_SIZE", String.valueOf(size));
                        Set<Device> toAdd = new HashSet<>();
                        for (int i = 0; i < size; i++) {
                            Log.v("DENTRO_DEL_FOR", "Entre");
                            Device dev = (Device) a.getItem(i);
                            if (dev != null) {
                                int pos = findDeviceAtSet(dev.getId(), devices);
                                if (pos < 0) {
                                    removedOrAdded = true;
                                    break;
                                }
                            }
                        }
                        if (!removedOrAdded) {
                            for (Device d : devices) {
                                int pos = findDeviceAtAdapter(d.getId(), a);
                                if (pos < 0) {
                                    removedOrAdded = true;
                                    break;
                                } else {
                                    boolean somethingChanged = false;
                                    Device lastDevice = (Device) a.getItem(pos);
                                    if ((lastDevice.getName() != null && !lastDevice.getName().equals(d.getName())) || (lastDevice.getName() == null && d.getName() != null)) {
                                        lastDevice.setName(d.getName());
                                        somethingChanged = true;
                                    }
                                    if ((lastDevice.getMeta() != null && !lastDevice.getMeta().equals(d.getMeta())) || (lastDevice.getMeta() == null && d.getMeta() != null)) {
                                        lastDevice.setMeta(d.getMeta());
                                        somethingChanged = true;
                                    }
                                    if ((lastDevice.getRoom() != null && !lastDevice.getRoom().equals(d.getRoom())) || (lastDevice.getRoom() == null && d.getRoom() != null)) {
                                        lastDevice.setRoom(d.getRoom());
                                        somethingChanged = true;
                                    }
                                    if ((lastDevice.getState() != null && !lastDevice.getState().equals(d.getState())) || (lastDevice.getState() == null && d.getState() != null)) {
                                        lastDevice.setState(d.getState());
                                        somethingChanged = true;
                                    }
                                    if (somethingChanged) {
                                        Log.d("SOMETHING_CHANGED", "Indeed");
                                        DeviceViewHolder dvh = (DeviceViewHolder) a.holders[pos];
                                        if (dvh != null) {
                                            if (dvh.observer != null) {
                                                dvh.observer.onChanged(lastDevice);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        changed = false;
                    }

                    if (!changed || removedOrAdded) {
                        Device[] deviceTypesDevicesArray = new Device[devices.size()];
                        int i = 0;
                        for (Device d : devices) {
                            deviceTypesDevicesArray[i++] = d;
                        }
                        DeviceArrayAdapter adapter = new DeviceArrayAdapter(getActivity(), deviceTypesDevicesArray);
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
            }
        });

        return root;
    }

    private int findDeviceAtSet(String id, Set<Device> deviceTypesDevices) {
        int i = 0;
        if (deviceTypesDevices != null) {
            for (Device d: deviceTypesDevices) {
                if (d != null && d.getId().equals(id)) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    private int findDeviceAtAdapter(String id, ListAdapter a) {
        int i;
        for(i = 0; i < a.getCount(); i++) {
            Device d = (Device) a.getItem(i);
            if (d != null && d.getId().equals(id)) {
                return i;
            }
        }
        return -1;
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