package ar.edu.itba.hci.uzr.intellifox;

import com.google.android.gms.location.FusedLocationProviderClient;

public class FusedLocationClientGetter {
    private static FusedLocationProviderClient fusedLocationClient;

    private FusedLocationClientGetter() {}

    static public FusedLocationProviderClient getInstance() {
        return fusedLocationClient;
    }

    static public void setInstance(FusedLocationProviderClient newFusedLocationClient) {
        fusedLocationClient = newFusedLocationClient;
    }
}
