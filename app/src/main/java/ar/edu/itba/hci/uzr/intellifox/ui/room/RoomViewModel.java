package ar.edu.itba.hci.uzr.intellifox.ui.room;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedHashSet;
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

import ar.edu.itba.hci.uzr.intellifox.api.models.device.MinimumComparableDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomViewModel extends ViewModel {

    private static final long UPDATE_RATE = 1;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private MutableLiveData<Set<MinimumComparableDevice>> mDevices;
    private MutableLiveData<Room> mRoom;
    private String roomId;

    public RoomViewModel() {
        mDevices = new MutableLiveData<>();
        mRoom = new MutableLiveData<>();
    }

    public void init(String roomId) {
        this.roomId = roomId;

        fetchDevices();
        fetchRoom();

        scheduleFetching();
    }

    public LiveData<Set<MinimumComparableDevice>> getDevices() {
        return mDevices;
    }

    public LiveData<Room> getRoom() { return mRoom; }

    private void fetchDevices() {
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Device>>> call, @NonNull Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Device>> result = response.body();
                    if (result != null) {
                        List<Device> comingDevicesList = result.getResult();
                        if (comingDevicesList != null) {
                            Set<MinimumComparableDevice> actualDevicesSet = comingDevicesList.stream().filter(d -> d != null && d.getRoom() != null && d.getRoom().getId() != null && d.getRoom().getId().equals(roomId)).sorted((a, b) -> a.getName().compareTo(b.getName())).map(MinimumComparableDevice::new).collect(Collectors.toCollection(LinkedHashSet::new));
                            Set<MinimumComparableDevice> devicesSet = mDevices.getValue();

                            if (devicesSet == null || !devicesSet.equals(actualDevicesSet)) {
                                mDevices.postValue(actualDevicesSet);
                                /*
                                for(Device d: actualDevicesSet) {
                                    Log.v("DEVICE_TYPE_DEVICE", d.toString());
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

    private void fetchRoom() {
        ApiClient.getInstance().getRoom(roomId, new Callback<Result<Room>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Room>> call, @NonNull Response<Result<Room>> response) {
                if (response.isSuccessful()) {
                    Result<Room> result = response.body();
                    if (result != null) {
                        Room comingRoom = result.getResult();
                        if (comingRoom != null) {
                            Room room = mRoom.getValue();
                            if (room == null || !room.equals(comingRoom)) {
                                mRoom.postValue(comingRoom);
                            }
                        }
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Room>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void scheduleFetching() {
        final Runnable fetcher = new Runnable() {
            public void run() {
                fetchDevices();
                fetchRoom();
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