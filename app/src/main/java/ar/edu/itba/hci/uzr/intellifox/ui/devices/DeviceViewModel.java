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
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDeviceState;
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
            });
            put("ac", (t) -> {
                updateACDevice();
                return null;
            });
            put("oven", (t) -> {
                updateOvenDevice();
                return null;
            });
            put("vacuum", (t) -> {
                updateVacuumDevice();
                return null;
            });
<<<<<<< HEAD
            put("light", (t) -> {
                updateLightDevice();
                return null;
=======
            put("speaker", (t) -> {
               updateSpeakerDevice();
               return null;
>>>>>>> c729bbb0dcb0bde8ac7d5d93f1dfea8bf24ba451
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
        Log.v("UPDATE_VACUUM", "Running");
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
                                Log.v("UPDATED_VACUUM", device.toString());
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

    private void updateTapDevice() {
        Log.v("UPDATE_DOOR", "Running");
        ApiClient.getInstance().getTapDeviceState(deviceId, new Callback<Result<TapDeviceState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<TapDeviceState>> call, @NonNull Response<Result<TapDeviceState>> response) {
                if (response.isSuccessful()) {
                    Result<TapDeviceState> result = response.body();
                    if (result != null) {
                        TapDeviceState actualDeviceState = result.getResult();
                        if (actualDeviceState != null) {
                            TapDevice device = (TapDevice) mDevice.getValue();

                            if (device != null && (device.getState() == null || !device.getState().equals(actualDeviceState))) {
                                device.setState(actualDeviceState);
                                mDevice.postValue(device);
                                Log.v("UPDATED_TAP", device.toString());
                            }
                        }
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<TapDeviceState>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void updateBlindDevice() {
        Log.v("UPDATE_DOOR", "Running");
        ApiClient.getInstance().getBlindDeviceState(deviceId, new Callback<Result<BlindDeviceState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<BlindDeviceState>> call, @NonNull Response<Result<BlindDeviceState>> response) {
                if (response.isSuccessful()) {
                    Result<BlindDeviceState> result = response.body();
                    if (result != null) {
                        BlindDeviceState actualDeviceState = result.getResult();
                        if (actualDeviceState != null) {
                            BlindDevice device = (BlindDevice) mDevice.getValue();

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
            public void onFailure(@NonNull Call<Result<BlindDeviceState>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void updateLightDevice() {
        Log.v("UPDATE_LIGHT", "Running");
        ApiClient.getInstance().getLightDeviceState(deviceId, new Callback<Result<LightDeviceState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<LightDeviceState>> call, @NonNull Response<Result<LightDeviceState>> response) {
                if (response.isSuccessful()) {
                    Result<LightDeviceState> result = response.body();
                    if (result != null) {
                        LightDeviceState actualDeviceState = result.getResult();
                        if (actualDeviceState != null) {
                            LightDevice device = (LightDevice) mDevice.getValue();

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
            public void onFailure(@NonNull Call<Result<LightDeviceState>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }


    private void updateSpeakerDevice() {
        Log.v("UPDATE_DOOR", "Running");
        ApiClient.getInstance().getSpeakerDeviceState(deviceId, new Callback<Result<SpeakerDeviceState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<SpeakerDeviceState>> call, @NonNull Response<Result<SpeakerDeviceState>> response) {
                if (response.isSuccessful()) {
                    Result<SpeakerDeviceState> result = response.body();
                    if (result != null) {
                        SpeakerDeviceState actualDeviceState = result.getResult();
                        if (actualDeviceState != null) {
                            SpeakerDevice device = (SpeakerDevice) mDevice.getValue();

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
            public void onFailure(@NonNull Call<Result<SpeakerDeviceState>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void updateACDevice() {
        Log.v("UPDATE_AC", "Running");
        ApiClient.getInstance().getAcDeviceState(deviceId, new Callback<Result<AcDeviceState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<AcDeviceState>> call, @NonNull Response<Result<AcDeviceState>> response) {
                if (response.isSuccessful()) {
                    Result<AcDeviceState> result = response.body();
                    if (result != null) {
                        AcDeviceState actualDeviceState = result.getResult();
                        if (actualDeviceState != null) {
                            AcDevice device = (AcDevice) mDevice.getValue();

                            if (device != null && (device.getState() == null || !device.getState().equals(actualDeviceState))) {
                                device.setState(actualDeviceState);
                                mDevice.postValue(device);
                                Log.v("UPDATED_AC", device.toString());
                            }
                        }
                    } else {
                        handleError(response);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Result<AcDeviceState>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void updateOvenDevice() {
        Log.v("UPDATE_OVEN", "Running");
        ApiClient.getInstance().getOvenDeviceState(deviceId, new Callback<Result<OvenDeviceState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<OvenDeviceState>> call, @NonNull Response<Result<OvenDeviceState>> response) {
                if (response.isSuccessful()) {
                    Result<OvenDeviceState> result = response.body();
                    if (result != null) {
                        OvenDeviceState actualDeviceState = result.getResult();
                        if (actualDeviceState != null) {
                            OvenDevice device = (OvenDevice) mDevice.getValue();

                            if (device != null && (device.getState() == null || !device.getState().equals(actualDeviceState))) {
                                device.setState(actualDeviceState);
                                mDevice.postValue(device);
                                Log.v("UPDATED_OVEN", device.toString());
                            }
                        }
                    } else {
                        handleError(response);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Result<OvenDeviceState>> call, @NonNull Throwable t) {
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
