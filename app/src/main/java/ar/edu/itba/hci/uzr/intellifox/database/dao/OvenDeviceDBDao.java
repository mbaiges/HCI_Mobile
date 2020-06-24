package ar.edu.itba.hci.uzr.intellifox.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


import ar.edu.itba.hci.uzr.intellifox.database.models.ACDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.OvenDeviceDB;

@Dao
public interface OvenDeviceDBDao {
    @Query("SELECT * FROM ovendevicedb")
    List<OvenDeviceDB> getAll();

    @Query("SELECT * FROM ovendevicedb WHERE id = :id LIMIT 1")
    OvenDeviceDB get(String id);

    @Query("SELECT * FROM ovendevicedb WHERE id IN (:ids)")
    List<OvenDeviceDB> loadAllByIds(String[] ids);

    @Insert
    void insertAll(OvenDeviceDB... devices);

    @Delete
    void delete(OvenDeviceDB device);

    @Update
    void update(OvenDeviceDB device);
}
