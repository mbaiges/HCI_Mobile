package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceViewModel extends ViewModel {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private String deviceId;
    private MutableLiveData<Device> mDevice;

    public DeviceViewModel() {
        mDevice = new MutableLiveData<>();
    }

    public void init(String deviceId) {
        this.deviceId = deviceId;
        fetchDevice();
        scheduleFetching();
    }

    public LiveData<Device> getDevice() {
        return mDevice;
    }

    private void fetchDevice() {
        ApiClient.getInstance().getDevice(deviceId, new Callback<Result<Device>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Device>> call, @NonNull Response<Result<Device>> response) {
                if (response.isSuccessful()) {
                    Result<Device> result = response.body();
                    if (result != null) {
                        Device actualDevice = result.getResult();
                        Device device = mDevice.getValue();

                        if (device == null || !device.equals(actualDevice)) {
                            mDevice.postValue(actualDevice);
                            Log.v("DEVICE", actualDevice.toString());
                        }
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void scheduleFetching() {
        final Runnable fetcher = new Runnable() {
            public void run() {
                fetchDevice();
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