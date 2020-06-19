package ar.edu.itba.hci.uzr.intellifox.ui.device_types;

import android.os.Bundle;
import android.util.Log;

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

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.models.Error;
import ar.edu.itba.hci.uzr.intellifox.api.models.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceTypesDevicesViewModel extends ViewModel {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private MutableLiveData<Set<Device>> mDevices;

    public DeviceTypesDevicesViewModel() {
        mDevices = new MutableLiveData<>();
        fetchDeviceTypes();
        scheduleFetching();
    }

    public LiveData<Set<Device>> getDevices() {
        return mDevices;
    }

    private void fetchDeviceTypes() {
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Device>>> call, @NonNull Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Device>> result = response.body();
                    if (result != null) {
                        Set<Device> devicesList = new HashSet<>(result.getResult());
                        Set<Device> actualDevicesList = mDevices.getValue();

                        if (actualDevicesList == null || actualDevicesList.equals(devicesList)) {
                            mDevices.postValue(devicesList);
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

    public void scheduleFetching() {
        final Runnable fetcher = new Runnable() {
            public void run() {
                fetchDeviceTypes();
            }
        };
        fetcherHandler = scheduler.scheduleAtFixedRate(fetcher, 4, 4, TimeUnit.SECONDS);
    }

    @Override
    public void onCleared() {
        fetcherHandler.cancel(true);
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
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