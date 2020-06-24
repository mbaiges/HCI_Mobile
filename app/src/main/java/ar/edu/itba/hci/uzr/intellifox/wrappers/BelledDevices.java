package ar.edu.itba.hci.uzr.intellifox.wrappers;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;

public class BelledDevices {
    @SerializedName("belledDevices")
    private HashSet<TypeAndDeviceId> belledDevices;

    public BelledDevices(HashSet<TypeAndDeviceId> belledDevices) {
        this.belledDevices = belledDevices;
    }

    public HashSet<TypeAndDeviceId> getBelledDevices() {
        return belledDevices;
    }

    public void setBelledDevices(HashSet<TypeAndDeviceId> belledDevices) {
        this.belledDevices = belledDevices;
    }
}
