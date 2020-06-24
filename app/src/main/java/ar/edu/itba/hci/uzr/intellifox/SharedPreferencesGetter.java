package ar.edu.itba.hci.uzr.intellifox;

import android.content.SharedPreferences;

public class SharedPreferencesGetter {
    private static SharedPreferences instance;

    private SharedPreferencesGetter() {}

    static public SharedPreferences getInstance() {
        return instance;
    }

    static public void setInstance(SharedPreferences newInstance) {
        instance = newInstance;
    }
}
