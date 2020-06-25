package ar.edu.itba.hci.uzr.intellifox.settings;

import com.google.android.gms.location.FusedLocationProviderClient;

public class FusedLocationClientSetting {
    private static FusedLocationProviderClient fusedLocationClient;

    private FusedLocationClientSetting() {}

    static public FusedLocationProviderClient getInstance() {
        return fusedLocationClient;
    }

    static public void setInstance(FusedLocationProviderClient newFusedLocationClient) {
        fusedLocationClient = newFusedLocationClient;
    }
}
