package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.InvocationTargetException;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;

public class DeviceFragment extends Fragment {

    static final String DEVICE_ID_ARG = "DEVICE_ID";
    static final String DEVICE_TYPE_NAME_ARG = "DEVICE_TYPE_NAME";

    private DeviceViewModel deviceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_device, container, false);
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);

        Bundle bundle = this.getArguments();
        String deviceId = null;
        if (bundle != null) {
            deviceId = bundle.getString(DEVICE_ID_ARG, null);
        }
        String deviceTypeName = null;
        if (bundle != null) {
            deviceTypeName = bundle.getString(DEVICE_TYPE_NAME_ARG, null);
        }

        if (deviceTypeName != null) {
            Integer fragmentLayoutId = DeviceObserverViewFactoryContainer.getInstance().getView(deviceTypeName);
            if (fragmentLayoutId != null) {
                root = inflater.inflate(fragmentLayoutId, container, false);
                Observer<Device> o = null;
                try {
                    o = DeviceObserverViewFactoryContainer.getInstance().getObserver(deviceTypeName, root);
                } catch (Exception e) {
                    Log.e("ERROR", e.toString());
                }
                ;
                if (o != null && deviceId != null) {
                    deviceViewModel.getDevice().observe(getViewLifecycleOwner(), o);
                    deviceViewModel.getDevice().observe(getViewLifecycleOwner(), new Observer<Device>() {
                        @Override
                        public void onChanged(Device device) {
                            if (device != null) {
                                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(device.getName());
                            }
                        }
                    });
                    deviceViewModel.init(deviceId);
                }
            }
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        deviceViewModel.scheduleUpdating();
    }

    @Override
    public void onPause() {
        super.onPause();
        deviceViewModel.stopUpdating();
    }
}