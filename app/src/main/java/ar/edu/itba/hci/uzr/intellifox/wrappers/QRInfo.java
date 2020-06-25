package ar.edu.itba.hci.uzr.intellifox.wrappers;

import com.google.gson.annotations.SerializedName;

public class QRInfo {

    // type can be "device", "routine" or "room".
    @SerializedName("type")
    private String type;

    // if type is "device", then typeName is the name of DeviceType.
    @SerializedName("typeName")
    private String typeName;

    @SerializedName("id")
    private String id;

    // if type is "routine" and execute is true, then it shows and execute.
    @SerializedName("executable")
    private Boolean executable;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isExecutable() {
        return executable;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }

    @Override
    public String toString() {
        return "QRInfo{" +
                "type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                ", id='" + id + '\'' +
                ", execute=" + executable +
                '}';
    }
}
