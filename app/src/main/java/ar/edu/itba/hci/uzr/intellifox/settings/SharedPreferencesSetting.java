package ar.edu.itba.hci.uzr.intellifox.settings;

import android.content.SharedPreferences;

public class SharedPreferencesSetting {
    private static SharedPreferences instance;

    private SharedPreferencesSetting() {}

    static public SharedPreferences getInstance() {
        return instance;
    }

    static public void setInstance(SharedPreferences newInstance) {
        instance = newInstance;
    }
}
