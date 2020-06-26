package ar.edu.itba.hci.uzr.intellifox.api.models.commands;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DeviceCommand {

    static protected String actionName;
    protected String[] params;
    protected String deviceId;
    protected Object result;

    public DeviceCommand(String deviceId, String[] params) {
        this.deviceId = deviceId;
        this.params = params;
    }

    public void execute(Callback<Result<Object>> callback) {
        Callback<Object> saveResult = new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        }
        ApiClient.getInstance().executeDeviceAction(deviceId, actionName, params, callback);
    }

    public void undo(Callback<Result<Object>> callback) {
        return;
    }
}
