package ar.edu.itba.hci.uzr.intellifox.api.models.routine_action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RoutineAction {
    @SerializedName("device")
    @Expose
    private RoutineActionDevice device;
    @SerializedName("actionName")
    @Expose
    private String actionName;
    @SerializedName("params")
    @Expose
    private String[] params;
    @SerializedName("meta")
    @Expose
    private RoutineActionMeta meta;

    public RoutineActionDevice getDevice() {
        return device;
    }

    public void setDevice(RoutineActionDevice device) {
        this.device = device;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public RoutineActionMeta getMeta() {
        return meta;
    }

    public void setMeta(RoutineActionMeta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "RoutineAction{" +
                "device=" + device +
                ", actionName='" + actionName + '\'' +
                ", params=" + Arrays.toString(params) +
                ", meta=" + meta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutineAction that = (RoutineAction) o;
        return Objects.equals(device, that.device) &&
                Objects.equals(actionName, that.actionName) &&
                Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(device, actionName, params, meta);
    }
}
