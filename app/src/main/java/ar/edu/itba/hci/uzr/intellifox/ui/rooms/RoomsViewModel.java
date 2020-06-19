package ar.edu.itba.hci.uzr.intellifox.ui.rooms;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.models.Error;
import ar.edu.itba.hci.uzr.intellifox.api.models.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomsViewModel extends ViewModel {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private MutableLiveData<List<Room>> mRooms;

    public RoomsViewModel() {
        mRooms = new MutableLiveData<>();
        fetchRooms();
        scheduleFetching();
    }

    public LiveData<List<Room>> getRooms() {
        return mRooms;
    }

    private void fetchRooms() {
        ApiClient.getInstance().getRooms(new Callback<Result<List<Room>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Room>>> call, @NonNull Response<Result<List<Room>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Room>> result = response.body();
                    if (result != null) {
                        List<Room> aux = mRooms.getValue();

                        if (aux == null || !(aux.equals(result.getResult()))) {
                            mRooms.postValue(result.getResult());
                            aux = result.getResult();
                            if (aux != null) {
                                for (Room r : aux) {
                                    Log.d("ROOM:", r.getId() + "+" + r.getName() + "+" + r.getMeta().getDesc() + "+" + r.getMeta().getIcon());
                                }
                            }
                        }

                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Room>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void scheduleFetching() {
        final Runnable fetcher = new Runnable() {
            public void run() {
                fetchRooms();
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