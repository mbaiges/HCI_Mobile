package ar.edu.itba.hci.uzr.intellifox.api.models.commands;

import android.util.Log;

import java.io.IOException;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DeviceCommand {

    static protected String actionName;
    protected String[] params;
    protected String deviceId;

    public DeviceCommand(String deviceId, String[] params) {
        this.deviceId = deviceId;
        this.params = params;
    }

    public void execute(Callback<Result<Object>> callback) {
        if (params == null) {
            params = new String[0];
        }

        for (int i = 0; i < params.length; i++) {
            Log.d("DEVICE_COMMAND", "Param nº" + i + ": " + params[i] );
        }
        Log.d("DEVICE_COMMAND", "Executing {" + actionName + "} over device id {" + deviceId + "} with params {" + params +"}");
        ApiClient.getInstance().executeDeviceAction(deviceId, actionName, params, callback);
    }

    public void undo(Callback<Result<Object>> callback) {
        return;
    }
}
