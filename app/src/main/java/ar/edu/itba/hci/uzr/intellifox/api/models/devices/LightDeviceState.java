package ar.edu.itba.hci.uzr.intellifox.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class LightDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("brightness")
    @Expose
    private Integer brightness;

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public Integer getBrightness() { return brightness; }

    public void setBrightness(Integer brightness) { this.brightness = brightness; }
}
