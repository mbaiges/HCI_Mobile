package ar.edu.itba.hci.uzr.intellifox.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.database.models.ACDeviceDB;


@Dao
public interface ACDeviceDBDao {
    @Query("SELECT * FROM acdevicedb")
    List<ACDeviceDB> getAll();

    @Query("SELECT * FROM acdevicedb WHERE id = :id LIMIT 1")
    ACDeviceDB get(String id);

    @Query("SELECT * FROM acdevicedb WHERE id IN (:ids)")
    List<ACDeviceDB> loadAllByIds(String[] ids);

    @Insert
    void insertAll(ACDeviceDB... devices);

    @Delete
    void delete(ACDeviceDB device);

    @Update
    void update(ACDeviceDB device);

    @Query("DELETE FROM acdevicedb")
    void deleteAllRows();
}
