package ar.edu.itba.hci.uzr.intellifox.settings;

import ar.edu.itba.hci.uzr.intellifox.database.AppDatabase;

public class DatabaseSetting {
    private static AppDatabase db;

    private DatabaseSetting() {}

    static public AppDatabase getInstance() {
        return db;
    }

    static public void setInstance(AppDatabase newDb) {
        db = newDb;
    }
}
