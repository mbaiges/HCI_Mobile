package ar.edu.itba.hci.uzr.intellifox.ui.device_types;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.models.Error;
import ar.edu.itba.hci.uzr.intellifox.api.models.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceTypeArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.RoomArrayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceTypesFragment extends Fragment {
    GridView gridView;
    DeviceType[] deviceTypes;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        gridView = root.findViewById(R.id.device_types_grid_view);
        getDeviceTypes(gridView);
        return root;
    }

    private void getDeviceTypes(GridView gridView) {
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Device>>> call, @NonNull Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Device>> result = response.body();
                    //showResult(result != null ? TextUtils.join(",", result.getResult()) : "null");
                    if (result != null) {
                        List<Device> deviceList = result.getResult();
                        deviceTypes = new DeviceType[deviceList.size()];
                        int i = 0;
                        for (Device d : deviceList) {
                            if (!isDeviceTypeInArray(d.getType().getName(), deviceTypes))
                                deviceTypes[i++] = new DeviceType(d.getType().getId(), d.getType().getName());
                        }
                        for (DeviceType d: deviceTypes) {
                            if (d != null)
                                Log.d("DEVICE_TYPE:", d.getId() + "+" + d.getName());
                        }
                        DeviceTypeArrayAdapter adapter = new DeviceTypeArrayAdapter(getActivity(), deviceTypes);
                        gridView.setAdapter(adapter);
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Device>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private Boolean isDeviceTypeInArray(String typeName, DeviceType[] deviceTypes) {
        for(DeviceType dt : deviceTypes) {
            if (dt != null && dt.getName().equals(typeName)) {
                return true;
            }
        }
        return false;
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }
}