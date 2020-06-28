package ar.edu.itba.hci.uzr.intellifox.api.models.device;

import java.util.Objects;

import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDeviceState;

public class MinimumComparableDevice {
    private Device device;

    private String deviceId;
    private String deviceName;
    private String status;
    private String lightColor;

    public MinimumComparableDevice(Device device) {
        this.device = device;
        this.deviceId = device.getId();
        this.deviceName = device.getName();
        if (this.device.getState() != null) {
            this.status = this.device.getState().getStatus();
        }
        if (this.device.getType() != null && this.device.getType().getName() != null && this.device.getType().getName().equals("lamp")) {
            this.lightColor = ((LightDeviceState) this.device.getState()).getColor();
        }
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
        this.deviceId = device.getId();
        this.deviceName = device.getName();
        if (this.device.getState() != null) {
            this.status = this.device.getState().getStatus();
        }
        if (this.device.getType() != null && this.device.getType().getName() != null && this.device.getType().getName().equals("lamp")) {
            this.lightColor = ((LightDeviceState) this.device.getState()).getColor();
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLightColor() {
        return lightColor;
    }

    public void setLightColor(String lightColor) {
        this.lightColor = lightColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinimumComparableDevice that = (MinimumComparableDevice) o;
        return  Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(status, that.status) &&
                Objects.equals(lightColor, that.lightColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, deviceName, status, lightColor);
    }
}
