package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceViewModel extends ViewModel {

    private HashMap<String, Function<Void, Void>> functionHashMap;
    private Function<Void, Void> deviceUpdater;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private String deviceId;
    private MutableLiveData<Device> mDevice;

    public DeviceViewModel() {
        functionHashMap = new HashMap<String, Function<Void, Void>>() {{
            put("door", (t) -> {
                updateDoorDevice();
                return null;
            });
            put("faucet", (t) -> {
                updateTapDevice();
                return null;
            });
            put("blinds", (t) -> {
                updateBlindDevice();
                return null;
            put("vacuum", (t) -> {
               updateVacuumDevice();
               return null;
            });

        }};
        mDevice = new MutableLiveData<>();
    }

    public void init(String deviceId) {
        this.deviceId = deviceId;
        fetchDevice();
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

    private void updateDevice() {
        if (deviceUpdater == null) {
            Device device = mDevice.getValue();
            if (device != null) {
                DeviceType deviceType = device.getType();
                if (deviceType != null) {
                    Function<Void, Void> updaterFunction = functionHashMap.get(deviceType.getName());
                    if (updaterFunction != null) {
                        deviceUpdater = updaterFunction;
                    }
                }
            }
        }
        if (deviceUpdater != null) {
            deviceUpdater.apply(null);
        }
    }

    private void updateDoorDevice() {
        Log.v("UPDATE_DOOR", "Running");
        ApiClient.getInstance().getDoorDeviceState(deviceId, new Callback<Result<DoorDeviceState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<DoorDeviceState>> call, @NonNull Response<Result<DoorDeviceState>> response) {
                if (response.isSuccessful()) {
                    Result<DoorDeviceState> result = response.body();
                    if (result != null) {
                        DoorDeviceState actualDeviceState = result.getResult();
                        if (actualDeviceState != null) {
                            DoorDevice device = (DoorDevice) mDevice.getValue();

                            if (device != null && (device.getState() == null || !device.getState().equals(actualDeviceState))) {
                                device.setState(actualDeviceState);
                                mDevice.postValue(device);
                                Log.v("UPDATED_DOOR", device.toString());
                            }
                        }
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<DoorDeviceState>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void updateVacuumDevice() {
        Log.v("UPDATE_DOOR", "Running");
        ApiClient.getInstance().getVacuumDeviceState(deviceId, new Callback<Result<VacuumDeviceState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<VacuumDeviceState>> call, @NonNull Response<Result<VacuumDeviceState>> response) {
                if (response.isSuccessful()) {
                    Result<VacuumDeviceState> result = response.body();
                    if (result != null) {
                        VacuumDeviceState actualDeviceState = result.getResult();
                        if (actualDeviceState != null) {
                            VacuumDevice device = (VacuumDevice) mDevice.getValue();

                            if (device != null && (device.getState() == null || !device.getState().equals(actualDeviceState))) {
                                device.setState(actualDeviceState);
                                mDevice.postValue(device);
                                Log.v("UPDATED_DOOR", device.toString());
                            }
                        }
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<VacuumDeviceState>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void scheduleUpdating() {
        final Runnable fetcher = new Runnable() {
            public void run() {
                updateDevice();
            }
        };
        fetcherHandler = scheduler.scheduleAtFixedRate(fetcher, 4, 4, TimeUnit.SECONDS);
    }

    public void stopUpdating() {
        fetcherHandler.cancel(true);
    }

    @Override
    public void onCleared() {
        stopUpdating();
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