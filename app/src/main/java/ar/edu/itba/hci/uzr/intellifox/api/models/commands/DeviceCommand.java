package ar.edu.itba.hci.uzr.intellifox.api.models.commands;

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
        ApiClient.getInstance().executeDeviceAction(deviceId, actionName, params, callback);
    }

    public void undo(Callback<Result<Object>> callback) {
        return;
    }
}
