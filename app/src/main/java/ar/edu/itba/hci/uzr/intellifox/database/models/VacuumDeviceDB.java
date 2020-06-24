package ar.edu.itba.hci.uzr.intellifox.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class VacuumDeviceDB {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "batteryLevel")
    public int batteryLevel;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "mode")
    public String mode;

}
