package ar.edu.itba.hci.uzr.intellifox.ui.routine;

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
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineViewModel extends ViewModel {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private MutableLiveData<Routine> mRoutine;
    private String routineId;

    public RoutineViewModel() {
        mRoutine = new MutableLiveData<>();
    }

    public void init(String routineId) {
        this.routineId = routineId;
        fetchRoutine();
    }

    public LiveData<Routine> getRoutine() {
        return mRoutine;
    }

    private void fetchRoutine() {
        Routine routine = mRoutine.getValue();
        if (routine == null) {
            ApiClient.getInstance().getRoutine(routineId, new Callback<Result<Routine>>() {
                @Override
                public void onResponse(@NonNull Call<Result<Routine>> call, @NonNull Response<Result<Routine>> response) {
                    if (response.isSuccessful()) {
                        Result<Routine> result = response.body();
                        if (result != null) {
                            Routine routine = result.getResult();
                            if (routine != null) {
                                mRoutine.postValue(routine);
                            }
                        } else {
                            handleError(response);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Result<Routine>> call, @NonNull Throwable t) {
                    handleUnexpectedError(t);
                }
            });
        }
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        List<String> descList = error.getDescription();
        String desc = "";
        if (descList != null) {
            desc = descList.get(0);
        }
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