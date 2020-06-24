package ar.edu.itba.hci.uzr.intellifox.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ar.edu.itba.hci.uzr.intellifox.database.dao.BlindDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.dao.TapDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.models.BlindDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.SpeakerDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.TapDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.VacuumDeviceDB;

@Database(entities = {TapDeviceDB.class, BlindDeviceDB.class, SpeakerDeviceDB.class, VacuumDeviceDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TapDeviceDBDao userDao();

    public void addDeviceToDatabase(String typeName, String deviceId) {
        
    }
}