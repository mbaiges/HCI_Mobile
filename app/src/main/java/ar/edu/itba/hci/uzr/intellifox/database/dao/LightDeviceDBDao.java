package ar.edu.itba.hci.uzr.intellifox.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.database.models.ACDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.LightDeviceDB;


@Dao
public interface LightDeviceDBDao {
    @Query("SELECT * FROM lightdevicedb")
    List<LightDeviceDB> getAll();

    @Query("SELECT * FROM lightdevicedb WHERE id = :id LIMIT 1")
    LightDeviceDB get(String id);

    @Query("SELECT * FROM lightdevicedb WHERE id IN (:ids)")
    List<LightDeviceDB> loadAllByIds(String[] ids);

    @Insert
    void insertAll(LightDeviceDB... devices);

    @Delete
    void delete(LightDeviceDB device);

    @Update
    void update(LightDeviceDB device);
}
