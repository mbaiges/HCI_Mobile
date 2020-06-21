package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceTypeArrayAdapter;

public class DevicesFragment extends Fragment {

    GridView gridView;
    private Map<String, Pair<String, Integer>> typeNames;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        gridView = root.findViewById(R.id.device_types_grid_view);

        typeNames = new HashMap<>();
        typeNames.put("faucet", new Pair<>("Tap", R.drawable.ic_device_water_pump));
        typeNames.put("lamp", new Pair<>("Light", R.drawable.ic_device_lightbulb_outline));

        /*
        DeviceType[] values = new DeviceType[]{
                new DeviceType(1, typeNames.get("faucet").first + "s", typeNames.get("faucet").second),
                new DeviceType(2, typeNames.get("lamp").first + "s", typeNames.get("lamp").second),
                new DeviceType(3, typeNames.get("lamp").first + "s", typeNames.get("lamp").second),
                new DeviceType(4, typeNames.get("lamp").first + "s", typeNames.get("lamp").second),
                new DeviceType(5, typeNames.get("lamp").first + "s", typeNames.get("lamp").second)
        };


        DeviceTypeArrayAdapter adapter = new DeviceTypeArrayAdapter(this.getActivity(), values);

        gridView.setAdapter(adapter);
        */
        return root;
    }
}