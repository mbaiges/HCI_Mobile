package ar.edu.itba.hci.uzr.intellifox.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.database.models.TapDeviceDB;

@Dao
public interface TapDeviceDBDao {
    @Query("SELECT * FROM tapdevicedb")
    List<TapDeviceDB> getAll();

    @Query("SELECT * FROM tapdevicedb WHERE id IN (:ids)")
    List<TapDeviceDB> loadAllByIds(String[] ids);

    @Insert
    void insertAll(TapDeviceDB... users);

    @Delete
    void delete(TapDeviceDB user);
}
