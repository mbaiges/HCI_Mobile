package ar.edu.itba.hci.uzr.intellifox.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class BlindDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("currentLevel")
    @Expose
    private Integer currentLevel;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlindDeviceState that = (BlindDeviceState) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(level, that.level) &&
                Objects.equals(currentLevel, that.currentLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, level, currentLevel);
    }
}
