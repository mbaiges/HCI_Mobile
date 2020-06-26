package ar.edu.itba.hci.uzr.intellifox.settings;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.InetAddress;

import ar.edu.itba.hci.uzr.intellifox.R;

public class ConnectivityManagerSetting {

    private static WeakReference<Context> mycontext;
    private static ConnectivityManager cm;
    private static ConnectivityManagerSetting instance;

    private ConnectivityManagerSetting(){

    }

    public static ConnectivityManagerSetting getInstance(){
        if(cm == null && mycontext != null){
            Context auxContext = mycontext.get();
            if(auxContext != null){
                cm = (ConnectivityManager)auxContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                instance = new ConnectivityManagerSetting();
            }
        }
        return instance;
    }

    public static void setContext(Context context){
        mycontext = new WeakReference<>(context);
    }
    
    public boolean isNetworkConnected() {
        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            Log.v("CM", "alta coneccion con" + ipAddr.toString());
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public void noInternetError(View contextView){
        Context auxContext = mycontext.get();
        if(auxContext != null){
            Snackbar snackbar = Snackbar.make(contextView, contextView.getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.warning));
            snackbar.show();
        }
    }

    public void noConnectionError(View contextView){
        Context auxContext = mycontext.get();
        if(auxContext != null){
            Snackbar snackbar = Snackbar.make(contextView, contextView.getResources().getString(R.string.no_connection), Snackbar.LENGTH_SHORT);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.warning));
            snackbar.show();
        }
    }
}

