package ar.edu.itba.hci.uzr.intellifox.database.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.database.models.BlindDeviceDB;

public interface BlindDeviceDBDao {
    @Query("SELECT * FROM blinddevicedb")
    List<BlindDeviceDB> getAll();

    @Query("SELECT * FROM blinddevicedb WHERE id = :id LIMIT 1")
    BlindDeviceDB get(String id);

    @Query("SELECT * FROM blinddevicedb WHERE id IN (:ids)")
    List<BlindDeviceDB> loadAllByIds(String[] ids);

    @Insert
    void insertAll(BlindDeviceDB... users);

    @Delete
    void delete(BlindDeviceDB user);

    @Update
    void update(BlindDeviceDB device);
}
