package ar.edu.itba.hci.uzr.intellifox.ui.favourites;

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
import java.util.stream.Collectors;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesViewModel extends ViewModel {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private MutableLiveData<Set<Device>> mDevices;
    private MutableLiveData<Set<Routine>> mRoutines;
    private String roomName;

    public FavouritesViewModel() {
        mDevices = new MutableLiveData<>();
        mRoutines = new MutableLiveData<>();
        fetchRoutines();
        fetchDevices();
    }

    public LiveData<Set<Device>> getDevices() {
        return mDevices;
    }

    private void fetchDevices() {
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Device>>> call, @NonNull Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Device>> result = response.body();
                    if (result != null) {
                        List<Device> comingDevicesList = result.getResult();
                        if (comingDevicesList != null) {
                            Set<Device> actualDevicesSet = comingDevicesList.stream().filter(d -> d.getMeta() != null && d.getMeta().getFavourites() != null && d.getMeta().getFavourites()).collect(Collectors.toSet());
                            Set<Device> devicesSet = mDevices.getValue();

                            if (devicesSet == null || !devicesSet.equals(actualDevicesSet)) {
                                mDevices.postValue(actualDevicesSet);
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


    public LiveData<Set<Routine>> getRoutines() {
        return mRoutines;
    }

    private void fetchRoutines() {
        ApiClient.getInstance().getRoutines(new Callback<Result<List<Routine>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Routine>>> call, @NonNull Response<Result<List<Routine>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Routine>> result = response.body();
                    if (result != null) {
                        List<Routine> comingRoutinesList = result.getResult();
                        if (comingRoutinesList != null) {
                            Set<Routine> actualRoutinesSet = comingRoutinesList.stream().filter(d -> d.getMeta() != null && d.getMeta().getFavourites() != null && d.getMeta().getFavourites()).collect(Collectors.toSet());
                            Set<Routine> routinesSet = mRoutines.getValue();

                            if (routinesSet == null || !(routinesSet.equals(actualRoutinesSet))) {
                                mRoutines.postValue(actualRoutinesSet);
                            }
                        }
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Routine>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void scheduleFetching() {
        final Runnable fetcher = new Runnable() {
            public void run() {
                fetchDevices();
                fetchRoutines();
            }
        };
        fetcherHandler = scheduler.scheduleAtFixedRate(fetcher, 4, 4, TimeUnit.SECONDS);
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