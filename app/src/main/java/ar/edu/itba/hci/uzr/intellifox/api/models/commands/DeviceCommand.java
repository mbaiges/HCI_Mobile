package ar.edu.itba.hci.uzr.intellifox.api.models.commands;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import retrofit2.Callback;

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
