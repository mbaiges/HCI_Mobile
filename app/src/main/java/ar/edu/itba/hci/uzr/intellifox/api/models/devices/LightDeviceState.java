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
    private String brightness;

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public String getBrightness() { return brightness; }

    public void setBrightness(String brightness) { this.brightness = brightness; }
}
