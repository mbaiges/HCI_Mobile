package ar.edu.itba.hci.uzr.intellifox.api.models.routine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;

public class RoutineAction {
    @SerializedName("device")
    @Expose
    private RoutineActionDevice device;
    @SerializedName("actionName")
    @Expose
    private String actionName;
    @SerializedName("params")
    @Expose
    private List<String> params;
    @SerializedName("meta")
    @Expose
    private RoutineActionMeta meta;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutineAction that = (RoutineAction) o;
        return Objects.equals(device, that.device) &&
                Objects.equals(actionName, that.actionName) &&
                Objects.equals(params, that.params) &&
                Objects.equals(meta, that.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(device, actionName, params, meta);
    }
}
