package ar.edu.itba.hci.uzr.intellifox.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class TapDeviceState extends DeviceState {

    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("dispensedQuantity")
    @Expose
    private Double dispensedQuantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getDispensedQuantity() {
        return dispensedQuantity;
    }

    public void setDispensedQuantity(Double dispensedQuantity) {
        this.dispensedQuantity = dispensedQuantity;
    }

    @Override
    public String toString() {
        return "TapDeviceState{" +
                "quantity='" + quantity + '\'' +
                ", unit='" + unit + '\'' +
                ", dispensedQuantity=" + dispensedQuantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TapDeviceState that = (TapDeviceState) o;
        return Objects.equals(quantity, that.quantity) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(dispensedQuantity, that.dispensedQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), quantity, unit, dispensedQuantity);
    }
}
