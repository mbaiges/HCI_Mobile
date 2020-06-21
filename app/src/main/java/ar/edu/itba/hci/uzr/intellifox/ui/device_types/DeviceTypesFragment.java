package ar.edu.itba.hci.uzr.intellifox.ui.device_types;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;
import java.util.Set;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceTypeArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.RoomArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.ui.rooms.RoomsViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceTypesFragment extends Fragment {

    DeviceTypesViewModel deviceTypesViewModel;
    GridView gridView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_devices, container, false);

        deviceTypesViewModel =
                ViewModelProviders.of(this).get(DeviceTypesViewModel.class);

        gridView = root.findViewById(R.id.device_types_grid_view);

        deviceTypesViewModel.getDeviceTypes().observe(getViewLifecycleOwner(), new Observer<Set<DeviceType>>() {
            @Override
            public void onChanged(@Nullable Set<DeviceType> deviceTypes) {
                if (deviceTypes != null) {
                    DeviceType[] deviceTypesArray = new DeviceType[deviceTypes.size()];
                    int i = 0;
                    for (DeviceType r : deviceTypes) {
                        deviceTypesArray[i++] = r;
                    }
                    DeviceTypeArrayAdapter adapter = new DeviceTypeArrayAdapter(getActivity(), deviceTypesArray);
                    gridView.setAdapter(adapter);
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        deviceTypesViewModel.scheduleFetching();
    }

    @Override
    public void onPause() {
        super.onPause();
        deviceTypesViewModel.stopFetching();
    }
}