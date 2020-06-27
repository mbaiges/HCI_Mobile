package ar.edu.itba.hci.uzr.intellifox.ui.device_types;

import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceTypeArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceTypesViewModel extends ViewModel {

    private static final long UPDATE_RATE = 1;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private MutableLiveData<Set<DeviceType>> mDeviceTypes;

    public DeviceTypesViewModel() {
        mDeviceTypes = new MutableLiveData<>();
        fetchDeviceTypes();
    }

    public LiveData<Set<DeviceType>> getDeviceTypes() {
        return mDeviceTypes;
    }

    private void fetchDeviceTypes() {
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Device>>> call, @NonNull Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Device>> result = response.body();
                    if (result != null) {
                        List<Device> devicesList = result.getResult();
                        Set<DeviceType> actualDeviceTypesSet = new HashSet<>();
                        Set<DeviceType> deviceTypesSet = mDeviceTypes.getValue();

                        if (devicesList != null) {
                            for (Device d: result.getResult()) {
                                actualDeviceTypesSet.add(new DeviceType(d.getType().getId(), d.getType().getName()));
                            }

                            if (deviceTypesSet == null || !(deviceTypesSet.equals(actualDeviceTypesSet))) {
                                mDeviceTypes.postValue(actualDeviceTypesSet);
                                /*
                                for (DeviceType dt: actualDeviceTypesSet) {
                                    Log.v("DEVICE_TYPE", dt.getName());
                                }
                                */
                            }
                        }
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

    public void scheduleFetching() {
        final Runnable fetcher = new Runnable() {
            public void run() {
                fetchDeviceTypes();
            }
        };
        fetcherHandler = scheduler.scheduleAtFixedRate(fetcher, UPDATE_RATE, UPDATE_RATE, TimeUnit.SECONDS);
    }

    public void stopFetching() {
        fetcherHandler.cancel(true);
    }

    @Override
    public void onCleared() {
        stopFetching();
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String desc = error.getDescription();
        String code = "Code " + String.valueOf(error.getCode());
        Log.e("ERROR", code + " - " + desc);
        /*
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        */
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }
}