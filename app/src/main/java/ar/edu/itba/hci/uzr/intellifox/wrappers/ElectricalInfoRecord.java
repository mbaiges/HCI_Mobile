package ar.edu.itba.hci.uzr.intellifox.wrappers;

import java.util.Objects;

public class ElectricalInfoRecord {
    String deviceId;
    String deviceName;
    String deviceTypeName;
    Double hours;
    Double wattagePerHour;

    public ElectricalInfoRecord(String deviceId, String deviceName, String deviceTypeName, Double hours, Double wattagePerHour) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceTypeName = deviceTypeName;
        this.hours = hours;
        this.wattagePerHour = wattagePerHour;
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

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Double getWattagePerHour() {
        return wattagePerHour;
    }

    public void setWattagePerHour(Double wattagePerHour) {
        this.wattagePerHour = wattagePerHour;
    }

    @Override
    public String toString() {
        return "ElectricalInfoRecord{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceTypeName='" + deviceTypeName + '\'' +
                ", hours=" + hours +
                ", wattagePerHour=" + wattagePerHour +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricalInfoRecord that = (ElectricalInfoRecord) o;
        return Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(deviceTypeName, that.deviceTypeName) &&
                Objects.equals(hours, that.hours) &&
                Objects.equals(wattagePerHour, that.wattagePerHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, deviceName, deviceTypeName, hours, wattagePerHour);
    }
}
