package ar.edu.itba.hci.uzr.intellifox.ui.device;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.ui.device_types.DeviceTypesDevicesViewModel;

public class DeviceFragment extends Fragment {

    static final String DEVICE_ID_ARG = "DEVICE_ID";
    private DeviceViewModel deviceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_device, container, false);
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);

        Bundle bundle = this.getArguments();
        String deviceId = null;
        if (bundle != null) {
            deviceId = bundle.getString(DEVICE_ID_ARG, null);
        }


        return root;
    }


}