package ar.edu.itba.hci.uzr.intellifox;

import android.os.AsyncTask;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.database.AppDatabase;

public class DatabaseTruncateTablesAsyncTask extends AsyncTask<Void, Void, Void> {

    static AppDatabase db;

    public DatabaseTruncateTablesAsyncTask() {
        if (db == null) {
            db = DatabaseGetter.getInstance();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        db.deleteAllRows();
        return null;
    }
}