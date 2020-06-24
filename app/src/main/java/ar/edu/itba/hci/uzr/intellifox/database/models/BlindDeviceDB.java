package ar.edu.itba.hci.uzr.intellifox.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class BlindDeviceDB {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "level")
    public int level;

    @ColumnInfo(name = "currentlevel")
    public int currentLevel;
}
