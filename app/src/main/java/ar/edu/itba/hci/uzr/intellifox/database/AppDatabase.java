package ar.edu.itba.hci.uzr.intellifox.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDeviceState;
import ar.edu.itba.hci.uzr.intellifox.database.dao.ACDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.dao.BlindDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.dao.DoorDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.dao.LightDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.dao.OvenDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.dao.SpeakerDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.dao.TapDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.dao.VacuumDeviceDBDao;
import ar.edu.itba.hci.uzr.intellifox.database.models.ACDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.BlindDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.DoorDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.LightDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.OvenDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.SpeakerDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.TapDeviceDB;
import ar.edu.itba.hci.uzr.intellifox.database.models.VacuumDeviceDB;


@Database(entities = {TapDeviceDB.class, ACDeviceDB.class, LightDeviceDB.class, OvenDeviceDB.class, SpeakerDeviceDB.class, BlindDeviceDB.class, VacuumDeviceDB.class, DoorDeviceDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TapDeviceDBDao tapDeviceDBDao();
    public abstract ACDeviceDBDao acDeviceDBDao();
    public abstract LightDeviceDBDao lightDeviceDBDao();
    public abstract OvenDeviceDBDao ovenDeviceDBDao();
    public abstract SpeakerDeviceDBDao speakerDeviceDBDao();
    public abstract BlindDeviceDBDao blindDeviceDBDao();
    public abstract VacuumDeviceDBDao vacuumDeviceDBDao();
    public abstract DoorDeviceDBDao doorDeviceDBDao();

    public void addDevice(String typeName, Device device) {
        if (typeName.equals("faucet")) {
            tapDeviceDBDao().insertAll(convertTapToDBModel(device));
        }
        else if (typeName.equals("ac")) {
            acDeviceDBDao().insertAll(convertAcToDBModel(device));
        }
        else if (typeName.equals("lamp")) {
            lightDeviceDBDao().insertAll(convertLightToDBModel(device));
        }
        else if (typeName.equals("oven")) {
            ovenDeviceDBDao().insertAll(convertOvenToDBModel(device));
        }
        else if (typeName.equals("speaker")) {
            speakerDeviceDBDao().insertAll(convertSpeakerToDBModel(device));
        }
        else if (typeName.equals("blinds")) {
            blindDeviceDBDao().insertAll(convertBlindToDBModel(device));
        }
        else if (typeName.equals("vacuum")) {
            vacuumDeviceDBDao().insertAll(convertVacuumToDBModel(device));
        }
        else if (typeName.equals("door")) {
            doorDeviceDBDao().insertAll(convertDoorToDBModel(device));
        }
    }

    public void updateDevice(String typeName, Device device) {
        if (typeName.equals("faucet")) {
            tapDeviceDBDao().update(convertTapToDBModel(device));
        }
        else if (typeName.equals("ac")) {
            acDeviceDBDao().update(convertAcToDBModel(device));
        }
        else if (typeName.equals("lamp")) {
            lightDeviceDBDao().update(convertLightToDBModel(device));
        }
        else if (typeName.equals("oven")) {
            ovenDeviceDBDao().update(convertOvenToDBModel(device));
        }
        else if (typeName.equals("speaker")) {
            speakerDeviceDBDao().update(convertSpeakerToDBModel(device));
        }
        else if (typeName.equals("blinds")) {
            blindDeviceDBDao().update(convertBlindToDBModel(device));
        }
        else if (typeName.equals("vacuum")) {
            vacuumDeviceDBDao().update(convertVacuumToDBModel(device));
        }
        else if (typeName.equals("door")) {
            doorDeviceDBDao().update(convertDoorToDBModel(device));
        }
    }

    public void removeDevice(String typeName, Device device) {
        if (typeName.equals("faucet")) {
            tapDeviceDBDao().delete(convertTapToDBModel(device));
        }
        else if (typeName.equals("ac")) {
            acDeviceDBDao().delete(convertAcToDBModel(device));
        }
        else if (typeName.equals("lamp")) {
            lightDeviceDBDao().delete(convertLightToDBModel(device));
        }
        else if (typeName.equals("oven")) {
            ovenDeviceDBDao().delete(convertOvenToDBModel(device));
        }
        else if (typeName.equals("speaker")) {
            speakerDeviceDBDao().delete(convertSpeakerToDBModel(device));
        }
        else if (typeName.equals("blinds")) {
            blindDeviceDBDao().delete(convertBlindToDBModel(device));
        }
        else if (typeName.equals("vacuum")) {
            vacuumDeviceDBDao().delete(convertVacuumToDBModel(device));
        }
        else if (typeName.equals("door")) {
            doorDeviceDBDao().delete(convertDoorToDBModel(device));
        }
    }

    public Device getDevice(String typeName, String deviceId) {
        if (typeName.equals("faucet")) {
            return convertDBModelToTap(tapDeviceDBDao().get(deviceId));
        }
        else if (typeName.equals("ac")) {
            return convertDBModelToAc(acDeviceDBDao().get(deviceId));
        }
        else if (typeName.equals("lamp")) {
            return convertDBModelToLight(lightDeviceDBDao().get(deviceId));
        }
        else if (typeName.equals("oven")) {
            return convertDBModelToOven(ovenDeviceDBDao().get(deviceId));
        }
        else if (typeName.equals("speaker")) {
            return convertDBModelToSpeaker(speakerDeviceDBDao().get(deviceId));
        }
        else if (typeName.equals("blinds")) {
            return convertDBModelToBlind(blindDeviceDBDao().get(deviceId));
        }
        else if (typeName.equals("vacuum")) {
            return convertDBModelToVacuum(vacuumDeviceDBDao().get(deviceId));
        }
        else if (typeName.equals("door")) {
            return convertDBModelToDoor(doorDeviceDBDao().get(deviceId));
        }
        return null;
    }

    public void deleteAllRows() {
        tapDeviceDBDao().deleteAllRows();
        acDeviceDBDao().deleteAllRows();
        lightDeviceDBDao().deleteAllRows();
        ovenDeviceDBDao().deleteAllRows();
        speakerDeviceDBDao().deleteAllRows();
        blindDeviceDBDao().deleteAllRows();
        vacuumDeviceDBDao().deleteAllRows();
        doorDeviceDBDao().deleteAllRows();
    }

    // ------ Device to DB Model Converters

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

    private DoorDeviceDB convertDoorToDBModel(Device device) {
        DoorDevice d = (DoorDevice) device;
        String id = null, name = null, status = null;
        if (d != null) {
            id = d.getId();
            name = d.getName();
            DoorDeviceState state = d.getState();
            if (state != null) {
                status = state.getStatus();
            }
        }
        DoorDeviceDB model = new DoorDeviceDB();
        model.id = id;
        model.name = name;
        model.status = status;
        return model;
    }

    private VacuumDeviceDB convertVacuumToDBModel(Device device) {
        VacuumDevice d = (VacuumDevice) device;
        String id = null, name = null, status = null;
        if (d != null) {
            id = d.getId();
            name = d.getName();
            VacuumDeviceState state = d.getState();
            if (state != null) {
                status = state.getStatus();
            }
        }
        VacuumDeviceDB model = new VacuumDeviceDB();
        model.id = id;
        model.name = name;
        model.status = status;
        return model;
    }

    private BlindDeviceDB convertBlindToDBModel(Device device) {
        BlindDevice d = (BlindDevice) device;
        String id = null, name = null, status = null;
        if (d != null) {
            id = d.getId();
            name = d.getName();
            BlindDeviceState state = d.getState();
            if (state != null) {
                status = state.getStatus();
            }
        }
        BlindDeviceDB model = new BlindDeviceDB();
        model.id = id;
        model.name = name;
        model.status = status;
        return model;
    }

    private SpeakerDeviceDB convertSpeakerToDBModel(Device device) {
        SpeakerDevice d = (SpeakerDevice) device;
        String id = null, name = null, status = null;
        if (d != null) {
            id = d.getId();
            name = d.getName();
            SpeakerDeviceState state = d.getState();
            if (state != null) {
                status = state.getStatus();
            }
        }
        SpeakerDeviceDB model = new SpeakerDeviceDB();
        model.id = id;
        model.name = name;
        model.status = status;
        return model;
    }

    private OvenDeviceDB convertOvenToDBModel(Device device) {
        OvenDevice d = (OvenDevice) device;
        String id = null, name = null, status = null;
        if (d != null) {
            id = d.getId();
            name = d.getName();
            OvenDeviceState state = d.getState();
            if (state != null) {
                status = state.getStatus();
            }
        }
        OvenDeviceDB model = new OvenDeviceDB();
        model.id = id;
        model.name = name;
        model.status = status;
        return model;
    }

    private LightDeviceDB convertLightToDBModel(Device device) {
        LightDevice d = (LightDevice) device;
        String id = null, name = null, status = null;
        if (d != null) {
            id = d.getId();
            name = d.getName();
            LightDeviceState state = d.getState();
            if (state != null) {
                status = state.getStatus();
            }
        }
        LightDeviceDB model = new LightDeviceDB();
        model.id = id;
        model.name = name;
        model.status = status;
        return model;
    }

    private ACDeviceDB convertAcToDBModel(Device device) {
        AcDevice d = (AcDevice) device;
        String id = null, name = null, status = null;
        if (d != null) {
            id = d.getId();
            name = d.getName();
            AcDeviceState state = d.getState();
            if (state != null) {
                status = state.getStatus();
            }
        }
        ACDeviceDB model = new ACDeviceDB();
        model.id = id;
        model.name = name;
        model.status = status;
        return model;
    }

    // ---- DB Model to Device Converters

    private TapDevice convertDBModelToTap(TapDeviceDB dbDevice) {
        TapDevice d = new TapDevice();
        d.setId(dbDevice.id);
        d.setName(dbDevice.name);
        TapDeviceState state = new TapDeviceState();
        state.setStatus(dbDevice.status);
        return d;
    }

    private Device convertDBModelToDoor(DoorDeviceDB dbDevice) {
        DoorDevice d = new DoorDevice();
        d.setId(dbDevice.id);
        d.setName(dbDevice.name);
        DoorDeviceState state = new DoorDeviceState();
        state.setStatus(dbDevice.status);
        return d;
    }

    private Device convertDBModelToVacuum(VacuumDeviceDB dbDevice) {
        VacuumDevice d = new VacuumDevice();
        d.setId(dbDevice.id);
        d.setName(dbDevice.name);
        VacuumDeviceState state = new VacuumDeviceState();
        state.setStatus(dbDevice.status);
        return d;
    }

    private Device convertDBModelToBlind(BlindDeviceDB dbDevice) {
        BlindDevice d = new BlindDevice();
        d.setId(dbDevice.id);
        d.setName(dbDevice.name);
        BlindDeviceState state = new BlindDeviceState();
        state.setStatus(dbDevice.status);
        return d;
    }

    private Device convertDBModelToSpeaker(SpeakerDeviceDB dbDevice) {
        SpeakerDevice d = new SpeakerDevice();
        d.setId(dbDevice.id);
        d.setName(dbDevice.name);
        SpeakerDeviceState state = new SpeakerDeviceState();
        state.setStatus(dbDevice.status);
        return d;
    }

    private Device convertDBModelToOven(OvenDeviceDB dbDevice) {
        OvenDevice d = new OvenDevice();
        d.setId(dbDevice.id);
        d.setName(dbDevice.name);
        OvenDeviceState state = new OvenDeviceState();
        state.setStatus(dbDevice.status);
        return d;
    }

    private Device convertDBModelToAc(ACDeviceDB dbDevice) {
        AcDevice d = new AcDevice();
        d.setId(dbDevice.id);
        d.setName(dbDevice.name);
        AcDeviceState state = new AcDeviceState();
        state.setStatus(dbDevice.status);
        return d;
    }

    private Device convertDBModelToLight(LightDeviceDB dbDevice) {
        LightDevice d = new LightDevice();
        d.setId(dbDevice.id);
        d.setName(dbDevice.name);
        LightDeviceState state = new LightDeviceState();
        state.setStatus(dbDevice.status);
        return d;
    }
}