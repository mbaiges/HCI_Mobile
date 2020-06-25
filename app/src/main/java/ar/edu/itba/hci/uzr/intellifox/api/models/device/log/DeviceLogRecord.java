package ar.edu.itba.hci.uzr.intellifox.api.models.device.log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class DeviceLogRecord {
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("params")
    @Expose
    private String[] params;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceLogRecord logRecord = (DeviceLogRecord) o;
        return Objects.equals(timestamp, logRecord.timestamp) &&
                Objects.equals(deviceId, logRecord.deviceId) &&
                Objects.equals(action, logRecord.action) &&
                Arrays.equals(params, logRecord.params);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(timestamp, deviceId, action);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }
}
