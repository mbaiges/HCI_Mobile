package ar.edu.itba.hci.uzr.intellifox;

import ar.edu.itba.hci.uzr.intellifox.database.AppDatabase;

public class DatabaseGetter {
    private static AppDatabase db;

    private DatabaseGetter() {}

    static public AppDatabase getInstance() {
        return db;
    }

    static public void setInstance(AppDatabase newDb) {
        db = newDb;
    }
}
