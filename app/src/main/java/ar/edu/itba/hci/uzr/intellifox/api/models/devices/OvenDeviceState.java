package ar.edu.itba.hci.uzr.intellifox.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class OvenDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("temperature")
    @Expose
    private String temperature;
    @SerializedName("heat")
    @Expose
    private String heat;
    @SerializedName("grill")
    @Expose
    private String grill;
    @SerializedName("convection")
    @Expose
    private String convection;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public String getGrill() {
        return grill;
    }

    public void setGrill(String grill) {
        this.grill = grill;
    }

    public String getConvection() {
        return convection;
    }

    public void setConvection(String convection) {
        this.convection = convection;
    }

}
