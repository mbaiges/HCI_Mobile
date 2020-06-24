package ar.edu.itba.hci.uzr.intellifox.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ar.edu.itba.hci.uzr.intellifox.database.dao.TapDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.models.TapDeviceDB;

@Database(entities = {TapDeviceDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TapDeviceDBDao userDao();
}