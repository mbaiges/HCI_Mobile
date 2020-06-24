package ar.edu.itba.hci.uzr.intellifox;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.database.AppDatabase;
import ar.edu.itba.hci.uzr.intellifox.wrappers.TypeAndDeviceId;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabaseReloadTablesAsyncTask extends AsyncTask<Void, Void, Void> {

    static AppDatabase db;
    private HashSet<TypeAndDeviceId> tadis;

    public DatabaseReloadTablesAsyncTask(HashSet<TypeAndDeviceId> tadis) {
        if (db == null) {
            db = DatabaseGetter.getInstance();
        }
        this.tadis = tadis;
    }

    @Override
    protected Void doInBackground(Void... params) {
        db.deleteAllRows();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        for (TypeAndDeviceId tadi : tadis) {
            String deviceId = tadi.getDeviceId();
            String typeName = tadi.getTypeName();
            if (deviceId != null && typeName != null) {
                ApiClient.getInstance().getDevice(deviceId, new Callback<Result<Device>>() {
                    @Override
                    public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                        if (response.isSuccessful()) {
                            Result<Device> result = response.body();
                            if (result != null) {
                                Device device = result.getResult();
                                if (device != null) {
                                    Log.d("DEVICE_BELLED", result.getResult().toString());
                                    DatabaseAddDeviceAsyncTask task = new DatabaseAddDeviceAsyncTask(typeName, device);
                                    task.execute();
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
        }
    }

    protected <T> void handleError(Response<T> response) {
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

    protected void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }
}