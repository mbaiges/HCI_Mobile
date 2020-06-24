package ar.edu.itba.hci.uzr.intellifox.database.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OvenDeviceDB {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "temperature")
    public int temperature;

    @ColumnInfo(name = "mode")
    public String mode;

    @ColumnInfo(name = "heat")
    public String heat;

    @ColumnInfo(name = "grill")
    public String grill;

    @ColumnInfo(name = "convection")
    public String convection;

}
