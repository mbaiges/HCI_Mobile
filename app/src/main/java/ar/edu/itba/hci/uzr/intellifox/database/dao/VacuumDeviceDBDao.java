package ar.edu.itba.hci.uzr.intellifox.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.database.models.TapDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.VacuumDeviceDB;

@Dao
public interface VacuumDeviceDBDao {
    @Query("SELECT * FROM vacuumdevicedb")
    List<VacuumDeviceDB> getAll();

    @Query("SELECT * FROM vacuumdevicedb WHERE id = :id LIMIT 1")
    VacuumDeviceDB get(String id);

    @Query("SELECT * FROM vacuumdevicedb WHERE id IN (:ids)")
    List<VacuumDeviceDB> loadAllByIds(String[] ids);

    @Insert
    void insertAll(TapDeviceDB... devices);

    @Delete
    void delete(TapDeviceDB device);

    @Update
    void update(TapDeviceDB device);
}
