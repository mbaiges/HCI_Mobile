package ar.edu.itba.hci.uzr.intellifox.ui.history;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.log.DeviceLogRecord;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryViewModel extends ViewModel {

    private final int OFFSET = 0;
    private final int LIMIT = 10;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> fetcherHandler;
    private MutableLiveData<List<DeviceLogRecord>> mLogs;

    public HistoryViewModel() {
        mLogs = new MutableLiveData<>();
        fetchDeviceLogs();
    }

    public LiveData<List<DeviceLogRecord>> getDeviceLogs() {
        return mLogs;
    }

    private void fetchDeviceLogs() {
        ApiClient.getInstance().getDevicesLogs(LIMIT, OFFSET, new Callback<Result<List<DeviceLogRecord>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<DeviceLogRecord>>> call, @NonNull Response<Result<List<DeviceLogRecord>>> response) {
                if (response.isSuccessful()) {
                    Result<List<DeviceLogRecord>> result = response.body();
                    if (result != null) {
                        List<DeviceLogRecord> deviceLogRecords = result.getResult();
                        if (deviceLogRecords != null) {
                            Log.d("LOGS", deviceLogRecords.toString());
                            mLogs.postValue(deviceLogRecords.stream().sorted(Comparator.comparing(DeviceLogRecord::getTimestamp).reversed()).collect(Collectors.toList()));
                        }
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<DeviceLogRecord>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
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