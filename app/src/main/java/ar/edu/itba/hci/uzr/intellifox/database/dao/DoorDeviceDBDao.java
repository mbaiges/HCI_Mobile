package ar.edu.itba.hci.uzr.intellifox.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.database.models.DoorDeviceDB;

@Dao
public interface DoorDeviceDBDao {
    @Query("SELECT * FROM doordevicedb")
    List<DoorDeviceDB> getAll();

    @Query("SELECT * FROM doordevicedb WHERE id = :id LIMIT 1")
    DoorDeviceDB get(String id);

    @Query("SELECT * FROM doordevicedb WHERE id IN (:ids)")
    List<DoorDeviceDB> loadAllByIds(String[] ids);

    @Insert
    void insertAll(DoorDeviceDB... devices);

    @Delete
    void delete(DoorDeviceDB device);

    @Update
    void update(DoorDeviceDB device);
}
