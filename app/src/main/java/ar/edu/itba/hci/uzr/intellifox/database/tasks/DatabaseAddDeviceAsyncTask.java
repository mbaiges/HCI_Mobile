package ar.edu.itba.hci.uzr.intellifox.database.tasks;

import android.os.AsyncTask;

import ar.edu.itba.hci.uzr.intellifox.settings.DatabaseSetting;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.database.AppDatabase;

public class DatabaseAddDeviceAsyncTask extends AsyncTask<Void, Void, Void> {

    static AppDatabase db;

    private String typeName;
    private Device device;

    public DatabaseAddDeviceAsyncTask(String typeName, Device device) {
        if (db == null) {
            db = DatabaseSetting.getInstance();
        }

        this.typeName = typeName;
        this.device = device;
    }

    @Override
    protected Void doInBackground(Void... params) {
        db.addDevice(typeName, device);
        return null;
    }
}
