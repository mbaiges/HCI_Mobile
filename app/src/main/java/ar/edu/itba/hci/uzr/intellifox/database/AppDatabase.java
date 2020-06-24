package ar.edu.itba.hci.uzr.intellifox.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.database.dao.TapDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.models.ACDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.LightDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.OvenDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.dao.BlindDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.dao.TapDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.models.BlindDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.SpeakerDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.TapDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.VacuumDeviceDB;

@Database(entities = {TapDeviceDB.class, ACDeviceDB.class, LightDeviceDB.class, OvenDeviceDB.class, BlindDeviceDB.class, SpeakerDeviceDB.class, VacuumDeviceDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TapDeviceDBDao tapDeviceDBDao();

    public void addDevice(String typeName, Device device) {
        if (typeName.equals("faucet")) {
            tapDeviceDBDao().insertAll(convertTapToDBModel(device));
        }
    }

    public void removeDevice(String typeName, Device device) {
        if (typeName.equals("faucet")) {
            tapDeviceDBDao().delete(convertTapToDBModel(device));
        }
    }

    public Device getDevice(String typeName, String deviceId) {
        if (typeName.equals("faucet")) {
            return convertDBModelToTap(tapDeviceDBDao().get(deviceId));
        }
        return null;
    }

    private TapDeviceDB convertTapToDBModel(Device device) {
        TapDevice d = (TapDevice) device;
        String id = null, name = null, status = null;
        if (d != null) {
            id = d.getId();
            name = d.getName();
            TapDeviceState state = d.getState();
            if (state != null) {
                status = state.getStatus();
            }
        }
        TapDeviceDB model = new TapDeviceDB();
        model.id = id;
        model.name = name;
        model.status = status;
        return model;
    }

    private TapDevice convertDBModelToTap(TapDeviceDB dbDevice) {
        TapDevice d = new TapDevice();
        d.setId(dbDevice.id);
        d.setName(dbDevice.name);
        TapDeviceState state = new TapDeviceState();
        state.setStatus(dbDevice.status);
        return d;
    }
}
