package ar.edu.itba.hci.uzr.intellifox.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.database.models.DoorDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.SpeakerDeviceDB;

@Dao
public interface SpeakerDeviceDBDao {
    @Query("SELECT * FROM speakerdevicedb")
    List<SpeakerDeviceDB> getAll();

    @Query("SELECT * FROM speakerdevicedb WHERE id = :id LIMIT 1")
    SpeakerDeviceDB get(String id);

    @Query("SELECT * FROM speakerdevicedb WHERE id IN (:ids)")
    List<SpeakerDeviceDB> loadAllByIds(String[] ids);

    @Insert
    void insertAll(SpeakerDeviceDB... devices);

    @Delete
    void delete(SpeakerDeviceDB device);

    @Update
    void update(SpeakerDeviceDB device);

    @Query("DELETE FROM speakerdevicedb")
    void deleteAllRows();
}
