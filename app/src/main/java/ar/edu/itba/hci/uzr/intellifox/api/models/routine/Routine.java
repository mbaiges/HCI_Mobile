package ar.edu.itba.hci.uzr.intellifox.api.models.routine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Objects;

import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineAction;

public class Routine {

    @SerializedName("id")
    @Expose(serialize = false)
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("meta")
    @Expose
    private RoutineMeta meta;
    @SerializedName("actions")
    @Expose
    private RoutineAction[] actions;

    public Routine(String name, RoutineMeta meta, RoutineAction[] actions) {
        this.name = name;
        this.meta = meta;
        this.actions = actions;
    }

    public Routine(String id, String name, RoutineMeta meta, RoutineAction[] actions) {
        this.id = id;
        this.name = name;
        this.meta = meta;
        this.actions = actions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoutineMeta getMeta() {
        return meta;
    }

    public void setMeta(RoutineMeta meta) {
        this.meta = meta;
    }

    public RoutineAction[] getActions() {
        return actions;
    }

    public void setActions(RoutineAction[] actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "Routine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", meta=" + meta +
                ", actions=" + Arrays.toString(actions) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Routine routine = (Routine) o;
        return Objects.equals(id, routine.id) &&
                Objects.equals(name, routine.name) &&
                Objects.equals(meta, routine.meta) &&
                Arrays.equals(actions, routine.actions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, meta);
        result = 31 * result + Arrays.hashCode(actions);
        return result;
    }
}
